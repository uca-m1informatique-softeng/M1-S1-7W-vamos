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
    private CardColor coloredCardResourceEffect ;
    private CardColor coinCardEffect;
    private int age;
    private int players;
    private Effect effect;
    private EnumMap<CardPoints, Integer> cardPoints;
    private EnumMap<Resource, Integer> resource;
    private EnumMap<Resource, Integer> cost;


    public Card(String name, int players) throws IOException {
        String content = Files.readString(Paths.get("Commun","src", "assets", "cards", name + ".json"));
        JSONArray card = new JSONArray(content);

        this.name = name;
        this.players = players;

        this.cardPoints = new EnumMap<>(CardPoints.class);
        for (CardPoints p : CardPoints.values()) {
            this.cardPoints.put(p , 0);
        }
        this.resource = new EnumMap<>(Resource.class);
        for (Resource r : Resource.values()) {
            this.resource.put(r , 0);
        }
        this.cost = new EnumMap<>(Resource.class);
        for (Resource r : Resource.values()) {
            this.cost.put(r , 0);
        }

        if (card.length() > 0) {
            for (int i = 0; i < card.length(); i++) {

                JSONObject json = card.getJSONObject(i);

                if ((json.has("players") && json.getInt("players") <= players) || !json.has("players")) {
                    // age
                    this.age = json.getInt("age");

                    // value of card = output that the player receives when playing the card
                    if(json.has("cardPoints")) {
                        CardPoints cardPointKey = CardPoints.valueOf(json.getJSONObject("cardPoints").keys().next());
                        Integer cardPointValue = json.getJSONObject("cardPoints").getInt(json.getJSONObject("cardPoints").keys().next());
                        this.cardPoints.put(cardPointKey, cardPointValue);
                    }

                    // cost for a card (if any)
                    if(json.has("cost")) {
                        for (int k = 0; k < json.getJSONObject("cost").names().length(); k++) {
                            String key = json.getJSONObject("cost").names().getString(k);
                            int value = json.getJSONObject("cost").getInt(key);

                            this.cost.put(Resource.valueOf(key), value);
                        }
                    }

                    // resource given by a card (if any)
                    if(json.has("resource")) {
                        for (int k = 0; k < json.getJSONObject("resource").names().length(); k++) {

                            String key = json.getJSONObject("resource").names().getString(k);
                            int value = json.getJSONObject("resource").getInt(key);

                            this.resource.put(Resource.valueOf(key), value);
                        }
                    }

                    // card color
                    this.color = CardColor.valueOf(json.getString("CardColor"));

                    if (json.has("resourceChoiceEffect")) {

                        JSONArray resourceChoiceEffect = json.getJSONArray("resourceChoiceEffect");
                        ArrayList<Resource> resList = new ArrayList<>();

                        for (int k = 0; k < resourceChoiceEffect.length(); k++) {
                            resList.add(Resource.valueOf(resourceChoiceEffect.getString(k)));
                        }

                        this.effect = new ResourceChoiceEffect(resList);
                    }

                    if (json.has("tradeResourceEffect")) {

                        JSONObject tradeResourceEffect = json.getJSONObject("tradeResourceEffect");
                        ArrayList<Resource> resList = new ArrayList<>();

                        for (int k = 0; k < tradeResourceEffect.length(); k++) {
                            resList.add(Resource.valueOf(tradeResourceEffect.getJSONArray("resourcesModified").getString(k)));
                        }

                        this.effect = new TradeResourceEffect(
                                tradeResourceEffect.getBoolean("prevPlayerAllowed"),
                                tradeResourceEffect.getBoolean("nextPlayerAllowed"),
                                resList);
                    }

                    if(json.has("coloredCardResourceEffect")){
                        this.coloredCardResourceEffect=CardColor.valueOf(json.getString("coloredCardResourceEffect")) ;

                        CardColor coloredCardResourceEffect = CardColor.valueOf(json.getString("coloredCardResourceEffect"));

                        this.effect = new ColoredCardResourceEffect(coloredCardResourceEffect) ;
                    }

                    if (json.has("ShipownersGuildEffect")){
                        this.effect= new ShipOwnersGuild() ;
                    }

                    if (json.has("BuildersGuildEffect")){
                        this.effect = new BuildersGuildEffect() ;
                    }

                    if (json.has("StrategistsGuildEffect")){
                        this.effect = new StrategistsGuildEffect() ;
                    }
                    if(json.has("coinCardEffect")){
                        if(json.get("coinCardEffect").equals("PYRAMID")) {
                            this.coinCardEffect = null;
                            this.effect = new CoinCardEffect(null, json.getInt("age"));
                        } else {
                            this.coinCardEffect = CardColor.valueOf(json.getString("coinCardEffect"));
                            this.effect = new CoinCardEffect(CardColor.valueOf(json.getString("coinCardEffect")), json.getInt("age"));
                        }
                    }
                    if (json.has("scienceChoiceEffect")){
                        this.effect = new ScienceChoiceEffect();
                    }
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

    public Effect getEffect() { return this.effect; }

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
        for(Integer cost : cost.values())
            if(cost > 0) return false;
        return true;
    }

    @Override
    public String toString() {
        return name;
    }
}
