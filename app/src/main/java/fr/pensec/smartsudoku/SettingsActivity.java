/**
 * @author William PENSEC
 * @date 12/01/2020
 * @version 1.0
 **/

package fr.pensec.smartsudoku;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    Switch sp2;
    Button sp;
    TextView tv11;
    Boolean chrono;

    /**
     * @description Permet d'afficher les différents éléments présents sur le layout
     * et de vérifier les valeurs des composants afin d'afficher différentes
     * valeurs en fonction
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setTitle(R.string.activitySettings);

        sp = findViewById(R.id.button7);
        sp2 = findViewById(R.id.switch2);
        tv11 = findViewById(R.id.textView11);
        sharedPref = this.getSharedPreferences("grille", MODE_PRIVATE);
        editor = sharedPref.edit();

        // On gère la valeur texte du switch selon ce qui est enregistré dans le fichier de sauvegarde.
        if(sharedPref.contains("chrono")){
            chrono = sharedPref.getBoolean("chrono", false);
            if(chrono){
                sp2.setText("Activé");
                sp2.setChecked(true);
            }else{
                sp2.setText("Non Actif");
                sp2.setChecked(false);
            }
            editor.commit();
        }
    }

    /**
     * @description Permet de supprimer les données sauvegardées par l'application
     * @param v
     */
    public void supprData(View v){
        /*// DEBUG
        String s1, s3;
        Integer s2;

        s1 = sharedPref.getString("sudoku", null);
        s2 = sharedPref.getInt("nbGrille", 30);
        s3 = sharedPref.getString("sudokuRef", null);
        Log.i("supprData", "Sudoku : " + s1);
        Log.i("supprData", "Numéro Grille :" + s2);
        Log.i("supprData", "SudokuRef : " + s3);
        // FIN DEBUG
        */

        editor.putString("sudoku", null);
        editor.putInt("nbGrille", 30);
        editor.putString("sudokuRef", null);
        editor.commit();

        tv11.setText("Données supprimées");
    }

    /**
     * @description Permet d'activer ou désactiver le chronomètre dans le jeu
     * @param v
     */
    public void activChrono(View v){
        chrono = sp2.isChecked();
        if(chrono){
            sp2.setText("Activé");
            editor.putBoolean("chrono", true);
        }else{
            sp2.setText("Non Actif");
            editor.putBoolean("chrono", false);
        }
        editor.commit();
    }

    /**
     * @description Retour à l'activité AboutActivity
     * @param v
     */
    public void retourS(View v){
        finish();
    }
}
