package player;

import card.Card;
import card.CardColor;
import card.CardPoints;
import effects.ResourceChoiceEffect;

public class Heuristic {

    /**
     * Computes an heuristic to be used by Monte-Carlo
     * @param p The Player whose the heuristic will be computed
     * @return The current heuristic of the player
     */
    static int heuristic(Player p) {
        int res = 0;
        int prev = 0;
        int next = 0;

        return Math.min();
    }

    /**
     * Computes a heuristic concerning the amount of the resources the player has
     * @param p The player whose military heuristic will be computed
     * @return An heuristic concerning only the current resources of the player
     */
    private int resourcesHeuristic(Player p) {
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
    private int goodCardsHeuristic(Player p) {
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
    private int militaryHeuristic(Player p) {
        int res = p.getFightPoints();

        int might = p.getPoints().get(CardPoints.MILITARY);
        int prevMight = p.getPrevNeighbor().getPoints().get(CardPoints.MILITARY);
        int nextMight = p.getNextNeighbor().getPoints().get(CardPoints.MILITARY);

        switch (p.getGame().getCurrentAge()) {
            case 1 :
                if (might > prevMight) {
                    res += 1;
                } else (might < prevMight) {
                    res -= 1;
                }
                if (might > nextMight) {
                    res += 1;
                } else (might < nextMight) {
                    res -= 1;
                }
                break;
            case 2 :
                if (might > prevMight) {
                    res += 3;
                } else (might < prevMight) {
                    res -= 3;
                }
                if (might > nextMight) {
                    res += 3;
                } else (might < nextMight) {
                    res -= 3;
                }
                break;
            case 3 :
                if (might > prevMight) {
                    res += 5;
                } else (might < prevMight) {
                    res -= 5;
                }
                if (might > nextMight) {
                    res += 5;
                } else (might < nextMight) {
                    res -= 5;
                }
                break;
        }

        return res;
    }

}
