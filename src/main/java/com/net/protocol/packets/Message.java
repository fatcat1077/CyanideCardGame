package com.net.protocol.packets;


import com.net.protocol.enums.PacketType;

public class Message extends Packet{
    private String sender;
    private String content;

    public Message(String sender, String content) {
        super(PacketType.Message);
        this.sender = sender;
        this.content = content;
    }

    public String getSender() {
        return sender;
    }

    public String getContent() {
        return content;
    }
}
