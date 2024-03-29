package player;

import card.Card;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;

import utility.Writer;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;
import java.security.SecureRandom;

@ExtendWith(MockitoExtension.class)
class DumbStrategyTest {

    Player player;
    Card oracle ;

    @Mock
    SecureRandom rand;

    @BeforeEach
    void setup() {
        player = new Player("Billy Joe Bob");
        player.strategy = new DumbStrategy();
        oracle = new Card("academy", 6);
        player.hand.add(new Card("altar", 6));
        player.hand.add(new Card("claypit", 6));
        player.hand.add(oracle);
        player.hand.add(new Card("baths", 6));
        player.getStrategy().rand = rand ;
    }

    @Test
    void chooseAction() {
        when(rand.nextInt(player.hand.size())) .thenReturn(2) ;
        when(rand.nextInt(3) ) .thenReturn(1) ;

        Action action = player.getStrategy().chooseAction(player);

        assertEquals(Action.WONDER, action.getAction());
        assertEquals(oracle, action.getCard());

    }

}