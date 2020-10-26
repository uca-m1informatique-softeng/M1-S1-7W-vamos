package effects;

import card.*;
import org.json.JSONObject;
import player.Player;
import utility.Writer;
import java.util.ArrayList;
import java.util.EnumMap;

/**
 * Yellow cards in Age 2 can have card effects that grant the player additional coins
 * depending on the number of built brown/grey cards in his/her own city as well as the neighboring cities.
 * Yellow cards in Age 3 can grant the player additional coins that will be added directly
 * as well as victory points that will be counted at the end of the game.
 */
public class CoinCardEffect extends Effect {
    /* for json files: add
     * coinCardEffect : COLOR/PYRAMID
     */
    CardColor color;
    Integer age;
    Integer brownCards = 0;
    Integer greyCards = 0;
    Integer yellowCards = 1; //initialized with 1 since the just played card counts too

    public CoinCardEffect(CardColor color, Integer age) {
        this.color = color;
        this.age = age;
    }
    @Override
    public void applyEffect(Player player, CardColor color, Integer age, EnumMap<Resource, Integer> cost, ArrayList<Card> discardCards) {
        String strGreyCards = "greyCards";
        JSONObject cards = new JSONObject();
        cards.put("brownCards", 0);
        cards.put(strGreyCards, 0);
        cards.put("yellowCards", 0);
        if (color != null) {
            ArrayList<Card> builtCards = new ArrayList<>();
            builtCards.addAll(player.getBuiltCards());
            /*
             *  get neighbors built cards for age 2 card
             */
            if (age == 2) {
                builtCards.addAll(player.getPrevNeighbor().getBuiltCards());
                builtCards.addAll(player.getNextNeighbor().getBuiltCards());
            }
            /*
             * iterate through all built cards and distinguish effects of brown and grey cards
             */
            cards = Card.countCards(cards, builtCards);
        }
        int oldCoins = player.getCoins();
        if (color == CardColor.BROWN) {
            player.setCoins(player.getCoins() + cards.getInt("brownCards"));
        } else if (color == CardColor.GREY) {
            if (age == 3) {
                cards.put(strGreyCards, cards.getInt(strGreyCards) * 2);
            }
            player.setCoins(player.getCoins() + cards.getInt(strGreyCards));
        } else if (color == CardColor.YELLOW) {
            player.setCoins(player.getCoins() + cards.getInt("yellowCards"));
        } else if (color == null) {
            player.setCoins(player.getCoins() + (player.getWonder().getState() * 3));
        }
        Writer.write(player.getName() + " got " + (player.getCoins() - oldCoins) + " coins because of cardCoinPoints");

    }
}


