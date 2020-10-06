package Player;

import Card.CardColor;
import Card.Effect;
import Card.ScienceChoiceEffect;

public class IA_One extends Player{

    public IA_One(String name) {
        super(name);
    }

    @Override
    public void chooseCard() {
        this.chosenCard = this.hand.get(0);
        if (!chooseCard(new ScienceChoiceEffect())
           && !chooseCard(CardColor.GREY)
           && !chooseCard(CardColor.BROWN))
            chooseCard(CardColor.GREEN);


    }

    @Override
    public void chooseAction() {
        chooseCard();
        if(!isBuildable(this.getChosenCard()))
            dumpCard();
        buildCard();
    }

    /**
     * Choose a card in the hand player in function of her color(the parameter), for set the choosenCard.
     * @param c color of the card
     * @return true if he find the card and set the choosenCard, otherwise he return false and set to a default card.
     */
    protected boolean chooseCard(CardColor c) {
        this.chosenCard = this.hand.get(0); // default choice
        for(int i = 0; i < this.getHand().size(); i++){
            if(this.getHand().get(i).getColor() == c && isBuildable(this.getHand().get(i))){
                    this.chosenCard = this.getHand().get(i);
                    return true;
            }
        }
        return false;
    }

    /**
     * Do the same function of chooseCart(CardColor c), but in relation to its effect.
     * @param e color of the card
     * @return true/false find/don't find the wished card.
     */
    protected boolean chooseCard(Effect e){
        this.chosenCard = this.hand.get(0); // default choice
        for(int i = 0; i < this.getHand().size(); i++) {
            if (this.getHand().get(i).getEffect() == e && isBuildable(this.getHand().get(i)))
            {
                    this.chosenCard = this.getHand().get(i);
                    return true;
            }
        }
        return false;
    }

}
