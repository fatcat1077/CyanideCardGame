package com.gui;

import java.awt.Color;
import java.awt.event.*;
import javax.swing.*;

import com.net.Room.WaitRoom;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class LobbyPanel extends Panel {
    private JLabel inviteCodeLabel;                                     // update
    private List<JLabel> playerLabels = new CopyOnWriteArrayList<>();   // update
    private JButton readyButton;                                        // update
    private JButton startButton;                                        
    private ChatPanel chatPanel;
    private ActionListener onSwitch;

    public LobbyPanel(int width, int height, ActionListener onSwitch) {
        this.onSwitch = onSwitch;

        setBounds(0, 0, width, height);
        setLayout(null);

        inviteCodeLabel = new JLabel();
        inviteCodeLabel.setBounds(0, 0, 100, 100);
        inviteCodeLabel.setText("Invite code");
        inviteCodeLabel.setOpaque(true);
        inviteCodeLabel.setBackground(Color.PINK);
        add(inviteCodeLabel);

        for (int i = 0; i < 3; i++) {
            JLabel playerLabel = new JLabel();
            playerLabel.setBounds(0, 100 + 50 * i, 100, 50);
            playerLabel.setText("player" + i);
            add(playerLabel);
            playerLabels.add(playerLabel);
        }

        readyButton = new JButton();
        readyButton.setBounds(650, 500, 100, 50);
        readyButton.setText("Ready");
        add(readyButton);

        // start button

        chatPanel = new ChatPanel();
        chatPanel.setBounds(0, 300, 300, 300);
        add(chatPanel);

        setAction();
    }

    private void setAction() {
        readyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
            }
        });
    }

    public void update(WaitRoom waitRoom) {
        ;
        revalidate();
        repaint();
    }
}
