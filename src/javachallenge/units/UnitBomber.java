package javachallenge.units;

/**
 * Created by merhdad on 2/6/14.
 */
public class UnitBomber extends UnitCell {
    private boolean arrived = false;

    public void setArrived(boolean arrived) {
        this.arrived = arrived;
    }

    public boolean isArrived() {
        return arrived;
    }
}
