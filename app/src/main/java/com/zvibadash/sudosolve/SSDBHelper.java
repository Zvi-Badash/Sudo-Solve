package com.zvibadash.sudosolve;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.zvibadash.sudosolve.SSDBContract.*;

public class SSDBHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "FeedReader.db";

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
     CREATE TABLE session (
                    sess_id INTEGER PRIMARY KEY,
                    sess_cont TEXT
     )
    * */
    private static final String SQL_CREATE_SESSION =
            "CREATE TABLE " + Session.TABLE_NAME + " (" +
                    Session.SESSION_ID + " INTEGER PRIMARY KEY," +
                    Session.SESSION_CONTENTS + " TEXT)";

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
                    sess_id INTEGER,
                    FOREIGN KEY(username) REFERENCES user (username),
                    FOREIGN KEY(sess_id) REFERENCES session (sess_id)
     )
    * */
    private static final String SQL_CREATE_LAST_SESS =
            "CREATE TABLE " + LastSessionForUser.TABLE_NAME + " (" +
                    LastSessionForUser.USERNAME + " TEXT PRIMARY KEY ," +
                    LastSessionForUser.SESSION_ID + " INTEGER," +
                    "FOREIGN KEY(" + LastSessionForUser.USERNAME + ") REFERENCES " + User.TABLE_NAME + "(" + User.USERNAME + ")," +
                    "FOREIGN KEY(" + LastSessionForUser.SESSION_ID + ") REFERENCES " + Session.TABLE_NAME + "(" + Session.SESSION_ID + "))";

    public SSDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_USER);
        db.execSQL(SQL_CREATE_SESSION);
        db.execSQL(SQL_CREATE_STATISTICS);
        db.execSQL(SQL_CREATE_LAST_SESS);

    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL("DROP TABLE IF EXISTS " + User.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Session.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Statistics.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + LastSessionForUser.TABLE_NAME);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
