package Core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import Core.Card;
import Core.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import org.mockito.Mock;

public class PlayerTest {
    private Card chosenCard;
    private int coins;
    private int points;
    private ArrayList<Card> hand ;
    Player player;

    @BeforeEach
    void setUp(){
        coins=0;
        points=0;
        hand = new ArrayList<>(7);
        player = new Player("Robot");
    }

    @Test
    public void getName(){
        assertEquals("Robot",player.getName());
    }

    @Test
    public void setName(){
        player.setName("SmartPlayer");
        assertEquals("SmartPlayer",player.getName());
    }

    @Test
    public void getCoins(){
        assertEquals(0,player.getCoins());
    }

    @Test
    public void setCoins(){
        player.setCoins(10);
        assertEquals(10,player.getCoins());
    }

    @Test
    public void getPoints(){
        assertEquals(0,player.getPoints().get(CardPoints.VICTORY));
    }

    @Test
    public void setPoints(){
        player.getPoints().put(CardPoints.VICTORY, 7);
        assertEquals(7,player.getPoints().get(CardPoints.VICTORY));
    }

    @Test
    public void getChosenCard(){
        assertEquals(player.getChosenCard(),chosenCard);
    }

    @Test
    public void setChosenCard(){
        try {
            Card card = new Card("altar", 3);;
            player.setChosenCard(card);
            assertEquals(card,player.getChosenCard());
        } catch (IOException e) {
            assertTrue(false);
        }
    }

    @Test
    public void getHand(){
        assertEquals(hand,player.getHand());
    }

    @Test
    public void setHand(){
        ArrayList<Card> hand2=new ArrayList<>(7);
        player.setHand(hand2);
        assertEquals(hand2,player.getHand());
    }

    @Test
    public void chooseCard(){
        assertEquals(player.getChosenCard(),chosenCard);
    }

    @Test
    public void buildCard() {
        try {
            chosenCard = new Card("altar", 3);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int oldVP = player.getPoints().get(CardPoints.VICTORY);

        ArrayList<Card> hand = new ArrayList<>();
        hand.add(chosenCard);
        player.setHand(hand);
        player.setChosenCard(chosenCard);
        player.buildCard();

        int newVP = player.getPoints().get(CardPoints.VICTORY);

        assertTrue(player.getBuiltCards().contains(chosenCard) && newVP > oldVP);
    }

}
