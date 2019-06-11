package com.example.cicerone.data.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBhelper extends SQLiteOpenHelper {
    public static final String DBNAME="BILLBOOK";
    public DBhelper(Context context) {
        super(context, DBNAME, null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String q="CREATE TABLE "+"Utente"+
                " ( _id INTEGER PRIMARY KEY AUTOINCREMENT," +
                " "+" TEXT," +
                " "+" TEXT," +
                " "+" TEXT)";
        db.execSQL(q);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    { }
}