package ddwu.mobile.lecture.etc.myretrofittest;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import ddwu.mobile.lecture.etc.myretrofittest.model.json.Book;


public class MainActivity extends AppCompatActivity {

    final static String TAG = "MainActivity";

    EditText editText;
    ListView listView;
    ImageView imageView;

    String naverId;
    String naverSecret;
    ArrayAdapter<Book> adapter;

    List<Book> books;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.etQuery);
        listView = findViewById(R.id.listView);
        imageView = findViewById(R.id.imageView);

        books = new ArrayList<Book>();
        adapter = new ArrayAdapter<Book>(this, android.R.layout.simple_list_item_1, books);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });

        naverId = getResources().getString(R.string.client_id);
        naverSecret = getResources().getString(R.string.client_secret);

    }



    public void onClick (View v) {
        switch (v.getId()) {
            case R.id.btnTest:


                break;
            case R.id.btnSearch:
                adapter.clear();
                String query = editText.getText().toString();


                break;
        }
    }




}
