package effects;

import card.*;
import player.Player;
import utility.Writer;
import java.util.ArrayList;
import java.util.EnumMap;

public class ShipOwnersGuildEffect extends Effect{

    /**
     * Give one victory point for each card of color purple, brown and grey build.
     * @param player Player to apply the effect to
     */
    @Override
    public void applyEffect(Player player, CardColor color, Integer age, EnumMap<Resource, Integer> cost, ArrayList<Card> discardCards){
        ArrayList<Card> playerBuiltCards;
        playerBuiltCards = player.getBuiltCards() ;

        int nbOfBrownGreyPurpleCards = 0 ;

        for (Card builtCard : playerBuiltCards) {
            if (builtCard.getColor() == CardColor.BROWN ||
                builtCard.getColor() == CardColor.GREY ||
                builtCard.getColor() == CardColor.PURPLE) {
                nbOfBrownGreyPurpleCards++;
            }
        }

        player.getPoints().put(CardPoints.VICTORY , nbOfBrownGreyPurpleCards) ;

        Writer.write( player.getName() + " got " + nbOfBrownGreyPurpleCards + " victory points from his neighbors using the shipowners guild card" );
    }
}
