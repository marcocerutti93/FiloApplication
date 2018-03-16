package com.tesi.marco.filo;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Marco on 07/03/2018.
 */

public class ConfirmingNotificationReceiver extends BroadcastReceiver{

    private static final String POSITIVE_ACTION = "YES", NEGATIVE_ACTION = "NO";

    public ConfirmingNotificationReceiver(){}

    @Override
    public void onReceive (Context context, Intent intent){

    /*    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ITALIAN);
        String myDate = dateFormat.format(Calendar.getInstance().getTime());

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String Uid = mAuth.getCurrentUser().getUid();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("data").child(Uid).child("Therapy").child(myDate);
    */
        Intent confirmingNotificationIntent = new Intent(context, ConfirmingNotificationIntentService.class);
        context.startService(confirmingNotificationIntent);

    /*    String action = intent.getAction();

        if (!action.isEmpty()){
            int id = intent.getIntExtra("id",0);

            if(POSITIVE_ACTION.equals(action)) {
                myRef.child("Therapy").setValue("Yes");
            } else if(NEGATIVE_ACTION.equals(action)) {
                myRef.child("Therapy").setValue("No");
            }

        }
    */

    }
}
