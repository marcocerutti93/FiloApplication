package com.tesi.marco.filo;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
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
    private TextView tvName, tvCode, tvDob, tvPob, tvRes, tvTel, tvGender;
    private TextView tvSmoker, tvHypertens, tvDyslip, tvDM, tvFam, tvChemo, tvRadio, tvHiv, tvKidFail, tvAllerg;
    private TextView tvBMI, tvPreSTEMI, tvPreNSTEMI, tvLVEF  ;
    private TextView tvInternistica, tvCardiologica, tvLastVisit, tvAdvices;
    private FirebaseDatabase db;
    private DatabaseReference userRef, personalDataRef, riskFactorsRef, statusRef, anamnesiRef;
    private String Uid, dispName;
    private String code, dateOfBirth, placeOfBirth, residency, telephone, gender;
    private String smoker, hypertens, dyslip, dm, familiarity, preChemo, preRadio, hiv, kidFail, allergies;
    private String bmi, preStemi, preNstemi, LVEF;
    private String internistica, cardiologica, lastVisit, advices;
    private View mProgressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_data);

        mProgressView = findViewById(R.id.data_progress_bar);
        mProgressView.setVisibility(View.VISIBLE);

        // Views
        tvName = (TextView) findViewById(R.id.patient_name);
        tvCode = (TextView) findViewById(R.id.fiscal_code_data);
        tvDob = (TextView) findViewById(R.id.date_of_birth_data);
        tvPob = (TextView) findViewById(R.id.place_of_birth_data);
        tvRes = (TextView) findViewById(R.id.residency_data);
        tvTel = (TextView) findViewById(R.id.telephone_data);
        tvGender = (TextView) findViewById(R.id.gender_data);
        tvSmoker = (TextView) findViewById(R.id.smoker_data);
        tvHypertens = (TextView) findViewById(R.id.hypertension_data);
        tvDyslip = (TextView) findViewById(R.id.dyslipidemia_data);
        tvDM = (TextView) findViewById(R.id.diabetic_data);
        tvFam = (TextView) findViewById(R.id.familiarity_data);
        tvChemo = (TextView) findViewById(R.id.previous_chemotherapy_data);
        tvRadio = (TextView) findViewById(R.id.previous_radiotherapy_data);
        tvHiv = (TextView) findViewById(R.id.hiv_data);
        tvKidFail = (TextView) findViewById(R.id.kidney_failure_data);
        tvAllerg = (TextView) findViewById(R.id.allergies_data);
        tvBMI = (TextView) findViewById(R.id.bmi_data);
        tvPreSTEMI = (TextView) findViewById(R.id.previous_stemi_data);
        tvPreNSTEMI = (TextView) findViewById(R.id.previous_nstemi_data);
        tvLVEF = (TextView) findViewById(R.id.left_ventricular_function_data);

        tvInternistica = (TextView) findViewById(R.id.internistica_text);
        tvCardiologica = (TextView) findViewById(R.id.cardiologica_text);
        tvLastVisit = (TextView) findViewById(R.id.last_visit_text);
        tvAdvices =(TextView) findViewById(R.id.advices_text);

        auth = FirebaseAuth.getInstance();
        Uid = auth.getCurrentUser().getUid();
        dispName = auth.getCurrentUser().getDisplayName();

        if (dispName != null){
            tvName.setText(dispName);
        } else {
            tvName.setText(getString(R.string.info_not_present));
        }

        db = FirebaseDatabase.getInstance();
        userRef = db.getReference().child("patients").child(Uid);
        personalDataRef = userRef.child("PersonalData");
        riskFactorsRef = userRef.child("RiskFactors");
        statusRef = userRef.child("Status");
        anamnesiRef = userRef.child("Anamnesis");

        personalDataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                code = dataSnapshot.child("FiscalCode").getValue().toString();
                tvCode.setText(code);
                dateOfBirth = dataSnapshot.child("DateOfBirth").getValue().toString();
                tvDob.setText(dateOfBirth);
                if (dataSnapshot.hasChild("Birthplace")) {
                    placeOfBirth = dataSnapshot.child("Birthplace").getValue().toString();
                    tvPob.setText(placeOfBirth);
                }
                if (dataSnapshot.hasChild("ResidencyCity") && !dataSnapshot.hasChild("ResidencyRoad")) {
                    residency = dataSnapshot.child("ResidencyCity").getValue().toString();
                    tvRes.setText(residency);
                }else if (dataSnapshot.hasChild("ResidencyCity") && dataSnapshot.hasChild("ResidencyRoad")) {
                    residency = dataSnapshot.child("ResidencyCity").getValue().toString() + "\n" +
                            dataSnapshot.child("ResidencyRoad").getValue().toString();
                    tvRes.setText(residency);
                }
                if (dataSnapshot.hasChild("Telephone")) {
                    telephone = dataSnapshot.child("Telephone").getValue().toString();
                    tvTel.setText(telephone);
                }
                if (dataSnapshot.hasChild("Gender")) {
                    gender = dataSnapshot.child("Gender").getValue().toString();
                    tvGender.setText(gender);
                }

                mProgressView.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        riskFactorsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("Smoker")) {
                    smoker = dataSnapshot.child("Smoker").getValue().toString();
                    tvSmoker.setText(smoker);
                }
                if (dataSnapshot.hasChild("Hypertension")) {
                    hypertens = dataSnapshot.child("Hypertension").getValue().toString();
                    tvHypertens.setText(hypertens);
                }
                if (dataSnapshot.hasChild("Dyslipidemia")) {
                    dyslip = dataSnapshot.child("Dyslipidemia").getValue().toString();
                    tvDyslip.setText(dyslip);
                }
                if (dataSnapshot.hasChild("Diabetic")) {
                    dm = dataSnapshot.child("Diabetic").getValue().toString();
                    tvDM.setText(dm);
                }
                if (dataSnapshot.hasChild("Familiarity")) {
                    familiarity = dataSnapshot.child("Familiarity").getValue().toString();
                    tvFam.setText(familiarity);
                }
                if (dataSnapshot.hasChild("PreviousChemio")) {
                    preChemo = dataSnapshot.child("PreviousChemio").getValue().toString();
                    tvChemo.setText(preChemo);
                }
                if (dataSnapshot.hasChild("PreviousRadio")) {
                    preRadio = dataSnapshot.child("PreviousRadio").getValue().toString();
                    tvRadio.setText(preRadio);
                }
                if (dataSnapshot.hasChild("HIVinTherapy")) {
                    hiv = dataSnapshot.child("HIVinTherapy").getValue().toString();
                    tvHiv.setText(hiv);
                }
                if (dataSnapshot.hasChild("KidneyFailure")) {
                    kidFail = dataSnapshot.child("KidneyFailure").getValue().toString();
                    tvKidFail.setText(kidFail);
                }
                if (dataSnapshot.hasChild("Allergies")) {
                    allergies = "";
                    String[] allergiesSeparated = dataSnapshot.child("Allergies").getValue().toString().split(",");
                    for (int i=0; i<allergiesSeparated.length; i++){
                        allergies = allergies + allergiesSeparated[i].trim() + "\n";
                    }
                    tvAllerg.setText(allergies);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        statusRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("BMI")) {
                    bmi = dataSnapshot.child("BMI").getValue().toString();
                    tvBMI.setText(bmi);
                }
                if (dataSnapshot.hasChild("STEMI")) {
                    preStemi = dataSnapshot.child("STEMI").getValue().toString();
                    tvPreSTEMI.setText(preStemi);
                }
                if (dataSnapshot.hasChild("NSTEMIACS")) {
                    preNstemi = dataSnapshot.child("NSTEMIACS").getValue().toString();
                    tvPreNSTEMI.setText(preNstemi);
                }
                if (dataSnapshot.hasChild("LVEF")) {
                    LVEF = dataSnapshot.child("LVEF").getValue().toString();
                    tvLVEF.setText(LVEF);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        anamnesiRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("Internistica")) {
                    internistica = dataSnapshot.child("Internistica").getValue().toString();
                    tvInternistica.setText(internistica);
                } else {
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)tvInternistica.getLayoutParams();
                    layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
                    tvInternistica.setLayoutParams(layoutParams);
                }
                if (dataSnapshot.hasChild("Cardiologica")) {
                    cardiologica = dataSnapshot.child("Cardiologica").getValue().toString();
                    tvCardiologica.setText(cardiologica);
                } else {
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)tvCardiologica.getLayoutParams();
                    layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
                    tvCardiologica.setLayoutParams(layoutParams);
                }
                if (dataSnapshot.hasChild("Odierna")) {
                    lastVisit = dataSnapshot.child("Odierna").getValue().toString();
                    tvLastVisit.setText(lastVisit);
                } else {
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)tvLastVisit.getLayoutParams();
                    layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
                    tvLastVisit.setLayoutParams(layoutParams);
                }
                if (dataSnapshot.hasChild("Consigli")) {
                    advices = dataSnapshot.child("Consigli").getValue().toString();
                    tvAdvices.setText(advices);
                } else {
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)tvAdvices.getLayoutParams();
                    layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
                    tvAdvices.setLayoutParams(layoutParams);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}