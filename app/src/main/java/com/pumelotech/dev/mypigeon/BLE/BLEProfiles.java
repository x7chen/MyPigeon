package com.pumelotech.dev.mypigeon.BLE;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.util.Log;

import com.pumelotech.dev.mypigeon.MyApplication;

import java.util.List;
import java.util.UUID;

/**
 * Created by x7che on 2016/7/14.
 */
public class BleProfiles implements TransferCallback {
    private final static String TAG = MyApplication.DebugTag;
    public static final UUID LE_UART_SERVICE_UUID = UUID.fromString("0000ffe0-0000-1000-8000-00805f9b34fb");
    public static final UUID LE_UART_CHAR_UUID = UUID.fromString("0000ffe1-0000-1000-8000-00805f9b34fb");
    private BluetoothGattCharacteristic workCharacteristic;
    LeConnector leConnector;
    BleProfileCallback mCallback;
    static BleProfiles mBleProfiles;

    public BleProfiles(BleProfileCallback callback) {
        mCallback = callback;
        leConnector = MyApplication.mLeConnector;

    }

    public static BleProfiles getInstance(BleProfileCallback callback) {
        if (mBleProfiles == null) {
            mBleProfiles = new BleProfiles(callback);
        }
        return mBleProfiles;
    }

    public static BleProfiles getInstance() {
        return mBleProfiles;
    }

    public void writeWorkCharacteristic(byte[] data) {
        if (workCharacteristic == null) {
            getWorkCharacteristic();
            Log.i(TAG, "getWorkCharacteristic()");
        } else {
            workCharacteristic.setValue(data);
            leConnector.getBluetoothGatt().writeCharacteristic(workCharacteristic);
            Log.i(TAG, "workCharacteristic is not null");
        }
    }

    void getWorkCharacteristic() {
        if (leConnector != null) {
            leConnector.setTransferCallback(this);
            if (leConnector.getmConnectionState() == LeConnector.STATE_CONNECTED) {
                List<BluetoothGattService> services = leConnector.getServices();
                if (services == null || services.isEmpty()) {
                    return;
                }
                for (BluetoothGattService service : services) {
                    List<BluetoothGattCharacteristic> characteristics = service.getCharacteristics();
                    for (BluetoothGattCharacteristic characteristic : characteristics) {
                        if (characteristic.getUuid().equals(LE_UART_CHAR_UUID)) {
                            workCharacteristic = characteristic;
                            leConnector.getBluetoothGatt().setCharacteristicNotification(workCharacteristic, true);
                            mCallback.onInitialized();
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onCharacteristicWrite(BluetoothGattCharacteristic characteristic) {

    }

    @Override
    public void onCharacteristicRead(BluetoothGattCharacteristic characteristic) {
        if (workCharacteristic == null) {
            getWorkCharacteristic();
        }
        if (characteristic.equals(workCharacteristic)) {
            mCallback.onReceived(workCharacteristic.getValue());
        }
    }

    @Override
    public void onCharacteristicChanged(BluetoothGattCharacteristic characteristic) {
        if (workCharacteristic == null) {
            getWorkCharacteristic();
        }
        if (characteristic.equals(workCharacteristic)) {
            mCallback.onReceived(workCharacteristic.getValue());
        }
    }
}
