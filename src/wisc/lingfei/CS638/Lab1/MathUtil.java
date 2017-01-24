package wisc.lingfei.CS638.Lab1;

import java.util.*;

/**
 * Created by Lingfei on 2017-1-22.
 */
public class MathUtil {


    public static int stepFunc(double input) {
        if(input >= 0.0) {
            return 1;
        }
        return 0;
    }

    public static double sigmoid(double t) {
        return 1.0/(1+Math.exp(-t));
    }

    public static double sigmoidDeriv(double sigmoidVal) {
        return sigmoidVal*(1-sigmoidVal);
    }

    public static double linearDeriv(double t) {
        return 1.0;
    }

    public static int getLabelIndex(double t) {
        if(t < 0.5) return 0;
        return 1;
    }

    public static String formatPercentage(double perc) {
        return String.format("%.2f%%", perc*100);
    }

    public static boolean doubleEquals(double a, double b) {
        double epsilon = 0.0001;
        if(Math.abs(a-b) < epsilon) {
            return true;
        }
        return false;
    }

    public static List<Integer> generateShuffledList(int n) {
        Set<Integer> set = new HashSet<>();
        List<Integer> ans = new ArrayList<>();
        Random rand = new Random();
        while(set.size() != n) {
            int num = rand.nextInt(n);
            if(!set.contains(num)) {
                ans.add(num);
                set.add(num);
            }
        }
        return ans;
    }



}
