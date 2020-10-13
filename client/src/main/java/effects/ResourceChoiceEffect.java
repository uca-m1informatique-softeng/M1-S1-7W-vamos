package effects;

import card.CardColor;
import card.Resource;
import player.Player;
import java.util.ArrayList;
import java.util.EnumMap;

/**
 * Extension of class Effect.
 * This class defines the ability of a card to let choose its owner between several resources.
 * e.g. : The card Mine can let you choose between getting either one ORE, or one STONE.
 */
public class ResourceChoiceEffect extends Effect {

    /**
     * The resources the player is able to choose from
     */
    private ArrayList<Resource> res;


    /**
     * The only way to create a ResourceChoiceEffect instance.
     * Should only be instantiated from the class card (its owner).
     * @param res The resources the player is able to choose from
     */
    public ResourceChoiceEffect(ArrayList<Resource> res) {
        this.res = res;
    }


    public ArrayList<Resource> getRes() {
        return res;
    }

    /**
     * Apply the effect to a cost.
     * DO NOT call this method directly on a card's cost ! Otherwise you will change its values.
     * Should only be applied to a local variable, in order to check the final cost after the effect has been applied.
     * Compares the needed resources (in cost) to the available choices given by the card. If there is a match,
     * cost will be decremented by one on the matched resource.
     * e.g. :   Effect is either 1 WOOD or 1 STONE
     *          Cost is 1 ORE and 2 STONES
     *          After effect.applyEffect(cost), cost will be 1 ORE and 1 STONE
     * @param cost Initial cost of a card.
     */
    public void applyEffect(Player player, CardColor color, Integer age, EnumMap<Resource, Integer> cost) {
        boolean applied = false;

        for (Resource neededResource : cost.keySet()) {
            for (Resource effectResource : this.res) {
                if (neededResource.equals(effectResource) && cost.get(neededResource) > 1) {
                    cost.put(neededResource, cost.get(neededResource) - 1);
                    applied = true;
                }
                if (applied) break;
            }
            if (applied) break;
        }
    }

    @Override
    public String toString() {
        return "ResourceChoiceEffect{" +
                "res=" + res +
                '}';
    }
}
