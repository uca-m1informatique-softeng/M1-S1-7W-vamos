package Effects;

import Card.Card;
import Card.Resource ;
import Player.*;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;


class TradeResourceEffectTest {

    @Test
    void tradeResourceEffect() {

        try {
            Card card = new Card("marketplace", 6);
            TradeResourceEffect effect = (TradeResourceEffect) card.getEffect();
            ArrayList<Resource> resList = new ArrayList<>();
            resList.add(Resource.GLASS);
            resList.add(Resource.LOOM);
            resList.add(Resource.PAPYRUS);

            assertTrue(effect.isPrevPlayerAllowed());
            assertTrue(effect.isNextPlayerAllowed());
            assertEquals(resList, effect.resourcesModified);

        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }

    }

    @Test
    void buyCheaperResource() {
        Player player = new Player("George");
        Player neighbor = new Player("Bob");
        player.setPrevNeighbor(new Player("BOBZZ"));
        player.setNextNeighbor(neighbor);
        player.setCoins(2);
        neighbor.getResources().put(Resource.CLAY, 2);
        try {
            player.getBuiltCards().add(new Card("eastTradingPost", 6));
            player.buyResource(Resource.CLAY, 1, player.getNextNeighbor());
            assertEquals(player.getBoughtResources().get(Resource.CLAY), 1);
            assertEquals(player.getCoins(), 1);
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void wonderTradeResourceEffect() {
        ArrayList<Resource> resList = new ArrayList<>();
        resList.add(Resource.CLAY);
        resList.add(Resource.STONE);
        resList.add(Resource.WOOD);
        resList.add(Resource.ORE);
        TradeResourceEffect effect =new TradeResourceEffect(true, true, resList);
        assertTrue(effect.isPrevPlayerAllowed());
        assertTrue(effect.isNextPlayerAllowed());
        assertEquals(resList, effect.resourcesModified);

    }
}