package com.net.Client.Controller;

import java.io.*;

import com.net.protocol.interfaces.UpdateListener;
import com.net.protocol.packets.Disconnect;
import com.net.protocol.packets.Message;
import com.players.Player;

public class MessageController /*implements Runnable*/{
    private ObjectOutputStream out;
    private Player player;
    private boolean running = false;


    private UpdateListener updateListener;

    public MessageController(ObjectOutputStream out, Player player) throws IOException {
        this.running = true;
        this.out = out;
        this.player = player;
    }

    public void setUpdateListener(UpdateListener updateListener){
        this.updateListener = updateListener;
    }

    public void send(String text) {
        String name = player.getName();

        Message msgPkt = new Message(name, text);
        sendPacket(msgPkt);
        System.out.println("send to server");
    }

    public void handle(Object obj){
        // output the message
        Message msg = (Message) obj;
        //System.out.println(msg.getSender() + ": " + msg.getContent());
        updateListener.OnUpdate(msg);
    }

    public void stop(){
        this.running = false;
    }

    private void disconnect(){
        Disconnect disconnectPkt = new Disconnect();
        sendPacket(disconnectPkt);        
    }
    private void sendPacket(Object packet){
        try{
            out.writeObject(packet);
            out.flush();
            out.reset();
        }catch(IOException e){
            System.out.println(String.format("Sending %s packet error", packet.getClass().getSimpleName()));
        }  
    }
}
