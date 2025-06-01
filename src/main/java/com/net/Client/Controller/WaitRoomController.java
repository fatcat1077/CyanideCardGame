package com.net.Client.Controller;

import java.util.List;

import com.net.Room.WaitRoom;
import com.net.protocol.packets.WaitRoomState;
import com.players.Player;

public class WaitRoomController {
    private WaitRoom room;
    private Player host;
    private List<Player> players;

    public void handle(Object obj){
        WaitRoomState roomState = (WaitRoomState) obj;
        this.room = roomState.getWaitRoom();
        this.host = this.room.getHost();
        this.players = this.room.getPlayers();

        update();
    }

    private void update(){
        System.out.println("Now roomState: ");
        System.out.print("Players: ");
        for(Player player : players){
            System.out.print(String.format("%s (%d), ", player.getName(), player.getPID()));
        }
        System.out.println("");
        System.out.println(String.format("Room Host: %s (%d)", host.getName(), host.getPID()));
    }

}
