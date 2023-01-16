package mobile.example.savecameraimagetest;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends Activity {

	private static final int CALL_CAMERA_THUMBNAIL = 0;
	private static final int CALL_CAMERA = 1;
	
	private String saveFilePath;
	private String saveFileName;
	private ImageView ivImage;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		saveFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/tmp";
		new File(saveFilePath).mkdir();
		
		ivImage = (ImageView)findViewById(R.id.ivImage);
	}
	
	public void onClick(View v) {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		switch(v.getId()) {
		case R.id.btnCameraThumbnail:
			startActivityForResult(intent, CALL_CAMERA_THUMBNAIL);
			break;
		case R.id.btnCamera:
			saveFileName = saveFilePath + "/image.jpg";
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(saveFileName)));
			startActivityForResult(intent, CALL_CAMERA);
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent receivedData) {
		if(resultCode == RESULT_OK) {
			switch(requestCode) {
			case CALL_CAMERA_THUMBNAIL:
				ivImage.setImageBitmap((Bitmap)receivedData.getExtras().get("data"));
				break;
			case CALL_CAMERA:
				ivImage.setImageBitmap(BitmapFactory.decodeFile(saveFileName));
//				sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + mFileName)));
				break;
			}
		}
	}
	
	
	
	
}
