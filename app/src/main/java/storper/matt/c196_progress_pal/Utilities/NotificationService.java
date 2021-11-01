package storper.matt.c196_progress_pal.Utilities;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.Date;

import storper.matt.c196_progress_pal.Activities.ModifyTermActivity;
import storper.matt.c196_progress_pal.R;

public class NotificationService extends BroadcastReceiver {
    private DateConverter dateConverter = new DateConverter();
    private int notificationId;
    private String notificationChannelId;
    private String notificationTitle;
    private String notificationDescription;
    private String dateString;
    private Date notificationDate;
    private Date now = new Date(System.currentTimeMillis());

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle extras = intent.getExtras();

        if(extras != null) {
            notificationId = extras.getInt("notificationId");
            notificationChannelId = extras.getString("notificationChannelId");
            notificationTitle = extras.getString("notificationTitle");
            notificationDescription = extras.getString("notificationDescription");
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, notificationChannelId)
                .setSmallIcon(R.drawable.ic_baseline_add_alert_24)
                .setContentTitle(notificationTitle)
                .setContentText(notificationDescription)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_MAX);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        System.out.println(notificationId);
        notificationManager.notify(notificationId, builder.build());
    }

    public PendingIntent setPendingIntent(Context context, int notificationId, String channelId, String title
            , String description) {
        Intent intent = new Intent(context, NotificationService.class);
        intent.putExtra("notificationId", notificationId);
        intent.putExtra("notificationChannelId", channelId);
        intent.putExtra("notificationTitle", title);
        intent.putExtra("notificationDescription", description);
        return PendingIntent.getBroadcast(context, notificationId, intent, 0);
    }





}
