package com.gui;

import java.awt.event.*;
import java.io.IOException;

import javax.swing.*;

import com.net.Client.Client;
import com.net.Server.Server;

public class RoomPanel extends Panel {
    private JButton createButton;
    private JButton joinButton;
    private JTextField nameField;
    private JTextField inviteCodeField;
    private JLabel hintLabel;
    private ActionListener onSwitch;

    private Client client;

    public RoomPanel(int width, int height, ActionListener onSwitch) {
        this.onSwitch = onSwitch;

        setBounds(0, 0, width, height);

        createButton = new JButton();
        createButton.setBounds(200, 200, 400, 50);
        createButton.setText("Create");
        add(createButton);

        joinButton = new JButton();
        joinButton.setBounds(200, 300, 400, 50);
        joinButton.setText("Join");
        add(joinButton);

        nameField = new JTextField();
        nameField.setBounds(300, 100, 200, 30);
        add(nameField);

        inviteCodeField = new JTextField();
        inviteCodeField.setBounds(600, 300, 100, 30);
        add(inviteCodeField);

        hintLabel = new JLabel();
        hintLabel.setBounds(300, 400, 200, 30);
        hintLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(hintLabel);

        setAction();
    }

    private void setAction() {
        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                if (name.equals("")) {
                    hintLabel.setText("Enter name!");
                    return;
                }

                // 跟Server說要Lobby，並要切換Panel
                String inviteCode = new Server().getInviteCode(); // new Server
                try {
                    System.out.println("before new Client");
                    client = new Client(inviteCode, name);
                    System.out.println("after new Client");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                onSwitch.actionPerformed(null);
            }
        });

        joinButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                if (name.equals("")) {
                    hintLabel.setText("Enter name!");
                    return;
                }
                
                String intiveCode = inviteCodeField.getText();

                /*
                try {
                    // 還有一種可能，房號不存在
                    // client = new Client(intiveCode);
                } catch (IOException ex) {
                    hintLabel.setText("Wrong invite code!");
                    return;
                }
                */

                onSwitch.actionPerformed(null);
            }
        });
    }

    public Client getGeneratedClient() {
        return client;
    }
}
