package ddwu.mobile.network.glidetest;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;

import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    ImageView imageView;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);
    }

    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.button:


                break;
        }
    }
}