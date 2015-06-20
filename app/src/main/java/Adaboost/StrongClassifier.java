package Adaboost;

import java.util.ArrayList;

public class StrongClassifier {

    ArrayList<WeakClassifier> h;
    ArrayList<Double> alpha;

    public StrongClassifier(ArrayList<WeakClassifier> h, ArrayList<Double> alpha) {
        this.h = h;
        this.alpha = alpha;
    }

    public Double StrongPrediction(int t, double[] mfcc) {
        double result = 0;
        for (int i = 0; i < t; i++) {
            if (h.get(i).WeakPrediction(mfcc)) {
                result += alpha.get(i);
            }
        }
        return result;
    }
}
