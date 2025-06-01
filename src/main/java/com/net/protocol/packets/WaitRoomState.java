package com.net.protocol.packets;

import java.io.Serializable;

import com.net.Room.WaitRoom;

public class WaitRoomState implements Serializable{
    private WaitRoom room;

    public WaitRoomState(WaitRoom room){
        this.room = room;
    }

    public WaitRoom getWaitRoom(){
        return room;
    }

}
