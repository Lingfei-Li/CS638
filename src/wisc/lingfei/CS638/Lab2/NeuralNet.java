package wisc.lingfei.CS638.Lab2;

import wisc.lingfei.CS638.Data.DataEntry;
import wisc.lingfei.CS638.Data.DataSet;
import wisc.lingfei.CS638.Util.MathUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.lang.System.exit;

/**
 * Created by Lingfei on 2017-1-24.
 */
public class NeuralNet {


    private double stepLen = 0.01;
    //Input of a node & Activation
    private double[][][] _W = new double[2][][];
    private double[][] _A = new double[3][];
    private double[][] _IN = new double[3][];
    private double[][] _DELTA = new double[3][];
    private int[] nodeNum = new int[3];
    private int outputLayer = 2;


    public NeuralNet(int inputNum, int hiddenNum, int outputNum, double stepLen) {
        nodeNum[0] = inputNum;
        nodeNum[1] = hiddenNum;
        nodeNum[2] = outputNum;
        this.stepLen = stepLen;

        //Init weights
        _W = new double[2][][];
        _W[0] = new double[inputNum + 1][hiddenNum];
        _W[1] = new double[hiddenNum + 1][outputNum];
        for(int i = 0; i < outputLayer; i ++) {
            for(int j = 0; j < _W[i].length; j ++) {
                for(int k = 0; k < _W[i][j].length; k ++) {
                    _W[i][j][k] = MathUtil.getRandWeight();
                }
            }
        }

        _A[0] = new double[inputNum + 1];
        _A[1] = new double[hiddenNum + 1];
        _A[2] = new double[outputNum];
        _IN[0] = new double[inputNum + 1];
        _IN[1] = new double[hiddenNum + 1];
        _IN[2] = new double[outputNum];
        _DELTA[0] = new double[0];
        _DELTA[1] = new double[hiddenNum + 1];
        _DELTA[2] = new double[outputNum + 1];

        //Bias
        _A[0][inputNum] = -1;
        _A[1][hiddenNum] = -1;
        _IN[0][inputNum] = -1;
        _IN[1][hiddenNum] = -1;
    }

    public void train(DataSet trainSet, DataSet tuneSet) {
        if(trainSet._featureNum != nodeNum[0]) {
            throw new IllegalArgumentException("Feature number is not equal to input node number");
        }

        int epoch = 0;
        while(++epoch < 100) {
            double accuracy = 0.0;
            for(DataEntry data : trainSet._data) {
                //Forward Feeding
                for(int node = 0; node < nodeNum[0]; node ++) {
                    _A[0][node] = (double)data.featureIndex[node];
                }
                for(int layer = 1; layer <= outputLayer; layer ++) {
                    for(int node2 = 0; node2 < nodeNum[layer]; node2 ++) {
                        _IN[layer][node2] = in(layer, node2);
                        _A[layer][node2] = activation(_IN[layer][node2]);
                    }
                }

                int maxProbLabel = -1;
                double maxProb = 0.0;
                for(int node = 0; node < nodeNum[outputLayer]; node ++) {
                    double v = _A[outputLayer][node];
                    if(maxProbLabel == -1 || v > maxProb) {
                        maxProb = v;
                        maxProbLabel = node;
                    }
                }
                if(maxProbLabel == data.labelIndex) {
                    accuracy ++;
                }


                //Backward Propagation
                for(int node = 0; node < nodeNum[outputLayer]; node++) {
                    double teacher = 0.0;
                    if(node == data.labelIndex) {
                        teacher = 1.0;
                    }
                    _DELTA[outputLayer][node] = MathUtil.sigmoidDeriv(_A[outputLayer][node]) * (teacher - _A[outputLayer][node]);
                }
                for(int layer = outputLayer - 1; layer >= 1; layer --) {  //
                    for(int node1 = 0; node1 < nodeNum[layer] + 1; node1++) {
                        double delta = 0.0;
                        for(int node2 = 0; node2 < nodeNum[layer+1]; node2++) {
                            delta += _W[layer][node1][node2] * _DELTA[layer+1][node2];
                        }
                        delta *= MathUtil.sigmoidDeriv(_A[layer][node1]);
                        _DELTA[layer][node1] = delta;
                    }
                }

                for(int layer = 0; layer < 2; layer ++) {
                    for(int node1 = 0; node1 < nodeNum[layer] + 1; node1 ++) {
                        for(int node2 = 0; node2 < nodeNum[layer+1]; node2 ++) {
                            double change = stepLen * _A[layer][node1] * _DELTA[layer+1][node2];
                            _W[layer][node1][node2] += change;
                        }
                    }
                }
            }
            accuracy /= trainSet._sampleNum;
        }
    }

    /*
    * @param node2 the node closer to output layer. node1 is closer to input layer.
    * */
    private double in(int layer, int node2) {
        double in = 0.0;
        // nodeNum +1 to include bias
        for(int node1 = 0; node1 < nodeNum[layer-1] + 1; node1 ++) {
            in += _W[layer-1][node1][node2] * _A[layer-1][node1];
        }
        return in;
    }

    private double activation(double in) {
        return MathUtil.sigmoid(in);
    }



    public double test(DataSet ds) {
        double accuracy = 0.0;
        for (DataEntry data : ds._data) {
            //Forward Feeding
            for (int node = 0; node < nodeNum[0]; node++) {
                _A[0][node] = (double) data.featureIndex[node];
            }
            for (int layer = 1; layer <= outputLayer; layer++) {
                for (int node2 = 0; node2 < nodeNum[layer]; node2++) {
                    _IN[layer][node2] = in(layer, node2);
                    _A[layer][node2] = activation(_IN[layer][node2]);
                }
            }

            int maxProbLabel = -1;
            double maxProb = 0.0;
            for (int node = 0; node < nodeNum[outputLayer]; node++) {
                double v = _A[outputLayer][node];
                if (maxProbLabel == -1 || v > maxProb) {
                    maxProb = v;
                    maxProbLabel = node;
                }
            }
            if (maxProbLabel == data.labelIndex) {
                accuracy++;
            }
        }
        accuracy /= ds._sampleNum;
        return accuracy;
    }






}
