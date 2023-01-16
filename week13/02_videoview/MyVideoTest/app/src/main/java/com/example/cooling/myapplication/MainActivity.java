package com.example.cooling.myapplication;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    final static int PERMISSION_REQ_CODE = 100;

    private SurfaceView mPreview;
    private SurfaceHolder mHolder;
    MediaPlayer mPlayer;
    Button mPlayBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPreview = (SurfaceView)findViewById(R.id.svDisplay);
        mHolder = mPreview.getHolder();
        mHolder.addCallback(displayCallback);

        mPlayBtn = (Button)findViewById(R.id.btnPlay);

        //		외부저장소 접근을 위한 권한 확인
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQ_CODE);
        }

    }

    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btnPlay:
                if (!mPlayer.isPlaying()) {
                    mPlayer.start();
                    mPlayBtn.setText("PAUSE");
                } else {
                    mPlayer.pause();
                    mPlayBtn.setText("PLAY");
                }
                break;
            case R.id.btnStop:
                mPlayer.stop();
                mPlayBtn.setText("PLAY");
                try {
                    mPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
    }


    SurfaceHolder.Callback displayCallback = new SurfaceHolder.Callback() {

        @Override
        public void surfaceCreated(SurfaceHolder surfaceHolder) {
            if (mPlayer == null) {
                mPlayer = new MediaPlayer();
            } else {
                mPlayer.reset();
            }

            try {
                String sd = Environment.getExternalStorageDirectory().getAbsolutePath();
                mPlayer.setDataSource(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES) + "/stand.mp4");
//                mPlayer.setDataSource(getResources().getString(R.string.movie_url));
                mPlayer.setDisplay(surfaceHolder);
                mPlayer.prepare();
                mPlayer.setOnCompletionListener(completionListener);
                mPlayer.setOnVideoSizeChangedListener(sizeChangeListener);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        MediaPlayer.OnCompletionListener completionListener = new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mPlayBtn.setText("PLAY");
            }
        };

        MediaPlayer.OnVideoSizeChangedListener sizeChangeListener = new MediaPlayer.OnVideoSizeChangedListener() {
            @Override
            public void onVideoSizeChanged(MediaPlayer mediaPlayer, int i, int i1) {

            }
        };

        @Override
        public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPlayer != null) {
            mPlayer.release();
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
