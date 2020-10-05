package Player;

import Card.Card;
import Card.Resource;
import Card.CardPoints;
import Utility.Writer;

import java.io.IOException;
import java.util.Collections;

/**
 * This class is used to define a player that tries to maximise its military might.
 * A military player both tries to choose as many RED cards as possible, and to build them.
 */
public class MilitaryPlayer extends Player {

    /**
     * Will the chosen card be built or not ?
     */
    boolean buildChosenCard;

    /**
     * Calls Player::Player(String name) to instantiate the MilitaryPlayer
     * @param name The name of the player.
     */
    public MilitaryPlayer(String name) {
        super(name);
        this.buildChosenCard = false;
    }

    /**
     * This method will choose a card from hand based on two factors :
     * - Does this card give any military might ?
     * - Is this card buildable ?
     * If not, chooses the buildable card that gives the most resources.
     */
    @Override
    public void chooseCard() {
        Collections.shuffle(this.hand);
        try {
            Card preferredCard = new Card("altar", 6);
            for (Card c : this.hand) {
                if (c.getCardPoints().get(CardPoints.MILITARY) > preferredCard.getCardPoints().get(CardPoints.MILITARY) && this.isBuildable(c)) {
                    preferredCard = c;
                    this.buildChosenCard = true;
                }
            }
            if (!this.buildChosenCard) {
                int resourcesSum = 0;
                int tempResourcesSum = 0;
                for (Card c : this.hand) {
                    for (Resource r : Resource.values()) {
                        tempResourcesSum += c.getResource().get(r);
                    }
                    if (tempResourcesSum > resourcesSum && this.isBuildable(c)) {
                        resourcesSum = tempResourcesSum;
                        preferredCard = c;
                        this.buildChosenCard = true;
                    }
                }
            }
            if (!this.buildChosenCard) {
                preferredCard = this.hand.get(0);
            }

            this.chosenCard = preferredCard;
        } catch (IOException e) {
            Writer.write("altar.json could not be read.");
        }


    }

    /**
     * If chosen card gives military points and is buildable, tries to build it,
     * otherwise tries to build next stage of wonder using the card, and if that fails, dumps the card for coins.
     */
    @Override
    public void chooseAction() {

        if (this.buildChosenCard) {
            this.buildCard();
        } else {
            if(!this.buildStageWonder()){
                this.dumpCard();
            }
        }

        this.buildChosenCard = false;
    }

}
