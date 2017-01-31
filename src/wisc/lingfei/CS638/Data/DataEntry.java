package wisc.lingfei.CS638.Data;

/**
 * Created by Lingfei on 2017-1-22.
 */
public class DataEntry {
    public String name;
    public int labelIndex;
    public int[] featureIndex;
    DataEntry(String name, String labelName, String[] featureNames, Labels labels, Feature[] features) {
        this.name = name;
        this.labelIndex = labels.nameIndex.get(labelName);
        this.featureIndex = new int[featureNames.length];
        for(int i = 0; i < featureNames.length; i ++) {
            this.featureIndex[i] = features[i].valueIndex.get(featureNames[i]);
        }
    }


}
