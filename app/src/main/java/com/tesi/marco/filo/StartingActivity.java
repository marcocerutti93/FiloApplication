package com.tesi.marco.filo;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;

public class StartingActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting);

        Intent notificationIntent1 = new Intent(this, NotificationReceiver.class);
        Intent notificationIntent2 = new Intent(this, NotificationReceiver.class);
        Intent notificationIntent3 = new Intent(this, NotificationReceiver.class);
        Intent notificationIntent4 = new Intent(this, ConfirmingNotificationReceiver.class);
        PendingIntent pendingIntent1 = PendingIntent.getBroadcast(StartingActivity.this, 1,
                notificationIntent1, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendingIntent2 = PendingIntent.getBroadcast(StartingActivity.this, 2,
                notificationIntent2, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendingIntent3 = PendingIntent.getBroadcast(StartingActivity.this, 3,
                notificationIntent3, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendingIntent4 = PendingIntent.getBroadcast(StartingActivity.this, 4,
                notificationIntent4, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmManager1 = (AlarmManager) getBaseContext().getSystemService(Context.ALARM_SERVICE);
        AlarmManager alarmManager2 = (AlarmManager) getBaseContext().getSystemService(Context.ALARM_SERVICE);
        AlarmManager alarmManager3 = (AlarmManager) getBaseContext().getSystemService(Context.ALARM_SERVICE);
        AlarmManager alarmManager4 = (AlarmManager) getBaseContext().getSystemService(Context.ALARM_SERVICE);


        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTimeInMillis(System.currentTimeMillis());
        calendar1.set(Calendar.HOUR_OF_DAY, 10);
        calendar1.set(Calendar.MINUTE, 1);
        calendar1.set(Calendar.SECOND, 1);
        alarmManager1.setRepeating(AlarmManager.RTC_WAKEUP, calendar1.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent1);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTimeInMillis(System.currentTimeMillis());
        calendar2.set(Calendar.HOUR_OF_DAY, 14);
        calendar2.set(Calendar.MINUTE, 1);
        calendar2.set(Calendar.SECOND, 1);
        alarmManager2.setRepeating(AlarmManager.RTC_WAKEUP, calendar2.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent2);
        Calendar calendar3 = Calendar.getInstance();
        calendar3.setTimeInMillis(System.currentTimeMillis());
        calendar3.set(Calendar.HOUR_OF_DAY, 18);
        calendar3.set(Calendar.MINUTE, 1);
        calendar3.set(Calendar.SECOND, 1);
        alarmManager3.setRepeating(AlarmManager.RTC_WAKEUP, calendar3.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent3);
        Calendar calendar4 = Calendar.getInstance();
        calendar4.setTimeInMillis(System.currentTimeMillis());
        calendar4.set(Calendar.HOUR_OF_DAY, 22);
        calendar4.set(Calendar.MINUTE, 1);
        calendar4.set(Calendar.SECOND, 1);
        alarmManager4.setRepeating(AlarmManager.RTC_WAKEUP, calendar4.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent4);


        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(StartingActivity.this, MainActivity.class));
            finish();
        } else {
            startActivity(new Intent(StartingActivity.this, LoginActivity.class));
            finish();
        }
    }
}
