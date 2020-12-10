package player;

import card.Card;
import card.CardColor;
import card.CardPoints;
import card.Resource;
import effects.ResourceChoiceEffect;

import java.util.*;

/**
 * This AI can manage the order of color priority.
 */
public class GuaranteedStrategy extends Strategy {

    private ArrayList<ArrayList<CardColor>> priorityColor = new ArrayList<>();
    //age/player/hand is defined in chooseAction(), this may cause bug if is not call.
    private Player player;

    public GuaranteedStrategy(Player player) {
        //Fix color priority.
        ArrayList<CardColor> age1 = quickList(CardColor.GREEN, CardColor.GREY, CardColor.BLUE);
        ArrayList<CardColor> age2 = quickList(CardColor.GREEN, CardColor.BROWN, CardColor.GREY, CardColor.BLUE);
        ArrayList<CardColor> age3 = quickList(CardColor.GREEN, CardColor.BLUE);

        //Add Color
        this.priorityColor.add(age1);
        this.priorityColor.add(age2);
        this.priorityColor.add(age3);

        this.player = player;
    }

    @Override
    public Action chooseAction(Player player) {
        // If the player has a military card and can afford it , he builds it
        if(this.militaryCardInHand() && this.usefulBuildMilitary()){
            ArrayList<Integer> militaryCardIndexes = this.militaryCardIndexes() ;
            for (int i = 0; i < militaryCardIndexes.size(); i++) {
                Card cardToBuild = player.getHand().get(militaryCardIndexes.get(i));
                if (player.isBuildable(cardToBuild)){
                    return new Action(cardToBuild , Action.BUILD) ;
                }
            }
        }
        // Prioritize color of cards: depending on age
        ArrayList<CardColor> currentColorPriority = this.priorityColor.get(this.player.getHand().get(0).getAge() - 1);
        ArrayList<Integer>[] setOfIndexByColor = cardGroupedByPriorityColor();
        for (int i = 0; i < currentColorPriority.size(); i++) {
            CardColor color = currentColorPriority.get(i);
            switch (color) {
                case GREEN:
                    Card bestScienceCard = this.getBestScienceCard();
                    Card bestBlockingCard = this.getBlockingScienceCard();
                    if (bestScienceCard != null) return new Action(bestScienceCard, Action.BUILD);
                    if (bestBlockingCard != null) return new Action(bestBlockingCard, Action.DUMP);
                    break;
                case GREY:
                    if (!setOfIndexByColor[i].isEmpty()) {
                        int indexChoosen = bestGrey(setOfIndexByColor[i]);
                        return new Action(player.getHand().get(indexChoosen), Action.BUILD);
                    }
                    break;
                case BROWN:
                    if (!setOfIndexByColor[i].isEmpty()) {
                        int indexChoosen = bestBrown(setOfIndexByColor[i]);
                        return new Action(player.getHand().get(indexChoosen), Action.BUILD);
                    }
                    break;
                case BLUE:
                    if (!setOfIndexByColor[i].isEmpty()) {
                        int indexChoosen = bestBlue(setOfIndexByColor[i]);
                        return new Action(player.getHand().get(indexChoosen), Action.BUILD);
                    }
                default :
                    Card cardToDump = this.getBlockingDumpCard();
                    if (this.player.getWonder().canUpgrade(this.player.getResources())) {
                        return new Action(cardToDump, Action.WONDER);
                    } else {
                        return new Action(cardToDump, Action.DUMP);
                    }
            }
        }
        //If the player has the marketPlace card in his hand he builds it (marketPlace is a free Card , so there is no need to check if it's buildable)
        if (this.marketPlaceInHand()) {
            int marketPlaceIndex = this.marketPlaceIndex();
            return new Action(this.player.getHand().get(marketPlaceIndex), Action.BUILD);
        }
        // If the player can't build any card , he tosses the first card in his hand
        return new Action(this.player.getHand().get(0), Action.DUMP);
    }

    /**
     * Can rapidly set a list in order we enter the elements.
     * @param colors is type CardColor
     * @return list with the CardColor put on parameters
     */
    protected ArrayList<CardColor> quickList(CardColor... colors) {
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
    protected ArrayList<Integer>[] cardGroupedByPriorityColor() {
        int age = this.player.getHand().get(0).getAge();


        ArrayList<Integer>[] indexL = initArray(priorityColor.get(age - 1).size());
        Card card;
        for (int i = 0; i < this.player.getHand().size(); i++) {
            card = this.player.getHand().get(i);
            for (int j = 0; j < priorityColor.get(age - 1).size(); j++) {
                CardColor color = priorityColor.get(age - 1).get(j);
                if (color.equals(card.getColor())) {
                    if (!player.alreadyBuilt(card) && player.isBuildable(card)) {
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
            case 0:
                return CardPoints.SCIENCE_WHEEL;
            case 1:
                return CardPoints.SCIENCE_COMPASS;
            default:
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
                    bestCandidates.put(card, 2 * card.getCardPoints().get(bestSciencePoint));
                } else {
                    bestCandidates.put(card,
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
        Player playerToBlock = this.getPlayerToBlock();

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
     * Checks the player's hand for the best card to either dump or build a next stage of wonder with,
     * according to player's current resources, money that the player can trade resources with, current victory points
     * of its neighbors, etc...
     * @return The best card to either dump or build a next stage of wonder with.
     */
    public Card getBlockingDumpCard() {
        Player playerToBlock = this.getPlayerToBlock();
        int highestCardPointScore = 0;
        CardPoints highestCardPoint = null;

        for (CardPoints cp : CardPoints.values()) {
            int cpScore = playerToBlock.getPoints().get(cp);
            if (cpScore >= highestCardPointScore && cp != CardPoints.RELAY_COIN) {
                highestCardPointScore = cpScore;
                highestCardPoint = cp;
            }
        }

        highestCardPointScore = 0;
        Card bestBlockingCard = null;

        for (Card card : this.player.getHand()) {
            int cardScore = card.getCardPoints().get(highestCardPoint);
            if (cardScore >= highestCardPointScore) {
                highestCardPointScore = cardScore;
                bestBlockingCard = card;
            }
        }

        return bestBlockingCard;
    }

    /**
     * Gets the player to player to block according to current age
     * @return Next player to pass the hand to.
     */
    private Player getPlayerToBlock() {
        Player playerToBlock;
        if (this.player.getHand().get(0).getAge() == 2) {
            playerToBlock = this.player.getNextNeighbor();
        } else {
            playerToBlock = this.player.getPrevNeighbor();
        }
        return playerToBlock;
    }

    /**
     * Checks if the marketPlace card is in the player's hand
     * @return true if the player has the marketPlace card , false otherwise
     */
    public boolean marketPlaceInHand() {
        for (int i = 0; i < this.player.getHand().size(); i++) {
            if (this.player.getHand().get(i).getName().equals("marketplace")) {
                return true;
            }
        }
        return false;
    }

    /**
     * If the player has the marketPlace card , this method returns it's index in the players hand
     * @return the index of the marketPlace card in the player's hand .If the player don't have the marketPlace card in his hand it returns -1
     */
    public int marketPlaceIndex() {
        if (this.marketPlaceInHand()) {
            for (int i = 0; i < this.player.getHand().size(); i++) {
                if (this.player.getHand().get(i).getName().equals("marketplace")) {
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * Checks if the player have military cards in his hands
     * @return true if the player has military cards in his hands , false otherwise
     */
    public boolean militaryCardInHand() {
        for (int i = 0; i < this.player.getHand().size(); i++) {
            if (this.player.getHand().get(i).getColor() == CardColor.RED) { // Military Cards are the Red Cards
                return true;
            }
        }
        return false;
    }

    /**
     * This method returns the indexes of military cards .
     * @return the indexes of military cards . Empty if the player don't have any military cards ,
     */
    public ArrayList<Integer> militaryCardIndexes() {
        ArrayList<Integer> militaryCardIndexes = new ArrayList<>();
        if (this.militaryCardInHand()) {
            for (int i = 0; i < this.player.getHand().size(); i++) {
                if (this.player.getHand().get(i).getColor() == CardColor.RED) {
                    militaryCardIndexes.add(i);
                }
            }
        }
        return militaryCardIndexes;
    }

    /**
     * This method tells if by building military cards the player will win the fights against his neighbors or not
     * @return
     */
    public boolean usefulBuildMilitary(){
        boolean nextNeighborGotMoreFightPoints = player.getNextNeighbor().getFightPoints() >= this.player.getFightPoints() ;
        boolean prevNeighborGotMoreFightPoints = player.getPrevNeighbor().getFightPoints() >= this.player.getFightPoints() ;
        if (nextNeighborGotMoreFightPoints || prevNeighborGotMoreFightPoints) {
            return true ;
        }
        return false ;
    }

    /**
     * The best brown card to buy is the resource we don't yet have.
     * The priority order is two resource with choice effect, two resource, o
     *
     * @return Returns the index of the best brown card  to buy.
     */
    protected int bestBrown(ArrayList<Integer> indexs) {
        int pos = -1;
        //Two resource with choice effect
        for (int i : indexs) {
            if (player.getHand().get(i).getEffect() instanceof ResourceChoiceEffect) {
                ResourceChoiceEffect rce = (ResourceChoiceEffect) player.getHand().get(i).getEffect();
                if (player.getResources().get(rce.getRes().get(0)) == 0 && player.getResources().get(rce.getRes().get(1)) == 0) {
                    return i;
                }
                if (player.getResources().get(rce.getRes().get(0)) == 0 || player.getResources().get(rce.getRes().get(1)) == 0) {
                    pos = i;
                }
            }
        }
        if (pos != -1) {
            return pos;
        }
        for (int i : indexs) {
            for (Resource r : Resource.values()) {
                //Two resource
                if (player.getHand().get(i).getResource().get(r) == 2) {
                    if (player.getResources().get(r) == 0) {
                        return i;
                    }
                    break;
                }
                //One resource
                if (player.getHand().get(i).getResource().get(r) == 1) {
                    if (player.getResources().get(r) == 0) {
                        pos = i;
                    }
                    break;
                }
            }
        }
        if (pos != -1) {
            return pos;
        }
        return indexs.get(0);
    }

    /**
     * Return the index of the blue card with the higher victory points.
     * @param indexs
     * @return
     */
    int bestBlue(ArrayList<Integer> indexs) {
        int pos = indexs.get(0);
        int vict = player.getHand().get(indexs.get(0)).getCardPoints().get(CardPoints.VICTORY);
        for (int i : indexs) {
            int tmpVict = player.getHand().get(i).getCardPoints().get(CardPoints.VICTORY);
            if (tmpVict > vict) {
                pos = i;
                vict = tmpVict;
            }
        }
        return pos;
    }

    /**
     * Return the grey card who give the ressource we don't have like starter resource.
     * @param indexs
     * @return
     */
    int bestGrey(ArrayList<Integer> indexs) {
        int pos = indexs.get(0);
        Resource initialR = player.wonder.getProducedResource();
        for (int i : indexs) {
            for (Resource r : Resource.values()) {
                if (player.getHand().get(i).getResource().get(r) > 0 && player.getHand().get(i).getResource().get(initialR) == 0) {
                    return i;
                }
            }
        }
        return pos;
    }

    @Override
    public String toString() {
        return "Guaranteed";
    }
}
