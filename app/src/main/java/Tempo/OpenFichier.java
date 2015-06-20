package Tempo;

import org.marsapact.schwapps.Data;

import java.io.File;
import java.io.IOException;

public class OpenFichier {
    double[] buffer;
    double[] lag;
    int longueur;
    int nb_frame;

    public OpenFichier() throws IOException, WavFileException {
        nb_frame = Data.getBufferSize();

        // Open the wav file specified as the first argument
        WavFile wavFile = WavFile.openWavFile(new File(Data.getMusicPathFile()), nb_frame);

        // Display information about the wav file
        // wavFile.display();

        // Get the number of audio channels in the wav file
        int numChannels = wavFile.getNumChannels();

        longueur = wavFile.getBuffer().length;

        buffer = new double[longueur * numChannels];

        int framesRead;

        do {   // Read frames into buffer
            framesRead = wavFile.readFrames(buffer, longueur);
        }
        while (framesRead != 0);

        lag = new double[buffer.length];
        for (int i1 = 0; i1 < buffer.length; i1++) {
            lag[i1] = (double) i1;
        }
        wavFile.close();
    }

    public final double[] getBuffer() {
        return buffer;
    }
}
