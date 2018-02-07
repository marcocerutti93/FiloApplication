package com.tesi.marco.filo;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddDataActivity extends AppCompatActivity {

    private Button PressureButton, WeightButton, SmokeButton, MedicineButton, PhysicalActivityButton;
    private Button HeartRateButton, GlycemiaButton, EventButton;
    private EditText ePressureMax, ePressureMin, eWeight, eWalk, eRun, eBike, eGym, eHeartRate, eGlycemia;
    private TextView tAlertOldDate;
    private RadioGroup eSmoke, eMedicine;
    private FirebaseAuth mAuth;
    private String Uid;
    private DatabaseReference myRef;
    private String myDate, todayDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_data);
        getSupportActionBar().setTitle(R.string.add_data_title_today);
        mAuth = FirebaseAuth.getInstance();
        Uid = mAuth.getCurrentUser().getUid();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("data").child(Uid);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ITALIAN);
        myDate = dateFormat.format(Calendar.getInstance().getTime());
        todayDate = dateFormat.format(Calendar.getInstance().getTime());

        PressureButton = (Button) findViewById(R.id.add_pressure_data);
        WeightButton = (Button) findViewById(R.id.add_weight_data);
        SmokeButton = (Button) findViewById(R.id.add_smoke_data);
        //MedicineButton = (Button) findViewById(R.id.add_medicine_data);
        PhysicalActivityButton = (Button) findViewById(R.id.add_physical_activity_data);
        HeartRateButton = (Button) findViewById(R.id.add_heart_rate_data);
        GlycemiaButton = (Button) findViewById(R.id.add_glycemia_data);
        EventButton = (Button) findViewById(R.id.add_event);

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

        /*MedicineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddMedicineData();
            }
        });*/

        PhysicalActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddPhysicalActivityData();
            }
        });

        HeartRateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddHeartRateData();
            }
        });

        GlycemiaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddGlycemiaData();
            }
        });

        EventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddDataActivity.this, AddEventActivity.class));
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_data, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_different_date_data:
                datePicker();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void datePicker(){
        Calendar calendar = Calendar.getInstance();
        final  SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ITALIAN);
        final  SimpleDateFormat dateTitleFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.ITALIAN);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                //System.out.print(dateFormatter.format(newDate.getTime()));
                //Toast.makeText(AddDataActivity.this,dateFormatter.format(newDate.getTime()) , Toast.LENGTH_SHORT).show();
                myDate = dateFormatter.format(newDate.getTime());
                if (Integer.parseInt(myDate.replace("-",""))>Integer.parseInt(todayDate.replace("-",""))){
                    myDate = todayDate;
                    Toast.makeText(AddDataActivity.this, R.string.error_future_date, Toast.LENGTH_SHORT).show();
                }
                String title;
                if (myDate.equals(todayDate)) {
                    title = getString(R.string.add_data_title_today);
                } else {
                    title = getString(R.string.app_name) + " - " + dateTitleFormatter.format(newDate.getTime());
                }
                getSupportActionBar().setTitle(title);
            }

        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
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
        tAlertOldDate = (TextView) pressureDialogueView.findViewById(R.id.alert_old_date);
        if (Integer.parseInt(myDate.replace("-",""))<Integer.parseInt(todayDate.replace("-",""))){
            tAlertOldDate.setVisibility(View.VISIBLE);
        }

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
                    if (checkPressureValue(pressureMax,pressureMin)){
                        DatabaseReference pRef = myRef.child("Pressure").child(myDate);
                        pRef.child("Maximum").setValue(pressureMax);
                        pRef.child("Minimum").setValue(pressureMin);
                        pressureDialogue.cancel();
                    } else {
                        Toast.makeText(AddDataActivity.this, getString(R.string.error_out_of_scale), Toast.LENGTH_SHORT).show();
                    }
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
        eWeight.setRawInputType(Configuration.KEYBOARD_12KEY);
        tAlertOldDate = (TextView) weightDialogueView.findViewById(R.id.alert_old_date);
        if (Integer.parseInt(myDate.replace("-",""))<Integer.parseInt(todayDate.replace("-",""))){
            tAlertOldDate.setVisibility(View.VISIBLE);
        }
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
                    DatabaseReference wRef = myRef.child("Weight").child(myDate);
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
        tAlertOldDate = (TextView) smokeDialogueView.findViewById(R.id.alert_old_date);
        if (Integer.parseInt(myDate.replace("-",""))<Integer.parseInt(todayDate.replace("-",""))){
            tAlertOldDate.setVisibility(View.VISIBLE);
        }
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
                    DatabaseReference sRef = myRef.child("Smoke").child(myDate);
                    sRef.child("Smoke").setValue(smoke);
                    Toast.makeText(AddDataActivity.this, "Fumato: "+smoke,Toast.LENGTH_SHORT).show();
                    smokeDialogue.cancel();
                } else {
                    Toast.makeText(AddDataActivity.this, getString(R.string.error_dialog_ok), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void AddPhysicalActivityData() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View physicalActivityDialogueView = inflater.inflate(R.layout.physical_activity_dialogue,null);
        builder.setTitle(R.string.add_physical_activity_data);
        builder.setView(physicalActivityDialogueView);
        eWalk = (EditText) physicalActivityDialogueView.findViewById(R.id.edit_physical_activity_type1);
        eWalk.setRawInputType(Configuration.KEYBOARD_12KEY);
        eRun = (EditText) physicalActivityDialogueView.findViewById(R.id.edit_physical_activity_type2);
        eRun.setRawInputType(Configuration.KEYBOARD_12KEY);
        eBike = (EditText) physicalActivityDialogueView.findViewById(R.id.edit_physical_activity_type3);
        eBike.setRawInputType(Configuration.KEYBOARD_12KEY);
        eGym = (EditText) physicalActivityDialogueView.findViewById(R.id.edit_physical_activity_type4);
        eGym.setRawInputType(Configuration.KEYBOARD_12KEY);
        tAlertOldDate = (TextView) physicalActivityDialogueView.findViewById(R.id.alert_old_date);
        if (Integer.parseInt(myDate.replace("-",""))<Integer.parseInt(todayDate.replace("-",""))){
            tAlertOldDate.setVisibility(View.VISIBLE);
        }
        builder.setCancelable(false);

        builder.setPositiveButton(R.string.ok,null);
        builder.setNegativeButton(
                R.string.cancel,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        final AlertDialog physicalActivityDialogue = builder.create();
        physicalActivityDialogue.show();
        Button ok = physicalActivityDialogue.getButton(DialogInterface.BUTTON_POSITIVE);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String walk = eWalk.getText().toString();
                String run = eRun.getText().toString();
                String bike = eBike.getText().toString();
                String gym = eGym.getText().toString();
                if (checkPhysicalActivityData(walk) || checkPhysicalActivityData(run) ||
                        checkPhysicalActivityData(bike) || checkPhysicalActivityData(gym)){
                    if (checkPhysicalActivityData(walk)){
                        DatabaseReference paRef = myRef.child("PhysicalActivity").child(myDate);
                        paRef.child("Walk").setValue(walk);
                    }
                    if (checkPhysicalActivityData(run)){
                        DatabaseReference paRef = myRef.child("PhysicalActivity").child(myDate);
                        paRef.child("Run").setValue(run);
                    }
                    if (checkPhysicalActivityData(bike)){
                        DatabaseReference paRef = myRef.child("PhysicalActivity").child(myDate);
                        paRef.child("Bike").setValue(bike);
                    }
                    if (checkPhysicalActivityData(gym)){
                        DatabaseReference paRef = myRef.child("PhysicalActivity").child(myDate);
                        paRef.child("Gym").setValue(gym);
                    }

                    physicalActivityDialogue.cancel();
                } else {
                    Toast.makeText(AddDataActivity.this, getString(R.string.error_dialog_ok), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void AddHeartRateData(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View heartRateDialogueView = inflater.inflate(R.layout.heart_rate_dialogue,null);
        builder.setTitle(R.string.insert_heart_rate);
        builder.setView(heartRateDialogueView);
        eHeartRate = (EditText) heartRateDialogueView.findViewById(R.id.edit_heart_rate);
        eHeartRate.setRawInputType(Configuration.KEYBOARD_12KEY);
        tAlertOldDate = (TextView) heartRateDialogueView.findViewById(R.id.alert_old_date);
        if (Integer.parseInt(myDate.replace("-",""))<Integer.parseInt(todayDate.replace("-",""))){
            tAlertOldDate.setVisibility(View.VISIBLE);
        }
        builder.setCancelable(false);

        builder.setPositiveButton(R.string.ok,null);
        builder.setNegativeButton(
                R.string.cancel,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        final AlertDialog heartRateDialogue = builder.create();
        heartRateDialogue.show();
        Button ok = heartRateDialogue.getButton(DialogInterface.BUTTON_POSITIVE);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String heartRate = eHeartRate.getText().toString();
                if (checkHeartRateData(heartRate)){
                    if (checkHeartRateValue(heartRate)){
                        DatabaseReference wRef = myRef.child("HeartRate").child(myDate);
                        wRef.child("HeartRate").setValue(heartRate);
                        heartRateDialogue.cancel();
                    } else {
                        Toast.makeText(AddDataActivity.this, getString(R.string.error_out_of_scale), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AddDataActivity.this, getString(R.string.error_dialog_ok), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void AddGlycemiaData(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View glycemiaDialogueView = inflater.inflate(R.layout.glycemia_dialogue,null);
        builder.setTitle(R.string.insert_glycemia);
        builder.setView(glycemiaDialogueView);
        eGlycemia = (EditText) glycemiaDialogueView.findViewById(R.id.edit_glycemia);
        eGlycemia.setRawInputType(Configuration.KEYBOARD_12KEY);
        tAlertOldDate = (TextView) glycemiaDialogueView.findViewById(R.id.alert_old_date);
        if (Integer.parseInt(myDate.replace("-",""))<Integer.parseInt(todayDate.replace("-",""))){
            tAlertOldDate.setVisibility(View.VISIBLE);
        }
        builder.setCancelable(false);

        builder.setPositiveButton(R.string.ok,null);
        builder.setNegativeButton(
                R.string.cancel,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        final AlertDialog glycemiaDialogue = builder.create();
        glycemiaDialogue.show();
        Button ok = glycemiaDialogue.getButton(DialogInterface.BUTTON_POSITIVE);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String glycemia = eGlycemia.getText().toString();
                if (checkGlycemiaData(glycemia)){
                    if (checkGlycemiaValue(glycemia)) {
                        DatabaseReference wRef = myRef.child("Glycemia").child(myDate);
                        wRef.child("Glycemia").setValue(glycemia);
                        glycemiaDialogue.cancel();
                    } else {
                        Toast.makeText(AddDataActivity.this, getString(R.string.error_out_of_scale), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AddDataActivity.this, getString(R.string.error_dialog_ok), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

/*    private void AddMedicineData() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View medicineDialogueView = inflater.inflate(R.layout.medicine_dialogue,null);
        builder.setTitle(R.string.medicine_today);
        builder.setView(medicineDialogueView);
        eMedicine = (RadioGroup) medicineDialogueView.findViewById(R.id.medicine_radiogroup);
        tAlertOldDate = (TextView) medicineDialogueView.findViewById(R.id.alert_old_date);
        if (Integer.parseInt(myDate.replace("-",""))<Integer.parseInt(todayDate.replace("-",""))){
            tAlertOldDate.setVisibility(View.VISIBLE);
        }
        builder.setCancelable(false);

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
                    DatabaseReference mRef = myRef.child("Medicine").child(myDate);
                    mRef.child("Medicine").setValue(medicine);
                    Toast.makeText(AddDataActivity.this, "Medicine: "+medicine,Toast.LENGTH_SHORT).show();
                    medicineDialogue.cancel();
                } else {
                    Toast.makeText(AddDataActivity.this, getString(R.string.error_dialog_ok), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    */

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

    private boolean checkPressureValue(String max, String min){
        boolean check = true;
        if (Double.parseDouble(min)<=20.0 || Double.parseDouble(min)>=150.0){
            ePressureMin.setError(getString(R.string.error_out_of_scale));
            ePressureMin.requestFocus();
            check = false;
        }
        if (Double.parseDouble(max)<=60.0 || Double.parseDouble(max)>=270.0){
            ePressureMax.setError(getString(R.string.error_out_of_scale));
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

    private boolean checkPhysicalActivityData(String dur){
        boolean check = true;
        if (dur.isEmpty()){
            check = false;
        }
        return check;
    }

    private boolean checkHeartRateData(String hr){
        boolean check = true;
        if (hr.isEmpty()){
            eHeartRate.setError(getString(R.string.error_field_required));
            eHeartRate.requestFocus();
            check = false;
        }
        return check;
    }

    private boolean checkHeartRateValue(String hr){
        boolean check = true;
        if (Double.parseDouble(hr)<=20.0 || Double.parseDouble(hr)>=220.0){
            eHeartRate.setError(getString(R.string.error_out_of_scale));
            eHeartRate.requestFocus();
            check = false;
        }
        return check;
    }

    private boolean checkGlycemiaData(String g){
        boolean check = true;
        if (g.isEmpty()){
            eGlycemia.setError(getString(R.string.error_field_required));
            eGlycemia.requestFocus();
            check = false;
        }
        return check;
    }

    private boolean checkGlycemiaValue(String g){
        boolean check = true;
        if (Double.parseDouble(g)<=10.0 || Double.parseDouble(g)>=1500.0){
            eGlycemia.setError(getString(R.string.error_out_of_scale));
            eGlycemia.requestFocus();
            check = false;
        }
        return check;
    }
}
