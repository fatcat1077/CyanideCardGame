package com.net.protocol.packets;

import com.net.protocol.enums.PacketType;

public class Init extends Packet{
    private int pid;
    private String name;

    public Init(int pid){
        super(PacketType.Init);
        this.pid = pid;
    }


    public int getPID(){
        return pid;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }
}
