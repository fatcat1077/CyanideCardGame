package com.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.*;
import javax.swing.*;

import com.net.Client.Client;
import com.net.Client.Controller.MessageController;
import com.net.protocol.packets.Message;

public class ChatPanel extends JPanel {
    private JTextArea chatArea;
    private JTextField textField;
    private JButton sendButton;

    private Client client;
    private MessageController msgCtrl;

    private int width = 300;
    private int height = 300;

    public ChatPanel(Client client) {
        this.client = client;
        this.msgCtrl = client.getMessageController();
        msgCtrl.setUpdateListener(text -> update(text));
        
        setPreferredSize(new Dimension(width, height));
        setLayout(null);

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(chatArea);
        scrollPane.setBounds(0, 0, 300, 270);
        add(scrollPane);

        textField = new JTextField();
        textField.setBounds(0, 270, 230, 30);
        add(textField);

        sendButton = new JButton("Send");
        sendButton.setBounds(230, 270, 70, 30);
        add(sendButton);

        setAction();
    }

    private void setAction() {
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = textField.getText();
                if (text.equals("")) return;

                // 清空並把文字送給Server
                sendText(text);
            }
        });

        textField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = textField.getText();
                if (text.equals("")) return;

                // 清空並把文字送給Server
                sendText(text);
            }
        });
    }

    private void sendText(String text) {
        textField.setText("");
        msgCtrl.send(text);
        // 送text給server
    }

    public void update(Object state) {
        Message msg = (Message) state;
        String text = msg.getSender() + ": " + msg.getContent() + "\n";
        chatArea.append(text);
        // update的方法，append最新的訊息就好
    }
}
