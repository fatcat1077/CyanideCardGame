package com.gui;

import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class LobbyPanel extends Panel {
    private JLabel inviteCodeLabel;
    private List<JLabel> playerLabels = new ArrayList<>();
    private ActionListener onSwitch;

    public LobbyPanel(int width, int height, ActionListener onSwitch) {
        this.onSwitch = onSwitch;

        setBounds(0, 0, width, height);

        ;
    }

    private void update() {
        ;
        revalidate();
        repaint();
    }
}
