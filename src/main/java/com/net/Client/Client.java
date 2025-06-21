package com.net.Client;

import java.io.*;
import java.net.*;
import java.util.*;

import com.net.Client.Controller.*;
import com.players.Player;

public class Client {
    //net
    private static String HOST;
    private static final int PORT = 8888;
    private Socket socket;

    //info
    private Player player;
    
    //todo :
    //listener
    // private UpdateListener updateListener;
    // private SwitchListener switchListener;

    private ClientPacketHandler handler;

    public Client(String invite_Code, String name) throws IOException, ConnectException{

        this.socket = new Socket(HOST, PORT);
        this.player = new Player(name);

        this.handler = new ClientPacketHandler(socket, player);


        //this.handler.start();
        new Thread(handler).start();

    }

    public WaitRoomController getWaitRoomController(){
        if(this.handler != null){
            return this.handler.getWaitRoomController();
        }
        return null;
    }

    public MessageController getMessageController(){
        if(this.handler != null){
            return this.handler.getMessageController();
        }
        return null;
    }

    //need to be set
    public ClientPacketHandler getClientPacketHandler(){
        if(this.handler != null){
            return this.handler;
        }
        return null;
    }

    public Player getPlayer() {
        return player;
    }

    

    public static void main(String[] args){
        // combine with GUI
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter invite code :");
        String invite_Code = scanner.nextLine();
        System.out.print("enter your name :");
        String name = scanner.nextLine();

        try{
            new Client(invite_Code, name);
        }catch (IOException e){
            System.out.println("---------------\nclient connect error");
            e.printStackTrace();
        }
        scanner.close();
    }

}
