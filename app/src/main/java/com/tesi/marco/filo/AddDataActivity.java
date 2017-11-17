package com.tesi.marco.filo;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Date;

public class AddDataActivity extends AppCompatActivity {

    private Button PressureButton, WeightButton, SmokeButton, MedicineButton;
    private EditText ePressureMax, ePressureMin, eWeight;
    private RadioGroup eSmoke, eMedicine;
    private FirebaseAuth mAuth;
    private String Uid;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_data);
        mAuth = FirebaseAuth.getInstance();
        Uid = mAuth.getCurrentUser().getUid();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("data").child(Uid);

        PressureButton = (Button) findViewById(R.id.add_pressure_data);
        WeightButton = (Button) findViewById(R.id.add_weight_data);
        SmokeButton = (Button) findViewById(R.id.add_smoke_data);
        MedicineButton = (Button) findViewById(R.id.add_medicine_data);

        PressureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddPressureData(myRef);
            }
        });

        WeightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddWeightData();
            }
        });

        SmokeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddSmokeData();
            }
        });

        MedicineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddMedicineData();
            }
        });

    }

    private void AddPressureData(final DatabaseReference myRef) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View pressureDialogueView = inflater.inflate(R.layout.pressure_dialogue,null);
        builder.setTitle(R.string.insert_pressure);
        builder.setView(pressureDialogueView);
        ePressureMax = (EditText) pressureDialogueView.findViewById(R.id.edit_max_pressure);
        ePressureMax.setRawInputType(Configuration.KEYBOARD_12KEY);
        ePressureMin = (EditText) pressureDialogueView.findViewById(R.id.edit_min_pressure);
        ePressureMin.setRawInputType(Configuration.KEYBOARD_12KEY);

        builder.setCancelable(false);

        builder.setPositiveButton(R.string.ok,null);
        builder.setNegativeButton(
                R.string.cancel,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        final AlertDialog pressureDialogue = builder.create();
        pressureDialogue.show();
        Button ok = pressureDialogue.getButton(DialogInterface.BUTTON_POSITIVE);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pressureMax = ePressureMax.getText().toString();
                String pressureMin = ePressureMin.getText().toString();
                if (checkPressureData(pressureMax,pressureMin)){
                    Date currentTime = Calendar.getInstance().getTime();
                    DatabaseReference pRef = myRef.child("Pressure").child(currentTime.toString());
                    pRef.child("Maximum").setValue(pressureMax);
                    pRef.child("Minimum").setValue(pressureMin);
                    Toast.makeText(AddDataActivity.this, "max: "+pressureMax+"\n"+"min: "+pressureMin,Toast.LENGTH_SHORT).show();
                    pressureDialogue.cancel();
                } else {
                    Toast.makeText(AddDataActivity.this, getString(R.string.error_dialog_ok), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void AddWeightData(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View weightDialogueView = inflater.inflate(R.layout.weight_dialogue,null);
        builder.setTitle(R.string.insert_weight);
        builder.setView(weightDialogueView);
        eWeight = (EditText) weightDialogueView.findViewById(R.id.edit_weight);
        builder.setCancelable(false);

        builder.setPositiveButton(R.string.ok,null);
        builder.setNegativeButton(
                R.string.cancel,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        final AlertDialog weightDialogue = builder.create();
        weightDialogue.show();
        Button ok = weightDialogue.getButton(DialogInterface.BUTTON_POSITIVE);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String weight = eWeight.getText().toString();
                if (checkWeightData(weight)){
                    Date currentTime = Calendar.getInstance().getTime();
                    DatabaseReference wRef = myRef.child("Weight").child(currentTime.toString());
                    wRef.child("Weight").setValue(weight);
                    Toast.makeText(AddDataActivity.this, "Peso: "+weight,Toast.LENGTH_SHORT).show();
                    weightDialogue.cancel();
                } else {
                    Toast.makeText(AddDataActivity.this, getString(R.string.error_dialog_ok), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void AddSmokeData() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View smokeDialogueView = inflater.inflate(R.layout.smoke_dialogue,null);
        builder.setTitle(R.string.smoke_today);
        builder.setView(smokeDialogueView);
        eSmoke = (RadioGroup) smokeDialogueView.findViewById(R.id.smoke_radiogroup);
        builder.setCancelable(false);

        builder.setPositiveButton(R.string.ok,null);
        builder.setNegativeButton(
                R.string.cancel,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        final AlertDialog smokeDialogue = builder.create();
        smokeDialogue.show();
        Button ok = smokeDialogue.getButton(DialogInterface.BUTTON_POSITIVE);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (eSmoke.getCheckedRadioButtonId()!=-1){
                    int id = eSmoke.getCheckedRadioButtonId();
                    int radioId = eSmoke.indexOfChild(eSmoke.findViewById(id));
                    RadioButton b = (RadioButton) eSmoke.getChildAt(radioId);
                    String smoke = b.getText().toString();
                    Date currentTime = Calendar.getInstance().getTime();
                    DatabaseReference sRef = myRef.child("Smoke").child(currentTime.toString());
                    sRef.child("Smoke").setValue(smoke);
                    Toast.makeText(AddDataActivity.this, "Fumato: "+smoke,Toast.LENGTH_SHORT).show();
                    smokeDialogue.cancel();
                } else {
                    Toast.makeText(AddDataActivity.this, getString(R.string.error_dialog_ok), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void AddMedicineData() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View medicineDialogueView = inflater.inflate(R.layout.medicine_dialogue,null);
        builder.setTitle(R.string.medicine_today);
        builder.setView(medicineDialogueView);
        eMedicine = (RadioGroup) medicineDialogueView.findViewById(R.id.medicine_radiogroup);
        builder.setCancelable(true);

        builder.setPositiveButton(R.string.ok,null);
        builder.setNegativeButton(
                R.string.cancel,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        final AlertDialog medicineDialogue = builder.create();
        medicineDialogue.show();
        Button ok = medicineDialogue.getButton(DialogInterface.BUTTON_POSITIVE);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (eMedicine.getCheckedRadioButtonId()!=-1){
                    int id = eMedicine.getCheckedRadioButtonId();
                    int radioId = eMedicine.indexOfChild(eMedicine.findViewById(id));
                    RadioButton b = (RadioButton) eMedicine.getChildAt(radioId);
                    String medicine = b.getText().toString();
                    Date currentTime = Calendar.getInstance().getTime();
                    DatabaseReference mRef = myRef.child("Medicine").child(currentTime.toString());
                    mRef.child("Medicine").setValue(medicine);
                    Toast.makeText(AddDataActivity.this, "Medicine: "+medicine,Toast.LENGTH_SHORT).show();
                    medicineDialogue.cancel();
                } else {
                    Toast.makeText(AddDataActivity.this, getString(R.string.error_dialog_ok), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean checkPressureData(String max, String min){
        boolean check = true;
        if (min.isEmpty()){
            ePressureMin.setError(getString(R.string.error_field_required));
            ePressureMin.requestFocus();
            check = false;
        }
        if (max.isEmpty()){
            ePressureMax.setError(getString(R.string.error_field_required));
            ePressureMax.requestFocus();
            check = false;
        }
        return check;
    }

    private boolean checkWeightData(String w){
        boolean check = true;
        if (w.isEmpty()){
            eWeight.setError(getString(R.string.error_field_required));
            eWeight.requestFocus();
            check = false;
        }
        return check;
    }
}
