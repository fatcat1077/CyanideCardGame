package com.net.Client;

import java.io.*;
import java.net.*;
import java.util.*;

import com.net.inviteCode;
import com.net.Client.Controller.WaitRoomController;
import com.players.Player;
import com.net.protocol.interfaces.*;

public class Client {
    //net
    private static String HOST;
    private static final int PORT = 8888;
    private Socket socket;

    private Scanner scanner;

    //info
    private Player player;
    
    //todo :
    //listener
    // private UpdateListener updateListener;
    // private SwitchListener switchListener;

    private ClientPacketHandler handler;

    public Client(String invite_Code, String name) throws IOException, ConnectException{
        /*
        this.scanner = new Scanner(System.in);
        while(true){
            if(inviteCode.isValidInviteCode(invite_Code)){
                break;
            }else{
                System.out.println("inviteCode error type");
                System.out.print("Retype inviteCode :");
                invite_Code = this.scanner.nextLine();
            }
        }
        */

        HOST = inviteCode.decodeInviteCode(invite_Code);

        System.out.println(HOST);
        this.socket = new Socket(HOST, PORT);
        this.player = new Player(name);

        System.out.println("a");
        this.handler = new ClientPacketHandler(socket, player);

        System.out.println("b");

        //this.handler.start();
        new Thread(handler).start();

        System.out.println("c");
    }

    public WaitRoomController getWaitRoomController(){
        if(this.handler != null){
            return this.handler.getWaitRoomController();
        }
        return null;
    }

    public Player getPlayer() {
        return player;
    }

    // todo
    // public void setUpdateListener(UpdateListener updateListener){
    //     this.updateListener = updateListener;
    //     if(this.handler != null){
    //         this.handler.setUpdateListener(updateListener);
    //     }
    // }

    //  public void setSwitchListener(SwitchListener switchListener){
    //     this.switchListener = switchListener;
    //     if(this.handler != null){
    //         this.handler.setSwitchListener(switchListener);
    //     }
    // }

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
        
    }

}
