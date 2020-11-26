package player;

import card.Card;
import card.CardColor;
import card.CardPoints;

import java.util.*;

/**
 * This AI can manage the order of color priority.
 */
public class GuaranteedStrategy extends Strategy{

    private ArrayList<ArrayList<CardColor>> priorityColor = new ArrayList<>();
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
    }

    @Override
    public Action chooseAction(Player player) {
        //If the player has the marketPlace card in his hand he builds it (marketPlace is a free Card)
        if (this.marketPlaceInHand()){
            int marketPlaceIndex = this.marketPlaceIndex();
            return new Action(this.player.getHand().get(marketPlaceIndex) , Action.BUILD);
        }

        ArrayList<CardColor> currentColorPriority = this.priorityColor.get(this.player.getHand().get(0).getAge()-1);

        for (CardColor color : currentColorPriority) {
            switch (color) {
                case GREEN:
                    Card bestScienceCard = this.getBestScienceCard();
                    Card bestBlockingCard = this.getBlockingScienceCard();
                    if (bestScienceCard != null) return new Action(bestScienceCard, Action.BUILD);
                    if (bestBlockingCard != null) return new Action(bestBlockingCard, Action.DUMP);
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
        for(int k = 0; k < size; k++) { res[k] = new ArrayList<>(); }
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
        HashMap<Card, Integer> bestCandidates = new HashMap<>();

        for (Card card : this.player.getHand()) {
            if (this.player.isBuildable(card)) {
                if (card.getCardPoints().get(bestSciencePoint) > 0) {
                    bestCandidates.put(card, 2*card.getCardPoints().get(bestSciencePoint));
                } else {
                    bestCandidates.put( card,
                                        card.getCardPoints().get(CardPoints.SCIENCE_WHEEL) +
                                        card.getCardPoints().get(CardPoints.SCIENCE_COMPASS) +
                                        card.getCardPoints().get(CardPoints.SCIENCE_TABLET));
                }
            }
        }

        if (bestCandidates.size() == 0) {
            return null;
        } else {
            Card res = null;
            int bestScore = 0;
            Iterator it = bestCandidates.entrySet().iterator();

            while (it.hasNext()) {
                Map.Entry<Card, Integer> pair = (Map.Entry) it.next();
                if (pair.getValue() > bestScore) {
                    bestScore = pair.getValue();
                    res = pair.getKey();
                }
            }
            return res;
        }
    }

    /**
     * If the player can't build a science card, he will try to dump a card in order to avoid its neighbors to win
     * by scientific victory.
     * @return the best card the player can block its neighbors with.
     */
    public Card getBlockingScienceCard() {
        Player playerToBlock;
        switch (this.player.getHand().get(0).getAge()) {
            case 2:
                playerToBlock = this.player.getNextNeighbor();
                break;
            default:
                playerToBlock = this.player.getPrevNeighbor();
                break;
        }

        int wheelPoints = playerToBlock.getPoints().get(CardPoints.SCIENCE_WHEEL);
        int compassPoints = playerToBlock.getPoints().get(CardPoints.SCIENCE_COMPASS);
        int tabletPoints = playerToBlock.getPoints().get(CardPoints.SCIENCE_TABLET);

        ArrayList<CardPoints> pointsToBlock = new ArrayList<>();
        if (wheelPoints >= compassPoints && wheelPoints >= tabletPoints) pointsToBlock.add(CardPoints.SCIENCE_WHEEL);
        if (compassPoints >= wheelPoints && compassPoints >= tabletPoints) pointsToBlock.add(CardPoints.SCIENCE_COMPASS);
        if (tabletPoints >= wheelPoints && tabletPoints >= compassPoints) pointsToBlock.add(CardPoints.SCIENCE_TABLET);

        ArrayList<Card> blockingCards = new ArrayList<>();
        for (Card c : this.player.getHand()) {
            if (c.getColor() == CardColor.GREEN) {
                for (CardPoints point : CardPoints.values()) {
                    if (c.getCardPoints().get(point) > 0 && pointsToBlock.contains(point)) {
                        blockingCards.add(c);
                    }
                }
            }
        }

        if (blockingCards.size() > 0) {
            return blockingCards.get(0);
        } else {
            return null;
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

    @Override
    public String toString() {
        return "Guaranteed";
    }
}
