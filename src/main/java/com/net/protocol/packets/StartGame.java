package com.net.protocol.packets;

import com.net.protocol.enums.PacketType;

public class StartGame extends Packet{
    
    public StartGame(){
        super(PacketType.StartGame);
    }
}
