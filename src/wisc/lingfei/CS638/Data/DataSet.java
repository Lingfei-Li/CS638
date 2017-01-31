package wisc.lingfei.CS638.Data;


import wisc.lingfei.CS638.Util.MathUtil;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DataSet {
    public int _featureNum = 0;
    public Feature[] _features;
    public Labels _labels;
    public int _sampleNum = 0;
    public List<DataEntry> _data = new ArrayList<>();

    public DataSet(String filename) {
        this.loadData(filename);
    }

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

            System.out.println("Feature '" + f.name + "'");

            int[][] cnt = new int[_labels.name.length][f.values.length];
            for(DataEntry d : _data) {
                cnt[d.labelIndex][d.featureIndex[f_index]] ++;
            }

            for(int l_index = 0; l_index < 2; l_index ++) {
                for(int fVal_index = 0; fVal_index < f.values.length; fVal_index ++) {
                    double perc = (double)cnt[l_index][fVal_index]/_sampleNum;
                    System.out.println("\t"+ MathUtil.formatPercentage(perc) + " " + f.values[fVal_index] +" " + _labels.name[l_index]);
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

            _labels = new Labels(readLine(br), readLine(br));

            line = readLine(br);
            _sampleNum = Integer.parseInt(line);
            for(int i = 0; i < _sampleNum; i ++) {
                line = readLine(br);
                String[] dataEntryStr = line.split("\\s+");
                if(dataEntryStr.length != _featureNum + 2) {
                    throw new IllegalArgumentException("Sample data entry doesn't have the required format");
                }

                DataEntry d = new DataEntry(dataEntryStr[0], dataEntryStr[1], Arrays.copyOfRange(dataEntryStr, 2, dataEntryStr.length), _labels, _features);
                _data.add(d);
            }

        }
        catch(IOException | NullPointerException | IllegalArgumentException ex) {
            System.out.println(ex);
        }

    }
}
