package Effects;

import Card.*;
import Player.Player;
import Utility.Writer;
import java.util.ArrayList;
import java.util.EnumMap;

public class ShipOwnersGuildEffect extends Effect{

    /**
     * Give one victory point for each card of color purple, brown and grey build.
     * @param player
     */
    @Override
    public void applyEffect(Player player, CardColor color, Integer age, EnumMap<Resource, Integer> cost){
        ArrayList<Card> playerBuiltCards = new ArrayList<Card>();
        playerBuiltCards = player.getBuiltCards() ;

        int nbOfBrownGreyPurpleCards = 0 ;

        for (int i = 0; i < playerBuiltCards.size(); i++) {
            if(playerBuiltCards.get(i).getColor() == CardColor.BROWN || playerBuiltCards.get(i).getColor() == CardColor.GREY
            || playerBuiltCards.get(i).getColor() == CardColor.PURPLE ){
                nbOfBrownGreyPurpleCards ++ ;
            }
        }

        player.getPoints().put(CardPoints.VICTORY , nbOfBrownGreyPurpleCards) ;

        Writer.write( player.getName() + " got " + nbOfBrownGreyPurpleCards + " victory points from his neighbors using the shipowners guild card" );
    }
}
