package com.net.Server.Controller;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Map;

import com.game.GameController;
import com.game.GameState;
import com.net.protocol.packets.DealerChoose;
import com.net.protocol.packets.DealerRate;
import com.net.protocol.packets.PlayerChoose;

public class GameStateController {
    private List<ObjectOutputStream> clients;
    private GameState state;
    private GameController controller;

    private GameState copyState;

    public GameStateController (List<ObjectOutputStream> clients, GameState state){
        this.clients = clients;
        this.state = state;
        this.controller = new GameController(state);
    }

    public GameState getState(){
        return this.state;
    }

    public void handleDealerChoose(Object obj){
        GameState newState = ((DealerChoose)obj).getGameState();
        this.controller.commitDealerPlay(newState);
        
        PlayerChoose playerChoosePkt = new PlayerChoose(this.state);
        broacastPkt(playerChoosePkt);
    }

    public void handlePlayerChoose(Object obj){
        GameState newGameState = ((PlayerChoose)obj).getGameState();
        //clear 
        if(this.copyState == null){
            this.copyState = new GameState(this.state);
        }

        Map<Integer, Integer> incomingChoices = newGameState.getPlayerChosenMap();
        Map<Integer, Integer> copyMap = copyState.getPlayerChosenMap();

        for (Map.Entry<Integer, Integer> entry : incomingChoices.entrySet()) {
            if (!copyMap.containsKey(entry.getKey())) {
                copyMap.put(entry.getKey(), entry.getValue());
            }
        }

        int chosenCount = copyMap.size();
        int totalPlayers = this.state.getPlayers().size();

        if (chosenCount >= totalPlayers - 1) { 
            this.controller.commitPlayersPlay(copyState);

            DealerRate dealRatePkt = new DealerRate(this.copyState); 
            broacastPkt(dealRatePkt);

            this.copyState = null;
        }

    }

    public void handleDealerRate(Object obj){
        GameState newState = ((DealerRate)obj).getGameState();
        this.controller.endRound(newState);

        DealerChoose dealerChoosePkt = new DealerChoose(this.state);
        broacastPkt(dealerChoosePkt);
    }
    
    private void broacastPkt(Object obj){
        for (ObjectOutputStream clientOut : clients) {
            try {
                clientOut.writeObject(obj);
                clientOut.flush();
                clientOut.reset();
            } catch (IOException e) {
                System.out.println("Failed to send disconnectPkt to a client.");
            }
        }
    }

}
