package effects;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EffectTest {

    @Test
    void testEquals() {
        CopyOneGuildEffect effect = new CopyOneGuildEffect();
        PlaySeventhCardEffect effect2 = new PlaySeventhCardEffect();
        assertFalse(effect.equals(effect2));
    }
}