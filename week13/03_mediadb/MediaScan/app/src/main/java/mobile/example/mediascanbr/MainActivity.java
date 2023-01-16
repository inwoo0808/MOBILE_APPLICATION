package mobile.example.mediascanbr;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

	final static int PERMISSION_REQ_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    
    
    public void onClick(View v) {
//    	SD 카드 전체 scan (특정 파일만 스캔할 경우 뒤쪽에 파일 경로명 및 파일명 추가)
    	Uri uri = Uri.parse("file://" + Environment.getExternalStorageDirectory());
    	
    	Intent intent = new Intent();
    	intent.setAction(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
    	intent.setData(uri);

    	switch(v.getId()) {
    	case R.id.btnScan:
    		sendBroadcast(intent);
    		Toast.makeText(this, "미디어 스캔 방송!", Toast.LENGTH_SHORT).show();
    		break;
    	}
    }

}
