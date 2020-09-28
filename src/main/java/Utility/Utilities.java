package Utility;

import Exceptions.ParserException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Utilities {

    public static void displayGameOutput(){
        Scanner sc;
        try
        {
            sc = new Scanner(new File(Writer.NOM_FICHIER));
            while (sc.hasNextLine())
            {
                System.out.println(sc.nextLine());
            }

            sc.close();
        } catch (FileNotFoundException e)
        {
            throw new ParserException("Game state couldn't correctly be displayed - File not found");
        }
    }
}
