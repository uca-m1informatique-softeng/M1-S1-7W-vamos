package Core;

import java.io.IOException;
import java.nio.file.*;

import org.json.*;

import java.util.EnumMap;

public class Card {

    public String getName() {
        return name;
    }

    public CardColor getColor() {
        return color;
    }

    public int getAge() {
        return age;
    }

    public int getPlayers() {
        return players;
    }

    public EnumMap<CardPoints, Integer> getCardPoints() {
        return cardPoints;
    }

    public EnumMap<Resource, Integer> getResource() {
        return resource;
    }

    public EnumMap<Resource, Integer> getCost() {
        return cost;
    }

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

        String content = Files.readString(Paths.get("src", "assets", "cards", name + ".json"));
        JSONArray card = new JSONArray(content);

        this.name = name;
        this.players = players;
        this.cardPoints = new EnumMap<>(CardPoints.class);
        this.resource = new EnumMap<>(Resource.class);
        this.cost = new EnumMap<>(Resource.class);

        if (card.length() > 0) {
            for (int i = 0; i < card.length(); i++) {
                if (card.getJSONObject(i).getInt("players") <= players ) {

                    // age
                    this.age = card.getJSONObject(i).getInt("age");

                    // value of card = output that the player receives when playing the card
                    CardPoints cardPointKey = CardPoints.valueOf(card.getJSONObject(i).getJSONObject("cardPoints").keys().next());
                    Integer cardPointValue = card.getJSONObject(i).getJSONObject("cardPoints").getInt(card.getJSONObject(i).getJSONObject("cardPoints").keys().next());
                    this.cardPoints.put(cardPointKey, cardPointValue);

                    // cost for a card (if any)
                    if(card.getJSONObject(i).has("cost")) {
                        for (int k = 0; k < card.getJSONObject(i).getJSONArray("cost").length(); k++) {

                            String keyStr = card.getJSONObject(i).getJSONArray("cost").getJSONObject(k).keys().next();
                            Resource key = Resource.STONE; // Default case
                            int value = card.getJSONObject(i).getJSONArray("cost").getJSONObject(k).getInt(card.getJSONObject(i).getJSONArray("cost").getJSONObject(k).keys().next());

                            switch (keyStr) {
                                case "WOOD" :
                                    key = Resource.WOOD;
                                    break;
                                case "STONE" :
                                    key = Resource.STONE;
                                    break;
                                case "ORE" :
                                    key = Resource.ORE;
                                    break;
                                case "CLAY" :
                                    key = Resource.CLAY;
                                    break;
                                case "GLASS" :
                                    key = Resource.GLASS;
                                    break;
                                case "LOOM" :
                                    key = Resource.LOOM;
                                    break;
                                case "PAPYRUS" :
                                    key = Resource.PAPYRUS;
                                    break;
                            }

                            this.cost.put(key, value);
                        }
                    }

                    // card color
                    this.color = CardColor.valueOf(card.getJSONObject(i).getString("CardColor"));
                }
            }
        }
    }
}