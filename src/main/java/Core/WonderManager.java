package Core;

import Card.Card;
import Exceptions.WondersException;
import Utility.Writer;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;

/**
 *   This class will handle wonders loading client side when the game starts.
 *   and maybe some other stuff we'll see.
 *
 */
public class WonderManager {

    static ArrayList<Wonder> parseWonders() throws WondersException {

        ArrayList<Wonder> wondersList = new ArrayList<>();

        File folder = new File("src/assets/wonders");

        File[] listOfFiles = folder.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                String name = pathname.getName().toLowerCase();
                return name.endsWith(".json") && pathname.isFile();
            }
        });
        if(listOfFiles.length < Game.getPlayers()) {
            throw new WondersException("There aren't enough wonders for each player please lower the number of players and restart the game. exiting..");
        }
        String fileName = "";

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile() && !listOfFiles[i].getName().contains(".DS_Store")) {
                fileName = listOfFiles[i].getName();
            } else if (listOfFiles[i].isDirectory()) {
                fileName = listOfFiles[i].getName();
            }
            if(!fileName.equals("")) {
                try {
                    Wonder wonder = new Wonder(fileName.replace(".json",""));
                    wondersList.add(wonder);
                    if(Game.debug){
                        Writer.write(wonder.getName() + "has been created");

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return wondersList;

    }


}
