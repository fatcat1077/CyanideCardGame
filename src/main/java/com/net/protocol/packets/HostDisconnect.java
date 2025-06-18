package com.net.protocol.packets;

import com.net.protocol.enums.PacketType;

public class HostDisconnect extends Packet{
    public HostDisconnect(){
        super(PacketType.HostDisconnect);
    }
    
}
