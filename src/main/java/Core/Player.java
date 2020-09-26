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

    private EnumMap<CardPoints, Integer> points;

    private EnumMap<Resource,Integer> resources;

    private ArrayList<Card> hand;

    private ArrayList<Card> builtCards;

    public Player(String name) {
        this.name = name;

        this.points = new EnumMap<>(CardPoints.class);
        this.points.put(CardPoints.VICTORY, 0);
        this.points.put(CardPoints.MILITARY, 0);
        this.points.put(CardPoints.SCIENCE_COMPASS, 0);
        this.points.put(CardPoints.SCIENCE_TABLET, 0);
        this.points.put(CardPoints.SCIENCE_WHEEL, 0);

        this.resources=new EnumMap<Resource, Integer>(Resource.class);
        this.resources.put(Resource.WOOD,0);
        this.resources.put(Resource.STONE,0);
        this.resources.put(Resource.ORE,0);
        this.resources.put(Resource.CLAY,0);
        this.resources.put(Resource.GLASS,0);
        this.resources.put(Resource.LOOM,0);
        this.resources.put(Resource.PAPYRUS,0);

        this.builtCards = new ArrayList<>();
        this.hand = new ArrayList<>();

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

    public EnumMap<CardPoints, Integer> getPoints() {
        return points;
    }

    public ArrayList<Card> getBuiltCards() {
        return builtCards;
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

    public void dumpCard() {
        this.hand.remove(this.chosenCard);
        System.out.println(this.name + "has now " + this.hand.size() + " cards in hand");
        System.out.println(this.name + " has obtained 3 coins for tossing");
        this.coins += 3;
    }

    public void buildCard() {
        Boolean enoughResources=true ;
        for(Resource resource : this.chosenCard.getCost().keySet()){
            if(this.chosenCard.getCost().get(resource) > this.resources.get(resource)){
                enoughResources=false ;
            }
        }

        if (enoughResources){
            this.builtCards.add(this.chosenCard);

            //points given by a card
            int currentVP = this.points.get(CardPoints.VICTORY);
            int cardVP = this.chosenCard.getCardPoints().get(CardPoints.VICTORY);
            this.points.put(CardPoints.VICTORY, currentVP + cardVP);

            int currentMP = this.points.get(CardPoints.MILITARY);
            int cardMP = this.chosenCard.getCardPoints().get(CardPoints.MILITARY);
            this.points.put(CardPoints.MILITARY, currentMP + cardMP);

            int currentSWP = this.points.get(CardPoints.SCIENCE_WHEEL);
            int cardSWP = this.chosenCard.getCardPoints().get(CardPoints.SCIENCE_WHEEL);
            this.points.put(CardPoints.SCIENCE_WHEEL, currentSWP + cardSWP);

            int currentSTP = this.points.get(CardPoints.SCIENCE_TABLET);
            int cardSTP = this.chosenCard.getCardPoints().get(CardPoints.SCIENCE_TABLET);
            this.points.put(CardPoints.SCIENCE_TABLET, currentSTP + cardSTP);

            int currentSCP = this.points.get(CardPoints.SCIENCE_COMPASS);
            int cardSCP = this.chosenCard.getCardPoints().get(CardPoints.SCIENCE_COMPASS);
            this.points.put(CardPoints.SCIENCE_COMPASS, currentSCP + cardSCP);

            //adding resource(s) if the card gives a ressource(s)
            int currentWOOD=this.resources.get(Resource.WOOD);
            int cardWOOD=this.chosenCard.getResource().get(Resource.WOOD);
            this.resources.put(Resource.WOOD, currentWOOD + cardWOOD);

            int currentSTONE=this.resources.get(Resource.STONE);
            int cardSTONE=this.chosenCard.getResource().get(Resource.STONE);
            this.resources.put(Resource.STONE, currentSTONE + cardSTONE);

            int currentORE=this.resources.get(Resource.ORE);
            int cardORE=this.chosenCard.getResource().get(Resource.ORE);
            this.resources.put(Resource.ORE, currentORE + cardORE);

            int currentCLAY=this.resources.get(Resource.CLAY);
            int cardCLAY=this.chosenCard.getResource().get(Resource.CLAY);
            this.resources.put(Resource.CLAY, currentCLAY + cardCLAY);

            int currentGLASS=this.resources.get(Resource.GLASS);
            int cardGLASS=this.chosenCard.getResource().get(Resource.GLASS);
            this.resources.put(Resource.GLASS, currentGLASS + cardGLASS);

            int currentLOOM=this.resources.get(Resource.LOOM);
            int cardLOOM=this.chosenCard.getResource().get(Resource.LOOM);
            this.resources.put(Resource.LOOM, currentLOOM + cardLOOM);

            int currentPAPYRUS=this.resources.get(Resource.PAPYRUS);
            int cardPAPYRUS=this.chosenCard.getResource().get(Resource.PAPYRUS);
            this.resources.put(Resource.PAPYRUS, currentPAPYRUS + cardPAPYRUS);

            this.hand.remove(this.chosenCard);

            System.out.println(this.name + " played the card " + this.chosenCard.getName() + " and got " + cardVP + " victory points.");
        }



    }
}
