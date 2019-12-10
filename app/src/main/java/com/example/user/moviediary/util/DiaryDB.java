package com.example.user.moviediary.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DiaryDB extends SQLiteOpenHelper {

    private static final String DB_NAME = "Diary";
    private static final int VERSION = 1;

    public DiaryDB(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String query = "CREATE TABLE USER_TBL("+
                "ID CHAR(20) not null,"+
                "PW CHAR(20)," +
                "NICKNAME CHAR())";
        db.execSQL(query);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
