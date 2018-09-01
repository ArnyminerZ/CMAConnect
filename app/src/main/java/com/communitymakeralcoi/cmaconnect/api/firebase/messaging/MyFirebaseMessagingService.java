package com.communitymakeralcoi.cmaconnect.api.firebase.messaging;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import com.communitymakeralcoi.cmaconnect.MainActivity;
import com.communitymakeralcoi.cmaconnect.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import androidx.core.app.NotificationCompat;

import static com.communitymakeralcoi.cmaconnect.api.config.AppConfigKt.sharedPreferences;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    String tag = "MyFirebaseMessagingService";

    @Override
    public void onMessageReceived(RemoteMessage message) {
        if(message.getNotification() != null){
            RemoteMessage.Notification notification = message.getNotification();
            sendMyNotification(notification.getBody(), notification.getTitle());
        }
    }


    private void sendMyNotification(String message, String title) {
        if(!sharedPreferences.getBoolean("server_notifications_enable", true)) {
            Log.w(tag, "Received show notification but server notifications are disabled");
            return;
        }
        //On click of notification it redirect to this Activity
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri soundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(soundUri)
                .setChannelId(sharedPreferences.getString("fcm_notification_channel_id", ""))
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (notificationManager != null)
            notificationManager.notify(0, notificationBuilder.build());
    }
}