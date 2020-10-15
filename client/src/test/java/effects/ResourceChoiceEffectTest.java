package effects;

import card.Card;
import card.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;

import static org.junit.jupiter.api.Assertions.assertEquals;


class ResourceChoiceEffectTest {

    @Test
    void resourceChoiceEffect1() {

        Card card = new Card("treefarm", 6);
        Assertions.assertEquals(((ResourceChoiceEffect) (card.getEffect())).getRes().get(0), Resource.WOOD);
        Assertions.assertEquals(((ResourceChoiceEffect) (card.getEffect())).getRes().get(1), Resource.CLAY);


    }

    @Test
    void resourceChoiceEffect2() {

        Card card = new Card("mine", 6);
        Assertions.assertEquals(((ResourceChoiceEffect) (card.getEffect())).getRes().get(0), Resource.ORE);
        Assertions.assertEquals(((ResourceChoiceEffect) (card.getEffect())).getRes().get(1), Resource.STONE);

    }

    @Test
    void applyEffect() {

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
        card1.getEffect().applyEffect(null, null, null, costAfterEffect);

        assertEquals(costAfterEffect, oracle);

    }

}