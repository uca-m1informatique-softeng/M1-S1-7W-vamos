package player;

import core.Game;

public class AmbitiousStrategy extends Strategy{

    @Override
    public Action chooseAction(Player player) {
        return null;
    }

    @Override
    public String toString() {
        return "Ambitious AI";
    }

    private int simul(Action a, Game game){
        return -1;
    }
}
