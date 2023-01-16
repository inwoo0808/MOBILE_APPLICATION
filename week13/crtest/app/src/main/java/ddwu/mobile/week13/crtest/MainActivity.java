package ddwu.mobile.week13.crtest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    final static String TAG = "MainActivity";

    final int PERMISSION_REQ_CODE = 100;    // Permission 요청 코드

    EditText editText;
    ListView listView;
    ImageView imageView;

    SimpleCursorAdapter adapter;
    ContentResolver contentResolver;
    Cursor mCursor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editText);
        listView = findViewById(R.id.listView);
        imageView = findViewById(R.id.imageView);

        // 시스템에서 DB 획득
        contentResolver = getContentResolver();

        initList();
    }


    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.button:
                if (checkPermission()) {
                    mCursor = queryImageByType(editText.getText().toString());
                    Log.d(TAG, "count: " + mCursor.getCount());
                    if (mCursor.getCount() == 0) { // 결과 있는지 체크
                        Toast.makeText(getApplicationContext(), "No images", Toast.LENGTH_SHORT).show();
                    } else {
                        adapter.changeCursor(mCursor); // 있으면 냅다 어댑터에 넣어주기
                    }
                }
                break;
        }
    }


    private Cursor queryImageByType(String imageType) { // 내가 찾고 싶은게 imageType임
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI; // 외부 저장소 접근

        String selection = MediaStore.Images.ImageColumns.MIME_TYPE + "=?";
        String[] selectArgs = new String[] { "image/" + imageType };

        if (TextUtils.isEmpty(imageType)) {
            selection = null;
            selectArgs = null;
        }

        // Cursor 반환
        return contentResolver.query(uri, null, selection , selectArgs, null);

    }


    private void initList() { // 어댑터에 있는 내용 보여주기
        adapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_2,
                null,       // 연결 Cursor
                new String[] { MediaStore.Images.ImageColumns.MIME_TYPE, MediaStore.Images.ImageColumns.DATA },
                new int[] { android.R.id.text1, android.R.id.text2 },
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) { 
                // 커서의 id 값이 자동으로 들어옴(_id 컬럼이 기본적으로 있으니까 커서 어댑터에서 인식 가능)
                Uri uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id); // 실제 저장 위치 반환
                // 외부 메모리에 저장한 이미지 id 항목의 URI 반환
                Log.d(TAG, "URI: " + uri);
                imageView.setImageURI(uri);
            }
        });
    }


    /* 필요 permission 요청 */
    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},
                        PERMISSION_REQ_CODE);
                return false;
            }
        }
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == PERMISSION_REQ_CODE) {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 퍼미션을 획득하였을 경우 계속 수행
                queryImageByType(editText.getText().toString());
            } else {
                // 퍼미션 미획득 시 액티비티 종료
                Toast.makeText(this, "앱 실행을 위해 권한 허용이 필요함", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

}