package Card;

import Effects.TradeResourceEffect;
import Player.DumbPlayer;
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
        DumbPlayer player = new DumbPlayer("George");
        DumbPlayer neighbor = new DumbPlayer("Bob");
        player.setPrevNeighbor(new DumbPlayer("BOBZZ"));
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

}