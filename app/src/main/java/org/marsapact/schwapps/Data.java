package org.marsapact.schwapps;

// Stockage des chemins vers l'image et la musique enregistrées
public class Data {

    private static String musicPathFile = null;
    private static String imagePathFile = null;
    private static int bufferSize = 0;

    public static int getBufferSize() {
        return bufferSize;
    }

    public static void setBufferSize(int bufferSize) {
        Data.bufferSize = bufferSize;
    }

    public static String getImagePathFile() {
        return imagePathFile;
    }

    public static void setImagePathFile(String imagePathFile) {
        Data.imagePathFile = imagePathFile;
    }

    public static String getMusicPathFile() {
        return musicPathFile;
    }

    public static void setMusicPathFile(String musicPathFile) {
        Data.musicPathFile = musicPathFile;
    }
}

