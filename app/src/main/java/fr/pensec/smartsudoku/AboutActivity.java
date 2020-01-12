package fr.pensec.smartsudoku;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        getSupportActionBar().setTitle(R.string.activiteAbout);
    }

    public void retour(View d){
        finish();
    }

    public void settings(View s){
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
}
