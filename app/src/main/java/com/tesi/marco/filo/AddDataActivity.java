package com.tesi.marco.filo;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

public class AddDataActivity extends AppCompatActivity {

    private Button PressureButton, WeightButton, SmokeButton, MedicineButton;
    private EditText ePressureMax, ePressureMin, eWeight;
    private RadioGroup eSmoke, eMedicine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_data);

        PressureButton = (Button) findViewById(R.id.add_pressure_data);
        WeightButton = (Button) findViewById(R.id.add_weight_data);
        SmokeButton = (Button) findViewById(R.id.add_smoke_data);
        MedicineButton = (Button) findViewById(R.id.add_medicine_data);

        PressureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddPressureData();
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

    private void AddPressureData() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View pressureDialogueView = inflater.inflate(R.layout.pressure_dialogue,null);
        builder.setTitle(R.string.insert_pressure);
        builder.setView(pressureDialogueView);
        ePressureMax = (EditText) pressureDialogueView.findViewById(R.id.edit_max_pressure);
        ePressureMin = (EditText) pressureDialogueView.findViewById(R.id.edit_min_pressure);
        builder.setCancelable(true);

        builder.setPositiveButton(
                R.string.ok,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(AddDataActivity.this, ePressureMax.getText().toString(),Toast.LENGTH_SHORT).show();
                        Toast.makeText(AddDataActivity.this, ePressureMin.getText().toString(),Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }
                });

        builder.setNegativeButton(
                R.string.cancel,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog pressureDialogue = builder.create();
        pressureDialogue.show();
    }

    private void AddWeightData(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View weightDialogueView = inflater.inflate(R.layout.weight_dialogue,null);
        builder.setTitle(R.string.insert_weight);
        builder.setView(weightDialogueView);
        eWeight = (EditText) weightDialogueView.findViewById(R.id.edit_weight);
        builder.setCancelable(true);

        builder.setPositiveButton(
                R.string.ok,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(AddDataActivity.this, eWeight.getText().toString(),Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }
                });

        builder.setNegativeButton(
                R.string.cancel,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog weightDialogue = builder.create();
        weightDialogue.show();

    }

    private void AddSmokeData() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View smokeDialogueView = inflater.inflate(R.layout.smoke_dialogue,null);
        builder.setTitle(R.string.smoke_today);
        builder.setView(smokeDialogueView);
        eSmoke = (RadioGroup) smokeDialogueView.findViewById(R.id.smoke_radiogroup);
        builder.setCancelable(true);

        builder.setPositiveButton(
                R.string.ok,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(AddDataActivity.this, String.valueOf(eSmoke.getCheckedRadioButtonId()) ,Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }
                });

        builder.setNegativeButton(
                R.string.cancel,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog smokeDialogue = builder.create();
        smokeDialogue.show();
    }

    private void AddMedicineData() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View medicineDialogueView = inflater.inflate(R.layout.medicine_dialogue,null);
        builder.setTitle(R.string.medicine_today);
        builder.setView(medicineDialogueView);
        eMedicine = (RadioGroup) medicineDialogueView.findViewById(R.id.medicine_radiogroup);
        builder.setCancelable(true);

        builder.setPositiveButton(
                R.string.ok,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(AddDataActivity.this, String.valueOf(eMedicine.getCheckedRadioButtonId()) ,Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }
                });

        builder.setNegativeButton(
                R.string.cancel,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog smokeDialogue = builder.create();
        smokeDialogue.show();
    }
}
