package com.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import com.net.Client.Client;

import java.util.*;

public class GamePanel extends Panel {

    private Client client;

    public GamePanel(int width, int height, ActionListener onSwitch) {

        setBounds(0, 0, width, height);
        setLayout(null);
        
        for (int i = 0; i < 5; i++) {
            Card c = new Card();
            c.setLocation(500 - 100 * i, 400);
            add(c, i);
        }

        setAction();
    }

    private void setAction() {
        ;
    }

    public void update(/* */) {

        ;

        revalidate();
        repaint();
    }
    /*
    private void switch() {
        onSwitch.actionPerformed(null);
    }
    */
}
