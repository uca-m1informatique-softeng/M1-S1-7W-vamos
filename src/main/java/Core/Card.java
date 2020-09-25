package Core;

import java.io.IOException;
import java.nio.file.*;

import org.json.*;

import java.util.EnumMap;

public class Card {

    private String name;
    private CardColor color;
    private int age;
    private int players;
    private EnumMap<CardPoints, Integer> cardPoints;
    private EnumMap<Resource, Integer> resource;
    private EnumMap<Resource, Integer> cost;

    public Card() {
    }

    public Card(String name, int players) throws IOException {
        String content = Files.readString(Paths.get("src", "assets", "cards", "blue", "altar.json"));
        JSONArray card = new JSONArray(content);
        this.name = name;
        this.players = players;
        if (card.length() > 0) {
            for (int i = 0; i < card.length(); i++) {
                if (card.getJSONObject(i).getInt("players") <= players ) {
                    // age
                    this.age = card.getJSONObject(i).getInt("age");
                    // value of card = output that the player receives when playing the card
                    CardPoints cardPointKey = CardPoints.valueOf(card.getJSONObject(i).getJSONObject("cardPoints").keys().next());
                    Integer cardPointValue = card.getJSONObject(i).getJSONObject("cardPoints").getInt(card.getJSONObject(i).getJSONObject("cardPoints").keys().next());
                    if(cardPointKey != null && cardPointValue != null) {
                        this.cardPoints = new EnumMap<CardPoints, Integer>(CardPoints.class);
                        this.cardPoints.put(cardPointKey, cardPointValue);
                    }
                    // cost for a card (if any)
                    if(card.getJSONObject(i).has("cost")) {
                        for (int k = 0; i < card.getJSONObject(i).getJSONArray("cost").length(); i++) {
                            this.cost.put(Resource.valueOf(card.getJSONObject(i).getJSONArray("cost").getJSONObject(k).keys().next()), card.getJSONObject(i).getJSONArray("cost").getJSONObject(k).getInt(card.getJSONObject(i).getJSONArray("cost").getJSONObject(k).keys().next()));
                        }
                    }
                    // card color
                    this.color = CardColor.valueOf(card.getJSONObject(i).getString("CardColor"));
                }
            }
        }
    }
}
