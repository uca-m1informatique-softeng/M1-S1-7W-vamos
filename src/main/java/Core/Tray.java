package Core;

import java.util.ArrayList;

public class Tray { //Tray = Plateau

    private ArrayList<Card> builtCards;

    public Tray() {
        this.builtCards = new ArrayList<>();;
    }

    public void add(Card c){
        builtCards.add(c);
    }

    public ArrayList<Card> getBuiltCards() {
        return builtCards;
    }

}
