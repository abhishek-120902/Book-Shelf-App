package com.abhishek.bookshelf.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.abhishek.bookshelf.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.jpardogo.android.googleprogressbar.library.GoogleProgressBar;

public class RegistrationActivity extends AppCompatActivity {

    //Variables
    private TextInputLayout regName, regEmail, regPhoneNo, regPassword;
    private AppCompatButton register, goToLogin;
    private FirebaseAuth mAuth;
    private GoogleProgressBar progressBar;
    private SharedPreferences preferences;
    private LinearLayout llReg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_registration);

        //Initialising variables
        initialise();

        boolean isFirstTime = preferences.getBoolean("isFirstTime", true);
        if (isFirstTime) {

            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("isFirstTime", false);
            editor.apply();
            startActivity(new Intent(RegistrationActivity.this, BookShelfActivity.class));
            finish();

        }

        if (mAuth.getCurrentUser() != null) {

            startActivity(new Intent(RegistrationActivity.this, BookShelfActivity.class));
            finish();

        }

        //Hide Navigation Bar
        hideNavigation();

        //Moving to SIGN UP screen
        goToLogin.setOnClickListener(v -> onBackPressed());

        //Register User
        register.setOnClickListener(v -> registerUser(v));

    }

    private void initialise() {
        regName = findViewById(R.id.name);
        regEmail = findViewById(R.id.emailRegistration);
        regPhoneNo = findViewById(R.id.phoneRegistration);
        regPassword = findViewById(R.id.passwordSignUp);
        goToLogin = findViewById(R.id.logInBtnSignUp);
        register = findViewById(R.id.registerBtn);
        mAuth = FirebaseAuth.getInstance();
        preferences = getSharedPreferences("LoginSharedPreference", MODE_PRIVATE);
        progressBar = findViewById(R.id.progressBarRegistration);
        llReg = findViewById(R.id.llReg);
    }

    private void hideNavigation() {
        this.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        );
    }

    private Boolean validateName() {
        String val = regName.getEditText().getText().toString();
        if (val.isEmpty()) {
            regName.setError("Field cannot be empty");
            return false;
        } else {
            regName.setError(null);
            regName.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateEmail() {
        String val = regEmail.getEditText().getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (val.isEmpty()) {
            regEmail.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(emailPattern)) {
            regEmail.setError("Invalid email address");
            return false;
        } else {
            regEmail.setError(null);
            regEmail.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validatePhoneNo() {
        String val = regPhoneNo.getEditText().getText().toString();
        if (val.isEmpty()) {
            regPhoneNo.setError("Field cannot be empty");
            return false;
        } else {
            regPhoneNo.setError(null);
            regPhoneNo.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validatePassword() {
        String val = regPassword.getEditText().getText().toString();
        String passwordVal = "^" +
                "(?=.*[a-zA-Z])" +      //any letter
                "(?=.*[@#$%^&+=])" +    //at least 1 special character
                "(?=\\S+$)" +           //no white spaces
                ".{4,}" +               //at least 4 characters
                "$";

        if (val.isEmpty()) {
            regPassword.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(passwordVal)) {
            regPassword.setError("Password is too weak");
            return false;
        } else {
            regPassword.setError(null);
            regPassword.setErrorEnabled(false);
            return true;
        }
    }

    private void registerUser(View view) {

        if (!validateName() | !validatePassword() | !validatePhoneNo() | !validateEmail()) {
            return;
        }

        //Get all the values in string
        String email = regEmail.getEditText().getText().toString();
        String password = regPassword.getEditText().getText().toString();

        llReg.setAlpha(0.6f);
        progressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(RegistrationActivity.this, task -> {

                    if (task.isSuccessful()) {
                        llReg.setAlpha(1f);
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(RegistrationActivity.this, "You have Registered Successfully", Toast.LENGTH_SHORT).show();

                        //Go to next Screen
                        startActivity(new Intent(RegistrationActivity.this, BookShelfActivity.class));
                        finish();

                    } else {
                        llReg.setAlpha(1f);
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(RegistrationActivity.this, "Registration Failed!" + task.getException(), Toast.LENGTH_SHORT).show();
                    }

                });

    }
}