package ddwu.com.mobile.multimedia.photomemo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddMemoActivity extends AppCompatActivity {

    private static final int REQUEST_TAKE_PHOTO = 200;

    private String mCurrentPhotoPath;

    ImageView ivPhoto;
    EditText etMemo;

    MemoDBHelper helper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_memo);

        helper = new MemoDBHelper(this);

        ivPhoto = (ImageView)findViewById(R.id.ivPhoto);
        etMemo = (EditText)findViewById(R.id.etMemo);

        ivPhoto.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
//                    외부 카메라 호출
                    // 여기서 카메라 호출하면 된다
                    // PHOTOCAPTURE에서 필요한거 복사해오기
                    dispatchTakePictureIntent ();
                    return true;
                }
                return false;
            }
        });

    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePictureIntent.resolveActivity (getPackageManager()) != null){
            File photoFile = null;
            try{
                photoFile = createImageFile();
            } catch (IOException ex){
                ex.printStackTrace();
            }

            // 파일을 정상 생성하였을 경우
            if(photoFile != null){
                Uri photoURI = FileProvider.getUriForFile(this, "ddwu.com.mobile.multimedia.photomemo.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult (takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    /*현재 시간 정보를 사용하여 파일 정보 생성*/
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat ("yyyyMMdd_HHmmss").format(new Date ());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    public void onClick(View v) {
        switch (v.getId ()) {
            case R.id.btnSave:
//                DB에 촬영한 사진의 파일 경로 및 메모 저장
                // 저장한 카메라 사진 경로도 저장해놔야해
                // 사진 파일 위치와 이름을 알 수 있음
                // DB에는 파일명으로 저장하면 된다
                // 사진 마음에 안들면 다시 찍을 수도 이어
                // 메모 안했는데 사진 또 찍을 수도 있어
                // DB에 저장할 때에는 가장 마지막에 찍은 사진만 저장하면 된다
                // 나머지 파일은 제거해야 한다
                // 이 말은 즉슨, 지금 파일을 찍어서 저장하는 위치가 임시 저장 공간이 되어야 한다는 뜻
                // 사진 실제 저장하는 위치로 파일을 옮기는 작업이 필요
                // 나중에 옮겨간 위치에서 읽어오는 작업도 필요
                // 사진을 여러장 찍어서 하나만 저장하게 되는 경우
                // 영구적으로 저장해야 되면 현재 위치에 저장하면 안되고 다른 폴더를 하나 더 만들고 그 폴더의 이름을 저장해야 한다
                // 갤러리 파일 그대로 쓴다. 다른 앱에서도 접근 가능
                // 갤러리 사진의 uri의 사진 정보를 보고 내 폴더로 카피해와야 한다.

                
                String memo = etMemo.getText().toString(); // 작성한 메모 내용 가져오기
                Toast.makeText (this, "Save!", Toast.LENGTH_SHORT).show ();
            case R.id.btnCancel:
                finish ();
                break;
        }
    }
}
