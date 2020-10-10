package Effects;

import Card.CardColor;
import Card.Resource;
import Player.Player;
import Player.Action;
import java.util.EnumMap;

/**
 * This class gives a Player the ability to construct a building
 * from his or her hand for free (Once per age)
 */
public class FreeCardPerAgeEffect extends Effect{
    /**
     * This method gives a player the ability to construct a card for free , regarding if it's a free card or not
     * @param player The player who can construct a free building once per age
     */
    public void applyEffect(Player player, CardColor color, Integer age, EnumMap<Resource, Integer> cost){
        Action action = player.getStrategy().chooseAction(player); //No matter what action, we build the card.
        player.setChosenCard(action.getCard());
        player.getBuiltCards().add(player.getChosenCard());
        player.addPointsAndResources();
        player.getHand().remove(player.getChosenCard());
    }
}
