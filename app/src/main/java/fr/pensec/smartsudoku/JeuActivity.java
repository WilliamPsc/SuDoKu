/**
 * @author William PENSEC
 * @date 12/01/2020
 * @version 1.0
 **/
package fr.pensec.smartsudoku;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Chronometer;
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
    Chronometer ch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("valeurOnCreate0", "Chaine loaded : " + value + " | nbGrille : " + valueGrille + " | Chaine loadedRef : " + valueRef);
        super.onCreate(savedInstanceState);
        sharedPref = this.getSharedPreferences("grille", MODE_PRIVATE);
        editor = sharedPref.edit();
        setContentView(R.layout.activity_jeu);
        getSupportActionBar().setTitle(R.string.activiteJeu);

        ch = findViewById(R.id.chronometre);
        winLose = findViewById(R.id.textView5);
        nbGame = findViewById(R.id.textView6);
        this.intent = getIntent();
        this.grille = findViewById(R.id.grilleView);
        Boolean chrono = false;


        // Gestion du chronomètre
        if(sharedPref.contains("chrono")){
            chrono = sharedPref.getBoolean("chrono", false);
        }
        if(chrono){
            ch.setBase(SystemClock.elapsedRealtime());
            ch.start();
        }else{
            ch.stop();
        }

        // Chargement des valeurs sauvegardées de l'application
        if (sharedPref.contains("sudoku") && sharedPref.contains("nbGrille")){
            valueRef = sharedPref.getString("sudokuRef", null);
            value = sharedPref.getString("sudoku", null);
            valueGrille = sharedPref.getInt("nbGrille", 30);
            nbGame.setText("Grille numéro : " + valueGrille);
            setValue(valueGrille);
            valueRef = value;
            grille.set(value);
            Log.i("valeurOnCreate1", "Chaine loaded : " + value + " | nbGrille : " + valueGrille + " | Chaine loadedRef : " + valueRef);
        }

        // Permet d'afficher la valeur sur la grille
        this.grille.setOnTouchListener(new Grille.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
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

    /**
     * @description Cette fonction permet de différencier la grille de base (référente) à la grille modifiée. La fonction supprime les caractères identiques en les remplaçant par un "-".
     * @param s1
     * @param s2
     * @return Une chaîne de caractère contenant soit des chiffres soit des tirets soit un mixte afin d'être traitée par la suite par une autre fonction
     */
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
                res[i] = '-';
            }
        }
        String resS = new String(res);
        return resS;
    }

    /**
     * @description Permet de charger au lancement la grille précédemment chargée.
     *  La fonction prend en paramètre un entier correspondant au numéro de la grille sauvegardée.
     *  Il concatène ce numéro avec une URL et appelle la classe recupererGrille qui fait la requête de chargement
     *
     *
     * @param which
     */
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
        if(haveInternetConnection()) {
            new recupererGrille().execute(url);
        }else{
            nbGame.setText("CONNEXION NON TROUVÉE");
        }
    }

    /**
     * @description Permet de stocker l'état de la grille lorsque l'utilisateur quitte la partie/application
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        value = grille.toString();
        editor.putString("sudoku", value);
        editor.putInt("nbGrille", valueGrille);
        editor.putString("sudokuRef", valueRef);
        editor.putBoolean("chrono", false);
        editor.commit();
        Log.i("valeurOnSave", "Chaine saved : " + value + " | nbGrille : " + valueGrille + " | Chaine savedRef : " + valueRef);
    }

    /**
     * @description Affiche la liste des nombres (entre 0 et 9) afin que l'utilisateur puisse en choisir un et l'inscrire dans la grille
     * @param valeurX
     * @param valeurY
     */
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

    /**
     * @description Charge la grille souhaitée en appellant la classe asynchrone recupererGrille
     * Elle fonctionne quasiment de la même façon que la fonction setValue(Integer)
     * @param v
     */
    public void onBoutonClicked(View v){
        valueRef = "---------------------------------------------------------------------------------";
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.builderTitle2);

        builder.setItems(getResources().getStringArray(R.array.choixSudoku), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 31) {
                    // Choix d'une grille aléatoire
                    int nombreAleatoire = (int) (Math.random() * ((29) + 1));
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
                winLose.setText("");
                grille.setWon(null);
                grille.invalidate();
                if(haveInternetConnection()) {
                    new recupererGrille().execute(url);
                }else{
                    nbGame.setText("CONNEXION NON TROUVÉE");
                }
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * @description Classe interne permettant de gérer de façon asynchrone la connexion à internet afin de faire appel à la grille.
     */
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

    /**
     * @description Permet de vérifier si l'utilisateur a gagné ou perdu et d'afficher des valeurs selon le cas
     * @param v
     */
    public void onValiderClicked(View v){
        boolean win = this.grille.gagne();
        this.grille.setWon(win);
        this.grille.invalidate();

        if(win) {
            winLose.setText("GAGNÉ !");
            ch.stop();
        } else{
            winLose.setText("PERDU !");
            ch.stop();
        }
    }

    /**
     * @description Permet de
     * @return true si la connexion est OK / false si il n'y a pas de connexion
     */
    private boolean haveInternetConnection(){
        NetworkInfo network = ((ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (network==null || !network.isConnected()) {
            // Le périphérique n'est pas connecté à Internet
            return false;
        }
        // Le périphérique est connecté à Internet
        return true;
    }
}
