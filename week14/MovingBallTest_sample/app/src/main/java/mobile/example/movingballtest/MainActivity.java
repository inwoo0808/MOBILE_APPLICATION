package mobile.example.movingballtest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

	BallView ballView;
	private SensorManager sensorManager;
	float[] mGravity;
	float[] mMagenetic;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_main);
		ballView = new BallView(this);
		setContentView(ballView);

		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

		}
	
	
	@Override
	protected void onPause() {
		super.onPause();
		sensorManager.unregisterListener (sensorEventListener);
	}


	@Override
	protected void onResume() {
		super.onResume();

		Sensor accelerometer = sensorManager.getDefaultSensor (Sensor.TYPE_ACCELEROMETER);
		Sensor magnetometer = sensorManager.getDefaultSensor (Sensor.TYPE_MAGNETIC_FIELD);
		int sensorDelay = SensorManager.SENSOR_DELAY_NORMAL;

		sensorManager.registerListener(sensorEventListener, accelerometer, sensorDelay);
		sensorManager.registerListener(sensorEventListener, magnetometer, sensorDelay);
	}

	SensorEventListener sensorEventListener = new SensorEventListener(){

		@Override
		public void onSensorChanged(SensorEvent event) {
			if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
				mGravity = event.values.clone();
			}
			if(event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD){
				mMagenetic = event.values.clone();
			}

			if(mGravity != null && mMagenetic != null){
				float rMatrix[] = new float[9];
				boolean success = SensorManager.getRotationMatrix(rMatrix, null, mGravity, mMagenetic);

				if(success){
					float values[] = new float[3];
					SensorManager.getOrientation(rMatrix, values);
					for(int i=0; i<values.length; i++){
						Double degree = Math.toDegrees(values[i]);
						values[i] = degree.floatValue();
					}

					if(values[1] > 0) // pitch 값이 양수 = 핸드폰 위쪽을 아래로 기울인 상태임 -> 그러면 y 좌표가 음수가 되는게 맞음
						ballView.y -= 10;
					else
						ballView.y += 10; // pitch 값이 음수 = 핸드폰 아래쪽을 아래로 기울인 상태임 -> 그러면 y 좌표가 양수가 되는게 맞음

					if(values[2] > 0)
						ballView.x += 10;
					else
						ballView.x -= 10;

					ballView.invalidate();
				}
			}

		}

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {

		}
	};
	
	class BallView extends View{

		Paint paint;

		int width;
		int height;
		
		int x;
		int y;
		int r;
		
		boolean isStart;
		
		public BallView(Context context) {
			super(context);
			paint = new Paint();
			paint.setColor(Color.BLUE);
			paint.setAntiAlias(true);
			isStart = true;
			r = 100;
		}
		
		public void onDraw(Canvas canvas) {
			if(isStart) {
				width = canvas.getWidth();
				height = canvas.getHeight(); 
				x =  width / 2;
				y =  height / 2;
				isStart = false;
			} 
			
			canvas.drawCircle(x, y, r, paint);
		}
		
	}	
}
