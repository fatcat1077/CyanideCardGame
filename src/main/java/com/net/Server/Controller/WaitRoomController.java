package com.net.Server.Controller;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;

import com.net.Room.WaitRoom;
import com.net.protocol.packets.WaitRoomState;

public class WaitRoomController {
    private List<ObjectOutputStream> clients;
    private WaitRoom waitRoom;

    public WaitRoomController(List<ObjectOutputStream> clients, WaitRoom waitRoom) throws IOException {
        this.clients = clients;
        this.waitRoom = waitRoom;
    }

    public void updateToAll(WaitRoomState roomStatePacket){
        
        //update everyone's roomState
        synchronized (clients) {
            for (ObjectOutputStream clientOut : clients) {
                try {
                    clientOut.writeObject(roomStatePacket);
                    clientOut.flush();
                    clientOut.reset();
                } catch (IOException e) {
                    System.out.println("Failed to send roomState to a client.");
                }
            }
        }
    }

    //recieve
    public void handle(Object obj){
        WaitRoomState newState = (WaitRoomState) obj;
        updateWaitRoom(newState.getWaitRoom());
        updateToAll(newState);
    }

    private void updateWaitRoom(WaitRoom newWaitRoom){
        this.waitRoom.setHost(newWaitRoom.getHost());
    }
}
