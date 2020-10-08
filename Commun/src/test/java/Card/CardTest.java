package Card;

import Card.*;
import Effects.ScienceChoiceEffect;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.EnumMap;

import static org.junit.jupiter.api.Assertions.*;

class CardTest {

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

    @Test
    void getColor() {
        CardColor oracle = CardColor.GREEN;
        assertEquals(oracle, card.getColor());
    }

    @Test
    void getAge() {
        int oracle = 3;
        assertEquals(oracle, card.getAge());
    }

    @Test
    void getPlayers() {
        int oracle = 3;
        assertEquals(oracle, card.getPlayers());
    }

    @Test
    void getCardPoints() {
        EnumMap<CardPoints, Integer> oracle;
        oracle = new EnumMap<>(CardPoints.class);
        oracle.put(CardPoints.VICTORY, 0);
        oracle.put(CardPoints.MILITARY, 0);
        oracle.put(CardPoints.SCIENCE_WHEEL, 0);
        oracle.put(CardPoints.SCIENCE_TABLET, 0);
        oracle.put(CardPoints.SCIENCE_COMPASS, 1);
        assertEquals(oracle, card.getCardPoints());

    }

    @Test
    void getResource() {
        EnumMap<Resource, Integer> oracle;
        oracle = new EnumMap<>(Resource.class);
        oracle.put(Resource.WOOD, 0);
        oracle.put(Resource.STONE, 0);
        oracle.put(Resource.ORE, 0);
        oracle.put(Resource.CLAY, 0);
        oracle.put(Resource.GLASS, 0);
        oracle.put(Resource.LOOM, 0);
        oracle.put(Resource.PAPYRUS, 0);
        oracle.put(Resource.COIN, 0);
        assertEquals(oracle, card.getResource());

    }

    @Test
    void getCost() {
        EnumMap<Resource, Integer> oracle;
        oracle = new EnumMap<>(Resource.class);
        oracle.put(Resource.WOOD, 0);
        oracle.put(Resource.STONE, 3);
        oracle.put(Resource.ORE, 0);
        oracle.put(Resource.CLAY, 0);
        oracle.put(Resource.GLASS, 1);
        oracle.put(Resource.LOOM, 0);
        oracle.put(Resource.PAPYRUS, 0);
        oracle.put(Resource.COIN, 0);
        assertEquals(oracle, card.getCost());
    }

    @Test
    void isFree() {
        assertEquals(false, card.isFree());
    }

    @Test
    void parseTest() throws IOException {
        Card c = new Card("scientistsguild", 3);
        assertTrue(c.getEffect() instanceof ScienceChoiceEffect);

    }
}