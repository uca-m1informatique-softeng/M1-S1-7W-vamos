package player;

import core.Game;
import core.GameState;

import java.io.IOException;

public class AmbitiousStrategy extends Strategy {

    @Override
    public Action chooseAction(Player player) {
        return null;
    }

    @Override
    public String toString() {
        return "Ambitious AI";
    }

    private int simulateGame(Player player, Action a) throws IOException {
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

    private void simulProcessTurn(Action a) {

    }
}
