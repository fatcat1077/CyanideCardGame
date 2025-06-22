package com.net.protocol.packets;

import com.game.GameState;
import com.net.protocol.enums.PacketType;

public class DealerChoose extends Packet{
    private GameState state;

    public DealerChoose(GameState state){
        super(PacketType.DealerChoose);
        this.state = state;
        this.state.setState(PacketType.DealerChoose);
    }

    public GameState getGameState(){
        return this.state;
    }
}