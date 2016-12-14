package MFCC;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;

import java.util.ArrayList;

// MODULE DESCRIPTEURS MFCC - ESCOULAN & TACONET

public class Mfcc {

    private int samplePerFrame; // nombre d'échantillons par fenêtre (N dans le pseudo code)

    public Mfcc(int samplePerFrame) {
        this.samplePerFrame = samplePerFrame;
    }

    //découpe le signal d'entrée en fenêtres et applique la fonction de hamming à chaque fenêtre
    public double[][] windowing(double[] signal) {

        int frameNumb = 2 * signal.length / samplePerFrame - 1; // nombre de fenêtres

        double[][] framedSignal = new double[frameNumb][samplePerFrame];
        // les lignes correspondent aux fenêtres du signal,
        // les colonnes correspondent aux échantillons pour chaque fenêtre

        for (int i = 0; i < frameNumb; i++) {
            // remplissage de la matrice
            int departNumb = (i * samplePerFrame / 2);
            System.arraycopy(signal, departNumb, framedSignal[i], 0, samplePerFrame);
        }

        double[] hammingWindow = new double[samplePerFrame + 1];

        for (int i = 1; i <= samplePerFrame; i++) {
            hammingWindow[i] = (float) (0.54 - 0.46 *
                    (Math.cos(2 * Math.PI * i / samplePerFrame)));
        }

        for (int n = 0; n < frameNumb; n++) {
            //chaque fenêtre est multipliée par la fonction de hamming
            for (int i = 0; i < samplePerFrame; i++) {
                framedSignal[n][i] = (framedSignal[n][i]) * hammingWindow[i + 1];
            }
        }

        return framedSignal;
    }

    // Fonction qui calcule des MFCC pour une fenêtre
    public double[] doMFCC(double[] frame) {

        double[] fftModule = magnitudeSpectrum(frame); // module des éléments de la TF discrète

        double f[] = nonLinearTransformation(fftModule); // tableau des log(X(k))

        double[] cepc = inverseMagnitudeSpectrum(f); // TF inverse et complétion par 0

        double[] coefficients = new double[13];
        //dans ce tableau on ne garde que les 13 coefficients cepstraux

        System.arraycopy(cepc, 0, coefficients, 0, coefficients.length);

        return coefficients;

    }

    public double[] createVector(double[] signal, double bpm) {
        double[] result = new double[14];
        ArrayList<double[]> ensemble = new ArrayList<>();
        double[][] matrix = windowing(signal);

        for (double[] aMatrix : matrix) {
            ensemble.add(doMFCC(aMatrix));
        }

        for (int y = 0; y < 13; y++) {
            double somme = 0;
            for (int x = 0; x < ensemble.size(); x++) {
                somme += (ensemble.get(x))[y];
            }
            somme = somme / ensemble.size();
            result[y] = somme;
        }
        result[13] = bpm;
        return result;

    }

    //Fonction qui renvoie les modules des coefficients de la TF
    public double[] magnitudeSpectrum(double frame[]) {

        Complex[] input = new Complex[(int) Math.pow(2, 11)];

        for (int u = 0; u < Math.pow(2, 11); u++) {
            if (u < frame.length) {
                input[u] = new Complex(frame[u], 0);
            } else {
                input[u] = new Complex(0, 0);
            }
        }

        FastFourierTransformer fft = new FastFourierTransformer(DftNormalization.UNITARY);
        Complex[] result = fft.transform(input, TransformType.FORWARD);

        double[] abs = new double[frame.length];

        for (int j = 0; j < frame.length; j++) {
            abs[j] = Math.sqrt(Math.pow(result[j].getReal(), 2) +
                    Math.pow(result[j].getImaginary(), 2));
        }

        return abs;
    }

    // Fonction qui renvoie les modules des coefficients de la TF inverse
    public double[] inverseMagnitudeSpectrum(double frame[]) {

        Complex[] input = new Complex[(int) Math.pow(2, 11)];

        for (int u = 0; u < Math.pow(2, 11); u++) {
            if (u < frame.length) {
                input[u] = new Complex(frame[u], 0);
            } else {
                input[u] = new Complex(0, 0);
            }
        }

        FastFourierTransformer fft = new FastFourierTransformer(DftNormalization.UNITARY);
        Complex[] result = fft.transform(input, TransformType.INVERSE);

        Complex[] abs = new Complex[frame.length];

        for (int j = 0; j < 13; j++) {
            if (j < 13) {
                abs[j] = result[j];
            } else {
                abs[j] = new Complex(0, 0);
            }
        }

        double[] abs1 = new double[frame.length];

        for (int j = 0; j < abs.length; j++) {
            if (j < 13) {
                abs1[j] = abs[j].getReal();
            } else {
                abs1[j] = 0;
            }
        }
        return abs1;
    }

    // Fonction qui fait le log des éléments de la transformée
    public double[] nonLinearTransformation(double frame[]) {

        double f[] = new double[frame.length];
        for (int i = 0; i < frame.length; i++) {

            f[i] = Math.log(100 * frame[i]);
        }
        return f;
    }
}

