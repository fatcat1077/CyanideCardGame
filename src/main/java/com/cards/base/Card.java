package com.cards.base;

import java.util.UUID;
import com.game.GameState;
import com.players.Player;
import com.cards.enums.Rarity;

/**
 * 所有具體卡牌的抽象基底類別，僅處理邏輯層。
 * <p>搭配 cards.json 以資料驅動方式載入：</p>
 * <pre>
 * {
 *   "id": "c001",
 *   "title": "Poop Attack",
 *   "description": "Throw poop at opponent.",
 *   "rarity": "COMMON",
 *   "image": "poop_attack.png",
 *   "caption": "擲：臭臭彈"
 * }
 * </pre>
 */
public abstract class Card {

    /* ---------- 常駐資料欄位 ---------- */
    private final UUID   instanceUuid = UUID.randomUUID(); // 這個要問問瑞倫圖要不要修
    private final int   cardId;      // 對應 JSON 的 id (e.g., "c001")
    private final String title;
    private final String description;
    private final Rarity rarity;
    private final String imagePath;    // e.g., "assets/cards/poop_attack.png"
    private final String captionText;  // e.g., "擲：臭臭彈"

    /* ---------- 建構子 ---------- */
    protected Card(int cardId,
                   String title,
                   String description,
                   Rarity rarity,
                   String imagePath,
                   String captionText) {

        this.cardId      = cardId;
        this.title       = title;
        this.description = description;
        this.rarity      = rarity;
        this.imagePath   = imagePath;
        this.captionText = captionText;
    }
    @Override public boolean equals(Object o){
        return this == o ||
               (o instanceof Card c && this.cardId == c.cardId);
    }
    @Override public int hashCode(){ return Integer.hashCode(cardId); }
    

    /* ---------- Getter ---------- */
    public UUID   getInstanceUuid() { return instanceUuid; }
    public int getCardId()       { return cardId;       }
    public String getTitle()        { return title;        }
    public String getDescription()  { return description;  }
    public Rarity getRarity()       { return rarity;       }
    public String getImagePath()    { return imagePath;    }
    public String getCaptionText()  { return captionText;  }

    /* ---------- 生命週期方法 ---------- */

    /** 打出卡牌時觸發（必須由子類實作） */
    public abstract void onPlay(GameState state, Player owner);

    /** 若卡牌有持續效果，可覆寫此方法；預設為空實作。 */
    public void onTick(GameState state, Player owner) {}

    /** 卡牌離場或效果結束時呼叫；預設為空實作。 */
    public void onRemove(GameState state, Player owner) {}
}
