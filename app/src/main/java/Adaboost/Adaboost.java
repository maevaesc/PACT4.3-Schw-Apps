package Adaboost;

import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

// MODULE CLASSIFICATION ADABOOST - ESCOULAN & TACONET

public class Adaboost {

    private static final int t = 100;
    public static int K = 7; // nombre de classe
    ArrayList<Integer> Y; // nombre d'étiquettes des exemples de la base de données
    Boolean[][] Y0;
    // tableau comportant dans chaque ligne les étiquettes associées à chaque exemples,
    // renvoie "true" si l'exemple appartient à la classe et "false" sinon
    private int exNumb; // nombre d'exemples utilisés pour construire des classifieurs forts
    private double[][] baseTest;

    public Adaboost(AssetManager asset) {

        // Chemin vers le fichier de la base de données d'exemples
        double[][] exemples = readFile(asset);

        Y = new ArrayList<>();

        baseTest = exemples;
        exNumb = baseTest.length;

        Y0 = new Boolean[7][exNumb];

        for (int i = 0; i < 5; i++) {
            Y.add(0);// Jazz
        }

        for (int i = 5; i < 10; i++) {
            Y.add(1);// Pop
        }

        for (int i = 10; i < 15; i++) {
            Y.add(2);// Techno
        }

        for (int i = 15; i < 20; i++) {
            Y.add(3);// Latina
        }

        for (int i = 20; i < 25; i++) {
            Y.add(4);// Rock
        }
        for (int i = 25; i < 30; i++) {
            Y.add(5);// Disco
        }
        for (int i = 30; i < 35; i++) {
            Y.add(6);// Classique
        }
    }

    public static double[][] readFile(AssetManager asset) {

        InputStream fr = null;
        BufferedReader br = null;
        double[][] res = new double[35][14];
        ArrayList<String> result = new ArrayList<>();

        try {
            fr = asset.open("examples.txt");
            br = new BufferedReader(new InputStreamReader(fr));

            for (String line = br.readLine(); line != null; line = br.readLine()) {

                result.add(line);
            }

            for (int j = 0; j < 35; j++) {
                for (int i = 0; i < 14; i++) {
                    String str;
                    if (j == 0) {
                        str = result.get(i);
                    } else {
                        str = result.get((j - 1) * 14 + i);
                    }
                    res[j][i] = Double.parseDouble(str);
                }
            }

        } catch (FileNotFoundException e) {
            System.err.println("FileNotFoundException");
        } catch (IOException e) {
            System.err.println("IOException");
        } catch (Exception e) {
            System.err.println("Unknown error");
            e.printStackTrace(System.err);
        } finally {
            if (fr != null)
                try {
                    fr.close();
                } catch (Exception ignored) {
                }
            // closes the fileReader if it is not empty
            if (br != null)
                try {
                    br.close();
                } catch (Exception ignored) {
                }
            // closes the BufferedReader if it is not empty
        }

        return res;
    }

    // fonction utilisée pour tester la classe d'un nouveau vecteur
    public int Test(double[] mfcc, ArrayList<StrongClassifier> strongSet) {

        double M = strongSet.get(0).StrongPrediction(t, mfcc);
        int classe = 0;
        for (int k = 1; k < K; k++) {
            if (strongSet.get(k).StrongPrediction(t, mfcc) > M) {
                M = strongSet.get(k).StrongPrediction(t, mfcc);
                classe = k;
            }
        }

        return classe;
    }

    // construction d'un ensemble de classifieurs forts pour toutes les classes
    public ArrayList<StrongClassifier> BuildStrongClassifiers() {
        ArrayList<StrongClassifier> result = new ArrayList<>();
        for (int k = 0; k < K; k++) {
            for (int j = 0; j < exNumb; j++) {
                Y0[k][j] = Y.get(j) == k;
            }

        }

        for (int k = 0; k < K; k++) {
            result.add(doAdaboost(k, t));
        }

        return result;
    }

    // Fonction qui construit un classifieur FORT pour la classe k
    public StrongClassifier doAdaboost(int k, int t) {

        double[][] D = new double[t][exNumb];
        ArrayList<Double> poids = new ArrayList<>();
        ArrayList<Double> norme = new ArrayList<>();
        ArrayList<WeakClassifier> h = new ArrayList<>();

        ArrayList<Double> e = new ArrayList<>();
        double init = 1.0 / exNumb;

        for (int j = 0; j < exNumb; j++) {
            D[0][j] = init;
        }

        for (int j = 0; j < t; j++) {

            double s1 = Sum(D[j]);

            if (j != 0) {
                for (int i = 0; i < exNumb; i++) {

                    D[j][i] = D[j][i] / s1;
                }
            }

            Properties weakClassifier = BuildWeakClassifier(k, D[j]);
            h.add(weakClassifier.getClassifier());
            e.add(weakClassifier.getError());


            if (e.get(j) > 0.5) {
                System.exit(0);
            }

            double erreur = e.get(j);

            double z = erreur / (1 - erreur);

            double x = Math.log10(1 / z);

            poids.add(x);
            norme.add(z);

            if (j != t - 1) {
                for (int i = 0; i < exNumb; i++) {

                    if (h.get(j).WeakPrediction(baseTest[i]) == Y0[k][i]) {
                        D[j + 1][i] = (D[j][i] * norme.get(j));
                    } else {
                        D[j + 1][i] = (D[j][i]);
                    }
                }
            }
        }
        return new StrongClassifier(h, poids);
    }

    public double Sum(double[] d) {
        double result = 0;
        for (double aD : d) {
            result += aD;
        }
        return result;
    }


    // Fonction qui construit un classifieur FAIBLE pour la classe k
    public Properties BuildWeakClassifier(int k, double[] d) {

        double e = 1;
        WeakClassifier h = new WeakClassifier();
        WeakClassifier h0;
        for (int j = 0; j < 14; j++) {


            for (int i = 0; i < exNumb; i++) {
                double e0;
                WeakClassifier h1 = new WeakClassifier(j, baseTest[i][j], true);
                double e1 = h1.Error(d, Y0[k], baseTest);
                WeakClassifier h2 = new WeakClassifier(j, baseTest[i][j], false);
                double e2 = h2.Error(d, Y0[k], baseTest);

                if (e1 < e2) {
                    h0 = h1;
                    e0 = e1;
                } else {
                    h0 = h2;
                    e0 = e2;
                }

                if ((i == 0 && j == 0) || ((e0 < e) && 0 < e0)) {
                    e = e0;
                    h = h0;
                }
            }
        }
        return new Properties(h, e);
    }
}