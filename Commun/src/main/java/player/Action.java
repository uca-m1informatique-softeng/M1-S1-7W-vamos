package player;

import card.Card;

/**
 * Class used to define an action that the player can do each turn.
 * In 7 Wonders, you can either build a card, dump a card, or use the card to build the next stage of your wonder.
 */
public class Action {

    protected static final int BUILD = 1;
    protected static final int WONDER = 2;
    protected static final int DUMP = 3;

    /**
     * The card with which to realize the action
     */
    private Card card;

    /**
     * The action to realize with this card.
     * Action.BUILD if card should be built, Action.WONDER if card should be used to build a wonder, and Action.DUMP
     * if card should be dumped.
     */
    private int action;


    /**
     * Dumb constructor. Should only be used from within Strategy children.
     * @param card The card on which the action should be executed.
     * @param action The action that should be done with the card.
     */
    protected Action(Card card, int action) {
        this.card = card;
        this.action = action;
    }


    public Card getCard() {
        return this.card;
    }

    public int getAction() {
        return this.action;
    }
}
