package Effects;

import Card.Card;
import Player.*;
import Effects.CoinCardEffect;
import org.junit.Test;

import java.io.IOException;

public class CoinCardEffectTest {
    @Test
    //build all yellow cards
    public void setup() throws IOException {
        Player player1 = new Player("Player1");
        Player player2 = new Player("Player2");
        Player player3 = new Player("Player3");
        Player player4 = new Player("Player4");
        Player player5 = new Player("Player5");
        Player player6 = new Player("Player6");

        Card card1  = new Card("arena" , 7);
        Card card2 = new Card("chamberOfCommerce" , 6);
        Card card3 = new Card("bazar" ,6);
        Card card4 = new Card("haven" , 4);
        Card card5 = new Card("lighthouse" ,6);
        Card card6 = new Card("vineyard" ,7);

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
        player2.setPrevNeighbor(player1);
        player2.setNextNeighbor(player3);
        player3.setPrevNeighbor(player2);
        player3.setNextNeighbor(player4);
        player4.setPrevNeighbor(player3);
        player4.setNextNeighbor(player5);
        player5.setPrevNeighbor(player4);
        player5.setNextNeighbor(player6);
        player6.setPrevNeighbor(player5);
        player6.setNextNeighbor(player1);

        this.testEffects(player1, card2);
    }

    public void testEffects(Player player, Card card){

        System.out.println("before " + player.getCoins());
        ((CoinCardEffect) card.getEffect()).applyEffect(player, card.getCoinCardEffect(), card.getAge(), null);
        System.out.println("after " + player.getCoins());

    }

}
