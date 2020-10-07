package Player;

import Card.*;
import Wonder.Wonder;
import org.junit.Ignore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;
import java.util.ArrayList;

public class MilitaryStrategyTest {

    private Player player;
    private Player prevNeighbor;
    private Player nextNeighbor;
    private Strategy strategy;

    @BeforeEach
    void setup() {
        player = new Player("Billy Joe War");
        prevNeighbor = new Player("Mâle Beta 1");
        nextNeighbor = new Player("Mâle Beta 2");
        strategy = new MilitaryStrategy();
        player.strategy = strategy;
        player.setPrevNeighbor(prevNeighbor);
        player.setNextNeighbor(nextNeighbor);
        player.hand = new ArrayList<>();
        try {
            player.hand.add(new Card("stockade", 6));
            player.hand.add(new Card("barracks", 6));
            player.hand.add(new Card("guardtower", 6));
            player.hand.add(new Card("orevein", 6));
            player.hand.add(new Card("pawnshop", 6));
            player.wonder = new Wonder("alexandriaA");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//TODO
    @Ignore
    public void chooseAction1() {
        player.getResources().put(Resource.ORE, 1);
        Action action = strategy.chooseAction(player);
        try {
            assertEquals(action.getCard().getName(), new Card("barracks", 6).getName());
            assertEquals(action.getAction(), Action.BUILD);
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    public void chooseAction2() {
        Action action = strategy.chooseAction(player);
        try {
            assertEquals(action.getCard().getName(), new Card("orevein", 6).getName());
            assertEquals(action.getAction(), Action.BUILD);
        } catch (IOException e) {
            fail();
        }
    }

}