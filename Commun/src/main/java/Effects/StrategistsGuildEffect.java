package Effects;

import Card.CardPoints;
import Player.Player;
import Utility.Writer;

public class StrategistsGuildEffect extends Effect{

    public void applyEffect(Player player){
        int prevPlayerDefeatToken = player.getPrevNeighbor().getDefeatToken() ;
        int nextPlayerDefeatToken = player.getNextNeighbor().getDefeatToken() ;

        int totalDefeatTokens = prevPlayerDefeatToken + nextPlayerDefeatToken ;
        player.getPoints().put(CardPoints.VICTORY , totalDefeatTokens) ;

        Writer.write(player.getName() + " got " + totalDefeatTokens + " victory points from his neighbors using the strategists guild card");
    }
}
