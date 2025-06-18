package com.cards.base;

import com.game.GameState;
import com.players.Player;
import com.cards.enums.Rarity;

/**
 * 由 JSON 動態產生的卡牌通用子類。
 */
public class CustomCard extends Card {

    public CustomCard(String cardId,
                      String title,
                      String description,
                      Rarity rarity,
                      String imagePath,
                      String captionText) {

        super(cardId, title, description, rarity, imagePath, captionText);
    }

    @Override
    public void onPlay(GameState state, Player owner) {
        // 這裡先簡單示範：之後可依 JSON 欄位擴充各種效果
        System.out.println("玩家 " + owner.getName() +
                           " 打出了卡牌《" + getTitle() + "》！");
    }
}
