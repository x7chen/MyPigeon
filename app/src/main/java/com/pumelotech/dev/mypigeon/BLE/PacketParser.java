package com.pumelotech.dev.mypigeon.BLE;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Calendar;

/**
 * Created by Administrator on 2015/10/16.
 */
public class PacketParser implements L1ControllerCallback{

    private final static String TAG = "PacketParserService";


    public final static String ACTION_PACKET_HANDLE =
            "com.pumelotech.dev.mypigeon.packet.parser.ACTION_PACKET_HANDLE";
    boolean BLE_CONNECT_STATUS = false;

    private LeConnector leConnector;
    private CallBack mPacketCallBack;


    public static final byte RECEIVED_PIGEON_RECORD = 1;


    public static final int NO_TRANSFER = 0;
    public static final int START_TRANSFER = 1;
    public static final int FINISHED_TRANSFER = 2;
    private int mFileTransferStatus = NO_TRANSFER;
    private boolean isDeviceConnected = false;
    static private Context mContext;
    static private PacketParser mPacketParser;

    static public PacketParser getInstance(Context context) {
        if (mContext == null) {
            mContext = context;
        }
        return getInstance();
    }

    static public PacketParser getInstance() {
        if (mContext == null) {
            return null;
        }
        if (mPacketParser == null) {
            mPacketParser = new PacketParser(mContext);
        }
        return mPacketParser;
    }

    public PacketParser(Context context) {

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

        L1Controller.getInstance(mContext,this).send(0x10,0x01,Packet.intToByte(aData),4);
    }

    @Override
    public void onTimeOut() {

    }

    @Override
    public void onResolved(int command, int key, byte[] data, int length) {
        switch (command) {
            case (byte) 0xA0:
                switch (key) {
                    case (byte) 0x01:

                        break;
                    default:
                        break;
                }
                break;
        }
    }

    public static class PigeonRecord implements Parcelable {
        public int Year;
        public int Month;
        public int Day;
        public int Hour;
        public int Minute;
        public int Second;
        public int ID;

        public PigeonRecord() {
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.Year);
            dest.writeInt(this.Month);
            dest.writeInt(this.Day);
            dest.writeInt(this.Hour);
            dest.writeInt(this.Minute);
            dest.writeInt(this.Second);
            dest.writeInt(this.ID);
        }

        protected PigeonRecord(Parcel in) {
            this.Year = in.readInt();
            this.Month = in.readInt();
            this.Day = in.readInt();
            this.Hour = in.readInt();
            this.Minute = in.readInt();
            this.Second = in.readInt();
            this.ID = in.readInt();
        }

        public static final Creator<PigeonRecord> CREATOR = new Creator<PigeonRecord>() {
            @Override
            public PigeonRecord createFromParcel(Parcel source) {
                return new PigeonRecord(source);
            }

            @Override
            public PigeonRecord[] newArray(int size) {
                return new PigeonRecord[size];
            }
        };
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

                if (mPacketCallBack != null) {
                    mPacketCallBack.onDataReceived(RECEIVED_PIGEON_RECORD);
                }
            }
        }.start();
    }


    private PigeonRecord PigeonRecordFromByte(byte[] data) {
        PigeonRecord pigeonRecord = new PigeonRecord();

        long aData;

        aData = data[0] & 0xFFL;
        aData = (aData << 8) | (data[1] & 0xFFL);
        aData = (aData << 8) | (data[2] & 0xFFL);
        aData = (aData << 8) | (data[3] & 0xFFL);
        pigeonRecord.ID = (int) aData;

        aData = data[4] & 0xFFL;
        aData = (aData << 8) | (data[5] & 0xFFL);
        aData = (aData << 8) | (data[6] & 0xFFL);
        aData = (aData << 8) | (data[7] & 0xFFL);

        pigeonRecord.Second = (int) (aData & 0x3F);
        aData >>>= 6;
        pigeonRecord.Minute = (int) (aData & 0x3F);
        aData >>>= 6;
        pigeonRecord.Hour = (int) (aData & 0x1F);
        aData >>>= 5;
        pigeonRecord.Day = (int) (aData & 0x1F);
        aData >>>= 5;
        pigeonRecord.Month = (int) (aData & 0x0F);
        aData >>>= 4;
        pigeonRecord.Year = (int) (aData & 0x3F);

        return pigeonRecord;
    }

    private void resolve(Packet.PacketValue packetValue) {
        byte command = packetValue.getCommandId();
        byte key = packetValue.getKey();
        int length = packetValue.getValueLength();
        byte[] data = packetValue.getValue();

    }


    public interface CallBack {
        void onSendSuccess();

        void onSendFailure();

        void onTimeOut();

        void onConnectStatusChanged(boolean status);

        void onDataReceived(byte category);

        void onCharacteristicNotFound();
    }

    public void registerCallback(CallBack callBack) {
        mPacketCallBack = callBack;
    }
}
