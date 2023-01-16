package mobile.example.soundloadtest;

import android.app.Activity;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends Activity {

	SoundPool soundPool;
	int soundStream;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
//        SoundPool.Builder builder = new SoundPool.Builder();
//        soundPool = builder.build();
	}
	
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.btnPlay:
			soundPool.setOnLoadCompleteListener(mListener);
			soundPool.load(this, R.raw.goodtime, 1);
			break;
		}
	}
	
	
	SoundPool.OnLoadCompleteListener mListener = new SoundPool.OnLoadCompleteListener() {
		@Override
		public void onLoadComplete(SoundPool pool, int soundId, int status) {
			if (status == 0) {
				soundStream = pool.play(soundId, 1, 1, 0, 0, 1);
			}
		}
	};
	
	
	
}
