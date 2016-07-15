package com.pumelotech.dev.mypigeon.BLE;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;

/**
 * Created by x7che on 2016/7/15.
 */
public interface TransferCallback {
    void onCharacteristicWrite(BluetoothGattCharacteristic characteristic);
    void onCharacteristicRead(BluetoothGattCharacteristic characteristic);
    void onCharacteristicChanged(BluetoothGattCharacteristic characteristic);
}
