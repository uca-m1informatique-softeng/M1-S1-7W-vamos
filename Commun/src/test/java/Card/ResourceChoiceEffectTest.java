package Card;

import Effects.ResourceChoiceEffect;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.EnumMap;

import static org.junit.jupiter.api.Assertions.*;


class ResourceChoiceEffectTest {

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

    @Test
    void resourceChoiceEffect2() {

        try {
            Card card = new Card("mine", 6);
            assertEquals(((ResourceChoiceEffect) (card.getEffect())).getRes().get(0), Resource.ORE);
            assertEquals(((ResourceChoiceEffect) (card.getEffect())).getRes().get(1), Resource.STONE);
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }

    }

    @Test
    void applyEffect() {

        try {
            Card card1 = new Card("mine", 6);
            Card card2 = new Card("statue", 6);
            EnumMap<Resource, Integer> oracle = new EnumMap<>(Resource.class);

            oracle.put(Resource.WOOD, 1);
            oracle.put(Resource.STONE, 0);
            oracle.put(Resource.ORE, 1);
            oracle.put(Resource.CLAY, 0);
            oracle.put(Resource.GLASS, 0);
            oracle.put(Resource.LOOM, 0);
            oracle.put(Resource.PAPYRUS, 0);
            oracle.put(Resource.COIN, 0);

            EnumMap<Resource, Integer> costAfterEffect = card2.getCost();
            ((ResourceChoiceEffect) (card1.getEffect())).applyEffect(costAfterEffect);

            assertEquals(costAfterEffect, oracle);
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }


    }

}