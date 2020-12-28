package player;
import utility.Tuple;
import utility.Utilities;
import card.*;
import java.util.ArrayList;

public class AmbitiousStrategy extends Strategy{

    /**
     * The number of simulations Monte-Carlo will launch for each available Action
     */
    private static int NUMBER_OF_SIMULATIONS = 10000;

    @Override
    public Action chooseAction(Player player) {
        ArrayList<Action> actions = this.availableActions(player);
        ArrayList<Tuple<Action, Integer>> actionScores = new ArrayList<>();

        // TODO call simulateGame on each action
        for (Action a : actions) {
            ArrayList<Integer> scores = new ArrayList<>();

            for (int i = 0; i < AmbitiousStrategy.NUMBER_OF_SIMULATIONS; i++) {
                //scores.add(player.simulateGame(a));
            }
            actionScores.add(new Tuple<>(a, Utilities.average(scores)));
        }

        int actionScore = 0;
        Action chosenAction = actions.get(0);

        for (Tuple<Action, Integer> t : actionScores) {
            if (t.y > actionScore) {
                chosenAction = t.x;
            }
        }

        return chosenAction;
    }

    /**
     * Checks all the current available Actions of the player.
     * Won't return Actions that the player can't do because of lack of money, resources, etc...
     * @param player The player whose available Actions are to be listed.
     * @return The available Actions of the player.
     */
    private ArrayList<Action> availableActions(Player player) {
        ArrayList<Action> actionList = new ArrayList<>();

        for (Card c : player.hand) {
            if (player.isBuildable(c)) {
                actionList.add(new Action(c, Action.BUILD));
            }
            if (player.getWonder().canUpgrade(player.getResources())) {
                actionList.add(new Action(c, Action.WONDER));
            }
            actionList.add(new Action(c, Action.DUMP)); // Dumping is ALWAYS an option
        }

        return actionList;
    }

    @Override
    public String toString() {
        return "(Monte-Carlo)";
    }
}
