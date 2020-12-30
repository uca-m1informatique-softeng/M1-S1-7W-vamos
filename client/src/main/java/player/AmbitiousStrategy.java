package player;
import effects.Effect;
import effects.TookDiscardCardEffect;
import exceptions.PlayerNumberException;
import utility.Tuple;
import utility.Utilities;
import card.*;
import java.util.ArrayList;

import core.Game;
import core.GameState;
import utility.Writer;

import java.io.IOException;

import static core.GameState.EXIT;
import static core.GameState.PLAY;

public class AmbitiousStrategy extends Strategy {

    /**
     * The number of simulations Monte-Carlo will launch for each available Action
     */
    private static int NUMBER_OF_SIMULATIONS = 100;
    private Player player;

    public AmbitiousStrategy(Player player) {
        this.player = player;
    }

    @Override
    public Action chooseAction(Player player) {
        Writer.stopWriting();

        ArrayList<Action> actions = this.availableActions();
        ArrayList<Tuple<Action, Float>> actionScores = new ArrayList<>();

        for (Action a : actions) {
            ArrayList<Integer> scores = new ArrayList<>();

            ArrayList<SimulationThread> threads = new ArrayList<>();
            for (int i = 0; i < AmbitiousStrategy.NUMBER_OF_SIMULATIONS; i++) {
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
                scores.add(t.getScore());
            }

            actionScores.add(new Tuple<>(a, Utilities.average(scores)));
        }

        float actionScore = 0;
        Action chosenAction = actions.get(0);

        for (Tuple<Action, Float> t : actionScores) {
            if (t.y > actionScore) {
                actionScore = t.y;
                chosenAction = t.x;
            }
        }

        Writer.resumeWriting();

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
            simPlayer.setStrategy(new DumbStrategy());
            while (simGame.getState() != GameState.EXIT)
                simGame.process();

            res = simPlayer.computeScore();

        } catch (PlayerNumberException e) {
            e.printStackTrace();
        } catch (IOException e) {
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
            this.score = ((AmbitiousStrategy) this.player.getStrategy()).simulateGame(this.player, this.action);
            this.finished = true;
        }
    }
}
