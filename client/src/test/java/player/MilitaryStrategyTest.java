package player;

import card.*;
import wonder.Wonder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;
import java.util.ArrayList;

class MilitaryStrategyTest {

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
            player.hand.add(new Card("stockade", 6)); //military might
            player.hand.add(new Card("barracks", 6)); //military might
            player.hand.add(new Card("guardtower", 6)); //military might
            player.hand.add(new Card("orevein", 6));
            player.hand.add(new Card("pawnshop", 6)); //no military might
            player.wonder = new Wonder("rhodosA");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void chooseAction1() {
        player.getResources().put(Resource.ORE, 1);
        player.getResources().put(Resource.CLAY, 1);
        player.getResources().put(Resource.WOOD, 1);
        Action action = strategy.chooseAction(player);
        //in chooseAction in MilitaryStrategy last card checked is guardtower so mostMilitary=guardtower
        assertEquals(action.getCard().getName(), new Card("guardtower", 6).getName());
        assertEquals(Action.BUILD, action.getAction());
    }

    @Test
    void chooseAction2() {
        Action action = strategy.chooseAction(player);
        assertEquals(action.getCard().getName(), new Card("orevein", 6).getName());
        assertEquals(Action.BUILD, action.getAction());
    }

}