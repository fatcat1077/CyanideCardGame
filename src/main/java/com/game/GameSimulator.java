package com.game;

import java.util.*;
import com.cards.base.Card;
import com.players.Player;

public class GameSimulator {

    private final GameState      serverState;
    private final GameController ctrl;
    private final Random         rnd = new Random();

    public static void main(String[] args){
        List<Player> players = List.of(
                new Player("Alice"),
                new Player("Bob"),
                new Player("Charlie"));
        new GameSimulator(players).run();
    }

    public GameSimulator(List<Player> sharedPlayers){
        serverState = new GameState(sharedPlayers);     // 伺服器端
        ctrl        = new GameController(serverState);
    }

    public void run(){
        int round=1;
        while(true){
            ctrl.banner("Round "+round);
            ctrl.showScores();

            /* ★ 模擬「客戶端」也持有同組 Player 物件 */
            GameState clientState = new GameState(serverState.getPlayers());

            /* 1. Dealer 選兩張 → 塞進 clientState */
            Player dealer = clientState.getDealer();         // 同一參考
            clientState.getDealerChosenCards().add(dealer.playRandom(rnd));
            clientState.getDealerChosenCards().add(dealer.playRandom(rnd));

            /* 伺服器同步 */
            ctrl.commitDealerPlay(clientState);

            /* 2. 兩位閒家選牌 */
            List<Card> list = new ArrayList<>();
            Map<Player,Card> map = new LinkedHashMap<>();
            for(Player p: clientState.getPlayers()){
                if(p==dealer) continue;
                Card c = p.playRandom(rnd);
                list.add(c);
                map.put(p,c);
            }
            clientState.getChosedCardList().addAll(list);
            clientState.getPlayerChosenCard().putAll(map);

            ctrl.commitPlayersPlay(clientState);

            /* 3. Dealer 指定得分牌 */
            Card winningCard = list.get(rnd.nextInt(list.size()));
            clientState.setWinningCard(winningCard);

            /* endRound */
            ctrl.endRound(clientState);  
            ctrl.showScores();

            /* 4. 判斷結束 */
            Player winner = map.entrySet().stream()
                               .filter(e->e.getValue().equals(winningCard))
                               .map(Map.Entry::getKey).findFirst().get();
            if(winner.getScore()>=GameState.WIN_SCORE){
                ctrl.banner("🏆 Winner: "+winner.getName());
                break;
            }
            round++;
        }
    }
}
