package com.net.Server;

import java.io.*;
import java.net.Socket;
import java.util.List;

import com.net.Room.WaitRoom;
import com.net.Server.Controller.*;
import com.net.Server.interfaces.ServerManager;
import com.net.protocol.enums.PacketType;
import com.net.protocol.packets.*;
import com.players.Player;

public class ServerPacketHandler implements Runnable{
    //net
    private Socket clientSkt;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private ServerManager serverManager;

    // shared info
    private List<ObjectOutputStream> clients;
    private WaitRoom waitRoom;
    
    // individual info
    private Player player;
    private int pid;
    public boolean isHost = false;
    public boolean isDisconnect = false;

    //Controller
    private MessageController msgController;
    private WaitRoomController waitRoomController;




    ServerPacketHandler(Socket clientSocket, List<ObjectOutputStream> clients, WaitRoom waitRoom, int playerId, ServerManager serverManager) throws IOException{
        this.clientSkt = clientSocket;
        this.clients = clients;
        this.waitRoom = waitRoom;
        this.out = new ObjectOutputStream(clientSocket.getOutputStream());
        this.in = new ObjectInputStream(clientSocket.getInputStream());
        this.msgController = new MessageController(out, clients);
        this.waitRoomController = new WaitRoomController(this.clients, this.waitRoom);
        this.pid = playerId;
        this.serverManager = serverManager;

        clients.add(out);
        sendInitPacket();
    }

    @Override
    public void run(){
        try {
            while (true) {
                Object revObject = in.readObject();
                
                //identify which packet it is
                if(revObject instanceof Packet){
                    PacketType type = ((Packet) revObject).getType();
                    switch (type) {
                        case Init:
                            handleInitPacket(revObject);
                            break;
                        case Message:
                            msgController.handle(revObject);
                            break;
                        case WaitRoomState:
                            waitRoomController.handle(revObject);
                            break;
                        case Disconnect:
                            handleDisconnect();
                            return;
                        case StartGame:
                            //todo
                            System.out.println("Start Game");

                        default:
                            System.out.println("Unknown packet type");
                            break;
                    } 
                }
            }
        } catch (Exception e) {
            System.out.println(String.format("Client (%d) disconnected: ", pid) + clientSkt);
        } finally {
            handleDisconnect();
        }
    }

    public void stop(){
        try{
            clientSkt.close();
        }catch(IOException e){
            System.out.println(String.format("Closing %s (%d) socket error", player.getName(), player.getPID()));
        }
    }

    private void sendInitPacket(){
        try{
            //init setup
            this.out.writeObject(new Init(this.pid));
            this.out.flush();
        }catch(IOException e){
            System.out.println("Sending Init Packet error");
        }

    }
    private void handleInitPacket(Object obj){
        Init info = (Init) obj;
        this.player = new Player(info.getName());
        this.player.setPID(pid);
        this.waitRoom.addPlayer(player);
        if(this.waitRoom.getHost().getPID() == this.pid){
            this.isHost = true;
        }
        
        //Update everyone's roomState
        WaitRoomState roomStatePacket = new WaitRoomState(this.waitRoom);
        waitRoomController.updateToAll(roomStatePacket);
    }

    private void handleDisconnect(){
        //avoid repeated excution
        if(isDisconnect) return;
        else isDisconnect = true;

        System.out.println(String.format("%s (%d) is disconnected", player.getName(), player.getPID()));
        
        clients.remove(out);
        waitRoom.removePlayer(player);
        // it is host
        if(isHost){
            hostDisconnect();
            System.out.println("Server host is disconnted !!");
            return;
        }


        Disconnect disconnectPkt = new Disconnect(waitRoom);

        // broacast to all clients
        broacastPkt(disconnectPkt);

        // close the disconnected client socket
        try {
            if(clientSkt != null && !clientSkt.isClosed()){
                clientSkt.close();
            }
        } catch (IOException e) {
            System.out.println("socket closing error (by server)");
        }
    }
    
    private void hostDisconnect(){
        HostDisconnect hostDisconnect = new HostDisconnect();
        broacastPkt(hostDisconnect);
        serverManager.shutdownServer();
    }

    private void broacastPkt(Object obj){
        for (ObjectOutputStream clientOut : clients) {
            try {
                clientOut.writeObject(obj);
                clientOut.flush();
                clientOut.reset();
            } catch (IOException e) {
                System.out.println("Failed to send disconnectPkt to a client.");
            }
        }
    }

}
