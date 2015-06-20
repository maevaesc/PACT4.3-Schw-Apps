package Adaboost;

public class Properties {

    WeakClassifier h = new WeakClassifier();
    double e;

    public Properties(WeakClassifier h, double e) {
        this.h = h;
        this.e = e;
    }

    public void setBoolean(double e) {
        this.e = e;
    }

    public double getError() {
        return this.e;
    }

    public WeakClassifier getClassifier() {
        return this.h;
    }

}

