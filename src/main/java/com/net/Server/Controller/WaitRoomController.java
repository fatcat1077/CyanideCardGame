package com.net.Server.Controller;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;

import com.net.Room.WaitRoom;
import com.net.protocol.packets.WaitRoomState;
import com.players.Player;

public class WaitRoomController {
    private List<ObjectOutputStream> clients;
    private WaitRoom waitRoom;

    public WaitRoomController(List<ObjectOutputStream> clients, WaitRoom waitRoom) throws IOException {
        this.clients = clients;
        this.waitRoom = waitRoom;
    }

    public void updateToAll(WaitRoomState roomStatePacket){
        
        //update everyone's roomState
        synchronized (clients) {
            for (ObjectOutputStream clientOut : clients) {
                try {
                    clientOut.writeObject(roomStatePacket);
                    clientOut.flush();
                    clientOut.reset();
                } catch (IOException e) {
                    System.out.println("Failed to send roomState to a client.");
                }
            }
        }
    }

    //recieve
    public void handle(Object obj, int pid){
        WaitRoomState newState = (WaitRoomState) obj;
        updateWaitRoom(newState.getWaitRoom(), pid);
        updateToAll(newState);
    }

    private void updateWaitRoom(WaitRoom newWaitRoom, int pid){
        this.waitRoom.setHost(newWaitRoom.getHost());
        
        Player newPlayer = null;
        for(Player player : newWaitRoom.getPlayers()){
            if(player.getPID() == pid){
                newPlayer = player;
                break;
            }
        }
        if(newPlayer != null){
            for(Player oldPlayer : this.waitRoom.getPlayers()){
                if(oldPlayer.getPID() == pid){
                    if(oldPlayer.getReady() != newPlayer.getReady()){
                        this.waitRoom.setReady(oldPlayer);
                        break;
                    }
                }
            }
        }
         
    }
}
