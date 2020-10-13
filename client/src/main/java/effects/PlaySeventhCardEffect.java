package effects;

import card.*;
import player.*;
import java.util.EnumMap;

public class PlaySeventhCardEffect extends Effect{

    @Override
    public void applyEffect(Player player, CardColor color, Integer age, EnumMap<Resource, Integer> cost) {
        player.play();
    }
}
