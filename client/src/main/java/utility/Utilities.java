package utility;

import card.CardPoints;
import card.Resource;
import exceptions.ParserException;
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

    public static Resource getResourceByString(String parser)
    {

        Resource key;
        switch (parser) {
            case "WOOD":
                key = Resource.WOOD;
                break;
            case "ORE":
                key = Resource.ORE;
                break;
            case "CLAY":
                key = Resource.CLAY;
                break;
            case "GLASS":
                key = Resource.GLASS;
                break;
            case "LOOM":
                key = Resource.LOOM;
                break;
            case "PAPYRUS":
                key = Resource.PAPYRUS;
                break;
            default:
                key = Resource.STONE;
                break;
        }
        return key;
    }

    public static CardPoints getCardPointByString(String parser){

        CardPoints key;
        switch (parser){
            case "VICTORY":
                key = CardPoints.VICTORY;
                break;
            case "MILITARY":
                key = CardPoints.MILITARY;
                break;
            case "SCIENCE_WHEEL":
                key = CardPoints.SCIENCE_WHEEL;
                break;
            case "SCIENCE_COMPASS":
                key = CardPoints.SCIENCE_COMPASS;
                break;
            case "COIN":
                key = CardPoints.RELAY_COIN;
                break;
            default:
                key = CardPoints.SCIENCE_TABLET;
                break;


        }
        return key;
    }
}
