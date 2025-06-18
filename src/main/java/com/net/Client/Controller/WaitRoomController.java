package com.net.Client.Controller;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;

import com.net.Room.WaitRoom;
import com.net.protocol.packets.WaitRoomState;
import com.players.Player;

public class WaitRoomController {
    //net
    private ObjectOutputStream out;

    //info
    private WaitRoom room;
    private Player host;
    private List<Player> players;
    private int pid;

    public WaitRoomController(int pid, ObjectOutputStream out){
        this.pid = pid;
        this.out = out;
    }

    public void handle(Object obj){
        WaitRoomState roomState = (WaitRoomState) obj;

        update(roomState.getWaitRoom());
    }

    private void update(WaitRoom waitRoom){
        this.room = waitRoom;
        this.host = this.room.getHost();
        this.players = this.room.getPlayers();

        System.out.println("Now roomState: ");
        System.out.print("Players: ");
        for(Player player : players){
            System.out.print(String.format("%s (%d), ", player.getName(), player.getPID()));
        }
        System.out.println("");
        System.out.println(String.format("Room Host: %s (%d)", host.getName(), host.getPID()));
    }

    // maybe will remove this command
    public void changeHost(int pid){
        if( this.pid == room.getHost().getPID()){ //check if i am host
            Player newHost = null;
            boolean isExist = false;
            for(Player player : players){
                if(player.getPID() == pid){
                    newHost = player;
                    isExist = true;
                    break;
                }
            }
            if(!isExist) return;

            WaitRoom newWaitRoom = this.room;
            newWaitRoom.setHost(newHost);
            WaitRoomState newRoomStatePkt = new WaitRoomState(newWaitRoom);
            sendPacket(newRoomStatePkt);
        }else{
            System.out.println("You are not host.");
            System.out.println("Host is : " + Integer.toString(room.getHost().getPID()));
        }
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
