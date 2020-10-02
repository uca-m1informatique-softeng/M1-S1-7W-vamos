package Card;

import org.json.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.EnumMap;

public class Card {

    private String name;
    private CardColor color;
    private CardColor coloredCardResourceEffect;
    private CardColor coinCardEffect;
    private int age;
    private int players;
    private Effect effect;
    private EnumMap<CardPoints, Integer> cardPoints;
    private EnumMap<Resource, Integer> resource;
    private EnumMap<Resource, Integer> cost;


    public Card(String name, int players) throws IOException {
        String content = Files.readString(Paths.get("src", "assets", "cards", name + ".json"));
        JSONObject card = new JSONObject(content);

        this.name = name;
        this.players = players;

        this.cardPoints = new EnumMap<>(CardPoints.class);
        for (CardPoints p : CardPoints.values()) {
            this.cardPoints.put(p, 0);
        }
        this.resource = new EnumMap<>(Resource.class);
        for (Resource r : Resource.values()) {
            this.resource.put(r, 0);
        }
        this.cost = new EnumMap<>(Resource.class);
        for (Resource r : Resource.values()) {
            this.cost.put(r, 0);
        }

        //if (card.length() > 0) {
        //for (int i = 0; i < card.length(); i++) {
        Boolean validPlayerNb = false;
        if (card.has("players")) {
            for (int i = 0; i < card.getJSONArray("players").length(); i++) {
                if (card.getJSONArray("players").getInt(i) <= players) {
                    validPlayerNb = true;
                }
            }
        }
        if (validPlayerNb || !card.has("players")) {
            // age
            this.age = card.getInt("age");

            // value of card = output that the player receives when playing the card
            if (card.has("cardPoints")) {
                CardPoints cardPointKey = CardPoints.valueOf(card.getJSONObject("cardPoints").keys().next());
                Integer cardPointValue = card.getJSONObject("cardPoints").getInt(card.getJSONObject("cardPoints").keys().next());
                this.cardPoints.put(cardPointKey, cardPointValue);
            }

            // cost for a card (if any)
            if (card.has("cost")) {
                for (int k = 0; k < card.getJSONObject("cost").names().length(); k++) {
                    String key = card.getJSONObject("cost").names().getString(k);
                    int value = card.getJSONObject("cost").getInt(key);

                    this.cost.put(Resource.valueOf(key), value);
                }
            }

            // resource given by a card (if any)
            if (card.has("resource")) {
                for (int k = 0; k < card.getJSONObject("resource").names().length(); k++) {

                    String key = card.getJSONObject("resource").names().getString(k);
                    int value = card.getJSONObject("resource").getInt(key);

                    this.resource.put(Resource.valueOf(key), value);
                }
            }

            // card color
            this.color = CardColor.valueOf(card.getString("CardColor"));

            if (card.has("resourceChoiceEffect")) {

                JSONArray resourceChoiceEffect = card.getJSONArray("resourceChoiceEffect");
                ArrayList<Resource> resList = new ArrayList<>();

                for (int k = 0; k < resourceChoiceEffect.length(); k++) {
                    resList.add(Resource.valueOf(resourceChoiceEffect.getString(k)));
                }

                this.effect = new ResourceChoiceEffect(resList);
            }

            if (card.has("tradeResourceEffect")) {

                JSONObject tradeResourceEffect = card.getJSONObject("tradeResourceEffect");
                ArrayList<Resource> resList = new ArrayList<>();

                for (int k = 0; k < tradeResourceEffect.length(); k++) {
                    resList.add(Resource.valueOf(tradeResourceEffect.getJSONArray("resourcesModified").getString(k)));
                }

                this.effect = new TradeResourceEffect(
                        tradeResourceEffect.getBoolean("prevPlayerAllowed"),
                        tradeResourceEffect.getBoolean("nextPlayerAllowed"),
                        resList);
            }

            if (card.has("coloredCardResourceEffect")) {
                this.coloredCardResourceEffect = CardColor.valueOf(card.getString("coloredCardResourceEffect"));

                CardColor coloredCardResourceEffect = CardColor.valueOf(card.getString("coloredCardResourceEffect"));

                this.effect = new ColoredCardResourceEffect(coloredCardResourceEffect);
            }

            if (card.has("ShipownersGuildEffect")) {
                this.effect = new ShipOwnersGuild();
            }

            if (card.has("BuildersGuildEffect")) {
                this.effect = new BuildersGuildEffect();
            }

            if (card.has("StrategistsGuildEffect")) {
                this.effect = new StrategistsGuildEffect();
            }
            if (card.has("coinCardEffect")) {
                if (card.get("coinCardEffect").equals("PYRAMID")) {
                    this.coinCardEffect = null;
                    this.effect = new CoinCardEffect(null, card.getInt("age"));
                } else {
                    this.coinCardEffect = CardColor.valueOf(card.getString("coinCardEffect"));
                    this.effect = new CoinCardEffect(CardColor.valueOf(card.getString("coinCardEffect")), card.getInt("age"));
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

    public CardColor getColoredCardResourceEffect() {
        return coloredCardResourceEffect;
    }

    public CardColor getCoinCardEffect() {
        return coinCardEffect;
    }

    public int getAge() {
        return age;
    }

    public int getPlayers() {
        return players;
    }

    public Effect getEffect() {
        return this.effect;
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

    public Boolean isFree() {
        for (Integer cost : cost.values())
            if (cost > 0) return false;
        return true;
    }

    @Override
    public String toString() {
        return name;
    }
}
