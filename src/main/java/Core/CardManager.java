package Core;
import Utility.Writer;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class CardManager {

    public static void main(String[] args) {

        ArrayList<Card> cards = CardManager.getAgeNDeck(1);

    }

    private static ArrayList<Card> parseCardFiles(int players) {

        ArrayList<Card> cards = new ArrayList<>();

        File folder = new File("src/assets/cards");

        File[] listOfFiles = folder.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                String name = pathname.getName().toLowerCase();
                return name.endsWith(".json") && pathname.isFile();
            }
            });

        String fileName = "";

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile() && !listOfFiles[i].getName().contains(".DS_Store")) {
                fileName = listOfFiles[i].getName();
            } else if (listOfFiles[i].isDirectory()) {
                fileName = listOfFiles[i].getName();
            }
            if(!fileName.equals("")) {
                // number of players is just a test
                try {
                    Card card = new Card(fileName.replace(".json",""), players);
                    cards.add(card);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

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
