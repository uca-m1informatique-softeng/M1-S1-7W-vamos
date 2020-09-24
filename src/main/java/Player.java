import java.util.ArrayList;
import java.util.Collections;

public class Player {

    public ArrayList<Carte> getHand() {
        return hand;
    }

    private ArrayList<Carte> hand = new ArrayList<>(7);

    private String name;

    private Carte chosenCard;

    private int coins;

    private int points;
    public Player(String name)
    {
        this.name = name;
        System.out.println("Player " + name +  " joined the game!");
    }


    public void initPlayerHand(){
        for(int i = 0; i < 7 ; i++)
            hand.add(null);
    }


    public void chooseCard(){
        Collections.shuffle(hand);
        chosenCard = hand.get(0);
    }

    public void chooseAction(){
        hand.remove(chosenCard);
        System.out.println( name + "has now " + hand.size() + " cards in hand");
        System.out.println( name + " has obtained 3 coins for tossing");
        coins += 3;
    }
}
