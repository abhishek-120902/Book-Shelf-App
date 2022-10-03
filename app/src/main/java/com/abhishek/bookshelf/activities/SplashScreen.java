package com.abhishek.bookshelf.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.abhishek.bookshelf.R;
import com.airbnb.lottie.LottieAnimationView;

public class SplashScreen extends AppCompatActivity {

    private LottieAnimationView animationView;
    private TextView appName;
    private Animation topAnim, bottomAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Initialise Variables
        initialise();

        //Set Animations
        animationView.setAnimation(topAnim);
        appName.setAnimation(bottomAnim);

        //Intent
        goToLoginActivity();

        //Hide Navigation Bar
        hideNavigation();

    }

    private void initialise() {
        animationView = findViewById(R.id.splashAnim);
        appName = findViewById(R.id.appNameSplash);
        topAnim = AnimationUtils.loadAnimation(this, R.anim.logo_anim_splash);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.name_anim_splash);
    }

    private void goToLoginActivity() {
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }, 2000);
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