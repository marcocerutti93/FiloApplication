package com.tesi.marco.filo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Marco on 07/03/2018.
 */

public class NotificationReceiver extends BroadcastReceiver {

    public NotificationReceiver(){}

    @Override
    public void onReceive (Context context, Intent intent){

        Intent notificationIntent = new Intent(context, NotificationIntentService.class);
        context.startService(notificationIntent);

    }
}
