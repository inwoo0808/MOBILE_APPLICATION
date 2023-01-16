package ddwu.mobile.place.placebasictest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.libraries.places.api.model.PlaceTypes;

import java.util.List;

import ddwu.mobile.place.placebasic.OnPlaceBasicResult;
import ddwu.mobile.place.placebasic.pojo.PlaceBasic;
import ddwu.mobile.place.placebasic.PlaceBasicManager;

public class MainActivity extends AppCompatActivity {

    final String TAG = "PlaceBasicTest";

    private PlaceBasicManager placeBasicManager;

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(ddwu.mobile.place.placebasictest.R.id.textView);

        placeBasicManager = new PlaceBasicManager(getString(ddwu.mobile.place.placebasictest.R.string.api_key));
        placeBasicManager.setOnPlaceBasicResult(new OnPlaceBasicResult() {
            @Override
            public void onPlaceBasicResult(List<PlaceBasic> list) {
                StringBuffer sb = new StringBuffer();
                if (list.size() == 0) {
                    textView.setText(getString(R.string.result_warning));
                } else {
                    for (PlaceBasic place : list) {
                        Log.d(TAG, place.toString());
                        sb.append(place.toString() + System.getProperty("line.separator"));
                        textView.setText(sb.toString());
                    }
                }
            }
        });
    }

    public void onClick(View v) {
        switch(v.getId()) {
            case ddwu.mobile.place.placebasictest.R.id.button:
                placeBasicManager.searchPlaceBasic(37.604094, 127.042463, 100, PlaceTypes.RESTAURANT);
                break;
        }
    }
}