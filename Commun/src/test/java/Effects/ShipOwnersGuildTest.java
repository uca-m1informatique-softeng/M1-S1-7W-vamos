package Effects;

import Card.Card;
import Card.CardColor ;
import Card.CardPoints;
import Player.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ShipOwnersGuildTest {
    Player player ;

    @BeforeEach
    void setUp(){
        player=new Player("Marc") ;
        player.setStrategy(new DumbStrategy());
    }

    @Test
    public void applyEffect(){
        try {
            Card card = new Card("spiesguild" ,7); //purple card
            Card card2 = new Card("mine" ,7); //brown card
            Card card3 = new Card("glassworks" ,7); //grey card

            player.getBuiltCards().add(card) ;
            player.getBuiltCards().add(card2) ;
            player.getBuiltCards().add(card3) ;
        }
        catch (IOException e){
            e.printStackTrace();
        }

        ArrayList<Card> playerBuiltCards = player.getBuiltCards() ;

        int nbOfBrownGreyPurpleCards = 0 ;

        for (int i = 0; i < playerBuiltCards.size(); i++) {
            if(playerBuiltCards.get(i).getColor() == CardColor.BROWN || playerBuiltCards.get(i).getColor() == CardColor.GREY
                    || playerBuiltCards.get(i).getColor() == CardColor.PURPLE ){
                nbOfBrownGreyPurpleCards ++ ;
            }
        }

        player.getPoints().put(CardPoints.VICTORY , nbOfBrownGreyPurpleCards) ;

        assertEquals(player.getPoints().get(CardPoints.VICTORY) , 3);
    }
}
