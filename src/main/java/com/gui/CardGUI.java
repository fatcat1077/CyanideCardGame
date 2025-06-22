package com.gui;

import java.awt.Color;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.*;

import javax.swing.*;

import com.cards.base.Card;

public class CardGUI extends JLabel {
    private Card card;
    private ImageIcon originalImage;
    private ImageIcon image;
    private CardState cardState = CardState.IN_HAND;
    private Point origin;

    private JLabel enlargedCard;

    private CardClickListener clickListener;

    int factor = 15;
    int width = 16 * factor;
    int height = 9 * factor;

    public CardGUI(Card card, Point origin, JLabel enlargedCard) {
        this.card = card;
        this.origin = origin;
        setLocation(origin);
        this.enlargedCard = enlargedCard;

        setOpaque(true);
        setBackground(Color.CYAN);
        setSize(width, height);
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        setCardImage();

        setAction();
    }

    private void setCardImage() {
        originalImage = new ImageIcon(/*card.getImagePath() */"C:\\Users\\user\\Downloads\\image\\mygo01.jpg");
        Image scaledImage = originalImage.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        this.image = new ImageIcon(scaledImage);
        setIcon(image);
    }

    private void setAction() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (cardState != CardState.PLACED) {
                    ImageIcon enlargedImage = new ImageIcon(originalImage.getImage().getScaledInstance(480, 270, Image.SCALE_SMOOTH));
                    enlargedCard.setIcon(enlargedImage);
                    enlargedCard.setVisible(true);
                }
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                enlargedCard.setVisible(false);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                clickListener.onCardClicked(CardGUI.this);
            }
        });
            
    }

    public void setClickListener(CardClickListener listener) {
        this.clickListener = listener;
    }

    public void raise() {
        int x = getX();
        int y = getY();
        setLocation(x, y - 50);
        cardState = CardState.RAISED;
    }

    public void back() {
        setLocation(origin);
        cardState = CardState.IN_HAND;
    }

    public void place() {
        cardState = CardState.PLACED;
    }

    public CardState getCardState() {
        return cardState;
    }

    public Card getCard() {
        return card;
    }
}
