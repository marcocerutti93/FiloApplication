package com.tesi.marco.filo;

import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationManagerCompat;

import java.util.Calendar;

/**
 * Created by Marco on 07/03/2018.
 */

public class NotificationIntentService extends IntentService {

    private static final int NOTIFICATION_ID = 1;
    private int tab;

    public NotificationIntentService(){
        super("NotificationIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent){
        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentTitle(getString(R.string.app_name));
        builder.setContentText(getString(R.string.remember_medicine));
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder.setSmallIcon(R.drawable.ic_heart_notification);
            builder.setColor(getResources().getColor(R.color.backgroundNotification));
            builder.setPriority(Notification.PRIORITY_DEFAULT);
        } else {
            builder.setSmallIcon(R.mipmap.ic_heart);
        }
        builder.setAutoCancel(true);
        builder.setDefaults(Notification.DEFAULT_SOUND|Notification.DEFAULT_LIGHTS|Notification.DEFAULT_VIBRATE);

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        switch (day) {
            case Calendar.SUNDAY:
                tab = 6;
                break;
            case Calendar.MONDAY:
                tab = 0;
                break;
            case Calendar.TUESDAY:
                tab = 1;
                break;
            case Calendar.WEDNESDAY:
                tab = 2;
                break;
            case Calendar.THURSDAY:
                tab = 3;
                break;
            case Calendar.FRIDAY:
                tab = 4;
                break;
            case Calendar.SATURDAY:
                tab = 5;
                break;
        }

        Intent notifyIntent = new Intent(this, TherapyActivity.class);
        notifyIntent.putExtra("tab",tab);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        Notification notificationCompat = builder.getNotification();
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        managerCompat.notify(NOTIFICATION_ID,notificationCompat);

    }
}
