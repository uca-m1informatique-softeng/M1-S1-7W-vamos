package Effects;

import Player.*;
import Card.*;

import java.util.ArrayList;

public class PlaySeventhCardEffect extends Effect{
    void applyEffect(Player player, Strategy strategy) {
        Action action = strategy.chooseAction(player);
        ArrayList<Card> cards = player.getHand();
        Card lastCard = cards.get(cards.size());
    }
}
