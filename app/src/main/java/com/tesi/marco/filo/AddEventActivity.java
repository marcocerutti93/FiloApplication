package com.tesi.marco.filo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddEventActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private String Uid;
    private DatabaseReference myRef;
    private Button ButtonTherapy, ButtonPhysicalExamination, ButtonHospitalization;
    private Button ButtonBleeding, ButtonExaminationData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        mAuth = FirebaseAuth.getInstance();
        Uid = mAuth.getCurrentUser().getUid();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("events").child(Uid);

        ButtonTherapy = (Button) findViewById(R.id.add_therapy_event);
        ButtonPhysicalExamination = (Button) findViewById(R.id.add_physical_examination_event);
        ButtonHospitalization = (Button) findViewById(R.id.add_hospitalization_event);
        ButtonBleeding = (Button) findViewById(R.id.add_bleeding_event);
        ButtonExaminationData = (Button) findViewById(R.id.add_examination_data);

        ButtonTherapy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AddEventActivity.this, "therapy", Toast.LENGTH_SHORT).show();
            }
        });

        ButtonPhysicalExamination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AddEventActivity.this, "physexam", Toast.LENGTH_SHORT).show();

            }
        });

        ButtonHospitalization.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AddEventActivity.this, "hospi", Toast.LENGTH_SHORT).show();

            }
        });

        ButtonBleeding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AddEventActivity.this, "bleed", Toast.LENGTH_SHORT).show();

            }
        });

        ButtonExaminationData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AddEventActivity.this, "examdata", Toast.LENGTH_SHORT).show();

            }
        });
    }
}
