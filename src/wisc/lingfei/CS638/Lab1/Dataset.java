package wisc.lingfei.CS638.Lab1;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class DataSet {


    private class Labels {
        private String[] name;
        HashMap<String, Integer> nameIndex;

        Labels(String label1, String label2) {
            this.name = new String[]{label1, label2};
            this.nameIndex = new HashMap<>();
            for(int i = 0; i < name.length; i ++) {
                nameIndex.put(name[i], i);
            }
        }
    }

    private class Feature {
        private String name;
        private String[] values;
        HashMap<String, Integer> valueIndex;

        Feature(String name, String[] values) {
            this.name = name;
            this.values = values;
            this.valueIndex = new HashMap<>();
            for(int i = 0; i < values.length; i ++) {
                valueIndex.put(values[i], i);
            }
        }
    }

    private class DataEntry {
        private String name;
        private int labelIndex;
        private int[] featureIndex;
        DataEntry(String name, String labelName, String[] featureNames) {
            this.name = name;
            this.labelIndex = _labels.nameIndex.get(labelName);
            this.featureIndex = new int[featureNames.length];
            for(int i = 0; i < featureNames.length; i ++) {
                this.featureIndex[i] = _features[i].valueIndex.get(featureNames[i]);
            }
        }
    }

    private int _featureNum = -1;
    private Feature[] _features;
    private Labels _labels;
    private int _sampleNum = -1;
    private List<DataEntry> _data = new ArrayList<>();

    /**
     *
     * Read a non-comment line from buffered reader
     * */
    private String readLine(BufferedReader br) throws IOException{
        String line;
        while((line = br.readLine()) != null && (line.trim().startsWith("//") || line.trim().length() == 0));
        if(line == null) {
            throw new IOException("End of File is reached");
        }
        return line.trim();
    }

    public void printData() {
        System.out.println("Number of features: " + _featureNum);
        System.out.println("Labels: \n\t" + _labels.name[0] + "\n\t" + _labels.name[1]);
        System.out.println("Number of samples: " + _sampleNum);

        int[] labelCnt = new int[2];
        for(DataEntry d : _data) {
            labelCnt[d.labelIndex] ++;
        }
        System.out.println(labelCnt[0] + " have output label '" + _labels.name[0] + "', " + labelCnt[1] + " have output label '" + _labels.name[1] + "'");

        for(int f_index = 0; f_index < _features.length; f_index ++) {
            Feature f = _features[f_index];

            System.out.println("Feature " + f.name + ":");

            int[][] cnt = new int[_labels.name.length][f.values.length];
            for(DataEntry d : _data) {
                cnt[d.labelIndex][d.featureIndex[f_index]] ++;
            }

            for(int l_index = 0; l_index < 2; l_index ++) {
                for(int fVal_index = 0; fVal_index < f.values.length; fVal_index ++) {
                    System.out.println(_labels.name[l_index] + " and " + f.values[fVal_index]);
                    System.out.println(cnt[l_index][fVal_index]);
                }
            }
        }
    }

    public void loadData(String file) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line = readLine(br);
            _featureNum = Integer.parseInt(line);
            _features = new Feature[_featureNum];
            for(int i = 0; i < _featureNum; i ++) {
                line = readLine(br);
                String[] featureEntry = line.split("-");
                if(featureEntry.length != 2) {
                    throw new IllegalArgumentException("Feature data doesn't have the required format");
                }
                String featureName = featureEntry[0].trim();
                String[] featureVal = featureEntry[1].trim().split("\\s+");

                Feature newFeature = new Feature(featureName, featureVal);
                _features[i] = newFeature;
            }

            System.out.println("hello");

            _labels = new Labels(readLine(br), readLine(br));

            line = readLine(br);
            _sampleNum = Integer.parseInt(line);
            for(int i = 0; i < _sampleNum; i ++) {
                line = readLine(br);
                String[] dataEntryStr = line.split("\\s+");
                if(dataEntryStr.length != _featureNum + 2) {
                    throw new IllegalArgumentException("Sample data entry doesn't have the required format");
                }

                DataEntry d = new DataEntry(dataEntryStr[0], dataEntryStr[1], Arrays.copyOfRange(dataEntryStr, 2, dataEntryStr.length));
                _data.add(d);
            }

        }
        catch(IOException | NullPointerException | IllegalArgumentException ex) {
            System.out.println(ex);
        }

        this.printData();

    }

    public static void main(String[] args) {
        String file;
        if(args.length == 0) {
            file = "titanic-fatalities.data";
        }
        else {
            file = args[0];
        }

        DataSet ds = new DataSet();
        ds.loadData(file);
    }
}
