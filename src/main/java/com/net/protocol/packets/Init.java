package com.net.protocol.packets;

import java.io.Serializable;

public class Init implements Serializable{
    private int pid;
    private String name;

    public Init(int pid){
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
