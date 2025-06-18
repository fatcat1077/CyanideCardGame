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
import com.net.Server.interfaces.ServerManager;


public class Server implements ServerManager{
    //net
    private static int PORT = 8888;
    private static String IP = null;
    private ServerSocket serverSkt;
    
    //shared info
    private static final List<ObjectOutputStream> clients = new CopyOnWriteArrayList<>();
    private static final WaitRoom waitRoom = new WaitRoom();
    private static final List<ServerPacketHandler> handlers = new CopyOnWriteArrayList<>();

    //variable
    private int cntId = 1;
    private boolean running = false;

    
    Server(){
        try{
            running = true;
            IP = InetAddress.getLocalHost().getHostAddress();
            InetAddress addr = InetAddress.getByName(IP);
            serverSkt = new ServerSocket(PORT, 20, addr);


            System.out.println("Server IP: " + IP);
            String invite_Code = inviteCode.encodeInviteCode(IP);
            waitRoom.setInviteCode(invite_Code);
            System.out.println("Server Invite Code :" + invite_Code);

        }catch(IOException e){
            System.out.println("create server error");
        }
        while(running){
            try{
                Socket clientSkt = serverSkt.accept();
                System.out.println("clinet connected!!!");

                ServerPacketHandler handler = new ServerPacketHandler(clientSkt, clients, waitRoom, cntId++, this);
                handlers.add(handler);
                new Thread(handler).start();

            }catch(IOException e){
                System.out.println("Server closed...");
                return;
            }
        }
    }

    public void shutdownServer() {
        running = false;

        for(ServerPacketHandler handler : handlers){
            handler.stop();
        }

        try {
            serverSkt.close();
            System.out.println("Server has been shut down.");
        } catch (IOException e) {
            System.out.println("Error closing server socket.");
        }
    }

    public static void main(String args[]) throws Exception{ 
        new Server();
    }
}
