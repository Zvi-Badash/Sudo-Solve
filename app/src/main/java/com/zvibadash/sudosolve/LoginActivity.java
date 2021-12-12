package com.zvibadash.sudosolve;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.zvibadash.sudosolve.SSDBContract.*;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {
    static final String TAG = "LOGIN_ACTIVITY";
    final int USERNAME_ERROR = 3;
    final int NO_ERROR = 0;

    SSDBHelper dbHelper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dbHelper = new SSDBHelper(getApplicationContext());
        db = dbHelper.getWritableDatabase();

        Button btLogin = findViewById(R.id.btLogin_login_activity);
        btLogin.setOnClickListener(view -> {
            EditText etUsername = findViewById(R.id.etUsername_login_activity);
            String username = etUsername.getText().toString();
            Log.i(TAG, "given username is " + username);

            switch (isValidUsername(username)) {
                case USERNAME_ERROR:
                    Toast.makeText(getApplicationContext(), "Hmm, it seems that this username does not appear in the system, try registering it first.", Toast.LENGTH_SHORT).show();
                    break;
                case NO_ERROR:
                    // TODO: make sure the login data moves to the home activity
                    startActivity(new Intent(this, HomeActivity.class));
                    break;
            }
        });

        Button btRegister = findViewById(R.id.btRegister_login_activity);
        btRegister.setOnClickListener(view -> startActivity(new Intent(this, RegisterActivity.class)));
    }

    private int isValidUsername(String givenUsername) {
        // If the username is of illegal length.
        if (!(givenUsername.length() < 16 && givenUsername.length() > 2))
            return USERNAME_ERROR;

        // If the username has non-alphanumeric characters.
        // This test and the former serve as the simplest defense measure against SQL injection.
        if (Pattern.compile("[^a-zA-Z0-9]").matcher(givenUsername).find())
            return USERNAME_ERROR;

        // Check to see if the username does not exists.
        String[] projection = {User.USERNAME};
        String[] selectionArgs = {givenUsername};
        String selection = User.USERNAME + " = ?";

        Cursor c = db.query(
                User.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (c.getCount() == 0)
            return USERNAME_ERROR;
        c.close();

        // No errors were found, return NO_ERROR
        return NO_ERROR;
    }
}