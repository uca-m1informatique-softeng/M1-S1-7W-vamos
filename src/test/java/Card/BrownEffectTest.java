package Card;

import org.junit.jupiter.api.Test;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;


class BrownEffectTest {

    @Test
    void BrownEffect() {
        Card card;

        try {
            card = new Card("treefarm", 6);
            assertTrue(((BrownEffect) (card.getEffect())).getRes1().equals(Resource.WOOD) && ((BrownEffect) (card.getEffect())).getRes2().equals(Resource.CLAY));
        } catch (IOException e) {
            e.printStackTrace();
            assertTrue(false);
        }


    }

}