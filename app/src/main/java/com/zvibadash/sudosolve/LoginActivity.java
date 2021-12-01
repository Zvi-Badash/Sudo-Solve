package com.zvibadash.sudosolve;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {
    SSDBHelper dbHelper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dbHelper = new SSDBHelper(getApplicationContext());
        db = dbHelper.getWritableDatabase();

        Button btLogin = findViewById(R.id.btLogin);
        btLogin.setOnClickListener(view -> {
            EditText etUsername = findViewById(R.id.etUsername);

        });
    }

    private Boolean isValidUsername(String username) {
        // 2 < username's length < 16
        return username.length() < 16 && username.length() > 2;
    }
}