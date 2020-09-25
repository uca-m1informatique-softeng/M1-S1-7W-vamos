package Core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import static Utility.Constante.MAX_HAND;

public class  Player {

    private String name;

    private Card chosenCard;

    private int coins;

    private int points;

    private ArrayList<Card> hand = new ArrayList<>(7);

    public Player(String name) {
        this.name = name;
        System.out.println("Player " + name +  " joined the game!");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public ArrayList<Card> getHand() {
        return hand;
    }

    public void setHand(ArrayList<Card> hand) {
        this.hand = hand;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public Card getChosenCard() {
        return chosenCard;
    }

    public void setChosenCard(Card chosenCard) {
        this.chosenCard = chosenCard;
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
