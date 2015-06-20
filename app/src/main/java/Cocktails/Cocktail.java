package Cocktails;

// Classe qui décrit les caractéristiques d'un cocktail donné
public class Cocktail {

    private String nom = null;
    private String degres = null;
    private String adresse = null;
    private double teinte = 0;
    private double[] hist = {0, 0, 0, 0, 0, 0};

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDegres() {
        return degres;
    }

    public void setDegres(String degres) {
        this.degres = degres;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public double getTeinte() {
        return teinte;
    }

    public void setTeinte(double teinte) {
        this.teinte = teinte;
    }

    public double[] getHist() {
        return hist;
    }

    public void setHist(double[] hist) {
        this.hist = hist;
    }

}
