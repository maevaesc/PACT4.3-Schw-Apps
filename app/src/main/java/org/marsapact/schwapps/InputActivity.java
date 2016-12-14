package org.marsapact.schwapps;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import static org.marsapact.schwapps.R.id.b_image;
import static org.marsapact.schwapps.R.id.b_photo;
import static org.marsapact.schwapps.R.id.imagePathFile;
import static org.marsapact.schwapps.R.id.nextstep1;

// Choix de l'image
public class InputActivity extends Activity {

    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Data.setImagePathFile(null);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);

        Button imageButton = (Button) findViewById(b_image);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent image = new Intent(InputActivity.this, ImageActivity.class);
                startActivity(image);
            }
        });

        Button photoButton = (Button) findViewById(b_photo);
        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent camera = new Intent(InputActivity.this, CameraActivity.class);
                startActivity(camera);
            }
        });

        Button next = (Button) findViewById(nextstep1);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextIntent = new Intent(InputActivity.this, Input2Activity.class);
                if (Data.getImagePathFile() == null) {
                    Toast.makeText(context, "Veuillez choisir une image", Toast.LENGTH_LONG).show();
                } else {
                    startActivity(nextIntent);
                }
            }
        });

        TextView musicPath = (TextView) findViewById(imagePathFile);
        musicPath.setText(Data.getImagePathFile());
    }

    @Override
    public void onResume() {
        super.onResume();

        TextView musicPath = (TextView) findViewById(imagePathFile);
        musicPath.setText(Data.getImagePathFile());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_adventure_mode, menu);
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