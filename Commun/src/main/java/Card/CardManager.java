package Card;

import java.io.*;

import Utility.Writer;

import java.nio.file.DirectoryStream;
import java.util.ArrayList;
import java.util.Collections;

import static java.nio.charset.StandardCharsets.UTF_8;

public class CardManager {

    private static ArrayList<Card> parseCardFiles(int players) {

        ArrayList<Card> cards = new ArrayList<>();

        InputStream is = Card.class.getClassLoader().getResourceAsStream("cards");
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
            e.printStackTrace();
        }

        //File folder = new File("Commun/src/main/assets/cards");
        /*
        File[] listOfFiles = folder.listFiles(pathname -> {
            String name = pathname.getName().toLowerCase();
            return name.endsWith(".json") && pathname.isFile();
        });

        String fileName = "";

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile() && !listOfFiles[i].getName().contains(".DS_Store") || listOfFiles[i].isDirectory()) {
                fileName = listOfFiles[i].getName();
            }
            if(!fileName.equals("")) {
                // number of players is just a test
                try {
                    Card card = new Card(fileName.replace(".json",""), players);
                    cards.add(card);
                } catch (Exception e) {
                    Writer.write(fileName + " could not be read.");
                }
            }
        }*/

        return cards;

    }

    public static ArrayList<Card> getAgeNDeck(int age) {

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
