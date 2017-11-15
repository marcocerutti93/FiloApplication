package com.tesi.marco.filo;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ForgotPasswordActivity extends AppCompatActivity {

    private AutoCompleteTextView mEmailField;
    private Button sendEmail;
    private String email;
    private View mProgressView;
    private View focusView = null;

    private FirebaseAuth mAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mAuth = FirebaseAuth.getInstance();

        // Views
        mEmailField = (AutoCompleteTextView) findViewById(R.id.field_email);
        sendEmail = (Button) findViewById(R.id.recover_password_button);
        mProgressView = findViewById(R.id.login_progress_bar);


        sendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = mEmailField.getText().toString();
                if (checkEmail()) {
                    focusView.requestFocus();
                } else {
                    mProgressView.setVisibility(View.VISIBLE);
                    InputMethodManager imm = (InputMethodManager) getSystemService(ForgotPasswordActivity.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput (InputMethodManager.SHOW_FORCED, 0);
                    mAuth.sendPasswordResetEmail(email)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(ForgotPasswordActivity.this, R.string.email_sent, Toast.LENGTH_SHORT).show();
                                        mProgressView.setVisibility(View.GONE);
                                        finish();
                                    }
                                }
                            });
                }
            }
        });
    }

    public boolean checkEmail() {

        boolean check = false;
        if (email.isEmpty()){
            mEmailField.setError(getString(R.string.error_field_required));
            focusView = mEmailField;
            check = true;
        } else if (!isEmailValid(email)){
            mEmailField.setError(getString(R.string.error_invalid_email));
            focusView = mEmailField;
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
}
