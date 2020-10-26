package effects;

import card.CardPoints;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import wonder.Wonder;
import player.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.EnumMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BuildersGuildEffectTest {
    BuildersGuildEffect e;

    @Mock
    Player dumbPlayer;
    @Mock
    Player dumbPlayer2;
    @Mock
    Player dumbPlayer3;

    @Mock
    Wonder wonder;
    @Mock
    Wonder wonder2;
    @Mock
    Wonder wonder3;


    @BeforeEach
    void setUp(){
        e = new BuildersGuildEffect();
    }

    @Test
     void testapplyEffect(){
        //Simulate the neighbor
        doReturn(dumbPlayer2).when(dumbPlayer).getPrevNeighbor();
        doReturn(dumbPlayer3).when(dumbPlayer).getNextNeighbor();
        //Simulate the wonders
        doReturn(wonder).when(dumbPlayer).getWonder();
        doReturn(wonder2).when(dumbPlayer2).getWonder();
        doReturn(wonder3).when(dumbPlayer3).getWonder();

        //Simulate the state wonders
        doReturn(3).when(wonder).getState();
        doReturn(2).when(wonder2).getState();
        doReturn(1).when(wonder3).getState();
        //So the effect will sum number of state as victory points.

        EnumMap<CardPoints, Integer> points = new EnumMap<>(CardPoints.class);
        for (CardPoints p : CardPoints.values()) {
            points.put(p, 0);
        }
        doReturn(points).when(dumbPlayer).getPoints();

        //Add 6 vp, the number of step of wonder build in the player and neighbor city
        e.applyEffect(dumbPlayer, null, null, null, null);
        assertEquals(6, dumbPlayer.getPoints().get(CardPoints.VICTORY) );
        //Verify he add the points to the previous value.
        e.applyEffect(dumbPlayer, null, null, null, null);
        assertEquals(12, dumbPlayer.getPoints().get(CardPoints.VICTORY) );

    }

}
