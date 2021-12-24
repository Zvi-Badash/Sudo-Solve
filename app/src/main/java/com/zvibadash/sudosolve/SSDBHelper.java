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

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.zvibadash.sudosolve.SSDBContract.*;

public class SSDBHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "SudoSolve.db";

    /*
     * This amounts to:
     CREATE TABLE user (
       "_id" INTEGER PRIMARY KEY
     )
    * */
    private static final String SQL_CREATE_USER =
            "CREATE TABLE " + User.TABLE_NAME + " (" +
                    User.USERNAME + " TEXT PRIMARY KEY)";

    /*
     * This amounts to:
     CREATE TABLE stat (
                    username TEXT PRIMARY KEY,
                    stat_cont TEXT,
                    FOREIGN KEY(username) REFERENCES user (username)
     )
    * */
    private static final String SQL_CREATE_STATISTICS =
            "CREATE TABLE " + Statistics.TABLE_NAME + " (" +
                    Statistics.USERNAME + " TEXT PRIMARY KEY ," +
                    Statistics.STATISTICS_CONTENTS + "TEXT," +
                    "FOREIGN KEY(" + Statistics.USERNAME + ") REFERENCES " + User.TABLE_NAME + "(" + User.USERNAME + "))";

    /*
     * This amounts to:
          CREATE TABLE last_sess (
                    username TEXT PRIMARY KEY,
                    sess_cont TEXT,
                    FOREIGN KEY(username) REFERENCES user (username),
     )
    * */
    private static final String SQL_CREATE_LAST_SESS =
            "CREATE TABLE " + LastSessionForUser.TABLE_NAME + " (" +
                    LastSessionForUser.USERNAME + " TEXT PRIMARY KEY ," +
                    LastSessionForUser.SESSION_CONTENTS + " TEXT," +
                    "FOREIGN KEY(" + LastSessionForUser.USERNAME + ") REFERENCES " + User.TABLE_NAME + "(" + User.USERNAME + "))";

    public SSDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_USER);
        db.execSQL(SQL_CREATE_STATISTICS);
        db.execSQL(SQL_CREATE_LAST_SESS);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + User.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Statistics.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + LastSessionForUser.TABLE_NAME);
        db.execSQL("DROP TABLE session");
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
