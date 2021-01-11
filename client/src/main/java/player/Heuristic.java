package player;

import card.Card;
import card.CardColor;
import card.CardPoints;
import effects.ResourceChoiceEffect;

public class Heuristic {

    private static final int RESOURCES_PONDERATION = 3;
    private static final int GOOD_CARDS_PONDERATION = 3;
    private static final int MILITARY_PONDERATION = 8;
    private static final int SCIENCE_PONDERATION = 6;
    private static final int CIVIL_PONDERATION = 4;
    private static final int COIN_PONDERATION = 1;

    /**
     * Computes an heuristic to be used by Monte-Carlo
     * @param p The Player whose the heuristic will be computed
     * @return The current heuristic of the player
     */
    static int heuristic(Player p) {
        int res = Heuristic.sumHeuristic(p);
        int prev = Heuristic.sumHeuristic(p.getPrevNeighbor());
        int next = Heuristic.sumHeuristic(p.getNextNeighbor());

        return Math.min(res - prev, res - next);
    }

    /**
     * Sums up all the different heuristics together with defined ponderations
     * @param p The player to sum the heuristics for
     * @return The ponderated sum of the different heuristics
     */
    private static int sumHeuristic(Player p) {
        int res = 0;

        res += Heuristic.RESOURCES_PONDERATION * resourcesHeuristic(p);
        res += Heuristic.GOOD_CARDS_PONDERATION * goodCardsHeuristic(p);
        res += Heuristic.MILITARY_PONDERATION * militaryHeuristic(p);
        res += Heuristic.SCIENCE_PONDERATION * p.getSciencePoint();
        res += Heuristic.CIVIL_PONDERATION * p.getPoints().get(CardPoints.VICTORY);
        res += Heuristic.COIN_PONDERATION * p.getCoins()/3;

        return res;
    }

    /**
     * Computes a heuristic concerning the amount of the resources the player has
     * @param p The player whose military heuristic will be computed
     * @return An heuristic concerning only the current resources of the player
     */
    private static int resourcesHeuristic(Player p) {
        int res = 0;

        for (Card c : p.getBuiltCards()) {
            if (c.getColor() == CardColor.BROWN) {
                if (c.getEffect() instanceof ResourceChoiceEffect) {
                    res += 2;
                } else {
                    res += 1;
                }
            }
            if (c.getColor() == CardColor.GREY) { res += 1; }

        }

        return res;
    }

    /**
     * Computes a heuristic based on whether the player has certain "good" cards or not
     * @param p The player whose military heuristic will be computed
     * @return A heuristic regarding certain cards
     */
    private static int goodCardsHeuristic(Player p) {
        int res = 0;

        for (Card c : p.getBuiltCards()) {
            if (c.getName().equals("caravansery")) res += 2;
            if (c.getName().equals("forum")) res += 2;
            if (c.getName().equals("marketplace")) res += 2;
            if (c.getName().equals("east trading post")) res += 1;
            if (c.getName().equals("west trading post")) res += 1;
        }

        return res;
    }

    /**
     * Computes a heuristic for the military aspect of the game
     * @param p The player whose military heuristic will be computed
     * @return An heuristic concerning only the military aspects of the game
     */
    private static int militaryHeuristic(Player p) {
        int res = p.getFightPoints();

        int might = p.getPoints().get(CardPoints.MILITARY);
        int prevMight = p.getPrevNeighbor().getPoints().get(CardPoints.MILITARY);
        int nextMight = p.getNextNeighbor().getPoints().get(CardPoints.MILITARY);

        switch (p.getGame().getCurrentAge()) {
            case 1 :
                if (might > prevMight) {
                    res += 1;
                } else if (might < prevMight) {
                    res -= 1;
                }
                if (might > nextMight) {
                    res += 1;
                } else if (might < nextMight) {
                    res -= 1;
                }
                break;
            case 2 :
                if (might > prevMight) {
                    res += 3;
                } else if (might < prevMight) {
                    res -= 3;
                }
                if (might > nextMight) {
                    res += 3;
                } else if (might < nextMight) {
                    res -= 3;
                }
                break;
            case 3 :
                if (might > prevMight) {
                    res += 5;
                } else if (might < prevMight) {
                    res -= 5;
                }
                if (might > nextMight) {
                    res += 5;
                } else if (might < nextMight) {
                    res -= 5;
                }
                break;
        }

        return res;
    }

}
