package Card;

import Wonder.Wonder;
import Player.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class BuildersGuildEffectTest {
    Player dumbPlayer;
    Player dumbPlayer2;
    Player dumbPlayer3;

    @BeforeEach
    void setUp(){
        dumbPlayer = new Player("marc") ;
        dumbPlayer.setStrategy(new DumbStrategy());
        dumbPlayer2 = new Player("marc2") ;
        dumbPlayer3 = new Player("marc3") ;
    }

    @Test
    public void applyEffect(){
        try{
            Wonder wonder = new Wonder("gizah") ;
            Wonder wonder2 = new Wonder("alexandria") ;
            Wonder wonder3 = new Wonder("babylon") ;
            dumbPlayer.setWonder(wonder);
            dumbPlayer2.setWonder(wonder2);
            dumbPlayer3.setWonder(wonder3);

            wonder.setState(3); //third stage so it gives 3 victory points
            wonder2.setState(2);
            wonder3.setState(1);
        }
        catch (IOException e){
            e.printStackTrace();
        }

        dumbPlayer.setPrevNeighbor(dumbPlayer2);
        dumbPlayer.setNextNeighbor(dumbPlayer3);

        int playerWonderState = dumbPlayer.getWonder().getState() ; //player wonder state
        int previousPlayerWonderState = dumbPlayer.getPrevNeighbor().getWonder().getState() ; //previous player wonder stage
        int nextPlayerWonderState = dumbPlayer.getNextNeighbor().getWonder().getState() ; //next player wonder stage

        int victoryPointsTotal = playerWonderState + previousPlayerWonderState + nextPlayerWonderState ;
        dumbPlayer.getPoints().put(CardPoints.VICTORY , victoryPointsTotal) ;

        assertEquals(dumbPlayer.getPoints().get(CardPoints.VICTORY) , victoryPointsTotal);

    }

}
