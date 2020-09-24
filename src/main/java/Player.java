import java.util.ArrayList;

public class Player {

    private ArrayList<Carte> hand = new ArrayList<>(7);

    private String name;

    public Player(String name)
    {
        this.name = name;
        System.out.println("Player " + name +  " joined the game!");
    }


    public void initPlayerHand(){
        for(int i = 0; i < 7 ; i++)
            hand.add(null);
    }

}
