package com.Cyris.kbc2020;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NotificationClass extends BroadcastReceiver {

    String CHANNEL_ID="NOTIFICATION_CHANNEL";
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {

        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.kbc2020_min);

        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                new Intent(context, SplashScreen.class), PendingIntent.FLAG_UPDATE_CURRENT);
        try {
            Notification notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setLargeIcon(bitmap)
                    .setSmallIcon(R.drawable.kbc2020_min)
                    .setContentTitle("KBC 2020")
                    .setContentText("Let's Play KBC and make your Records..")
                    .setContentIntent(contentIntent)
                    .build();
            // Show notification
            Log.i("checkNotification", "workedNow");
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Notification", NotificationManager.IMPORTANCE_DEFAULT);
                manager.createNotificationChannel(channel);
            }

            manager.notify(42, notification);
        } catch (Exception e)
        {
            Log.i("Notification Error","Notification Error");
        }
    }
}
