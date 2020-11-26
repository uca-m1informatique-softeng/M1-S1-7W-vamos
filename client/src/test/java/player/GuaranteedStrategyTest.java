package player;

import card.Card;
import card.CardColor;
import card.CardPoints;
import card.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.security.SecureRandom;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class GuaranteedStrategyTest {

    GuaranteedStrategy guaranteedStrategy;
    Player player;
    Card c1;
    Card c2;

    @Mock
    SecureRandom rand;

    @BeforeEach
    void setUp() {
        this.player = new Player("bob");
        this.guaranteedStrategy = new GuaranteedStrategy(player);
        player.setPrevNeighbor(new Player("jean"));
        player.setNextNeighbor(new Player("jacques"));
        player.setStrategy(guaranteedStrategy);
        player.getStrategy().rand = this.rand;
    }

    @Test
    void testquickList() {
        GuaranteedStrategy g = (GuaranteedStrategy) player.getStrategy();
        ArrayList<CardColor> priority = g.quickList(CardColor.GREEN, CardColor.GREY);
        //Is in the good order.
        assertEquals(CardColor.GREEN, priority.get(0));
        assertEquals(CardColor.GREY, priority.get(1));
    }

    @Test
    void testcardGroupedByPriorityColor() {
        //Init hand and card mock object
        ArrayList<Card> test_hand = new ArrayList<>(2);
        c1 = new Card("apothecary", 3);
        c2 = new Card("loom", 3);
        test_hand.add(c1);
        test_hand.add(c2);
        this.player.setHand(test_hand);

        //Test the case when the cond if is false, true
        int size = 2;
        ArrayList<Integer>[] oracle = new ArrayList[size];
        ArrayList<Integer>[] res = ((GuaranteedStrategy) player.getStrategy()).cardGroupedByPriorityColor();

        //GREEN -> GREY we verify the array have the good size.
        assertEquals(size, res.length);

        //Create oracle
        oracle[0] = new ArrayList<>();
        oracle[1] = new ArrayList<>();
        oracle[0].add(0);
        oracle[1].add(1);

        //Test if the result of cardGroupedByPriorityColor() do what is expected.
        //assertArrayEquals(oracle, res);
    }

    @Test
    void chooseAction() {
        //Init hand and card mock object
        c1 = new Card("apothecary", 3);
        c2 = new Card("loom", 3);
        this.player.getHand().add(c1);
        this.player.getHand().add(c2);
        this.player.getResources().put(Resource.LOOM, 1);

        //Age 1 Fixed at priority GREEN -> GREY Card c1 is a GREEN card, so we expect this one.
        Action res = this.guaranteedStrategy.chooseAction(player);
        assertEquals(c1, res.getCard());
        assertEquals(Action.BUILD, res.getAction()); //chooseAction already choose 3 : build card

        //Test if there are no green card
        this.player.getHand().remove(c1);
        res = this.guaranteedStrategy.chooseAction(player);
        assertEquals(c2, res.getCard());

        //Test if there are no color wanted
        // TODO
        /*
        doReturn(CardColor.PURPLE).when(c2).getColor();
        res = this.guaranteedStrategy.chooseAction(player);
        assertFalse(CardColor.GREY == res.getCard().getColor() || CardColor.GREEN == res.getCard().getColor());
        assertEquals(c2, res.getCard());
        */
    }

    @Test
    void initArray() {
        int size = 4;
        ArrayList<Integer>[] arr = ((GuaranteedStrategy) player.getStrategy()).initArray(size);
        assertEquals(size, arr.length);
        for (int i = 0; i < size; i++) { assertNotNull(arr[i]); }
    }

    @Test
    void getSciencePriority1() {
        this.player.getPoints().put(CardPoints.SCIENCE_COMPASS, 2);
        this.player.getPoints().put(CardPoints.SCIENCE_WHEEL, 1);
        this.player.getPoints().put(CardPoints.SCIENCE_TABLET, 0);

        assertEquals(CardPoints.SCIENCE_COMPASS, ((GuaranteedStrategy) this.player.getStrategy()).getSciencePriority());
    }

    @Test
    void getSciencePriority2() {
        this.player.getPoints().put(CardPoints.SCIENCE_COMPASS, 1);
        this.player.getPoints().put(CardPoints.SCIENCE_WHEEL, 1);
        this.player.getPoints().put(CardPoints.SCIENCE_TABLET, 0);

        assertEquals(CardPoints.SCIENCE_WHEEL, ((GuaranteedStrategy) this.player.getStrategy()).getSciencePriority());
    }

    @Test
    void getSciencePriority3() {
        this.player.getPoints().put(CardPoints.SCIENCE_COMPASS, 0);
        this.player.getPoints().put(CardPoints.SCIENCE_WHEEL, 0);
        this.player.getPoints().put(CardPoints.SCIENCE_TABLET, 0);

        assertEquals(CardPoints.SCIENCE_WHEEL, ((GuaranteedStrategy) this.player.getStrategy()).getSciencePriority());

        this.player.getPoints().put(CardPoints.SCIENCE_COMPASS, 7); // The player has more SCIENCE_COMPASS points
        assertEquals(CardPoints.SCIENCE_COMPASS, ((GuaranteedStrategy) this.player.getStrategy()).getSciencePriority());
    }

    @Test
    void getBestScienceCard1() {
        this.player.getHand().add(new Card("dispensary", 3));
        this.player.getHand().add(new Card("laboratory", 3));
        this.player.getHand().add(new Card("school", 3));

        this.player.getResources().put(Resource.ORE, 2);
        this.player.getResources().put(Resource.CLAY, 2);
        this.player.getResources().put(Resource.STONE, 2);
        this.player.getResources().put(Resource.WOOD, 1);
        this.player.getResources().put(Resource.GLASS, 1);
        this.player.getResources().put(Resource.PAPYRUS, 1);
        this.player.getResources().put(Resource.LOOM, 1);

        this.player.getPoints().put(CardPoints.SCIENCE_COMPASS, 1);
        this.player.getPoints().put(CardPoints.SCIENCE_WHEEL, 1);
        this.player.getPoints().put(CardPoints.SCIENCE_TABLET, 2);

        assertEquals(this.player.getHand().get(2), this.player.getStrategy().chooseAction(this.player).getCard());
        assertEquals(Action.BUILD, this.player.getStrategy().chooseAction(this.player).getAction());
    }

    @Test
    void getBestScienceCard2() {
        this.player.getHand().add(new Card("laboratory", 3));
        this.player.getHand().add(new Card("library", 3));
        this.player.getHand().add(new Card("school", 3));

        this.player.getResources().put(Resource.CLAY, 2);
        this.player.getResources().put(Resource.WOOD, 1);
        this.player.getResources().put(Resource.PAPYRUS, 1);

        this.player.getPoints().put(CardPoints.SCIENCE_COMPASS, 1);
        this.player.getPoints().put(CardPoints.SCIENCE_WHEEL, 1);
        this.player.getPoints().put(CardPoints.SCIENCE_TABLET, 2);

        assertEquals(this.player.getHand().get(2), this.player.getStrategy().chooseAction(this.player).getCard());
        assertEquals(Action.BUILD, this.player.getStrategy().chooseAction(this.player).getAction());
    }

    @Test
    void getBlockingScienceCard1() {
        this.player.getHand().add(new Card("apothecary", 3));
        this.player.getHand().add(new Card("workshop", 3));
        this.player.getHand().add(new Card("scriptorium", 3));
        this.player.getPrevNeighbor().getPoints().put(CardPoints.SCIENCE_WHEEL, 0);
        this.player.getPrevNeighbor().getPoints().put(CardPoints.SCIENCE_COMPASS, 0);
        this.player.getPrevNeighbor().getPoints().put(CardPoints.SCIENCE_TABLET, 2);

        assertEquals(this.player.getHand().get(2), ((GuaranteedStrategy) this.player.getStrategy()).getBlockingScienceCard());
    }

    @Test
    void getBlockingScienceCard2() {
        this.player.getHand().add(new Card("dispensary", 3));
        this.player.getHand().add(new Card("laboratory", 3));
        this.player.getHand().add(new Card("library", 3));
        this.player.getPrevNeighbor().getPoints().put(CardPoints.SCIENCE_WHEEL, 1);
        this.player.getPrevNeighbor().getPoints().put(CardPoints.SCIENCE_COMPASS, 1);
        this.player.getPrevNeighbor().getPoints().put(CardPoints.SCIENCE_TABLET, 0);

        assertEquals(this.player.getHand().get(0), ((GuaranteedStrategy) this.player.getStrategy()).getBlockingScienceCard());
    }

    @Test
    void marketPlaceInHand(){
        // MarketPlace card is not in the player's hand
        assertEquals(false , this.guaranteedStrategy.marketPlaceInHand());

        // We add MarketPlace card to the player's hand
        this.player.getHand().add(new Card("marketplace" , 3));

        assertEquals(true , this.guaranteedStrategy.marketPlaceInHand());
    }

    @Test
    void marketPlaceIndex(){
        // We add MarketPlace card to the player's hand
        Card card = new Card("marketplace" , 3) ;
        this.player.getHand().add(card);

        int marketPlaceIndex = 0 ;
        if(this.guaranteedStrategy.marketPlaceInHand()){
            for (int i = 0; i < this.player.getHand().size(); i++) {
                if (this.player.getHand().get(i).getName().equals("marketplace")){
                    marketPlaceIndex = i ;
                }
            }
            assertEquals(marketPlaceIndex , this.player.getHand().indexOf(card)) ;
        }
        else{ // MarketPlace card is not in the player's hand
            marketPlaceIndex = -1 ;
            assertEquals(marketPlaceIndex , this.guaranteedStrategy.marketPlaceIndex());
        }

    }
}