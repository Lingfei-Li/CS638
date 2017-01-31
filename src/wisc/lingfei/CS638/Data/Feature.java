package wisc.lingfei.CS638.Data;

import java.util.HashMap;

/**
 * Created by Lingfei on 2017-1-22.
 */
public class Feature {
    public String name;
    public String[] values;
    public HashMap<String, Integer> valueIndex;

    Feature(String name, String[] values) {
        this.name = name;
        this.values = values;
        this.valueIndex = new HashMap<>();
        for(int i = 0; i < values.length; i ++) {
            valueIndex.put(values[i], i);
        }
    }
}
