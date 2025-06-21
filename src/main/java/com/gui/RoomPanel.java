package com.gui;

import java.awt.event.*;
import java.io.IOException;

import javax.swing.*;

import com.net.Server.Server;

public class RoomPanel extends Panel {
    private JButton createButton;
    private JButton joinButton;
    private JTextField nameField;
    private JTextField inviteCodeField;
    private JLabel hintLabel;
    private ActionListener onSwitch;

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
                if (nameField.getText().equals("")) {
                    hintLabel.setText("Enter name!");
                    return;
                }

                // 跟Server說要Lobby，並要切換Panel
                //new Server();

                onSwitch.actionPerformed(null);
            }
        });

        joinButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (nameField.getText().equals("")) {
                    hintLabel.setText("Enter name!");
                    return;
                }
                
                String intiveCode = inviteCodeField.getText();
                if (!isInviteCode(intiveCode)) {
                    hintLabel.setText("Wrong invite code!");
                    return;
                }

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

    private boolean isInviteCode(String text) { // only accept pure number, can't be empty
        if (text.isEmpty()) return false;

        for (char c : text.toCharArray()) {
            if (!Character.isDigit(c)) return false;
        }
        return true;
    }
}
