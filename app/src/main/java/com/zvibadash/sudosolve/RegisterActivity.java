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

import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    static final String TAG = "REGISTER_ACTIVITY";
    final int LENGTH_ERROR = 1;
    final int ALPHANUMERIC_ERROR = 2;
    final int ALREADY_EXISTS_ERROR = 3;
    final int NO_ERROR = 0;

    SSDBHelper dbHelper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        dbHelper = new SSDBHelper(getApplicationContext());
        db = dbHelper.getWritableDatabase();

        Button btRegister = findViewById(R.id.btRegister_register_activity);
        btRegister.setOnClickListener(view -> {
            EditText etUsername = findViewById(R.id.etUsername_register_activity);
            String username = etUsername.getText().toString();
            Log.i(TAG, "given username is " + username);

            switch (isValidUsername(username)) {
                case LENGTH_ERROR:
                    Toast.makeText(getApplicationContext(), "Usernames should be less than 16 characters but more than 2 characters.", Toast.LENGTH_SHORT).show();
                    break;
                case ALPHANUMERIC_ERROR:
                    Toast.makeText(getApplicationContext(), "Usernames should only contain alphanumeric characters.", Toast.LENGTH_SHORT).show();
                    break;
                case ALREADY_EXISTS_ERROR:
                    Toast.makeText(getApplicationContext(), "Oops, this username already exists in the system.\nTry to log in with it instead.", Toast.LENGTH_SHORT).show();
                    break;
                case NO_ERROR:
                    ContentValues values = new ContentValues();
                    values.put(SSDBContract.User.USERNAME, username);
                    long newRowId = db.insert(SSDBContract.User.TABLE_NAME, null, values);
                    Log.i(TAG, "new row inserted into " + SSDBContract.User.TABLE_NAME + ", with key " + newRowId);

                    // TODO: make sure the login data moves to the home activity
                    startActivity(new Intent(this, HomeActivity.class));
                    break;
            }
        });

        Button btLogin = findViewById(R.id.btLogin_register_activity);
        btLogin.setOnClickListener(view -> startActivity(new Intent(this, LoginActivity.class)));
    }

    private int isValidUsername(String givenUsername) {
        // If the username is of illegal length.
        if (!(givenUsername.length() < 16 && givenUsername.length() > 2))
            return LENGTH_ERROR;

        // If the username has non-alphanumeric characters.
        if (Pattern.compile("[^a-zA-Z0-9]").matcher(givenUsername).find())
            return ALPHANUMERIC_ERROR;

        // Check to see if the username already exists.
        String[] projection = {SSDBContract.User.USERNAME};
        String[] selectionArgs = {givenUsername};
        String selection = SSDBContract.User.USERNAME + " = ?";

        Cursor c = db.query(
                SSDBContract.User.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (c.getCount() != 0)
            return ALREADY_EXISTS_ERROR;
        c.close();

        // No errors were found, return NO_ERROR
        return NO_ERROR;
    }
}