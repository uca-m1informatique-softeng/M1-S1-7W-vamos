package Core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Random;

import static Utility.Constante.MAX_HAND;

public class Player {

    private String name;

    private Card chosenCard;

    private int coins;

    public EnumMap<CardPoints, Integer> getPoints() {
        return points;
    }

    public void setPoints(EnumMap<CardPoints, Integer> points) {
        this.points = points;
    }

    public ArrayList<Card> getBuiltCards() {
        return builtCards;
    }

    public void setBuiltCards(ArrayList<Card> builtCards) {
        this.builtCards = builtCards;
    }

    private EnumMap<CardPoints, Integer> points;

    private ArrayList<Card> hand = new ArrayList<>(7);

    private ArrayList<Card> builtCards = new ArrayList<>();

    public Player(String name) {
        this.name = name;
        System.out.println("Core.Player " + name +  " joined the game!");
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

    public Card getChosenCard() {
        return chosenCard;
    }

    public void setChosenCard(Card chosenCard) {
        this.chosenCard = chosenCard;
    }

    public void initPlayerHand(){
        for(int i = 0; i < MAX_HAND ; i++)
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
            this.dumpCard();
        }
        else
            this.buildCard();
    }

    private void dumpCard() {
        this.hand.remove(this.chosenCard);
        System.out.println(this.name + "has now " + this.hand.size() + " cards in hand");
        System.out.println(this.name + " has obtained 3 coins for tossing");
        this.coins += 3;
    }

    private void buildCard() {
        this.hand.remove(this.chosenCard);
        this.builtCards.add(this.chosenCard);

        int currentVP = this.points.get(CardPoints.VICTORY);
        int cardVP = this.chosenCard.getCardPoints().get(CardPoints.VICTORY);

        this.points.put(CardPoints.VICTORY, currentVP + cardVP);
        System.out.println(this.name + " played the card " + this.chosenCard.getName() + " and got " + cardVP + " victory points.");
    }
}
