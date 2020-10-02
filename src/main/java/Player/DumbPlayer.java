package Player;

import java.util.Collections;

/**
 * This class is used to define a player that choose actions in a random manner.
 */
public class DumbPlayer extends Player {

    /**
     * Calls Player::Player(String name) to instantiate the DumbPlayer
     * @param name The name of the player.
     */
    public DumbPlayer(String name) {
        super(name);
    }

    /**
     * Used to pick a card from payer's hand in a "dumb" manner.
     * Shuffles the player's hand, then picks the first card in the hand.
     */
    @Override
    public void chooseCard(){
        Collections.shuffle(this.hand);
        this.chosenCard = this.hand.get(0);
    }

    /**
     * Used to choose the Action to perform in a "dumb" manner.
     * Picks a random int, with 60% probability to try to build it, 10% to try to build a wonder stage,
     * and 30% to dump the card in order to get coins from it.
     */
    @Override
    public void chooseAction() {
        int rand_int1 = rand.nextInt(1000);
        if (rand_int1 < 600) {
            if(!this.buildCard()){
                chooseAction();
            }
        } else if (rand_int1 >= 600 && rand_int1 < 700){
            if(!this.buildStageWonder()){
                chooseAction();
            }
        }
        else {
            this.dumpCard();
        }
    }

}
