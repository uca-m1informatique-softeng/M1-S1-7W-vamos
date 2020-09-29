package Card;

import java.util.ArrayList;


public class ResourceChoiceEffect extends Effect {

    private ArrayList<Resource> res;

    protected ResourceChoiceEffect(ArrayList<Resource> res) {
        this.res = res;
    }


    public ArrayList<Resource> getRes() {
        return res;
    }

}
