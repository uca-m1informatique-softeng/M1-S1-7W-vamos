package Wonder;

import Card.Card;
import Core.Game;
import Exceptions.WondersException;
import Utility.Writer;

import java.io.*;
import java.util.ArrayList;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * This class will handle wonders loading client side when the game starts.
 * and maybe some other stuff we'll see.
 */
public class WonderManager {

    public static ArrayList<Wonder> parseWonders() throws WondersException {

        ArrayList<Wonder> wondersList = new ArrayList<>();

        InputStream is = WonderManager.class.getClassLoader().getResourceAsStream("wonders");
        InputStreamReader isr = new InputStreamReader(is, UTF_8);
        BufferedReader br = new BufferedReader(isr);

        ArrayList<String> files = new ArrayList<>();
        try {
            while (br.ready()) {
                files.add(br.readLine());
            }
            for (String fileName : files) {
                wondersList.add(new Wonder(fileName.replace(".json", "")));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        /*
        File folder = new File("resources/wonders");

        File[] listOfFiles = folder.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                String name = pathname.getName().toLowerCase();
                return name.endsWith(".json") && pathname.isFile();
            }
        });
        ArrayList<String> wonderNames = new ArrayList<String>();
        for (File file : listOfFiles) {
            //check whether the wonder either in form A or B already exists in fileNames
            String wonderName = (file.getName().replace(".json", "")).substring(0, (file.getName().replace(".json", "")).length() - 1);
            if (!wonderNames.contains(wonderName)) {
                wonderNames.add(wonderName);
            }
        }
        if (wonderNames.size() < Game.getPlayers()) {
            throw new WondersException("There aren't enough wonders for each player please lower the number of players and restart the game. exiting..");
        }
        String fileName = "";

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile() && !listOfFiles[i].getName().contains(".DS_Store")) {
                fileName = listOfFiles[i].getName();
            } else if (listOfFiles[i].isDirectory()) {
                fileName = listOfFiles[i].getName();
            }
            if (!fileName.equals("") &&
                    //FOR TESTING
                    (!fileName.contains("alexandria")&&!fileName.contains("gizah")
                            &&!fileName.contains("ephesos") && !fileName.contains("halikarnassos"))) {
                try {
                    Wonder wonder = new Wonder(fileName.replace(".json", ""));
                    wondersList.add(wonder);
                    if (Game.debug) {
                        Writer.write(wonder.getName() + "has been created");
                    }
                } catch (Exception e) {
                    Writer.write(fileName + " could not be read.");
                }
            }
        }*/
        return wondersList;
    }
}
