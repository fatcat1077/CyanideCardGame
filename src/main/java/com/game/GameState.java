package com.game;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Random;

import com.cards.base.Card;
import com.players.Player;

/** 持有整局遊戲的動態資料。 */
public class GameState {

    /* ------------ 核心區域 ------------ */
    private final Deque<Card> deck  = new ArrayDeque<>();
    private final Deque<Card> trash = new ArrayDeque<>();
    private final List<Player> players = new ArrayList<>();

    /* 用索引記錄現任莊家；永遠指向 players List 內的物件 */
    private int dealerIndex = -1;

    /* ------------ 建構：一次建立 3 名玩家並隨機決定莊家 ------------ */
    public GameState() {
        players.add(new Player("Alice"));
        players.add(new Player("Bob"));
        players.add(new Player("Charlie"));

        dealerIndex = new Random().nextInt(players.size());
        players.get(dealerIndex).setDealer(true);
    }

    /* ------------ Getter ------------ */
    public Deque<Card>  getDeck()        { return deck; }
    public Deque<Card>  getTrash()       { return trash; }
    public List<Player> getPlayers()     { return players; }
    public Player       getDealer()      { return players.get(dealerIndex); }

    /* 之後可加入：當前回合編號、階段、勝負判定等 */
    public void initDeck(java.util.List<Card> allCards) {
    java.util.Collections.shuffle(allCards);
    deck.addAll(allCards);
    }

    public Card drawOne() {
        if (deck.isEmpty()) throw new IllegalStateException("牌堆抽光了！");
        return deck.pop();
    }

}
