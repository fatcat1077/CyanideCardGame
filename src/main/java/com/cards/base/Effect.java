// -----------------------------
// Effect.java
// -----------------------------
package com.cards.base;

import com.game.GameState;
import com.players.Player;

/**
 * 函式式介面：任何可對 GameState 造成改變的行為都可封裝成 Effect。
 * 讓卡牌把 Effect 當作策略物件使用，便於資料驅動（JSON 也能指向 Effect ID）。
 */
@FunctionalInterface
public interface Effect {

    /**
     * 對遊戲狀態進行修改。
     * @param state  當前遊戲狀態快照
     * @param source 觸發此效果的玩家（可能是出牌者或裁判）
     */
    void apply(GameState state, Player source);

    // ───────────────────────── static helpers ────────────────────────────

    /**
     * 讓 lambda / method‑reference 直接轉成 Effect，增進可讀性。
     * <pre>{@code
     * Effect drawTwo = Effect.of((s,p) -> p.drawCards(2));
     * }</pre>
     */
    static Effect of(Effect effect) { return effect; }
}
