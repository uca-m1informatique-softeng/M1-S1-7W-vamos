package Effects;

import Player.*;
import Card.*;

public class PlaySeventhCardEffect extends Effect{
    void applyEffect(Player player, Strategy strategy) {
        Action action = strategy.chooseAction(player);
        Card chosenCard = action.getCard();
    }
}
