package Card;

import java.io.*;
import Utility.Writer;
import java.util.ArrayList;
import java.util.Collections;
import static java.nio.charset.StandardCharsets.UTF_8;

public class CardManager {

    private static ArrayList<Card> parseCardFiles(int players) throws IOException {

        ArrayList<Card> cards = new ArrayList<>();

        InputStream is = Card.class.getClassLoader().getResourceAsStream("cards");
        if (is == null) throw new IOException("Cards directory could not be read !");
        InputStreamReader isr = new InputStreamReader(is, UTF_8);
        BufferedReader br = new BufferedReader(isr);

        ArrayList<String> files = new ArrayList<>();
        try {
            while (br.ready()) {
                files.add(br.readLine());
            }
            for (String fileName : files) {
                cards.add(new Card(fileName.replace(".json", ""), players));
            }
        } catch (IOException e) {
            Writer.write("json file could not be read !");
        }

        return cards;

    }

    public static ArrayList<Card> getAgeNDeck(int age) throws IOException {

        // Maximum amount of players since number of players isn't implemented yet
        ArrayList<Card> cards = CardManager.parseCardFiles(7);
        ArrayList<Card> res = new ArrayList<>();

        for (Card c : cards) {
            if (c.getAge() == age) {
                res.add(c);
            }
        }

        Collections.shuffle(res);
        return res;
    }

}
