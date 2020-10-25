package effects;

import card.Card;
import card.CardColor ;
import card.CardPoints;
import player.*;
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
        Card card = new Card("spiesguild" ,7); //purple card
        Card card2 = new Card("mine" ,7); //brown card
        Card card3 = new Card("glassworks" ,7); //grey card

        player.getBuiltCards().add(card) ;
        player.getBuiltCards().add(card2) ;
        player.getBuiltCards().add(card3) ;

        ArrayList<Card> playerBuiltCards = player.getBuiltCards() ;

        int nbOfBrownGreyPurpleCards = 0 ;

        for (Card c : playerBuiltCards) {
            if (c.getColor() == CardColor.BROWN ||
                c.getColor() == CardColor.GREY ||
                c.getColor() == CardColor.PURPLE) {
                nbOfBrownGreyPurpleCards++;
            }
        }

        player.getPoints().put(CardPoints.VICTORY , nbOfBrownGreyPurpleCards) ;

        assertEquals(3, player.getPoints().get(CardPoints.VICTORY));
    }
}
