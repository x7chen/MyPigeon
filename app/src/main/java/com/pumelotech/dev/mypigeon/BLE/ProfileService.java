package com.pumelotech.dev.mypigeon.BLE;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.Intent;
import android.util.Log;

import java.util.List;
import java.util.UUID;

/**
 * Created by x7che on 2016/7/14.
 */
public class ProfileService {
    public static final UUID LE_UART_SERVICE_UUID = UUID.fromString("0000ffe0-0000-1000-8000-00805f9b34fb");
    public static final UUID LE_UART_CHAR_UUID = UUID.fromString("0000ffe1-0000-1000-8000-00805f9b34fb");
    private BluetoothGattCharacteristic leUartCharacteristic;
    LeConnector leConnector;
    ProfileServiceCallback profileServiceCallback;
    public ProfileService(ProfileServiceCallback callback) {
        profileServiceCallback =callback;
        leConnector=LeConnector.getInstance();
        List<BluetoothGattService> services = leConnector.getServices();
        for(BluetoothGattService service:services){
            List<BluetoothGattCharacteristic> characteristics = service.getCharacteristics();
            for (BluetoothGattCharacteristic characteristic:characteristics){
                if(characteristic.getUuid().equals(LE_UART_CHAR_UUID)){
                    leUartCharacteristic = characteristic;
                }
            }
        }
    }

    public void send(byte[] data) {
        if (leUartCharacteristic == null) {
            return;
        }
        leUartCharacteristic.setValue(data);
        leConnector.getBluetoothGatt().writeCharacteristic(leUartCharacteristic);
    }

    public void enableNotification() {
        leConnector.getBluetoothGatt().setCharacteristicNotification(leUartCharacteristic, true);

        List<BluetoothGattDescriptor> descriptors = leUartCharacteristic.getDescriptors();
        for (BluetoothGattDescriptor dp : descriptors) {
            dp.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            leConnector.getBluetoothGatt().writeDescriptor(dp);
        }
    }
    LeConnectorCallback leConnectorCallback = new LeConnectorCallback() {
        @Override
        public void onDeviceConnected() {
            receive_packet.clear();
            send_packet.clear();
            if (mPacketCallBack != null) {
                mPacketCallBack.onConnectStatusChanged(true);
            }
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
        public void onConnectionStateChange(int newState) {
            if(newState== BluetoothProfile.STATE_CONNECTED){
                profileServiceCallback.onConnected();
            }
            if(newState== BluetoothProfile.STATE_DISCONNECTED){
                profileServiceCallback.onDisconnected();
            }
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
