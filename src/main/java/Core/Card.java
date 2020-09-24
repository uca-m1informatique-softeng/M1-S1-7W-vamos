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

    public Card(String name, int players, CardColor color) throws IOException {
        String content = Files.readString(Paths.get("src", "assets", "cards", "blue", "altar.json"));
        JSONArray card = new JSONArray(content);
        this.name = name;
        this.players = players;
        this.color = color;
        if (card.length() > 0) {
            for (int i = 0; i < card.length(); i++) {
                if (card.getJSONObject(i).getInt("players") == players) {
                    this.age = card.getJSONObject(i).getInt("age");
                    this.cardPoints.put(CardPoints.valueOf(card.getJSONObject(i).getJSONObject("cardPoints").keys().next()), card.getJSONObject(i).getJSONObject("cardPoints").getInt(card.getJSONObject(i).getJSONObject("cardPoints").keys().next()));
                    for (int k = 0; i < card.getJSONObject(i).getJSONArray("cost").length(); i++) {
                        this.cost.put(Resource.valueOf(card.getJSONObject(i).getJSONArray("cost").getJSONObject(k).keys().next()), card.getJSONObject(i).getJSONArray("cost").getJSONObject(k).getInt(card.getJSONObject(i).getJSONArray("cost").getJSONObject(k).keys().next()));
                    }
                }
            }
        }
    }
}
