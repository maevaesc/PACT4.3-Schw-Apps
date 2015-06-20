package Images;

import org.apache.commons.math3.complex.Complex;

import java.util.ArrayList;

import Cocktails.Cocktail;

// MODULE TRAITEMENT DES IMAGES - BARRANGOU & COQUOIN

public class Filtre {

    // Mise en place du filtre 1 qui renvoie le sous ensemble A
    public static ArrayList<Cocktail> filtreHisto_Teinte(ArrayList<Cocktail> cocktailList,
                                                         ArrayList<Pixel> pixels, int taille) {

        Complex[] underList = new Complex[cocktailList.size()];
        double real;
        double[] hist = Hist2.histCompact(pixels);
        double hue = MeanHue.meanHue(pixels);
        for (int i = 0; i < cocktailList.size(); i++) {
            real = (Hist2.distHistChi(hist, cocktailList.get(i))) + MeanHue.distHue(hue,
                    cocktailList.get(i));
            underList[i] = new Complex(real, i);
        }

        cocktailList = reArrangeCoctail(cocktailList, tri(underList));

        ArrayList<Cocktail> cocktails = new ArrayList<>();

        for (int i = 0; i < taille; i++) {
            cocktails.add(cocktailList.get(i));
        }

        return cocktails;
    }

    public static Complex[] tri(Complex[] valeurComparaison) {
        int jiem = 0;
        int longeur = valeurComparaison.length;
        double min;

        for (int i = 0; i < longeur; i++) {
            min = 10.0;
            for (int j = i; j < longeur; j++) {
                if (min > valeurComparaison[j].getReal()) {
                    min = valeurComparaison[j].getReal();
                    jiem = j;
                }
            }
            exchange(valeurComparaison, i, jiem);
        }
        return valeurComparaison;
    }

    public static void exchange(Complex[] list, int i, int j) {

        Complex iem = list[i];
        Complex jiem = list[j];
        list[j] = iem;
        list[i] = jiem;
    }

    public static ArrayList<Cocktail> reArrangeCoctail(ArrayList<Cocktail> cocktailList,
                                                       Complex[] arrayComplex) {
        ArrayList<Cocktail> cocktailArranged = new ArrayList<>();
        for (Complex anArrayComplex : arrayComplex) {
            cocktailArranged.add(cocktailList.get((int) anArrayComplex.getImaginary()));
        }
        return cocktailArranged;
    }

    // Fonction qui trie les cocktails par ordre de force
    public static Cocktail triForce(ArrayList<Cocktail> cocktails, double N) {
        ArrayList<Cocktail> doux = new ArrayList<>();
        ArrayList<Cocktail> leger = new ArrayList<>();
        ArrayList<Cocktail> medium = new ArrayList<>();
        ArrayList<Cocktail> fort = new ArrayList<>();
        ArrayList<Cocktail> extreme = new ArrayList<>();
        ArrayList<Cocktail> fin = new ArrayList<>();

        for (int i = 0; i < cocktails.size(); i++) {
            if (cocktails.get(i).getDegres().equals("Doux")) {
                doux.add(cocktails.get(i));
            }
            if (cocktails.get(i).getDegres().equals("Leger")) {
                leger.add(cocktails.get(i));
            }
            if (cocktails.get(i).getDegres().equals("Medium")) {
                medium.add(cocktails.get(i));
            }
            if (cocktails.get(i).getDegres().equals("Fort")) {
                fort.add(cocktails.get(i));
            }
            if (cocktails.get(i).getDegres().equals("Extreme")) {
                extreme.add(cocktails.get(i));
            }
        }

        for (int i = 0; i < doux.size(); i++)
            fin.add(doux.get(i));
        for (int i = 0; i < leger.size(); i++)
            fin.add(leger.get(i));
        for (int i = 0; i < medium.size(); i++)
            fin.add(medium.get(i));
        for (int i = 0; i < fort.size(); i++)
            fin.add(fort.get(i));
        for (int i = 0; i < extreme.size(); i++)
            fin.add(extreme.get(i));

        return filtreSon(fin, N);
    }

    // Fonction qui détermine le cocktail final
    public static Cocktail filtreSon(ArrayList<Cocktail> cocktailList, double N) {
        int A = 0;
        if (N < 0) {
            A = 0;
        }
        if (N > 0 && N < cocktailList.size()) {
            A = (int) Math.floor(cocktailList.size() * N);
        }
        if (N > cocktailList.size()) {
            A = cocktailList.size() - 1;
        }
        return cocktailList.get(A);
    }
}
