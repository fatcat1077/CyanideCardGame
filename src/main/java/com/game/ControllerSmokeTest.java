package com.game;

import java.util.*;
import com.cards.base.Card;
import com.players.Player;

/** 
 * Smoke Test：跑完多回合無例外即視為成功 
 */
public class ControllerSmokeTest {

    public static void main(String[] args) {
        // 1. 建立伺服器端玩家
        List<Player> serverPlayers = List.of(
            makePlayer("Alice",   1),
            makePlayer("Bob",     2),
            makePlayer("Charlie", 3)
        );

        // 2. 初始化伺服器 GameState 與 Controller
        GameState serverState = new GameState(serverPlayers);
        GameController ctrl  = new GameController(serverState);
        Random rnd = new Random();

        // 設定要跑的回合數
        int rounds = 2;
        for (int r = 1; r <= rounds; r++) {
            System.out.println("\n--- Round " + r + " ---");

            // 深拷貝伺服器狀態到客戶端
            GameState clientState = new GameState(serverState);

            // 在客戶端根據 PID 找到莊家
            int dealerPid = serverState.getDealer().getPID();
            Player clientDealer = clientState.getPlayers().stream()
                .filter(p -> p.getPID() == dealerPid)
                .findFirst()
                .orElseThrow();

            // (a) 客戶端莊家出兩張牌
            clientState.getDealerChosenCards().add(clientDealer.playRandom(rnd));
            clientState.getDealerChosenCards().add(clientDealer.playRandom(rnd));
            //這是測試
            /*for (Card card:serverState.getDealer().getHand()){
                System.out.println("---------------修改前");
                System.out.println(card.getCardId());
                System.out.println("---------------");
            }*/
            ctrl.commitDealerPlay(clientState);
            //下面在測試有沒有正確的修改serverstate
            /*for (Card card:serverState.getDealer().getHand()){
                System.out.println("---------------修改後");
                System.out.println(card.getCardId());
                System.out.println("---------------");
            }*/

            // (b) 其他玩家各出一張牌到桌面
            List<Card> table = clientState.getTableCards();
            int idx = 0;
            for (Player p : clientState.getPlayers()) {
                if (p.getPID() == dealerPid) continue;
                Card c = p.playRandom(rnd);
                table.set(idx++, c);
                clientState.getPlayerChosenMap().put(c.getCardId(), p.getPID());
            }
            //修改之前測試
            /*Map<Integer,Integer> map = clientState.getPlayerChosenMap();
            for (Map.Entry<Integer,Integer> entry : map.entrySet()) {
                Integer cardId   = entry.getKey();
                Integer playerId = entry.getValue();
                System.out.println("修改之前");
                System.out.printf("卡號 %d → 玩家 %d%n", cardId, playerId);
            }*/
            ctrl.commitPlayersPlay(clientState);//神之一行
            //修改之後測試
            /*Map<Integer,Integer> map2 = serverState.getPlayerChosenMap();
            for (Map.Entry<Integer,Integer> entry : map2.entrySet()) {
                Integer cardId   = entry.getKey();
                Integer playerId = entry.getValue();
                System.out.println("修改之後");
                System.out.printf("卡號 %d → 玩家 %d%n", cardId, playerId);
            }*/

            // (c) 指定獲勝卡並結束回合
            Card winningCard = table.get(rnd.nextInt(idx));
            clientState.setWinningCard(winningCard);
            ctrl.endRound(clientState);
        }

        // 顯示最終結果
        ctrl.banner("Smoke Test 完成，最終分數如下");
        ctrl.showScores();
        System.out.println("\u2705  Smoke test passed!  (No exception thrown)");
    }

    private static Player makePlayer(String name, int pid) {
        Player p = new Player(name);
        p.setPID(pid);
        return p;
    }
}
