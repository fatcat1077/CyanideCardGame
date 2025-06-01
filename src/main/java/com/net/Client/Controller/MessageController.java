package com.net.Client.Controller;

import java.io.*;
import java.util.Scanner;

import com.net.protocol.packets.Message;

public class MessageController implements Runnable{
    private ObjectOutputStream out;
    private int pid;
    private String name;

    //for change host
    WaitRoomController waitRoomController;

    public MessageController(ObjectOutputStream out, String name, int playerId, WaitRoomController waitRoomController) throws IOException {
        this.name = name;
        this.out = out;
        this.pid = playerId;
        this.waitRoomController = waitRoomController;
    }

    public void run(){
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                String input = scanner.nextLine();
                if(input.contains("changeHost")){
                    String[] words = input.split(" ");
                    int hostPID = Integer.parseInt(words[1]);
                    waitRoomController.changeHost(hostPID);
                    continue;
                }else{
                    System.out.println("not changeHost command");
                }

                Message msg = new Message(String.format("%s (%d)", name, pid), input);
                out.writeObject(msg);
                out.flush();
            }
        } catch (IOException e) {
            System.out.println("Error sending message.");
        }
    }

    public void handle(Object obj){
        printMsg(obj);
    }

    private void printMsg(Object obj){
        Message msg = (Message) obj;
        System.out.println(msg.getSender() + ": " + msg.getContent());
    }
}
