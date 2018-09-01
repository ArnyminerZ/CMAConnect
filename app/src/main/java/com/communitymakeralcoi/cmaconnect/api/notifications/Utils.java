package com.communitymakeralcoi.cmaconnect.api.notifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

public class Utils {
    public static void createNotificationChannel(Context context, String CHANNEL_ID, String name, String description, int importance) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            if (notificationManager != null)
                notificationManager.createNotificationChannel(channel);
        }
    }
}
