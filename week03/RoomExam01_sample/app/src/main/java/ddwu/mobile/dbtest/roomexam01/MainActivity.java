package ddwu.mobile.dbtest.roomexam01;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    final static String TAG = "MainActivity";

    EditText etFood;
    EditText etNation;
    ListView listView;

//    ArrayAdapter<Food> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etFood = findViewById(R.id.etFood);
        etNation = findViewById(R.id.etNation);
        listView = findViewById(R.id.listView);
//        adapter = new ArrayAdapter<Food>(this, android.R.layout.simple_list_item_1, new ArrayList<Food>());
        
    }


    public void onClick(View v) {

        final String food = etFood.getText().toString();
        final String nation = etNation.getText().toString();

        switch (v.getId()) {
            case R.id.btnInsert:

                break;
            case R.id.btnUpdate:

                break;
            case R.id.btnDelete:

                break;
            case R.id.btnShow:

                break;
        }
    }
}