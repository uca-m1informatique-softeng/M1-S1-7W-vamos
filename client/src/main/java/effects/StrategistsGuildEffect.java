package effects;

import card.Card;
import card.CardColor;
import card.CardPoints;
import card.Resource;
import player.Player;
import utility.Writer;

import java.util.ArrayList;
import java.util.EnumMap;

public class StrategistsGuildEffect extends Effect{

    public void applyEffect(Player player, CardColor color, Integer age, EnumMap<Resource, Integer> cost, ArrayList<Card> discardCards){
        int prevPlayerDefeatToken = player.getPrevNeighbor().getDefeatToken() ;
        int nextPlayerDefeatToken = player.getNextNeighbor().getDefeatToken() ;

        int totalDefeatTokens = prevPlayerDefeatToken + nextPlayerDefeatToken ;
        player.getPoints().put(CardPoints.VICTORY , totalDefeatTokens) ;

        Writer.write(player.getName() + " got " + totalDefeatTokens + " victory points from his neighbors using the strategists guild card");
    }
}
