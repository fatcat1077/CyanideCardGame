package com.gui;

import javax.swing.*;

import com.net.Client.Client;

public class Frame extends JFrame {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    
    Panel panel;
    Client client;
    
    public Frame() {
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);

        //panel = new HomePanel(WIDTH, HEIGHT, e -> switchToRoom());
        panel = new GamePanel(WIDTH, HEIGHT, null);
        add(panel);
    }

    public void switchToRoom() {
        remove(panel);
        revalidate();
        repaint();
        
        panel = new RoomPanel(WIDTH, HEIGHT, e -> switchToLobby());
        add(panel);
    }

    public void switchToLobby() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        client = ((RoomPanel) panel).getGeneratedClient();

        remove(panel);
        revalidate();
        repaint();
        panel = new LobbyPanel(WIDTH, HEIGHT, client, e -> switchToGame());
        add(panel);
    }

    public void switchToGame() {
        remove(panel);
        revalidate();
        repaint();

        // panel = new GamePanel(WIDTH, HEIGHT, client, e -> switchToEnd());
        // add(panel);

    }
}
