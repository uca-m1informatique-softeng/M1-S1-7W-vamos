package player;

import card.*;
import effects.*;

public class ScienceStrategy extends Strategy {

    Card chosenCard;

    @Override
    public Action chooseAction(Player player) {

        this.chosenCard = player.hand.get(0);
        if(!chooseCard(player, ScienceChoiceEffect.class)) {
            if (!chooseCard(player, CardColor.GREY)) {
                if (!chooseCard(player, CardColor.BROWN)) {
                    chooseCard(player, CardColor.GREEN);
                }
            }
        }

        if (player.isBuildable(this.chosenCard)) {
            return new Action(this.chosenCard, Action.BUILD);
        } else {
            return new Action(this.chosenCard, Action.DUMP);
        }
    }

    /**
     * Choose a card in the hand player in function of her color(the parameter), for set the choosenCard.
     * @param c
     * @return true if he find the card and set the choosenCard, otherwise he return false and set to a default card.
     */
    protected boolean chooseCard(Player player, CardColor c) {
        this.chosenCard = player.hand.get(0); // default choice
        for(int i = 0; i < player.getHand().size(); i++){
            if(player.getHand().get(i).getColor() == c){
                if(player.isBuildable(player.getHand().get(i))) {
                    this.chosenCard = player.getHand().get(i);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Do the same function of chooseCart(CardColor c), but in relation to its effect.
     * @param e
     * @return true/false find/don't find the wished card.
     */
    protected boolean chooseCard(Player player, Class e){
        this.chosenCard = player.hand.get(0); // default choice
        for(int i = 0; i < player.getHand().size(); i++) {
            if (player.getHand().get(i).getEffect() != null &&(player.getHand().get(i).getEffect()).getClass().equals(e)) {
                if (player.isBuildable(player.getHand().get(i))) {
                    this.chosenCard = player.getHand().get(i);
                    return true;
                }
            }
        }
        return false;
    }
}
