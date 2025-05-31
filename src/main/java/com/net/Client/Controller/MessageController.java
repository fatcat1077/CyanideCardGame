package com.net.Client.Controller;

import java.io.*;
import java.util.Scanner;

import com.net.protocol.packets.Message;

public class MessageController implements Runnable{
    private ObjectOutputStream out;
    private int pid;
    private String name;

    public MessageController(ObjectOutputStream out, String name, int playerId) throws IOException {
        this.name = name;
        this.out = out;
        this.pid = playerId;
    }

    public void run(){
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                String input = scanner.nextLine();
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
