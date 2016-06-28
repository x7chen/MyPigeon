package com.pumelotech.dev.mypigeon;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2016/6/28.
 */
public class PigeonDBHelper extends SQLiteOpenHelper {


    String createPigeonTable = "Create Table IF NOT EXISTS PigeonTable (id INTEGER PRIMARY KEY AUTOINCREMENT " +
            "NOT NULL, name VARCHAR,shed_id INTEGER,owner_id INTEGER)";
    String createShedTable = "Create Table IF NOT EXISTS ShedTable (id INTEGER PRIMARY KEY AUTOINCREMENT " +
            "NOT NULL, name VARCHAR,address VARCHAR)";
    String createOwnerTable = "Create Table IF NOT EXISTS OwnerTable (id INTEGER PRIMARY KEY AUTOINCREMENT " +
            "NOT NULL,name VARCHAR,sex VARCHAR,telephone VARCHAR,address VARCHAR)";
    String createFlyTable = "Create Table IF NOT EXISTS FlyTable (id INTEGER PRIMARY KEY AUTOINCREMENT " +
            "NOT NULL,pigeon_id INTEGER,start_time VARCHAR,start_shed_id INTEGER,back_time VARCHAR," +
            "back_shed_id INTEGER,distance INTEGER,elapsed_time INTEGER)";

    public PigeonDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createPigeonTable);
        db.execSQL(createShedTable);
        db.execSQL(createOwnerTable);
        db.execSQL(createFlyTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
