package com.pumelotech.dev.mypigeon.BLE;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.pumelotech.dev.mypigeon.DataType.PigeonInfo;
import com.pumelotech.dev.mypigeon.DataType.RecordInfo;
import com.pumelotech.dev.mypigeon.MyApplication;
import com.pumelotech.dev.mypigeon.MyPigeonDAO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Administrator on 2015/10/16.
 */
public class PacketParser implements L1ControllerCallback {

    private final static String TAG = MyApplication.DebugTag;
    public final static String ACTION_PACKET_HANDLE =
            "com.pumelotech.dev.mypigeon.packet.parser.ACTION_PACKET_HANDLE";


    public static final int NO_TRANSFER = 0;
    public static final int START_TRANSFER = 1;
    public static final int FINISHED_TRANSFER = 2;
    private int mFileTransferStatus = NO_TRANSFER;
    private boolean isDeviceConnected = false;
    static private Context mContext;
    static private PacketParser mPacketParser;
    L1Controller mL1Controller;

    static public PacketParser getInstance(Context context) {
        if (mContext == null) {
            mContext = context;
        }
        if (mPacketParser == null) {
            mPacketParser = new PacketParser(mContext);

        }

        return mPacketParser;
    }

    public PacketParser(Context context) {
        mL1Controller = L1Controller.getInstance(context, this);
    }

    public void requestRecord() {
        byte[] data = {};
        mL1Controller.send(0xA0, 0x01, data);
    }

    public void setTime() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        int aData;
        aData = (year - 2000) & 0x3F;
        aData = aData << 4 | ((month + 1) & 0x0F);
        aData = aData << 5 | (day & 0x1F);
        aData = aData << 5 | (hour & 0x1F);
        aData = aData << 6 | (minute & 0x3F);
        aData = aData << 6 | (second & 0x3F);

        mL1Controller.send(0x10, 0x01, Packet.intToByte(aData));
    }

    @Override
    public void onTimeOut() {

    }

    @Override
    public void onResolved(int command, int key, byte[] data, int length) {
        Log.i(TAG, "onResolved:" + command + ":" + key);

        switch (command) {
            case 160:
                switch (key) {
                    case 1:
                        record(data);
                        break;
                    default:
                        break;
                }
                break;
        }
    }

    @Override
    public void onInitialized() {
        setTime();
    }

    void record(byte[] data) {
        if (data.length < 21) {
            Log.i(TAG, "record length is not enough!");
            return;
        }
        String ID = new String(data, 1, 16);
        Log.i(TAG, "ID:" + ID);
        long aData;
        aData = data[17] & 0xFFL;
        aData = (aData << 8) | (data[18] & 0xFFL);
        aData = (aData << 8) | (data[19] & 0xFFL);
        aData = (aData << 8) | (data[20] & 0xFFL);
        Log.i(TAG, String.format(Locale.ENGLISH, "%08X", aData));
        long second = aData & 0x3F;
        aData >>>= 6;
        long minute = aData & 0x3F;
        aData >>>= 6;
        long hour = aData & 0x1F;
        aData >>>= 5;
        long day = aData & 0x1F;
        aData >>>= 5;
        long month = aData & 0x0F;
        aData >>>= 4;
        long year = 2000 + (aData & 0x3F);
        String aTime = String.format(Locale.ENGLISH, "%04d-%02d-%02d %02d:%02d:%02d", year, month, day, hour, minute, second);
        Log.i(TAG, aTime);
        PigeonInfo pigeon;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        MyPigeonDAO myPigeonDAO = MyPigeonDAO.getInstance();
        if (myPigeonDAO != null) {
            Log.i(TAG,"myPigeonDAO != null");
            int index = myPigeonDAO.getActiveRecordIndex(ID);
            RecordInfo record = myPigeonDAO.getRecord(index);
            if(record.PigeonID.equals("--")){
                return;
            }
            pigeon = myPigeonDAO.getPigeon(ID);
            Date start_time = new Date();
            Date arrive_time = new Date();
            try {
                start_time = format.parse(record.StartTime);
                arrive_time = format.parse(aTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            record.ArriveTime = aTime;
            record.PigeonID = ID;
            long between_minutes = (arrive_time.getTime() - start_time.getTime()) / 60000;
            record.ElapsedMinutes = (int) between_minutes;
            record.DistanceMeter = 100 * 1000;
            record.ArriveShedID = "C000003";
            record.Status = "REST";
            pigeon.FlyTimes = pigeon.FlyTimes + 1;
            pigeon.TotalDistance = pigeon.TotalDistance + record.DistanceMeter;
            pigeon.TotalMinutes = pigeon.TotalMinutes + record.ElapsedMinutes;
            pigeon.Status="REST";
            myPigeonDAO.updatePigeon(myPigeonDAO.getPigeonIndex(pigeon.ID), pigeon);
            myPigeonDAO.updateRecord(index, record);
            for (PigeonInfo pigeonInfo : MyApplication.mPigeonList) {
                if (pigeon.ID.equals(pigeonInfo.ID)) {
                    MyApplication.mPigeonList.set(MyApplication.mPigeonList.indexOf(pigeonInfo), pigeon);
                    Log.i(TAG,"indexOf(pigeon):"+MyApplication.mPigeonList.indexOf(pigeonInfo));
                    MyApplication.pigeonRecyclerAdapter.notifyItemChanged(MyApplication.mPigeonList.indexOf(pigeonInfo));
                }

            }
            Log.i(TAG, "["+Thread.currentThread().getStackTrace()[2].getFileName()+","+Thread.currentThread().getStackTrace()[2].getLineNumber()+"]");
            MyApplication.mainActivity.updateDisplay();
            if(MyApplication.mRecordListAdapter!=null) {
                MyApplication.mRecordListAdapter.notifyDataSetChanged();
            }
            if(MyApplication.pigeonRecyclerAdapter!=null){
                MyApplication.pigeonListActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        MyApplication.pigeonRecyclerAdapter.notifyDataSetChanged();
                    }
                });

            }

        }

    }

    public void mock() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(50L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                int second = calendar.get(Calendar.SECOND);

            }
        }.start();
    }

}
