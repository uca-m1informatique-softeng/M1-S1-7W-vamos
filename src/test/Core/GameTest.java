package Core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class GameTest {

    Game g;
    @BeforeEach
    void setUp() {
        g = new Game(2);
    }

    @Test
    void structureTest() {
        assertEquals(1, g.getRound());
        assertEquals(2, g.getPlayers());
        assertEquals(1, g.getCurrentAge());
        assertEquals(GameState.START, g.getState());
    }

    @Test
    void initPlayersTest() {
        ArrayList<Player> tmp = g.getPlayersArray();
        for (int i = 1; i < g.getPlayers(); i++) {
            assertNotEquals(tmp.get(0), tmp.get(i));
        }
    }

    @Test
    void processTest() {
        System.out.print(g.getState());
    }
}