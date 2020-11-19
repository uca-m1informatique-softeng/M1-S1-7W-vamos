package player;

import card.Card;
import card.CardColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class GuaranteedStrategyTest {

    GuaranteedStrategy g;

    @Mock
    Player player;
    @Mock
    Card c1;
    @Mock
    Card c2;

    @BeforeEach
    void setUp() {
        g = new GuaranteedStrategy(player);
        doReturn((Strategy) g).when(player).getStrategy();
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
        test_hand.add(c1);
        test_hand.add(c2);
        doReturn(test_hand).when(player).getHand();
        doReturn(CardColor.GREEN).when(c1).getColor();
        doReturn(CardColor.GREY).when(c2).getColor();
        doReturn(1).when(c1).getAge();
        //Test the case when the cond if is false, true
        doReturn(false).when(player).alreadyBuilt(any(Card.class));
        doReturn(true).when(player).isBuildable(any(Card.class));
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
        assertArrayEquals(oracle, res);
    }

    @Test
    void chooseAction() {
        //Init hand and card mock object
        ArrayList<Card> test_hand = new ArrayList<>(2);
        test_hand.add(c1);
        test_hand.add(c2);
        doReturn(test_hand).when(player).getHand();
        doReturn(CardColor.GREEN).when(c1).getColor();
        doReturn(CardColor.GREY).when(c2).getColor();
        doReturn(1).when(c1).getAge();
        doReturn(1).when(c2).getAge();
        //Test the case when the cond if is false, true
        doReturn(false).when(player).alreadyBuilt(any(Card.class));
        doReturn(true).when(player).isBuildable(any(Card.class));
        GuaranteedStrategy g = ((GuaranteedStrategy) player.getStrategy());
        //Age 1 Fixed at priority GREEN -> GREY Card c1 is a GREEN card, so we expect this one.
        Action res = g.chooseAction(player);
        assertEquals(c1, res.getCard());
        assertEquals(Action.BUILD, res.getAction()); //chooseAction already choose 3 : build card
        //Test if there are no green card
        test_hand.remove(c1);
        res = g.chooseAction(player);
        assertEquals(c2, res.getCard());
        //Test if there are no color wanted
        doReturn(CardColor.PURPLE).when(c2).getColor();
        res = g.chooseAction(player);
        assertFalse(CardColor.GREY == res.getCard().getColor() || CardColor.GREEN == res.getCard().getColor());
        assertEquals(c2, res.getCard());
    }

    @Test
    void initArray() {
        int size = 4;
        ArrayList<Integer>[] arr = ((GuaranteedStrategy) player.getStrategy()).initArray(size);
        assertEquals(size, arr.length);
        for (int i = 0; i < size; i++) { assertNotNull(arr[i]); }
    }
}