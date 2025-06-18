package com.cards.base;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.cards.enums.Rarity;

/**
 * 讀取 cards.json 並產生對應的 {@link Card} 物件清單。
 *
 * <p>若 JSON 中的 rarity 字串無法對應列舉，將列印警告並以 {@code COMMON} 取代，
 * 以避免整個程式因 {@link IllegalArgumentException} 而中斷。</p>
 */
public class CardFactory {

    /**
     * @param jsonPath cards.json 檔案路徑
     * @return 轉換完成的卡牌清單；若讀檔或解析失敗則回傳空清單
     */
    public static List<Card> loadCardsFromJson(String jsonPath) {

        List<Card> cards = new ArrayList<>();

        try {
            // 讀取檔案內容（明確指定 UTF-8）
            String content = Files.readString(Path.of(jsonPath), StandardCharsets.UTF_8);
            JSONArray arr = new JSONArray(content);

            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);

                // 解析欄位
                String id          = obj.getString("id");
                String title       = obj.getString("title");
                String description = obj.getString("description");

                // ───── 稀有度：字串 → Enum，若不匹配則預設 COMMON ─────
                String rarityStr = obj.getString("rarity");
                Rarity rarity;
                try {
                    rarity = Rarity.valueOf(rarityStr);
                } catch (IllegalArgumentException e) {
                    System.err.printf("⚠️  cards.json 內的稀有度「%s」無法解析，已改用 COMMON%n", rarityStr);
                    rarity = Rarity.COMMON;
                }

                // 讀圖片檔名並加前綴
                String imagePath   = "assets/cards/" + obj.getString("image");
                String captionText = obj.getString("caption");

                // 建立卡牌並加入清單
                cards.add(new CustomCard(
                        id, title, description, rarity, imagePath, captionText));
            }

        } catch (IOException e) {
            System.err.println("讀取 " + jsonPath + " 失敗：" + e.getMessage());
        } catch (JSONException e) {
            System.err.println("解析 cards.json 時發生錯誤：" + e.getMessage());
        }

        return cards;
    }

    // 若日後需要更多載入選項，可再加其他靜態方法
}
