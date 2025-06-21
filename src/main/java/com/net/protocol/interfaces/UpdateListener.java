package com.net.protocol.interfaces;

import com.net.Room.WaitRoom;
import com.net.protocol.packets.Packet;

public interface UpdateListener {
    public void OnUpdate(WaitRoom room);
}
