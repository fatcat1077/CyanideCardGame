package com.net.protocol.packets;

import java.io.Serializable;

import com.net.protocol.enums.PacketType;

public class Packet implements Serializable{
    private PacketType type;
    Packet(PacketType type){
        this.type = type;
    }

    public PacketType getType(){
        return type;
    }
}
