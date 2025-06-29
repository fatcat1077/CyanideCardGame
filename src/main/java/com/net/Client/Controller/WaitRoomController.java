package com.net.Client.Controller;

import java.io.IOException;
import java.io.ObjectOutputStream;
import com.net.Room.WaitRoom;
import com.net.protocol.interfaces.SwitchListener;
import com.net.protocol.interfaces.UpdateListener;
import com.net.protocol.packets.StartGame;
import com.net.protocol.packets.WaitRoomState;
import com.players.Player;

public class WaitRoomController {
    //net
    private ObjectOutputStream out;

    //info
    private WaitRoom room; 
    private Player player;
    private Player host;
    private int pid;

    //listener
    private UpdateListener updateListener;
    private SwitchListener switchListener;

    public WaitRoomController(int pid, ObjectOutputStream out, Player player){
        this.pid = pid;
        this.out = out;
        this.player = player;
    }

    //setter
    public void setUpdateListener(UpdateListener updateListener){
        this.updateListener = updateListener;
    }

    public void setSwitchListener(SwitchListener switchListener){
        this.switchListener = switchListener;
    }

    public void switchToGame(){
        this.switchListener.OnSwitch(null);
    }

    public void handle(Object obj){
        WaitRoomState roomState = (WaitRoomState) obj;

        update(roomState.getWaitRoom());
    }

    public void ready(){

        WaitRoom newWaitRoom = this.room;
        newWaitRoom.setReady(this.player);
        WaitRoomState newRoomStatePkt = new WaitRoomState(newWaitRoom);
        sendPacket(newRoomStatePkt);
    }

    public void startGame(){
        StartGame startGamePkt = new StartGame();
        sendPacket(startGamePkt);
    }

    // maybe will remove this command
    public void changeHost(int pid){
        if( this.pid == room.getHost().getPID()){ //check if i am host
            Player newHost = null;
            boolean isExist = false;
            for(Player player : this.room.getPlayers()){
                if(player.getPID() == pid){
                    newHost = player;
                    isExist = true;
                    break;
                }
            }
            if(!isExist) return;

            WaitRoom newWaitRoom = this.room;
            newWaitRoom.setHost(newHost);
            WaitRoomState newRoomStatePkt = new WaitRoomState(newWaitRoom);
            sendPacket(newRoomStatePkt);
        }else{
            System.out.println("You are not host.");
            System.out.println("Host is : " + Integer.toString(room.getHost().getPID()));
        }
    }
    
    private void update(WaitRoom waitRoom){
        this.room = waitRoom;
        this.host = this.room.getHost();
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("update");
        this.updateListener.OnUpdate(waitRoom); //update the waitroom
        
        System.out.println(String.format("Now RoomState (invite code = %s): ", waitRoom.getInviteCode()));
        System.out.println("Players: ");
        for(Player player : this.room.getPlayers()){
            System.out.println(String.format("%s (%d) ,ready: %b ", player.getName(), player.getPID(), player.getReady()));
        }
        System.out.println(String.format("Room Host: %s (%d)", host.getName(), host.getPID()));
        System.out.println("----------------------");
        
    }

    private void sendPacket(Object packet){
        try{
            out.writeObject(packet);
            out.flush();
            out.reset();
        }catch(IOException e){
            System.out.println(String.format("Sending %s packet error", packet.getClass().getSimpleName()));
        }  
    }
}
