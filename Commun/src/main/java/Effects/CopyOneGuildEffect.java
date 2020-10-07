package Effects;

import Card.Card;
import Card.CardColor;
import Card.Resource;
import Player.Player;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Random;

public class CopyOneGuildEffect extends Effect {

    @Override
    public void applyEffect(Player player, CardColor color, Integer age, EnumMap<Resource, Integer> cost) {
        ArrayList<Card> builtCards = new ArrayList<Card>();
        Player neighbor1 = player.getNextNeighbor();
        Player neighbor2 = player.getPrevNeighbor();
        builtCards.addAll(neighbor1.getBuiltCards());
        builtCards.addAll(neighbor2.getBuiltCards());
        builtCards.removeIf(card -> card.getColor() != CardColor.PURPLE);
        if(builtCards.size() > 0){
            Random newrandom = new Random();
            Card card = builtCards.get(newrandom.nextInt(builtCards.size()));
            player.getBuiltCards().add(card);
        }
    }
}
