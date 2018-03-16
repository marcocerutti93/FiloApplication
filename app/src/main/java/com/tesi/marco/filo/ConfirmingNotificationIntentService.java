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

public class ConfirmingNotificationIntentService extends IntentService{

    private static final int NOTIFICATION_ID = 2;
    private static final String POSITIVE_ACTION = "YES", NEGATIVE_ACTION = "NO";
    private int tab;

    public ConfirmingNotificationIntentService(){
        super("ConfirmingNotificationIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent){
        Intent positiveIntent = new Intent(this, ConfirmingNotificationReceiver.class);
        positiveIntent.putExtra("id", NOTIFICATION_ID);
        positiveIntent.setAction(POSITIVE_ACTION);
        PendingIntent positivePendingIntent = PendingIntent.getBroadcast(this, 0, positiveIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Intent negativeIntent = new Intent(this, ConfirmingNotificationReceiver.class);
        negativeIntent.putExtra("id", NOTIFICATION_ID);
        negativeIntent.setAction(NEGATIVE_ACTION);
        PendingIntent negativePendingIntent = PendingIntent.getBroadcast(this, 0, negativeIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder confirmBuilder = new Notification.Builder(this);
        confirmBuilder.setContentTitle(getString(R.string.app_name));
        confirmBuilder.setContentText(getString(R.string.medicine_today));
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            confirmBuilder.setSmallIcon(R.drawable.ic_heart_notification);
            confirmBuilder.setColor(getResources().getColor(R.color.backgroundNotification));
            confirmBuilder.setPriority(Notification.PRIORITY_DEFAULT);
        } else {
            confirmBuilder.setSmallIcon(R.mipmap.ic_heart);
        }
        confirmBuilder.setAutoCancel(true);
        confirmBuilder.setOngoing(true);
        confirmBuilder.setDefaults(Notification.DEFAULT_SOUND|Notification.DEFAULT_LIGHTS|Notification.DEFAULT_VIBRATE);
    //    confirmBuilder.addAction(R.mipmap.ic_yes ,getString(R.string.yes),positivePendingIntent);
    //    confirmBuilder.addAction(R.mipmap.ic_no ,getString(R.string.no),negativePendingIntent);

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

        Intent ConfirmNotifyIntent = new Intent(this, TherapyActivity.class);
        ConfirmNotifyIntent.putExtra("tab",tab);
        ConfirmNotifyIntent.putExtra("confirmation","confirm");
        PendingIntent ConfirmPendingIntent = PendingIntent.getActivity(this, 2, ConfirmNotifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        confirmBuilder.setContentIntent(ConfirmPendingIntent);
        Notification confirmNotificationCompat = confirmBuilder.getNotification();
        NotificationManagerCompat confirmManagerCompat = NotificationManagerCompat.from(this);
        confirmManagerCompat.notify(NOTIFICATION_ID,confirmNotificationCompat);

    }
}
