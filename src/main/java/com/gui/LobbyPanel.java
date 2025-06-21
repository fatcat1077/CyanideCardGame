package com.gui;

import java.awt.Color;
import java.awt.event.*;
import javax.swing.*;

import com.net.Room.WaitRoom;
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

    private boolean variableForTestIsHost;
    private boolean variableForTestIsReady = false;

    public LobbyPanel(int width, int height, /* Client client, 這裡會傳Client進來 */ActionListener onSwitch) {
        this.onSwitch = onSwitch;
        //this.client = client;
        // this.wrCtrl = // 從Client拿;
        // client.setUpdateListener(waitRoom -> update(waitRoom));
        // client.setSwitchListener(GAME-STATE -> swutch(GAME-STATE));

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

        // 如果是房主，就display startButton
        // 否則(只是房客)display readyButton

        // 先設一個變數當作暫時測試
        variableForTestIsHost = true;

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

        if (variableForTestIsHost) {
            startButton.setVisible(true);
            readyButton.setVisible(false);
        } else {
            startButton.setVisible(false);
            readyButton.setVisible(true);
        }

        chatPanel = new ChatPanel();
        chatPanel.setBounds(0, 300, 300, 300);
        add(chatPanel);

        setAction();
    }

    private void setAction() {
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 告訴Server遊戲開始
            }
        });

        readyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!variableForTestIsReady) {
                    variableForTestIsReady = true;
                    readyButton.setText("Cancel");
                    readyButton.setBackground(Color.GREEN);

                    // wrCtrl.ready();
                } else {
                    variableForTestIsReady = false;
                    readyButton.setText("Ready");
                    readyButton.setBackground(null);

                    // wrCtrl.ready();
                }
            }
        });
    }

    public void update(WaitRoom waitRoom) {
        /*
        // players is gotton from WaitRoom
        for (int i = 0; i < playerLabels.length; i++) {
            JLabel playerLabel = playerLabels.get(i);
         
            if (i < players.length) {
                playerLabel.setText(player.getName());
                if (player.getReady()) {
                    playerLabel.setBackground(Color.GREEN);
                }
            } else {
                playerLabel.setText("");
                playerLabel.setBackground(null);
            }
            
        }
         
        // 每次都要判斷display哪個button
        if (IS-HOST) {
            startButton.setVisible(true);
            readyButton.setVisible(false);
        } else {
            startButton.setVisible(false);
            readyButton.setVisible(true);
        }
         
        // only for host
        if (IS-HOST and 人數 == 3 and ALL-PLAYERS-READY) {
            startButton.setEnabled(true);
        }
        */
        revalidate();
        repaint();
    }
    /*
    private void switch() {
        onSwitch.actionPerformed(null);
    }
    */
}
