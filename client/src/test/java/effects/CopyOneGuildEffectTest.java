package effects;

import card.Card;
import org.junit.jupiter.api.BeforeEach;
import player.*;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

public class CopyOneGuildEffectTest {
    Player player ;
    Player player2 ;
    Player player3 ;

    @BeforeEach
    void setUp(){
        player = new Player("Player1");
        player2 = new Player("Player2");
        player3 = new Player("Player3");

        player.setNextNeighbor(player3);
        player.setPrevNeighbor(player2);
    }

    @Test
    public void applyEffect(){
        ArrayList<Card> builtCards = new ArrayList<>();
        Player neighbor1 = player.getNextNeighbor();
        Player neighbor2 = player.getPrevNeighbor();

        assertEquals(player.getNextNeighbor() , neighbor1);
        assertEquals(player.getPrevNeighbor() , neighbor2);

        builtCards.addAll(neighbor1.getBuiltCards());
        assertTrue(builtCards.equals(neighbor1.getBuiltCards()));

        builtCards.addAll(neighbor2.getBuiltCards());
        assertTrue(builtCards.equals(neighbor2.getBuiltCards()));
    }
}
