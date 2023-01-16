package ddwu.com.mbile.example.notitest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createNotificationChannel(); // onCreate()에서 알림 채널 생성
    }

    public void onClick (View v) {
        switch (v.getId()) {
            case R.id.btnNoti:
                // 알림 실행 시 알림 버튼 탭 시 실행할 동작 지정
                Intent intent = new Intent(this, NotiActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

                // 알림 생성
                NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "MY_CHANNEL")
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Notification")
                        .setContentText("This is notification")
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText("This is notification\n알림의 확장된 영역에 표시되는 컨텐츠입니다."))
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .addAction(R.drawable.ic_launcher_foreground, "noti", pendingIntent) // noti 버튼
                        .setAutoCancel(true);
                // pendingIntent를 addAction에 전달 (여기서 메소드 인자로 넣는 것을 전달이라고 표현하는 듯)

                // 알림 실행
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
                int notificationId = 100; // 알림 구분 위한 정수형 식별자 지정
                notificationManager.notify(notificationId, builder.build()); // 생성 알림 실행

                break;
        }
    }


    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);       // strings.xml 에 채널명 기록
            String description = getString(R.string.channel_description);       // strings.xml에 채널 설명 기록
            int importance = NotificationManager.IMPORTANCE_DEFAULT;    // 알림의 우선순위 지정
            NotificationChannel channel = new NotificationChannel(getString(R.string.CHANNEL_ID), name, importance);    // CHANNEL_ID 지정
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);  // 채널 생성
            notificationManager.createNotificationChannel(channel);
        }
    }

}
