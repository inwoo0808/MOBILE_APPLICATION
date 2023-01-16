package ddwu.mobile.network.myretrofittest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    final static String TAG = "MainActivity";

    EditText editText;
    TextView tvResult;

    String apiUrl;
    String apiKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = findViewById(R.id.etDate);
        tvResult = findViewById(R.id.tvResult);

        apiUrl = getResources().getString(R.string.api_url);
        apiKey = getResources().getString(R.string.kobis_key);
    }



    public void onClick (View v) {
        switch (v.getId()) {
            case R.id.button:
                String targetDate = editText.getText().toString();


                break;
        }
    }
}