package Player;

import Card.Card;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import Card.Resource;
import java.io.IOException;
import java.util.ArrayList;

public class MilitaryPlayerTest {

    private static MilitaryPlayer player;
    private static DumbPlayer nextNeighbor;
    private static DumbPlayer prevNeighbor;

    @BeforeEach
    void setup() {
        player = new MilitaryPlayer("Boris le Guerrier de la Mort");
        prevNeighbor = new DumbPlayer("Mâle Beta 1");
        nextNeighbor = new DumbPlayer("Mâle Beta 2");
        player.setPrevNeighbor(prevNeighbor);
        player.setNextNeighbor(nextNeighbor);
        player.hand = new ArrayList<>();
        try {
            player.hand.add(new Card("stockade", 6));
            player.hand.add(new Card("barracks", 6));
            player.hand.add(new Card("guardtower", 6));
            player.hand.add(new Card("orevein", 6));
            player.hand.add(new Card("pawnshop", 6));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void chooseCard1() {
        player.getResources().put(Resource.ORE, 1);
        player.chooseCard();
        try {
            assertEquals(player.chosenCard.getName(), new Card("barracks", 6).getName());
            assertTrue(player.buildChosenCard);
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    public void chooseCard2() {
        player.chooseCard();
        try {
            assertEquals(player.chosenCard.getName(), new Card("orevein", 6).getName());
            assertTrue(player.buildChosenCard);
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    public void chooseCard3() {
        try {
            player.getHand().remove(new Card("orevein", 6));
        } catch (IOException e) {
            fail();
            e.printStackTrace();
        }

        player.chooseCard();
        try {
            assertEquals(player.chosenCard.getName(), new Card("pawnshop", 6).getName());
            assertFalse(player.buildChosenCard);
        } catch (IOException e) {
            fail();
        }
    }

}
