package wisc.lingfei.CS638.Lab1;

import java.util.HashMap;

/**
 * Created by Lingfei on 2017-1-22.
 */
public class Labels {
    public String[] name;
    public HashMap<String, Integer> nameIndex;

    Labels(String[] labels) {
        this.name = labels;
        this.nameIndex = new HashMap<>();
        for(int i = 0; i < name.length; i ++) {
            nameIndex.put(name[i], i);
        }
    }
    Labels(String label1, String label2) {
        this(new String[]{label1, label2});
    }

}
