package Core;

import Card.Card;
import Card.CardManager;
import Exceptions.WondersException;
import Player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static Utility.Constante.MAX_HAND;
import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    private Game game;
    private static int round ;
    private static int players;
    private static int currentAge ;
    private static ArrayList<Player> playersArray;
    private static GameState state;
    private ArrayList<Card> deck;


    @BeforeEach
    void setUp() throws WondersException {
        game = new Game(3);

        players = 3;
        round = 1;
        currentAge = 1;

        playersArray=new ArrayList<>(players);
        playersArray.add(new Player("Bot0"));
        playersArray.add(new Player("Bot1"));
        playersArray.add(new Player("Bot2"));


        state=GameState.START;
        deck = new ArrayList<>();
    }

    @Test
    public void getRound(){
        assertEquals(round,game.getRound());
    }

    @Test
    public void getPlayers(){
        assertEquals(players,game.getPlayers());
    }

    @Test
    public void getCurrentAge(){
        assertEquals(currentAge,game.getCurrentAge());
    }

    /*@Test
    public void getPlayersArray(){
        assertEquals(playersArray,game.getPlayersArray());
    }

     */

    @Test
    public void getState(){
        assertEquals(state,game.getState());
    }


    @Test
    public void initDeck(){
        ArrayList<Card> stack = CardManager.getAgeNDeck(this.currentAge);
        for(int i = stack.size(); i < MAX_HAND * players;i++ )
            stack.add(stack.get(0));
        deck = stack;

        game.initDeck();
        assertEquals(deck.size(),game.getDeck().size());

        for (int i = 0; i < game.getDeck().size(); i++) {
            assertTrue(game.getDeck().get(i) instanceof Card);
        }
    }

     @Test
     public void initPlayers(){
         for(int i = 0; i < players; i++) {
             playersArray.add(new Player("Bot" + i));
         }

         game.initPlayers();
         assertEquals(playersArray.size(),game.getPlayersArray().size());

         for (int i = 0; i < game.getPlayersArray().size(); i++) {
             assertTrue(game.getPlayersArray().get(i) instanceof Player);
         }
     }

}