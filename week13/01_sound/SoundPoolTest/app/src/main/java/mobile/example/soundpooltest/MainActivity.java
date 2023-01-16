package mobile.example.soundpooltest;

import android.app.Activity;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

	SoundPool pool;
	int sound;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// (동시 재생 최대스트림 개수,  스트림 타입,  샘플링 품질)
		pool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
		// (Context, 재생 리소스명,  우선순위) 
		sound = pool.load(this, R.raw.dingdong, 1);
		// sound = pool.load(String path, int priority)
		
	}
	
	
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.btnPlay:
			
			// (int soundId, flat leftVol, float rightVol, int priority, int loop, float playSpeed)
			pool.play(sound, 1, 1, 0, 0, 1);
			
			break;
		}
	}
	
	
	
}
