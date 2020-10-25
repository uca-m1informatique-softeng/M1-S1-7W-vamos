package wonder;

import card.*;
import effects.FreeCardPerAgeEffect;
import effects.ResourceChoiceEffect;
import org.junit.Ignore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.EnumMap;

import static org.junit.jupiter.api.Assertions.*;
class WonderTest {

    Wonder wonder;

    @BeforeEach
    void setUp() throws IOException {
        wonder = new Wonder("babylonA");
    }

    @Test
    void testGetName() {
        String str = "babylonA";
        assertEquals(str,wonder.getName());
    }

    @Test
    void getState() {
        int state =0;
        assertEquals(state,wonder.getState());
    }

    @Test
    void getMaxstate() {
        int state = 3;
        assertEquals(state,wonder.getMaxstate());
    }

    @Test
    void getCurrentRewardsFromUpgradeTest() {
        //setup wonder = babyloneA
        EnumMap<CardPoints,Integer> oracle = new EnumMap<CardPoints, Integer>(CardPoints.class);
        oracle.put(CardPoints.VICTORY, 3);
        assertEquals(oracle, this.wonder.getCurrentRewardsFromUpgrade()); //index/state  = 0, cost stage 1
        assertEquals(oracle, this.wonder.getProp().get(this.wonder.getState()).y); //index/state  = 0, cost stage 1

        wonder.setState(wonder.getState() + 1); // stage one is build
        oracle = new EnumMap<CardPoints, Integer>(CardPoints.class);
        //reward is an effect, so no cardpoints
        assertEquals(oracle, this.wonder.getCurrentRewardsFromUpgrade()); // index/state = 1, cost stage 2

        wonder.setState(wonder.getState() + 1); // 2 stage build
        oracle = new EnumMap<CardPoints, Integer>(CardPoints.class);
        oracle.put(CardPoints.VICTORY, 7);
        assertEquals(oracle, this.wonder.getCurrentRewardsFromUpgrade()); // index/state = 2, cost stage 3
        wonder.setState(wonder.getState() + 1); // Build the last stage

        //return an outIndexError when we call this method
    }

    @Test
    void isWonderFinished() {
        assertFalse(wonder.isWonderFinished()); //No stage built

        wonder.setState(wonder.getState() + 1); //Build stage One
        assertFalse(wonder.isWonderFinished()); // 2 build more can be build

        wonder.setState(wonder.getState() + 1); // 2 stage build
        assertFalse(wonder.isWonderFinished()); // One more for finish the wonder

        wonder.setState(wonder.getState() + 1); // Build the last stage
        assertTrue(wonder.isWonderFinished()); // Finish

    }

    @Test
    void canUpgrade() {
        assertFalse(wonder.canUpgrade(new EnumMap<Resource, Integer>(Resource.class)));
    }

    @Test
    void parseTest() throws IOException {
        Wonder w = new Wonder("alexandriaA");
        Wonder w2 = new Wonder("alexandriaB");
        Wonder w3 = new Wonder("babylonA");
        Wonder w4 = new Wonder("babylonB");
        Wonder w5 = new Wonder("ephesosA");
        Wonder w6 = new Wonder("ephesosB");
        Wonder w7 = new Wonder("gizahA");
        Wonder w8 = new Wonder("gizahB");
        Wonder w9 = new Wonder("halikarnassosA");
        Wonder w10 = new Wonder("halikarnassosB");
        Wonder w11 = new Wonder("olympiaA");
        Wonder w12 = new Wonder("olympiaB");
        Wonder w13 = new Wonder("rhodosA");
        Wonder w14 = new Wonder("rhodosB");

        //Display the good effect.
        assertTrue(w.getEffects().get(2) instanceof ResourceChoiceEffect);
        assertTrue(w11.getEffects().get(2) instanceof FreeCardPerAgeEffect);

    }

    @Test
    void getCurrentUpgradeCost() {
        //setup wonder = babyloneA
        EnumMap<Resource,Integer> oracle = new EnumMap<Resource, Integer>(Resource.class);
        oracle.put(Resource.CLAY, 2);
        assertEquals(oracle, this.wonder.getCurrentUpgradeCost()); //index/state  = 0, cost stage 1

        wonder.setState(wonder.getState() + 1); // stage one is build
        oracle = new EnumMap<Resource, Integer>(Resource.class);
        oracle.put(Resource.WOOD, 3);
        assertEquals(oracle, this.wonder.getCurrentUpgradeCost()); // index/state = 1, cost stage 2

        wonder.setState(wonder.getState() + 1); // 2 stage build
        oracle = new EnumMap<Resource, Integer>(Resource.class);
        oracle.put(Resource.CLAY, 4);
        assertEquals(oracle, this.wonder.getCurrentUpgradeCost()); // index/state = 2, cost stage 3
        wonder.setState(wonder.getState() + 1); // Build the last stage

        //return an outIndexError when we call this method
    }

    @Test
    void wonderGiveCoin() throws IOException {
        Wonder w = new Wonder("rhodosB");
        EnumMap<CardPoints,Integer> oracle = new EnumMap<CardPoints, Integer>(CardPoints.class);
        oracle.put(CardPoints.MILITARY, 1);
        oracle.put(CardPoints.VICTORY, 3);
        oracle.put(CardPoints.RELAY_COIN, 3);
        assertEquals(oracle, w.getCurrentRewardsFromUpgrade());
    }
}
