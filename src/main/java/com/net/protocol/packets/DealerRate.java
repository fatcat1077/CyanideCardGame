package com.net.protocol.packets;

import com.game.GameState;
import com.net.protocol.enums.PacketType;

public class DealerRate extends Packet{
    private GameState state;

    public DealerRate(GameState state){
        super(PacketType.DealerRate);
        this.state = state;
    }

    public GameState getGameState(){
        return this.state;
    }
}
