package com.net.Client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.net.Client.Controller.*;
import com.net.protocol.enums.PacketType;
import com.net.protocol.packets.*;
import com.players.Player;



public class ClientPacketHandler{
    //net
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;


    //individual info
    private Player player;


    //controller
    private MessageController msgController;
    private WaitRoomController waitRoomController;

    ClientPacketHandler(Socket socket, Player player) throws IOException{
        this.socket = socket;
        this.player = player;
        this.out = new ObjectOutputStream(this.socket.getOutputStream());
        this.in = new ObjectInputStream(this.socket.getInputStream());
    }

    public void start(){
        try {
            // // when client disconnect (program finished), send a Disconnect packet 
            // Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            //     disconnectGracefully(); // 程式結束時自動發送 Disconnect 封包
            // }));

            while (true) {
                Object revObject = in.readObject();
                
                if(revObject instanceof Packet){
                    PacketType type = ((Packet) revObject).getType();
                    switch (type) {
                        case Init:
                            init(revObject);
                            break;
                        case Message:
                            msgController.handle(revObject);
                            break;
                        case WaitRoomState:
                            waitRoomController.handle(revObject);
                            break;
                        case Disconnect:
                            otherDisconnect(revObject);
                            break;
                        case HostDisconnect:
                            System.out.println("Host is disconnected.");
                            return;
                        default:
                            System.out.println("Unknown packet type: " + revObject.getClass());
                            break;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Disconnected from server.");
        } finally {
            disconnect();
        }
    }

    private void init(Object obj) throws IOException{
        Init init = (Init) obj;
        this.player.setPID(init.getPID());

        //return Init packet to server
        init.setName(this.player.getName());

        out.writeObject(init);
        out.flush();

        // open waitRoom

        this.waitRoomController = new WaitRoomController(player.getPID(), out, this.player);

        // open chat
        this.msgController = new MessageController(this.out, this.player, this.waitRoomController);

        new Thread(this.msgController).start();
    }

    private void disconnect(){
        msgController.stop();
        try {
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            System.out.println("socket closing error (by client)");
        }
    }

    private void otherDisconnect(Object obj){
        Disconnect disconnectPkt = (Disconnect) obj;
        waitRoomController.handle(disconnectPkt.getWaitRoomState());

    }

    // public void disconnectGracefully() {
    //     try {
    //         if (out != null && !socket.isClosed()) {
    //             out.writeObject(new Disconnect());
    //             out.flush();
    //             out.reset();
    //         }
    //         if (socket != null && !socket.isClosed()) {
    //             socket.close();
    //             System.out.println("Disconnected gracefully.");
    //         }
    //     } catch (IOException e) {
    //         e.printStackTrace();
    //     }
    // }
}
