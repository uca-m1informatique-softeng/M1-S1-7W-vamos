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
import static utility.Constante.RESET;
import static utility.Constante.YELLOW_UNDERLINED;

public class AmbitiousStrategy extends Strategy {

    /**
     * The number of simulations Monte-Carlo will launch for each available Action
     */
    private static int NUMBER_OF_SIMULATIONS = 1;
    private Player player;

    public AmbitiousStrategy(Player player) {
        this.player = player;
    }

    @Override
    public Action chooseAction(Player player) {
        Writer.stopWriting();

        ArrayList<Action> actions = this.availableActions();
        ArrayList<Tuple<Action, Integer>> actionScores = new ArrayList<>();

        for (Action a : actions) {
            ArrayList<Integer> scores = new ArrayList<>();

            ArrayList<SimulationThread> threads = new ArrayList<>();
            for (int i = 0; i < AmbitiousStrategy.NUMBER_OF_SIMULATIONS; i++) {
                SimulationThread thread = new SimulationThread(this.player, a);
                threads.add(thread);
                thread.start();
            }

            // Waiting for every thread to finish
            boolean letsgo = false;
            do {
                for (SimulationThread t : threads) {
                    if (t.isFinished()) {
                        letsgo = true;
                    } else {
                        letsgo = false;
                        break;
                    }
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (!letsgo);
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
            Game simulGame = new Game(player.getGame());
            Player simulPlayer = simulGame.getPlayersArray().get(0);
            for (Player p : simulGame.getPlayersArray()) {
                if (p.getName().equals(player.getName())) {
                    simulPlayer = p;
                }
            }

            //First turn
            switch (simulGame.getState()) {
                case START:
                    simulGame.processNewAge();
                    simulGame.setState(PLAY);
                    break;
                case PLAY:
                    this.simulProcessTurn(simulGame, a);
                    simulGame.incrRound();
                    simulGame.processEndAge();
                    break;
                case END:
                    simulGame.applyAllEndEffect();
                    simulGame.setState(EXIT);
                    break;
                default:
                    break;
            }

            //Second part
            simulPlayer.setStrategy(new DumbStrategy());
            while (simulGame.getState() != GameState.EXIT)
                simulGame.process();

            res = simulPlayer.computeScore();

        } catch (PlayerNumberException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return res;
    }

    private void simulProcessTurn(Game game, Action a) {
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
