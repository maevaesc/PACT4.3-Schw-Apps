package Tempo;

import org.apache.commons.math3.complex.Complex;

// MODULE DETECTION DU TEMPO - BOUTIN & CHATON & COQUOIN & GALAS

public class Algo {
    public static Complex[][] bank(double[] input) {
        Complex[][] output = new Complex[9][];

        Hamming h0 = new Hamming(0.56, 44100, 0, 250);
        Hamming h1 = new Hamming(0.56, 44100, 250, 500);
        Hamming h2 = new Hamming(0.56, 44100, 500, 750);
        Hamming h3 = new Hamming(0.56, 44100, 750, 1000);
        Hamming h4 = new Hamming(0.56, 44100, 1000, 2000);
        Hamming h5 = new Hamming(0.56, 44100, 2000, 4000);
        Hamming h6 = new Hamming(0.56, 44100, 4000, 6000);
        Hamming h7 = new Hamming(0.56, 44100, 6000, 8000);
        Hamming h8 = new Hamming(0.56, 44100, 8000, 20000);

        Complex[] c1 = h0.getVectorHammingExp();
        Complex[] c2 = h1.getVectorHammingExp();
        Complex[] c3 = h2.getVectorHammingExp();
        Complex[] c4 = h3.getVectorHammingExp();
        Complex[] c5 = h4.getVectorHammingExp();
        Complex[] c6 = h5.getVectorHammingExp();
        Complex[] c7 = h6.getVectorHammingExp();
        Complex[] c8 = h7.getVectorHammingExp();
        Complex[] c9 = h8.getVectorHammingExp();

        // Renormalisation des vecteurs de Hamming
        for (int i = 0; i < c1.length - 1; i++) {
            double real = c1[i].getReal() * Math.pow(10, -(45.86) / 20);
            double imaginary = c1[i].getImaginary() * Math.pow(10, -(45.86) / 20);
            c1[i] = new Complex(real, imaginary);
        }

        for (int i = 0; i < c2.length - 1; i++) {
            double real = c2[i].getReal() * Math.pow(10, -(45.89) / 20);
            double imaginary = c2[i].getImaginary() * Math.pow(10, -(45.89) / 20);
            c2[i] = new Complex(real, imaginary);
        }

        for (int i = 0; i < c3.length - 1; i++) {
            double real = c3[i].getReal() * Math.pow(10, -(45.88) / 20);
            double imaginary = c3[i].getImaginary() * Math.pow(10, -(45.89) / 20);
            c3[i] = new Complex(real, imaginary);
        }

        for (int i = 0; i < c4.length - 1; i++) {
            double real = c4[i].getReal() * Math.pow(10, -(45.88) / 20);
            double imaginary = c4[i].getImaginary() * Math.pow(10, -(45.89) / 20);
            c4[i] = new Complex(real, imaginary);
        }

        for (int i = 0; i < c5.length - 1; i++) {
            double real = c5[i].getReal() * Math.pow(10, -(33.79) / 20);
            double imaginary = c5[i].getImaginary() * Math.pow(10, -(33.79) / 20);
            c5[i] = new Complex(real, imaginary);
        }

        for (int i = 0; i < c6.length - 1; i++) {
            double real = c6[i].getReal() * Math.pow(10, -(27.71) / 20);
            double imaginary = c6[i].getImaginary() * Math.pow(10, -(27.71) / 20);
            c6[i] = new Complex(real, imaginary);
        }

        for (int i = 0; i < c7.length - 1; i++) {
            double real = c7[i].getReal() * Math.pow(10, -(27.73) / 20);
            double imaginary = c7[i].getImaginary() * Math.pow(10, -(27.73) / 20);
            c7[i] = new Complex(real, imaginary);
        }

        for (int i = 0; i < c8.length - 1; i++) {
            double real = c8[i].getReal() * Math.pow(10, -(27.74) / 20);
            double imaginary = c8[i].getImaginary() * Math.pow(10, -(27.74) / 20);
            c8[i] = new Complex(real, imaginary);
        }

        for (int i = 0; i < c9.length - 1; i++) {
            double real = c9[i].getReal() * Math.pow(10, -(11.27) / 20);
            double imaginary = c9[i].getImaginary() * Math.pow(10, -(11.27) / 20);
            c9[i] = new Complex(real, imaginary);
        }
        // Fin de la renormalisation


        // Application du banc de filtre
        output[0] = new PlayHamming(c1, input).getVectorFiltered();
        output[1] = new PlayHamming(c2, input).getVectorFiltered();
        output[2] = new PlayHamming(c3, input).getVectorFiltered();
        output[3] = new PlayHamming(c4, input).getVectorFiltered();
        output[4] = new PlayHamming(c5, input).getVectorFiltered();
        output[5] = new PlayHamming(c6, input).getVectorFiltered();
        output[6] = new PlayHamming(c7, input).getVectorFiltered();
        output[7] = new PlayHamming(c8, input).getVectorFiltered();
        output[8] = new PlayHamming(c9, input).getVectorFiltered();
        // Fin de l'application du banc de filtre

        return output;
    }

    public static double[] enveloppe(double[] input) {
        double[] output = new double[input.length];

        // Méthode du réservoir (cf. OASIS)
        for (int i = 1; i < input.length; i++) {
            output[i] = (1 - 0.007) * output[i - 1] + (0.007) * Math.pow(input[i], 2);
        }
        return output;
    }

    public static double[] downSampling(double[] input) {
        int length = input.length;
        double[] downsampledArray = new double[length / 100 - 1];

        for (int i = 0; i < downsampledArray.length; i++) {
            downsampledArray[i] = input[i * 100];
        }

        return downsampledArray;
    }

    public static double[] derivateur(double[] input) {
        double[] output = new double[input.length - 24];
        double sum;
        for (int i = 0; i < input.length - 24; i++) {
            sum = 0;
            for (int j = 0; j < 24; j++) {
                if (j < 12) {
                    sum += input[23 + i - j];
                } else {
                    sum -= input[23 + i - j];
                }

                output[i] = sum;

            }
        }
        return output;
    }

    // Auto-corrélation rapide via FFT
    public static double[] FFTautoCorrelation(double[] doubleArray) {
        int length = doubleArray.length;
        double[] xcorr = new double[length];
        xcorr = TestFFT.fftAutoCorrelation(doubleArray, xcorr);
        return xcorr;
    }

    public static double getBeat(double[] doubleArray, double Fs) {
        double max = 0.000001;
        int count = 0;
        double[] doubleArray2 = new double[doubleArray.length];
        for (int i = 0; i < doubleArray.length; i++) {
            if (i < 200) {
                doubleArray2[i] = 0;
            } else {
                doubleArray2[i] = doubleArray[i] / Math.log10(i);
            }
        }

        for (int i = 1; i < 1000; i++) {
            if (max < doubleArray2[i]) {
                max = doubleArray2[i];
                count = i;
            }
        }

         /* On divise par 100 pour compenser le
           sous-échantillonnage à la sortie du détecteur d'enveloppe */
        return (2 * 60 * Fs) / (100 * count);

    }

    public static double[] realPart(Complex[] arrayComplex) {
        double[] real = new double[arrayComplex.length];
        for (int i = 0; i < arrayComplex.length - 1; i++) {
            real[i] = arrayComplex[i].getReal();
        }
        return real;
    }
}
