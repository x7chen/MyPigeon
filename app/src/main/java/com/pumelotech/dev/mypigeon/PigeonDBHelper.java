package com.pumelotech.dev.mypigeon;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2016/6/28.
 */
public class PigeonDBHelper extends SQLiteOpenHelper {


    String createPigeonTable = "Create Table IF NOT EXISTS PigeonTable (num INTEGER PRIMARY KEY AUTOINCREMENT " +
            "NOT NULL, pigeon_id VARCHAR,name VARCHAR,birth_date VARCHAR,shed_id VARCHAR,owner_id VARCHAR," +
            "fly_times INTEGER,total_distance INTEGER,total_minutes INTEGER,status VARCHAR)";
    String createShedTable = "Create Table IF NOT EXISTS ShedTable (num INTEGER PRIMARY KEY AUTOINCREMENT " +
            "NOT NULL, shed_id VARCHAR,Name VARCHAR,address VARCHAR)";
    String createUserTable = "Create Table IF NOT EXISTS UserTable (num INTEGER PRIMARY KEY AUTOINCREMENT " +
            "NOT NULL,user_id VARCHAR,Name VARCHAR,Sex VARCHAR,BirthDate VARCHAR,PhoneNum VARCHAR,Address VARCHAR)";
    String createRecordTable = "Create Table IF NOT EXISTS RecordTable (num INTEGER PRIMARY KEY AUTOINCREMENT " +
            "NOT NULL,pigeon_id VARCHAR,status INTEGER,start_time VARCHAR,start_shed_id VARCHAR,arrive_time VARCHAR," +
            "arrive_shed_id VARCHAR,distance_meter INTEGER,elapsed_minutes INTEGER)";

    public PigeonDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createPigeonTable);
        db.execSQL(createShedTable);
        db.execSQL(createUserTable);
        db.execSQL(createRecordTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
