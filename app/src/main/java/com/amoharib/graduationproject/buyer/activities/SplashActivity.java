package com.amoharib.graduationproject.buyer.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.amoharib.graduationproject.R;
import com.amoharib.graduationproject.seller.activities.SellerDashboardActivity;
import com.amoharib.graduationproject.seller.activities.SellerLoginActivity;
import com.amoharib.graduationproject.seller.activities.SellerRegisterActivity;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {


    private ProgressBar progressLoader;
    private LinearLayout splashText;
    private LinearLayout selectionLayout;
    private Button buyerBtn;
    private Button sellerBtn;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Fresco.initialize(getApplicationContext());
        initView();
        btnListeners();

        selectionLayout.setVisibility(View.INVISIBLE);
        firebaseAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    String email = user.getEmail();
                    Intent intent;
                    if (email.contains("firebase")) {
                        intent = new Intent(SplashActivity.this, SellerDashboardActivity.class);
                    } else {
                        intent = new Intent(SplashActivity.this, CategoryActivity.class);
                    }
                    startActivity(intent);
                    finish();

                } else {
                    progressLoader.setVisibility(View.INVISIBLE);
                    initInputAnimations();
                }
            }
        };
    }

    private void btnListeners() {
        buyerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SplashActivity.this, UserLoginActivity.class);
                startActivity(intent);
            }
        });

        sellerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SplashActivity.this, SellerLoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initView() {
        progressLoader = (ProgressBar) findViewById(R.id.progress_loader);
        splashText = (LinearLayout) findViewById(R.id.splashText);
        selectionLayout = (LinearLayout) findViewById(R.id.selectionLayout);
        buyerBtn = (Button) findViewById(R.id.buyerBtn);
        sellerBtn = (Button) findViewById(R.id.sellerBtn);
        selectionLayout.setVisibility(View.INVISIBLE);
    }

    private void initTextAnimation() {
        Animation exitAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_out);
        Animation fadeOutAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_out);

        AnimationSet animationSet = new AnimationSet(false);
        animationSet.addAnimation(exitAnimation);
        animationSet.addAnimation(fadeOutAnimation);
        animationSet.setDuration(1000);

        splashText.startAnimation(animationSet);
        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                splashText.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void initInputAnimations() {
        Animation enterAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_in);
        Animation fadeAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in);


        AnimationSet animationSet = new AnimationSet(false);
        animationSet.addAnimation(enterAnimation);
        animationSet.addAnimation(fadeAnimation);
        animationSet.setDuration(1000);

        selectionLayout.startAnimation(animationSet);
        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                initTextAnimation();
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        selectionLayout.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(mAuthListener);
    }
}
