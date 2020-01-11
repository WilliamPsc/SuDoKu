package fr.pensec.smartsudoku;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class Grille extends View {

    private int screenWidth;
    private int screenHeight;
    private int n;
    public int tailleCase;

    private Paint paint1;   // Pour dessiner la grille (lignes noires)
    private Paint paint2;   // Pour le texte des cases fixes
    private Paint paint3;   // Pour dessiner les lignes rouges (grosse)
    private Paint paint4;   // Pour le texte noir des cases a modifier
    private Paint paint5;   // Lorsque l'on appui sur le bouton valider

    private int[][] matrix = new int[9][9];
    private boolean[][] fixIdx = new boolean[9][9];

    private Boolean won = null;

    public Grille(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public Grille(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.Grille, 0, 0);

        this.screenWidth = getWidth();
        this.screenHeight = getHeight();
        int w = Math.min(screenWidth, screenHeight);
        w = w - (w%9);
        this.n = w / 9 ;

        try {
            tailleCase = a.getInt(R.styleable.Grille_tailleCase, 1);
        } finally {
            a.recycle();
        }

        init();
    }

    public Grille(Context context) {
        super(context);
        init();
    }

    private void init() {


        // Grille de depart
        set("000105000140000670080002400063070010900000003010090520007200080026000035000409000");

        // Grille gagnante
        //set("002145398145983672389762451263574819958621743714398526597236184426817935831459267");

        // Grille vide
        //set("000000000000000000000000000000000000000000000000000000000000000000000000000000000");

        paint1 = new Paint(); //lignes noire
        paint1.setAntiAlias(true);
        paint1.setColor(Color.BLACK);
        paint1.setStyle(Paint.Style.STROKE); // modification car cases noir sinon

        paint2 = new Paint(); // texte cases fixes
        paint2.setAntiAlias(true);
        paint2.setColor(Color.RED);
        paint2.setTextSize(50f);
        paint2.setTextAlign(Paint.Align.CENTER);

        paint3 = new Paint(); // grosse lignes rouges
        paint3.setAntiAlias(true);
        paint3.setColor(Color.RED);
        paint3.setStrokeWidth(8f);

        paint4 = new Paint(); // texte noir des cases à modifier
        paint4.setColor(Color.BLACK);
        paint4.setTextSize(40f);

        paint5 = new Paint();
        paint5.setStyle(Paint.Style.STROKE);
        paint5.setStrokeWidth(15f);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        this.screenWidth = getWidth();
        this.screenHeight = getHeight();
        int w = Math.min(screenWidth, screenHeight);
        w = w - (w%9);
        this.n = w / 9 ;

        // Dessiner w lignes verticales et w lignes horizontales noires
        // Dessiner 2 lignes rouges verticales et 2 lignes rouges horizontales
        for(int i = 0; i<9; i++){
            for(int j = 0; j<9; j++){
                canvas.drawRect(new Rect(j*n+this.tailleCase,i*n+this.tailleCase,(j*n)+n-this.tailleCase,(i*n)+n-this.tailleCase),paint1);
            }
        }

        // A t on gagné?
        if(this.won != null){
           if(this.won == true){
               paint5.setColor(Color.GREEN);
           }else{
               paint5.setColor(Color.RED);
           }
           canvas.drawRect(new Rect(0,0,w,w), paint5);
        }

        // Dessiner 2 lignes rouges verticales et 2 lignes rouges horizontales
        canvas.drawLine(w/3,0, w/3, w, paint3);
        canvas.drawLine(w*2/3,0, w*2/3, w, paint3);
        canvas.drawLine(0,w/3, w, w/3, paint3);
        canvas.drawLine(0,w*2/3, w, w*2/3, paint3);

        // Les contenus des cases
        String s;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                s = "" + (matrix[j][i] == 0 ? "" : matrix[j][i]);
                if (fixIdx[j][i])
                    canvas.drawText(s, i * n + (n / 1.65f) - (n / 10), j * n
                            + (n / 1.6f) + (n / 15), paint2);
                else
                    canvas.drawText(s, i * n + (n / 1.65f) - (n / 10), j * n
                            + (n / 1.6f) + (n / 15), paint4);
            }
        }
    }

    public int getXFromMatrix(int x) {
        // Renvoie l'indice d'une case à partir du pixel x de sa position h
        return (x / n);
    }

    public int getYFromMatrix(int y) {
        // Renvoie l'indice d'une case à partir du pixel y de sa position v
        return (y / n);
    }

    public void setV(String s){
        // Remplir la matrice matrix a partir d'un vecteur String s
        for (int i = 0; i < 9; i++) {
            //Log.i("setV", "Set = " + s.substring(i * 9, i * 9 + 9));
            setSV(s.substring(i * 9, i * 9 + 9), i);
        }
    }

    public void setSV(String s, Integer i) {
        // Remplir la ieme ligne de la matrice matrix avec un vecteur String s
        int v;
        for (int j = 0; j < 9; j++) {
            v = s.charAt(j) - '0';
            if(s.charAt(j) != '-'){
                matrix[i][j] = v;
                //Log.i("setSV", "Valeur[" + i + "]" + "[" + j + "] : " + v);
                fixIdx[i][j] = false;
            }
        }
        this.invalidate();
    }

    public void set(String s, int i) {
        // Remplir la ieme ligne de la matrice matrix avec un vecteur String s
        int v;
        for (int j = 0; j < 9; j++) {
            v = s.charAt(j) - '0';
            matrix[i][j] = v;
            if (v == 0)
                fixIdx[i][j] = false;
            else
                fixIdx[i][j] = true;
        }
        this.invalidate();
    }

    public void set(String s) {
        // Remplir la matrice matrix a partir d'un vecteur String s
        for (int i = 0; i < 9; i++) {
            //Log.i("set", "Set = " + s.substring(i * 9, i * 9 + 9));
            set(s.substring(i * 9, i * 9 + 9), i);
        }
    }

    public void set(int x, int y, int v) {
        // Affecter la valeur v a la case (y, x)
        // y : ligne
        // x : colonne
        if(this.isNotFix(x,y)){
            this.matrix[y][x] = v;
        }
        this.invalidate();
    }

    public boolean isNotFix(int x, int y) {
        // Renvoie si la case (y, x) n'est pas fixe
        return !this.fixIdx[y][x];
    }

    public boolean gagne() {
        // Verifier si la case n'est pas vide ou bien s'il existe
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if(this.matrix[i][j]==0) return false;
            }
        }
        // un numero double dans chaque ligne ou chaque colonne de la grille
        for (int v = 1; v <= 9; v++) {
            for (int i = 0; i < 9; i++) {
                boolean bx = false;
                boolean by = false;
                for (int j = 0; j < 9; j++) {
                    if (matrix[i][j] == 0) return false;
                    if ((matrix[i][j] == v) && bx) return false;
                    if ((matrix[i][j] == v) && !bx) bx=true;
                    if ((matrix[j][i] == v) && by) return false;
                    if ((matrix[j][i] == v) && !by) by=true;
                }
            }
        }
        // ------
        // Gagne
        return true;
    }

    public String toString(){
        String res = "";
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 9; j++){
                res += matrix[i][j];
            }
        }
        return res;
    }


    public void setWon(Boolean val){
        this.won = val;
    }

    public Boolean getWon() {
        return won;
    }
}