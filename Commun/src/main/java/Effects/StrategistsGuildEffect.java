package Effects;

import Card.CardColor;
import Card.CardPoints;
import Card.Resource;
import Player.Player;
import Utility.Writer;

import java.util.EnumMap;

public class StrategistsGuildEffect extends Effect{

    public void applyEffect(Player player, CardColor color, Integer age, EnumMap<Resource, Integer> cost){
        int prevPlayerDefeatToken = player.getPrevNeighbor().getDefeatToken() ;
        int nextPlayerDefeatToken = player.getNextNeighbor().getDefeatToken() ;

        int totalDefeatTokens = prevPlayerDefeatToken + nextPlayerDefeatToken ;
        player.getPoints().put(CardPoints.VICTORY , totalDefeatTokens) ;

        Writer.write(player.getName() + " got " + totalDefeatTokens + " victory points from his neighbors using the strategists guild card");
    }
}
