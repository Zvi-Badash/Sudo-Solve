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

package com.zvibadash.sudosolve;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.zvibadash.sudosolve.database.SSDBContract;
import com.zvibadash.sudosolve.database.SSDBHelper;


// TODO: PRIORITY: MINOR.
//       DESC: There could be a risk for SQL injection in all of the following methods,
//             need to do research on the provided SQLiteDatabase methods.
public class SessionHandler {
    public static void delegateSession(Context context, String user) {
        Globals.CURRENT_SESSION = new Session(user, "");

        Session lastSess = updateFromPreviousSession(context, user);
        if (lastSess != null)
            Globals.CURRENT_SESSION = lastSess;

        _writeSession(context, Globals.CURRENT_SESSION);
    }

    public static void updateState(Context context, String boardState) {
        Globals.CURRENT_SESSION.lastBoardState = boardState;
        _writeSession(context, Globals.CURRENT_SESSION);
    }

    public static Session updateFromPreviousSession(Context context, String user) {
        Session sess = _readSession(context, user);
        if (sess == null) {
            Log.i("DB MESSAGE", "Couldn't fetch last session for " + user);
            return null;
        }
        Log.i("DB MESSAGE", "fetched " + sess.lastBoardState + " from memory.");
        Globals.CURRENT_SESSION.lastBoardState = sess.lastBoardState;
        return Globals.CURRENT_SESSION;
    }

    private static void _writeSession(Context context, Session sess) {
        SSDBHelper dbHelper = new SSDBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Check if the username exists in the DB, if not, write it. Else, update it.
        String[] projection = {SSDBContract.LastSessionForUser.USERNAME,
                SSDBContract.LastSessionForUser.SESSION_CONTENTS};

        String[] selectionArgs = {sess.userName};
        String selection = SSDBContract.LastSessionForUser.USERNAME + " = ?";

        Cursor c = db.query(
                SSDBContract.LastSessionForUser.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        int count = c.getCount();
        c.close();

        if (count == 0) { // Insert the new value
            ContentValues values = new ContentValues();
            values.put(SSDBContract.LastSessionForUser.USERNAME, sess.userName);
            values.put(SSDBContract.LastSessionForUser.SESSION_CONTENTS, sess.lastBoardState);

            db.insert(SSDBContract.LastSessionForUser.TABLE_NAME, null, values);
        } else { // Update the session
            // New value for one column
            ContentValues values = new ContentValues();
            values.put(SSDBContract.LastSessionForUser.SESSION_CONTENTS, sess.lastBoardState);

            // Which row to update, based on the title
            String uSelection = SSDBContract.LastSessionForUser.USERNAME + " = ?";
            String[] uSelectionArgs = {sess.userName};
            db.update(
                    SSDBContract.LastSessionForUser.TABLE_NAME,
                    values,
                    selection,
                    selectionArgs
            );
        }

    }

    private static Session _readSession(Context context, String user) {
        SSDBHelper dbHelper = new SSDBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String boardState;

        String[] projection = {SSDBContract.LastSessionForUser.USERNAME,
                SSDBContract.LastSessionForUser.SESSION_CONTENTS};

        String[] selectionArgs = {user};
        String selection = SSDBContract.LastSessionForUser.USERNAME + " = ?";

        Cursor c = db.query(
                SSDBContract.LastSessionForUser.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (c.getCount() == 0) {
            return null;
        }

        c.moveToFirst();
        boardState = c.getString(c.getColumnIndexOrThrow(SSDBContract.LastSessionForUser.SESSION_CONTENTS));
        c.close();

        return new Session(user, boardState);
    }
}
