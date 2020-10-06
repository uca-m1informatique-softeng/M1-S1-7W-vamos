package Player;

import Card.Card;
import Card.Resource;
import Card.CardColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;
import java.util.ArrayList;

public class ScienceStrategyTest {

    Player player;
    Card c1, c2;
    Player pright;

    @BeforeEach
    void setUp() {
        player = new Player("Scientist");
        player.setStrategy(new ScienceStrategy());
        pright = new Player("Scientist");
        player.setNextNeighbor(pright);
        player.setPrevNeighbor(pright);
        try {
            c1 = new Card("scientistsguild", 3);
            c2 = new Card("altar", 3);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayList<Card> tmp = new ArrayList<>(2);
        tmp.add(c1);
        tmp.add(c2);
        player.setHand(tmp);
    }

    /*
    @Test
    void chooseCard() {
        player.getResources().put(Resource.WOOD, 2);
        player.getResources().put(Resource.ORE, 2);
        player.getResources().put(Resource.PAPYRUS, 1);
        assertTrue(player.strategy.chooseCard(player, CardColor.PURPLE));
        assertEquals(c1, player.getChosenCard());
        assertTrue(player.strategy.chooseCard(player, CardColor.BLUE));
        assertEquals(c2, player.getChosenCard());
        assertTrue(player.strategy.chooseCard(player, CardColor.PURPLE));
        //TODO correct bug on chooseCard(Effect e)
        //assertTrue(player.chooseCard(new ScienceChoiceEffect()));
    }
     */

}
