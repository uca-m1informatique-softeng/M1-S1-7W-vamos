package player;

import effects.*;
import card.*;
import wonder.Wonder;
import utility.Writer;
import java.security.SecureRandom;
import java.util.*;

public class Player {

    protected String name;

    protected Card chosenCard;

    protected EnumMap<CardPoints, Integer> points;

    protected EnumMap<Resource, Integer> resources;

    protected EnumMap<Resource, Integer> boughtResources;

    protected ArrayList<Card> hand;

    protected ArrayList<Card> builtCards;

    protected Wonder wonder;

    protected ArrayList<Effect> wonderEffectNotApply;

    protected Player prevNeighbor;

    protected Player nextNeighbor;

    protected SecureRandom rand = new SecureRandom();

    protected int defeatToken ;

    protected Strategy strategy;

    protected HashMap<Integer , Boolean> freeCardPerAge ; //{1:true or false ...} true if the effect was used in the age(the key) and false if not

    protected Card dumpCard;


    public Player(String name) {
        this.name = name;
        this.defeatToken = 0;

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
        this.wonderEffectNotApply = new ArrayList<>();

        this.freeCardPerAge=new HashMap<>() ;
        freeCardPerAge.put(1, false);
        freeCardPerAge.put(2, false);
        freeCardPerAge.put(3, false);

        Writer.write("Player " + name + " joined the game!");
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

    public int getMilitaryPoints() {
        return getPoints().get(CardPoints.MILITARY);
    }

    public void addMilitaryPoints(int mp) {
        mp = mp + getMilitaryPoints();
        this.points.put(CardPoints.MILITARY, mp);
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

    public Wonder getWonder() {
        return wonder;
    }

    public void setWonder(Wonder wonder) {
        this.wonder = wonder;
    }

    public int getDefeatToken() {
        return defeatToken;
    }

    public void addDefeatToken(int n) {
        this.defeatToken += n;
    }

    public Strategy getStrategy() {
        return strategy;
    }

    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }

    public Card getDumpCard() { return dumpCard; }

    public void setDumpCard(Card dumpCard) { this.dumpCard = dumpCard; }

    public ArrayList<Effect> getWonderEffectNotApply() { return wonderEffectNotApply; }

    public void play() {
        this.setDumpCard(null);
        Action action = this.strategy.chooseAction(this);
        this.chosenCard = action.getCard();

        switch (action.getAction()) {
            case Action.BUILD:
                this.buildCard();
                break;
            case Action.WONDER:
                this.buildStageWonder();
                break;
            default:
                this.dumpCard();
                break;
        }
    }

    /**
     * Remove the chosen card of this hand and give 3 coins to the player.
     */
    public void dumpCard() {
        this.hand.remove(this.chosenCard);
        this.setDumpCard(this.chosenCard);
        Writer.write(this.name + " has obtained 3 coins for tossing");
        this.setCoins(this.getCoins() + 3);
    }

    /**
     * buildCard() ajoute la carte à l'inventaire builtCards, ainsi que les points et ressources SEULEMENT
     * si il à assez de ressource, sinon elle renvoie faux.
     * Seul le cout en or est supprimer.
     */
    public boolean buildCard() {
        //player can't build twice the same card
        if (alreadyBuilt(getChosenCard())) return false;

        boolean enoughResources = true;
        boolean haveFreeCardEffect = false;
        for(Effect e : this.wonderEffectNotApply){
            if (e instanceof FreeCardPerAgeEffect) {
                haveFreeCardEffect = true;
                break;
            }
        }

        // Here, the resourceChoiceEffects are applied, in order to smartly choose the resources every card should produce in order to build currentCard
        EnumMap<Resource, Integer> costAfterEffects = this.chosenCard.getCost();

        for (Card card : this.builtCards) {
            if (card.getEffect() != null) {
                if (card.getEffect() instanceof ResourceChoiceEffect) {
                    card.getEffect().applyEffect(null, null, null, costAfterEffects, null);
                }
                if (card.getEffect() instanceof ColoredCardResourceEffect) {
                    card.getEffect().applyEffect(this, card.getColor(), null, null, null);
                }
                if (card.getEffect() instanceof ShipOwnersGuildEffect) { //Ship Owners Guild Effect
                    card.getEffect().applyEffect(this, null, null, null, null);
                }
                if (card.getEffect() instanceof BuildersGuildEffect) { //Builders Guild card Effect
                    card.getEffect().applyEffect(this, null, null, null, null);
                }
                if (card.getEffect() instanceof StrategistsGuildEffect) { //Strategist Guild card Effect
                    card.getEffect().applyEffect(this, null, null, null, null);
                }
                if (card.getEffect() instanceof CoinCardEffect) {
                    card.getEffect().applyEffect(this, card.getCoinCardEffect(), card.getAge(), null, null);
                }
            }
            //The ScienceChoiceEffect is only apply at the end of the game.
        }

        // Applies ResourceChoiceEffects from the wonder
        if (this.wonder.getAppliedEffects() != null) {
            for (Effect effect : this.wonder.getAppliedEffects()) {
                if (effect instanceof ResourceChoiceEffect) {
                    effect.applyEffect(null, null, null, costAfterEffects, null);
                }
            }
        }

        // Here the player will try to buy resources from its neighbors if he doesn't have enough in order to buildCurrentCard
        for (Map.Entry<Resource, Integer> resource : costAfterEffects.entrySet()) {
            if (resource.getValue() > this.resources.get(resource.getKey())) {

                int missingResources = resource.getValue() - this.resources.get(resource.getKey()) - this.boughtResources.get(resource.getKey());
                this.buyResource(resource.getKey(), missingResources, this.prevNeighbor);

                missingResources -= this.boughtResources.get(resource.getKey());
                if (missingResources > 0 && !this.buyResource(resource.getKey(), missingResources, this.nextNeighbor)) {
                    break;
                }
            }
        }

        for (Map.Entry<Resource, Integer> resource : costAfterEffects.entrySet()) {
            if (resource.getValue() > this.resources.get(resource.getKey()) + this.boughtResources.get(resource.getKey())) {
                enoughResources = false;
            }
        }

        // Checks if player has a built a card that would allow him to build this card for free
        for (Card card : this.builtCards) {
            if (card.getFreeCards() != null && card.getFreeCards().contains(this.chosenCard.getName())) {
                this.builtCards.add(this.chosenCard);
                addPointsAndResources();
                this.hand.remove(this.chosenCard);
                return true;
            }
        }

        if (chosenCard.isFree()) {
            this.builtCards.add(this.chosenCard);
            addPointsAndResources();
            this.hand.remove(this.chosenCard);
            return true;

            //Once per age,a player can construct a building from his or her hand for free (if he built the stage2 of olympiaA)
        } else if (haveFreeCardEffect && !this.freeCardPerAge.get(this.hand.get(0).getAge())) {
                Effect effect;
                for (int i = 0; i < this.wonder.getEffects().size(); i++) {
                    effect = this.wonder.getEffects().get(i);
                    if (effect instanceof FreeCardPerAgeEffect) {
                        effect.applyEffect(this, null, null, null, null);
                        //adding the effect to the appliedEffects
                        this.wonder.getAppliedEffects().add(effect);
                        //saying that the effect is used for the current age
                        this.freeCardPerAge.put(this.hand.get(0).getAge(), true);
                    }
                }
                return true;

        } else{ //not a free card sow check if the player have enough resources to build the card
            if (enoughResources) {
                this.builtCards.add(this.chosenCard);
                addPointsAndResources();
                this.clearBoughtResources();

                //removing the cost of a card if it's not a free card
                this.resources.put(Resource.COIN, this.resources.get(Resource.COIN) - this.chosenCard.getCost().get(Resource.COIN));

                this.hand.remove(this.chosenCard);
                return enoughResources;
            } else { //if the player don't have enough resources to buy the card he toss it
                Writer.write("Not enough ressources");
                return false;
            }
        }

    }

    /**
     * Buys a resource from a neighbor in case the player doesn't have enough resources to build a card.
     *
     * @param resourceToBuy Resource the player wishes to buy
     * @param neighbor      Neighbor to buy the resource from
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
                }
                if (effect.isNextPlayerAllowed()) {
                    for (Resource r : effect.getResourcesModified()) {
                        tradeResourceModifier.get("next").add(r);
                    }
                }
            }
        }
        //check if neighbor has requested resources in stock
        for (Resource r : neighbor.getResources().keySet()) {
            if (resourceToBuy.equals(r) && neighbor.getResources().get(r) >= quantity) {

                this.boughtResources.put(r, this.boughtResources.get(r) + quantity);

                if (((this.prevNeighbor.equals(neighbor) && tradeResourceModifier.get("prev").contains(r)) ||
                        (this.nextNeighbor.equals(neighbor) && tradeResourceModifier.get("next").contains(r))) &&
                        this.getCoins() >= 1) {
                    neighbor.setCoins(neighbor.getCoins() + quantity);
                    this.setCoins(this.getCoins() - quantity);

                    Writer.write(this + " buys " + quantity + " " + r + " from " + neighbor + " for " + quantity + " coin(s).");

                    return true;
                } else if (this.getCoins() >= 2 * quantity) {
                    neighbor.setCoins(neighbor.getCoins() + 2 * quantity);
                    this.setCoins(this.getCoins() - 2 * quantity);

                    Writer.write(this + " buys " + quantity + " " + r + " from " + neighbor + " for " + 2 * quantity + " coin(s).");

                    return true;
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

    /**
     * Buy a stage of a wonder, give reward, and remove one card.
     */
    public boolean buildStageWonder() {
        boolean enoughResources = true;
        if (wonder.isWonderFinished()) return false;
        EnumMap<Resource, Integer> costAfterEffects = this.wonder.getCurrentUpgradeCost();

        for (Card card : this.builtCards) {
            if (card.getEffect() instanceof ResourceChoiceEffect) {
                card.getEffect().applyEffect(null, null, null, costAfterEffects, null);
            }
        }

        // Applies ResourceChoiceEffects from the wonder
        if (this.wonder.getAppliedEffects() != null) {
            for (Effect effect : this.wonder.getAppliedEffects()) {
                if (effect instanceof ResourceChoiceEffect) {
                    effect.applyEffect(null, null, null, costAfterEffects, null);
                }
            }
        }

        // Here the player will try to buy resources from its neighbors if he doesn't have enough in order to buildCurrentCard
        for (Map.Entry<Resource, Integer> resource : costAfterEffects.entrySet()) {
            if (resource.getValue() > this.resources.get(resource.getKey())) {

                int missingResources = resource.getValue() - this.resources.get(resource.getKey()) - this.boughtResources.get(resource.getKey());
                this.buyResource(resource.getKey(), missingResources, this.prevNeighbor);

                missingResources -= this.boughtResources.get(resource.getKey());
                if (missingResources > 0 && !this.buyResource(resource.getKey(), missingResources, this.nextNeighbor)) {
                    break;
                }
            }
        }

        for (Map.Entry<Resource, Integer> resource : costAfterEffects.entrySet()) {
            if (resource.getValue() > this.resources.get(resource.getKey()) + this.boughtResources.get(resource.getKey())) {
                enoughResources = false;
            }
        }

        if (enoughResources) { //enoughResources so the player construct the stage of the wonder
            int coinCost =  0;
            if(this.wonder.getCurrentUpgradeCost().containsKey(Resource.COIN)) {
                coinCost = this.wonder.getCurrentUpgradeCost().get(Resource.COIN); }
            this.addWonderReward();
            this.wonder.setState(wonder.getState() + 1);
            //removing the cost in coin of the wonder
            this.resources.put(Resource.COIN, this.resources.get(Resource.COIN) - coinCost);
            Writer.write(this.name + " builds a stage of wonder.");
            this.hand.remove(this.chosenCard);
            this.clearBoughtResources();

            //adding the stage Effect(if any) to the appliedEffects
            if (this.wonder.getEffects().get(this.wonder.getState()) != null){
                this.wonder.getAppliedEffects().add(this.wonder.getEffects().get(this.wonder.getState())) ;
                this.wonderEffectNotApply.add(this.wonder.getEffects().get(this.wonder.getState()));
            }

            return true;
        } else { //if the player doesn't have enough resources to buy a stage re-choose action
            Writer.write(this.name + " tries to build stage of wonder, but he doesn't have enough resources.");
            return false;
        }
    }

    public void addPointsAndResources() {
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
        for (Resource resource : this.chosenCard.getResource().keySet()) {
            int currentResource = this.resources.get(resource);
            int cardResource = this.chosenCard.getResource().get(resource);
            this.resources.put(resource, currentResource + cardResource);
            if (cardResource != 0) {
                Writer.write(this.name + " played the card " + this.chosenCard.getName() + " and got " + cardResource + " " + resource);
            }
        }

        Writer.write(this.name + " played the card " + this.chosenCard.getName() + " and got " + cardVP + " victory points, " + cardMP + " military points, and " + (cardSCP + cardSTP + cardSWP) + " science points.");
    }

    /**
     * adds military points, and victory points to the player equal to the construction of the current stage of the wonder
     */
    public void addWonderReward() {
        EnumMap<CardPoints, Integer> reward = this.wonder.getCurrentRewardsFromUpgrade();
        int currentVP = 0;
        int wonderVP = 0;
        int currentMP = 0;
        int wonderMP = 0;
        int currentC = 0;
        int wonderC = 0;
        if (reward.containsKey(CardPoints.VICTORY)) {
            currentVP = this.points.get(CardPoints.VICTORY);
            wonderVP = reward.get(CardPoints.VICTORY);
        }
        this.points.put(CardPoints.VICTORY, currentVP + wonderVP);
        if (reward.containsKey(CardPoints.MILITARY)) {
            currentMP = this.points.get(CardPoints.MILITARY);
            wonderMP = this.wonder.getCurrentRewardsFromUpgrade().get(CardPoints.MILITARY);
        }
        this.points.put(CardPoints.MILITARY, currentMP + wonderMP);
        if (reward.containsKey(CardPoints.RELAY_COIN)) {
            currentC = this.getCoins(); //from Ressource.COIN
            wonderC = reward.get(CardPoints.RELAY_COIN);
        }
        this.setCoins(currentC + wonderC);

        Writer.write(this.name + " build stage wonder " + this.wonder.getName() + " and got " + wonderVP + " victory points, " + wonderMP + " military points, " + wonderC + " coin.");
    }

    public int computeScore() {
        int res = 0;
        endApplyEffect();
        // Military points
        res += this.getMilitaryPoints();
        // Treasury Contents
        res += this.getCoins() / 3;
        // Civilian Structures and Wonders
        res += this.getPoints().get(CardPoints.VICTORY);
        res += getSciencePoint();

        return res;
    }

    public int getSciencePoint() {
        int res = 0;
        // Science score
        res += this.getPoints().get(CardPoints.SCIENCE_WHEEL) * this.getPoints().get(CardPoints.SCIENCE_WHEEL);
        res += this.getPoints().get(CardPoints.SCIENCE_COMPASS) * this.getPoints().get(CardPoints.SCIENCE_COMPASS);
        res += this.getPoints().get(CardPoints.SCIENCE_TABLET) * this.getPoints().get(CardPoints.SCIENCE_TABLET);
        // Sets of different symbols
        res += 7 * Math.min(Math.min(this.getPoints().get(CardPoints.SCIENCE_TABLET), this.getPoints().get(CardPoints.SCIENCE_WHEEL)), Math.min(this.getPoints().get(CardPoints.SCIENCE_WHEEL), this.getPoints().get(CardPoints.SCIENCE_COMPASS)));
        return res;
    }

    @Override
    public String toString() {
        return this.name;
    }

    /**
     * Checks if card is buildable given the cards that are already built, and vuyable resources from neighbors.
     *
     * @param card card to check if it's buildable
     * @return true if card is buildable, false otherwise
     */
    protected boolean isBuildable(Card card) {
        EnumMap<Resource, Integer> cardCost = card.getCost();

        for (Card c : this.builtCards) {
            if (c.getEffect() instanceof ResourceChoiceEffect) {
                c.getEffect().applyEffect(null, null, null, cardCost, null);
            }
        }

        for (Resource r : Resource.values()) {
            if (cardCost.get(r) > this.resources.get(r) + this.prevNeighbor.getResources().get(r) + this.nextNeighbor.getResources().get(r)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Apply the effect to the player if he have it.
     */
    protected void endApplyEffect() {
        boolean wonderScienceEffect = false;
        boolean cardScienceEffect = false;
        for(Card c : this.getBuiltCards()){
            if(c.getEffect() instanceof ScienceChoiceEffect){
                cardScienceEffect = true;
                for(Effect e : this.wonderEffectNotApply){
                    if(e instanceof ScienceChoiceEffect){
                        wonderScienceEffect = true;
                        ((ScienceChoiceEffect) e).applyCumulativeEffect(this);
                        break;
                    }
                }
                if(!wonderScienceEffect){ //we are still in the condition when c is a ScienceChoiceEffect
                    c.getEffect().applyEffect(this, null, null, null, null);
                }
                break;
            }
        }
        if(!cardScienceEffect){
            for(Effect e : this.wonderEffectNotApply){
                if(e instanceof ScienceChoiceEffect){
                    e.applyEffect(this, null, null, null, null);
                    break;
                }
            }
        }
    }

    /**
     * This method is used in order to choose a specific strategy depending on the wonder.
     * In most cases, this method chooses a random strategy because few wonders are helpful in regards of strategy.
     */
    public void chooseStrategy() {
        if (this.wonder.getName().equals("rhodesA") || this.wonder.getName().equals("rhodesB")) {
            this.strategy = new MilitaryStrategy();
            this.name += " (Military)";
        } else if (this.wonder.getName().equals("babylonA")) {
            this.strategy = new ScienceStrategy();
            this.name += " (Science)";
        } else {
            int randomInt = this.rand.nextInt(100);
            if (randomInt < 33) {
                this.strategy = new DumbStrategy();
                this.name += " (Dumb)";
            } else if (randomInt < 66) {
                this.strategy = new MilitaryStrategy();
                this.name += " (Military)";
            } else {
                this.strategy = new ScienceStrategy();
                this.name += " (Science)";
            }
        }
    }

    protected boolean alreadyBuilt (Card c){
        for(Card card : this.getBuiltCards()){
            if(card.getName().equals(c.getName())) { return true; }
        }
        return false;
    }

}
