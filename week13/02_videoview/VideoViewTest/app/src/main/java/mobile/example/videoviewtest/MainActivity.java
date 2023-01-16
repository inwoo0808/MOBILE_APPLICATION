package mobile.example.videoviewtest;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

public class MainActivity extends Activity {

	final static int PERMISSION_REQ_CODE = 100;

	VideoView videoDisplay;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		videoDisplay = (VideoView)findViewById(R.id.vvDisplay);

		//		외부저장소 접근을 위한 권한 확인
		if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQ_CODE);
		}

	}


	public void onClick(View v) {
		switch(v.getId()) {
			case R.id.btnLoad:
				videoDisplay.setVideoPath(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES).getAbsolutePath() + "/starwars.mp4");
				final MediaController controller = new MediaController(this);
				videoDisplay.setMediaController(controller);
				break;
			case R.id.btnPlay:
				if (videoDisplay != null) videoDisplay.start();
				break;
			case R.id.btnPause:
				if (videoDisplay != null) videoDisplay.pause();
				break;
		}
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
