package Player;

import Card.CardColor;

public class IA_One extends Player{

    public IA_One(String name) {
        super(name);
    }

    @Override
    public void chooseCard() {
        this.chosenCard = this.hand.get(0);
        if(!chooseCard(CardColor.GREY)){
            if(!chooseCard(CardColor.BROWN)){
                chooseCard(CardColor.GREEN);
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

    protected boolean chooseCard(CardColor c) {
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
