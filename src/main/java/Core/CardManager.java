package Core;
import java.io.File;
import java.io.IOException;

public class CardManager {
    public static void main(String[] args) throws IOException {
        File folder = new File("src/assets/cards/blue");
        File[] listOfFiles = folder.listFiles();
        String fileName = "";
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile() && !listOfFiles[i].getName().contains(".DS_Store")) {
                System.out.println("File " + listOfFiles[i].getName());
                fileName = listOfFiles[i].getName();
            } else if (listOfFiles[i].isDirectory()) {
                System.out.println("Directory " + listOfFiles[i].getName());
                fileName = listOfFiles[i].getName();
            }
            if(fileName != "") {
                Card card = new Card(fileName.replace(".json",""), 3, CardColor.BLUE);
            }
        }

    }

}
