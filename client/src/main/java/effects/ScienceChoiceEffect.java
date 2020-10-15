package effects;

import card.Card;
import card.CardColor;
import card.CardPoints;
import card.Resource;
import player.Player;
import java.util.ArrayList;
import java.util.EnumMap;

public class ScienceChoiceEffect extends Effect {

    private EnumMap<CardPoints, Integer> science_points;
    private ArrayList<CardPoints> iterScience = new ArrayList<CardPoints>();

    /**
     * Add the best symbol who will give the more of point to the player.
     * Warning this method do side effect on the EnumMap player.points.
     * @param player
     */
    public void applyEffect(Player player, CardColor color, Integer age, EnumMap<Resource, Integer> cost, ArrayList<Card> discardCards) {
        CardPoints best = CardPoints.SCIENCE_COMPASS;
        science_points = player.getPoints(); //side effect on the attribute player's

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

    public void applyCumulativeEffect(Player player){
        iterScience.add(CardPoints.SCIENCE_COMPASS);
        iterScience.add(CardPoints.SCIENCE_TABLET);
        iterScience.add(CardPoints.SCIENCE_WHEEL);
        science_points = player.getPoints(); //side effect on the attribute player's
        int science_score = player.getSciencePoint();
        int compass = 0;
        int tablet = 0;
        int wheel = 0;
        int best_compass = 0;
        int best_tablet = 0;
        int best_wheel = 0;
        for(CardPoints symbole : iterScience){
            if(symbole == CardPoints.SCIENCE_COMPASS) {compass++;}
            else if (symbole == CardPoints.SCIENCE_TABLET) {tablet++;}
            else {wheel++;}
            for(CardPoints symbole2 : iterScience) {
                if (symbole2 == CardPoints.SCIENCE_COMPASS) { compass++; }
                else if (symbole2 == CardPoints.SCIENCE_TABLET) { tablet++; }
                else { wheel++; }
                science_points.put(CardPoints.SCIENCE_COMPASS, science_points.get(CardPoints.SCIENCE_COMPASS) + compass);
                science_points.put(CardPoints.SCIENCE_TABLET, science_points.get(CardPoints.SCIENCE_TABLET) + tablet);
                science_points.put(CardPoints.SCIENCE_WHEEL, science_points.get(CardPoints.SCIENCE_WHEEL) + wheel);

                if(science_score < player.getSciencePoint()){
                    science_score = player.getSciencePoint();
                    best_compass = compass;
                    best_tablet = tablet;
                    best_wheel = wheel;

                }
                science_points.put(CardPoints.SCIENCE_COMPASS, science_points.get(CardPoints.SCIENCE_COMPASS) - compass);
                science_points.put(CardPoints.SCIENCE_TABLET, science_points.get(CardPoints.SCIENCE_TABLET) - tablet);
                science_points.put(CardPoints.SCIENCE_WHEEL, science_points.get(CardPoints.SCIENCE_WHEEL) - wheel);
                if (symbole2 == CardPoints.SCIENCE_COMPASS) { compass--; }
                else if (symbole2 == CardPoints.SCIENCE_TABLET) { tablet--; }
                else { wheel--; }

            }
            if (symbole == CardPoints.SCIENCE_COMPASS) { compass--; }
            else if (symbole == CardPoints.SCIENCE_TABLET) { tablet--; }
            else { wheel--; }
        }
        player.getPoints().put(CardPoints.SCIENCE_COMPASS, science_points.get(CardPoints.SCIENCE_COMPASS) + best_compass);
        player.getPoints().put(CardPoints.SCIENCE_TABLET, science_points.get(CardPoints.SCIENCE_TABLET) + best_tablet);
        player.getPoints().put(CardPoints.SCIENCE_WHEEL, science_points.get(CardPoints.SCIENCE_WHEEL) + best_wheel);
    }
}
