package com.app.s236372.smaom;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBhjelp extends SQLiteOpenHelper
{
    public DBhjelp(Context context)
    {
        super(context,DBinfo.DBnavn,null,DBinfo.DBversjon);

    }
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String createTable = "CREATE TABLE " + DBinfo.Nyoppgave.TABELL + " ( " +
                DBinfo.Nyoppgave._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DBinfo.Nyoppgave.KOL + " TEXT NOT NULL);" ;
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + DBinfo.Nyoppgave.TABELL);
        onCreate(db);
    }
}
