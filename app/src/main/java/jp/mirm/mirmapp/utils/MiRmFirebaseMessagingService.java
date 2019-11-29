package jp.mirm.mirmapp.utils;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import jp.mirm.mirmapp.servermain.MainActivity;
import jp.mirm.mirmapp.R;

import java.util.Map;

public class MiRmFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Map<String, String> data = remoteMessage.getData();
        String title = data.get("title");
        String body = data.get("body");

        NotificationCompat.Builder notificationCompatBuilder = new NotificationCompat.Builder(this);
        notificationCompatBuilder.setSmallIcon(R.drawable.ic_menu_manage);
        notificationCompatBuilder.setBadgeIconType(R.mipmap.icon1);
        notificationCompatBuilder.setColor(getResources().getColor(R.color.colorPrimary));
        notificationCompatBuilder.setContentTitle(title);
        notificationCompatBuilder.setContentText(body);
        notificationCompatBuilder.setDefaults(Notification.DEFAULT_ALL);
        notificationCompatBuilder.setAutoCancel(true);

        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        notificationCompatBuilder.setContentIntent(pendingIntent);
        notificationCompatBuilder.setFullScreenIntent(pendingIntent, false);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(346, notificationCompatBuilder.build());
    }

}
