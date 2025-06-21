package com.net.protocol.interfaces;

import com.net.protocol.packets.Packet;

public interface  SwitchListener {
    public void OnSwitch(Packet packet);
}
