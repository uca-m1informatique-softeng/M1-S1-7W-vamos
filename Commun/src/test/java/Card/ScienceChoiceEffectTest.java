package Card;

import Player.DumbPlayer;
import Player.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ScienceChoiceEffectTest {

    @Test
    void applyEffect() {
        Player player = new DumbPlayer("Test_player");
        ScienceChoiceEffect sc = new ScienceChoiceEffect();
        sc.applyEffect(player);
        assertEquals(1, player.getPoints().get(CardPoints.SCIENCE_COMPASS));
        sc.applyEffect(player);
        sc.applyEffect(player);
        assertEquals(3, player.getPoints().get(CardPoints.SCIENCE_COMPASS));

        player.getPoints().put(CardPoints.SCIENCE_COMPASS, player.getPoints().get(CardPoints.SCIENCE_COMPASS) - 2);
        player.getPoints().put(CardPoints.SCIENCE_WHEEL, player.getPoints().get(CardPoints.SCIENCE_WHEEL) + 1);
        sc.applyEffect(player);
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
}