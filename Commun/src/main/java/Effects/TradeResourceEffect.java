package Effects;

import Card.CardColor;
import Card.Resource;
import Player.Player;
import java.util.ArrayList;
import java.util.EnumMap;

/**
 * This effect is used to define the property of some yellow cards to allow the player to buy particular resources
 * for a fraction of the initial cost.
 * e.g. :   If player has bought the East Trading Cost, he can buy CLAY, STONE, WOOD, or ORE for 1 coin to its right
 * neighbor.
 */
public class TradeResourceEffect extends Effect {

    /**
     * Tells if the effect applies to the next player
     */
    boolean nextPlayerAllowed;
    /**
     * Tells if the effect applies to the previous player
     */
    boolean prevPlayerAllowed;
    /**
     * List of resources the player can buy for 1 coin instead of 2
     */
    public ArrayList<Resource> resourcesModified;


    /**
     * Creates a new TradeResourceEffect
     *
     * @param prevPlayerAllowed Does the effect applies to trade with previous player ?
     * @param nextPlayerAllowed Does the effect applies to trade with previous player ?
     * @param resourcesModified Resources the player can buy for 1 coin instead of 2
     */
    public TradeResourceEffect(boolean prevPlayerAllowed, boolean nextPlayerAllowed, ArrayList<Resource> resourcesModified) {
        this.prevPlayerAllowed = prevPlayerAllowed;
        this.nextPlayerAllowed = nextPlayerAllowed;
        this.resourcesModified = resourcesModified;
    }

    @Override
    public void applyEffect(Player player, CardColor color, Integer age, EnumMap<Resource, Integer> cost) {

    }

    public boolean isNextPlayerAllowed() {
        return nextPlayerAllowed;
    }

    public boolean isPrevPlayerAllowed() {
        return prevPlayerAllowed;
    }

    public ArrayList<Resource> getResourcesModified() {
        return resourcesModified;
    }

}
