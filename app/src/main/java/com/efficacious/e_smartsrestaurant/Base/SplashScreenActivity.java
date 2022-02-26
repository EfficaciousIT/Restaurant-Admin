package com.efficacious.e_smartsrestaurant.Base;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.efficacious.e_smartsrestaurant.Billing.BillTableStatusFragment;
import com.efficacious.e_smartsrestaurant.ExistingOrder.ExistingOrderFragment;
import com.efficacious.e_smartsrestaurant.Kitchen.KitchenHomeFragment;
import com.efficacious.e_smartsrestaurant.R;
import com.efficacious.e_smartsrestaurant.TakeAway.TakeAwayOrderHistoryFragment;
import com.efficacious.e_smartsrestaurant.WebService.WebConstant;

import java.util.Timer;
import java.util.TimerTask;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        ImageView logo = findViewById(R.id.logo);
        TextView appName = findViewById(R.id.appName);
        ScaleAnimation zoom = new ScaleAnimation(1,1,0,1);
        zoom.setDuration(1000);
        logo.startAnimation(zoom);
        appName.startAnimation(zoom);
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logo.startAnimation(zoom);
            }
        });

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
                finish();
            }
        },2000);

    }

    private void openFragments(String flag) {

    }
}