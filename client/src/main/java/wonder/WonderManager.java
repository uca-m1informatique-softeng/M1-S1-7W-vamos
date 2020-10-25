package wonder;

import utility.Writer;

import java.io.*;
import java.util.ArrayList;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * This class will handle wonders loading client side when the game starts.
 * and maybe some other stuff we'll see.
 */
public class WonderManager {

    private WonderManager() {}

    public static ArrayList<Wonder> parseWonders() throws IOException {

        ArrayList<Wonder> wondersList = new ArrayList<>();

        InputStream is = WonderManager.class.getClassLoader().getResourceAsStream("wonders");
        if (is == null) throw new IOException();
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
            Writer.write("Couldn't read wonders directory !");
        }

        return wondersList;
    }
}
