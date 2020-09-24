package Core;

import org.json.*;
import java.util.EnumMap;
import java.nio.file.*;
public class Card {

    private String name;
    private CardColor color;
    private int age;
    private int players;
    private EnumMap<CardPoints, Integer> cardPoints;
    private EnumMap<Resource, Integer> resource;
    private EnumMap<Resource, Integer> cost;

    public static void main(String[] args){
        String content = Files.readString(Paths.get("src/assets/cards/blue/altar.json"));
     JSONObject json = new JSONObject(content);
        System.out.println(json);
//String name = json.getJSONObject(0).getString("name");
  //   System.out.println(name);
    }
}
