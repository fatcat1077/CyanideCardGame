package com.net.Room;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.players.Player;

public class WaitRoom implements Serializable{
    private final List<Player> players = new CopyOnWriteArrayList<>();
    private volatile Player host; 

    public void addPlayer(Player player) {
        players.add(player);
        if (host == null) {
            host = player; // first one becomes host
        }
    }

    public void removePlayer(Player player) {
        players.remove(player);
        if (player.equals(host)) {
            host = players.isEmpty() ? null : players.get(0); // change host
        }
    }

    public List<Player> getPlayers() {
        return players;
    }

    public Player getHost() {
        return host;
    }

    public void setHost(Player newHost) {
        boolean isExist = false;
        for(Player player : players){
            if(player.getPID() == newHost.getPID()){
                isExist = true;
                break;
            }
        }
        if (isExist) {
            this.host = newHost;
        }else{
            System.out.println("WaitRoom doesn't have the player");
        }
    }

}
