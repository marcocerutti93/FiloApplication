package com.tesi.marco.filo;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AddEventActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private String Uid;
    private DatabaseReference myRef ,alertRef;
    private Button ButtonTherapy, ButtonPhysicalExamination, ButtonHospitalization;
    private Button ButtonBleeding, ButtonExaminationData;
    private RadioGroup radioTherapy;
    private EditText eTherapy, ePhysicalExamination, eHospitalization, eBleeding, eExamName;
    private ImageButton bAddPhoto;
    private String todayDate;
    private SimpleDateFormat dateFormat;

    final static int GET_IMAGE = 1;
    private String encodedImage,strImageUri;
    private Bitmap imageBitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        mAuth = FirebaseAuth.getInstance();
        Uid = mAuth.getCurrentUser().getUid();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("events").child(Uid);
        alertRef = database.getReference("patientsUid").child(Uid);

        dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss", Locale.ITALIAN);

        ButtonTherapy = (Button) findViewById(R.id.add_therapy_event);
        ButtonPhysicalExamination = (Button) findViewById(R.id.add_physical_examination_event);
        ButtonHospitalization = (Button) findViewById(R.id.add_hospitalization_event);
        ButtonBleeding = (Button) findViewById(R.id.add_bleeding_event);
        ButtonExaminationData = (Button) findViewById(R.id.add_examination_data);

        ButtonTherapy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddTherapyEvent();
            }
        });

        ButtonPhysicalExamination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddPhysicalExaminationEvent();
            }
        });

        ButtonHospitalization.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddHospitalizationEvent();
            }
        });

        ButtonBleeding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddBleedingEvent();
            }
        });

        ButtonExaminationData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddExaminationData();
            }
        });
    }

    private void AddTherapyEvent() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View therapyEventDialogueView = inflater.inflate(R.layout.change_therapy_dialogue,null);
        TextView title = new TextView(this);
        title.setGravity(Gravity.CENTER);
        title.setText(R.string.event_therapy);
        title.setPadding(10, 10, 10, 10);
        title.setTextSize(20);
        title.setTextColor(Color.parseColor("#000000"));
        title.setTypeface(Typeface.DEFAULT_BOLD);
        builder.setCustomTitle(title);
        builder.setView(therapyEventDialogueView);
        radioTherapy = (RadioGroup) therapyEventDialogueView.findViewById(R.id.therapy_radiogroup);
        eTherapy = (EditText) therapyEventDialogueView.findViewById(R.id.description_change_therapy_text);
        builder.setCancelable(false);

        builder.setPositiveButton(R.string.ok,null);
        builder.setNegativeButton(
                R.string.cancel,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        final AlertDialog therapyDialogue = builder.create();
        therapyDialogue.show();
        Button ok = therapyDialogue.getButton(DialogInterface.BUTTON_POSITIVE);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String changeTherapyDescription = eTherapy.getText().toString();
                if (radioTherapy.getCheckedRadioButtonId()!=-1 && checkTherapyData(changeTherapyDescription)){
                    Map<String,Object> update = new HashMap<>();
                    update.put("Alert", true);
                    alertRef.updateChildren(update);
                    int id = radioTherapy.getCheckedRadioButtonId();
                    int radioId = radioTherapy.indexOfChild(radioTherapy.findViewById(id));
                    RadioButton b = (RadioButton) radioTherapy.getChildAt(radioId);
                    String therapyDecision = b.getText().toString();
                    todayDate = dateFormat.format(Calendar.getInstance().getTime());
                    DatabaseReference tRef = myRef.child(todayDate);
                    tRef.child("Type").setValue("Modifica Terapia");
                    tRef.child("Decision").setValue(therapyDecision);
                    tRef.child("Description").setValue(changeTherapyDescription);
                    therapyDialogue.cancel();
                } else if (radioTherapy.getCheckedRadioButtonId()==-1 && checkTherapyData(changeTherapyDescription)) {
                    Toast.makeText(AddEventActivity.this, getString(R.string.error_therapy_radio), Toast.LENGTH_SHORT).show();
                } else if (radioTherapy.getCheckedRadioButtonId()!=-1 && !checkTherapyData(changeTherapyDescription)) {
                    Toast.makeText(AddEventActivity.this, getString(R.string.error_therapy_description), Toast.LENGTH_SHORT).show();
                } else if (radioTherapy.getCheckedRadioButtonId()!=-1 && !checkTherapyData(changeTherapyDescription)) {
                    Toast.makeText(AddEventActivity.this, getString(R.string.error_dialog_ok), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void AddPhysicalExaminationEvent() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View physicalExaminationEventDialogueView = inflater.inflate(R.layout.physical_examination_dialogue,null);
        TextView title = new TextView(this);
        title.setGravity(Gravity.CENTER);
        title.setText(R.string.event_physical_examination);
        title.setPadding(10, 10, 10, 10);
        title.setTextSize(20);
        title.setTextColor(Color.parseColor("#000000"));
        title.setTypeface(Typeface.DEFAULT_BOLD);
        builder.setCustomTitle(title);
        builder.setView(physicalExaminationEventDialogueView);
        ePhysicalExamination = (EditText) physicalExaminationEventDialogueView
                .findViewById(R.id.description_physical_examination);
        builder.setCancelable(false);

        builder.setPositiveButton(R.string.ok,null);
        builder.setNegativeButton(
                R.string.cancel,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        final AlertDialog physicalExaminationDialogue = builder.create();
        physicalExaminationDialogue.show();
        Button ok = physicalExaminationDialogue.getButton(DialogInterface.BUTTON_POSITIVE);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String physicalExaminationDescription = ePhysicalExamination.getText().toString();
                if (checkPhysicalExaminationData(physicalExaminationDescription)){
                    Map<String,Object> update = new HashMap<>();
                    update.put("Alert", true);
                    alertRef.updateChildren(update);
                    todayDate = dateFormat.format(Calendar.getInstance().getTime());
                    DatabaseReference peRef = myRef.child(todayDate);
                    peRef.child("Type").setValue("Visita Medica");
                    peRef.child("Description").setValue(physicalExaminationDescription);
                    physicalExaminationDialogue.cancel();
                } else {
                    Toast.makeText(AddEventActivity.this, getString(R.string.error_dialog_ok), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void AddHospitalizationEvent() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View hospitalizationEventDialogueView = inflater.inflate(R.layout.hospitalization_dialogue,null);
        TextView title = new TextView(this);
        title.setGravity(Gravity.CENTER);
        title.setText(R.string.event_hospitalization);
        title.setPadding(10, 10, 10, 10);
        title.setTextSize(20);
        title.setTextColor(Color.parseColor("#000000"));
        title.setTypeface(Typeface.DEFAULT_BOLD);
        builder.setCustomTitle(title);
        builder.setView(hospitalizationEventDialogueView);
        eHospitalization = (EditText) hospitalizationEventDialogueView
                .findViewById(R.id.description_hospitalization);
        builder.setCancelable(false);

        builder.setPositiveButton(R.string.ok,null);
        builder.setNegativeButton(
                R.string.cancel,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        final AlertDialog hospitalizationDialogue = builder.create();
        hospitalizationDialogue.show();
        Button ok = hospitalizationDialogue.getButton(DialogInterface.BUTTON_POSITIVE);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String hospitalizationDescription = eHospitalization.getText().toString();
                if (checkHospitalizationData(hospitalizationDescription)){
                    Map<String,Object> update = new HashMap<>();
                    update.put("Alert", true);
                    alertRef.updateChildren(update);
                    todayDate = dateFormat.format(Calendar.getInstance().getTime());
                    DatabaseReference hRef = myRef.child(todayDate);
                    hRef.child("Type").setValue("Ricovero Ospedaliero");
                    hRef.child("Description").setValue(hospitalizationDescription);
                    hospitalizationDialogue.cancel();
                } else {
                    Toast.makeText(AddEventActivity.this, getString(R.string.error_dialog_ok), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void AddBleedingEvent(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View bleedingEventDialogueView = inflater.inflate(R.layout.bleeding_dialogue,null);
        TextView title = new TextView(this);
        title.setGravity(Gravity.CENTER);
        title.setText(R.string.event_bleeding);
        title.setPadding(10, 10, 10, 10);
        title.setTextSize(20);
        title.setTextColor(Color.parseColor("#000000"));
        title.setTypeface(Typeface.DEFAULT_BOLD);
        builder.setCustomTitle(title);
        builder.setView(bleedingEventDialogueView);
        eBleeding = (EditText) bleedingEventDialogueView
                .findViewById(R.id.edit_duration_bleeding);
        builder.setCancelable(false);

        builder.setPositiveButton(R.string.ok,null);
        builder.setNegativeButton(
                R.string.cancel,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        final AlertDialog bleedingDialogue = builder.create();
        bleedingDialogue.show();
        Button ok = bleedingDialogue.getButton(DialogInterface.BUTTON_POSITIVE);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String bleedingDuration = eBleeding.getText().toString();
                if (checkBleedingData(bleedingDuration)){
                    Map<String,Object> update = new HashMap<>();
                    update.put("Alert", true);
                    alertRef.updateChildren(update);
                    todayDate = dateFormat.format(Calendar.getInstance().getTime());
                    DatabaseReference bRef = myRef.child(todayDate);
                    bRef.child("Type").setValue("Sanguinamento");
                    bRef.child("Duration").setValue(bleedingDuration);
                    bleedingDialogue.cancel();
                } else {
                    Toast.makeText(AddEventActivity.this, getString(R.string.error_dialog_ok), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void AddExaminationData(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View examinationDataDialogueView = inflater.inflate(R.layout.add_examination_data_dialogue,null);
        TextView title = new TextView(this);
        title.setGravity(Gravity.CENTER);
        title.setText(R.string.event_add_examination_data);
        title.setPadding(10, 10, 10, 10);
        title.setTextSize(20);
        title.setTextColor(Color.parseColor("#000000"));
        title.setTypeface(Typeface.DEFAULT_BOLD);
        builder.setCustomTitle(title);
        builder.setView(examinationDataDialogueView);
        eExamName = (EditText) examinationDataDialogueView.findViewById(R.id.add_examination_name);
        bAddPhoto = (ImageButton) examinationDataDialogueView.findViewById(R.id.add_examination_photo);
        builder.setCancelable(false);

        bAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chooseImageIntent = ImagePicker.getPickImageIntent(AddEventActivity.this);
                startActivityForResult(chooseImageIntent, 1);
            }
        });

        builder.setPositiveButton(R.string.ok,null);
        builder.setNegativeButton(
                R.string.cancel,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        final AlertDialog examinationDataDialogue = builder.create();
        examinationDataDialogue.show();
        Button ok = examinationDataDialogue.getButton(DialogInterface.BUTTON_POSITIVE);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String photoName = eExamName.getText().toString();
                if (checkExaminationImage(encodedImage) && checkExaminationName(photoName)){
                    Map<String,Object> update = new HashMap<>();
                    update.put("Alert", true);
                    alertRef.updateChildren(update);
                    todayDate = dateFormat.format(Calendar.getInstance().getTime());
                    DatabaseReference erRef = myRef.child(todayDate);
                    erRef.child("Type").setValue("Risultati Esame");
                    erRef.child("Name").setValue(photoName);
                    erRef.child("Photo").setValue(encodedImage);
                    examinationDataDialogue.cancel();
                } else if (checkExaminationImage(encodedImage) && !checkExaminationName(photoName)){
                    Toast.makeText(AddEventActivity.this, getString(R.string.error_image_name_dialogue), Toast.LENGTH_SHORT).show();
                } else if (!checkExaminationImage(encodedImage) && checkExaminationName(photoName)){
                    Toast.makeText(AddEventActivity.this, getString(R.string.error_image_dialogue), Toast.LENGTH_SHORT).show();
                } else if (!checkExaminationImage(encodedImage) && !checkExaminationName(photoName)) {
                    Toast.makeText(AddEventActivity.this, getString(R.string.error_dialog_ok), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GET_IMAGE){
            if (resultCode == RESULT_OK){
                Uri imageUri = ImagePicker.getImageFromResult(this, resultCode, data);
                strImageUri = imageUri.toString();

                CropImage.activity(imageUri)
                        .setMinCropResultSize(100,100)
                        .start(this);
            }
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                try {
                    imageBitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(resultUri));
                    BitmapDrawable bDrawable = new BitmapDrawable(getApplicationContext().getResources(), imageBitmap);
                    bAddPhoto.setBackgroundDrawable(bDrawable);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    imageBitmap.compress(Bitmap.CompressFormat.JPEG, 25, baos);
                    byte[] byteArrayImage = baos.toByteArray();
                    encodedImage = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);
                    Log.d("IMAGE", encodedImage);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private boolean checkTherapyData(String d){
        boolean check = true;
        if (d.isEmpty()){
            eTherapy.setError(getString(R.string.error_field_required));
            eTherapy.requestFocus();
            check = false;
        }
        return check;
    }

    private boolean checkPhysicalExaminationData(String d){
        boolean check = true;
        if (d.isEmpty()){
            ePhysicalExamination.setError(getString(R.string.error_field_required));
            ePhysicalExamination.requestFocus();
            check = false;
        }
        return check;
    }

    private boolean checkHospitalizationData(String h) {
        boolean check = true;
        if (h.isEmpty()){
            eHospitalization.setError(getString(R.string.error_field_required));
            eHospitalization.requestFocus();
            check = false;
        }
        return check;
    }

    private boolean checkBleedingData(String b) {
        boolean check = true;
        if (b.isEmpty()){
            eBleeding.setError(getString(R.string.error_field_required));
            eBleeding.requestFocus();
            check = false;
        }
        return check;
    }

    private boolean checkExaminationImage(String i) {
        boolean check = true;
        if (i.isEmpty()){
            check = false;
        }
        return check;
    }

    private boolean checkExaminationName(String n){
        boolean check = true;
        if (n.isEmpty()){
            eExamName.setError(getString(R.string.error_field_required));
            eExamName.requestFocus();
            check = false;
        }
        return check;
    }
}
