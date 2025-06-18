package com.game;

import java.util.*;
import com.cards.base.*;
import com.players.Player;

public class GameSimulator {

    private static final int HAND_SIZE = 3;
    private static final int WIN_SCORE = 4;
    private static final String JSON = "assets/cards.json";

    private final GameState gs = new GameState();
    private final Random rnd = new Random();

    public static void main(String[] args) { 
        System.out.println("hello");
        new GameSimulator().play(); 
    }

    public void play() {

        // ① 準備牌堆 & 起始手牌
        gs.initDeck(CardFactory.loadCardsFromJson(JSON));
        gs.getPlayers().forEach(this::drawToFull);

        int round = 1;

        while (true) {
            banner("第 " + round + " 輪開始");

            showScoresAndHands();

            // ② 出牌
            Player dealer = gs.getDealer();
            System.out.println("[莊家] " + dealer.getName() + " 行動：從手牌選兩張…");
            List<Card> dealerPlays = List.of(dealer.playRandom(rnd), dealer.playRandom(rnd));
            System.out.println("  → 莊家出了：" + titles(dealerPlays));

            Map<Player, Card> table = new LinkedHashMap<>();
            for (Player p : gs.getPlayers()) {
                if (p == dealer) continue;
                Card c = p.playRandom(rnd);
                table.put(p, c);
                System.out.printf("  → 閒家 %-8s 出了：%s%n", p.getName(), c.getTitle());
            }

            // ③ 莊家決定得分者
            System.out.print("[莊家] 從閒家中選一位得分… ");
            Player winner = pickWinner(dealer, table.keySet());
            winner.addScore(1);
            System.out.printf("%s 得 1 分！%n", winner.getName());

            showScores();

            // ④ 判定勝負
            if (winner.getScore() >= WIN_SCORE) {
                banner("🏆 遊戲結束！獲勝者：" + winner.getName());
                break;
            }

            // ⑤ 補牌 & 輪莊
            rotateDealer();
            gs.getPlayers().forEach(p -> { p.resetForNewRound(); drawToFull(p); });
            round++;
        }
    }

    /* ────────────── 輔助 ────────────── */

    private void drawToFull(Player p) {
        while (p.getHand().size() < HAND_SIZE) p.draw(gs.drawOne());
    }

    private void rotateDealer() {
        List<Player> ps = gs.getPlayers();
        int idx = ps.indexOf(gs.getDealer());
        ps.get(idx).setDealer(false);
        ps.get((idx + 1) % ps.size()).setDealer(true);
    }

    private Player pickWinner(Player dealer, Set<Player> nonDealers) {
        // 目前採隨機；日後可改 UI 讓莊家手動挑
        return nonDealers.stream().skip(rnd.nextInt(nonDealers.size())).findFirst().get();
    }

    private void showScoresAndHands() {
        for (Player p : gs.getPlayers()) {
            System.out.printf("‧ %-8s 分數=%d%s%n",
                    p.getName(), p.getScore(),
                    p.isDealer() ? "（莊家）" : "");
            System.out.println("   手牌：" + titles(p.getHand()));
        }
    }

    private void showScores() {
        System.out.println(">>> 目前分數板 <<<");
        gs.getPlayers().forEach(p ->
            System.out.printf("   %-8s %d 分%n", p.getName(), p.getScore()));
    }

    private String titles(Collection<Card> cards) {
        return String.join("、", cards.stream().map(Card::getTitle).toList());
    }

    private void banner(String msg) {
        System.out.println("\n========== " + msg + " ==========");
    }
}
