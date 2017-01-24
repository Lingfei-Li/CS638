package wisc.lingfei.CS638.Lab1;

/**
 * Created by Lingfei on 2017-1-22.
 */
public class Main {

    public static void main(String[] args) {
        String trainFile, tuneFile, testFile;
        if(args.length != 4) {
            trainFile = "red-wine-quality-train.data";
            tuneFile = "red-wine-quality-tune.data";
            testFile = "red-wine-quality-test.data";

//            trainFile = "Thoracic_Surgery_Data_train.data";
//            tuneFile = "Thoracic_Surgery_Data_tune.data";
//            testFile = "Thoracic_Surgery_Data_test.data";
        }
        else {
            trainFile = args[1];
            tuneFile = args[2];
            testFile = args[3];
        }

        double stepLen = 0.01;

        DataSet trainData = new DataSet(trainFile);
        DataSet tuneData = new DataSet(tuneFile);
        DataSet testData = new DataSet(testFile);

        double testRound = 10;

        boolean enableEnsemble = false;
        if(enableEnsemble) {
            double avgEnsembleAcc = 0.0;
            for(int i = 0; i < testRound; i ++) {
                int ensembleSize = 11;
                Ensemble e = new Ensemble(trainData, tuneData, testData, ensembleSize);
                e.train();
                double testAcc = e.test();
                System.out.println("Test set accuracy: " + MathUtil.formatPercentage(testAcc));
                avgEnsembleAcc += testAcc;
            }
            avgEnsembleAcc /= testRound;
            System.out.println("Ensemble accuracy: " + MathUtil.formatPercentage(avgEnsembleAcc));
            System.out.println();

        }


        double avgPerceptronAcc = 0.0;
        boolean earlyStop = true;
        for(int i = 0; i < 10; i ++) {
            Perceptron p = new Perceptron(trainData._featureNum, stepLen, earlyStop);
            p.train(trainData, tuneData);
            double testAcc = p.test(testData);
            System.out.println("Test set accuracy: " + MathUtil.formatPercentage(testAcc));
            avgPerceptronAcc += testAcc;
        }
        avgPerceptronAcc /= testRound;
        System.out.println("Average accuracy: " + MathUtil.formatPercentage(avgPerceptronAcc));


    }

}
