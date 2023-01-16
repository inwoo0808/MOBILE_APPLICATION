package ddwu.mobile.network.sample.naverbooksearch;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import ddwu.mobile.network.sample.naverbooksearch.model.json.BookRoot;
import ddwu.mobile.network.sample.naverbooksearch.model.json.NaverBook;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    EditText etTarget;
    ListView lvList;
    String apiAddress;

    String query;

    MyBookAdapter adapter;
    List<NaverBook> resultList;

    Retrofit retrofit;
    INaverBookSearchService naverApiService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etTarget = findViewById(R.id.etTarget);
        lvList = findViewById(R.id.lvList);

        resultList = new ArrayList();
        adapter = new MyBookAdapter(this, R.layout.listview_book, resultList);
        lvList.setAdapter(adapter);

        apiAddress = getResources().getString(R.string.api_url);


//        Retrofit 생성

//        Retrofit Service 생성


        lvList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                // Glide 를 사용하여 이미지 파일을 외장메모리에 저장
                // 해당 부분은 파일매니저로 분리 필요

                Glide.with(MainActivity.this)
                        .asBitmap()
                        .load(resultList.get(position).getImage())
                        .into(new CustomTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                
                                // 파일 처리 클래스로 분리 필요
                                
                                if (isExternalStorageWritable()) {
                                    File file = new File (getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                                            "myalbum");
                                    if (!file.mkdirs()) {
                                        Log.d(TAG, "directory not created");
                                    }
                                    File saveFile = new File(file.getPath(), "test.jpg");
                                    try {
                                        FileOutputStream fos = new FileOutputStream((saveFile));
                                        resource.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                                        fos.flush();
                                        fos.close();
                                        Toast.makeText(MainActivity.this, "Saved!", Toast.LENGTH_SHORT).show();
                                    } catch (IOException e) { e.printStackTrace(); }
                                }
                            }

                            @Override
                            public void onLoadCleared(@Nullable Drawable placeholder) {

                            }
                        });

                return true;
            }
        });

    }

    private boolean isExternalStorageWritable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btnSearch:
                query = etTarget.getText().toString();  // UTF-8 인코딩 필요

//                Retrofit 사용 시 인코딩 불필요
//                try {
//                    query = URLEncoder.encode(query, "UTF-8");
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }

//                Retrofit 서비스 호출

                break;
        }
    }


    Callback<BookRoot> naverApiCallBack = new Callback<BookRoot>() {
        @Override
        public void onResponse(Call<BookRoot> call, Response<BookRoot> response) {
            if (response.isSuccessful()) {
                // 파싱 결과를 adapter 에 설정
            }
        }

        @Override
        public void onFailure(Call<BookRoot> call, Throwable t) {
            Log.d(TAG, t.toString());
            Toast.makeText(MainActivity.this, "Cannot Access OpenAPI Server", Toast.LENGTH_SHORT).show();
        }
    };



}
