package com.gui;

import javax.swing.*;

public class Frame extends JFrame {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    
    Panel panel;
    
    public Frame() {
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);

        panel = new HomePanel(WIDTH, HEIGHT, e -> switchToRoom());
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
        ;
    }
}
