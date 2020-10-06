package Player;

import Card.Card;
import Card.CardPoints;
import Card.Resource;
import java.io.IOException;
import java.util.EnumMap;

public class MilitaryStrategy extends Strategy {

    /**
     * Implementation of Strategy::chooseAction(Player player).
     * This implementation focuses on cards that give military might if available, and on cards that give resources
     * if no military cards are available.
     * @param player The player whose action should be chosen.
     * @return An Action with a military strategy in mind.
     */
    @Override
    public Action chooseAction(Player player) {
        Card mostMilitary;
        Card mostResources;
        try {
            mostMilitary = new Card("altar", 3); // Altar gives no military might
            mostResources = new Card("apothecary", 6); // Apothecary gives no resources

            for (Card card : player.getHand()) {
                if (card.getCardPoints().get(CardPoints.MILITARY) >= mostMilitary.getCardPoints().get(CardPoints.MILITARY)) {
                    mostMilitary = card;
                }
                if (resourceSum(card.getResource()) >= resourceSum(mostResources.getResource())) {
                    mostResources = card;
                }
            }

            /* if (player.wonder.getCurrentRewardsFromUpgrade().get(CardPoints.MILITARY) > mostMilitary.getCardPoints().get(CardPoints.MILITARY) &&
                player.wonder.canUpgrade(player.resources)) {
                return new Action(mostMilitary, Action.WONDER);
            } else */
            if (player.isBuildable(mostMilitary)) {
                return new Action(mostMilitary, Action.BUILD);
            } else if (player.isBuildable(mostResources)) {
                return new Action(mostResources, Action.BUILD);
            } else {
                return new Action(mostMilitary, Action.DUMP);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Gives the sum of resources in a card.
     * @param map The resources that a card gives.
     * @return the number of resources the card gives.
     */
    private static int resourceSum(EnumMap<Resource, Integer> map) {
        int sum = 0;
        for (Resource r : Resource.values()) {
            sum += map.get(r);
        }
        return sum;
    }
}
