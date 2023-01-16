package mobile.example.savegalleryimagetest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends Activity {
	
	private static final int CALL_GALLERY = 0;
	private String mPath;
	private String mFileName;
	private ImageView ivImage;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		ivImage = (ImageView)findViewById(R.id.ivImage);
		
//		mPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/tmp";
//		new File(mPath).mkdir();
		
	}	
	
	public void onClick(View v) {
		Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		switch(v.getId()) {
		case R.id.btnGallery:
			startActivityForResult(intent, CALL_GALLERY);
			break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == RESULT_OK) {
			switch(requestCode) {
			case CALL_GALLERY:
//				mFileName = mPath + "/image.png";
//				copyImageFile(mFileName, data.getData());
//                ivImage.setImageBitmap(BitmapFactory.decodeFile(mFileName));
				ivImage.setImageURI(data.getData());
				break;
			}
		}
	}

//	private void copyImageFile(String destFileName, Uri selectedImageUri){
//
//		File sourceFile = new File(getRealPathFromURI(selectedImageUri));
//		File destFile = new File(destFileName);
//
//		if (!sourceFile.exists()) return;
//
//		FileChannel source = null;
//		FileChannel destination = null;
//
//		try {
//			source = new FileInputStream(sourceFile).getChannel();
//	        destination = new FileOutputStream(destFile).getChannel();
//
//	        if (destination != null && source != null) {
//	            destination.transferFrom(source, 0, source.size());
//	        }
//
//	        if (source != null) source.close();
//
//	        if (destination != null) destination.close();
//
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//    }
//
//    private String getRealPathFromURI(Uri contentUri) {
//       String[] proj = { MediaStore.Video.Media.DATA };
//       Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
//       int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//       cursor.moveToFirst();
//       return cursor.getString(column_index);
//    }
	
}