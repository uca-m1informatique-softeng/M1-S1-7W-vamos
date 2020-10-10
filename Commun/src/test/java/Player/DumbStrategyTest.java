package Player;

import Card.Card;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;

public class DumbStrategyTest {

    private Player player;

    @BeforeEach
    void setup() {
        player = new Player("Billy Joe Bob");
        player.strategy = new DumbStrategy();
        try {
            player.hand.add(new Card("altar", 6));
            player.hand.add(new Card("claypit", 6));
        } catch (IOException e) {

        }
    }

    @Test
    public void chooseAction() {
        Action action = player.getStrategy().chooseAction(player);
        String cardName = action.getCard().getName();

        assertTrue(cardName == "altar" || cardName == "claypit");
        assertTrue( action.getAction() == Action.BUILD ||
                            action.getAction() == Action.WONDER ||
                            action.getAction() == Action.DUMP);
    }

}
