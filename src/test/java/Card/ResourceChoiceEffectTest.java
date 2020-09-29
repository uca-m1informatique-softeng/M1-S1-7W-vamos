package Card;

import org.junit.jupiter.api.Test;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;


class ResourceChoiceEffectTest {

    @Test
    void resourceChoiceEffect1() {
        Card card;

        try {
            card = new Card("treefarm", 6);
            assertTrue(((ResourceChoiceEffect) (card.getEffect())).getRes().get(0).equals(Resource.WOOD) && ((ResourceChoiceEffect) (card.getEffect())).getRes().get(1).equals(Resource.CLAY));
        } catch (IOException e) {
            e.printStackTrace();
            assertTrue(false);
        }


    }

    @Test
    void resourceChoiceEffect2() {
        Card card;

        try {
            card = new Card("mine", 6);
            assertTrue(((ResourceChoiceEffect) (card.getEffect())).getRes().get(0).equals(Resource.ORE) && ((ResourceChoiceEffect) (card.getEffect())).getRes().get(1).equals(Resource.STONE));
        } catch (IOException e) {
            e.printStackTrace();
            assertTrue(false);
        }


    }

}