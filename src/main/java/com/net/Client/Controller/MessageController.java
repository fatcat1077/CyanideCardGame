package com.net.Client.Controller;

import java.io.*;
import java.util.Scanner;

import com.net.protocol.interfaces.UpdateListener;
import com.net.protocol.packets.Disconnect;
import com.net.protocol.packets.Message;
import com.net.protocol.packets.StartGame;
import com.players.Player;

public class MessageController /*implements Runnable*/{
    private ObjectOutputStream out;
    private Player player;
    private boolean running = false;

    //for change host
    WaitRoomController waitRoomController;

    private UpdateListener updateListener;

    public MessageController(ObjectOutputStream out, Player player, WaitRoomController waitRoomController) throws IOException {
        this.running = true;
        this.out = out;
        this.player = player;
        this.waitRoomController = waitRoomController;
    }

    public void setUpdateListener(UpdateListener updateListener){
        this.updateListener = updateListener;
    }

    /*
    public void run(){
        Scanner scanner = new Scanner(System.in);
        while (running) {
            String input = scanner.nextLine();
            if(!running) break;

            // todo: combine with GUI
            if(input.equals("disconnect")){
                disconnect();
                break;
            }else if(input.contains("changeHost")){
                String[] words = input.split(" ");
                int hostPID = Integer.parseInt(words[1]);
                waitRoomController.changeHost(hostPID);
                continue;
            }else if (input.equals("ready")){
                waitRoomController.ready();
                continue;
            }else if (input.equals("start")){
                waitRoomController.startGame();
                continue;

            }
            if(input.equals("help")){
                System.out.println("Commands:\n (1)disconnect\n(2)changeHost <newHost_pid>");
                continue;
            }

            Message msgPkt = new Message(String.format("%s (%d)", player.getName(), player.getPID()), input);

            sendPacket(msgPkt);
        }
        scanner.close();
    }
    */

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
