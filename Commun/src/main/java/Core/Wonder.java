package Core;

import Card.CardPoints;
import Card.Effect;
import Card.Resource;
import Card.ResourceChoiceEffect;
import Utility.Utilities;
import Utility.Writer;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

/**
 *  Implementation of Wonders in the game.
 *  The several states of the wonders are represented by an ArrayList where each element of the List represents a state.
 */
public class Wonder {


    private String name;

    /**
     *  Current state of the wonder, if this hits the maxstate value, then the wonder cannot be built further
     */
    private int state = 0;

    private int maxstate;

    /**
     *  Properties of the wonder, will hold the cost of each state and the rewards
     *  Basically the list is
     *  {  [{COST : REWARD }] , .... , N }
     */
    private ArrayList<HashMap<EnumMap<Resource,Integer>, EnumMap<CardPoints,Integer>>> prop;

    /**
     *  The resource that the wonder produces, player will be given one after the choose their wonders.
     */
    private Resource productedResource;

    /**
     *  If the state of the wonder doesn't give a reward ( fixed ressources or points) it will give an effect (choice between a list of resources ).
     *  These effects will be stored in that list.
     */
    private ArrayList<Effect> effects;

    /**
     *  List of already applied effects of the wonder, the player can stack them.
     */
    private ArrayList<Effect> appliedEffects;

    /**
     *
     * The wonder is parsed in the constructor from the json file
     * @param name name of the wonder
     * @throws IOException
     */
    public Wonder(String name) throws IOException {
        String content = Files.readString(Paths.get("Commun","src", "assets", "wonders", name + ".json"));
        JSONArray card = new JSONArray(content);

        this.name = name;
        this.productedResource = Utilities.getResourceByString(card.getJSONObject(0).getString("productedRes"));
        this.effects = new ArrayList<>();
        if (card.length() > 0) {
            maxstate = card.length();
            prop = new ArrayList<>(card.length());
            for (int i = 0; i < card.length(); i++) {
                JSONObject json = card.getJSONObject(i);

                EnumMap<Resource, Integer> tmpMap = new EnumMap<>(Resource.class);
                EnumMap<CardPoints, Integer> tmpMap2 = new EnumMap<>(CardPoints.class);
                if (card.getJSONObject(i).has("resourceChoiceEffect")) {

                    JSONArray resourceChoiceEffect = json.getJSONArray("resourceChoiceEffect");
                    ArrayList<Resource> resList = new ArrayList<>();

                    for (int k = 0; k < resourceChoiceEffect.length(); k++) {
                        resList.add(Resource.valueOf(resourceChoiceEffect.getString(k)));
                    }
                    this.effects.add(new ResourceChoiceEffect(resList));

                }

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
                HashMap<EnumMap<Resource, Integer>, EnumMap<CardPoints, Integer>> tmpPropMap = new HashMap<>();
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

    public ArrayList<HashMap<EnumMap<Resource, Integer>, EnumMap<CardPoints, Integer>>> getProp() {
        return prop;
    }

    public void setProp(ArrayList<HashMap<EnumMap<Resource, Integer>, EnumMap<CardPoints, Integer>>> prop) {
        this.prop = prop;
    }

    public Resource getProductedResource() {
        return productedResource;
    }

    public void setProductedResource(Resource productedResource) {
        this.productedResource = productedResource;
    }

    public EnumMap<Resource, Integer> getCurrentUpgradeCost()
    {
        for(Map.Entry<EnumMap<Resource,Integer>, EnumMap<CardPoints,Integer>> test: prop.get(state).entrySet())
            return test.getKey();

        return null;
    }

    public EnumMap<CardPoints,Integer> getCurrentRewardsFromUpgrade()
    {
        for(Map.Entry<EnumMap<Resource,Integer>, EnumMap<CardPoints,Integer>> test: prop.get(state).entrySet())
            return test.getValue();

        return null;

    }

    public boolean isWonderFinished()
    {
        return state == maxstate;
    }

    /**
     * This function compares the player inventory with the current upgrade cost of the wonder.
     * @param playerResources Player owned resources
     * @return if the player can build his wonder of not
     */
    public boolean canUpgrade(EnumMap<Resource, Integer> playerResources)
    {
        if(playerResources.isEmpty()) return false;

        if(playerResources.size() < getCurrentUpgradeCost().size()) return false;

        for(Map.Entry<Resource,Integer> entry1 : getCurrentUpgradeCost().entrySet())
        {
            Resource res1 = entry1.getKey();
            int nbRes1 = entry1.getValue();
            if(playerResources.get(res1) == null) return false;

            else if(playerResources.get(res1) < nbRes1) return false;
        }

        return true;
    }

    @Override
    public String toString() {
        return "Wonder{" +
                "name='" + name + '\'' +
                ", state=" + state +
                ", maxstate=" + maxstate +
                ", prop=" + prop +
                ", productedResource=" + productedResource +
                ", effects=" + effects +
                ", appliedEffects=" + appliedEffects +
                '}';
    }
}
