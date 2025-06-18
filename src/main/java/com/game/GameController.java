package com.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.cards.base.Card;
import com.players.Player;

public class GameController {

    private final GameState state;
    private final Random    rnd = new Random();

    public GameController(GameState state) {
        this.state = state;
    }

    /* ────────── 基本行為 ────────── */

    /** 從牌堆抽 1 張；若牌堆空則嘗試把棄牌堆洗回來 */
    public Card drawOne() {
        if (state.getDeck().isEmpty())
            reloadDeckFromTrash();
        if (state.getDeck().isEmpty())
            throw new IllegalStateException("牌堆與棄牌堆都沒牌可抽！");
        return state.getDeck().pop();
    }

    /** 把棄牌堆洗回牌堆 */
    private void reloadDeckFromTrash() {
        if (state.getTrash().isEmpty()) return;
        List<Card> tmp = new ArrayList<>(state.getTrash());
        Collections.shuffle(tmp);
        state.getDeck().addAll(tmp);
        state.getTrash().clear();
    }

    /** 讓指定玩家手牌補到 GameState.HAND_SIZE 張 */
    public void drawToFull(Player p) {
        while (p.getHand().size() < GameState.HAND_SIZE)
            p.draw(drawOne());
    }

    /** 依順序輪到下一位玩家當莊家 */
    public void rotateDealer() {
        List<Player> ps = state.getPlayers();
        int curIdx = state.getDealerIndex();
        ps.get(curIdx).setDealer(false);

        int nextIdx = (curIdx + 1) % ps.size();
        ps.get(nextIdx).setDealer(true);
        state.setDealerIndex(nextIdx);
    }

    /** 從 candidates 中亂數挑一名（可改 UI 決定） */
    public Player pickRandomPlayer(List<Player> candidates) {
        return candidates.get(rnd.nextInt(candidates.size()));
    }

    /* ────────── CLI 列印工具 ────────── */

    public void banner(String msg) {
        System.out.println("\n========== " + msg + " ==========");
    }

    /** 顯示所有玩家分數 + 手牌 */
    public void showScoresAndHands() {
        for (Player player : state.getPlayers()) {
            System.out.printf("‧ %-8s 分數=%d%s%n",
                    player.getName(), player.getScore(),
                    player.isDealer() ? "（莊家）" : "");
            System.out.println("   手牌：" + titles(player.getHand()));
        }
    }

    /** 只顯示分數板 */
    public void showScores() {
        System.out.println(">>> 目前分數板 <<<");
        state.getPlayers().forEach(p ->
            System.out.printf("   %-8s %d 分%n", p.getName(), p.getScore()));
    }

    private String titles(Iterable<Card> cards) {
        StringBuilder sb = new StringBuilder();
        for (Card c : cards) {
            if (sb.length() > 0) sb.append('、');
            sb.append(c.getTitle());
        }
        return sb.toString();
    }
}
