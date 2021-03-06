package com.pumelotech.dev.mypigeon;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.pumelotech.dev.mypigeon.DataType.PigeonInfo;
import com.pumelotech.dev.mypigeon.DataType.RecordInfo;
import com.pumelotech.dev.mypigeon.DataType.ShedInfo;
import com.pumelotech.dev.mypigeon.DataType.UserInfo;

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

    public static MyPigeonDAO getInstance(Context context) {
        if (mContext == null) {
            mContext = context;
        }
        return getInstance();
    }

    public static MyPigeonDAO getInstance() {
        if (mContext == null) {
            return null;
        }
        if (mMyPigeonDAO == null) {
            mMyPigeonDAO = new MyPigeonDAO();
        }
        return mMyPigeonDAO;
    }

    public List<PigeonInfo> getAllPigeon() {
        List<PigeonInfo> pigeonInfoList = new ArrayList<>();
        Cursor cs = db.rawQuery("SELECT * FROM PigeonTable", new String[]{});
        while (cs.moveToNext()) {
            PigeonInfo pigeon = new PigeonInfo();
            pigeon.Name = cs.getString(cs.getColumnIndex("name"));
            pigeon.ID = cs.getString(cs.getColumnIndex("pigeon_id"));
            pigeon.BirthDate = cs.getString(cs.getColumnIndex("birth_date"));
            pigeon.ShedID = cs.getString(cs.getColumnIndex("shed_id"));
            pigeon.OwnerID = cs.getString(cs.getColumnIndex("owner_id"));
            pigeon.Status = cs.getString(cs.getColumnIndex("status"));
            pigeon.FlyTimes = cs.getInt(cs.getColumnIndex("fly_times"));
            pigeon.TotalDistance = cs.getInt(cs.getColumnIndex("total_distance"));
            pigeon.TotalMinutes = cs.getInt(cs.getColumnIndex("total_minutes"));
            pigeonInfoList.add(pigeon);
        }
        cs.close();
        return pigeonInfoList;
    }

    public int getPigeonIndex(String id) {
        int num = 0;
        Cursor cs = db.rawQuery("SELECT * FROM PigeonTable WHERE pigeon_id=" + "'"+id+"'", new String[]{});
        while (cs.moveToNext()) {
            num = cs.getInt(cs.getColumnIndex("num"));
        }
        cs.close();
        return num;
    }
    public PigeonInfo getPigeon(String id){
        PigeonInfo pigeon = new PigeonInfo();
        Cursor cs = db.rawQuery("SELECT * FROM PigeonTable WHERE pigeon_id=" + "'"+id+"'", new String[]{});
        while (cs.moveToNext()) {
            pigeon.Name = cs.getString(cs.getColumnIndex("name"));
            pigeon.ID = cs.getString(cs.getColumnIndex("pigeon_id"));
            pigeon.BirthDate = cs.getString(cs.getColumnIndex("birth_date"));
            pigeon.ShedID = cs.getString(cs.getColumnIndex("shed_id"));
            pigeon.OwnerID = cs.getString(cs.getColumnIndex("owner_id"));
            pigeon.Status = cs.getString(cs.getColumnIndex("status"));
            pigeon.FlyTimes = cs.getInt(cs.getColumnIndex("fly_times"));
            pigeon.TotalDistance = cs.getInt(cs.getColumnIndex("total_distance"));
            pigeon.TotalMinutes = cs.getInt(cs.getColumnIndex("total_minutes"));
        }
        cs.close();
        return pigeon;
    }
    public void insertPigeon(PigeonInfo pigeonInfo) {
        String sql = "insert into PigeonTable (Name,pigeon_id,birth_date,shed_id,owner_id,status," +
                "fly_times,total_distance,total_minutes)values(?,?,?,?,?,?,?,?,?)";
        db.execSQL(sql, new Object[]{pigeonInfo.Name, pigeonInfo.ID, pigeonInfo.BirthDate,
                pigeonInfo.ShedID, pigeonInfo.OwnerID, pigeonInfo.Status, pigeonInfo.FlyTimes,
                pigeonInfo.TotalDistance, pigeonInfo.TotalMinutes});
    }

    public void updatePigeon(int index, PigeonInfo pigeonInfo) {
        String sql = "update PigeonTable set Name=?, pigeon_id=?,birth_date=?, shed_id=?, owner_id=?," +
                "status=?,fly_times=?,total_distance=?,total_minutes=?" +
                " where num= " + index;
        db.execSQL(sql, new Object[]{pigeonInfo.Name, pigeonInfo.ID, pigeonInfo.BirthDate,
                pigeonInfo.ShedID, pigeonInfo.OwnerID, pigeonInfo.Status, pigeonInfo.FlyTimes,
                pigeonInfo.TotalDistance, pigeonInfo.TotalMinutes});
    }

    public void deletePigeon(int index) {
        String sql = "delete from PigeonTable where num=" + index;
        db.execSQL(sql);
    }

    public List<UserInfo> getAllUser() {
        List<UserInfo> userInfoList = new ArrayList<>();
        Cursor cs = db.rawQuery("SELECT * FROM UserTable", new String[]{});
        while (cs.moveToNext()) {
            UserInfo user = new UserInfo();
            user.Name = cs.getString(cs.getColumnIndex("Name"));
            user.ID = cs.getString(cs.getColumnIndex("user_id"));
            user.Sex = cs.getString(cs.getColumnIndex("Sex"));
            user.BirthDate = cs.getString(cs.getColumnIndex("BirthDate"));
            user.PhoneNum = cs.getString(cs.getColumnIndex("PhoneNum"));
            user.Address = cs.getString(cs.getColumnIndex("Address"));
            userInfoList.add(user);
        }
        cs.close();
        return userInfoList;
    }

    public void insertUser(UserInfo user) {
        String sql = "insert into UserTable (Name,user_id,Sex,BirthDate,PhoneNum,Address)values(?,?,?,?,?,?)";
        db.execSQL(sql, new Object[]{user.Name, user.ID, user.Sex, user.BirthDate, user.PhoneNum, user.Address});
    }

    public void updateUser(int index, UserInfo user) {
        String sql = "update UserTable set Name=? user_id=?, Sex=?, BirthDate=?,PhoneNum=?,Address=?" +
                " where num= " + index;
        db.execSQL(sql, new Object[]{user.Name, user.ID, user.Sex, user.BirthDate, user.PhoneNum, user.Address});
    }

    public void deleteUser(int index) {
        String sql = "delete from UserTable where num=" + index;
        db.execSQL(sql);
    }

    public List<ShedInfo> getAllShed() {
        List<ShedInfo> shedInfoList = new ArrayList<>();
        Cursor cs = db.rawQuery("SELECT * FROM ShedTable", new String[]{});
        while (cs.moveToNext()) {
            ShedInfo shed = new ShedInfo();
            shed.Name = cs.getString(cs.getColumnIndex("Name"));
            shed.ID = cs.getString(cs.getColumnIndex("shed_id"));
            shed.Address = cs.getString(cs.getColumnIndex("Address"));
            shedInfoList.add(shed);
        }
        cs.close();
        return shedInfoList;
    }

    public void insertShed(ShedInfo shed) {
        String sql = "insert into ShedTable (Name,shed_id,Address)values(?,?,?)";
        db.execSQL(sql, new Object[]{shed.Name, shed.ID, shed.Address});
    }

    public void updateShed(int index, ShedInfo shed) {
        String sql = "update ShedTable set Name=? shed_id=?,Address=?" +
                " where num= " + index;
        db.execSQL(sql, new Object[]{shed.Name, shed.ID, shed.Address});
    }

    public void deleteShed(int index) {
        String sql = "delete from ShedTable where num=" + index;
        db.execSQL(sql);
    }


    public List<RecordInfo> getPigeonRecord(String pigeon_id) {
        List<RecordInfo> recordInfoList = new ArrayList<>();
        Cursor cs = db.rawQuery("SELECT * FROM RecordTable where pigeon_id=? order by num desc ", new String[]{pigeon_id});
        while (cs.moveToNext()) {
            RecordInfo record = new RecordInfo();
            record.PigeonID = cs.getString(cs.getColumnIndex("pigeon_id"));
            record.Status = cs.getString(cs.getColumnIndex("status"));
            record.StartTime = cs.getString(cs.getColumnIndex("start_time"));
            record.StartShedID = cs.getString(cs.getColumnIndex("start_shed_id"));
            record.ArriveTime = cs.getString(cs.getColumnIndex("arrive_time"));
            record.ArriveShedID = cs.getString(cs.getColumnIndex("arrive_shed_id"));
            record.DistanceMeter = cs.getInt(cs.getColumnIndex("distance_meter"));
            record.ElapsedMinutes = cs.getInt(cs.getColumnIndex("elapsed_minutes"));

            recordInfoList.add(record);
        }
        cs.close();
        return recordInfoList;
    }

    public void insertRecord(RecordInfo record) {
        String sql = "insert into RecordTable (pigeon_id,status,start_time,start_shed_id," +
                "arrive_time,arrive_shed_id,distance_meter,elapsed_minutes)values(?,?,?,?,?,?,?,?)";
        db.execSQL(sql, new Object[]{record.PigeonID, record.Status, record.StartTime,
                record.StartShedID, record.ArriveTime, record.ArriveShedID, record.DistanceMeter, record.ElapsedMinutes});
    }

    public int getActiveRecordIndex(String id) {
        int num = 0;
        Cursor cs = db.rawQuery("SELECT * FROM RecordTable WHERE pigeon_id='" + id + "' AND status='FLY'", new String[]{});
        while (cs.moveToNext()) {
            num = cs.getInt(cs.getColumnIndex("num"));
        }
        cs.close();
        return num;
    }

    public RecordInfo getRecord(int index) {
        RecordInfo record = new RecordInfo();
        Cursor cs = db.rawQuery("SELECT * FROM RecordTable WHERE num=" + index, new String[]{});
        if (cs.moveToNext()) {
            record.PigeonID = cs.getString(cs.getColumnIndex("pigeon_id"));
            record.Status = cs.getString(cs.getColumnIndex("status"));
            record.StartTime = cs.getString(cs.getColumnIndex("start_time"));
            record.StartShedID = cs.getString(cs.getColumnIndex("start_shed_id"));
            record.ArriveTime = cs.getString(cs.getColumnIndex("arrive_time"));
            record.ArriveShedID = cs.getString(cs.getColumnIndex("arrive_shed_id"));
            record.DistanceMeter = cs.getInt(cs.getColumnIndex("distance_meter"));
            record.ElapsedMinutes = cs.getInt(cs.getColumnIndex("elapsed_minutes"));
        }
        cs.close();
        return record;
    }

    public void updateRecord(int index, RecordInfo record) {
        String sql = "update RecordTable set pigeon_id=?, status=?,start_time=?,start_shed_id=?," +
                "arrive_time=?,arrive_shed_id=?,distance_meter=?,elapsed_minutes=?" +
                " where num= " + index;
        db.execSQL(sql, new Object[]{record.PigeonID, record.Status, record.StartTime,
                record.StartShedID, record.ArriveTime, record.ArriveShedID, record.DistanceMeter, record.ElapsedMinutes});
    }

    public void deleteRecord(int index) {
        String sql = "delete from RecordTable where num=" + index;
        db.execSQL(sql);
    }
}
