package Core;

import org.json.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.EnumMap;

public class Card {

    private String name;
    private CardColor color;
    private int age;
    private int players;
    private EnumMap<CardPoints, Integer> cardPoints;
    private EnumMap<Resource, Integer> resource;
    private EnumMap<Resource, Integer> cost;


    public Card(String name, int players) throws IOException {
        String content = Files.readString(Paths.get("src", "assets", "cards", name + ".json"));
        JSONArray card = new JSONArray(content);

        this.name = name;
        this.players = players;

        this.cardPoints = new EnumMap<>(CardPoints.class);
        this.cardPoints.put(CardPoints.VICTORY, 0);
        this.cardPoints.put(CardPoints.MILITARY, 0);
        this.cardPoints.put(CardPoints.SCIENCE_WHEEL, 0);
        this.cardPoints.put(CardPoints.SCIENCE_TABLET, 0);
        this.cardPoints.put(CardPoints.SCIENCE_COMPASS, 0);

        this.resource = new EnumMap<>(Resource.class);
        this.resource.put(Resource.WOOD, 0);
        this.resource.put(Resource.STONE, 0);
        this.resource.put(Resource.ORE, 0);
        this.resource.put(Resource.CLAY, 0);
        this.resource.put(Resource.GLASS, 0);
        this.resource.put(Resource.LOOM, 0);
        this.resource.put(Resource.PAPYRUS, 0);
        this.resource.put(Resource.COIN, 0);

        this.cost = new EnumMap<>(Resource.class);
        this.cost.put(Resource.WOOD, 0);
        this.cost.put(Resource.STONE, 0);
        this.cost.put(Resource.ORE, 0);
        this.cost.put(Resource.CLAY, 0);
        this.cost.put(Resource.GLASS, 0);
        this.cost.put(Resource.LOOM, 0);
        this.cost.put(Resource.PAPYRUS, 0);
        this.cost.put(Resource.COIN, 0);

        if (card.length() > 0) {
            for (int i = 0; i < card.length(); i++) {
                if (card.getJSONObject(i).getInt("players") <= players ) {

                    // age
                    this.age = card.getJSONObject(i).getInt("age");

                    // value of card = output that the player receives when playing the card
                    if(card.getJSONObject(i).has("cardPoints")) {
                        CardPoints cardPointKey = CardPoints.valueOf(card.getJSONObject(i).getJSONObject("cardPoints").keys().next());
                        Integer cardPointValue = card.getJSONObject(i).getJSONObject("cardPoints").getInt(card.getJSONObject(i).getJSONObject("cardPoints").keys().next());
                        this.cardPoints.put(cardPointKey, cardPointValue);
                    }

                    // cost for a card (if any)
                    if(card.getJSONObject(i).has("cost")) {
                        for (int k = 0; k < card.getJSONObject(i).getJSONObject("cost").names().length(); k++) {
                            String keyStr = card.getJSONObject(i).getJSONObject("cost").names().getString(k);
                            Resource key = Resource.STONE; // Default case
                            int value = card.getJSONObject(i).getJSONObject("cost").getInt(keyStr);

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
                                case "COIN" :
                                    key = Resource.COIN;
                                    break;
                            }

                            this.cost.put(key, value);
                        }
                    }

                    // resource given by a card (if any)
                    if(card.getJSONObject(i).has("resource")) {
                        for (int k = 0; k < card.getJSONObject(i).getJSONObject("resource").names().length(); k++) {

                            String keyStr = card.getJSONObject(i).getJSONObject("resource").names().getString(k);
                            Resource key = Resource.STONE; // Default case
                            int value = card.getJSONObject(i).getJSONObject("resource").getInt(keyStr);

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
                                case "COIN" :
                                    key = Resource.COIN;
                                    break;
                            }

                            this.resource.put(key, value);
                        }
                    }

                    // card color
                    this.color = CardColor.valueOf(card.getJSONObject(i).getString("CardColor"));
                }
            }
        }

    }

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

    public Boolean isFree(){
        for(Integer cost : resource.values())
            if(cost > 0) return false;
        return true;
    }

    @Override
    public String toString() {
        return name;
    }
}
