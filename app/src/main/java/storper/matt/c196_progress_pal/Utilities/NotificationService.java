package storper.matt.c196_progress_pal.Utilities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import storper.matt.c196_progress_pal.R;

public class NotificationService extends BroadcastReceiver {
    private int notificationId;
    private String notificationChannelId;

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle extras = intent.getExtras();

        if(extras != null) {
            notificationId = extras.getInt("notificationId");
            notificationChannelId = extras.getString("notificationChannelId");
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, notificationChannelId)
                .setSmallIcon(R.drawable.ic_baseline_add_alert_24)
                .setContentTitle("Alarm Test")
                .setContentText("This is an alarm test")
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_MAX);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        System.out.println(notificationId);
        notificationManager.notify(notificationId, builder.build());
    }
}
