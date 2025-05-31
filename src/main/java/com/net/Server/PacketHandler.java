package com.net.Server;

import java.io.*;
import java.net.Socket;
import java.util.List;

import com.net.Server.Controller.*;
import com.net.protocol.packets.*;

public class PacketHandler implements Runnable{
    //net
    private Socket clientSkt;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    
    // info
    private List<ObjectOutputStream> clients;
    private int pid;

    //Controller
    private MessageController msgController;




    PacketHandler(Socket clientSocket, List<ObjectOutputStream> clients, int playerId) throws IOException{
        this.clientSkt = clientSocket;
        this.clients = clients;
        this.out = new ObjectOutputStream(clientSocket.getOutputStream());
        this.in = new ObjectInputStream(clientSocket.getInputStream());
        this.msgController = new MessageController(out, clients);
        this.pid = playerId;

        synchronized (clients) {
            clients.add(out);
        }

        //init setup
        this.out.writeObject(new Init(this.pid));
        this.out.flush();

    }

    @Override
    public void run(){
        try {
            while (true) {
                Object revObject = in.readObject();
                
                //identify which packet it is 
                if(revObject instanceof Message){
                    msgController.handle(revObject);
                }

            }
        } catch (Exception e) {
            System.out.println(String.format("Client (%d) disconnected: ", pid) + clientSkt);
        } finally {
            synchronized (clients) {
                clients.remove(out);
            }
            try {
                clientSkt.close();
            } catch (IOException e) {
                System.out.println("socket closing error (by server)");
            }
        }
    }
}
