package com.tesi.marco.filo;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class PatientDataActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private TextView userData;
    private Spinner fvsSpinner;
    private ArrayAdapter<Integer> fvsSpinnerAdapter;
    private FirebaseDatabase db;
    private DatabaseReference userRef;
    private String name, surname;
    private static TextView dob;
    private static String dateOfBirth;
    private static String Uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_data);

        userData = (TextView) findViewById(R.id.name_surname);
        auth = FirebaseAuth.getInstance();
        Uid = auth.getCurrentUser().getUid();
        db = FirebaseDatabase.getInstance();
        userRef = db.getReference().child("patients").child(Uid);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                name = dataSnapshot.child("Name").getValue().toString();
                surname = dataSnapshot.child("Surname").getValue().toString();
                Toast.makeText(PatientDataActivity.this,name + " " + surname,Toast.LENGTH_LONG).show();
                userData.setText(name + " " + surname);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        dob = (TextView) findViewById(R.id.date_of_birth_text2);
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        dateOfBirth = String.valueOf(day) + "/" + String.valueOf(month) + "/" + String.valueOf(year);
        dob.setText(dateOfBirth);
        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });

        fvsSpinner = (Spinner) findViewById(R.id.left_ventricular_function_spinner);
        Integer[] perc = new Integer[101];
        for (int i = 0; i < perc.length; ++i) {
            perc[i] = i;
        }
        fvsSpinnerAdapter = new ArrayAdapter<>(this,R.layout.spinner_item, perc);
        fvsSpinner.setAdapter(fvsSpinnerAdapter);
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            dateOfBirth = String.valueOf(day) + "/" + String.valueOf(month) + "/" + String.valueOf(year);
            dob.setText(dateOfBirth);
        }
    }

}