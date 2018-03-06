package com.tesi.marco.filo;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private ImageButton addData, medicine, info, signOutButton;
    private int day, tab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addData = (ImageButton) findViewById(R.id.add_data_button);
        addData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AddDataActivity.class));
            }
        });

        medicine = (ImageButton) findViewById(R.id.medicine_button);
        medicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                day = cal.get(Calendar.DAY_OF_WEEK);
                switch (day){
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
                Intent intent = new Intent(MainActivity.this, TherapyActivity.class);
                intent.putExtra("tab",tab);
                startActivity(intent);
            }
        });

        info = (ImageButton) findViewById(R.id.patient_information_button);
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, PatientDataActivity.class));
            }
        });

        signOutButton = (ImageButton) findViewById(R.id.logout_button);
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });


    }

    private void signOut() {
        mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
