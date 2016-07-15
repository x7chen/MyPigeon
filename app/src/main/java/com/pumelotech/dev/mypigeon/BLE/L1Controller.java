package com.pumelotech.dev.mypigeon.BLE;

import android.content.Context;
import android.os.Environment;
import android.os.Message;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

/**
 * Created by Administrator on 2016/7/15.
 */
public class L1Controller implements BleProfileCallback {
    Context mContext;
    private TimerThread sendTimerThread;
    private TimerThread receiveTimerThread;
    private sendThread mSendThread;
    private Packet sPacket = new Packet();
    private Packet rPacket = new Packet();
    private BleProfiles mBleProfiles;
    static private L1ControllerCallback mL1ControllerCallback;
    static private L1Controller mL1Controller;
    private int resent_cnt = 0;

    public L1Controller(Context context) {
        mContext = context;
        sendTimerThread = new TimerThread().setStatus(TimerThread.STOP).setWhat(0xAA);
        sendTimerThread.start();
        receiveTimerThread = new TimerThread().setStatus(TimerThread.STOP).setWhat(0xBB);
        receiveTimerThread.start();
        mBleProfiles = BleProfiles.getInstance(this);
    }

    public static L1Controller getInstance(Context context,L1ControllerCallback callback) {
        if (mL1Controller == null) {
            mL1Controller = new L1Controller(context);
        }
        mL1ControllerCallback = callback;
        return mL1Controller;
    }

    final android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0xAA) {
                if (mL1ControllerCallback != null) {
                    mL1ControllerCallback.onTimeOut();
                    Log.i(LeConnector.TAG, "ACK TimeOut!");
                }
            } else if (msg.what == 0xBB) {
                rPacket.clear();
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

    public void writeCharacteristic(byte[] data) {
        mBleProfiles.writeWorkCharacteristic(data);
    }

    public void send(int command, int key, byte[] data, int length) {
        Packet.PacketValue packetValue = new Packet.PacketValue();
        packetValue.setCommandId((byte) command);
        packetValue.setKey((byte) key);
        packetValue.setValue(data);
        sPacket.setPacketValue(packetValue, true);
        sPacket.print();
        sendPacket(sPacket);
        resent_cnt = 3;
    }

    private void sendACK(Packet rPacket, boolean error) {
        Packet.L1Header l1Header = new Packet.L1Header();
        l1Header.setLength((short) 0);
        l1Header.setACK(true);
        l1Header.setError(error);
        l1Header.setSequenceId(rPacket.getL1Header().getSequenceId());
        l1Header.setCRC16((short) 0);
        sPacket.setL1Header(l1Header);
        sPacket.setPacketValue(null, false);
        sPacket.print();

        final byte[] data = sPacket.toByteArray();
        mSendThread = new sendThread(data);
        mSendThread.start();
        sendTimerThread.setStatus(TimerThread.STOP);
        writeLog("Send ACK:" + sPacket.toString());
    }

    private void sendPacket(Packet packet) {

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
                writeCharacteristic(sendData);
                SEND_OVER = false;
            }
        }
    }

    private void resolve(Packet.PacketValue packetValue) {
        byte command = packetValue.getCommandId();
        byte key = packetValue.getKey();
        int length = packetValue.getValueLength();
        byte[] data = packetValue.getValue();
        mL1ControllerCallback.onResolved(command, key, data, length);
    }

    @Override
    public void onSending() {
        if (mSendThread != null) {
            mSendThread.updateStatus(true);
        }
    }

    @Override
    public void onInitialized() {

    }

    @Override
    public void onReceived(byte[] data) {
        rPacket.append(data);
        receiveTimerThread.setTimeOut(500).setStatus(TimerThread.RESTART);
        int checkResult = rPacket.checkPacket();
        Log.i(LeConnector.TAG, "Check:" + Integer.toHexString(checkResult));
        rPacket.print();
        //数据头错误，清空
        if (checkResult == 0x05) {
            rPacket.clear();
        }
        //发送成功
        else if (checkResult == 0x10) {
            sendTimerThread.setStatus(TimerThread.STOP);
            receiveTimerThread.setStatus(TimerThread.STOP);
            writeLog("Receive ACK:" + rPacket.toString());
            rPacket.clear();
        }
        //ACK错误，需要重发
        else if (checkResult == 0x30) {
            sendTimerThread.setStatus(TimerThread.STOP);
            receiveTimerThread.setStatus(TimerThread.STOP);
            writeLog("Receive ACK:" + rPacket.toString());
            if (0 < resent_cnt--) {
                Log.i(LeConnector.TAG, "Resent Packet!");
                sendPacket(sPacket);
            } else {
            }
            rPacket.clear();
        }
        //接收数据包校验正确
        else if (checkResult == 0) {
            receiveTimerThread.setStatus(TimerThread.STOP);
            try {
                Packet.PacketValue packetValue = (Packet.PacketValue) rPacket.getPacketValue().clone();
                resolve(packetValue);
            } catch (CloneNotSupportedException e) {
                Log.i(LeConnector.TAG, "Packet.PacketValue:CloneNotSupportedException");
            }
            Log.i(LeConnector.TAG, "Send ACK!");
            writeLog("Receive:" + rPacket.toString());
            sendACK(rPacket, false);
            rPacket.clear();
        }
        //接收数据包校验错误
        else if (checkResult == 0x0b) {
            receiveTimerThread.setStatus(TimerThread.STOP);
            writeLog("Receive:" + rPacket.toString());
            sendACK(rPacket, true);
            rPacket.clear();
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
}
