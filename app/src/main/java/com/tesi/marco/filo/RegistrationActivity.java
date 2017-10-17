package com.tesi.marco.filo;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringDef;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistrationActivity extends AppCompatActivity {

    private static final String TAG = "Login";

    private EditText mEmailField, mPasswordField, mNameField, mSurnameField, mFiscalCode, mDateOfBirth;
    private Button registrationButton;
    private String name,surname,email,password, fiscalCode, dateOfBirth, Uid;

    private View mProgressView;
    private View focusView = null;

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        mAuth = FirebaseAuth.getInstance();

        // Views
        mNameField = (EditText) findViewById(R.id.field_name);
        mSurnameField = (EditText) findViewById(R.id.field_surname);
        mFiscalCode = (EditText) findViewById(R.id.field_codice_fiscale);
        mEmailField = (EditText) findViewById(R.id.field_registration_email);
        mPasswordField = (EditText) findViewById(R.id.field_registration_password);
        mDateOfBirth = (EditText) findViewById(R.id.field_registration_dateofbirth);
        mDateOfBirth.setText("GG/MM/AAAA");
        mProgressView = findViewById(R.id.registration_progress_bar);

        TextWatcher tw = new TextWatcher() {
            private String current = "";
            private String ddmmyyyy = "GGMMAAAA";
            private Calendar cal = Calendar.getInstance();

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals(current)) {
                    String clean = s.toString().replaceAll("[^\\d.]", "");
                    String cleanC = current.replaceAll("[^\\d.]", "");

                    int cl = clean.length();
                    int sel = cl;
                    for (int i = 2; i <= cl && i < 6; i += 2) {
                        sel++;
                    }
                    //Fix for pressing delete next to a forward slash
                    if (clean.equals(cleanC)) sel--;

                    if (clean.length() < 8) {
                        clean = clean + ddmmyyyy.substring(clean.length());
                    } else {
                        //This part makes sure that when we finish entering numbers
                        //the date is correct, fixing it otherwise
                        int day = Integer.parseInt(clean.substring(0, 2));
                        int mon = Integer.parseInt(clean.substring(2, 4));
                        int year = Integer.parseInt(clean.substring(4, 8));

                        if (mon > 12) mon = 12;
                        cal.set(Calendar.MONTH, mon - 1);
                        year = (year < 1900) ? 1900 : (year > 2100) ? 2100 : year;
                        cal.set(Calendar.YEAR, year);
                        // ^ first set year for the line below to work correctly
                        //with leap years - otherwise, date e.g. 29/02/2012
                        //would be automatically corrected to 28/02/2012

                        day = (day > cal.getActualMaximum(Calendar.DATE)) ? cal.getActualMaximum(Calendar.DATE) : day;
                        clean = String.format("%02d%02d%02d", day, mon, year);
                    }

                    clean = String.format("%s/%s/%s", clean.substring(0, 2),
                            clean.substring(2, 4),
                            clean.substring(4, 8));

                    sel = sel < 0 ? 0 : sel;
                    current = clean;
                    mDateOfBirth.setText(current);
                    mDateOfBirth.setSelection(sel < current.length() ? sel : current.length());
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };
        mDateOfBirth.addTextChangedListener(tw);


        // Button
        registrationButton = (Button) findViewById(R.id.registration_button);
        registrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = mNameField.getText().toString();
                surname = mSurnameField.getText().toString();
                fiscalCode = mFiscalCode.getText().toString();
                email = mEmailField.getText().toString();
                password = mPasswordField.getText().toString();
                dateOfBirth = mDateOfBirth.getText().toString();
                if (checkDataForRegistration()){
                    focusView.requestFocus();
                } else {
                    mProgressView.setVisibility(View.VISIBLE);
                    InputMethodManager imm = (InputMethodManager)getSystemService(RegistrationActivity.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    createAccount();
                }
            }
        });
    }

    private void createAccount() {
        Log.d(TAG, "createAccount:" + email);
        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");

                            Uid = mAuth.getCurrentUser().getUid();
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference myRef = database.getReference("patients").child(Uid);

                            myRef.child("Name").setValue(name);
                            myRef.child("Surname").setValue(surname);
                            myRef.child("Email").setValue(email);
                            myRef.child("Password").setValue(password);
                            myRef.child("FiscalCode").setValue(fiscalCode);
                            myRef.child("DateOfBirth").setValue(dateOfBirth);

                            DatabaseReference idRef = database.getReference("patientsUid").child(fiscalCode);
                            idRef.child("UserId").setValue(Uid);

                            Intent intent = new Intent(RegistrationActivity.this,MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegistrationActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
        // [END create_user_with_email]
    }

    public boolean checkDataForRegistration() {

        boolean check = false;
        if (name.isEmpty() || surname.isEmpty() || email.isEmpty() || password.isEmpty()){
            if (password.isEmpty()) {
                mPasswordField.setError(getString(R.string.error_field_required));
                focusView = mPasswordField;
            }
            if (email.isEmpty()) {
                mEmailField.setError(getString(R.string.error_field_required));
                focusView = mEmailField;
            }
            if (surname.isEmpty()) {
                mSurnameField.setError(getString(R.string.error_field_required));
                focusView = mSurnameField;
            }
            if (name.isEmpty()) {
                mNameField.setError(getString(R.string.error_field_required));
                focusView = mNameField;
            }
            check = true;
        } else if (!isEmailValid(email)){
            mEmailField.setError(getString(R.string.error_invalid_email));
            focusView = mEmailField;
            check = true;
        } else if (!isPasswordValid(password)) {
            mPasswordField.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordField;
            Toast.makeText(RegistrationActivity.this, getString(R.string.type_of_password), Toast.LENGTH_SHORT).show();
            check = true;
        }
        return check;
    }

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    private boolean isEmailValid(String email) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(email);
        return matcher.find();
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 6;
    }
}
