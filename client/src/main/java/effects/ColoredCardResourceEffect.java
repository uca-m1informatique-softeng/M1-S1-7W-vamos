package effects;

import card.*;
import player.Player;
import utility.Writer;
import java.util.ArrayList;
import java.util.EnumMap;

/**
 * This class defines the ability of a card of color x to let her owner
 * get victory point(s) per the number of x cards in both neighboring cities.
 */
public class ColoredCardResourceEffect extends Effect{
    private CardColor cardColor ;

    public ColoredCardResourceEffect(CardColor cardColor) {
        this.cardColor = cardColor ;
    }

    public CardColor getCardColor() {
        return cardColor;
    }

    public void applyEffect(Player player , CardColor cardColor, Integer age, EnumMap<Resource, Integer> cost, ArrayList<Card> discardCards){
        ArrayList<Card> previousPlayerBuiltCards = player.getPrevNeighbor().getBuiltCards();
        ArrayList<Card> NextPlayerBuiltCards = player.getNextNeighbor().getBuiltCards();

        int nbOfCards = 0 ; //number of cards who have the color cardColor

        for (int i = 0; i < previousPlayerBuiltCards.size(); i++) {
            if (previousPlayerBuiltCards.get(i).getColor() == cardColor ){
                if (cardColor == CardColor.GREY){
                    nbOfCards = nbOfCards + 2; //only grey cards will give 2 victory points per grey card in the neighbors cards
                }
                else{
                    nbOfCards ++ ;
                }
            }
        }

        for (int i = 0; i < NextPlayerBuiltCards.size(); i++) {
            if (NextPlayerBuiltCards.get(i).getColor() == cardColor ){
                if (cardColor == CardColor.GREY){
                    nbOfCards = nbOfCards + 2; //only grey cards will give 2 victory points per grey card in the neighbors cards
                }
                else{
                    nbOfCards ++ ;
                }
            }
        }

        int currentVictoryPoints = player.getPoints().get(CardPoints.VICTORY);
        player.getPoints().put(CardPoints.VICTORY, currentVictoryPoints + nbOfCards   );

        Writer.write(player.getName() + " got " + nbOfCards + " victory points from his neighbors ");


    }

}
