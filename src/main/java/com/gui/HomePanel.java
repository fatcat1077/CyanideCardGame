package com.gui;

import java.awt.event.*;
import javax.swing.*;

public class HomePanel extends Panel {
    private JButton startButton;
    private JButton rulesButton;
    private ActionListener onSwitch;
    
    public HomePanel(int width, int height, ActionListener onSwitch) {
        this.onSwitch = onSwitch;

        setBounds(0, 0, width, height);

        startButton = new JButton();
        startButton.setBounds(200, 200, 400, 50);
        startButton.setText("Start");
        add(startButton);

        rulesButton = new JButton();
        rulesButton.setBounds(200, 300, 400, 50);
        rulesButton.setText("Rules");
        add(rulesButton);

        setAction();
    }

    private void setAction() {
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onSwitch.actionPerformed(null);
            }
        });

        rulesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ;
            }
        });
    }
}
