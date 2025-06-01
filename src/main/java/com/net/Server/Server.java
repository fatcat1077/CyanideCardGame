package com.net.Server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.net.inviteCode;
import com.net.Room.*;
import com.players.Player;

public class Server {
    //net
    private static int PORT = 8888;
    private static String IP = "192.168.0.23";
    private ServerSocket serverSkt;
    
    //shared info
    private static final List<ObjectOutputStream> clients = new CopyOnWriteArrayList<>();
    private static final WaitRoom waitRoom = new WaitRoom();

    //variable
    private int cntId = 1;

    
    Server(){
        try{
            InetAddress addr = InetAddress.getByName(IP);
            serverSkt = new ServerSocket(PORT, 20, addr);


            System.out.println("Server IP: " + IP);
            System.out.println("Server Invite Code :" + inviteCode.encodeInviteCode(IP));
        }catch(IOException e){
            System.out.println("create server error");
        }
        while(true){
            try{
                Socket clientSkt = serverSkt.accept();
                System.out.println("clinet connected!!!");
                new Thread(new PacketHandler(clientSkt, clients, waitRoom, cntId++)).start();

            }catch(IOException e){
                System.out.println("client connect error");
            }
        }
    }

    public static void main(String args[]){ 
        new Server();
    }
}
