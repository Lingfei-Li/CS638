package wisc.lingfei.CS638.Lab1;

import wisc.lingfei.CS638.Data.DataEntry;
import wisc.lingfei.CS638.Data.DataSet;
import wisc.lingfei.CS638.Util.MathUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Lingfei on 2017-1-22.
 */
public class Perceptron {

    private int inputNum;
    private double bias = -1;
    private double biasWeight;
    private List<Double> weight;
    private double stepLen;
    private boolean earlyStop = false;

    Perceptron(int inputNum, double stepLen) {
        Random rand = new Random();
        this.inputNum = inputNum;
        this.stepLen = stepLen;
        weight = new ArrayList<>();
        for(int i = 0; i < inputNum; i ++) {
            weight.add(rand.nextDouble() - 0.5);
        }
        biasWeight = rand.nextDouble() - 0.5;
    }

    Perceptron(int inputNum, double stepLen, boolean earlyStop) {
        this(inputNum, stepLen);
        this.earlyStop = earlyStop;
    }

    public double train(DataSet trainSet, DataSet tuneSet) {
        if(trainSet._featureNum != inputNum) {
            throw new IllegalArgumentException("Number of features is not equal to given input number");
        }
        //Stochastic gradient descent
        double trainAcc = 0.0;
        double tuneAcc = 0.0;
        int epoch = 0;
        while(epoch++ < 1000) {
            double epochTrainAcc = 0.0;
            List<Integer> shuffledIndex = MathUtil.generateShuffledList(trainSet._sampleNum);
            for(int i = 0; i < trainSet._sampleNum; i ++) {
                int sampleIndex = shuffledIndex.get(i);
                DataEntry data = trainSet._data.get(sampleIndex);

                double out = getOutput(data);
                double teacher = (double)data.labelIndex;

                backProp(out, data);

                if(teacher == MathUtil.getLabelIndex(out)) {
                    epochTrainAcc ++;
                }
            }
            epochTrainAcc /= trainSet._sampleNum;
            trainAcc = epochTrainAcc;

            //Prevent overfitting with tuning set early stopping
            double epochTuneAcc = this.test(tuneSet);
            if(tuneAcc != 0.0 && epochTuneAcc < tuneAcc && this.earlyStop) {
                break;
            }
            tuneAcc = epochTuneAcc;

        }
        return trainAcc;
    }

    private double getNet(DataEntry data) {
        double net = 0.0;
        for(int j = 0; j < this.inputNum; j ++) {
            net += this.weight.get(j) * (double)data.featureIndex[j];
        }
        net += biasWeight * bias;
        return net;
    }

    public double getOutput(DataEntry data) {
//        return MathUtil.stepFunc(getNet(data));
        return MathUtil.sigmoid(getNet(data));
    }

    private void backProp(double out, DataEntry data) {
        double teacher = (double)data.labelIndex;
        for(int j = 0; j < this.inputNum; j ++) {
            double delta = stepLen * (teacher - out) * MathUtil.sigmoidDeriv(out) * (double)data.featureIndex[j];
            this.weight.set(j, this.weight.get(j) + delta);
        }
        double delta = stepLen * (teacher - out) * bias;
        biasWeight += delta;
    }


    public double test(DataSet ds) {
        double acc = 0.0;
        for(int i = 0; i < ds._sampleNum; i ++) {
            DataEntry data = ds._data.get(i);
            double out = getOutput(data);
            int teacher = data.labelIndex;

            if(teacher == MathUtil.getLabelIndex(out)) {
                acc ++;
            }
        }
        acc /= ds._sampleNum;
        return acc;
    }
}
