package com.net.protocol.packets;

import com.net.Room.WaitRoom;
import com.net.protocol.enums.PacketType;

public class Disconnect extends Packet{
    private WaitRoomState waitRoomState;

    public Disconnect(){
        super(PacketType.Disconnect);
    }

    public Disconnect(WaitRoom waitRoom){
        super(PacketType.Disconnect);
        this.waitRoomState = new WaitRoomState(waitRoom);
    }

    public WaitRoomState getWaitRoomState(){
        return waitRoomState;
    }

}
