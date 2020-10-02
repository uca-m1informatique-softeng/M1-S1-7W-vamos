package Player;

import Card.CardColor;
import Utility.Writer;

public class IA_One extends Player{

    public IA_One(String name) {
        super(name);
    }

    @Override
    public void chooseCard() {
        this.chosenCard = this.hand.get(0);
        if(!chooseCardColor(CardColor.GREY)){
            if(!chooseCardColor(CardColor.BROWN)){
                chooseCardColor(CardColor.GREEN);
            }
        }
    }

    @Override
    public void chooseAction() {
        chooseCard();
        if(!isBuildable(this.getChosenCard())) {
            dumpCard();
        }
        buildCard();
    }

    protected boolean chooseCardColor(CardColor c) {
        this.chosenCard = this.hand.get(0); // default choice
        for(int i = 0; i < this.getHand().size(); i++){
            if(this.getHand().get(i).getColor() == c){
                if(isBuildable(this.getHand().get(i))) {
                    this.chosenCard = this.getHand().get(i);
                    return true;
                }
            }
        }
        return false;
    }

}
