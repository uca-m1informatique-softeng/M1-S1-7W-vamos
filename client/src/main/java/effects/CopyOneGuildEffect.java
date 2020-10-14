package effects;

import card.Card;
import card.CardColor;
import card.Resource;
import player.Player;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.EnumMap;

public class CopyOneGuildEffect extends Effect {

    private SecureRandom random = new SecureRandom();

    @Override
    public void applyEffect(Player player, CardColor color, Integer age, EnumMap<Resource, Integer> cost, ArrayList<Card> discardCards) {
        ArrayList<Card> builtCards = new ArrayList<>();
        Player neighbor1 = player.getNextNeighbor();
        Player neighbor2 = player.getPrevNeighbor();
        builtCards.addAll(neighbor1.getBuiltCards());
        builtCards.addAll(neighbor2.getBuiltCards());
        builtCards.removeIf(card -> card.getColor() != CardColor.PURPLE);
        if(builtCards.size() > 0){
            Card card = builtCards.get(random.nextInt(builtCards.size()));
            player.getBuiltCards().add(card);
        }
    }
}
