package com.pumelotech.dev.mypigeon.BLE;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.Message;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;

/**
 * Created by Administrator on 2015/10/16.
 */
public class PacketParser {

    private int mVERSION = 110;

    private final static String TAG = "PacketParserService";

    public final static String ACTION_PACKET_HANDLE =
            "com.pumelotech.dev.mypigeon.packet.parser.ACTION_PACKET_HANDLE";
    public final static String ACTION_PROGRESSBAR =
            "com.pumelotech.dev.mypigeon.parser.ACTION_PROGRESSBAR";
    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    boolean BLE_CONNECT_STATUS = false;
    private int resent_cnt = 0;
    private LeConnector leConnector;
    private CallBack mPacketCallBack;
    private BluetoothDevice mDevice;

    private TimerThread sendTimerThread;
    private TimerThread receiveTimerThread;
    public static final byte RECEIVED_PIGEON_RECORD = 1;
    public static final byte RECEIVED_SPORT_DATA = 2;
    public static final byte RECEIVED_DAILY_DATA = 3;

    private Packet send_packet = new Packet();
    private Packet receive_packet = new Packet();
    private sendThread mSendThread;

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
        mContext = context;
        sendTimerThread = new TimerThread().setStatus(TimerThread.STOP).setWhat(0xAA);
        sendTimerThread.start();
        receiveTimerThread = new TimerThread().setStatus(TimerThread.STOP).setWhat(0xBB);
        receiveTimerThread.start();
        leConnector = LeConnector.getInstance(context);
        if (leConnector != null) {
            leConnector.registerCallbacks(leConnectorCallBacks);
        }
    }

    final android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0xAA) {
                if (mPacketCallBack != null) {
                    mPacketCallBack.onTimeOut();
                    Log.i(LeConnector.TAG, "ACK TimeOut!");
                }
            } else if (msg.what == 0xBB) {
                receive_packet.clear();
            }
        }
    };

    static void writeLog(String content) {
        String logFileName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/MyPigeon";
        File file = new File(logFileName);
        if (!file.exists()) {
            file.mkdirs();
        }
        logFileName += "/Log.txt";
        FileWriter fileWriter;
        try {
            fileWriter = new FileWriter(logFileName, true);
            fileWriter.append(content);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isIdle() {
        if (sendTimerThread == null || receiveTimerThread == null) {
            return false;
        }
        if ((TimerThread.STOP.equals(sendTimerThread.getStatus()))
                && (TimerThread.STOP.equals(receiveTimerThread.getStatus()))) {
            return true;
        } else {
            return false;
        }
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

    public int getVersion() {
        return mVERSION;
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

        Packet.PacketValue packetValue = new Packet.PacketValue();
        packetValue.setCommandId((byte) (0x10));
        packetValue.setKey((byte) (0x01));
        packetValue.setValue(Packet.intToByte(aData));
        send_packet.setPacketValue(packetValue, true);
        send_packet.print();
        send(send_packet);
        resent_cnt = 3;
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

    private void sendACK(Packet rPacket, boolean error) {
        Packet.L1Header l1Header = new Packet.L1Header();
        l1Header.setLength((short) 0);
        l1Header.setACK(true);
        l1Header.setError(error);
        l1Header.setSequenceId(rPacket.getL1Header().getSequenceId());
        l1Header.setCRC16((short) 0);
        send_packet.setL1Header(l1Header);
        send_packet.setPacketValue(null, false);
        send_packet.print();

        final byte[] data = send_packet.toByteArray();
        mSendThread = new sendThread(data);
        mSendThread.start();
        sendTimerThread.setStatus(TimerThread.STOP);
        writeLog("Send ACK:" + send_packet.toString());
    }

    private void send(Packet packet) {

        final byte[] data = packet.toByteArray();
        if (!isIdle()) {
            return;
        }
        mSendThread = new sendThread(data);
        mSendThread.start();
        sendTimerThread.setTimeOut(500).setStatus(TimerThread.RESTART);
        writeLog("Send:" + packet.toString());
    }

    class sendThread extends Thread {
        byte[] mData;
        boolean SEND_OVER = true;

        sendThread(byte[] data) {
            mData = data;
        }

        public void updateStatus(boolean status) {
            SEND_OVER = status;
        }

        public void run() {
            final int packLength = 20;
            int leftLength = mData.length;
            byte[] sendData;
            int sendIndex = 0;
            try {
                Thread.sleep(50L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            while (leftLength > 0) {
                if (leftLength <= packLength) {
                    sendData = Arrays.copyOfRange(mData, sendIndex, sendIndex + leftLength);
                    sendIndex += leftLength;
                    leftLength = 0;
                } else {
                    sendData = Arrays.copyOfRange(mData, sendIndex, sendIndex + packLength);
                    sendIndex += packLength;
                    leftLength -= packLength;
                }
                do {
                    try {
                        Thread.sleep(20L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } while (!SEND_OVER);
                leConnector.send(sendData);
                SEND_OVER = false;
            }
        }
    }

    private PigeonRecord PigeonRecordFromByte(byte[] data) {
        PigeonRecord pigeonRecord = new PigeonRecord();

        long aData;

        aData = data[0] & 0xFFL;
        aData = (aData << 8) | (data[1] & 0xFFL);
        aData = (aData << 8) | (data[2] & 0xFFL);
        aData = (aData << 8) | (data[3] & 0xFFL);
        pigeonRecord.ID = (int)aData;

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


    class TimerThread extends Thread {
        static final String START = "start";
        static final String RESTART = "restart";
        static final String PAUSE = "pause";
        static final String STOP = "stop";
        static final String EXIT = "exit";
        int TimeOut = 100;
        int mCount = 0;
        int What = 0;
        public String Status;

        public void run() {
            while (true) {
                if (START.equals(Status)) {
                    try {
                        Thread.sleep(20);
                        mCount++;

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else if (RESTART.equals(Status)) {
                    mCount = 0;
                    Status = START;
                    Thread.yield();
                } else if (PAUSE.equals(Status)) {
                    Thread.yield();
                } else if (STOP.equals(Status)) {
                    mCount = 0;
                    Thread.yield();
                } else if (EXIT.equals(Status)) {
                    break;
                }
                if (mCount >= TimeOut) {
                    mHandler.sendEmptyMessage(What);
                    mCount = 0;
                    Status = STOP;
                }

            }
        }

        public int getWhat() {
            return What;
        }

        public TimerThread setWhat(int what) {
            What = what;
            return this;
        }

        public String getStatus() {
            return Status;
        }

        public TimerThread setStatus(String status) {
            Status = status;
            return this;
        }

        public int getTimeOut() {
            return TimeOut;
        }

        public TimerThread setTimeOut(int mTimeOut) {
            this.TimeOut = mTimeOut;
            return this;
        }
    }

    LeConnector.LeConnectorCallBacks leConnectorCallBacks = new LeConnector.LeConnectorCallBacks() {
        @Override
        public void onDeviceConnected() {
            receive_packet.clear();
            send_packet.clear();
        }

        @Override
        public void onDeviceDisconnected() {
            BLE_CONNECT_STATUS = false;
            Intent intent = new Intent(ACTION_PACKET_HANDLE);
            intent.putExtra("CONN", BLE_CONNECT_STATUS);
            mContext.sendBroadcast(intent);
            if (mPacketCallBack != null) {
                mPacketCallBack.onConnectStatusChanged(false);
            }
            receive_packet.clear();
            send_packet.clear();
        }

        @Override
        public void onServiceFound() {

        }

        @Override
        public void onReceived(byte[] data) {
            receive_packet.append(data);
            receiveTimerThread.setTimeOut(500).setStatus(TimerThread.RESTART);
            int checkResult = receive_packet.checkPacket();
            Log.i(LeConnector.TAG, "Check:" + Integer.toHexString(checkResult));
            receive_packet.print();
            //数据头错误，清空
            if (checkResult == 0x05) {
                receive_packet.clear();
            }
            //发送成功
            else if (checkResult == 0x10) {
                sendTimerThread.setStatus(TimerThread.STOP);
                receiveTimerThread.setStatus(TimerThread.STOP);
                writeLog("Receive ACK:" + receive_packet.toString());
                receive_packet.clear();
                if (mPacketCallBack != null) {
                    mPacketCallBack.onSendSuccess();
                }
            }
            //ACK错误，需要重发
            else if (checkResult == 0x30) {
                sendTimerThread.setStatus(TimerThread.STOP);
                receiveTimerThread.setStatus(TimerThread.STOP);
                writeLog("Receive ACK:" + receive_packet.toString());
                if (0 < resent_cnt--) {
                    Log.i(LeConnector.TAG, "Resent Packet!");
                    send(send_packet);
                } else {
                    if (mPacketCallBack != null) {
                        mPacketCallBack.onSendFailure();
                    }
                }
                receive_packet.clear();
            }
            //接收数据包校验正确
            else if (checkResult == 0) {
                receiveTimerThread.setStatus(TimerThread.STOP);
                try {
                    Packet.PacketValue packetValue = (Packet.PacketValue) receive_packet.getPacketValue().clone();
                    resolve(packetValue);
                } catch (CloneNotSupportedException e) {
                    Log.i(LeConnector.TAG, "Packet.PacketValue:CloneNotSupportedException");
                }
                Log.i(LeConnector.TAG, "Send ACK!");
                writeLog("Receive:" + receive_packet.toString());
                sendACK(receive_packet, false);
                receive_packet.clear();
            }
            //接收数据包校验错误
            else if (checkResult == 0x0b) {
                receiveTimerThread.setStatus(TimerThread.STOP);
                writeLog("Receive:" + receive_packet.toString());
                sendACK(receive_packet, true);
                receive_packet.clear();
            }
        }

        @Override
        public void onInitialized() {
            if (mPacketCallBack != null) {
                mPacketCallBack.onConnectStatusChanged(true);
            }
            setTime();
        }

        @Override
        public void onSending() {
            if (mSendThread != null) {
                mSendThread.updateStatus(true);
            }
        }

        @Override
        public void onError(String message, int errorCode) {
            mPacketCallBack.onCharacteristicNotFound();
        }

        @Override
        public void onDeviceNotSupported() {
            mPacketCallBack.onCharacteristicNotFound();
        }
    };
}
