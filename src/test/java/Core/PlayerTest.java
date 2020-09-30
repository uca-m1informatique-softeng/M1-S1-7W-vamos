package Core;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Random;

import Card.*;
import Player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PlayerTest {
    private Card chosenCard0;
    private Card chosenCard;
    private Card chosenCard2;
    private Card chosenCard3;
    private Card chosenCard4;
    private int coins;
    private int militaryPoints;
    private ArrayList<Card> hand ;
    private ArrayList<Card> builtCards;
    private EnumMap<CardPoints, Integer> points;
    Player player;

    @Mock
    Random rand;

    @BeforeEach
    void setUp(){
        coins=0;
        militaryPoints=0 ;
        hand = new ArrayList<>(7);
        player = new Player("Robot");
        player.rand=rand; //assigner le mock au rand de Player
        builtCards = new ArrayList<>();

        points = new EnumMap<>(CardPoints.class);
        points.put(CardPoints.VICTORY, 0);
        points.put(CardPoints.MILITARY, 0);
        points.put(CardPoints.SCIENCE_COMPASS, 0);
        points.put(CardPoints.SCIENCE_TABLET, 0);
        points.put(CardPoints.SCIENCE_WHEEL, 0);
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
            Card card = new Card("altar", 3);
            player.setChosenCard(card);
            assertEquals(card,player.getChosenCard());
        } catch (IOException e) {
            fail();
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
    public void getMilitaryPoints(){
        assertEquals(militaryPoints,player.getMilitaryPoints());
    }

    @Test
    public void addMilitaryPoints(){
        //before addition
        assertEquals(militaryPoints,player.getMilitaryPoints());
        //after addition
        player.addMilitaryPoints(3);
        assertEquals(3,player.getMilitaryPoints());
    }

    @Test
    public void chooseAction(){
        when(rand.nextInt(1000)).thenReturn(2,1);

        //if we get inside the if statement
        player.chooseAction();

        //if we get inside the else
        try {
            Card card=new Card("altar",3);
            player.setChosenCard(card);
        }
        catch (IOException e){
            e.printStackTrace();
        }

        player.chooseAction();


    }

    @Test
    public void dumpCard(){
        //before method call
        assertEquals(coins,player.getCoins());
        assertEquals(hand,player.getHand());

        hand.remove(chosenCard);
        coins+=3;

        //method call
        player.dumpCard();

        //after method call
        assertEquals(3,player.getCoins());
        assertEquals(coins,player.getCoins());
        assertEquals(hand,player.getHand());
    }

    @Test
    public void buildCard(){
        try {
            chosenCard = new Card("altar", 3);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //cheching that altar is a free card
        assertEquals(true,chosenCard.isFree());

        //method call
        player.setChosenCard(chosenCard);
        player.buildCard();

        builtCards.add(chosenCard);
        assertEquals(builtCards,player.getBuiltCards());

    }

    @Test
    public void addPointsAndResources() {
        try {
            chosenCard0 = new Card("sawmill", 3);
            chosenCard = new Card("altar", 3);
            chosenCard2 = new Card("laboratory", 3);//SCIENCE_WHEEL
            chosenCard3 = new Card("observatory", 3);//SCIENCE_TABLET
            chosenCard4 = new Card("academy", 3);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //checking the points added when the card gives VICTORY points
        int oldVP = player.getPoints().get(CardPoints.VICTORY);

        ArrayList<Card> hand = new ArrayList<>();
        //Buy a card with 1 coin cost, and return 2 WOOD
        hand.add(chosenCard0);
        player.setHand(hand);
        player.setChosenCard(chosenCard0);
        player.setCoins(1);
        player.buildCard();
        System.out.println(this.chosenCard0.getCost().get(Resource.COIN));
        System.out.println(player.getCoins());
        assertEquals(0, player.getCoins());
        assertEquals(2, player.getResources().get(Resource.WOOD));

        hand.add(chosenCard);
        player.setHand(hand);
        player.setChosenCard(chosenCard);
        player.buildCard();

        int newVP = player.getPoints().get(CardPoints.VICTORY);

        assertEquals(player.getPoints().get(CardPoints.VICTORY) , oldVP + newVP);

        //checking the points added when the card gives SCIENCE_WHEEL points
        int oldSW = player.getPoints().get(CardPoints.SCIENCE_WHEEL);

        hand = new ArrayList<>();
        hand.add(chosenCard2);
        player.setHand(hand);
        player.setChosenCard(chosenCard2);
        player.buildCard();

        int newSW = player.getPoints().get(CardPoints.SCIENCE_WHEEL);

        assertEquals(player.getPoints().get(CardPoints.SCIENCE_WHEEL) , oldSW + newSW);

        //checking the points added when the card gives SCIENCE_TABLET points
        int oldST = player.getPoints().get(CardPoints.SCIENCE_TABLET);

        hand = new ArrayList<>();
        hand.add(chosenCard3);
        player.setHand(hand);
        player.setChosenCard(chosenCard3);
        player.buildCard();

        int newST = player.getPoints().get(CardPoints.SCIENCE_TABLET);

        assertEquals(player.getPoints().get(CardPoints.SCIENCE_TABLET) , oldST + newST);

        //checking the points added when the card gives SCIENCE_COMPASS points
        int oldSC = player.getPoints().get(CardPoints.SCIENCE_COMPASS);

        hand = new ArrayList<>();
        hand.add(chosenCard4);
        player.setHand(hand);
        player.setChosenCard(chosenCard4);
        player.buildCard();

        int newSC = player.getPoints().get(CardPoints.SCIENCE_COMPASS);

        assertEquals(player.getPoints().get(CardPoints.SCIENCE_COMPASS) , oldSC + newSC);

        //@TODO
        /*
        check if resources given by a card are given to the player (currently no card who gives resources is implemented)
         */
    }

    @Test
    public void computeScore(){
        int res = 0;

        // Military points
        res += militaryPoints;
        // Treasury Contents
        res += coins/3;
        // Civilian Structures and Wonders
        res += points.get(CardPoints.VICTORY);
        // Science score
        res += points.get(CardPoints.SCIENCE_WHEEL)* points.get(CardPoints.SCIENCE_WHEEL);
        res += points.get(CardPoints.SCIENCE_COMPASS)* points.get(CardPoints.SCIENCE_COMPASS);
        res += points.get(CardPoints.SCIENCE_TABLET)* points.get(CardPoints.SCIENCE_TABLET);
        // Sets of different symbols
        res += 7*Math.min(Math.min(points.get(CardPoints.SCIENCE_TABLET), points.get(CardPoints.SCIENCE_WHEEL)), Math.min(points.get(CardPoints.SCIENCE_WHEEL), points.get(CardPoints.SCIENCE_COMPASS)));
        int resPlayer= player.computeScore();
        assertEquals(res,resPlayer);

        //checking with CardPoints incremented
        player.addMilitaryPoints(1);
        player.getPoints().put(CardPoints.VICTORY,2);
        player.getPoints().put(CardPoints.SCIENCE_WHEEL,3);
        player.getPoints().put(CardPoints.SCIENCE_COMPASS,4);
        player.getPoints().put(CardPoints.SCIENCE_TABLET,10);

        militaryPoints+=1;
        points.put(CardPoints.VICTORY,2);
        points.put(CardPoints.SCIENCE_WHEEL,3);
        points.put(CardPoints.SCIENCE_COMPASS,4);
        points.put(CardPoints.SCIENCE_TABLET,10);
        int res2 = 0;
        // Military points
        res2 += militaryPoints;
        // Treasury Contents
        res2 += coins/3;
        // Civilian Structures and Wonders
        res2 += points.get(CardPoints.VICTORY);
        // Science score
        res2 += points.get(CardPoints.SCIENCE_WHEEL)* points.get(CardPoints.SCIENCE_WHEEL);
        res2 += points.get(CardPoints.SCIENCE_COMPASS)* points.get(CardPoints.SCIENCE_COMPASS);
        res2 += points.get(CardPoints.SCIENCE_TABLET)* points.get(CardPoints.SCIENCE_TABLET);
        // Sets of different symbols
        res2 += 7*Math.min(Math.min(points.get(CardPoints.SCIENCE_TABLET), points.get(CardPoints.SCIENCE_WHEEL)), Math.min(points.get(CardPoints.SCIENCE_WHEEL), points.get(CardPoints.SCIENCE_COMPASS)));
        int resPlayer2= player.computeScore();
        assertEquals(res2,resPlayer2);
    }

    @Test
    public void buyResource1() {
        Resource resourceToBuy = Resource.CLAY;
        Player neighbor = new Player("Neighbor");

        int oldCoins = player.getCoins();
        player.setCoins(oldCoins + 2);
        neighbor.getResources().put(resourceToBuy, 2);

        assertTrue(player.buyResource(resourceToBuy, neighbor));
        assertEquals(player.getBoughtResources().get(Resource.CLAY), 1);
        assertEquals(player.getCoins(), oldCoins);
    }

    @Test
    public void buyResource2() {
        Resource resourceToBuy = Resource.CLAY;
        Player neighbor = new Player("Neighbor");

        player.setCoins(0);
        neighbor.getResources().put(resourceToBuy, 2);

        assertFalse(player.buyResource(resourceToBuy, neighbor));
    }

    @Test
    public void buyResource3() {
        Resource resourceToBuy = Resource.CLAY;
        Player neighbor = new Player("Neighbor");

        player.setCoins(2);

        assertFalse(player.buyResource(resourceToBuy, neighbor));
    }

    @Test
    public void clearBoughtResources() {
        Resource resourceToBuy = Resource.CLAY;
        Player neighbor = new Player("Neighbor");

        int oldCoins = player.getCoins();
        player.setCoins(oldCoins + 2);
        neighbor.getResources().put(resourceToBuy, 2);

        player.buyResource(resourceToBuy, neighbor);
        player.clearBoughtResources();

        EnumMap<Resource, Integer> emptyMap = new EnumMap<>(Resource.class);
        for (Resource r : Resource.values()) {
            emptyMap.put(r, 0);
        }
        assertEquals(emptyMap, player.getBoughtResources());
    }

}
