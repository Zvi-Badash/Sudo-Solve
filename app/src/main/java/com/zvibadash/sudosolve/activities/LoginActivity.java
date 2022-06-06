/*
 * MIT License
 *
 * Copyright (c) 2021 Zvi Badash
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.zvibadash.sudosolve.activities;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.zvibadash.sudosolve.R;
import com.zvibadash.sudosolve.database.SSDBContract.User;
import com.zvibadash.sudosolve.database.SSDBHelper;
import com.zvibadash.sudosolve.database.SessionHandler;

import java.util.regex.Pattern;

public class LoginActivity extends LoginMenuTemplateActivity {
    static final String TAG = "LOGIN_ACTIVITY";
    final int USERNAME_ERROR = 3;
    final int NO_ERROR       = 0;

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
            String username = etUsername.getText().toString().trim().replaceAll("\\s","_");
            Log.i(TAG, "given username is " + username);

            switch (isValidUsername(username)) {
                case USERNAME_ERROR:
                    Toast.makeText(getApplicationContext(), "Hmm, it seems that this username does not appear in the system, try registering it first.", Toast.LENGTH_SHORT).show();
                    break;
                case NO_ERROR:
                    SessionHandler.delegateSession(getApplicationContext(), username);
                    startActivity(new Intent(this, HomeActivity.class));
                    break;
            }
        });

        Button btRegister = findViewById(R.id.btRegister_login_activity);
        btRegister.setOnClickListener(view -> startActivity(new Intent(this, RegisterActivity.class)));
    }

    /**
     * This method validates the given username given by the user.
     *
     * @param givenUsername the given username.
     * @return is it valid.
     * */
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