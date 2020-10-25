package player;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.EnumMap;
import card.*;
import effects.Effect;
import effects.TradeResourceEffect;
import wonder.Wonder;
import effects.ScienceChoiceEffect;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
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
    SecureRandom rand;

    @BeforeEach
    void setUp() throws IOException {
        coins=0;
        militaryPoints=0 ;
        hand = new ArrayList<>(7);
        player = new Player("Robot");
        player.setStrategy(new DumbStrategy());
        player.rand = rand; //assigner le mock au rand de player
        builtCards = new ArrayList<>();
        player.setPrevNeighbor(new Player("PreviousRobot"));
        player.setNextNeighbor(new Player("NextRobot"));

        points = new EnumMap<>(CardPoints.class);
        for (CardPoints p : CardPoints.values()) {
            this.points.put(p, 0);
        }
        player.wonder = new Wonder("babylonA");
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
        Card card = new Card("altar", 3);
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
    public void addPointsAndResources() {
        chosenCard0 = new Card("sawmill", 3);
        chosenCard = new Card("altar", 3);
        chosenCard2 = new Card("laboratory", 3);//SCIENCE_WHEEL
        chosenCard3 = new Card("observatory", 3);//SCIENCE_TABLET
        chosenCard4 = new Card("academy", 3);

        //checking the points added when the card gives VICTORY points
        int oldVP = player.getPoints().get(CardPoints.VICTORY);

        ArrayList<Card> hand = new ArrayList<>();
        //Buy a card with 1 coin cost, and return 2 WOOD
        hand.add(chosenCard0);
        player.setHand(hand);
        player.setChosenCard(chosenCard0);
        player.setCoins(1);


        player.buildCard();
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

        assertTrue(player.buyResource(resourceToBuy, 1, neighbor));
        assertEquals(1, player.getBoughtResources().get(Resource.CLAY));
        assertEquals(oldCoins, player.getCoins());
    }

    @Test
    public void buyResource2() {
        Resource resourceToBuy = Resource.CLAY;
        Player neighbor = new Player("Neighbor");

        player.setCoins(0);
        neighbor.getResources().put(resourceToBuy, 2);

        assertFalse(player.buyResource(resourceToBuy, 1, neighbor));
    }

    @Test
    public void buyResource3() {
        Resource resourceToBuy = Resource.CLAY;
        Player neighbor = new Player("Neighbor");

        player.setCoins(2);

        assertFalse(player.buyResource(resourceToBuy, 1, neighbor));
    }

    @Test
    public void clearBoughtResources() {
        Resource resourceToBuy = Resource.CLAY;
        Player neighbor = player.getNextNeighbor();

        int oldCoins = player.getCoins();
        player.setCoins(oldCoins + 2);
        neighbor.getResources().put(resourceToBuy, 2);

        player.buyResource(resourceToBuy, 1, neighbor);
        player.clearBoughtResources();

        EnumMap<Resource, Integer> emptyMap = new EnumMap<>(Resource.class);
        for (Resource r : Resource.values()) {
            emptyMap.put(r, 0);
        }
        assertEquals(emptyMap, player.getBoughtResources());
    }

    @Test
    public void buildCardWithTradedResource() {
        Card baths = new Card("baths", 6);
        player.setCoins(1);
        player.getBuiltCards().add(new Card("eastTradingPost", 6));
        player.getNextNeighbor().getResources().put(Resource.STONE, 3);
        player.setChosenCard(baths);
        player.buildCard();
        assertTrue(player.getBoughtResources().containsKey(Resource.STONE));
        assertTrue(player.getBuiltCards().contains(baths));
        //Try to buy 2 same card
        assertTrue(player.getBuiltCards().contains(baths));
        assertSame(player.getChosenCard(), baths);
        assertFalse(player.buildCard());
    }


    @Test
    void endApplyEffect() {
        //Test if the ScienceChoiceEffect is applied
        player.getBuiltCards().add(new Card("scientistsguild", 7));
        assertEquals(0, player.getSciencePoint());
        player.endApplyEffect();
        assertEquals(1, player.getSciencePoint());
        player.endApplyEffect();
        assertEquals(4, player.getSciencePoint());
        player.endApplyEffect();
        assertEquals(9, player.getSciencePoint());

        //Test if the cumulative effect is applied.
        player.wonderEffectNotApply.add(new ScienceChoiceEffect());
        player.endApplyEffect();
        assertEquals(25, player.getSciencePoint());

        //Test if ScienceCHoiceEffect is applied when there are just wonder effect
        player.getBuiltCards().remove(0);
        for(Card c : player.getBuiltCards()){ assertFalse(c.getEffect() instanceof ScienceChoiceEffect); }
        player.endApplyEffect();
        assertEquals(36, player.getSciencePoint());

        //Test with no ScienceEffect
        player.wonderEffectNotApply.remove(0);
        player.endApplyEffect();
        //Points don't change because there are no science effect to apply.
        assertEquals(36, player.getSciencePoint());
    }

    @Test
    void alreadyBuilt() {
        Card card1 = new Card("baths", 3);
        Card card2 = new Card("baths", 3);
        Card card3 = new Card("academy", 3);
        player.getBuiltCards().add(card1);
        assertTrue(player.alreadyBuilt(card2));
        assertFalse(player.alreadyBuilt(card3));
    }

    @Test
    void buyStageWonderWithEffect() throws IOException {
        player.wonder = new Wonder("olympiaB");
        for(Resource resource : player.getResources().keySet()){
            player.getResources().put(resource, 10);
        }
        assertTrue(player.buildStageWonder());
        assertTrue(player.buildStageWonder());
        assertTrue(player.buildStageWonder());
        boolean check = false;
        for(Effect e : player.wonder.getAppliedEffects()){
            if (e instanceof TradeResourceEffect){
                check = true;
                break;
            }
        }
        assertTrue(check);
    }


    @Test
    void addWonderRewardTest() throws IOException {
        player.wonder = new Wonder("rhodosB");
        player.addWonderReward();
        assertEquals(1, player.getMilitaryPoints());
        assertEquals(3, player.getPoints().get(CardPoints.VICTORY));
        assertEquals(3, player.getCoins());
        player.wonder.setState(player.wonder.getState() + 1);
        player.addWonderReward();
        assertEquals(2, player.getMilitaryPoints());
        assertEquals(7, player.getPoints().get(CardPoints.VICTORY));
        assertEquals(7, player.getCoins());
    }
}
