package com.net.protocol.packets;

import com.game.GameState;
import com.net.protocol.enums.PacketType;

public class PlayerChoose extends Packet{
    private GameState state;

    public PlayerChoose(GameState state){
        super(PacketType.PlayerChoose);
        this.state = state;
        this.state.setState(PacketType.PlayerChoose);
    }

    public GameState getGameState(){
        return this.state;
    }
}
