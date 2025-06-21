package com.net.Client.Controller;

import java.io.IOException;
import java.io.ObjectOutputStream;

import com.game.GameState;
import com.net.protocol.interfaces.UpdateListener;
import com.net.protocol.packets.DealerChoose;
import com.net.protocol.packets.DealerRate;
import com.net.protocol.packets.PlayerChoose;

public class GameStateController {
    //listener
    private UpdateListener updateListener;

    //net
    private ObjectOutputStream out;

    //info
    private GameState state;
    private int pid;

    public GameStateController(ObjectOutputStream out, int pid){
        this.out = out;
        this.pid = pid;
    }


    public void setUpdateListener(UpdateListener updateListener){
        this.updateListener = updateListener;
    }

    public void handle(Object obj){
        GameState newState = null;
        if(obj instanceof DealerChoose){
            newState = ((DealerChoose) obj).getGameState();
        }else if(obj instanceof PlayerChoose){
            newState = ((PlayerChoose) obj).getGameState();
        }else if(obj instanceof DealerRate){
            newState = ((DealerRate) obj).getGameState();
        }
        this.state = newState;
        this.updateListener.OnUpdate(this.state);
    }

    public void dealerChoose(){
        if(this.state.getDealer().getPID() == this.pid){
            // you are dealer
            // do something

            //Last, send the new state
            DealerChoose dealerChoosePkt = new DealerChoose(state);
            sendPacket(dealerChoosePkt);
        }
    }

    //player need to choose
    public void playerChoose(){
        if(this.state.getDealer().getPID() != this.pid){
            // you are player
            // do something

            //Last, send the new state
            PlayerChoose playerChoosePkt = new PlayerChoose(state);
            sendPacket(playerChoosePkt);
        }
    }

    public void dealerRate(){
        if(this.state.getDealer().getPID() == this.pid){
            // you are dealer
            // do something

            //Last, send the new state
            DealerRate dealerRatePkt = new DealerRate(state);
            sendPacket(dealerRatePkt);
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
