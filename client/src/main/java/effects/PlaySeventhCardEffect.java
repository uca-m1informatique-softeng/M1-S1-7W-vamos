package effects;

import card.*;
import player.*;

import java.util.ArrayList;
import java.util.EnumMap;

public class PlaySeventhCardEffect extends Effect{

    @Override
    public void applyEffect(Player player, CardColor color, Integer age, EnumMap<Resource, Integer> cost, ArrayList<Card> discardCards) {
        player.play();
    }
}
