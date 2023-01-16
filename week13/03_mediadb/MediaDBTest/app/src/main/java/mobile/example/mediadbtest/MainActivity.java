package mobile.example.mediadbtest;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class MainActivity extends AppCompatActivity {

    final static int PERMISSION_REQ_CODE = 100;

	ContentResolver contentResolver;
	TextView tvResult;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        tvResult = (TextView)findViewById(R.id.tvResult);
        
        contentResolver = getContentResolver();
        
//		외부저장소 접근을 위한 권한 확인
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQ_CODE);
        }

    }

    @Override
    protected void onDestroy() {
    	super.onDestroy();
    	unregisterReceiver(receiver);
    }
    
    
    BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			dumpQuery();
		}
    	
    };
    
    public void onClick(View v) {
    	switch(v.getId()) {
        case R.id.btnBR:
            IntentFilter filter = new IntentFilter();
            filter.addAction(Intent.ACTION_MEDIA_SCANNER_STARTED);
            filter.addAction(Intent.ACTION_MEDIA_SCANNER_FINISHED);
            filter.addAction(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            filter.addDataScheme("file");
            registerReceiver(receiver, filter);
    	case R.id.btnSearch:
    		dumpQuery();
    		break;
    	}
    }
    
    private void dumpQuery() {
    	StringBuilder resultString = new StringBuilder();
    	String fileName;
    	Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    	
    	Cursor cursor = contentResolver.query(uri, null, null, null, null);
		
		resultString.append("num of files: " + cursor.getCount() + "\n");
		
		while(cursor.moveToNext()) {
			fileName = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA));
			resultString.append(fileName + System.getProperty("line.separator"));
		}

		tvResult.setText(resultString.toString());
    }


    //	외부저장소 접근 권한 획득 결과 확인
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQ_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "외부저장소 읽기 권한 획득!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "외부저장소 읽기 권한 없음", Toast.LENGTH_SHORT).show();
                }

        }
    }

    
}
