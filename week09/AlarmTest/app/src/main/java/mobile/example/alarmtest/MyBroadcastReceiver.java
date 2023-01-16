package mobile.example.alarmtest;

import android.app.PendingIntent;
import android.content.*;
import android.widget.*;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class MyBroadcastReceiver extends BroadcastReceiver {
	public void onReceive(Context context, Intent intent) {
		Toast.makeText(context, "one time!", Toast.LENGTH_LONG).show();

		// Notification 출력
		// 메인에서 만드는 게 아니고 이 클래스는 액티비티가 아니라서, this 자리에 this 넣을 수 x
		// 매개변수로 들어오는 context를 Intent에 넣는다.
		intent = new Intent(context, MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0); // activity를 받아오는 pendingIntent 객체 생성

		// 알람 생성
		NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "MY_CHANNEL")
				.setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle("기상 시간")
				.setContentText("일어나! 공부할 시간이야!")
				.setStyle(new NotificationCompat.BigTextStyle()
						.bigText("일어나! 공부할 시간이야!\n모바일 응용 9주차 실습입니다."))
				.setPriority(NotificationCompat.PRIORITY_DEFAULT)
				.addAction(R.drawable.ic_launcher, "noti", pendingIntent)
				.setAutoCancel(true);

		// 알람 생성
		NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
		int notificationId = 100;
		notificationManager.notify(notificationId, builder.build());
	}
}