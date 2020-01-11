package fr.pensec.smartsudoku;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class JeuActivity extends AppCompatActivity {
    Grille grille;
    Integer x = 0;
    Integer y = 0;
    Intent intent;
    TextView winLose;
    TextView nbGame;
    String nbGrille = "";
    String value;
    String valueRef;
    Integer valueGrille = 30;
    URL url;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("valeurOnCreate0", "Chaine loaded : " + value + " | nbGrille : " + valueGrille + " | Chaine loadedRef : " + valueRef);
        super.onCreate(savedInstanceState);
        sharedPref = this.getSharedPreferences("grille", MODE_PRIVATE);
        editor = sharedPref.edit();
        setContentView(R.layout.activity_jeu);
        getSupportActionBar().setTitle(R.string.activiteJeu);

        winLose = findViewById(R.id.textView5);
        nbGame = findViewById(R.id.textView6);
        this.intent = getIntent();
        this.grille = findViewById(R.id.grilleView);

        if (sharedPref.contains("sudoku") && sharedPref.contains("nbGrille")/* && sharedPref.contains("sudokuRef")*/){
            valueRef = sharedPref.getString("sudokuRef", null);
            value = sharedPref.getString("sudoku", null);
            valueGrille = sharedPref.getInt("nbGrille", 30);
            nbGame.setText("Grille numéro : " + valueGrille);
            setValue(valueGrille);
            valueRef = value;
            grille.set(value);
            Log.i("valeurOnCreate1", "Chaine loaded : " + value + " | nbGrille : " + valueGrille + " | Chaine loadedRef : " + valueRef);
        }
        this.grille.setOnTouchListener(new Grille.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    //Log.i("valeurOnCreate2 ", "TOUCHE = " + (int) event.getX() + " " + (int) event.getY());
                    x = (int) event.getX();
                    y = (int) event.getY();

                }
                return false;
            }
        });

        this.grille.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onGrilleClicked(x, y);
                }
            });
    }

    public static String strCmp(String s1, String s2){
        if(s1 == null)
            return "---------------------------------------------------------------------------------";
        if(s2 == null)
            return "---------------------------------------------------------------------------------";

        if(s1.length() != s2.length())
            return s2;

        char[] res = new char[s1.length()];
        for(int i = 0; i < s1.length();i++){
            res[i] = s1.charAt(i);
        }
        Log.i("valeurStrCmp", "Chaine loaded : " + s2 + " | Chaine loadedRef : " + s1);
        for(int i = 0; i < s2.length(); i++) {
            if(s1.charAt(i) == s2.charAt(i)){
                //Log.i("valeurStrCmp", "s1[i] : " + s1.charAt(i) + " | s2[i] : " + s2.charAt(i));
                res[i] = '-';
                //Log.i("valeurStrCmp", "Chaine modifiée : " + res[i] + " | indice : " + i);
            }
        }
        String resS = new String(res);
        return resS;
    }

    public void setValue(Integer which){
        if(which == 31){
            // Choix d'une grille aléatoire
            int nombreAleatoire = 0 + (int)(Math.random() * ((29) + 1));
            valueGrille = nombreAleatoire;
            nbGame.setText("Grille numéro : " + nombreAleatoire);
            nbGrille = "http://labsticc.univ-brest.fr/~bounceur/cours/android/tps/sudoku/index.php?v="+nombreAleatoire;
        } else if(which == 30){
            // Choix de la grille par défaut
            nbGame.setText("Grille numéro : DEFAUT");
            value = "000105000140000670080002400063070010900000003010090520007200080026000035000409000";
            grille.set(value);
            valueGrille = which;
            winLose.setText("");
            grille.setWon(null);
            grille.invalidate();
            Log.i("valeurSetValue", "Hello why i'm here?");
            return;
        } else{
            nbGame.setText("Grille numéro : " + which);
            valueGrille = which;
            nbGrille = "http://labsticc.univ-brest.fr/~bounceur/cours/android/tps/sudoku/index.php?v="+which;
        }
        try {
            url = new URL(nbGrille);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        grille.set(value);
        winLose.setText("");
        grille.setWon(null);
        grille.invalidate();
        new recupererGrille().execute(url);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        value = grille.toString();
        editor.putString("sudoku", value);
        editor.putInt("nbGrille", valueGrille);
        editor.putString("sudokuRef", valueRef);
        editor.commit();
        Log.i("valeurOnSave", "Chaine saved : " + value + " | nbGrille : " + valueGrille + " | Chaine savedRef : " + valueRef);
    }

    protected void onGrilleClicked(int valeurX, int valeurY){
        this.x = this.grille.getXFromMatrix(valeurX);
        this.y = this.grille.getYFromMatrix(valeurY);

        if(this.grille.isNotFix(this.x, this.y)){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.builderTitle);

            builder.setItems(getResources().getStringArray(R.array.numbers), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    grille.set(x,y,which);
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    public void onBoutonClicked(View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.builderTitle2);

        builder.setItems(getResources().getStringArray(R.array.choixSudoku), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 31) {
                    // Choix d'une grille aléatoire
                    int nombreAleatoire = 0 + (int) (Math.random() * ((29) + 1));
                    valueGrille = nombreAleatoire;
                    nbGame.setText("Grille numéro : " + nombreAleatoire);
                    nbGrille = "http://labsticc.univ-brest.fr/~bounceur/cours/android/tps/sudoku/index.php?v=" + nombreAleatoire;
                } else if (which == 30) {
                    // Choix de la grille par défaut
                    nbGame.setText("Grille numéro : DEFAUT");
                    value = "000105000140000670080002400063070010900000003010090520007200080026000035000409000";
                    valueGrille = which;
                    grille.set(value);
                    winLose.setText("");
                    grille.setWon(null);
                    grille.invalidate();
                    return;
                } else {
                    nbGame.setText("Grille numéro : " + which);
                    valueGrille = which;
                    nbGrille = "http://labsticc.univ-brest.fr/~bounceur/cours/android/tps/sudoku/index.php?v=" + which;
                }
                try {
                    url = new URL(nbGrille);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                //valueRef = "000000000000000000000000000000000000000000000000000000000000000000000000000000000";
                winLose.setText("");
                grille.setWon(null);
                grille.invalidate();
                new recupererGrille().execute(url);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public class recupererGrille extends AsyncTask<URL, Integer, String> {
        protected String doInBackground(URL... urls) {
            String inputLine = "";
            try{
                BufferedReader in = new BufferedReader(new InputStreamReader(urls[0].openStream()));
                inputLine = in.readLine();
                in.close();
            }
            catch(Exception e) {
                e.printStackTrace();
            }
            return inputLine;
        }

        protected void onPostExecute(String result) {
            value = result;
            grille.set(result);

            Log.i("informationRecupGrille", "valueRef0 = " + valueRef);
            valueRef = strCmp(valueRef, result);
            editor.putString("sudokuRef", valueRef);
            editor.commit();
            Log.i("informationRecupGrille", "result = " + result);
            Log.i("informationRecupGrille", "value = " + value);
            Log.i("informationRecupGrille", "valueRef1 = " + valueRef);
            grille.setV(valueRef);
        }
    }

    public void onValiderClicked(View v){
        boolean win = this.grille.gagne();
        this.grille.setWon(win);
        this.grille.invalidate();

        if(win) {
            winLose.setText("GAGNÉ !");
        } else{
            winLose.setText("PERDU !");
        }
    }

}
