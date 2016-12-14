package org.marsapact.schwapps;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

// Choix de la musique : selection d'une musique existante (en .WAV !!!!)
public class MusicActivity extends Activity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int bufferSize = 5 * 44100;
        // On ne prend qu'un échantillon de 5 secondes de la musique d'entrée
        Data.setBufferSize(bufferSize);

        Intent intent1 = new Intent();

        // On force le format .WAV
        intent1.setType("audio/wav");

        intent1.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent1, "Choix de musique"), 1);
        onResume();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Uri selectedAudioUri = data.getData();
                String selectedAudioPath = getPathAudio(selectedAudioUri);
                Data.setMusicPathFile(selectedAudioPath);
                Toast.makeText(this, "Adresse de la musique :\n" +
                        selectedAudioPath, Toast.LENGTH_LONG);
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Aucune musique récupérée !", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "La récupération de musique a échoué !", Toast.LENGTH_LONG).show();
            }
        }
        finish();
    }

    private String getPathAudio(Uri uriAudio) {
        String selectedAudioPath;
        String[] projection = {MediaStore.Audio.Media.DATA};

        Cursor cursor = getContentResolver().query(uriAudio, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
            cursor.moveToFirst();
            selectedAudioPath = cursor.getString(column_index);
            cursor.close();
        } else {
            selectedAudioPath = null;
        }

        if (selectedAudioPath == null) {
            selectedAudioPath = uriAudio.getPath();
        }

        return selectedAudioPath;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_music, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}