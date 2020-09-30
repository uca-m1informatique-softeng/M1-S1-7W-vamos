package Player;

import Card.Card;
import Card.CardPoints;
import Card.Resource;
import Card.ResourceChoiceEffect;
import Card.TradeResourceEffect;
import Core.Wonder;
import Utility.Writer;

import java.util.*;

public class Player {

    private String name;

    private Card chosenCard;

    private int militaryPoints;

    private EnumMap<CardPoints, Integer> points;

    private EnumMap<Resource, Integer> resources;

    private EnumMap<Resource, Integer> boughtResources;

    private ArrayList<Card> hand;

    private ArrayList<Card> builtCards;

    private Wonder wonder;

    private Player prevNeighbor;

    private Player nextNeighbor;

    public Random rand = new Random();


    public Player(String name) {
        this.name = name;
        this.militaryPoints = 0;

        this.points = new EnumMap<>(CardPoints.class);
        for (CardPoints p : CardPoints.values()) {
            this.points.put(p, 0);
        }

        this.resources = new EnumMap<>(Resource.class);
        for (Resource r : Resource.values()) {
            this.resources.put(r, 0);
        }

        this.boughtResources = new EnumMap<>(Resource.class);
        for (Resource r : Resource.values()) {
            this.boughtResources.put(r, 0);
        }

        this.builtCards = new ArrayList<>();
        this.hand = new ArrayList<>();

        Writer.write("Player " + name +  " joined the game!");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCoins() {
        return getResources().get(Resource.COIN);
    }

    public void setCoins(int coins) {
        this.resources.put(Resource.COIN, coins);
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

    public int getMilitaryPoints() { return this.militaryPoints; }

    public void addMilitaryPoints(int mp) {
        this.militaryPoints += mp;
    }

    public EnumMap<Resource, Integer> getResources() {
        return resources;
    }

    public EnumMap<Resource, Integer> getBoughtResources() {
        return boughtResources;
    }

    public Player getPrevNeighbor() {
        return prevNeighbor;
    }

    public Player getNextNeighbor() {
        return nextNeighbor;
    }

    public void setPrevNeighbor(Player prevNeighbor) {
        this.prevNeighbor = prevNeighbor;
    }

    public void setNextNeighbor(Player nextNeighbor) {
        this.nextNeighbor = nextNeighbor;
    }

    public void chooseCard(){
        Collections.shuffle(hand);
        chosenCard = hand.get(0);
    }

    public void chooseAction(){
        int rand_int1 = rand.nextInt(1000);
        if(rand_int1 % 2 == 0) {
            this.dumpCard();
        }
        else
            this.buildCard();
    }

    /**
     * Remove the chosen card of this hand and give 3 coins to the player.
     */
    public void dumpCard() {
        this.hand.remove(this.chosenCard);
        Writer.write(this.name + "has now " + this.hand.size() + " cards in hand");
        Writer.write(this.name + " has obtained 3 coins for tossing");
        this.setCoins(this.getCoins() + 3);
    }

    /**
     * buildCard() ajoute la carte à l'inventaire builtCards, ainsi que les points et ressources SEULEMENT
     * si il à assez de ressource, sinon elle est revendu 3 d'or avec la fonction dumpCard.
     * Seul le cout en or est supprimmer.
     */
    public void buildCard() {
        boolean enoughResources = true ;

        // Here, the resourceChoiceEffects are applied, in order to smartly choose the resources every card should produce in order to build currentCard
        EnumMap<Resource, Integer> costAfterEffects = this.chosenCard.getCost();
        for (Card card : this.builtCards) {
            if (card.getEffect() != null && card.getEffect() instanceof ResourceChoiceEffect) {
                ((ResourceChoiceEffect) (card.getEffect())).applyEffect(costAfterEffects);
            }
        }

        // Here the player will try to buy resources from its neighbors if he doesn't have enough in order to buildcurrentCard
        for (Resource resource : costAfterEffects.keySet()){
            if (costAfterEffects.get(resource) > this.resources.get(resource)){

                int missingResources = costAfterEffects.get(resource) - this.resources.get(resource) - this.boughtResources.get(resource);
                this.buyResource(resource, missingResources, this.prevNeighbor);

                missingResources -= this.boughtResources.get(resource);
                if (missingResources > 0) {
                    if (!this.buyResource(resource, missingResources, this.nextNeighbor)) break;
                }
            }
        }

        for (Resource resource : costAfterEffects.keySet()) {
            if (costAfterEffects.get(resource) > this.resources.get(resource) + this.boughtResources.get(resource)) {
                enoughResources = false;
            }
        }

        if (chosenCard.isFree())
        {
            this.builtCards.add(this.chosenCard);
            addPointsAndResources();
            this.hand.remove(this.chosenCard);
        }
        else{ //not a free card sowe check if the player have enough resources to build the card
            if (enoughResources){
                this.builtCards.add(this.chosenCard);
                addPointsAndResources();

                //removing the cost of a card if it's not a free card
                this.resources.put(Resource.COIN, this.resources.get(Resource.COIN) - this.chosenCard.getCost().get(Resource.COIN)  );

                this.hand.remove(this.chosenCard);
            }
            else{ //if the player don't have enough resources to buy the card he toss it
                Writer.write("Not enough ressources");
                this.dumpCard();
            }
        }

    }

    /**
     * Buys a resource from a neighbor in case the player doesn't have enough resources to build a card.
     * @param resourceToBuy Resource the player wishes to buy
     * @param neighbor Neighbor to buy the resource from
     * @return true if resource could be bought, false if not
     */
    public boolean buyResource(Resource resourceToBuy, int quantity, Player neighbor) {
        HashMap<String, ArrayList<Resource>> tradeResourceModifier = new HashMap<>();
        tradeResourceModifier.put("prev", new ArrayList<>());
        tradeResourceModifier.put("next", new ArrayList<>());

        for (Card card : this.builtCards) {
            if (card.getEffect() instanceof TradeResourceEffect) {
                TradeResourceEffect effect = ((TradeResourceEffect) card.getEffect());
                if (effect.isPrevPlayerAllowed()) {
                    for (Resource r : effect.getResourcesModified()) {
                        tradeResourceModifier.get("prev").add(r);
                    }
                } else if (effect.isNextPlayerAllowed()) {
                    for (Resource r : effect.getResourcesModified()) {
                        tradeResourceModifier.get("next").add(r);
                    }
                }
            }
        }

        for (Resource r : neighbor.getResources().keySet()) {
            if (resourceToBuy.equals(r) && neighbor.getResources().get(r) >= quantity) {

                this.boughtResources.put(r, this.boughtResources.get(r) + quantity);

                if (   ((this.prevNeighbor.equals(neighbor) && tradeResourceModifier.get("prev").contains(r)) ||
                        (this.nextNeighbor.equals(neighbor) && tradeResourceModifier.get("next").contains(r))) &&
                         this.getCoins() >= 1) {
                    neighbor.setCoins(neighbor.getCoins() + quantity);
                    this.setCoins(this.getCoins() - quantity);

                    Writer.write(this + " buys " + quantity + " " + r + " from " + neighbor + " for " + quantity + " coin(s).");

                    return true;
                } else if (this.getCoins() >= 2){
                    neighbor.setCoins(neighbor.getCoins() + 2*quantity);
                    this.setCoins(this.getCoins() - 2*quantity);

                    Writer.write(this + " buys " + quantity + " " + r + " from " + neighbor + " for " + 2*quantity + " coin(s).");


                }
            }
        }
        return false;
    }

    /**
     * Clears all the bought resources from the player.
     * Should be called at the end of every turn.
     */
    public void clearBoughtResources() {
        for (Resource r : Resource.values()) {
            this.boughtResources.put(r, 0);
        }
    }

    public void addPointsAndResources(){
        //adding points if the card gives a points
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
        for(Resource resource : this.chosenCard.getResource().keySet()){
            int currentResource = this.resources.get(resource);
            int cardResource = this.chosenCard.getResource().get(resource);
            this.resources.put(resource, currentResource + cardResource);
            if (cardResource != 0){
                Writer.write(this.name + " played the card " + this.chosenCard.getName() + " and got " + cardResource +" " + resource );
            }
        }

        Writer.write(this.name + " played the card " + this.chosenCard.getName() + " and got " + cardVP + " victory points.");

        this.clearBoughtResources();
    }

    public int computeScore() {
        int res = 0;

        // Military points
        res += this.militaryPoints;
        // Treasury Contents
        res += this.getCoins()/3;
        // Civilian Structures and Wonders
        res += this.getPoints().get(CardPoints.VICTORY);
        res += getSciencePoint();

        return res;
    }

    public int getSciencePoint() {
        int res = 0;
        // Science score
        res += this.getPoints().get(CardPoints.SCIENCE_WHEEL)* this.getPoints().get(CardPoints.SCIENCE_WHEEL);
        res += this.getPoints().get(CardPoints.SCIENCE_COMPASS)*this.getPoints().get(CardPoints.SCIENCE_COMPASS);
        res += this.getPoints().get(CardPoints.SCIENCE_TABLET)*this.getPoints().get(CardPoints.SCIENCE_TABLET);
        // Sets of different symbols
        res += 7*Math.min(Math.min(this.getPoints().get(CardPoints.SCIENCE_TABLET), this.getPoints().get(CardPoints.SCIENCE_WHEEL)), Math.min(this.getPoints().get(CardPoints.SCIENCE_WHEEL), this.getPoints().get(CardPoints.SCIENCE_COMPASS)));
        return res;
    }

    public Wonder getWonder() {
        return wonder;
    }

    public void setWonder(Wonder wonder) {
        this.wonder = wonder;
    }

    @Override
    public String toString() {
        return this.name ;
    }

}
