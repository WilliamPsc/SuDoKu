package fr.pensec.smartsudoku;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle(R.string.activiteMain);
    }

    public void newParty(View d){
        Intent intent = new Intent(this, JeuActivity.class);
        startActivity(intent);
    }

    public void about(View d){
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }
}
