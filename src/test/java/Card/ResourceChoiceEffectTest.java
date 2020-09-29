package Card;

import org.junit.jupiter.api.Test;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;


class ResourceChoiceEffectTest {

    @Test
    void BrownEffect() {
        Card card;

        try {
            card = new Card("treefarm", 6);
            assertTrue(((ResourceChoiceEffect) (card.getEffect())).getRes().get(0).equals(Resource.WOOD) && ((ResourceChoiceEffect) (card.getEffect())).getRes().get(1).equals(Resource.CLAY));
        } catch (IOException e) {
            e.printStackTrace();
            assertTrue(false);
        }


    }

}