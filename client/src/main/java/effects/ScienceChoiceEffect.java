package effects;

import card.Card;
import card.CardColor;
import card.CardPoints;
import card.Resource;
import player.Player;
import java.util.ArrayList;
import java.util.EnumMap;

public class ScienceChoiceEffect extends Effect {

    private EnumMap<CardPoints, Integer> sciencePoints;
    private ArrayList<CardPoints> iterScience = new ArrayList<CardPoints>();

    /**
     * Add the best symbol who will give the more of point to the player.
     * Warning this method do side effect on the EnumMap player.points.
     * @param player
     */
    public void applyEffect(Player player, CardColor color, Integer age, EnumMap<Resource, Integer> cost, ArrayList<Card> discardCards) {
        CardPoints best = CardPoints.SCIENCE_COMPASS;
        sciencePoints = player.getPoints(); //side effect on the attribute player's

        sciencePoints.put(CardPoints.SCIENCE_COMPASS, sciencePoints.get(CardPoints.SCIENCE_COMPASS) + 1);
        int scienceScore = player.getSciencePoint();
        sciencePoints.put(CardPoints.SCIENCE_COMPASS, sciencePoints.get(CardPoints.SCIENCE_COMPASS) - 1);

        sciencePoints.put(CardPoints.SCIENCE_TABLET, sciencePoints.get(CardPoints.SCIENCE_TABLET) + 1);
        if (scienceScore < player.getSciencePoint()) {
            best = CardPoints.SCIENCE_TABLET;
            scienceScore = player.getSciencePoint();
        }
        sciencePoints.put(CardPoints.SCIENCE_TABLET, sciencePoints.get(CardPoints.SCIENCE_TABLET) - 1);

        sciencePoints.put(CardPoints.SCIENCE_WHEEL, sciencePoints.get(CardPoints.SCIENCE_WHEEL) + 1);
        if (scienceScore < player.getSciencePoint()) {
            best = CardPoints.SCIENCE_WHEEL;
        }
        sciencePoints.put(CardPoints.SCIENCE_WHEEL, sciencePoints.get(CardPoints.SCIENCE_WHEEL) - 1);

        //Add the best symbol for the player
        player.getPoints().put(best, sciencePoints.get(best) + 1);
        //change player.getPoints() to science_points don't remove the side effect(effet de bord).
    }

    public void applyCumulativeEffect(Player player){
        iterScience.add(CardPoints.SCIENCE_COMPASS);
        iterScience.add(CardPoints.SCIENCE_TABLET);
        iterScience.add(CardPoints.SCIENCE_WHEEL);
        sciencePoints = player.getPoints(); //side effect on the attribute player's
        int scienceScore = player.getSciencePoint();
        int compass = 0;
        int tablet = 0;
        int wheel = 0;
        int bestCompass = 0;
        int bestTablet = 0;
        int bestWheel = 0;
        for(CardPoints symbole : iterScience){
            if(symbole == CardPoints.SCIENCE_COMPASS) {compass++;}
            else if (symbole == CardPoints.SCIENCE_TABLET) {tablet++;}
            else {wheel++;}
            for(CardPoints symbole2 : iterScience) {
                if (symbole2 == CardPoints.SCIENCE_COMPASS) { compass++; }
                else if (symbole2 == CardPoints.SCIENCE_TABLET) { tablet++; }
                else { wheel++; }
                sciencePoints.put(CardPoints.SCIENCE_COMPASS, sciencePoints.get(CardPoints.SCIENCE_COMPASS) + compass);
                sciencePoints.put(CardPoints.SCIENCE_TABLET, sciencePoints.get(CardPoints.SCIENCE_TABLET) + tablet);
                sciencePoints.put(CardPoints.SCIENCE_WHEEL, sciencePoints.get(CardPoints.SCIENCE_WHEEL) + wheel);

                if(scienceScore < player.getSciencePoint()){
                    scienceScore = player.getSciencePoint();
                    bestCompass = compass;
                    bestTablet = tablet;
                    bestWheel = wheel;

                }
                sciencePoints.put(CardPoints.SCIENCE_COMPASS, sciencePoints.get(CardPoints.SCIENCE_COMPASS) - compass);
                sciencePoints.put(CardPoints.SCIENCE_TABLET, sciencePoints.get(CardPoints.SCIENCE_TABLET) - tablet);
                sciencePoints.put(CardPoints.SCIENCE_WHEEL, sciencePoints.get(CardPoints.SCIENCE_WHEEL) - wheel);
                if (symbole2 == CardPoints.SCIENCE_COMPASS) { compass--; }
                else if (symbole2 == CardPoints.SCIENCE_TABLET) { tablet--; }
                else { wheel--; }

            }
            if (symbole == CardPoints.SCIENCE_COMPASS) { compass--; }
            else if (symbole == CardPoints.SCIENCE_TABLET) { tablet--; }
            else { wheel--; }
        }
        player.getPoints().put(CardPoints.SCIENCE_COMPASS, sciencePoints.get(CardPoints.SCIENCE_COMPASS) + bestCompass);
        player.getPoints().put(CardPoints.SCIENCE_TABLET, sciencePoints.get(CardPoints.SCIENCE_TABLET) + bestTablet);
        player.getPoints().put(CardPoints.SCIENCE_WHEEL, sciencePoints.get(CardPoints.SCIENCE_WHEEL) + bestWheel);
    }
}
