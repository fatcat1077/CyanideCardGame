package com.game;

import java.util.*;
import com.cards.base.Card;
import com.players.Player;

public class GameController {

    private final GameState serverState;          // 伺服器真正持久狀態
    private final Random    rnd = new Random();

    public GameController(GameState serverState){ this.serverState = serverState; }

    /* ────────── 牌堆 & 輔助 ────────── */
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

    /* ────────── ① commitDealerPlay ────────── */
    public void commitDealerPlay(GameState newState) {

        Player dealer = serverState.getDealer();

        serverState.getDealerChosenCards().addAll(
                newState.getDealerChosenCards());

        for (Card c : newState.getDealerChosenCards()) {
            removeCardById(dealer, c.getCardId());
        }
    }

    /* ────────── ② commitPlayersPlay ────────── */
    public void commitPlayersPlay(GameState newState) {

        Collections.copy(serverState.getTableCards(), newState.getTableCards());

        serverState.getPlayerChosenMap().putAll(newState.getPlayerChosenMap());

        for (var e : newState.getPlayerChosenMap().entrySet()) {
            int cardId   = e.getKey();
            int playerId = e.getValue();
            Player p = serverState.getPlayers().stream()
                    .filter(pl -> pl.getPID() == playerId)     // ★ 改用 getPID()
                    .findFirst()
                    .orElseThrow();
            removeCardById(p, cardId);
        }
    }

    /* ────────── ③ endRound ────────── */
    public void endRound(GameState newState){

        Card winningCard = newState.getWinningCard();
        if(winningCard == null)
            throw new IllegalStateException("newState.winningCard 未設定！");
        serverState.setWinningCard(winningCard);

        Collections.copy(serverState.getTableCards(), newState.getTableCards());
        serverState.getPlayerChosenMap().putAll(newState.getPlayerChosenMap());

        int winCid   = winningCard.getCardId();
        int winnerId = serverState.getPlayerChosenMap()
                                  .getOrDefault(winCid, -1);

        Player winner = serverState.getPlayers().stream()
                .filter(p -> p.getPID() == winnerId)          // ★ 改用 getPID()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("winnerId invalid"));

        winner.addScore(1);

        serverState.getTrash().addAll(serverState.getDealerChosenCards());
        serverState.getTrash().addAll(
            serverState.getTableCards().stream().filter(Objects::nonNull).toList());

        for (Player p : serverState.getPlayers()) {
            p.resetForNewRound();
            drawToFull(p);
        }

        serverState.clearRoundTemp();
        rotateDealer();
    }

    /* ────────── 工具 ────────── */
    private static void removeCardById(Player player, int cardId) {
        // 先印出玩家手牌中的所有 cardId
        System.out.println(">>> " + player.getName() + " 手牌: " +
            player.getHand().stream()
                .map(Card::getCardId)
                .toList());  // Java 16+ 可用 .toList()

        // 再印出欲移除的卡號
        System.out.println(">>> 要移除的卡號: " + cardId);

        // 執行移除
        boolean ok = player.getHand().removeIf(c -> c.getCardId() == cardId);

        // 若移除失敗，再拋出例外
        if (!ok) {
            throw new IllegalStateException(
                player.getName() + " missing Card " + cardId);
        }
    }


    /* CLI 顯示（測試用） */
    public Player pickRandomPlayer(List<Player> cs){ return cs.get(rnd.nextInt(cs.size())); }
    public void banner(String m){ System.out.println("\n========== "+m+" =========="); }
    public void showScores(){
        System.out.println(">>> Score Board <<<");
        serverState.getPlayers().forEach(p->
            System.out.printf(" %-8s %d%n",p.getName(),p.getScore()));
    }
}
