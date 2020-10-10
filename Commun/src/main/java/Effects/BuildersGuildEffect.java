package Effects;

import Card.CardColor;
import Card.CardPoints;
import Card.Resource;
import Wonder.Wonder;
import Player.Player;
import Utility.Writer;
import java.util.EnumMap;

public class BuildersGuildEffect extends Effect{

    public void applyEffect(Player player, CardColor color, Integer age, EnumMap<Resource, Integer> cost){
        Wonder wonder = player.getWonder() ;

        wonder.getState() ;

        int playerWonderState = player.getWonder().getState() ; //player wonder state
        int previousPlayerWonderState = player.getPrevNeighbor().getWonder().getState() ; //previous player wonder stage
        int nextPlayerWonderState = player.getNextNeighbor().getWonder().getState() ; //next player wonder stage

        int victoryPointsTotal = playerWonderState + previousPlayerWonderState + nextPlayerWonderState ;
        player.getPoints().put(CardPoints.VICTORY , victoryPointsTotal) ;

        Writer.write( player.getName() + " got " + victoryPointsTotal + " victory points from his neighbors using the builders guild card" );
    }
}
