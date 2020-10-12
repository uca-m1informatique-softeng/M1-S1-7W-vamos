package Effects;

import Card.CardPoints;
import Player.Player;
import Player.DumbStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class StrategistsGuildEffectTest {
    Player dumbPlayer ;
    Player dumbPlayer2 ;
    Player dumbPlayer3 ;

    @BeforeEach
    void setUp(){
        dumbPlayer = new Player("Marc") ;
        dumbPlayer.setStrategy(new DumbStrategy());
        dumbPlayer2 = new Player("Marc2") ;
        dumbPlayer3 = new Player("Marc3") ;
    }

    @Test
    public void applyEffect(){
        dumbPlayer2.addDefeatToken(2);
        dumbPlayer3.addDefeatToken(10);

        dumbPlayer.getPoints().put(CardPoints.VICTORY , dumbPlayer2.getDefeatToken() + dumbPlayer3.getDefeatToken()) ;

        assertEquals(dumbPlayer.getPoints().get(CardPoints.VICTORY) , 12);
    }
}