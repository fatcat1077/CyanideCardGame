package com.game;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import com.cards.base.Card;
import com.cards.base.CardFactory;
import com.players.Player;

/** 只保存「靜態 + 動態」遊戲資料，不控制流程。 */
public class GameState {

    /* ─────── 全域常數（可改讀設定檔） ─────── */
    public static final int    HAND_SIZE = 3;
    public static final int    WIN_SCORE = 4;
    public static final String CARD_JSON = "assets/cards.json";

    /* ─────── 不變資料 ─────── */
    private final Deque<Card> deck  = new ArrayDeque<>();
    private final Deque<Card> trash = new ArrayDeque<>();
    private final List<Player> players;

    /* ─────── 回合級資料 ─────── */
    private Card winningCard = null;
    private final List<Card> dealerChosenCards = new CopyOnWriteArrayList<>();
    private final List<Card> tableCards        = new CopyOnWriteArrayList<>(
            Arrays.asList(new Card[3]));
    private final Map<Integer,Integer> playerChosenMap = new LinkedHashMap<>();
    //private final Map<Player, Card>    playerChosenCard  = new LinkedHashMap<>();
    private int                        dealerIndex       = -1;
    private Player  roundWinner = null;   // 

    /* ─────── 建構 ─────── */
    public GameState(List<Player> players) {
        if (players == null || players.isEmpty())
            throw new IllegalArgumentException("至少要有 1 名玩家才能開局！");
        this.players = players;

        /* 1. 首任莊家 */
        dealerIndex = new Random().nextInt(players.size());
        players.get(dealerIndex).setDealer(true);

        /* 2. 牌堆 */
        List<Card> all = CardFactory.loadCardsFromJson(CARD_JSON);
        Collections.shuffle(all);
        deck.addAll(all);

        /* 3. 發起始牌 */
        for (Player p : players) {
            for (int i = 0; i < HAND_SIZE; i++) p.draw(deck.pop());
        }
    }

    /* ─────── Getter ─────── */
    public Deque<Card>              getDeck()             { return deck; }
    public Deque<Card>              getTrash()            { return trash; }
    public List<Player>             getPlayers()          { return players; }
    public Player                   getDealer()           { return players.get(dealerIndex); }
    public List<Card>               getDealerChosenCards(){ return dealerChosenCards; }
    public List<Card>              getTableCards()       { return tableCards; }
    public Map<Integer,Integer>    getPlayerChosenMap()  { return playerChosenMap; }
    //public Map<Player, Card>        getPlayerChosenCard() { return playerChosenCard; }
   
    public Card getWinningCard()        { return winningCard; }

    public void setWinningCard(Card c)  { this.winningCard = c; }//winning card的setter

    /* ★ 新增回合勝者欄位 */
    public Player getRoundWinner()            { return roundWinner; }
    public void   setRoundWinner(Player p)    { this.roundWinner = p; }

    /* package-private：給 Controller 用 */
    int  getDealerIndex()                { return dealerIndex; }
    void setDealerIndex(int idx)         { dealerIndex = idx; }

    /** 重設「本回合暫存」資料（在 Controller endRound() 會呼叫） */
    void clearRoundTemp() {
        dealerChosenCards.clear();
        Collections.fill(tableCards,null);    // 三格都改回 null
        playerChosenMap.clear();
        winningCard = null;;            
    }
    public GameState(GameState other) {
        // 1. 複製玩家
        this.players = new ArrayList<>();
        for (Player p : other.players) {
            Player cp = new Player(p.getName());
            cp.setPID(p.getPID());
            if (p.isDealer()) cp.setDealer(true);
            cp.addScore(p.getScore());
            // 複製手牌
            for (Card c : p.getHand()) {
                cp.draw(c);
            }
            this.players.add(cp);
        }
        // 2. 複製牌堆與棄牌堆
        this.deck.clear();
        this.deck.addAll(other.getDeck());
        this.trash.clear();
        this.trash.addAll(other.getTrash());
        // 3. 複製回合暫存資料
        this.winningCard = other.getWinningCard();
        this.dealerChosenCards.clear();
        this.dealerChosenCards.addAll(other.getDealerChosenCards());
        this.tableCards.clear();
        this.tableCards.addAll(other.getTableCards());
        this.playerChosenMap.clear();
        this.playerChosenMap.putAll(other.getPlayerChosenMap());
        // 4. 複製索引與回合勝者
        this.dealerIndex = other.getDealerIndex();
        this.roundWinner = other.getRoundWinner();
    }
}
