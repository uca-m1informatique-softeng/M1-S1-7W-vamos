package Wonder;

import Card.*;
import Core.Game;
import Effects.*;
import Utility.Constante;
import Utility.Utilities;
import Utility.Writer;
import org.json.JSONArray;
import org.json.JSONObject;
import static Utility.Constante.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Implementation of Wonders in the game.
 * The several states of the wonders are represented by an ArrayList where each element of the List represents a state.
 */
public class Wonder {

    private String name;

    /**
     * Current state of the wonder, if this hits the maxstate value, then the wonder cannot be built further
     */
    private int state = 0;

    private int maxState;

    /**
     * Properties of the wonder, will hold the cost of each state and the rewards
     * Basically the list is
     * {  [{COST : REWARD }] , .... , N }
     */
    private ArrayList<HashMap<EnumMap<Resource, Integer>, EnumMap<CardPoints, Integer>>> prop;

    /**
     * The resource that the wonder produces, player will be given one after the choose their wonders.
     */
    private Resource producedResource;

    /**
     * If the state of the wonder doesn't give a reward ( fixed ressources or points) it will give an effect (choice between a list of resources ).
     * These effects will be stored in that list.
     */
    //private ArrayList<Effect> effects;
    public HashMap<Integer , Effect> effects;

    /**
     * List of already applied effects of the wonder, the player can stack them.
     */
    private ArrayList<Effect> appliedEffects;

    /**
     * The wonder is parsed in the constructor from the json file
     *
     * @param name name of the wonder
     * @throws IOException if wonder's file could not be accessed
     */
    public Wonder(String name) throws IOException {
        //String content = Files.readString(Paths.get("resources", "wonders", name + ".json"));
        InputStream is = Card.class.getClassLoader().getResourceAsStream("wonders/"+name+".json");
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String content = br.lines().collect(Collectors.joining());
        JSONObject card = new JSONObject(content);
        ArrayList<String> stages = new ArrayList<>();
        for (String key : card.keySet()) {
            if (key.contains("stage"))
                stages.add(key);
        }
        this.name = name;
        this.producedResource = Utilities.getResourceByString(card.getString("producedRes"));
        this.effects = new HashMap<>();
        this.appliedEffects = new ArrayList<>() ;
        if (stages.size() > 0) {
            maxState = stages.size();
            prop = new ArrayList<>(stages.size());
            for (int i = 0; i < stages.size(); i++) {
                EnumMap<Resource, Integer> tmpMap = new EnumMap<>(Resource.class);
                EnumMap<CardPoints, Integer> tmpMap2 = new EnumMap<>(CardPoints.class);
                JSONObject stage = card.getJSONObject(stages.get(i));
                if (stage.has(STR_COST)) {
                    for (int k = 0; k < stage.getJSONObject(STR_COST).names().length(); k++) {
                        String keyStr = stage.getJSONObject(STR_COST).names().getString(k);
                        int value = stage.getJSONObject(STR_COST).getInt(keyStr);
                        tmpMap.put(Utilities.getResourceByString(keyStr), value);
                    }
                }
                if (stage.has(STR_REWARD)) {
                    for (int k = 0; k < stage.getJSONObject(STR_REWARD).names().length(); k++) {
                        if (stage.getJSONObject(STR_REWARD).has("ScienceChoiceEffect")) {
                            this.effects.put(i + 1 , new ScienceChoiceEffect());
                        }
                        else if (stage.getJSONObject(STR_REWARD).has("TookOneDiscardedCard")) {
                            //TODO Nicolas will implemente this effect soon.
                        }
                        else if (stage.getJSONObject(STR_REWARD).has("ResourceChoiceEffect")) {
                            JSONArray effectW = stage.getJSONObject(STR_REWARD).getJSONArray("ResourceChoiceEffect");
                            ArrayList<Resource> resList = new ArrayList<>();
                            for (int l = 0; l < effectW.length(); l++) {
                                resList.add(Resource.valueOf(effectW.getString(l)));
                            }
                            this.effects.put(i + 1,new ResourceChoiceEffect(resList));
                        }
                        else if (stage.getJSONObject(STR_REWARD).has("PlaySeventhCardEffect")) {
                            this.effects.put(i + 1, new PlaySeventhCardEffect());
                        } else if (stage.getJSONObject(STR_REWARD).has("CopyOneGuildEffect")) {
                            this.effects.put(i + 1, new CopyOneGuildEffect());
                        } else if (stage.getJSONObject(STR_REWARD).has("FreeCardPerAgeEffect")){
                            this.effects.put(i + 1, new FreeCardPerAgeEffect());
                        }
                        else if (stage.getJSONObject(STR_REWARD).has("ResourceChoiceFromBoardEffect")){
                            //TODO Implement ResourceChoiceFromBoardEffect this.effects.put(i + 1, new ResourceChoiceFromBoardEffect());
                        }


                        else {
                            String keyStr = stage.getJSONObject(STR_REWARD).names().getString(k);
                            int value = stage.getJSONObject(STR_REWARD).getInt(keyStr);
                            tmpMap2.put(Utilities.getCardPointByString(keyStr), value);
                        }
                    }
                }
                HashMap<EnumMap<Resource, Integer>, EnumMap<CardPoints, Integer>> tmpPropMap = new HashMap<>();
                tmpPropMap.put(tmpMap, tmpMap2);
                prop.add(tmpPropMap);
            }
        }
        if (Game.debug) {
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
        return maxState;
    }

    public void setMaxstate(int maxStage) {
        this.maxState = maxStage;
    }

    public ArrayList<HashMap<EnumMap<Resource, Integer>, EnumMap<CardPoints, Integer>>> getProp() {
        return prop;
    }

    public void setProp(ArrayList<HashMap<EnumMap<Resource, Integer>, EnumMap<CardPoints, Integer>>> prop) {
        this.prop = prop;
    }

    public Resource getProducedResource() {
        return producedResource;
    }

    public void setProducedResource(Resource producedResource) {
        this.producedResource = producedResource;
    }

    public EnumMap<Resource, Integer> getCurrentUpgradeCost() {
        for (Map.Entry<EnumMap<Resource, Integer>, EnumMap<CardPoints, Integer>> test : prop.get(state).entrySet())
            return test.getKey();

        return null;
    }

    public EnumMap<CardPoints, Integer> getCurrentRewardsFromUpgrade() {
        for (Map.Entry<EnumMap<Resource, Integer>, EnumMap<CardPoints, Integer>> test : prop.get(state).entrySet())
            return test.getValue();
        return null;

    }

    public boolean isWonderFinished() {
        return state == maxState;
    }

    /**
     * Return the effects of the stage wonder.
     * @return key are the index of the step wonder who give the effect
     */
    public HashMap<Integer , Effect> getEffects() {
        return effects;
    }

    public ArrayList<Effect> getAppliedEffects() {
        return appliedEffects;
    }

    /**
     * This function compares the player inventory with the current upgrade cost of the wonder.
     *
     * @param playerResources Player owned resources
     * @return if the player can build his wonder of not
     */
    public boolean canUpgrade(EnumMap<Resource, Integer> playerResources) {
        if (playerResources.isEmpty()) return false;

        if (playerResources.size() < getCurrentUpgradeCost().size()) return false;

        for (Map.Entry<Resource, Integer> entry1 : getCurrentUpgradeCost().entrySet()) {
            Resource res1 = entry1.getKey();
            int nbRes1 = entry1.getValue();
            if (playerResources.get(res1) == null) return false;

            else if (playerResources.get(res1) < nbRes1) return false;
        }

        return true;
    }

    @Override
    public String toString() {
        return "Wonder{" +
                "name='" + name + '\'' +
                ", state=" + state +
                ", maxState=" + maxState +
                ", prop=" + prop +
                ", producedResource=" + producedResource +
                ", effects=" + effects +
                ", appliedEffects=" + appliedEffects +
                '}';
    }
}
