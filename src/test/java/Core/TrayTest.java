package Core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class TrayTest {

    private ArrayList<Card> builtCards;
    private Tray tray;

    @BeforeEach
    void setUp(){
        builtCards=new ArrayList<>();
        tray=new Tray();
    }

    @Test
    public void getBuiltCards(){
        //test with empty builtCards
        assertEquals(tray.getBuiltCards(),builtCards);

        try {
            Card card=new Card("altar",3);
            tray.getBuiltCards().add(card);
            builtCards.add(card);
        }
        catch (IOException e){
            e.printStackTrace();
        }
        //test with one card in builtCards
        assertEquals(tray.getBuiltCards(),builtCards);
    }

    @Test
    public void add(){
        try {
            Card card=new Card("altar",3);

            tray.add(card);

            builtCards.add(card);
        }
        catch (IOException e){
            e.printStackTrace();
        }
        assertEquals(tray.getBuiltCards(),builtCards);
    }

}
