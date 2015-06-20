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

import static org.marsapact.schwapps.R.id.b_enregistrement;
import static org.marsapact.schwapps.R.id.b_musique;
import static org.marsapact.schwapps.R.id.musicPathFile;
import static org.marsapact.schwapps.R.id.nextstep2;

// Choix de la musique
public class Input2Activity extends Activity {

    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input2);

        Data.setMusicPathFile(null);
        Button musicButton = (Button) findViewById(b_musique);
        musicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent music = new Intent(Input2Activity.this, MusicActivity.class);
                startActivity(music);
            }
        });

        Button recordButton = (Button) findViewById(b_enregistrement);
        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent recording = new Intent(Input2Activity.this, RecordingActivity.class);
                startActivity(recording);
            }
        });

        Button next2Button = (Button) findViewById(nextstep2);
        next2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent next2Intent = new Intent(Input2Activity.this, LoadingScreenActivity.class);

                if (Data.getMusicPathFile() == null) {
                    Toast.makeText(context, "Veuillez choisir une musique", Toast.LENGTH_LONG).show();
                } else {
                    startActivity(next2Intent);
                }
            }
        });
    }

    public void onResume() {
        super.onResume();

        TextView path = (TextView) findViewById(musicPathFile);
        path.setText(Data.getMusicPathFile());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_input2, menu);
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
