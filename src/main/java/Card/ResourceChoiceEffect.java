package Card;

import java.util.ArrayList;
import java.util.EnumMap;


public class ResourceChoiceEffect extends Effect {

    private ArrayList<Resource> res;

    protected ResourceChoiceEffect(ArrayList<Resource> res) {
        this.res = res;
    }


    public ArrayList<Resource> getRes() {
        return res;
    }

    public void applyEffect(EnumMap<Resource, Integer> cost) {
        boolean applied = false;

        for (Resource neededResource : cost.keySet()) {
            for (Resource effectResource : this.res) {
                if (neededResource.equals(effectResource) && cost.get(neededResource) > 1) {
                    cost.put(neededResource, cost.get(neededResource) - 1);
                    applied = true;
                }
                if (applied) break;
            }
            if (applied) break;
        }
    }

}
