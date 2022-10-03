package com.abhishek.bookshelf.activities;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.abhishek.bookshelf.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.jpardogo.android.googleprogressbar.library.GoogleProgressBar;

public class ForgotPassword extends AppCompatActivity {

    //Variables
    private ImageView back;
    private TextInputLayout email;
    private AppCompatButton next;
    private GoogleProgressBar progressBar;
    private FirebaseAuth mAuth;
    private RelativeLayout rlForgotPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_forgot_password);

        //Initialising variables
        initialise();

        //Hide Navigation Bar
        hideNavigation();

        //Taking back to LOGIN screen
        back.setOnClickListener(v -> onBackPressed());

        //Reset Password
        next.setOnClickListener(v -> resetPassword());

    }

    private void initialise() {
        back = findViewById(R.id.backForgetPass);
        email = findViewById(R.id.emailForgotPassword);
        next = findViewById(R.id.nextBtn);
        progressBar = findViewById(R.id.progress_bar_forgot_pass);
        mAuth = FirebaseAuth.getInstance();
        rlForgotPass = findViewById(R.id.rlForgotPass);
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

    private void resetPassword() {

        if (!validateEmail()) {
            return;
        }

        String emailStr = email.getEditText().getText().toString().trim();

        rlForgotPass.setAlpha(0.6f);
        progressBar.setVisibility(View.VISIBLE);
        next.setEnabled(false);

        mAuth.sendPasswordResetEmail(emailStr)
                .addOnCompleteListener(ForgotPassword.this, task -> {

                    if (task.isSuccessful()) {
                        rlForgotPass.setAlpha(1f);
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(ForgotPassword.this, "Check your email to reset your password", Toast.LENGTH_LONG).show();
                        email.getEditText().setText("");
                        next.setEnabled(true);

                    } else {
                        rlForgotPass.setAlpha(1f);
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(ForgotPassword.this, "Something went wrong. Error : " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        next.setEnabled(true);

                    }
                });

    }
}