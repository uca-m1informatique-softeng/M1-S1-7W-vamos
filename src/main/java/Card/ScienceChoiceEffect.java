package Card;

import Player.Player;

import java.util.EnumMap;

public class ScienceChoiceEffect extends Effect {

    private EnumMap<CardPoints, Integer> science_points;

    /**
     * Add the best symbol who will give the more of point to the player.
     * Warning this method do side effect on the EnumMap player.points.
     * @param player
     */
    public void applyEffect(Player player) {
        CardPoints best = CardPoints.SCIENCE_COMPASS;
        science_points = player.getPoints();

        science_points.put(CardPoints.SCIENCE_COMPASS, science_points.get(CardPoints.SCIENCE_COMPASS) + 1);
        int science_score = player.getSciencePoint();
        science_points.put(CardPoints.SCIENCE_COMPASS, science_points.get(CardPoints.SCIENCE_COMPASS) - 1);

        science_points.put(CardPoints.SCIENCE_TABLET, science_points.get(CardPoints.SCIENCE_TABLET) + 1);
        if (science_score < player.getSciencePoint()) {
            best = CardPoints.SCIENCE_TABLET;
            science_score = player.getSciencePoint();
        }
        science_points.put(CardPoints.SCIENCE_TABLET, science_points.get(CardPoints.SCIENCE_TABLET) - 1);

        science_points.put(CardPoints.SCIENCE_WHEEL, science_points.get(CardPoints.SCIENCE_WHEEL) + 1);
        if (science_score < player.getSciencePoint()) {
            best = CardPoints.SCIENCE_WHEEL;
        }
        science_points.put(CardPoints.SCIENCE_WHEEL, science_points.get(CardPoints.SCIENCE_WHEEL) - 1);

        //Add the best symbol for the player
        player.getPoints().put(best, science_points.get(best) + 1);
        //change player.getPoints() to science_points don't remove the side effect(effet de bord).
    }
}
