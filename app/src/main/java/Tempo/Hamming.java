package Tempo;

import org.apache.commons.math3.complex.Complex;

public class Hamming {
    int N;
    int N1;
    double v0;
    private Complex[] vectorHammingExp;

    public Hamming(final double alpha, final double Fe, final double f1, final double f2) {
        double v1 = f1 / Fe;
        double v2 = f2 / Fe;

        v0 = (v1 + v2) / 2; // Fréquence réduite centrale du passe-bande

        N = (int) (4 / (v2 - v1));
        N1 = (int) (2 / (v2 - v1));


        if (f2 < f1) {
            System.err.println("Error: entrez f2 > f1");
        } else {
            if (f1 != 0) {
                vectorHammingExp = new Complex[N];
                for (int i = 0; i < N; i++) {
                    vectorHammingExp[i] = new Complex((alpha - (1 - alpha) * Math.cos((2 * Math.PI * i) / (N - 1))) * Math.cos(2 * Math.PI * v0 * i), (alpha - (1 - alpha) * Math.cos((2 * Math.PI * i) / (N - 1))) * Math.sin(2 * Math.PI * v0 * i));
                }
            } else {
                vectorHammingExp = new Complex[N1];
                for (int i = 0; i < N1 - 1; i++) {
                    vectorHammingExp[i] = new Complex((alpha - (1 - alpha) * Math.cos((2 * Math.PI * i) / (N1 - 1))), 0);
                }
            }
        }
    }

    public final Complex[] getVectorHammingExp() {
        return vectorHammingExp;
    }
}






