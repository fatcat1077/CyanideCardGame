package com.net.Server;

import java.io.*;
import java.net.Socket;
import java.util.List;

import com.net.Room.WaitRoom;
import com.net.Server.Controller.*;
import com.net.protocol.packets.*;
import com.players.Player;

public class PacketHandler implements Runnable{
    //net
    private Socket clientSkt;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    
    // shared info
    private List<ObjectOutputStream> clients;
    private WaitRoom waitRoom;
    
    // individual info
    private Player player;
    private int pid;

    //Controller
    private MessageController msgController;
    private WaitRoomController waitRoomController;




    PacketHandler(Socket clientSocket, List<ObjectOutputStream> clients, WaitRoom waitRoom, int playerId) throws IOException{
        this.clientSkt = clientSocket;
        this.clients = clients;
        this.waitRoom = waitRoom;
        this.out = new ObjectOutputStream(clientSocket.getOutputStream());
        this.in = new ObjectInputStream(clientSocket.getInputStream());
        this.msgController = new MessageController(out, clients);
        this.waitRoomController = new WaitRoomController(this.clients, this.waitRoom);
        this.pid = playerId;

        clients.add(out);
        sendInitPacket();
    }

    @Override
    public void run(){
        try {
            while (true) {
                Object revObject = in.readObject();
                
                //identify which packet it is
                if(revObject instanceof Init){
                    handleInitPacket(revObject);
                }else if(revObject instanceof Message){
                    msgController.handle(revObject);
                }else if(revObject instanceof WaitRoomState){
                    waitRoomController.handle(revObject);
                }

            }
        } catch (Exception e) {
            System.out.println(String.format("Client (%d) disconnected: ", pid) + clientSkt);
        } finally {
                clients.remove(out);
                waitRoom.removePlayer(player);
            try {
                clientSkt.close();
            } catch (IOException e) {
                System.out.println("socket closing error (by server)");
            }
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
        
        //Update everyone's roomState
        WaitRoomState roomStatePacket = new WaitRoomState(this.waitRoom);
        waitRoomController.updateToAll(roomStatePacket);

    }

}
