package Card;


public class BrownEffect extends Effect {

    private Resource res1;
    private Resource res2;

    protected BrownEffect(Resource res1, Resource res2) {
        this.res1 = res1;
        this.res2 = res2;
    }


    public Resource getRes1() {
        return res1;
    }

    public Resource getRes2() {
        return res2;
    }

}
