package com.pumelotech.dev.mypigeon.BLE;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;

import java.util.List;
import java.util.UUID;

/**
 * Created by x7che on 2016/7/14.
 */
public class BleProfiles implements TransferCallback {
    public static final UUID LE_UART_SERVICE_UUID = UUID.fromString("0000ffe0-0000-1000-8000-00805f9b34fb");
    public static final UUID LE_UART_CHAR_UUID = UUID.fromString("0000ffe1-0000-1000-8000-00805f9b34fb");
    private BluetoothGattCharacteristic workCharacteristic;
    LeConnector leConnector;
    TransferCallback transferCallback;
    BleProfileCallback mCallback;
    static BleProfiles mBleProfiles;
    public BleProfiles(BleProfileCallback callback) {
        mCallback = callback;
        leConnector = LeConnector.getInstance();
        if (leConnector != null) {
            leConnector.setTransferCallback(this);
            List<BluetoothGattService> services = leConnector.getServices();
            for (BluetoothGattService service : services) {
                List<BluetoothGattCharacteristic> characteristics = service.getCharacteristics();
                for (BluetoothGattCharacteristic characteristic : characteristics) {
                    if (characteristic.getUuid().equals(LE_UART_CHAR_UUID)) {
                        workCharacteristic = characteristic;
                        leConnector.getBluetoothGatt().setCharacteristicNotification(workCharacteristic, true);
                    }
                }
            }
        }
    }
    public static BleProfiles getInstance(BleProfileCallback callback){
        if(mBleProfiles == null){
          mBleProfiles = new BleProfiles(callback);
        }
        return mBleProfiles;
    }
    public static BleProfiles getInstance(){
        return mBleProfiles;
    }
    public void writeWorkCharacteristic(byte[] data) {
        if (workCharacteristic != null) {
            workCharacteristic.setValue(data);
            leConnector.getBluetoothGatt().writeCharacteristic(workCharacteristic);
        }
    }

    @Override
    public void onCharacteristicWrite(BluetoothGattCharacteristic characteristic) {

    }

    @Override
    public void onCharacteristicRead(BluetoothGattCharacteristic characteristic) {

    }

    @Override
    public void onCharacteristicChanged(BluetoothGattCharacteristic characteristic) {
        if(characteristic.equals(workCharacteristic)){
            mCallback.onReceived(workCharacteristic.getValue());
        }
    }
}
