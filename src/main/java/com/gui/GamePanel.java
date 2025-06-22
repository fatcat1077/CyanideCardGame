package com.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import com.cards.base.Card;
import com.game.GameController;
import com.game.GameState;
import com.net.Client.Client;
import com.net.Client.Controller.GameStateController;
import com.net.protocol.enums.PacketType;
import com.players.Player;

import java.util.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class GamePanel extends Panel {
    private List<JLabel> playerLabels = new CopyOnWriteArrayList<>();
    private List<JLabel> scoreLabels = new CopyOnWriteArrayList<>();
    private Point[] cardLocations = new Point[5];
    private Point[] tableLocations = new Point[3];
    private JLabel enlargedCard;
    private JButton confirmButton;
    private JLabel[] cardGrids = new JLabel[3];
    private CardGUI[] cardAt = new CardGUI[3];
    private int placedNumber = 0;
    private PacketType state;
    private boolean isDealer;

    private List<CardGUI> existingCards = new CopyOnWriteArrayList<>();

    private CardGUI currentRaisedCard = null;

    private Client client;
    private GameStateController gsCtrl;

    private Player me;

    public GamePanel(int width, int height, Client client, ActionListener onSwitch) {
        this.client = client;
        this.gsCtrl = client.getGameStateController();
        this.me = client.getPlayer();
        gsCtrl.setUpdateListener(gameState -> update(gameState));

        setBounds(0, 0, width, height);
        setLayout(null);

        enlargedCard = new JLabel();
        enlargedCard.setBounds(160, 100, 480, 270);
        add(enlargedCard, JLayeredPane.PALETTE_LAYER);

        for (int i = 0; i < 3; i++) {
            JLabel playerLabel = new JLabel();
            playerLabel.setBounds(0, 0 + 50 * i, 70, 50);
            playerLabel.setText("player" + i);
            add(playerLabel);
            playerLabels.add(playerLabel);

            JLabel scorLabel = new JLabel();
            scorLabel.setBounds(70, 0 + 50 * i, 30, 50);
            scorLabel.setText("0");
            add(scorLabel);
            scoreLabels.add(scorLabel);
        }
        
        for (int i = 0; i < 5; i++) {
            cardLocations[i] = new Point(420 - 100 * i, 450);
        }

        for (int i = 0; i < 3; i++) {
            tableLocations[i] = new Point(20 + 260 * i, 200);
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
        confirmButton.setEnabled(false);
        add(confirmButton);

        setAction();
    }

    private void setAction() {
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (state == PacketType.DealerChoose) {
                    if (isDealer) {
                        
                        gsCtrl.dealerChoose();
                    } else {
                        // don't care
                    }
                } else if (state == PacketType.PlayerChoose) {
                    ;
                } else if (state == PacketType.DealerRate) {
                    ;
                }
                gsCtrl.dealerChoose();
            }
        });
    }

    public void onCardClicked(CardGUI card) {
        if (card.getCardState() == CardState.IN_HAND) {
            if (placedNumber < 2) {
                if (currentRaisedCard == null) {
                    card.raise();
                    currentRaisedCard = card;
                } else if (currentRaisedCard != card) {
                    currentRaisedCard.back();
                    card.raise();
                    currentRaisedCard = card;
                }
            }
        } else if (card.getCardState() == CardState.RAISED) {
            card.back();
            currentRaisedCard = null;
        } else if (card.getCardState() == CardState.PLACED) {
            if (currentRaisedCard == null) {
                card.back();
                placedNumber--;
            }
        }

        if (placedNumber == 2) {
            confirmButton.setEnabled(true);
        } else {
            confirmButton.setEnabled(false);
        }
    }

    public void onGridClicked(int index) {
        CardGUI card = cardAt[index];
        JLabel grid = cardGrids[index];
        if (currentRaisedCard != null && card == null && placedNumber < 2) {
            currentRaisedCard.place();
            currentRaisedCard.setLocation(grid.getLocation());
            

            gsCtrl.getState().setTableCards(index, currentRaisedCard.getCard());
            gsCtrl.getState().removeCard(client.getPlayer().getPID(), currentRaisedCard.getCard().getCardId());

            currentRaisedCard = null;
            placedNumber++;
        }

        if (placedNumber == 2) {
            confirmButton.setEnabled(true);
        } else {
            confirmButton.setEnabled(false);
        }
    }

    public void update(Object object) {
        for (CardGUI cardGUI : existingCards) {
            remove(cardGUI);
            existingCards.remove(cardGUI);
        }


        GameState newGameState = (GameState) object;
        gsCtrl.setGameState(newGameState);

        isDealer = newGameState.getDealer().getPID() == client.getPlayer().getPID();

        List<Player> players = newGameState.getPlayers();
        for (int i = 0; i < 3; i++) {
            Player player = players.get(i);

            playerLabels.get(i).setText(player.getName());
            scoreLabels.get(i).setText(String.valueOf(player.getScore()));
        }

        //

        Player player = getPlayerFrom(players);
        List<Card> hand = player.getHand();
        for (int i = 0; i < hand.size(); i++) {
            CardGUI c = new CardGUI(hand.get(i), cardLocations[i], enlargedCard);
            c.setClickListener(card -> onCardClicked(card));
            existingCards.add(c);
            add(c, i);
        }

        List<Card> tableCards = gsCtrl.getState().getTableCards();
        for (int i = 0; i < tableCards.size(); i++) {
            Card card = tableCards.get(i);
            if (card == null) continue;

            CardGUI c = new CardGUI(card, tableLocations[i], enlargedCard);
            existingCards.add(c);
            add(c);
        }

        state = newGameState.getState();

        System.out.println("update game panel" + state);
        if (state == PacketType.DealerChoose) {
            if (isDealer) {
                confirmButton.setVisible(true);
            } else {
                confirmButton.setVisible(false);
            }
        } else if (state == PacketType.PlayerChoose) {
            ;
        } else if (state == PacketType.DealerRate) {
            ;
        }

        revalidate();
        repaint();
    }
    /*
    private void switch() {
        onSwitch.actionPerformed(null);
    }
    */

    private Player getPlayerFrom(List<Player> players) {
        for (Player player : players) {
            if (player.getPID() == client.getPlayer().getPID()) {
                return player;
            }
        }
        return null;
    }
}
