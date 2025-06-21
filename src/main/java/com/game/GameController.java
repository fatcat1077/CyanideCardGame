package com.game;

import java.util.*;
import com.cards.base.Card;
import com.players.Player;

public class GameController {

    private final GameState serverState;          // 伺服器真正持久狀態
    private final Random    rnd = new Random();

    public GameController(GameState serverState){ this.serverState = serverState; }

    /* ─────────────────────────────
     *  工具：牌堆 & 補牌 & 輪莊
     * ───────────────────────────── */
    private void reloadDeckFromTrash(){
        if(serverState.getTrash().isEmpty()) return;
        List<Card> tmp = new ArrayList<>(serverState.getTrash());
        Collections.shuffle(tmp);
        serverState.getDeck().addAll(tmp);
        serverState.getTrash().clear();
    }
    private Card drawOne(){
        if(serverState.getDeck().isEmpty()) reloadDeckFromTrash();
        if(serverState.getDeck().isEmpty())
            throw new IllegalStateException("no card in deck+trash!");
        return serverState.getDeck().pop();
    }
    private void drawToFull(Player p){
        while(p.getHand().size()<GameState.HAND_SIZE)
            p.draw(drawOne());
    }
    private void rotateDealer(){
        List<Player> ps=serverState.getPlayers();
        int cur=serverState.getDealerIndex();
        ps.get(cur).setDealer(false);
        int nxt=(cur+1)%ps.size();
        ps.get(nxt).setDealer(true);
        serverState.setDealerIndex(nxt);
    }

    /* ─────────────────────────────
     *  ① commitDealerPlay(newState)
     * ───────────────────────────── */
    public void commitDealerPlay(GameState newState) {

        Player dealer = serverState.getDealer();

        // 將新狀態的 dealerChosenCards 複製到 serverState
        //serverState.getDealerChosenCards().clear();
        serverState.getDealerChosenCards()
                .addAll(newState.getDealerChosenCards());

        // 逐張比 cardId 後刪除
        for (Card c : newState.getDealerChosenCards()) {
            removeCardById(dealer, c);
        }
    }

    /* ─────────────────────────────
     *  ② commitPlayersPlay(newState)
     * ───────────────────────────── */
    public void commitPlayersPlay(GameState newState) {

        // 同步暫存欄位
        //serverState.getChosedCardList().clear();
        serverState.getChosedCardList().addAll(newState.getChosedCardList());

        //serverState.getPlayerChosenCard().clear();
        serverState.getPlayerChosenCard().putAll(newState.getPlayerChosenCard());

        // 依 cardId 對每位閒家手牌做 remove
        for (Map.Entry<Player, Card> e : newState.getPlayerChosenCard().entrySet()) {
            removeCardById(e.getKey(), e.getValue());
        }
    }

    /* ─────────────────────────────
     *  ③ endRound(newState, winningCard)
     *      - newState：最新回傳狀態（保險起見再同步一次）
     *      - winningCard：莊家指定的得分卡
     * ───────────────────────────── */
    public void endRound(GameState newState){

        /* 0. 取出 & 同步 winningCard */
        Card winningCard = newState.getWinningCard();
        if(winningCard == null)
            throw new IllegalStateException("newState.winningCard 未設定！");
        serverState.setWinningCard(winningCard);

        /* (保險) 再同步 List / Map，可視情況刪除 */
        serverState.getChosedCardList().clear();
        serverState.getChosedCardList()
                .addAll(newState.getChosedCardList());

        serverState.getPlayerChosenCard().clear();
        serverState.getPlayerChosenCard()
                .putAll(newState.getPlayerChosenCard());

        /* 1. 找得分者 */
        Player winner = serverState.getPlayerChosenCard()
                                .entrySet()
                                .stream()
                                .filter(e -> e.getValue().equals(winningCard))
                                .map(Map.Entry::getKey)
                                .findFirst()
                                .orElseThrow(() ->
                                    new IllegalArgumentException("winningCard 不在 map 中"));

        winner.addScore(1);                 // 2. 計分

        /* 3. 棄牌 */
        serverState.getTrash().addAll(serverState.getDealerChosenCards());
        serverState.getTrash().addAll(serverState.getChosedCardList());

        /* 4. 重設玩家狀態並補牌 */
        for (Player p : serverState.getPlayers()) {
            p.resetForNewRound();
            drawToFull(p);
        }

        /* 5. 清暫存 & 輪莊 */
        serverState.clearRoundTemp();
        rotateDealer();
    }

    /* ─── 輔助 CLI (測試用) ─── */
    public Player pickRandomPlayer(List<Player> cs){ return cs.get(rnd.nextInt(cs.size())); }
    public void banner(String m){ System.out.println("\n========== "+m+" ==========");}
    public void showScores(){
        System.out.println(">>> Score Board <<<");
        serverState.getPlayers().forEach(p->
            System.out.printf(" %-8s %d%n",p.getName(),p.getScore()));
    }
    private static void removeCardById(Player player, Card target) {
        /*System.out.println("-------------");
        for(Card card : player.getHand() ){
            System.out.println(card.getCardId());
        }
        System.out.println("-------------");
        System.out.println(target.getCardId());
        System.out.println("-------------"); */
        boolean removed = player.getHand().removeIf(
            h -> h.getCardId().equals(target.getCardId()));   // ← 透過 getCardId()
        if (!removed) {
            throw new IllegalStateException(
                    player.getName() + " missing Card " + target.getCardId());
        }
    }
}
