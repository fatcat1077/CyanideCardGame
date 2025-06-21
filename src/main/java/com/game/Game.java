package com.game;

import java.util.List;

import com.players.Player;

public class Game {
    private static GameState state;
    private GameController controller;

    //check info
    public boolean running = false;

    //info

    public Game(List<Player> players){
        this.running = true;
        state = new GameState(players);
        this.controller = new GameController(state);
    }
}
