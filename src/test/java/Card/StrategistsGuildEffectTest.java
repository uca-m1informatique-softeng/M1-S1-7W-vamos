package Card;

import Player.DumbPlayer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class StrategistsGuildEffectTest {
    DumbPlayer dumbPlayer ;
    DumbPlayer dumbPlayer2 ;
    DumbPlayer dumbPlayer3 ;

    @BeforeEach
    void setUp(){
        dumbPlayer = new DumbPlayer("Marc") ;
        dumbPlayer2 = new DumbPlayer("Marc2") ;
        dumbPlayer3 = new DumbPlayer("Marc3") ;
    }

    @Test
    public void applyEffect(){
        dumbPlayer2.addDefeatToken(2);
        dumbPlayer3.addDefeatToken(10);

        dumbPlayer.getPoints().put(CardPoints.VICTORY , dumbPlayer2.getDefeatToken() + dumbPlayer3.getDefeatToken()) ;

        assertEquals(dumbPlayer.getPoints().get(CardPoints.VICTORY) , 12);
    }
}
