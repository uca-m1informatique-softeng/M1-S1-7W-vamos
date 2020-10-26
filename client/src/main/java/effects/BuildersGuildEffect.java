package effects;

import card.Card;
import card.CardColor;
import card.CardPoints;
import card.Resource;
import wonder.Wonder;
import player.Player;
import utility.Writer;

import java.util.ArrayList;
import java.util.EnumMap;

public class BuildersGuildEffect extends Effect{

    /**
     * Add victory points in function of the wonder state of the city player's and his neighbor.
     * @param player who had the effect.
     */
    public void applyEffect(Player player, CardColor color, Integer age, EnumMap<Resource, Integer> cost, ArrayList<Card> discardCards){
        Wonder wonder = player.getWonder() ;

        wonder.getState() ;

        int playerWonderState = player.getWonder().getState() ; //player wonder state
        int previousPlayerWonderState = player.getPrevNeighbor().getWonder().getState() ; //previous player wonder stage
        int nextPlayerWonderState = player.getNextNeighbor().getWonder().getState() ; //next player wonder stage

        int victoryPointsTotal = playerWonderState + previousPlayerWonderState + nextPlayerWonderState ;
        int initialPoints = + player.getPoints().get(CardPoints.VICTORY) ;
        player.getPoints().put(CardPoints.VICTORY , victoryPointsTotal + initialPoints) ;

        Writer.write( player.getName() + " got " + victoryPointsTotal + " victory points from his neighbors using the builders guild card" );
    }
}
