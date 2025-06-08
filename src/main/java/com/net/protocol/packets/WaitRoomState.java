package com.net.protocol.packets;

import com.net.Room.WaitRoom;
import com.net.protocol.enums.PacketType;

public class WaitRoomState extends Packet{
    private WaitRoom room;

    public WaitRoomState(WaitRoom room){
        super(PacketType.WaitRoomState);
        this.room = room;
    }

    public WaitRoom getWaitRoom(){
        return room;
    }

}
