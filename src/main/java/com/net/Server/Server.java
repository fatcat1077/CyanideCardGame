package com.net.Server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.game.GameState;
import com.net.inviteCode;
import com.net.Room.*;
import com.net.Server.Controller.GameStateController;
import com.net.Server.interfaces.ServerManager;


public class Server implements ServerManager, Runnable{
    //net
    private static int PORT = 8888;
    private static String IP = null;
    private ServerSocket serverSkt;
    
    //shared info
    private static final List<ObjectOutputStream> clients = new CopyOnWriteArrayList<>();
    private static final WaitRoom waitRoom = new WaitRoom();
    private static final List<ServerPacketHandler> handlers = new CopyOnWriteArrayList<>();
    private String invite_Code;

    //shared controller
    private static GameStateController gameStateController = new GameStateController(clients, new GameState(waitRoom.getPlayers()));

    //variable
    private final int playerCnt = 3;
    private int cntId = 1;
    private boolean running = false;

    
    public Server(){
        try{
            running = true;
            IP = InetAddress.getLocalHost().getHostAddress();
            InetAddress addr = InetAddress.getByName(IP);
            serverSkt = new ServerSocket(PORT, playerCnt, addr);


            System.out.println("Server IP: " + IP);
            this.invite_Code = inviteCode.encodeInviteCode(IP);
            waitRoom.setInviteCode(invite_Code);
            System.out.println("Server Invite Code :" + invite_Code);

        }catch(IOException e){
            System.out.println("create server error");
        }
    }

    @Override
    public void run(){
        while(running){
            System.out.println("wait");
            try{

                Socket clientSkt = serverSkt.accept();
                System.out.println("clinet connected!!!");

                ServerPacketHandler handler = new ServerPacketHandler(clientSkt, clients, waitRoom, cntId++, this, gameStateController);
                handlers.add(handler);
                new Thread(handler).start();

            }catch(IOException e){
                System.out.println("Server closed...");
                return;
            }
        }
    }

    public String getInviteCode(){
        return this.invite_Code;
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
        new Thread(new Server()).start();
    }
}
