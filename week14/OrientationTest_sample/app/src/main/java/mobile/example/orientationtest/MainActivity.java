package mobile.example.orientationtest;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

	private TextView tvText;
	private SensorManager sensorManager;
	float[] mGravity;
	float[] mMagenetic;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		tvText = findViewById(R.id.tvText);
		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
	}

	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.btnStart:
		Sensor accelerometer = sensorManager.getDefaultSensor (Sensor.TYPE_ACCELEROMETER);
		Sensor magnetometer = sensorManager.getDefaultSensor (Sensor.TYPE_MAGNETIC_FIELD);
		int sensorDelay = SensorManager.SENSOR_DELAY_NORMAL;

		sensorManager.registerListener(sensorEventListener, accelerometer, sensorDelay);
		sensorManager.registerListener(sensorEventListener, magnetometer, sensorDelay);

		break;
		}
	}
	
	
	@Override
	protected void onPause() {
		super.onPause();
		sensorManager.unregisterListener (sensorEventListener);
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

					String result = String.format("azimuth : %f\npitch : %f\nroll : %f\n",
							values[0], values[1], values[2] );
					tvText.setText(result);
				}
			}

		}

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {

		}
	};
	
}
