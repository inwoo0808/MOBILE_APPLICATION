package mobile.example.network.requesthttp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    final static String TAG = "MainActivity";

    EditText etDate;
    TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etDate = findViewById(R.id.etUrl);
        tvResult = findViewById(R.id.tvResult);

        StrictMode.ThreadPolicy pol
                = new StrictMode.ThreadPolicy.Builder().permitNetwork().build();
        StrictMode.setThreadPolicy(pol);
    }


    public void onClick(View v) {

        if (!isOnline()) {
            Toast.makeText(this, "Network is not available!", Toast.LENGTH_SHORT).show();
            return;
        }


        switch (v.getId()) {
            case R.id.btn_request:

                break;
        }
    }


    private boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }







}
