package com.net.Client;

import java.io.*;
import java.net.*;
import java.util.*;

import com.net.inviteCode;
import com.players.Player;

public class Client {
    //net
    private static String HOST;
    private static final int PORT = 8888;
    private Socket socket;

    private Scanner scanner;

    //info
    private Player player;

    //handler
    private ClientPacketHandler handler;

    Client(String host, int port){
        try {
            System.out.println(host);
            this.socket = new Socket(host, port);
            this.scanner = new Scanner(System.in);
            
            System.out.print("enter your name :");
            this.player = new Player(this.scanner.nextLine());

            handler = new ClientPacketHandler(socket, player);
            handler.start();


        }catch(IOException e){
            System.out.println("server connect error");
            e.printStackTrace();
        }
    }


    public static void main(String[] args){
        // combine with GUI
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter invite code :");
        HOST = inviteCode.decodeInviteCode(scanner.nextLine());
        new Client(HOST, PORT);
    }

}
