package Player;

import Card.*;
import Card.CardColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class IA_OneTest {

    IA_One player;
    Card c1, c2;
    DumbPlayer pright;

    @BeforeEach
    void setUp() {
        player = new IA_One("Scientist");
        pright = new DumbPlayer("Scientist");
        player.setNextNeighbor(pright);
        player.setPrevNeighbor(pright);
        try {
            c1 = new Card("scientistsguild", 3);
            c2 = new Card("altar", 3);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayList<Card> tmp = new ArrayList<Card>(2);
        tmp.add(c1);
        tmp.add(c2);
        player.setHand(tmp);
    }

    @Test
    void chooseCard() {
        player.getResources().put(Resource.WOOD, 2);
        player.getResources().put(Resource.ORE, 2);
        player.getResources().put(Resource.PAPYRUS, 1);
        assertTrue(player.chooseCard(CardColor.PURPLE));
        assertEquals(c1, player.getChosenCard());
        assertTrue(player.chooseCard(CardColor.BLUE));
        assertEquals(c2, player.getChosenCard());
        assertTrue(player.chooseCard(CardColor.PURPLE));
        //TODO correct bug on chooseCard(Effect e)
        //assertTrue(player.chooseCard(new ScienceChoiceEffect()));
    }
}