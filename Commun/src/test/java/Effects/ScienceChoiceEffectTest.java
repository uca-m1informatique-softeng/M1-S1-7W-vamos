package Effects;

import Card.CardPoints;
import Player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ScienceChoiceEffectTest {

    Player player;
    ScienceChoiceEffect sc;

    @BeforeEach
    void setUp() {
        player = new Player("Test_player");
        sc = new ScienceChoiceEffect();
    }

    @Test
    void applyEffect() {
        sc.applyEffect(player, null, null, null);
        assertEquals(1, player.getPoints().get(CardPoints.SCIENCE_COMPASS));
        sc.applyEffect(player, null, null, null);
        sc.applyEffect(player, null, null, null);
        assertEquals(3, player.getPoints().get(CardPoints.SCIENCE_COMPASS));

        player.getPoints().put(CardPoints.SCIENCE_COMPASS, player.getPoints().get(CardPoints.SCIENCE_COMPASS) - 2);
        player.getPoints().put(CardPoints.SCIENCE_WHEEL, player.getPoints().get(CardPoints.SCIENCE_WHEEL) + 1);
        sc.applyEffect(player, null, null, null);
        assertEquals(1, player.getPoints().get(CardPoints.SCIENCE_TABLET));

        int best_score = player.getSciencePoint();
        player.getPoints().put(CardPoints.SCIENCE_TABLET, player.getPoints().get(CardPoints.SCIENCE_TABLET) - 1);
        player.getPoints().put(CardPoints.SCIENCE_COMPASS, player.getPoints().get(CardPoints.SCIENCE_COMPASS) +1);
        int score2 = player.getSciencePoint();
        player.getPoints().put(CardPoints.SCIENCE_COMPASS, player.getPoints().get(CardPoints.SCIENCE_COMPASS) -1);
        player.getPoints().put(CardPoints.SCIENCE_WHEEL, player.getPoints().get(CardPoints.SCIENCE_WHEEL) + 1);
        int score3 = player.getSciencePoint();
        assertTrue(best_score >= score2 && best_score >= score3);
    }

    @Test
    void applyCumulativeEffect() {
        //Test for choose n*n choice
        sc.applyCumulativeEffect(player);
        assertEquals(4, player.getSciencePoint());
        sc.applyCumulativeEffect(player);
        assertEquals(16, player.getSciencePoint());
        sc.applyCumulativeEffect(player);
        assertEquals(36, player.getSciencePoint());

        //Test for choose point win with set of 3 elements
        //Symbol wheel will be choose
        player.getPoints().put(CardPoints.SCIENCE_COMPASS, 2);
        player.getPoints().put(CardPoints.SCIENCE_TABLET, 2);
        player.getPoints().put(CardPoints.SCIENCE_WHEEL, 0);
        sc.applyCumulativeEffect(player);
        assertEquals(26, player.getSciencePoint());
        assertEquals(2, player.getPoints().get(CardPoints.SCIENCE_WHEEL));

        //Symbol tablet will be choose
        player.getPoints().put(CardPoints.SCIENCE_COMPASS, 2);
        player.getPoints().put(CardPoints.SCIENCE_TABLET, 0);
        player.getPoints().put(CardPoints.SCIENCE_WHEEL, 2);
        sc.applyCumulativeEffect(player);
        assertEquals(26, player.getSciencePoint());
        assertEquals(2, player.getPoints().get(CardPoints.SCIENCE_TABLET));

        //Symbol compass will be choose
        player.getPoints().put(CardPoints.SCIENCE_COMPASS, 0);
        player.getPoints().put(CardPoints.SCIENCE_WHEEL, 2);
        player.getPoints().put(CardPoints.SCIENCE_TABLET, 2);
        sc.applyCumulativeEffect(player);
        assertEquals(26, player.getSciencePoint());
        assertEquals(2, player.getPoints().get(CardPoints.SCIENCE_COMPASS));
    }
}