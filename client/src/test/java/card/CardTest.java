package card;

import effects.ScienceChoiceEffect;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumMap;

import static org.junit.jupiter.api.Assertions.*;

class CardTest {

    Card card;
    JSONObject cardCount = new JSONObject() ;

    @BeforeEach
    void setUp() throws IOException {
        card = new Card("academy", 3);

        cardCount.put("brownCards" ,1);
        cardCount.put("greyCards" ,1);
        cardCount.put("yellowCards" ,1);
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
        oracle.put(CardPoints.RELAY_COIN, 0);
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

    @Test
    void countCards(){
        JSONObject cardCount2 = new JSONObject() ;
        cardCount2.put("brownCards" ,2);
        cardCount2.put("greyCards" ,2);
        cardCount2.put("yellowCards" ,2);
        ArrayList<Card> builtCards = new ArrayList<>() ;
        builtCards.add(new Card("timberyard" , 6));
        builtCards.add(new Card("glassworks" , 6));
        builtCards.add(new Card("marketplace" , 6));

        JSONObject cardCountResult = Card.countCards(cardCount , builtCards) ;
        assertEquals(cardCount2.get("brownCards") , cardCountResult.get("brownCards"));
        assertEquals(cardCount2.get("greyCards") , cardCountResult.get("greyCards"));
        assertEquals(cardCount2.get("yellowCards") , cardCountResult.get("yellowCards"));
    }
}