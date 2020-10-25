package core;

import card.Card;
import card.CardManager;
import card.CardPoints;
import exceptions.AgeException;
import exceptions.PlayerNumberException;
import org.json.JSONObject;
import org.junit.Ignore;
import player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utility.Writer;
import wonder.Wonder;

import java.io.IOException;
import java.util.ArrayList;
import static utility.Constante.MAX_HAND;
import static org.junit.jupiter.api.Assertions.*;
import static utility.Constante.RED_BOLD;

class GameTest {

    private Game game;
    private static int round ;
    private static int players;
    private static int currentAge ;
    private static ArrayList<Player> playersArray;
    private static GameState state;
    private ArrayList<Card> deck;
    Player player ;
    Player player2 ;
    Player player3 ;
    Card card ;
    Card card2 ;
    Card card3 ;
    Wonder wonder ;


    @BeforeEach
    void setUp() throws IOException {
        try {
            game = new Game(3);
        } catch (PlayerNumberException e) {
            Writer.write(RED_BOLD + "Could not launch game ! " + e);
        }

        players = 3;
        round = 1;
        currentAge = 1;

        wonder = new Wonder("gizahA") ;

        card = new Card("chamberOfCommerce" , 6) ;
        card2 = new Card("caravansery" , 6) ; //card.getCoinCardEffect() == null
        card3 = new Card("vineyard" , 6) ;
        player = new Player("Bot1") ;
        player2 = new Player("Bot2") ;
        player3 = new Player("Bot3") ;
        player.getBuiltCards().add(card);
        player.getBuiltCards().add(card2);
        player.getBuiltCards().add(card3);

        player.setWonder(wonder);
        player.getWonder().setState(2);

        playersArray=new ArrayList<>(players);
        playersArray.add(player);
        playersArray.add(new Player("Bot1"));
        playersArray.add(new Player("Bot2"));


        state=GameState.START;

        game.initDeck();
        deck = new ArrayList<>() ;
    }

    @Test
    void initDeck() throws IOException {
        ArrayList<Card> stack = (ArrayList<Card>) CardManager.getAgeNDeck(this.currentAge);
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
     void initPlayers(){
         for(int i = 0; i < players; i++) {
             playersArray.add(new Player("Bot" + i));
         }

         game.initPlayers();
         assertEquals(playersArray.size(),game.getPlayersArray().size());

         for (int i = 0; i < game.getPlayersArray().size(); i++) {
             assertTrue(game.getPlayersArray().get(i) instanceof Player);
         }
     }

     @Test
     void fight() throws AgeException {
        player = game.getPlayersArray().get(0) ;
        player2 = game.getPlayersArray().get(1) ;

        //if we get inside the if
        player.getPoints().put(CardPoints.MILITARY , 7);
        player2.getPoints().put(CardPoints.MILITARY , 5);
        game.fight(player , player2);
        assertEquals(8 , player.getMilitaryPoints() ); // it's age 1 : player win the fight against player2 and earns 1 military might

         game.setCurrentAge(2);
         game.fight(player , player2);
         assertEquals(11 , player.getMilitaryPoints() ); // it's age 2 : player win the fight against player2 and earns 3 military might

         game.setCurrentAge(3);
         game.fight(player , player2);
         assertEquals(16 , player.getMilitaryPoints() );

         //if we get inside the else
         player.getPoints().replace(CardPoints.MILITARY , 5) ;
         player2.getPoints().replace(CardPoints.MILITARY , 7) ;

         game.fight(player , player2);

         assertEquals(4 , player.getMilitaryPoints() );
         assertEquals(1 , player.getDefeatToken() );

     }

     @Test
      void swapHands(){
         ArrayList<ArrayList<Card>> tmpList = new ArrayList<>();
         for (Player player : playersArray) {
             tmpList.add(new ArrayList<>(player.getHand()));
         }

         //if we get inside the if
         currentAge = 3 ;
         for (int i = 0; i < playersArray.size(); i++) {
             if (currentAge % 2 == 1) {
                 //Clockwise trade
                 if (i != 0) {
                     playersArray.get(i).setHand(tmpList.get(i - 1));
                 }
                 else {
                     playersArray.get(i).setHand(tmpList.get(playersArray.size() - 1));
                 }
             } else {
                 //Counter clockwise
                 if (i == 0)
                     playersArray.get(playersArray.size() - 1).setHand(tmpList.get(i));
                 else
                     playersArray.get(i - 1).setHand(tmpList.get(i));
             }
         }
         assertEquals(playersArray.size() , game.getPlayersArray().size());

         for (int i = 0; i < playersArray.size(); i++) {
             assertEquals(playersArray.get(i).getHand() , game.getPlayersArray().get(i).getHand());
         }

         game.swapHands(3);

         for (int i = 0; i < playersArray.size(); i++) {
             assertEquals(playersArray.get(i).getHand() , game.getPlayersArray().get(i).getHand());
         }

         //if we get inside the else
         currentAge = 2 ;
         game.swapHands(3);

         for (int i = 0; i < playersArray.size(); i++) {
             assertEquals(playersArray.get(i).getHand() , game.getPlayersArray().get(i).getHand());
         }
     }

     @Test
      void initPlayersHand() throws IOException {
         game.initDeck();
         deck.addAll(game.getDeck()) ;

         for (Player player : this.playersArray) {
             for (int i = 0; i < MAX_HAND; i++) {
                 player.getHand().add(this.deck.remove(0));
             }
         }

         game.initPlayersHand() ;
         for (int i = 0; i < playersArray.size(); i++) {
             assertEquals(playersArray.get(i).getHand(), game.getPlayersArray().get(i).getHand());
         }
     }

    @Ignore
     void addVictoryPoints(){
        player = game.getPlayersArray().get(0) ;

        player.setWonder(wonder);
        player.getWonder().setState(2);

        player.getBuiltCards().add(card);
        player.getBuiltCards().add(card2);
        player.getBuiltCards().add(card3);

        System.out.println(card.getCoinCardEffect());
        System.out.println(card2.getCoinCardEffect());
        System.out.println(card3.getCoinCardEffect());
        System.out.println(player.getPoints().get(CardPoints.VICTORY));
        System.out.println(player.getBuiltCards());
        game.addVictoryPoints();


        assertEquals(2 , player.getPoints().get(CardPoints.VICTORY) );

    }

}