package Utility;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.Scanner;

public class Utilities {

    public static void displayGameOutput() throws ParseException {
        Scanner sc;
        try
        {
            sc = new Scanner(new File(Writer.NOM_FICHIER));
            if(!sc.hasNextLine()) System.out.println("empty scanner");
            while (sc.hasNextLine())
            {
                System.out.println(sc.nextLine());
            }

            sc.close();
        } catch (FileNotFoundException e)
        {
            throw new ParseException("Erreur lors de l'affichage du déroulement du jeu.",0);
        }
    }
}
