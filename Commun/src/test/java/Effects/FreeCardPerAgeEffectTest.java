package Effects;

import Card.Card;
import Player.Player;
import Player.DumbStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class FreeCardPerAgeEffectTest {

    Player player ;
    FreeCardPerAgeEffect freeCard;

    Card card;
    Card card2;

    @BeforeEach
    void setUp() throws IOException {
        player=new Player("Marc") ;
        player.setStrategy(new DumbStrategy());
        freeCard = new FreeCardPerAgeEffect();
        card = new Card("spiesguild" ,7); //purple card
        card2 = new Card("mine" ,7); //brown card
        ArrayList<Card> tmp = new ArrayList<Card>();
        tmp.add(card);
        tmp.add(card2);
        player.setHand(tmp);
    }

    @Test
    void applyEffect() {
        player.setChosenCard(card);
        //Test if he can build a card with a cost without resource.
        for(Card c : player.getHand()) { assertFalse(c.isFree()); }
        assertTrue(player.getBuiltCards().isEmpty());
        freeCard.applyEffect(player, null, null, null);
        assertEquals(1, player.getBuiltCards().size());

        //Test if the chosen card is duplicate.
        freeCard.applyEffect(player, null, null, null);
        assertNotEquals(player.getBuiltCards().get(0), player.getBuiltCards().get(1));

    }
}