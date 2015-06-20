package Tempo;

import org.apache.commons.math3.complex.Complex;

public class PlayHamming {
    Complex[] vectorFiltered;

    public PlayHamming(Complex[] complexHamming, double[] arrayDouble) {
        int longueurHamming = complexHamming.length;
        int longueurDouble = arrayDouble.length;
        vectorFiltered = new Complex[longueurDouble - longueurHamming];

        for (int i = longueurHamming; i < longueurDouble; i++) {
            vectorFiltered[i - longueurHamming] = crossProduct(complexHamming, underVector(longueurHamming, i, arrayDouble));
        }
    }

    public final Complex[] getVectorFiltered() {
        return vectorFiltered;
    }

    private Complex crossProduct(Complex[] complexHamming, double[] doubleArray) {
        Complex sum;
        double real = 0;
        double img = 0;

        int longueurHamming = complexHamming.length;
        int longueurDouble = doubleArray.length;

        if (longueurHamming == longueurDouble) {
            for (int i = 0; i < longueurDouble - 1; i++) {
                real += complexHamming[i].getReal() * doubleArray[(longueurDouble - i) - 1];
                img += complexHamming[i].getImaginary() * doubleArray[(longueurDouble - i) - 1];
            }
        } else {
            System.err.println("Erreur: dimensions invalides");
        }
        sum = new Complex(real, img);

        return sum;
    }

    private double[] underVector(int longueurHamming, int position, double[] doubleArray) {
        double[] out = new double[longueurHamming];
        System.arraycopy(doubleArray, position - longueurHamming, out, position - longueurHamming - position + longueurHamming, position - (position - longueurHamming));
        return out;
    }
}
