package wonder;

import card.*;
import core.Game;
import effects.*;
import utility.Tuple;
import utility.Utilities;
import utility.Writer;
import org.json.JSONArray;
import org.json.JSONObject;
import static utility.Constante.*;
import java.io.*;
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
    private ArrayList<Tuple<EnumMap<Resource, Integer>, EnumMap<CardPoints, Integer>>> prop;

    /**
     * The resource that the wonder produces, player will be given one after the choose their wonders.
     */
    private Resource producedResource;

    /**
     * If the state of the wonder doesn't give a reward ( fixed ressources or points) it will give an effect (choice between a list of resources ).
     * These effects will be stored in that list.
     */
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
        InputStream is = Card.class.getClassLoader().getResourceAsStream("wonders/"+name+".json");
        if (is == null) throw new IOException();
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
            for (int i = maxState-1; i >= 0; i--) {
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
                            this.effects.put(i + 1, new TookDiscardCardEffect());
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
                        } else if (stage.getJSONObject(STR_REWARD).has("TookDiscardCardEffect")){
                            this.effects.put(i + 1, new TookDiscardCardEffect());
                        }
                        else if (stage.getJSONObject(STR_REWARD).has("TradeResourceEffect")){
                            JSONObject tradeResourceEffect = stage.getJSONObject(STR_REWARD).getJSONObject("TradeResourceEffect");
                            ArrayList<Resource> resList = new ArrayList<>();

                            for (int j = 0; j < tradeResourceEffect.length(); j++) {
                                resList.add(Resource.valueOf(tradeResourceEffect.getJSONArray("resourcesModified").getString(k)));  }
                            Effect effect = new TradeResourceEffect(
                                    tradeResourceEffect.getBoolean("prevPlayerAllowed"),
                                    tradeResourceEffect.getBoolean("nextPlayerAllowed"),
                                    resList);
                            this.effects.put(i + 1, effect);
                        }


                        else {
                            String keyStr = stage.getJSONObject(STR_REWARD).names().getString(k);
                            int value = stage.getJSONObject(STR_REWARD).getInt(keyStr);
                            tmpMap2.put(Utilities.getCardPointByString(keyStr), value);
                        }
                    }
                }
                Tuple<EnumMap<Resource, Integer>, EnumMap<CardPoints, Integer>> tmpPropMap = new Tuple<>(tmpMap, tmpMap2);
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

    public ArrayList<Tuple<EnumMap<Resource, Integer>, EnumMap<CardPoints, Integer>>> getProp() {
        return prop;
    }

    public Resource getProducedResource() {
        return producedResource;
    }

    public EnumMap<Resource, Integer> getCurrentUpgradeCost() {
        return prop.get(state).x;
    }

    public EnumMap<CardPoints, Integer> getCurrentRewardsFromUpgrade() {
        return  prop.get(state).y;
    }

    public boolean isWonderFinished() {
        return state >= maxState - 1;
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
     * @param playerResources player owned resources
     * @return if the player can build his wonder of not
     */
    public boolean canUpgrade(EnumMap<Resource, Integer> playerResources) {
        if (this.state >= 2) return false;

        if (playerResources.isEmpty()) return false;

        if (playerResources.size() < getCurrentUpgradeCost().size()) return false;

        for (Map.Entry<Resource, Integer> entry1 : getCurrentUpgradeCost().entrySet()) {
            Resource res1 = entry1.getKey();
            int nbRes1 = entry1.getValue();
            if (playerResources.get(res1) == null || playerResources.get(res1) < nbRes1) return false;
        }

        return true;
    }

    @Override
    public String toString() {
        return "wonder{" +
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
