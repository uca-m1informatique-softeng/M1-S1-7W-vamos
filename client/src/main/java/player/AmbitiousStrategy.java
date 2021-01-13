package player;

import effects.Effect;
import effects.TookDiscardCardEffect;
import utility.Tuple;
import utility.Utilities;
import card.*;
import java.util.ArrayList;
import java.util.Date;
import core.Game;
import core.GameState;
import utility.Writer;
import static core.GameState.EXIT;
import static core.GameState.PLAY;
import static utility.Constante.*;

public class AmbitiousStrategy extends Strategy {

    /**
     * The number of simulations Monte-Carlo will launch for each available Action
     */
    private static final int NUMBER_OF_SIMULATIONS = 1000;
    /**
     * The number of turns Monte-Carlo will simulate. If -1, depth will be dynamic.
     */
    private static final int MAXIMUM_DEPTH = -1;


    private final Player player;

    public AmbitiousStrategy(Player player) {
        this.player = player;
    }

    @Override
    public Action chooseAction(Player player) {
        if (this.player.getGame().mode.equals(GAME_MODE)) {
            Writer.stopWriting();
        }
        long t1 = (new Date()).getTime();

        ArrayList<Action> actions = this.availableActions();
        ArrayList<Tuple<Action, Float>> actionScores = new ArrayList<>();

        ArrayList<SimulationThread> threads = new ArrayList<>();
        for (Action a : actions) {
            SimulationThread thread = new SimulationThread(this.player, a);
            threads.add(thread);
            thread.start();
        }

        // Waiting for every thread to finish
        boolean letsGo = false;
        while (!letsGo) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for (SimulationThread t : threads) {
                if (t.isFinished()) {
                    letsGo = true;
                } else {
                    letsGo = false;
                    break;
                }
            }
        }

        for (SimulationThread t : threads) {
            actionScores.add(new Tuple<>(t.action, t.score));
        }

        float actionScore = 0;
        Action chosenAction = actions.get(0);

        for (Tuple<Action, Float> t : actionScores) {
            if (t.y > actionScore) {
                actionScore = t.y;
                chosenAction = t.x;
            }
        }

        long t2 = (new Date()).getTime();
        System.out.println("Age " + this.player.getGame().getCurrentAge() + ", Turn " + this.player.getGame().getRound() + " - Monte-Carlo took " + ((float) (t2 - t1))/1000.0 + "s to choose an action.");
        if (this.player.getGame().mode.equals(GAME_MODE)) {
            Writer.resumeWriting();
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

    /**
     * Launches AmbitiousStrategy.NUMBER_OF_SIMULATIONS games for a given action and gives an average score
     * if the player plays this action.
     * @param a The Action to play with
     * @return An average score if the player chooses this action.
     */
    private float simulateAction(Action a) {
        ArrayList<Integer> scores = new ArrayList<>();

        for (int i = 0; i < AmbitiousStrategy.NUMBER_OF_SIMULATIONS; i++) {
            scores.add(this.simulateGame(this.player, a));
        }

        return Utilities.average(scores);
    }

    private int simulateGame(Player player, Action a) {
        int res = -1;

        try {
            Game simGame = new Game(player.getGame());
            Player simPlayer = simGame.getPlayersArray().get(0);
            for (Player p : simGame.getPlayersArray()) {
                if (p.getName().equals(player.getName())) {
                    simPlayer = p;
                }
            }

            //First turn
            switch (simGame.getState()) {
                case START:
                    simGame.processNewAge();
                    simGame.setState(PLAY);
                    break;
                case PLAY:
                    this.simProcessTurn(simGame, a);
                    simGame.incrRound();
                    simGame.processEndAge();
                    break;
                case END:
                    simGame.applyAllEndEffect();
                    simGame.setState(EXIT);
                    break;
                default:
                    break;
            }

            //Second part
            simPlayer.setStrategy(new GuaranteedStrategy(simPlayer));
            int depth = 2;
            if (AmbitiousStrategy.MAXIMUM_DEPTH != DYNAMIC_DEPTH) {
                while ( simGame.getState() != GameState.EXIT &&
                        depth <= AmbitiousStrategy.MAXIMUM_DEPTH) {
                    simGame.process();
                    depth++;
                }
            } else {
                if (simGame.getCurrentAge() == 1 && simGame.getRound() <= 3) {
                    while (depth <= 3) {
                        simGame.process();
                        depth++;
                    }
                } if (simGame.getCurrentAge() == 1 && simGame.getRound() > 3) {
                    while (depth <= 4) {
                        simGame.process();
                        depth++;
                    }
                } else if (simGame.getCurrentAge() >= 2) {
                    while (depth <= 6) {
                        simGame.process();
                        depth++;
                    }
                }
            }

            res = (simGame.getState() != GameState.EXIT) ?
                Heuristic.heuristic(simPlayer) :
                Math.min(simPlayer.computeScore() - simPlayer.getPrevNeighbor().computeScore(), simPlayer.computeScore() - simPlayer.getNextNeighbor().computeScore());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return res;
    }

    private void simProcessTurn(Game game, Action a) {
        for (Player player : game.getPlayersArray()) {
            for (Effect e : player.getWonderEffectNotApply()) {
                if (e instanceof TookDiscardCardEffect) {
                    e.applyEffect(player, null, null, null, game.getDiscardCards());
                    player.getWonderEffectNotApply().remove(e);
                    game.getDiscardCards().remove(player.getChosenCard());
                    break;
                }
            }

            if (player.getName().equals(this.player.getName())) {
                player.chosenCard = a.getCard();

                switch (a.getAction()) {
                    case Action.BUILD:
                        player.buildCard();
                        break;
                    case Action.WONDER:
                        player.buildStageWonder();
                        break;
                    default:
                        player.dumpCard();
                        break;
                }
            } else {
                player.play();
            }

            if (player.getDumpCard() != null) {
                game.getDiscardCards().add(player.getDumpCard());
            }
        }
        game.swapHands(game.getCurrentAge());
    }

    @Override
    public String toString() {
        return "Monte-Carlo";
    }

    private class SimulationThread extends Thread {
        private float score = 0;
        private final Action action;
        private final Player player;
        private boolean finished = false;

        SimulationThread(Player p, Action a) {
            this.player = p;
            this.action = a;
        }

        boolean isFinished() {
            return this.finished;
        }

        @Override
        public void run() {
            this.score = ((AmbitiousStrategy) this.player.getStrategy()).simulateAction(this.action);
            this.finished = true;
        }
    }
}
