package com.pritesh.repeatingalarmserviceandroid;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import java.sql.Timestamp;
import java.util.Date;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class ShowNotificationIntentService extends IntentService
{
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    public static final String ACTION_FOO = "com.pritesh.repeatingalarmserviceandroid.action.FOO";
    public static final String ACTION_BAZ = "com.pritesh.repeatingalarmserviceandroid.action.BAZ";

    // TODO: Rename parameters
    public static final String EXTRA_PARAM1 = "com.pritesh.repeatingalarmserviceandroid.extra.PARAM1";
    public static final String EXTRA_PARAM2 = "com.pritesh.repeatingalarmserviceandroid.extra.PARAM2";

    public ShowNotificationIntentService()
    {
        super("ShowNotificationIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
        if(intent != null)
        {
            final String action = intent.getAction();
            if(ACTION_FOO.equals(action))
            {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionFoo(param1, param2);
            } else if(ACTION_BAZ.equals(action))
            {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionBaz(param1, param2);
            }
        }
        Timestamp stamp = new Timestamp(System.currentTimeMillis());
        Date date = new Date(stamp.getTime());

        String time = String.valueOf(intent.getLongExtra("TIME", System.currentTimeMillis()));

        customNotification(date.toString());
        Intent mIntent = new Intent();
        mIntent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        mIntent.setAction("com.pritesh.BroadcastReceiver");
        mIntent.putExtra("TIME", date.toString());
        sendBroadcast(mIntent);
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo(String param1, String param2)
    {
        // TODO: Handle action Foo
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2)
    {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void customNotification(String time)
    {
        //TODO - Notification FOR > Orio refer : https://github.com/firebase/quickstart-android/blob/3c2a1684eba8f43e65cff3fe7b0da52ca5f88fa4/messaging/app/src/main/java/com/google/firebase/quickstart/fcm/java/MyFirebaseMessagingService.java
        NotificationManager notificationManager;
        Intent notificationIntent;
        int notificationId = 0;
        String channelId = getString(R.string.default_notification_channel_id);

        notificationIntent = new Intent(getApplicationContext(), MainActivity.class);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        notificationIntent.setAction("Yes");

        String title = "Hello World";
        String alertMessage = "I am testing Alarm Services - " + time;

        PendingIntent pendingNotificationIntent = PendingIntent.getActivity(this, notificationId, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        //Optional
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntent(notificationIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
                0,
                PendingIntent.FLAG_UPDATE_CURRENT
        );


        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setDefaults(Notification.DEFAULT_ALL)
                .setColor(getResources().getColor(R.color.color_app))
                .setSmallIcon(R.drawable.ic_notification)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(alertMessage))
                .setContentText(alertMessage)
                .setContentIntent(pendingNotificationIntent)
                //.setContentIntent(resultPendingIntent)
                .setContentTitle(title)
                .setAutoCancel(true);

        notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_HIGH);
            //channel.setDescription("This notification is for Toyota Dealer");
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(notificationId, notificationBuilder.build());
    }
}
