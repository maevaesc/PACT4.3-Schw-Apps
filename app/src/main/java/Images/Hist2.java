package Images;

import java.util.ArrayList;

import Cocktails.Cocktail;

public abstract class Hist2 {

    // Fonction utilisée pour calculer l'histogramme d'une image
    public static double[] histCompact(ArrayList<Pixel> pixels) {

        double rouge = 0, orange = 0, jaune = 0, vert = 0, bleu = 0, rose = 0, noir = 0,
                blanc = 0, gris = 0, count;

        for (int i = 0; i < pixels.size(); i++) {
            double theta = pixels.get(i).getH();
            double s = pixels.get(i).getS();
            double v = pixels.get(i).getV();

            if (v < 20) {
                noir++;
            } else {
                if (v < 80 && s < 20) {
                    gris++;
                } else {
                    if (s < 20) {
                        blanc++;
                    } else {
                        if (theta < 20 || theta > 350)
                            rouge = rouge + 1;

                        if (10 < theta && theta < 50)
                            orange = orange + 1;

                        if (40 < theta && theta < 90)
                            jaune++;

                        if (85 < theta && theta < 185)
                            vert++;

                        if (175 < theta && theta < 275)
                            bleu++;

                        if (265 < theta && theta < 350)
                            rose++;
                    }
                }
            }
        }

        count = rouge + orange + jaune + vert + bleu + rose + blanc + noir + gris;

        rouge = rouge / count;
        orange = orange / count;
        jaune = jaune / count;
        vert = vert / count;
        bleu = bleu / count;
        rose = rose / count;
        blanc = blanc / count;
        noir = noir / count;
        gris = gris / count;

        return new double[]{rouge, orange, jaune, vert, bleu, rose, blanc, gris, noir};
    }

    public static double distHistChi(double[] hist, Cocktail cocktail) {
        double[] histCocktail = cocktail.getHist();
        double disthist = 0;

        for (int i = 0; i < 9; i++) {
            if (!(hist[i] == 0.0 && histCocktail[i] == 0)) {
                disthist = disthist +
                        ((hist[i] - histCocktail[i]) *
                                (hist[i] - histCocktail[i])) /
                                (hist[i] + histCocktail[i]);
            }
        }
        return disthist;
    }
}