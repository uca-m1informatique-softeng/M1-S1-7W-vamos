package effects;

import card.Card;
import card.CardColor;
import card.Resource;
import player.Player;

import java.util.ArrayList;
import java.util.EnumMap;

public class TookDiscardCardEffect extends Effect{

    @Override
    public void applyEffect(Player player, CardColor color, Integer age, EnumMap<Resource, Integer> cost, ArrayList<Card> discardCards) {
        if(!discardCards.isEmpty()) {
            ArrayList<Card> tmpHand = player.getHand();
            player.setHand(discardCards);
            player.play();
            player.setHand(tmpHand);
        }
    }

}
