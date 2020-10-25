package player;

import card.Card;

/**
 * Class used to define an action that the player can do each turn.
 * In 7 Wonders, you can either build a card, dump a card, or use the card to build the next stage of your wonder.
 */
public class Action {

    public static final int BUILD = 1;
    public static final int WONDER = 2;
    public static final int DUMP = 3;

    /**
     * The card with which to realize the action
     */
    private Card card;

    /**
     * The action to realize with this card.
     * Action.BUILD if card should be built, Action.WONDER if card should be used to build a wonder, and Action.DUMP
     * if card should be dumped.
     */
    private int choice;


    /**
     * Dumb constructor. Should only be used from within Strategy children.
     * @param card The card on which the action should be executed.
     * @param choice The action that should be done with the card.
     */
    protected Action(Card card, int choice) {
        this.card = card;
        this.choice = choice;
    }


    public Card getCard() {
        return this.card;
    }

    public int getAction() {
        return this.choice;
    }
}
