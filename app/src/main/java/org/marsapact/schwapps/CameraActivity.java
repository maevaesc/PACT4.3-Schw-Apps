package org.marsapact.schwapps;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.File;

// Choix de l'image : prise d'une photo via la caméra du téléphone
public class CameraActivity extends Activity {

    int photoResult = 100;
    private File mFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFile = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "/photo.bmp");
        Uri fileUri = Uri.fromFile(mFile);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(intent, photoResult);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == photoResult) {
            if (resultCode == RESULT_OK) {
                Data.setImagePathFile(mFile.getAbsolutePath());
                Toast.makeText(this, "Adresse de l'image :\n" +
                        mFile.getAbsolutePath(), Toast.LENGTH_LONG).show();
                finish();
            } else if (resultCode == RESULT_CANCELED) {

                Toast.makeText(this, "Aucune photo n'a été prise !", Toast.LENGTH_LONG).show();
                finish();
            } else {
                Toast.makeText(this, "La prise de photo a échoué !", Toast.LENGTH_LONG).show();
                finish();
            }
        } else {
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_camera, menu);
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
