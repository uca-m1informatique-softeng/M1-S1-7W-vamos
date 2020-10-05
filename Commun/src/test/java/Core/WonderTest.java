package Core;
import Card.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.EnumMap;

import static org.junit.jupiter.api.Assertions.*;
public class WonderTest {

    Wonder wonder;

    @BeforeEach
    void setUp() throws IOException {
        wonder = new Wonder("babylon");
    }



    @Test
    void testGetName() {
        String str = "babylon";
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
}
