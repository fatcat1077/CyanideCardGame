package com.players;

import java.util.ArrayList;
import java.util.List;

import com.cards.base.Card;

/** 玩家資料封裝（邏輯層）。 */
public class Player {

    /* ------------ 基本身份 ------------ */
    private final String name;
    private boolean dealer;     // 是否為莊家

    /* ------------ 遊戲狀態 ------------ */
    private final List<Card> hand = new ArrayList<>();
    private int score = 0;
    private int actions = 1;    // 每回合可出牌數，Cyanide Card Game 規則預設 1

    /* ------------ 建構 ------------ */
    public Player(String name) {
        this.name = name;
    }

    /* ------------ 基本 Getter/Setter ------------ */
    public String  getName()         { return name; }
    public boolean isDealer()        { return dealer; }
    public void    setDealer(boolean dealer) { this.dealer = dealer; }

    public List<Card> getHand()      { return hand; }
    public int  getScore()           { return score; }
    public void addScore(int delta)  { score += delta; }

    public int  getActions()         { return actions; }
    public void resetActions()       { actions = 1; }
    public void useAction()          { actions--; }

    /* ------------ 與手牌相關的便捷方法 ------------ */
    public void draw(Card card)      { hand.add(card); }
    public void play(Card card)      { hand.remove(card); }

    public Card playRandom(java.util.Random rnd) {
    Card c = hand.remove(rnd.nextInt(hand.size()));
    String s= c.getCardId();
    System.out.println(s);
    return c;
    }

    public void resetForNewRound() {           // 每輪行動數歸 1
        actions = 1;
    }

    /* 之後可擴充：生命值、能量、棄牌區等 */
}
