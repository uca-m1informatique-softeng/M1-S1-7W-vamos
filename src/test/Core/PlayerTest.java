package Core;

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
        player=new Player("Robot");
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
        assertEquals(0,player.getPoints());
    }

    @Test
    public void setPoints(){
        player.setPoints(7);
        assertEquals(7,player.getPoints());
    }

    @Test
    public void getChosenCard(){
        assertEquals(player.getChosenCard(),chosenCard);
    }

    @Test
    public void setChosenCard(){
        Card card=new Card();
        player.setChosenCard(card);
        assertEquals(card,player.getChosenCard());
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
    public void playCard(){
        chosenCard=new Card();
        player.setChosenCard(chosenCard);
        player.playCard();

        ArrayList<Card> hand = new ArrayList<>(6);
        hand.add(chosenCard);
        hand.remove(chosenCard);

        assertEquals(hand,player.getHand());
    }

}