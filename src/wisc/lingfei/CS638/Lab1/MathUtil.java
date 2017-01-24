package wisc.lingfei.CS638.Lab1;

import java.util.*;

/**
 * Created by Lingfei on 2017-1-22.
 */
public class MathUtil {


    public static int stepFunc(Double input, Double bias) {
        if(input >= bias) {
            return 1;
        }
        return 0;
    }

    public static int stepFunc(Double input) {
        return stepFunc(input, 0.0);
    }

    public static String formatPercentage(double perc) {
        return String.format("%.2f%%", perc*100);
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
