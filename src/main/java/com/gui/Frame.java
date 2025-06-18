package com.gui;

import java.awt.Dimension;

import javax.swing.*;

public class Frame extends JFrame {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    
    Panel panel;
    
    public Frame() {
        getContentPane().setPreferredSize(new Dimension(WIDTH, HEIGHT));
        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //panel = new HomePanel(WIDTH, HEIGHT, e -> switchToRoom());
        panel = new LobbyPanel(WIDTH, HEIGHT, null);
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
        remove(panel);
        revalidate();
        repaint();

        panel = new LobbyPanel(WIDTH, HEIGHT, e -> switchToLobby());
        add(panel);
    }

    public void switchToGame() {
        ;
        revalidate();
        repaint();

    }
}
