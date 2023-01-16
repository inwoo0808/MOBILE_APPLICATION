package ddwu.mobile.week4.threadbasic;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    final static String TAG = "MainActivity";

    EditText etText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etText = findViewById(R.id.etText);
    }

    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btnStart:
                TestThread t = new TestThread();
                t.start();
                etText.setText("Thread start!");
                Toast.makeText(this, "Running!", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    class TestThread extends Thread {
        @Override
        public void run() {
            for (int i=0; i < 100; i++) {
                Log.d(TAG, "i: " + i );
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}

