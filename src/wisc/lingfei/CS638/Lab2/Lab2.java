package wisc.lingfei.CS638.Lab2;

import wisc.lingfei.CS638.Data.*;
import wisc.lingfei.CS638.Util.*;

/**
 * Created by Lingfei on 2017-1-24.
 */
public class Lab2 {


    public static void main(String[] args) {
        String trainFile, tuneFile, testFile;
        trainFile = "red-wine-quality-train.data";
        tuneFile = "red-wine-quality-tune.data";
        testFile = "red-wine-quality-test.data";

        double stepLen = 0.01;

        DataSet trainData = new DataSet(trainFile);
        DataSet tuneData = new DataSet(tuneFile);
        DataSet testData = new DataSet(testFile);


        int hiddenNum = 10;
        NeuralNet nn = new NeuralNet(trainData._featureNum, hiddenNum, trainData._labels.name.length, stepLen);
        nn.train(trainData, tuneData);
        double testAcc = nn.test(testData);
        System.out.println("Test accuracy: " + MathUtil.formatPercentage(testAcc));



    }


}
