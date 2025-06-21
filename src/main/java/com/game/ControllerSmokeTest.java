package com.game;

import java.util.*;
import com.cards.base.Card;
import com.players.Player;

/** 
 * Smoke Test：跑完 1 回合無例外即視為成功 
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

        // 3. 深拷贝伺服器狀態到客戶端
        GameState clientState = new GameState(serverState);

        // 4. 在客戶端根據 PID 找到莊家
        int dealerPid = serverState.getDealer().getPID();
        Player clientDealer = clientState.getPlayers().stream()
            .filter(p -> p.getPID() == dealerPid)
            .findFirst()
            .orElseThrow();
        Random rnd = new Random();

        // (a) 客戶端莊家出兩張牌
        clientState.getDealerChosenCards().add(clientDealer.playRandom(rnd));
        clientState.getDealerChosenCards().add(clientDealer.playRandom(rnd));
        ctrl.commitDealerPlay(clientState);

        // (b) 其他玩家各出一張牌到桌面
        List<Card> table = clientState.getTableCards();
        int idx = 0;
        for (Player p : clientState.getPlayers()) {
            if (p.getPID() == dealerPid) continue;
            Card c = p.playRandom(rnd);
            table.set(idx++, c);
            clientState.getPlayerChosenMap().put(c.getCardId(), p.getPID());
        }
        ctrl.commitPlayersPlay(clientState);

        // (c) 指定獲勝卡
        Card winningCard = table.get(rnd.nextInt(idx));
        clientState.setWinningCard(winningCard);

        // 5. 結束回合
        ctrl.endRound(clientState);

        // 6. 顯示結果
        ctrl.banner("Smoke Test 完成，分數如下");
        ctrl.showScores();
        System.out.println("\u2705  Smoke test passed!  (No exception thrown)");
    }

    private static Player makePlayer(String name, int pid) {
        Player p = new Player(name);
        p.setPID(pid);
        return p;
    }
}
