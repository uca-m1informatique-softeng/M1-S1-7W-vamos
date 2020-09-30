package Card;

import org.junit.jupiter.api.Test;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;


class TradeResourceEffectTest {

    @Test
    void resourceChoiceEffect1() {

        try {
            Card card = new Card("treefarm", 6);
            assertEquals(((ResourceChoiceEffect) (card.getEffect())).getRes().get(0), Resource.WOOD);
            assertEquals(((ResourceChoiceEffect) (card.getEffect())).getRes().get(1), Resource.CLAY);
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }


    }


}