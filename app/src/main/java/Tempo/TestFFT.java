package Tempo;

import edu.emory.mathcs.jtransforms.fft.DoubleFFT_1D;

public class TestFFT {
    private static double sqr(double x) {
        return x * x;
    }

    public static double[] fftAutoCorrelation(double[] x, double[] ac) {
        int n = x.length;
        DoubleFFT_1D fft = new DoubleFFT_1D(n);
        fft.realForward(x);
        ac[0] = sqr(x[0]);
        ac[1] = sqr(x[1]);

        for (int i = 2; i < n - 1; i += 2) {
            ac[i] = sqr(x[i]) + sqr(x[i + 1]);
            ac[i + 1] = 0;
        }

        DoubleFFT_1D ifft = new DoubleFFT_1D(n);
        ifft.realInverse(ac, true);
        return ac;
    }
}