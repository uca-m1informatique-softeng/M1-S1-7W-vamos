package Core;
import Card.*;
import Effects.ResourceChoiceEffect;
import Effects.ScienceChoiceEffect;
import Wonder.Wonder;
import org.junit.Ignore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.EnumMap;

import static org.junit.jupiter.api.Assertions.*;
public class WonderTest {

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

   //TODO
    @Ignore
    void getCurrentRewardsFromUpgrade() {
        EnumMap<CardPoints,Integer> test = new EnumMap<CardPoints, Integer>(CardPoints.class);
        test.put(CardPoints.VICTORY,5);
        assertEquals(test,wonder.getCurrentRewardsFromUpgrade());
    }

    @Test
    void isWonderFinished() {
        assertFalse(wonder.isWonderFinished());
    }

    @Test
    void canUpgrade() {
        assertFalse(wonder.canUpgrade(new EnumMap<Resource, Integer>(Resource.class)));
    }

    @Test
    void parseTest() throws IOException {
        Wonder w = new Wonder("alexandriaA");
        Wonder w2 = new Wonder("alexandriaB");
        Wonder w3 = new Wonder("babylonB");
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

        System.out.println(w2);
        //assertTrue(w.getEffects().get(2) instanceof ResourceChoiceEffect);

    }
}
