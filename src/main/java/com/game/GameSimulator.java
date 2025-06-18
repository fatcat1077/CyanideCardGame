package com.game;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.cards.base.Card;
import com.players.Player;

public class GameSimulator {

    private final GameState      state;
    private final GameController ctrl;
    private final Random         rnd = new Random();

    /* ------------ 進入點 ------------ */
    public static void main(String[] args) {
        List<Player> players = List.of(
                new Player("Alice"),
                new Player("Bob"),
                new Player("Charlie"));
        new GameSimulator(players).play();
    }

    public GameSimulator(List<Player> players) {
        this.state = new GameState(players);      // 內部已完成牌堆初始化
        this.ctrl  = new GameController(state);
    }

    /* ------------ 遊戲主迴圈 ------------ */
    public void play() {

        /* 起始牌：每位玩家補到滿手 */
        //state.getPlayers().forEach(ctrl::drawToFull);

        int round = 1;

        while (true) {
            ctrl.banner("第 " + round + " 輪開始");
            ctrl.showScoresAndHands();

            /* ─── 1. 出牌 ─── */
            Player dealer = state.getDealer();
            System.out.println("[莊家] " + dealer.getName() + " 隨機打 2 張…");
            List<Card> dealerPlays = List.of(
                    dealer.playRandom(rnd),
                    dealer.playRandom(rnd));
            System.out.println("  → 莊家出了：" + dealerPlays.get(0).getTitle()
                    + "、" + dealerPlays.get(1).getTitle());

            Map<Player, Card> table = new LinkedHashMap<>();
            for (Player p : state.getPlayers()) {
                if (p == dealer) continue;
                Card c = p.playRandom(rnd);
                table.put(p, c);
                System.out.printf("  → 閒家 %-8s 出了：%s%n",
                        p.getName(), c.getTitle());
            }

            /* ─── 2. 莊家選得分者 ─── */
            System.out.print("[莊家] 從閒家中選一位得分… ");
            Player winner = ctrl.pickRandomPlayer(
                                table.keySet().stream().toList());
            winner.addScore(1);
            System.out.printf("%s 得 1 分！%n", winner.getName());

            ctrl.showScores();

            /* ─── 3. 勝負判定 ─── */
            if (winner.getScore() >= GameState.WIN_SCORE) {
                ctrl.banner("🏆 遊戲結束！獲勝者：" + winner.getName());
                break;
            }

            /* ─── 4. 結束收尾 ─── */
            ctrl.rotateDealer();
            state.getPlayers().forEach(p -> {
                p.resetForNewRound();
                ctrl.drawToFull(p);
            });
            round++;
        }
    }
}
