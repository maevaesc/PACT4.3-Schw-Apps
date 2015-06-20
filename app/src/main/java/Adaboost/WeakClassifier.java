package Adaboost;

public class WeakClassifier {

    int j;
    double s;
    boolean b;

    public WeakClassifier(int j, double s, boolean b) {
        this.j = j;
        this.s = s;
        this.b = b;
    }

    public WeakClassifier() {
    }

    public void setBoolean(boolean b) {
        this.b = b;
    }

    public boolean WeakPrediction(double[] mfcc) {
        return (b && mfcc[j] > s) || (!b && mfcc[j] < s) || (mfcc[j] == s);
    }

    public double Error(double[] d, Boolean[] y, double[][] baseTest) {

        double result;

        result = 0;
        for (int i = 0; i < baseTest.length; i++) {
            if (y[i] != this.WeakPrediction(baseTest[i])) {
                result += d[i];
            }
        }

        return result;
    }
}
