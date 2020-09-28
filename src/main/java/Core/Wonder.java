package Core;

import org.json.JSONArray;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

public class Wonder {


    private String name;

    private int state;

    private int maxstate;

    private ArrayList<HashMap<HashMap<Resource,Integer>, HashMap<Resource,Integer>>> prop;

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

                HashMap<Resource, Integer> tmpMap2 = new HashMap<>();
                System.out.println(card.length());
                if (card.getJSONObject(i).has("cost")) {
                    for (int k = 0; k < card.getJSONObject(i).getJSONArray("cost").length(); k++) {

                        String keyStr = card.getJSONObject(i).getJSONArray("cost").getJSONObject(k).keys().next();
                        Resource key = Resource.STONE; // Default case
                        int value = card.getJSONObject(i).getJSONArray("cost").getJSONObject(k).getInt(card.getJSONObject(i).getJSONArray("cost").getJSONObject(k).keys().next());

                        switch (keyStr) {
                            case "WOOD":
                                key = Resource.WOOD;
                                break;
                            case "STONE":
                                key = Resource.STONE;
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
                        }

                        tmpMap.put(key, value);
                    }
                }

                for (int k = 0; k < card.getJSONObject(i).getJSONArray("reward").length(); k++) {

                    String keyStr = card.getJSONObject(i).getJSONArray("reward").getJSONObject(k).keys().next();
                    Resource key = Resource.STONE; // Default case
                    int value = card.getJSONObject(i).getJSONArray("reward").getJSONObject(k).getInt(card.getJSONObject(i).getJSONArray("resource").getJSONObject(k).keys().next());

                    switch (keyStr) {
                        case "WOOD":
                            key = Resource.WOOD;
                            break;
                        case "STONE":
                            key = Resource.STONE;
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
                    }

                    tmpMap2.put(key, value);
                }


                // card color
                HashMap<HashMap<Resource, Integer>, HashMap<Resource, Integer>> tmpPropMap = null;
                tmpPropMap.put(tmpMap, tmpMap2);
                prop.add(tmpPropMap);
            }
        }

    }

}
