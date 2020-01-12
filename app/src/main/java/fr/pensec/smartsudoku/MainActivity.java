/**
 * @author William PENSEC
 * @date 12/01/2020
 * @version 1.0
 **/
package fr.pensec.smartsudoku;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    /**
     * @description Gère la création de l'activité lors du lancement de l'application
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle(R.string.activiteMain);
    }

    /**
     * @description Permet de lancer une nouvelle partie
     * @param d
     */
    public void newParty(View d){
        Intent intent = new Intent(this, JeuActivity.class);
        startActivity(intent);
    }


    /**
     * @description Permet de lancer une nouvelle vue pour l'activité A Propos
     * @param d
     */
    public void about(View d){
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }
}
