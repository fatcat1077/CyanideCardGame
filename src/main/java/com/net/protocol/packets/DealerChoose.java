package com.net.protocol.packets;

import com.game.GameState;
import com.net.protocol.enums.PacketType;

public class DealerChoose extends Packet{
    private GameState state;

    public DealerChoose(GameState state){
        super(PacketType.DealerChoose);
        this.state = state;
    }

    public GameState getGameState(){
        return this.state;
    }
}