package wisc.lingfei.CS638.Lab1;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lingfei on 2017-1-23.
 */
public class Ensemble {


    private DataSet trainSet;
    private DataSet tuneSet;
    private DataSet testSet;
    private List<Perceptron> perceptrons;
    private int ensembleSize;

    Ensemble(DataSet trainSet, DataSet tuneSet, DataSet testSet, int n) {
        this.trainSet = trainSet;
        this.tuneSet = tuneSet;
        this.testSet = testSet;
        this.ensembleSize = n;
        double stepLen = 0.1;

        this.perceptrons = new ArrayList<>();
        for(int i = 0; i < n; i ++) {
            Perceptron p = new Perceptron(trainSet._featureNum, stepLen);
            this.perceptrons.add(p);
        }
    }

    public void train() {
        for(Perceptron p : this.perceptrons) {
            p.train(trainSet, tuneSet);
        }
    }

    public double test() {
        double accuracy = 0.0;
        for(DataEntry data : testSet._data) {
            int teacher = data.labelIndex;
            int out = 0;
            for(Perceptron p : this.perceptrons) {
                out += MathUtil.getLabelIndex(p.getOutput(data));
            }
            if(out > this.ensembleSize/2) {
                out = 1;
            }
            else {
                out = 0;
            }
            if(out == teacher) {
                accuracy ++;
            }
        }
        accuracy /= testSet._sampleNum;
        return accuracy;
    }
}
