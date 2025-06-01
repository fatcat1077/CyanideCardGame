package com.net.Server.Controller;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;

import com.net.Room.WaitRoom;
import com.net.protocol.packets.WaitRoomState;

public class WaitRoomController {
    private List<ObjectOutputStream> clients;

    public WaitRoomController(List<ObjectOutputStream> clients) throws IOException {
        this.clients = clients;
    }

    public void updateToAll(WaitRoom waitRoom){
        //update everyone's roomState
        WaitRoomState roomStatePacket = new WaitRoomState(waitRoom);

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

    }

}
