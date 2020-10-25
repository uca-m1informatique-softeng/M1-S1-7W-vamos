package card;

import effects.*;
import org.json.*;

import java.io.*;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.stream.Collectors;

import static utility.Constante.*;

public class Card {

    private String name;
    private CardColor color;
    private CardColor coloredCardResourceEffect;
    private CardColor coinCardEffect;
    private int age;
    private int players;
    private Effect effect;
    private ArrayList<String> freeCards;
    private EnumMap<CardPoints, Integer> cardPoints;
    private EnumMap<Resource, Integer> resource;
    private EnumMap<Resource, Integer> cost;


    public Card(String name, int players) {
        InputStream is = Card.class.getClassLoader().getResourceAsStream("cards/"+name+".json");
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String content = br.lines().collect(Collectors.joining());
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

        boolean validPlayerNb = false;
        if (card.has(STR_PLAYERS)) {
            for (int i = 0; i < card.getJSONArray(STR_PLAYERS).length(); i++) {
                if (card.getJSONArray("players").getInt(i) <= players) {
                    validPlayerNb = true;
                }
            }
        }
        if (validPlayerNb || !card.has(STR_PLAYERS)) {
            // age
            this.age = card.getInt("age");

            // value of card = output that the player receives when playing the card
            if (card.has(STR_CARDPOINTS)) {
                CardPoints cardPointKey = CardPoints.valueOf(card.getJSONObject(STR_CARDPOINTS).keys().next());
                Integer cardPointValue = card.getJSONObject(STR_CARDPOINTS).getInt(card.getJSONObject(STR_CARDPOINTS).keys().next());
                this.cardPoints.put(cardPointKey, cardPointValue);
            }

            // cost for a card (if any)
            if (card.has(STR_COST)) {
                for (int k = 0; k < card.getJSONObject(STR_COST).names().length(); k++) {
                    String key = card.getJSONObject(STR_COST).names().getString(k);
                    int value = card.getJSONObject(STR_COST).getInt(key);

                    this.cost.put(Resource.valueOf(key), value);
                }
            }

            // resource given by a card (if any)
            if (card.has(STR_RESOURCE)) {
                for (int k = 0; k < card.getJSONObject(STR_RESOURCE).names().length(); k++) {

                    String key = card.getJSONObject(STR_RESOURCE).names().getString(k);
                    int value = card.getJSONObject(STR_RESOURCE).getInt(key);

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

            if (card.has(STR_COLOREDCARDEFFECT)) {
                this.coloredCardResourceEffect = CardColor.valueOf(card.getString(STR_COLOREDCARDEFFECT));

                CardColor tmpCardResEffect = CardColor.valueOf(card.getString(STR_COLOREDCARDEFFECT));

                this.effect = new ColoredCardResourceEffect(tmpCardResEffect);
            }

            if (card.has("ShipownersGuildEffect")) {
                this.effect = new ShipOwnersGuildEffect();
            }

            if (card.has("BuildersGuildEffect")) {
                this.effect = new BuildersGuildEffect();
            }
            if (card.has("StrategistsGuildEffect")) {
                this.effect = new StrategistsGuildEffect();
            }
            if (card.has("scienceChoiceEffect")) {
                this.effect = new ScienceChoiceEffect();
            }

            String coinCardEffect = "coinCardEffect";

            if (card.has(coinCardEffect)) {
                if (card.get(coinCardEffect).equals("PYRAMID")) {
                    this.coinCardEffect = null;
                    this.effect = new CoinCardEffect(null, card.getInt("age"));
                } else {
                    this.coinCardEffect = CardColor.valueOf(card.getString(coinCardEffect));
                    this.effect = new CoinCardEffect(CardColor.valueOf(card.getString(coinCardEffect)), card.getInt("age"));
                }
            }

            String str = "freeCards";

            //free cards
            if (card.has(str)) {
                freeCards = new ArrayList<String>();
                for (int k = 0; k < card.getJSONArray(str).length(); k++) {
                    freeCards.add(card.getJSONArray(str).getString(k));
                }
            }
        }
    }

    public static JSONObject countCards(JSONObject cardCount, ArrayList<Card> builtCards){
        for (Card card : builtCards) {
            switch (card.getColor()) {
                case BROWN:
                    cardCount.put("brownCards", cardCount.getInt("brownCards") + 1);
                    break;
                case GREY:
                    cardCount.put("greyCards", cardCount.getInt("greyCards") + 1);
                    break;
                case YELLOW:
                    cardCount.put("yellowCards", cardCount.getInt("yellowCards") + 1);
                    break;
                default:
                    break;
            }
        }
        return cardCount;
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

    public ArrayList<String> getFreeCards() {
        return freeCards;
    }

    public Boolean isFree() {
        for (Integer i : cost.values())
            if (i > 0) return false;
        return true;
    }

    @Override
    public String toString() {
        return name;
    }
}