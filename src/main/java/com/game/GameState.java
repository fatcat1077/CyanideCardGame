package com.game;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.Random;

import com.cards.base.Card;
import com.cards.base.CardFactory;
import com.players.Player;

/** 只保存「靜態 + 動態」遊戲資料，不負責流程控制。 */
public class GameState {

    /* ─────── 全域常數（可視需要改成讀設定檔） ─────── */
    public static final int    HAND_SIZE = 3;                     // 每人手牌上限
    public static final int    WIN_SCORE = 4;                     // 遊戲終止條件
    public static final String CARD_JSON = "assets/cards.json";   // 牌面資訊

    /* ─────── 持久資料 ─────── */
    private final Deque<Card> deck  = new ArrayDeque<>();
    private final Deque<Card> trash = new ArrayDeque<>();
    private final List<Player> players;

    /* ─────── 會改變的資料 ─────── */
    private int dealerIndex = -1;  // 指向莊家players

    //初始化玩家手牌

    
    /* ─────── 建構：完成「載入牌堆」＋「決定莊家」 ─────── */
    public GameState(List<Player> players) {
        if (players == null || players.isEmpty())
            throw new IllegalArgumentException("至少要有 1 名玩家才能開局！");
        this.players = players;

        // 1. 隨機決定首任莊家
        dealerIndex = new Random().nextInt(players.size());
        players.get(dealerIndex).setDealer(true);

        // 2. 初始化牌堆
        List<Card> all = CardFactory.loadCardsFromJson(CARD_JSON);
        Collections.shuffle(all);
        deck.addAll(all);

        // 3. 發起始牌
        for (Player p : players) {
            for (int i = 0; i < HAND_SIZE; i++) {
                p.draw(deck.pop());
            }
        }
    }

    /* ─────── Getter ─────── */
    public Deque<Card>  getDeck()    { return deck; }
    public Deque<Card>  getTrash()   { return trash; }
    public List<Player> getPlayers() { return players; }
    public Player       getDealer()  { return players.get(dealerIndex); }

    /* package-private：給 Controller 更新莊家位置 */
    int  getDealerIndex()        { return dealerIndex; }
    void setDealerIndex(int idx) { dealerIndex = idx; }
}
