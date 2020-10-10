package Effects;

import Card.*;
import Player.*;
import java.util.EnumMap;

public class PlaySeventhCardEffect extends Effect{

    @Override
    public void applyEffect(Player player, CardColor color, Integer age, EnumMap<Resource, Integer> cost) {
        player.play();
    }
}
