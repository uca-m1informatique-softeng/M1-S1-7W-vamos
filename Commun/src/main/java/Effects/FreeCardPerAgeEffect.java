package Effects;

import Player.Player;

/**
 * This class gives a Player the ability to construct a building
 * from his or her hand for free (Once per age)
 */
public class FreeCardPerAgeEffect extends Effect{
    /**
     * This method gives a player the ability to construct a card for free , regarding if it's a free card or not
     * @param player The player who can construct a free building once per age
     */
    public void applyEffect(Player player){
        player.getBuiltCards().add(player.getChosenCard());
        player.addPointsAndResources();
        player.getHand().remove(player.getChosenCard());
    }
}
