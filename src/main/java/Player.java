import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Player {

    public ArrayList<Carte> getHand() {
        return hand;
    }

    private ArrayList<Carte> hand = new ArrayList<>(7);

    public String getName() {
        return name;
    }

    public Carte getChosenCard() {
        return chosenCard;
    }

    public int getCoins() {
        return coins;
    }

    public void setHand(ArrayList<Carte> hand) {
        this.hand = hand;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setChosenCard(Carte chosenCard) {
        this.chosenCard = chosenCard;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getPoints() {
        return points;
    }

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

        Random rand = new Random();
        int rand_int1 = rand.nextInt(1000);
        if(rand_int1 % 2 == 0) {
            hand.remove(chosenCard);
            System.out.println(name + "has now " + hand.size() + " cards in hand");
            System.out.println(name + " has obtained 3 coins for tossing");
            coins += 3;
        }
        else
            playCard();
    }

    private void playCard() {
        hand.remove(chosenCard);
        System.out.println(name + " played the card");
    }
}
