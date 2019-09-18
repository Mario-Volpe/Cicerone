package com.example.cicerone.data.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.example.cicerone.data.model.DBhelper;

public class DBManager
{
    private DBhelper dbhelper;
    public DBManager(Context ctx)
    {
        dbhelper=new DBhelper(ctx);
    }
    public void save(String sub, String txt, String date)
    {
        SQLiteDatabase db=dbhelper.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put("", sub);
        cv.put("", txt);
        cv.put("", date);
        try
        {
            db.insert("", null,cv);
        }
        catch (SQLiteException sqle)
        {
// Gestione delle eccezioni
        }
    }
    public boolean delete(long id)
    {
        SQLiteDatabase db=dbhelper.getWritableDatabase();
        try
        {
            if (db.delete("", ""+"=?", new String[]{Long.toString(id)})>0)
                return true;
            return false;
        }
        catch (SQLiteException sqle)
        {
            return false;
        }
    }
    public Cursor query()
    {
        Cursor crs=null;
        try
        {
            SQLiteDatabase db=dbhelper.getReadableDatabase();
            crs=db.query("", null, null, null, null, null, null, null);
        }
        catch(SQLiteException sqle)
        {
            return null;
        }
        return crs;
    }
}