package com.players;

/**
 * 玩家物件，用於追蹤玩家的名稱、手牌、得分等。
 * 此為簡化版本，日後可加入更多欄位。
 */
public class Player {
    private String name;

    public Player(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
