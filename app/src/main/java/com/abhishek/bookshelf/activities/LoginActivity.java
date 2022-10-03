package com.abhishek.bookshelf.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.abhishek.bookshelf.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.jpardogo.android.googleprogressbar.library.GoogleProgressBar;

public class LoginActivity extends AppCompatActivity {

    //Variables
    private TextView loginSlogan, loginToCont;
    private TextInputLayout email, password;
    private AppCompatButton forgotPassword, logIn, signUp;
    private CheckBox remember;
    private GoogleProgressBar progressBar;
    private FirebaseAuth mAuth;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private LinearLayout loginActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        //Initialising variables
        initialise();

        //Hide Navigation Bar
        hideNavigation();

        //Remember Me
        rememberMe();

        //Login the User
        logIn.setOnClickListener(v -> loginUser());

        //Moving to SIGN UP screen
        signUp.setOnClickListener(v -> goToSignUp());

        //Moving to FORGOT PASSWORD screen
        forgotPassword.setOnClickListener(v -> goToForgotPass());

    }

    private void initialise() {
        loginSlogan = findViewById(R.id.loginTitle);
        loginToCont = findViewById(R.id.loginToContinue);
        email = findViewById(R.id.emailLogin);
        password = findViewById(R.id.passwordLogIn);
        remember = findViewById(R.id.rememberMe);
        forgotPassword = findViewById(R.id.forgotPassword);
        logIn = findViewById(R.id.loginBtn);
        signUp = findViewById(R.id.loginSignUp);
        progressBar = findViewById(R.id.progressBarLogin);
        mAuth = FirebaseAuth.getInstance();
        loginActivity = findViewById(R.id.loginActivity);
    }

    private void goToForgotPass() {
        Intent intent = new Intent(LoginActivity.this, ForgotPassword.class);
        startActivity(intent);
    }

    private void goToSignUp() {
        Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
        startActivity(intent);
    }

    private void loginUser() {
        if (!validatePassword() | !validateEmail()) {
            return;
        }

        loginActivity.setAlpha(0.6f);
        progressBar.setVisibility(View.VISIBLE);

        String emailStr = email.getEditText().getText().toString();
        String passwordStr = password.getEditText().getText().toString();
        mAuth.signInWithEmailAndPassword(emailStr, passwordStr)
                .addOnCompleteListener(this, task -> {

                    if (task.isSuccessful()) {
                        //Login user
                        loginActivity.setAlpha(1f);
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(LoginActivity.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, BookShelfActivity.class));
                        finish();

                    } else {
                        loginActivity.setAlpha(1f);
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(LoginActivity.this, "Login Failed! Error : " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                });
    }

    private void rememberMe() {
        preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
        Boolean isRememberMeChecked = preferences.getBoolean("remember", false);
        if (isRememberMeChecked) {
            startActivity(new Intent(LoginActivity.this, BookShelfActivity.class));
            finish();
        }

        remember.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (buttonView.isChecked()) {

                preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
                editor = preferences.edit();
                editor.putBoolean("remember", true);
                editor.apply();

            } else if (!buttonView.isChecked()) {

                SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
                editor = preferences.edit();
                editor.putBoolean("remember", false);
                editor.apply();

            }

        });
    }

    private Boolean validateEmail() {
        String val = email.getEditText().getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (val.isEmpty()) {
            email.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(emailPattern)) {
            email.setError("Invalid email address");
            return false;
        } else {
            email.setError(null);
            email.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validatePassword() {
        String val = password.getEditText().getText().toString();
        String passwordVal = "^" +
                "(?=.*[a-zA-Z])" +      //any letter
                "(?=.*[@#$%^&+=])" +    //at least 1 special character
                "(?=\\S+$)" +           //no white spaces
                ".{4,}" +               //at least 4 characters
                "$";

        if (val.isEmpty()) {
            password.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(passwordVal)) {
            password.setError("Password is too weak");
            return false;
        } else {
            password.setError(null);
            password.setErrorEnabled(false);
            return true;
        }
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
}