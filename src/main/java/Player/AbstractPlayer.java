package Player;

import Card.Card;
import Core.Wonder;

import java.util.ArrayList;
import java.util.EnumMap;
import Card.CardPoints;
import Card.Resource;

public abstract class AbstractPlayer {

    private String name;
    private int militaryPoints;
    private EnumMap<CardPoints, Integer> points;
    private EnumMap<Resource, Integer> resources;
    private EnumMap<Resource, Integer> boughtResources;
    private ArrayList<Card> hand;
    private ArrayList<Card> builtCards;
    private Wonder wonder;
    private Player prevNeighbor;
    private Player nextNeighbor;

    abstract String getName();

    abstract int getCoins();
    abstract void setCoins(int coins);

    abstract ArrayList<Card> getHand();
    abstract void setHand(ArrayList<Card> hand);

    abstract Card getChosenCard();
    abstract void setChosenCard(Card chosenCard);

    abstract EnumMap<CardPoints, Integer> getPoints();

    abstract ArrayList<Card> getBuiltCards();

    abstract int getMilitaryPoints();
    abstract void addMilitaryPoints(int mp);

    abstract EnumMap<Resource, Integer> getResources();
    abstract EnumMap<Resource, Integer> getBoughtResources();

    abstract Player getPrevNeighbor();
    abstract Player getNextNeighbor();

    abstract void setPrevNeighbor(Player prevNeighbor);
    abstract void setNextNeighbor(Player nextNeighbor);

    abstract void chooseAction();
    abstract void dumpCard();
    abstract  void buildCard();
    abstract boolean buyResource(Resource resourceToBuy, int quantity, Player neighbor);
    abstract void clearBoughtResources();
    abstract void buildStageWonder();

    abstract void addPointsAndResources();
    abstract void addWonderReward();
    abstract  int computeScore();
    abstract int getSciencePoint();

    abstract Wonder getWonder();
    abstract void setWonder(Wonder wonder);

}
