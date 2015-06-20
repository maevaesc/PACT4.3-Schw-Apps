package org.marsapact.schwapps;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import Cocktails.Cocktail;
import Cocktails.DataBase;

import static org.marsapact.schwapps.R.id;

// MODULE TESTS & INTEGRATION - COQUOIN & GALAS

// Classe qui gère l'affichage du cocktail sélectionné
public class CocktailActivity extends Activity {

    private ShakeListener mShaker;
    private Cocktail cocktailFinal;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cocktail);

        final ImageView iv = (ImageView) findViewById(id.imageView);
        final TextView cocktailName = (TextView) findViewById(id.cocktailname);

        cocktailFinal = DataBase.getCocktailFinal();

        final Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        mShaker = new ShakeListener(this);
        mShaker.setOnShakeListener(new ShakeListener.OnShakeListener() {
            public void onShake() {
                vibe.vibrate(500);
                mShaker.pause();

                String imagePath = cocktailFinal.getAdresse();
                Uri path = Uri.parse("android.resource://org.marsapact.schwapps/drawable/"
                        + imagePath);

                iv.setImageResource(0);
                iv.setImageURI(path);

                cocktailName.setText(cocktailFinal.getNom());
            }
        });

        final Button menuButton = (Button) findViewById(id.b_menu);

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cocktail, menu);
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

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }
}
