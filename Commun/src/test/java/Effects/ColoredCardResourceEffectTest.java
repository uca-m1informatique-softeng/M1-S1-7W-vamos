package Effects;

import Card.Card;
import Card.CardColor ;
import Card.CardPoints ;
import Player.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

public class ColoredCardResourceEffectTest {
    private Player player;

    @BeforeEach
    void setUp(){
        player = new Player("Maestro") ;
        player.setStrategy(new DumbStrategy());
    }

    @Test
    public void applyColor() throws IOException {
        Player player2 = new Player("Maestro2");
        Player player3 = new Player("Maestro3");

        Card card  = new Card("workersguild" , 7);
        Card card2 = new Card("craftmensguild" , 7);
        Card card3 = new Card("tradersguild" ,7);
        Card card4 = new Card("philosophersguild" , 7);
        Card card5 = new Card("spiesguild" ,7);
        Card card6 = new Card("magistratesguild" ,7);
        Card card7 = new Card("craftmensguild" , 7);

        player2.getBuiltCards().add(card);
        player2.getBuiltCards().add(card2);
        player2.getBuiltCards().add(card3);
        player2.getBuiltCards().add(card4);
        player2.getBuiltCards().add(card5);
        player2.getBuiltCards().add(card6);
        player2.getBuiltCards().add(card7);

        player3.getBuiltCards().add(card);
        player3.getBuiltCards().add(card2);
        player3.getBuiltCards().add(card3);
        player3.getBuiltCards().add(card4);
        player3.getBuiltCards().add(card5);
        player3.getBuiltCards().add(card6);

        player.setPrevNeighbor(player2);
        player.setNextNeighbor(player3);

        ArrayList<Card> previousPlayerBuiltCards = player.getPrevNeighbor().getBuiltCards();
        ArrayList<Card> NextPlayerBuiltCards = player.getNextNeighbor().getBuiltCards();

        int nbOfCards = 0 ; //number of cards who have the color cardColor

        for (int i = 0; i < previousPlayerBuiltCards.size(); i++) {
            if (previousPlayerBuiltCards.get(i).getColoredCardResourceEffect() == CardColor.GREY ) {
                nbOfCards = nbOfCards + 2; //only grey cards will give 2 victory points per grey card in the neighbors cards
            }else{
                    nbOfCards ++ ;
                }
        }

        for (int i = 0; i < NextPlayerBuiltCards.size(); i++) {
            if (NextPlayerBuiltCards.get(i).getColoredCardResourceEffect() == CardColor.GREY ) {
                nbOfCards = nbOfCards + 2; //only grey cards will give 2 victory points per grey card in the neighbors cards
            }else{
                    nbOfCards ++ ;
                }
            }

        int currentVictoryPoints = player.getPoints().get(CardPoints.VICTORY);
        player.getPoints().put(CardPoints.VICTORY, currentVictoryPoints + nbOfCards   );
        System.out.println(nbOfCards);

        assertEquals(player.getPoints().get(CardPoints.VICTORY) , 16);

        }

}
