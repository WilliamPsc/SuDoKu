package fr.pensec.smartsudoku;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ChoixActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choix);
        getSupportActionBar().setTitle(R.string.activiteChoix);

        ListView lv = findViewById(R.id.listView2);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // selected item
                String selected = (String) parent.getAdapter().getItem(position);
                quitActivity(selected);
                Toast toast = Toast.makeText(getApplicationContext(), selected, Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    public void quitActivity(String item) {
        Intent intent = new Intent();
        intent.putExtra("value", Integer.parseInt(item));
        setResult(1, intent);
        finish();
    }
}
