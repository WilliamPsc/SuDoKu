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

public class AboutActivity extends AppCompatActivity {

    /**
     * @description S'exécute au lancement de la vue et permet d'afficher un titre et de charger le layout xml
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        getSupportActionBar().setTitle(R.string.activiteAbout);
    }

    /**
     * @description Prend en paramètre une vue et fini l'intent afin de revenir à la vue précédente (Main dans ce cas)
     * @param d
     */
    public void retour(View d){
        finish();
    }

    /**
     * @description Prend en paramètre une view et permet de lancer une nouvelle vue afin d'afficher les paramètres de l'application
     * @param s
     */
    public void settings(View s){
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
}
