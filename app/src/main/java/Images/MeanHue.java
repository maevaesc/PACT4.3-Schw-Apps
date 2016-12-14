package Images;

import java.util.ArrayList;

import Cocktails.Cocktail;

public abstract class MeanHue {

    public static double meanHue(ArrayList<Pixel> pixels) {

        // initialisation des sommes des vecteurs x et y et du nombre de vecteurs comptés
        double sommex = 0;
        double sommey = 0;
        int taille = 0;

        for (int i = 0; i < pixels.size(); i++) {

            double theta = pixels.get(i).getH();
            double s = pixels.get(i).getS();
            double v = pixels.get(i).getV();

            if (!(v < 20 || s < 20)) {
                theta = theta * (Math.PI) / 180;

                // passage en coordonnées cartésiennes
                double x = Math.cos(theta);
                double y = Math.sin(theta);
                taille++;

                sommex = sommex + x;
                sommey = sommey + y;
            }
        }

        double meanhue;

        if (taille != 0) {
            double meanx = sommex / taille;
            double meany = sommey / taille;

            meanhue = Math.atan2(meany, meanx);
            if (meanhue < 0)
                meanhue = (meanhue + 2 * Math.PI) * (180 / Math.PI);
            else
                meanhue = meanhue * (180 / Math.PI);

        } else {
            meanhue = 0;
        }
        return meanhue;
    }

    // DISTANCE ENTRE DEUX TEINTES MOYENNES
    public static double distHue(double hue, Cocktail cocktail) {

        //Soient deux valeurs de teinte moyenne, hue1 et hue2, calculées comme ci-dessus
        double hueCocktail = cocktail.getTeinte();

        double result = Math.min(Math.abs(hue - hueCocktail), 360 - Math.abs(hue - hueCocktail));
        result = result / 180;
        return result;
    }
}