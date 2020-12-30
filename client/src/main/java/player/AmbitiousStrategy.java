package player;
import utility.Tuple;
import utility.Utilities;
import card.*;
import java.util.ArrayList;

import core.Game;
import core.GameState;

import java.io.IOException;

public class AmbitiousStrategy extends Strategy {

    /**
     * The number of simulations Monte-Carlo will launch for each available Action
     */
    private static int NUMBER_OF_SIMULATIONS = 1000;
    private Player player;

    public AmbitiousStrategy(Player player) {
        this.player = player;
    }

    @Override
    public Action chooseAction(Player player) {
        ArrayList<Action> actions = this.availableActions();
        ArrayList<Tuple<Action, Integer>> actionScores = new ArrayList<>();

        for (Action a : actions) {
            ArrayList<Integer> scores = new ArrayList<>();

            ArrayList<SimulationThread> threads = new ArrayList<>();
            for (int i = 0; i < AmbitiousStrategy.NUMBER_OF_SIMULATIONS; i++) {
                threads.add(new SimulationThread(this.player, a));
                threads.get(i).start();
            }

            // Waiting for every thread to finish
            boolean letsgo = false;
            while (!letsgo) {
                for (SimulationThread t : threads) {
                    if (t.isFinished()) {
                        letsgo = true;
                    } else {
                        letsgo = false;
                        break;
                    }
                }
            }
            for (SimulationThread t : threads) {
                scores.add(t.getScore());
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
     * @return The available Actions of the player.
     */
    protected ArrayList<Action> availableActions() {
        ArrayList<Action> actionList = new ArrayList<>();

        for (Card c : this.player.getHand()) {
            if (this.player.isBuildable(c)) {
                actionList.add(new Action(c, Action.BUILD));
            }
            if (this.player.getWonder().canUpgrade(this.player.getResources())) {
                actionList.add(new Action(c, Action.WONDER));
            }
            actionList.add(new Action(c, Action.DUMP)); // Dumping is ALWAYS an option
        }

        return actionList;
    }

    @Override
    public String toString() {
        return "Monte-Carlo";
    }

    private class SimulationThread extends Thread {
        private int score = 0;
        private Action action;
        private Player player;
        private boolean finished = false;

        SimulationThread(Player p, Action a) {
            this.player = p;
            this.action = a;
        }

        int getScore() {
            return this.score;
        }

        boolean isFinished() {
            return this.finished;
        }

        @Override
        public void run() {
            //this.score = this.simulateGame(this.player, this.action);
            this.finished = true;
        }
    }

    protected int simulateGame(Player player, Action a) throws IOException {
        //Game simulGame = new Game(player.getGame());
        Game simulGame = player.getGame();
        //First turn

        simulGame.processTurn();
        //Second part
        simulGame.forceStrategy(new DumbStrategy(), new DumbStrategy(), new DumbStrategy());
        while (simulGame.getState() != GameState.EXIT)
            simulGame.process();

        return player.computeScore();
    }

    protected void simulProcessTurn(Action a) {


    }
}
