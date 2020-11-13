package player;

import card.Card;
import card.CardColor;

import java.util.ArrayList;

/**
 * This AI can manage the order of color priority.
 */
public class GuaranteedStrategy extends Strategy{

    private ArrayList<ArrayList<CardColor>> priorityColor = new ArrayList();
    //age/player/hand is defined in chooseAction(), this may cause bug if is not call.
    private int age; //Redefine at each call of chooseAction
    private Player player;
    private ArrayList<Card> hand;

    public GuaranteedStrategy() {
        //Fix color priority.
        ArrayList<CardColor> age1 = quickList(CardColor.GREEN, CardColor.GREY);
        ArrayList<CardColor> age2 = quickList(CardColor.GREEN, CardColor.BROWN, CardColor.GREY);
        ArrayList<CardColor> age3 = quickList(CardColor.GREEN, CardColor.RED);

        //Add Color
        this.priorityColor.add(age1);
        this.priorityColor.add(age2);
        this.priorityColor.add(age3);
    }

    /**
     * Facilitate access to the player's hand, current age, and the current player.
     * @param player
     */
    protected void init(Player player){
        this.age = player.getHand().get(0).getAge();
        this.player = player;
        this.hand = player.getHand();
    }

    @Override
    public Action chooseAction(Player player) {
        init(player);
        ArrayList<Integer>[] colorIndexSet = reduceChoice();
        int indexChosen = 0;
        for (ArrayList<Integer> Lindex : colorIndexSet) {
            if (!Lindex.isEmpty()) {
                indexChosen = Lindex.get(0);
                break;
            }
        }
        return new Action(this.hand.get(indexChosen), 3);
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
    protected ArrayList<Integer>[] reduceChoice(){
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
}
