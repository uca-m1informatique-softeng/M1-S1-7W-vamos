package Card;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;


class EffectTest {

    Card card;

    @BeforeEach
    void setUp() throws IOException {
        card = new Card("academy", 3);
    }

    @Test
    void getName() {
        String oracle = "academy";
        assertEquals(oracle, card.getName());
    }

}