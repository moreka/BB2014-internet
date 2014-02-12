package javachallenge.message;

import javachallenge.util.Map;

import java.io.Serializable;

/**
 * Created by mohammad on 2/6/14.
 */
public class InitialMessage implements Serializable {
    private Map map;

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
    }
}
