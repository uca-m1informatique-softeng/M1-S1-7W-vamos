package player;

import card.Card;
import card.CardColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class GuarantedStrategyTest {

    Player player;
    Card c1, c2;
    Player pRight;

    @BeforeEach
    void setUp() {
        player = new Player("Guaranted");
        player.setStrategy(new GuarantedStrategy());
        pRight = new Player("Scientist");
        c1 = new Card("scientistsguild", 3);
        c2 = new Card("altar", 3);
        ArrayList<Card> tmp = new ArrayList<>(2);
        tmp.add(c1);
        tmp.add(c2);
        player.setHand(tmp);
    }

    @Test
    void testChooseAction() {
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

    }
}