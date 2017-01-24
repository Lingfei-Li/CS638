package wisc.lingfei.CS638.Lab1;

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

    Perceptron(int inputNum, double stepLen) {
        Random rand = new Random();
        this.inputNum = inputNum;
//        this.stepLen = stepLen;
        this.stepLen = 0.01;
        weight = new ArrayList<>();
        for(int i = 0; i < inputNum; i ++) {
            weight.add(rand.nextDouble() - 0.5);
        }
        biasWeight = rand.nextDouble() - 0.5;
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
                int out = getOutput(data);
                int teacher = data.labelIndex;
//                System.out.println(trainSet._labels.name[out] + " - " + trainSet._labels.name[teacher]);
                if(teacher == out) {
                    epochTrainAcc ++;
                }
                backProp(out, data);
            }
            epochTrainAcc /= trainSet._sampleNum;
            trainAcc = epochTrainAcc;

            double epochTuneAcc = this.test(tuneSet);
            if(tuneAcc != 0.0 && epochTuneAcc < tuneAcc) {
//                break;
            }
            tuneAcc = epochTuneAcc;

//            System.out.println(MathUtil.formatPercentage(epochTrainAcc) + " - " + MathUtil.formatPercentage(epochTuneAcc));
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

    public int getOutput(DataEntry data) {
        return MathUtil.stepFunc(getNet(data));
    }

    private void backProp(int out, DataEntry data) {
        int teacher = data.labelIndex;
        for(int j = 0; j < this.inputNum; j ++) {
            double delta = stepLen * (double)(teacher - out) * (double)data.featureIndex[j];
            this.weight.set(j, this.weight.get(j) + delta);
        }
        double delta = stepLen * (double)(teacher - out) * bias;
        biasWeight += delta;
    }


    public double test(DataSet ds) {
        double acc = 0.0;
        for(int i = 0; i < ds._sampleNum; i ++) {
            DataEntry data = ds._data.get(i);
            int out = getOutput(data);
            int teacher = data.labelIndex;
            if(teacher == out) {
                acc ++;
            }
        }
        acc /= ds._sampleNum;
        return acc;
    }


}
