package player;

import card.Card;
import card.CardColor;
import card.CardPoints;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.NoSuchElementException;

/**
 * This AI can manage the order of color priority.
 */
public class GuaranteedStrategy extends Strategy{

    private ArrayList<ArrayList<CardColor>> priorityColor = new ArrayList();
    //age/player/hand is defined in chooseAction(), this may cause bug if is not call.
    private Player player;

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
        this.rand = new SecureRandom();
    }

    @Override
    public Action chooseAction(Player player) {
        //If the player has the marketPlace card in his hand he builds it (marketPlace is a free Card)
        if (this.marketPlaceInHand()){
            int marketPlaceIndex = this.marketPlaceIndex();
            return new Action(this.player.getHand().get(marketPlaceIndex) , Action.BUILD);
        }

        ArrayList<CardColor> currentColorPriority = this.priorityColor.get(this.player.getHand().get(0).getAge());

        for (CardColor color : currentColorPriority) {
            switch (color) {
                case GREEN:
                    Card bestScienceCard = this.getBestScienceCard();
                    if (bestScienceCard != null) return new Action(bestScienceCard, Action.BUILD);
                    break;
                case GREY:
                    // TODO
                    break;
                case BROWN:
                    // TODO
                    break;
                case RED:
                    // TODO
                    break;
                default :
                    break;
            }
        }

        return new Action(this.player.getHand().get(0), Action.DUMP);
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
        int age = this.player.getHand().get(0).getAge();

        ArrayList<Integer>[] indexL = initArray(priorityColor.get(age-1).size());
        Card card;
        for(int i = 0; i < this.player.getHand().size(); i++) {
            card = this.player.getHand().get(i);
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
    public CardPoints getSciencePriority() {
        int wheelPoints = this.player.getPoints().get(CardPoints.SCIENCE_WHEEL);
        int compassPoints = this.player.getPoints().get(CardPoints.SCIENCE_COMPASS);
        int tabletPoints = this.player.getPoints().get(CardPoints.SCIENCE_TABLET);

        ArrayList<Integer> sciencePoints = new ArrayList<>();
        sciencePoints.add(wheelPoints);
        sciencePoints.add(compassPoints);
        sciencePoints.add(tabletPoints);

        switch (sciencePoints.indexOf(Collections.max(sciencePoints))) {
            case 0 :
                return CardPoints.SCIENCE_WHEEL;
            case 1 :
                return CardPoints.SCIENCE_COMPASS;
            default :
                return CardPoints.SCIENCE_TABLET;
        }

    }

    /**
     * Checks the player's hand for the best green card to choose, depending on the current player's
     * science points, resources, and the tradable resources.
     * If several cards match the description, a random card is chosen.
     * @return The best science card to build.
     */
    public Card getBestScienceCard() {
        CardPoints bestSciencePoint = this.getSciencePriority();
        ArrayList<Card> bestCandidates = new ArrayList<>();

        for (Card card : this.player.getHand()) {
            if (card.getCardPoints().get(bestSciencePoint) > 0 &&
                this.player.isBuildable(card)) {
                bestCandidates.add(card);
            }
        }

        if (bestCandidates.size() == 0) {
            return null;
        } else {
            return bestCandidates.get(this.rand.nextInt(bestCandidates.size()));
        }
    }

    /**
     * Checks if the marketPlace card is in the player's hand
     * @return true if the player has the marketPlace card , false otherwise
     */
    public boolean marketPlaceInHand(){
        for (int i = 0; i < this.player.getHand().size(); i++) {
            if (this.player.getHand().get(i).getName().equals("marketplace")){
                return true;
            }
        }
        return false;
    }

    /**
     * If the player has the marketPlace card , this method returns it's index in the players hand
     * @return the index of the marketPlace card in the player's hand .If the player don't have the marketPlace card in his hand it returns -1
     */
    public int marketPlaceIndex(){
        if(this.marketPlaceInHand()){
            for (int i = 0; i < this.player.getHand().size(); i++) {
                if (this.player.getHand().get(i).getName().equals("marketplace")){
                    return i ;
                }
            }
        }
        return -1;
    }
}
