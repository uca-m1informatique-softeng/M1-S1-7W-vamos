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
import java.util.Map;

public class Wonder {


    private String name;

    private int state = 0;

    private int maxstate;

    private ArrayList<HashMap<HashMap<Resource,Integer>, HashMap<CardPoints,Integer>>> prop;

    private Resource productedResource;


    public Wonder(String name) throws IOException {
        String content = Files.readString(Paths.get("src", "assets", "wonders", name + ".json"));
        JSONArray card = new JSONArray(content);

        this.name = name;
        this.productedResource = Utilities.getResourceByString(card.getJSONObject(0).getString("productedRes"));

        if (card.length() > 0) {
            maxstate = card.length();
            prop = new ArrayList<>(card.length());
            for (int i = 0; i < card.length(); i++) {
                HashMap<Resource, Integer> tmpMap = new HashMap<>();
                HashMap<CardPoints, Integer> tmpMap2 = new HashMap<>();
                if (card.getJSONObject(i).has("cost")) {
                    for (int k = 0; k < card.getJSONObject(i).getJSONObject("cost").names().length(); k++) {

                        String keyStr = card.getJSONObject(i).getJSONObject("cost").names().getString(k);
                        int value = card.getJSONObject(i).getJSONObject("cost").getInt(keyStr);

                        tmpMap.put(Utilities.getResourceByString(keyStr), value);
                    }
                }
                if (card.getJSONObject(i).has("reward")) {
                    for (int k = 0; k < card.getJSONObject(i).getJSONObject("reward").names().length(); k++) {

                        String keyStr = card.getJSONObject(i).getJSONObject("reward").names().getString(k);
                        int value = card.getJSONObject(i).getJSONObject("reward").getInt(keyStr);
                        tmpMap2.put(Utilities.getCardPointByString(keyStr),value);
                    }
                }
                HashMap<HashMap<Resource, Integer>, HashMap<CardPoints, Integer>> tmpPropMap = new HashMap<>();
                tmpPropMap.put(tmpMap, tmpMap2);

                prop.add(tmpPropMap);
            }
        }
        if(Game.debug) {

            Writer.write(prop.toString());
            Writer.write(getCurrentUpgradeCost().toString());
            Writer.write(getCurrentRewardsFromUpgrade().toString());
        }

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getMaxstate() {
        return maxstate;
    }

    public void setMaxstate(int maxstate) {
        this.maxstate = maxstate;
    }

    public ArrayList<HashMap<HashMap<Resource, Integer>, HashMap<CardPoints, Integer>>> getProp() {
        return prop;
    }

    public void setProp(ArrayList<HashMap<HashMap<Resource, Integer>, HashMap<CardPoints, Integer>>> prop) {
        this.prop = prop;
    }

    public Resource getProductedResource() {
        return productedResource;
    }

    public void setProductedResource(Resource productedResource) {
        this.productedResource = productedResource;
    }

    public HashMap<Resource, Integer> getCurrentUpgradeCost()
    {
        for(Map.Entry<HashMap<Resource,Integer>, HashMap<CardPoints,Integer>> test: prop.get(state).entrySet())
            return test.getKey();

        return null;
    }

    public HashMap<CardPoints,Integer> getCurrentRewardsFromUpgrade()
    {
        for(Map.Entry<HashMap<Resource,Integer>, HashMap<CardPoints,Integer>> test: prop.get(state).entrySet())
            return test.getValue();

        return null;

    }
}
