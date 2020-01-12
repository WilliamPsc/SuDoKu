package fr.pensec.smartsudoku;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    Switch sp, sp2;
    Boolean stateSP, stateSP2;
    TextView tv11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setTitle(R.string.activitySettings);

        sp = findViewById(R.id.switch1);
        sp2 = findViewById(R.id.switch2);
        tv11 = findViewById(R.id.textView11);
        sharedPref = this.getSharedPreferences("grille", MODE_PRIVATE);
        editor = sharedPref.edit();

    }

    public void supprData(View v){
        stateSP = sp.isChecked();
        if(stateSP){
            sp.setText("Ok");
            String s1, s3;
            Integer s2;

            s1 = sharedPref.getString("sudoku", null);
            s2 = sharedPref.getInt("nbGrille", 30);
            s3 = sharedPref.getString("sudokuRef", null);
            Log.i("supprData", "Sudoku : " + s1);
            Log.i("supprData", "Numéro Grille :" + s2);
            Log.i("supprData", "SudokuRef : " + s3);

            editor.putString("sudoku", null);
            editor.putInt("nbGrille", 30);
            editor.putString("sudokuRef", null);
            editor.commit();

            tv11.setText("Données supprimées");

        }else{
            sp.setText("Non");
        }
    }

    public void activChrono(View v){
        stateSP2 = sp2.isChecked();
        if(stateSP2){
            sp2.setText("Activé");
            editor.putBoolean("chrono", true);
        }else{
            sp2.setText("Non Actif");
            editor.putBoolean("chrono", false);
        }
        editor.commit();
    }

    public void retourS(View v){
        finish();
    }
}
