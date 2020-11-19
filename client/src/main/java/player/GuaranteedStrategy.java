package player;

import card.Card;
import card.CardColor;
import card.CardPoints;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.EnumMap;

/**
 * This AI can manage the order of color priority.
 */
public class GuaranteedStrategy extends Strategy{

    private ArrayList<ArrayList<CardColor>> priorityColor = new ArrayList();
    //age/player/hand is defined in chooseAction(), this may cause bug if is not call.
    private int age; //Redefine at each call of chooseAction
    private Player player;
    private ArrayList<Card> hand;
    private SecureRandom rand;

    public GuaranteedStrategy(Player player) {
        //Fix color priority.
        ArrayList<CardColor> age1 = quickList(CardColor.GREEN, CardColor.GREY);
        ArrayList<CardColor> age2 = quickList(CardColor.GREEN, CardColor.BROWN, CardColor.GREY);
        ArrayList<CardColor> age3 = quickList(CardColor.GREEN, CardColor.RED);

        //Add Color
        this.priorityColor.add(age1);
        this.priorityColor.add(age2);
        this.priorityColor.add(age3);

        this.player = player;
        this.hand = this.player.getHand();
        this.age = this.hand.get(0).getAge();
        this.rand = new SecureRandom();
    }

    @Override
    public Action chooseAction(Player player) {
        ArrayList<Integer>[] colorIndexSet = cardGroupedByPriorityColor();
        int indexChosen = 0;
        for (ArrayList<Integer> Lindex : colorIndexSet) {
            if (!Lindex.isEmpty()) {
                indexChosen = Lindex.get(0);
                break;
            }
        }
        return new Action(this.hand.get(indexChosen), Action.BUILD);
        }

    /**
     * Can rapidly set a list in order we enter the elements.
     * @param colors is type CardColor
     * @return list with the CardColor put on parameters
     */
    protected ArrayList<CardColor> quickList(CardColor ... colors){
        ArrayList<CardColor> res = new ArrayList<>();
        for (CardColor elt : colors) { res.add(elt); }
        return res;
    }

    /**
     * Initialize the array and this elements like list of integer.
     * @param size correpond to the number of priority
     * @return the initialize empty array.
     */
    protected ArrayList<Integer>[] initArray(int size){
        ArrayList<Integer>[] res =  new ArrayList[size];
        for(int k = 0; k < size; k++) { res[k] = new ArrayList<Integer>(); }
        return res;
    }

    /**
     * Browse the list only once for create un array. The array represent
     * the new set of elements we will choose on it.
     * Ex : GREEN -> GREY <==> array[0] -> array[1]
     * <==> {1, 2} -> {3} So there are a green card in position 1, 2 in hand
     * and grey card in position 3.
     * @return a table containing a list of the positions in the player's hand.
     * Each list design the position of one color.
     */
    protected ArrayList<Integer>[] cardGroupedByPriorityColor(){
        ArrayList<Integer>[] indexL = initArray(priorityColor.get(age-1).size());
        Card card;
        for(int i = 0; i < hand.size(); i++) {
            card = hand.get(i);
            for (int j = 0; j < priorityColor.get(age-1).size(); j++) {
                CardColor color = priorityColor.get(age-1).get(j);
                if(color.equals(card.getColor())){
                    if(!player.alreadyBuilt(card) && player.isBuildable(card)){
                        indexL[j].add(i);
                    }
                }
            }
        }
        return indexL;
    }

    /**
     * Checks what Science Point the player has the most of, and returns it in order to maximize its
     * score.
     * @return the Science Point the player should prioritize
     */
    private CardPoints getSciencePriority() {
        int wheelPoints = this.player.getPoints().get(CardPoints.SCIENCE_WHEEL);
        int compassPoints = this.player.getPoints().get(CardPoints.SCIENCE_COMPASS);
        int tabletPoints = this.player.getPoints().get(CardPoints.SCIENCE_TABLET);

        if (wheelPoints >= compassPoints && wheelPoints >= tabletPoints) {
            return CardPoints.SCIENCE_WHEEL;
        } else if (compassPoints >= wheelPoints && compassPoints >= tabletPoints) {
            return CardPoints.SCIENCE_COMPASS;
        } else if (tabletPoints >= wheelPoints && tabletPoints >= compassPoints) {
            return CardPoints.SCIENCE_TABLET;
        } else {
            switch (this.rand.nextInt(3)) {
                case 0 :
                    return CardPoints.SCIENCE_WHEEL;
                case 1 :
                    return CardPoints.SCIENCE_COMPASS;
                default :
                    return CardPoints.SCIENCE_TABLET;
            }
        }
    }
}
