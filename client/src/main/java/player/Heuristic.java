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
    static int heuristic(Player p, Action a) {
        int opti = 0;
        if (a.getAction() == Action.DUMP) { return p.computeScore(); }
        for(Card c : p.getBuiltCards()) {
            if(c.getColor() == CardColor.BROWN) {
                if (c.getEffect() instanceof ResourceChoiceEffect) {
                    opti += 4;
                } else {
                    opti += 3;
                }
            }
            if(c.getColor() == CardColor.RED) { opti += 3; }
            if(c.getColor() == CardColor.GREY) { opti += 4; }
            if(c.getName().equals("caravansery")) { opti += 6; }
        }
        int s = p.computeScore();
        int ss = p.prevNeighbor.computeScore();
        int sss = p.nextNeighbor.computeScore();
        opti += s-ss + s-sss;
        return p.computeScore() + opti;
    }

    /**
     * Computes an heuristic for the military aspect of the game
     * @param p The player whose military heuristic wiil be computed
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

    private int scienceHeuristic(Player p) {
        int res = 0;

        

        return res;
    }

}
