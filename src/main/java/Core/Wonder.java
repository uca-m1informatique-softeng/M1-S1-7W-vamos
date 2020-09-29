package Core;

import Card.CardPoints;
import Utility.Utilities;
import Utility.Writer;
import org.json.JSONArray;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

public class Wonder {


    private String name;

    private int state = 1;

    private int maxstate;

    private ArrayList<HashMap<HashMap<Resource,Integer>, HashMap<CardPoints,Integer>>> prop;
    //[0] cost [1] gain

    private Resource productedResource;

    public Wonder(String name) throws IOException {
        String content = Files.readString(Paths.get("src", "assets", "wonders", name + ".json"));
        JSONArray card = new JSONArray(content);
        this.name = name;

        if (card.length() > 0) {
            maxstate = card.length();
            prop = new ArrayList<>(card.length());
            for (int i = 0; i < card.length(); i++) {
                HashMap<Resource, Integer> tmpMap = new HashMap<>();
                HashMap<CardPoints, Integer> tmpMap2 = new HashMap<>();
                if (card.getJSONObject(i).has("cost")) {
                    for (int k = 0; k < card.getJSONObject(i).getJSONObject("cost").names().length(); k++) {

                        String keyStr = card.getJSONObject(i).getJSONObject("cost").names().getString(k);
                        Resource key = Resource.STONE; // Default case
                        int value = card.getJSONObject(i).getJSONObject("cost").getInt(keyStr);

                        tmpMap.put(Utilities.getResourceByString(keyStr), value);
                    }
                }
                if (card.getJSONObject(i).has("reward")) {
                    for (int k = 0; k < card.getJSONObject(i).getJSONObject("reward").names().length(); k++) {

                        String keyStr = card.getJSONObject(i).getJSONObject("reward").names().getString(k);
                        CardPoints key = CardPoints.SCIENCE_WHEEL; // Default case
                        int value = card.getJSONObject(i).getJSONObject("reward").getInt(keyStr);


                        tmpMap2.put(Utilities.getCardPointByString(keyStr),value);
                    }
                }

                // card color
                HashMap<HashMap<Resource, Integer>, HashMap<CardPoints, Integer>> tmpPropMap = new HashMap<>();
                tmpPropMap.put(tmpMap, tmpMap2);

                prop.add(tmpPropMap);
            }
        }
       // Writer.write(name +prop.toString() + maxstate);
    }

}
