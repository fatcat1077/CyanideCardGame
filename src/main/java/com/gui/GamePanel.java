package com.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import com.net.Client.Client;

import java.util.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class GamePanel extends Panel {
    private List<JLabel> playerLabels = new CopyOnWriteArrayList<>();
    private List<JLabel> scoreLabels = new CopyOnWriteArrayList<>();
    private JLabel enlargedCard;
    private JButton confirmButton;
    private JLabel[] cardGrids = new JLabel[3];
    private CardGUI[] cardAt = new CardGUI[3];
    private int placedNumber = 0;

    private CardGUI currentRaisedCard = null;

    private Client client;

    public GamePanel(int width, int height, /* Client client, GameState gameState, */ ActionListener onSwitch) {
        // this.client = client;

        setBounds(0, 0, width, height);
        setLayout(null);

        enlargedCard = new JLabel();
        enlargedCard.setBounds(160, 100, 480, 270);
        add(enlargedCard, JLayeredPane.PALETTE_LAYER);

        // List<Player> players = gameState.getPlayers();
        for (int i = 0; i < 3; i++) {
            JLabel playerLabel = new JLabel();
            playerLabel.setBounds(0, 0 + 50 * i, 70, 50);
            playerLabel.setText("player" + i);
            playerLabel.setOpaque(true);
            add(playerLabel);
            playerLabels.add(playerLabel);

            JLabel scorLabel = new JLabel();
            scorLabel.setBounds(70, 0 + 50 * i, 30, 50);
            scorLabel.setText("0");
            scorLabel.setOpaque(true);
            add(scorLabel);
            scoreLabels.add(scorLabel);
        }
        
        for (int i = 0; i < 5; i++) {
            Point origin = new Point(420 - 100 * i, 450);
            CardGUI c = new CardGUI(null, origin, enlargedCard);
            c.setClickListener(card -> onCardClicked(card));
            add(c, i);
        }

        for (int i = 0; i < 3; i++) {
            int index = i;
            JLabel cardGrid = new JLabel();
            cardGrid.setBounds(20 + 260 * i, 200, 240, 135);
            cardGrid.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            cardGrid.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    onGridClicked(index);
                }
            });
            add(cardGrid);
            cardGrids[i] = cardGrid;
        }

        confirmButton = new JButton();
        confirmButton.setBounds(680, 500, 100, 50);
        confirmButton.setText("Confirm");
        add(confirmButton);

        setAction();
    }

    private void setAction() {
        ;
    }

    public void onCardClicked(CardGUI card) {
        if (card.getCardState() == CardState.IN_HAND) {
            if (currentRaisedCard == null) {
                card.raise();
                currentRaisedCard = card;
            } else if (currentRaisedCard != card) {
                currentRaisedCard.back();
                card.raise();
                currentRaisedCard = card;
            }
        } else if (card.getCardState() == CardState.RAISED) {
            card.back();
            currentRaisedCard = null;
        } else {
            if (currentRaisedCard == null) {
                card.back();
                placedNumber--;
            }
        }
    }

    public void onGridClicked(int index) {
        CardGUI card = cardAt[index];
        JLabel grid = cardGrids[index];
        if (currentRaisedCard != null && card == null && placedNumber < 2) {
            currentRaisedCard.place();
            currentRaisedCard.setLocation(grid.getLocation());
            currentRaisedCard = null;
            placedNumber++;
        }
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
