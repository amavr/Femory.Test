package com.amavr.femory;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.amavr.tools.NotificationID;
import com.amavr.tools.XMem;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

public class MsgService extends FirebaseMessagingService {

    private static final String TAG = "XDBG.svc";

    @Override
    public void onMessageReceived(RemoteMessage msg) {
        Log.d(TAG, "message received");
        RemoteMessage.Notification ntf = msg.getNotification();
        Log.d(TAG, "Notification Message Body: " + ntf.getBody());
        sendNotification(ntf.getBody());
    }

    @Override
    public void onNewToken(String token) {
        Log.d(TAG, String.format("new token: %s", token));
    }

    private void sendNotification(String messageBody) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, getString(R.string.channel_id))
                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .setContentTitle("FCM Message")
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        int id = NotificationID.getID();
        notificationManager.notify( id, notificationBuilder.build());
        Log.d(TAG, String.format("sent notification id: %s", id));
    }
}
