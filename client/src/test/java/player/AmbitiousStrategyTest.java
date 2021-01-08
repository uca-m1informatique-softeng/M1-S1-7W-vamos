package player;

import card.Card;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import wonder.Wonder;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class AmbitiousStrategyTest {

    AmbitiousStrategy ambi;
    @Mock
    Player mockedPlayer;
    @Mock
    Wonder wonder;
    @Mock
    Card mockedC1;
    @Mock
    Card mockedC2;

    @Test
    void chooseAction() {
    }

    @Test
    void availableActions() {
        //Init hand and card mock object
        ArrayList<Card> test_hand = new ArrayList<>(2);
        test_hand.add(mockedC1);
        test_hand.add(mockedC2);
        doReturn(test_hand).when(mockedPlayer).getHand();
        doReturn(wonder).when(mockedPlayer).getWonder();
        ambi = new AmbitiousStrategy(mockedPlayer);
        //No card can be build, or build a stge of wonder
        doReturn(false).when(mockedPlayer).isBuildable(mockedC1);
        doReturn(false).when(mockedPlayer).isBuildable(mockedC2);
        doReturn(false).when(wonder).canUpgrade(Mockito.any());

        ArrayList<Action> res = ambi.availableActions();
        assertEquals(2, res.size()); //The cards can be dump
        assertTrue(res.get(0).getCard() == mockedC1 && res.get(0).getAction() == 3);
        assertTrue(res.get(1).getCard() == mockedC2 && res.get(1).getAction() == 3);

        //C2 can be build
        doReturn(true).when(mockedPlayer).isBuildable(mockedC2);
        res = ambi.availableActions();
        assertEquals(3, res.size()); //The card can be dump and one be build
        assertTrue(res.get(0).getCard() == mockedC1 && res.get(0).getAction() == 3);
        assertTrue(res.get(1).getCard() == mockedC2 && res.get(1).getAction() == 1);
        assertTrue(res.get(2).getCard() == mockedC2 && res.get(2).getAction() == 3);

        //Wonder Stage and C2 can be build
        doReturn(true).when(wonder).canUpgrade(Mockito.any());
        res = ambi.availableActions();
        assertEquals(5, res.size()); //The card can be dump and one be build, and 2 for build a stage
        assertTrue(res.get(0).getCard() == mockedC1 && res.get(0).getAction() == 2);
        assertTrue(res.get(1).getCard() == mockedC1 && res.get(1).getAction() == 3);
        assertTrue(res.get(2).getCard() == mockedC2 && res.get(2).getAction() == 1);
        assertTrue(res.get(3).getCard() == mockedC2 && res.get(3).getAction() == 2);
        assertTrue(res.get(4).getCard() == mockedC2 && res.get(4).getAction() == 3);

        //All possible
        doReturn(true).when(mockedPlayer).isBuildable(mockedC1);
        res = ambi.availableActions();
        assertEquals(6, res.size()); //3 actions * 2 cards
    }

    @Test
    void simulateGame() {
    }

    @Test
    void simulProcessTurn() {
    }
}