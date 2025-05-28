package com.cards.base;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import org.json.*;

import com.cards.enums.Rarity;

public class CardFactory {

    public static List<Card> loadCardsFromJson(String jsonPath) {
        List<Card> cards = new ArrayList<>();
        try {
            String content = Files.readString(Path.of(jsonPath));
            JSONArray arr = new JSONArray(content);

            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);

                String id = obj.getString("id");
                String title = obj.getString("title");
                String description = obj.getString("description");
                Rarity rarity = Rarity.valueOf(obj.getString("rarity"));
                String imagePath = "assets/cards/" + obj.getString("image");
                String captionText = obj.getString("caption");

                Card card = new CustomCard(id, title, description, rarity, imagePath, captionText);
                cards.add(card);
            }

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return cards;
    }
}
