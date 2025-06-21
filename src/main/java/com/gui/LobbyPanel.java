package com.gui;

import java.awt.Color;
import java.awt.event.*;
import javax.swing.*;

import com.net.Room.WaitRoom;
import com.players.Player;
import com.net.Client.Client;
import com.net.Client.Controller.WaitRoomController;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class LobbyPanel extends Panel {
    private JLabel inviteCodeLabel;
    private List<JLabel> playerLabels = new CopyOnWriteArrayList<>();   // update
    private JButton startButton;                                        // update
    private JButton readyButton;
    private ChatPanel chatPanel;
    private ActionListener onSwitch;

    private Client client;
    private WaitRoomController wrCtrl;

    private boolean variableForTestIsReady = false;

    public LobbyPanel(int width, int height, Client client, ActionListener onSwitch) {
        this.onSwitch = onSwitch;
        this.client = client;
        this.wrCtrl = client.getWaitRoomController();
        wrCtrl.setUpdateListener(waitRoom -> update(waitRoom));
        //wrCtrl.setSwitchListener(GAME-STATE -> _switch(GAME-STATE));

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
            playerLabel.setOpaque(true);
            add(playerLabel);
            playerLabels.add(playerLabel);
        }

        // 如果是房主，就display startButton
        // 否則(只是房客)display readyButton

        // 先設一個變數當作暫時測試
        //variableForTestIsHost = true;

        // start button
        startButton = new JButton();
        startButton.setBounds(650, 500, 100, 50);
        startButton.setText("Start");
        startButton.setEnabled(false);
        add(startButton);

        // ready button
        readyButton = new JButton();
        readyButton.setBounds(650, 500, 100, 50);
        readyButton.setText("Ready");
        add(readyButton);

        // if (variableForTestIsHost) {
        //     startButton.setVisible(true);
        //     readyButton.setVisible(false);
        // } else {
        //     startButton.setVisible(false);
        //     readyButton.setVisible(true);
        // }

        chatPanel = new ChatPanel(client);
        chatPanel.setBounds(0, 300, 300, 300);
        add(chatPanel);

        setAction();
    }

    private void setAction() {
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                wrCtrl.startGame();
            }
        });

        readyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!variableForTestIsReady) {
                    variableForTestIsReady = true;
                    readyButton.setText("Cancel");
                    readyButton.setBackground(Color.GREEN);

                    wrCtrl.ready();
                } else {
                    variableForTestIsReady = false;
                    readyButton.setText("Ready");
                    readyButton.setBackground(null);

                    wrCtrl.ready();
                }
            }
        });
    }

    public void update(Object state) {
        WaitRoom waitRoom = (WaitRoom) state;

        String inviteCode = waitRoom.getInviteCode();
        inviteCodeLabel.setText(inviteCode);
        
        List<Player> players = waitRoom.getPlayers();
        for (int i = 0; i < playerLabels.size(); i++) {
            JLabel playerLabel = playerLabels.get(i);
         
            if (i < players.size()) {
                Player player = players.get(i);
                playerLabel.setText(player.getName());
                if (player.getReady()) {
                    playerLabel.setBackground(Color.GREEN);
                } else {
                    playerLabel.setBackground(null);
                }
            } else {
                playerLabel.setText("");
                playerLabel.setBackground(null);
            }
            
        }

        for (int i = 0; i < players.size(); i++) {
            System.out.println(players.get(i).getPID());
        }

        boolean isHost = (waitRoom.getHost().getPID() == client.getPlayer().getPID());
        
        if (isHost) {
            startButton.setVisible(true);
            readyButton.setVisible(false);
        } else {
            startButton.setVisible(false);
            readyButton.setVisible(true);
        }

        boolean allPlayersReady = true;
        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            if (player.getName().equals(client.getPlayer().getName())) continue;
            if (!player.getReady()) {
                allPlayersReady = false;
                break;
            }
        }
        
        if (isHost && players.size() == 3 && allPlayersReady) {
            startButton.setEnabled(true);
        }
        
        revalidate();
        repaint();
    }
    /*
    private void _switch() {
        onSwitch.actionPerformed(null);
    }
    */
}
