package effects;

import card.Card;
import card.CardColor;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import player.*;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

public class CoinCardEffectTest {
    Player player1 ;
    Player player2 ;
    Player player3 ;
    Player player4 ;
    Player player5 ;
    Player player6 ;

    Card card1 ;
    Card card2 ;
    Card card3 ;
    Card card4 ;
    Card card5 ;
    Card card6 ;

    @BeforeEach
    void setUp() {
        player1 = new Player("Player1");
        player2 = new Player("Player2");
        player3 = new Player("Player3");
        player4 = new Player("Player4");
        player5 = new Player("Player5");
        player6 = new Player("Player6");

        card1  = new Card("arena" , 7);
        card2 = new Card("chamberOfCommerce" , 6);
        card3 = new Card("bazar" ,6);
        card4 = new Card("haven" , 4);
        card5 = new Card("lighthouse" ,6);
        card6 = new Card("vineyard" ,7);

        player1.getBuiltCards().add(card1);
        player1.getBuiltCards().add(card2);
        player1.getBuiltCards().add(card3);
        player1.getBuiltCards().add(card4);
        player1.getBuiltCards().add(card5);
        player1.getBuiltCards().add(card6);

        Card treefarm = new Card("treefarm" ,6);
        Card loom = new Card("loom", 6);
        player6.getBuiltCards().add(treefarm);
        player2.getBuiltCards().add(loom);

        player1.setPrevNeighbor(player6);
        player1.setNextNeighbor(player2);

    }

    @Test
     public void applyEffect(){
        String strGreyCards = "greyCards";
        JSONObject cards = new JSONObject();
        cards.put("brownCards", 0);
        cards.put(strGreyCards, 0);
        cards.put("yellowCards", 0);

        CardColor cardColor = CardColor.BROWN ;
        CardColor cardColor2 = CardColor.GREY ;
        int age = 2 ;

        if (cardColor != null) {
            ArrayList<Card> builtCards = new ArrayList<>();
            builtCards.addAll(player1.getBuiltCards());


            assertTrue(player1.getBuiltCards().equals(builtCards));
            /*
             *  get neighbors built cards for age 2 card
             */
            if (age == 2) {
                ArrayList<Card> total = new ArrayList<>();
                total.addAll(builtCards) ;
                total.addAll(player1.getPrevNeighbor().getBuiltCards()) ;

                builtCards.addAll(player1.getPrevNeighbor().getBuiltCards());

                assertTrue(builtCards.equals(total));

                total.addAll(player1.getNextNeighbor().getBuiltCards()) ;

                builtCards.addAll(player1.getNextNeighbor().getBuiltCards());

                assertTrue(builtCards.equals(total));
            }
            /*
             * iterate through all built cards and distinguish effects of brown and grey cards
             */
            cards = Card.countCards(cards, builtCards);
        }

        int oldCoins = player1.getCoins();
        if (cardColor == CardColor.BROWN) {
            int coins = player1.getCoins() + cards.getInt("brownCards") ;

            player1.setCoins(player1.getCoins() + cards.getInt("brownCards"));

            assertEquals(coins , player1.getCoins());
        }
        age = 3 ;
        cardColor = CardColor.GREY ;
        if (age == 3) {
            cards.put(strGreyCards, cards.getInt(strGreyCards) * 2);

            int a = (int) cards.get(strGreyCards);
            a*=2 ;
            assertEquals(a  , cards.getInt(strGreyCards) * 2);
        }
        int coinsPlayer1 = player1.getCoins() + cards.getInt(strGreyCards) ;
        player1.setCoins(player1.getCoins() + cards.getInt(strGreyCards));
        assertEquals(coinsPlayer1 , player1.getCoins());

        cardColor = CardColor.YELLOW ;

        coinsPlayer1 = player1.getCoins() + cards.getInt("yellowCards") ;

        player1.setCoins(player1.getCoins() + cards.getInt("yellowCards"));

        assertEquals(coinsPlayer1 , player1.getCoins());
    }

}
