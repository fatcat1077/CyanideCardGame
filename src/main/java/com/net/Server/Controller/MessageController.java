package com.net.Server.Controller;

import java.io.*;
import java.util.List;

import com.net.protocol.packets.*;

public class MessageController{
    private ObjectOutputStream out;
    private List<ObjectOutputStream> clients;

    public MessageController(ObjectOutputStream out, List<ObjectOutputStream> clients) throws IOException {
        this.clients = clients;
        this.out = out;
    }

    public void handle(Object obj){
        broadcastMsg(obj);
    }

    private void broadcastMsg(Object obj) {
        Message msg = (Message) obj;
        System.out.println(msg.getSender() + ": " + msg.getContent());
        
        synchronized (clients) {
            for (ObjectOutputStream clientOut : clients) {
                try {
                    if(clientOut == this.out) continue;
                    clientOut.writeObject(msg);
                    clientOut.flush();
                } catch (IOException e) {
                    System.out.println("Failed to send message to a client.");
                }
            }
        }
    }
}
