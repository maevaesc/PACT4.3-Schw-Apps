package org.marsapact.schwapps;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import java.util.ArrayList;

import Adaboost.StrongClassifier;
import Cocktails.Cocktail;
import Images.Filtre;
import Images.Pixel;
import Tempo.OpenFichier;

import static Cocktails.DataBase.masterList;
import static Cocktails.DataBase.setCocktailFinal;
import static android.graphics.Color.blue;
import static android.graphics.Color.green;
import static android.graphics.Color.red;

/* Lors de l'exécution de cette activité, le système Android effectue les calculs
   qui vont permettre de déterminer le cocktail adéquat */

// MODULE TESTS & INTEGRATION - COQUOIN & GALAS

@SuppressWarnings("all")
public class LoadingScreenActivity extends Activity {

    private ProgressDialog progressDialog;
    private String genre;
    private double bpm = 60;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new LoadViewTask().execute();
    }

    public double getBpm() {
        return bpm;
    }

    private class LoadViewTask extends AsyncTask<Void, Integer, Void> {
        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(LoadingScreenActivity.this, "Chargement...",
                    "Calcul du cocktail, veuillez patienter...", false, false);
        }

        // Le code qui suit s'exécute en tâche d'arrière-fond
        @Override
        protected Void doInBackground(Void... params) {

            synchronized (this) {
                try {

                    /* DEBUT du chargement en mémoire de la musique d'entrée
                    Utilisation de la classe OpenFichier dans le package Tempo
                    qui convertit le .WAV enregistrée en un tableau de "double" via la
                    classe WavFile

                    MODULE CAPTURE, LECTURE ET TRAITEMENT DE FICHIERS AUDIO

                    */
                    OpenFichier open = new OpenFichier();
                    double[] buffer = open.getBuffer();

                    // System.out.println("Buffer enregistré");

                    /* FIN du chargement en mémoire de la musique d'entrée */

                    /* DEBUT du traitement de la musique d'entrée
                    On calcule le vecteur MFCC de la musique d'entrée qui prend en compte
                    également son BPM. Les classes correspondantes sont présentes dans les packages
                    Tempo et MFCC.

                    MODULE DESCRIPTEURS MFCC & MODULE DETECTION DU TEMPO

                    */

                    // samplePerFrame correspond au nombre d'échantillons par fenêtre
                    int samplePerFrame = (int) (0.02 * 44100);

                    // System.out.println("samplePerFrame =" + samplePerFrame);

                    // On prend des fenetres de 20ms, ce qui nous donne 882 échantillons
                    // par fenêtre. Le vecteur MFCC est formé de 13 composantes
                    MFCC.Mfcc mfcc = new MFCC.Mfcc(samplePerFrame);

                    // Création du vecteur MFCC de 13 coefficients (avec le calcul de la BPM)
                    double[] coeffs = mfcc.createVector(buffer, getBpm());

                    // System.out.println("Tableau =" + coeffs[0] + coeffs[1]);

                    /* FIN du traitement de la musique d'entrée */

                    /* DEBUT de la classification de la musique d'entrée
                    Cette partie, grâce aux descripteurs MFCC de la musique, permet de
                    déterminer le genre de la musique

                    MODULE CLASSIFICATION ADABOOST

                    */

                    AssetManager asset = getAssets();
                    Adaboost.Adaboost ada;
                    ada = new Adaboost.Adaboost(asset);

                    // Importation de la base de données Adaboost
                    ArrayList<StrongClassifier> strongClassifiers = ada.BuildStrongClassifiers();

                    // On obtient le genre auquel appartient la musique
                    int classe = ada.Test(coeffs, strongClassifiers);

                    switch (classe) {
                        case 0:
                            genre = "Jazz";
                            classe = 2;
                            break;
                        case 1:
                            genre = "Pop";
                            classe = 4;
                            break;
                        case 2:
                            genre = "Techno";
                            classe = 6;
                            break;
                        case 3:
                            genre = "Latina";
                            classe = 0;
                            break;
                        case 4:
                            genre = "Rock";
                            classe = 5;
                            break;
                        case 5:
                            genre = "Disco";
                            classe = 3;
                            break;
                        case 6:
                            genre = "Classique";
                            classe = 1;
                            break;
                    }

                    // System.out.println("Genre =" +

                    final int finalClasse = classe;

                    /* FIN de la classification de la musique d'entrée */

                    /* DEBUT du traitement de l'image d'entrée
                    Détermination de la teinte moyenne et de l'histogramme de teintes de l'image

                    MODULE TRAITEMENT DES IMAGES

                    */

                    // On récupère le chemin de l'image enregistrée
                    String path = Data.getImagePathFile();

                    // Sous-échantillonnage de l'image d'entrée (On prend 1 pixel sur 11^2)
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 11;
                    Bitmap image = BitmapFactory.decodeFile(path, options);
                    // Fin du sous-échantillonnage

                    int w = image.getWidth();
                    int h = image.getHeight();
                    ArrayList<Pixel> pixels = new ArrayList<>();

                    // Conversion de l'image en tableau de floats
                    float[] hsv = new float[h * w];
                    for (int i = 0; i < w; i++) {
                        for (int j = 0; j < h; j++) {
                            int c = image.getPixel(i, j);
                            Color.RGBToHSV(red(c), green(c), blue(c), hsv);
                            pixels.add(new Pixel());
                            pixels.get(i * h + j).setH(hsv[0]);
                            pixels.get(i * h + j).setS(hsv[1] * 100);
                            pixels.get(i * h + j).setV(hsv[2] * 100);
                        }
                    }

                    // Réduction de la liste des cocktails à partir de l'analyse de l'image
                    final ArrayList<Cocktail> cocktailListTriImage =
                            Filtre.filtreHisto_Teinte(masterList(), pixels, 9);

                     /* FIN du traitement de l'image d'entrée */

                    /* Tirage d'un nombre aléatoire qui prend part légèrement
                    dans le choix du cocktail */
                    double tirage = -1 + 15 * Math.random();

                    double N = ((double) finalClasse / 7) + (tirage / 100);

                    // System.out.println("Genre = " + genre);
                    // System.out.println("N = " + N);

                    /* Permutation des éléments de l'arrayList cocktailListTriImage en les classant
                    par liste de cocktail du plus faible au plus fort.
                    Puis élection du cocktail final grâce au pourcentage qui correspond au nombre N
                    (indique une position dans la liste) */
                    Cocktail cocktailFinal = Images.Filtre.triForce(cocktailListTriImage, N);

                    // System.out.println("Cocktail= " + cocktailFinal.getNom());

                    // Mise à jour de la classe Data avec le cocktail final sélectionné
                    setCocktailFinal(cocktailFinal);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        }

        //Met à jour la progression
        @Override
        protected void onProgressUpdate(Integer... values) {

            progressDialog.setProgress(values[0]);
        }

        //after executing the code in the thread
        @Override
        protected void onPostExecute(Void result) {

            // Méthode appelée lorsque les calculs sont terminés
            progressDialog.dismiss();

            Intent intent = new Intent(LoadingScreenActivity.this, CocktailActivity.class);
            startActivity(intent);
        }
    }

}  