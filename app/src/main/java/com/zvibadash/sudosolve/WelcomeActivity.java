package com.zvibadash.sudosolve;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Toast;

public class WelcomeActivity extends AppCompatActivity {
    CountDownTimer cdt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        Toast toast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT);
        cdt = new CountDownTimer(1_000, 1_000) {
            @Override
            public void onTick(long l) {
                toast.cancel();
                toast.setText("...");
                toast.show();
            }

            @Override
            public void onFinish() {
                startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
            }
        }.start();
    }
}