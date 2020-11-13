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
class GuarantedStrategyTest {

    GuarantedStrategy g;

    @Mock
    Player player;
    @Mock
    Card c1;
    @Mock
    Card c2;

    @BeforeEach
    void setUp() {
        g = new GuarantedStrategy();
        doReturn((Strategy) g).when(player).getStrategy();
    }

    @Test
    void testquickList() {
        GuarantedStrategy g = (GuarantedStrategy) player.getStrategy();
        ArrayList<CardColor> priority = g.quickList(CardColor.GREEN, CardColor.GREY);
        //Is in the good order.
        assertEquals(CardColor.GREEN, priority.get(0));
        assertEquals(CardColor.GREY, priority.get(1));
    }

    @Test
    void testReduceChoice() {
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
        ((GuarantedStrategy) player.getStrategy()).init(player);
        int size = 2;
        ArrayList<Integer>[] oracle = new ArrayList[size];
        ArrayList<Integer>[] res = ((GuarantedStrategy) player.getStrategy()).reduceChoice();
        //GREEN -> GREY we verify the array have the good size.
        assertEquals(size, res.length);
        //Create oracle
        oracle[0] = new ArrayList<>();
        oracle[1] = new ArrayList<>();
        oracle[0].add(0);
        oracle[1].add(1);
        //Test if the result of reduceChoice() do what is expected.
        assertArrayEquals(oracle, res);
    }
}