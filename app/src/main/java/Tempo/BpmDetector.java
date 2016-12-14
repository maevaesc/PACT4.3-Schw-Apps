package Tempo;

import org.apache.commons.math3.complex.Complex;

@SuppressWarnings("all")
public class BpmDetector {
    private double bpm;

    public BpmDetector(double[] buffer) {
        // Application du banc de filtre
        Complex[][] projs = Algo.bank(buffer);

        double[][] realProjs = new double[9][];
        for (int i = 0; i < 9; i++) {
            realProjs[i] = Algo.realPart(projs[i]);
        }

        double[][] envs = new double[realProjs.length][];

        for (int i = 0; i < projs.length; i++) {
            envs[i] = Algo.enveloppe(realProjs[i]);
        }

        double[][] dsEnvs = new double[envs.length][];

        for (int i = 0; i < dsEnvs.length; i++) {
            dsEnvs[i] = Algo.downSampling(envs[i]);
        }

        double[][] ders = new double[dsEnvs.length][];

        for (int i = 0; i < ders.length; i++) {
            ders[i] = Algo.derivateur(dsEnvs[i]);
        }

        //Détermination de la longueur minimale des vecteurs
        int minimalLength = ders[0].length;
        for (double[] der1 : ders) {
            if (der1.length < minimalLength) {
                minimalLength = der1.length;
            }
        }

        double[] sum = new double[minimalLength];

        for (int i = 0; i < minimalLength; i++) {
            for (double[] der : ders) {
                sum[i] += der[i];
            }
        }

        // Auto-corrélation via FFT
        double[] xcorr = Algo.FFTautoCorrelation(sum);

        bpm = Algo.getBeat(xcorr, 44100);
    }

    public double getBpm() {
        return bpm;
    }
}
