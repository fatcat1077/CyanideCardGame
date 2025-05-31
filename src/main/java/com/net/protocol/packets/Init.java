package com.net.protocol.packets;

import java.io.Serializable;

public class Init implements Serializable{
    int pid;

    public Init(int pid){
        this.pid = pid;
    }

    public int getPID(){
        return pid;
    }
}
