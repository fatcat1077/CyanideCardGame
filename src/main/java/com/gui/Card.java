package com.gui;

import java.awt.Color;

import javax.swing.*;

public class Card extends JLabel {
    public Card() {
        setOpaque(true);
        setBackground(Color.CYAN);
        setSize(240, 135);
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
    }
}
