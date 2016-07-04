package com.pumelotech.dev.mypigeon;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/7/4.
 */
public class MyPigeonDAO {
    static private MyPigeonDAO mMyPigeonDAO;
    static private Context mContext;
    public SQLiteDatabase db;
    public MyPigeonDAO() {
        String dbName = "MyPigeonDB.db";
        PigeonDBHelper pigeonDBHelper = new PigeonDBHelper(mContext, dbName, null, 1);
        db = pigeonDBHelper.getWritableDatabase();
    }
    public static MyPigeonDAO getInstance(Context context){
        if (mContext == null) {
            mContext = context;
        }
        return getInstance();
    }
    public static MyPigeonDAO getInstance(){
        if (mContext == null) {
            return null;
        }
        if (mMyPigeonDAO == null) {
            mMyPigeonDAO = new MyPigeonDAO();
        }
        return mMyPigeonDAO;
    }
    public List<PigeonInfo> getAllPigeon(){
        List<PigeonInfo> pigeonInfoList = new ArrayList<>();
        Cursor cs = db.rawQuery("SELECT * FROM PigeonTable", new String[]{});
        while (cs.moveToNext()) {
            PigeonInfo pigeon = new PigeonInfo();
            pigeon.Name=cs.getString(cs.getColumnIndex("name"));
            pigeon.ID=cs.getString(cs.getColumnIndex("pigeon_id"));
            pigeon.ShedID=cs.getString(cs.getColumnIndex("shed_id"));
            pigeon.OwnerID=cs.getString(cs.getColumnIndex("owner_id"));
            pigeon.Status=cs.getString(cs.getColumnIndex("name"));
            pigeonInfoList.add(pigeon);
        }
        return pigeonInfoList;
    }
    public void insertPigeon(PigeonInfo pigeonInfo){
        String sql = "insert into PigeonTable (name,pigeon_id,shed_id,owner_id)values(?,?,?,?)";
        db.execSQL(sql, new Object[] { pigeonInfo.Name, pigeonInfo.ID,pigeonInfo.ShedID,pigeonInfo.OwnerID});
    }

    public void updatePigeon(int index){

    }
}
