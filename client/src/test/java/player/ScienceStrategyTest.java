package player;

import card.Card;
import card.Resource;
import card.CardColor;
import effects.ScienceChoiceEffect;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;

class ScienceStrategyTest {

    Player player;
    Card c1, c2;
    Player pRight;

    @BeforeEach
    void setUp() {
        player = new Player("Scientist");
        player.setStrategy(new ScienceStrategy());
        pRight = new Player("Scientist");
        player.setNextNeighbor(pRight);
        player.setPrevNeighbor(pRight);
        c1 = new Card("scientistsguild", 3);
        c2 = new Card("altar", 3);
        ArrayList<Card> tmp = new ArrayList<>(2);
        tmp.add(c1);
        tmp.add(c2);
        player.setHand(tmp);
    }


    @Test
    void chooseCard() {
        player.getResources().put(Resource.WOOD, 2);
        player.getResources().put(Resource.ORE, 2);
        player.getResources().put(Resource.PAPYRUS, 1);
        ScienceStrategy ss = (ScienceStrategy) player.strategy;
        //choose the purple card
        assertTrue(ss.chooseCard(player, CardColor.PURPLE));
        assertEquals(c1, ss.chosenCard);
        //choose the blue card
        assertTrue(ss.chooseCard(player, CardColor.BLUE));
        assertEquals(c2, ss.chosenCard);
        //choose card by this effect
        assertTrue(ss.chooseCard(player, ScienceChoiceEffect.class));
        assertTrue(ss.chooseCard(player, CardColor.PURPLE));
        //Test when the chosen card don't have effect
        player.hand.remove(c1);
        assertFalse(ss.chooseCard(player, ScienceChoiceEffect.class));
    }

}
