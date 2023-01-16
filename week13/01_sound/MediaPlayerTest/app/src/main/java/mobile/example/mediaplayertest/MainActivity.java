package mobile.example.mediaplayertest;

import java.io.IOException;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {

	final static String TAG = "MediaPlayerTest";
	final static int PERMISSION_REQ_CODE = 100;
	
	MediaPlayer resMediaPlayer = null;
	MediaPlayer fileMediaPlayer = null;
	MediaPlayer netMediaPlayer = null;
	
	String soundPath;
	Uri soundUri;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
        fileMediaPlayer = new MediaPlayer();		// 파일로 저장하고 있는 sound 재생용
        netMediaPlayer = new MediaPlayer();		// 네트워크를 통한 서버의 sound 재생용

//		외부저장소의 공용 폴더(Music) 의 사운드 파일 경로
		soundPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).getAbsolutePath() + "/" + getResources().getString(R.string.song_title);
		Log.i(TAG, "SD Path: " + soundPath);

//		외부저장소 접근을 위한 권한 확인
		if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQ_CODE);
		}

//		네트워크 상의 서버로부터 사운드를 비동기 로딩 준비 후 사운드 재생
		netMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
			@Override
			public void onPrepared(MediaPlayer mediaPlayer) {
//			    fileMediaPlayer.seekTo(0);
                mediaPlayer.start();
			}
		});

	}
	
	
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.btnResourcePlay:
			resMediaPlayer = MediaPlayer.create(this, R.raw.dont_hate_me2);		// res 의 사운드 생성 및 로딩 수행
			resMediaPlayer.start();
			break;
		case R.id.btnFilePlay:
			fileMediaPlayer.reset();        // 이전 재생 상태를 reset
			try {
				fileMediaPlayer.setDataSource(soundPath);       // 파일 경로에 해당하는 사운드를 지정
				fileMediaPlayer.prepare();                          // 사운드 로딩 준비 - 동기 방식
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			fileMediaPlayer.start();
			break;
		case R.id.btnNetworkPaly:

			try {
			    netMediaPlayer.reset();         // 이전 재생 상태를 reset
				netMediaPlayer.setDataSource(getResources().getString(R.string.music_url));     // url 경로의 서버 파일에 해당하는 사운드를 지정
//				netMediaPlayer.prepare();
				netMediaPlayer.prepareAsync();      // 사운드 로딩 준비 - 비동기
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//			netMediaPlayer.start();
			break;
		case R.id.btnStop:
			if (resMediaPlayer != null) resMediaPlayer.pause();
			fileMediaPlayer.pause();
			netMediaPlayer.pause();
			break;
		}
		 
	}


	// 각 MediaPlayer의 자원 해제
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		if (resMediaPlayer != null) {
			resMediaPlayer.release();
			resMediaPlayer = null;
		}
		if (fileMediaPlayer != null) {
			fileMediaPlayer.release();
			fileMediaPlayer = null;
		}
		if (netMediaPlayer != null) {
			netMediaPlayer.release();
			netMediaPlayer = null;
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
