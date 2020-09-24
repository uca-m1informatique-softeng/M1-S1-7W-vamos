package Core;

import java.util.EnumMap;

public class Card {

    private String name;
    private CardColor color;
    private int age;
    private int players;
    private EnumMap<CardPoints, Integer> cardPoints;
    private EnumMap<Resource, Integer> resource;
    private EnumMap<Resource, Integer> cost;

    public String getName() {
        return name;
    }

    public CardColor getColor() {
        return color;
    }

    public int getAge() {
        return age;
    }

    public int getPlayers() {
        return players;
    }

    public EnumMap<CardPoints, Integer> getCardPoints() {
        return cardPoints;
    }

    public EnumMap<Resource, Integer> getResource() {
        return resource;
    }

    public EnumMap<Resource, Integer> getCost() {
        return cost;
    }
}
