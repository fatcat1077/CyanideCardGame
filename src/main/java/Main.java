// Main.java －－ 專案啟動入口
// -------------------------------------------
import java.util.List;
import java.util.Random;

import com.cards.base.Card;
import com.cards.base.CardFactory;
import com.game.GameState;
import com.players.Player;

/**
 * 測試用主程式：<br>
 * 1. 從 JSON 載入所有卡牌<br>
 * 2. 列印卡牌資訊<br>
 * 3. 隨機抽一張卡並模擬 onPlay()
 */
public class Main {

    public static void main(String[] args) {

        // 1. 讀取 assets/cards/cards.json
        String jsonPath = "assets/cards.json";
        List<Card> deck = CardFactory.loadCardsFromJson(jsonPath);

        System.out.printf("✅ 已讀取 %d 張卡牌%n", deck.size());
        deck.forEach(c ->
            System.out.printf("  • %-15s | %-6s | %s%n",
                c.getTitle(), c.getRarity(), c.getImagePath())
        );

        // 2. 隨機抽一張卡 & 測試 onPlay()
        if (!deck.isEmpty()) {
            Card randomCard = deck.get(new Random().nextInt(deck.size()));
            System.out.printf("%n🎲 隨機抽中：%s%n", randomCard.getTitle());

            // 建立測試用 GameState / Player（目前僅空殼）
            GameState state = new GameState();
            Player     me   = new Player("Tester");

            randomCard.onPlay(state, me);
        }
    }
}
