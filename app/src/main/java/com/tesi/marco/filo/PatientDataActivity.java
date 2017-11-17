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
    private TextView tvName, tvDob, tvCode;
    private TextView tvGender, tvSmoker, tvHypertens, tvDyslip, tvDM, tvPreStr, tvPreAngio, tvLVEF;
    private FirebaseDatabase db;
    private DatabaseReference userRef, infoRef;
    private String dateOfBirth, code;
    private String Uid, dispName;
    private String gender, smoker, hypertens, dyslip, dm, preStr, preAngio;
    private View mProgressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_data);

        mProgressView = findViewById(R.id.data_progress_bar);
        mProgressView.setVisibility(View.VISIBLE);

        // Views
        tvName = (TextView) findViewById(R.id.patient_name);
        tvDob = (TextView) findViewById(R.id.date_of_birth_data);
        tvCode = (TextView) findViewById(R.id.fiscal_code_data);
        tvGender = (TextView) findViewById(R.id.gender_value);
        tvSmoker = (TextView) findViewById(R.id.smoker_value);
        tvHypertens = (TextView) findViewById(R.id.hypertension_value);
        tvDyslip = (TextView) findViewById(R.id.dyslipidemia_value);
        tvDM = (TextView) findViewById(R.id.diabetic_value);
        tvPreStr = (TextView) findViewById(R.id.previous_heart_attack_value);
        tvPreAngio = (TextView) findViewById(R.id.previous_angioplasty_value);

        auth = FirebaseAuth.getInstance();
        Uid = auth.getCurrentUser().getUid();
        dispName = auth.getCurrentUser().getDisplayName();

        if (dispName != null){
            tvName.setText(dispName);
        } else {
            tvName.setText("Non presente");
        }

        db = FirebaseDatabase.getInstance();
        userRef = db.getReference().child("patients").child(Uid);
        infoRef = userRef.child("GeneralInformation");

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dateOfBirth = dataSnapshot.child("DateOfBirth").getValue().toString();
                code = dataSnapshot.child("FiscalCode").getValue().toString();
                tvDob.setText(dateOfBirth);
                tvCode.setText(code);

                mProgressView.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        infoRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("Gender")) {
                    gender = dataSnapshot.child("Gender").getValue().toString();
                    tvGender.setText(gender);
                } else { tvGender.setText("non registrato");}
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}