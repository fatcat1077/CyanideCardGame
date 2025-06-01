package com.net.Client;

import java.io.*;
import java.net.Socket;

import com.net.Client.Controller.*;
import com.net.Room.WaitRoom;
import com.net.protocol.packets.*;

public class PacketHandler{
    //net
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;


    //individual info
    private String name;
    private int pid;

    //controller
    private MessageController msgController;
    private WaitRoomController waitRoomController;

    PacketHandler(Socket socket, String name) throws IOException{
        this.socket = socket;
        this.name = name;
        this.out = new ObjectOutputStream(this.socket.getOutputStream());
        this.in = new ObjectInputStream(this.socket.getInputStream());
        this.waitRoomController = new WaitRoomController();
    }

    public void start(){
        try {
            while (true) {
                Object revObject = in.readObject();
                
                //identify which packet it is 
                if (revObject instanceof Init){
                    init(revObject);
                }else if(revObject instanceof Message){
                    msgController.handle(revObject);
                }else if(revObject instanceof WaitRoomState){
                    waitRoomController.handle(revObject);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Disconnected from server.");
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                System.out.println("socket closing error (by client)");
            }
        }
    }

    private void init(Object obj) throws IOException{
        Init init = (Init) obj;
        this.pid = init.getPID();

        //return Init packet to server
        init.setName(name);
        out.writeObject(init);
        out.flush();

        // open chat
        this.msgController = new MessageController(this.out, this.name, this.pid);
        new Thread(this.msgController).start();
    }
}
