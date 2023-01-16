package mobile.example.alarmtest;

import android.app.PendingIntent;
import android.content.*;
import android.widget.*;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;


public class RepeatReceiver extends BroadcastReceiver {
	public void onReceive(Context context, Intent intent) {
		Toast.makeText(context, "Hi all!", Toast.LENGTH_SHORT).show();

		intent = new Intent(context, MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

		NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "MY_CHANNEL")
				.setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle("기상 시간")
				.setContentText("일어나! 공부할 시간이야!")
				.setStyle(new NotificationCompat.BigTextStyle()
						.bigText("일어나! 공부할 시간이야!\n모바일 응용 9주차 실습입니다. version.2"))
				.setPriority(NotificationCompat.PRIORITY_DEFAULT)
				.addAction(R.drawable.ic_launcher, "noti", pendingIntent)
				.setAutoCancel(true);

		NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
		int notificationId = 100;
		notificationManager.notify(notificationId, builder.build());

	}
}
