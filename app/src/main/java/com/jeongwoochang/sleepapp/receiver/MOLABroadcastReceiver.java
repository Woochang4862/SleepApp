package com.jeongwoochang.sleepapp.receiver;

/**
 * Created by Ilya Anshmidt on 19.09.2017.
 */

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import android.util.Log;
import com.jeongwoochang.sleepapp.R;
import com.jeongwoochang.sleepapp.activity.MainActivity;

public class MOLABroadcastReceiver extends BroadcastReceiver {

    private int FAST_ALARM_REQUEST_CODE = 1000;
    private int FAST_FAILED_CODE = 1001;
    private int FAST_SUCCESS_CODE = 1002;
    private String LOG_TAG = MOLABroadcastReceiver.class.getSimpleName();
    private String CHANNEL_ID = "1000";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(LOG_TAG, "Notification Created");

        createNotificationChannel(context);
        long[] pattern = {0, 300, 0};
        Intent _intent = new Intent(context, MainActivity.class);
        _intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pi = PendingIntent.getActivity(context, 111, _intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Intent noI = new Intent(context, MainActivity.class);
        Intent yesI = new Intent(context, MainActivity.class);
        noI.putExtra("FAST_ACHIEVEMENT", FAST_FAILED_CODE);
        yesI.putExtra("FAST_ACHIEVEMENT", FAST_FAILED_CODE);
        PendingIntent noPI = PendingIntent.getActivity(context, FAST_FAILED_CODE, noI, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent yesPI = PendingIntent.getActivity(context, FAST_SUCCESS_CODE, yesI, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("금식 알림")
                .setContentText("자기 전 금식 하겼나요?")
                .setVibrate(pattern)
                .addAction(R.drawable.ic_close, "아니요", noPI)
                .addAction(R.drawable.ic_check, "예", yesPI)
                .setPriority(NotificationManager.IMPORTANCE_HIGH)
                .setAutoCancel(false)
                .setOngoing(true)
                .setContentIntent(pi);


        mBuilder.setDefaults(Notification.DEFAULT_SOUND);
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(FAST_ALARM_REQUEST_CODE, mBuilder.build());
    }

    private void createNotificationChannel(Context context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "fast_channel";
            String description = "fast_channel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
