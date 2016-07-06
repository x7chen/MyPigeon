/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pumelotech.dev.mypigeon.BLE;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.pumelotech.dev.mypigeon.MainActivity;
import com.pumelotech.dev.mypigeon.R;

import java.util.List;
import java.util.UUID;

/**
 * Service for managing connection and data communication with a GATT server hosted on a
 * given Bluetooth LE device.
 */
public class LeConnector {
    public static final String TAG = MainActivity.DebugTag;
    private BluetoothGatt mBluetoothGatt;
    private BluetoothDevice mDevice;
    private int mConnectionState = STATE_DISCONNECTED;

    private static final int STATE_DISCONNECTED = 0;
    private static final int STATE_CONNECTING = 1;
    private static final int STATE_CONNECTED = 2;

    public static final UUID LE_UART_SERVICE_UUID = UUID.fromString("0000ffe0-0000-1000-8000-00805f9b34fb");
    public static final UUID LE_UART_CHAR_UUID = UUID.fromString("6e400002-b5a3-f393-e0a9-e50e24dcca9e");
    public static final String CLIENT_CHARACTERISTIC_CONFIG = "00002902-0000-1000-8000-00805f9b34fb";

    public final static String ERROR_DISCOVERY_SERVICE = "Error on discovering services";
    public final static String ERROR_WRITE_CHARACTERISTIC = "Error on writing characteristic";
    public final static String ERROR_WRITE_DESCRIPTOR = "Error on writing descriptor";

    private BluetoothAdapter mBluetoothAdapter;
    private LeConnectorCallBacks mCallbacks;
    private boolean isNUSServiceFound = false;
    static private LeConnector mLeConnector;
    static private Context mContext;
    private BluetoothGattCharacteristic leUartCharacteristic;

    // Implements callback methods for GATT events that the app cares about.  For example,
    // connection change and services discovered.
    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                mConnectionState = STATE_CONNECTED;
                mCallbacks.onDeviceConnected();
                Log.i(TAG, "Connected to GATT server.");
                // Attempts to discover services after successful connection.
                Log.i(TAG, "Attempting to start service discovery:" +
                        mBluetoothGatt.discoverServices());

            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                mConnectionState = STATE_DISCONNECTED;
                mCallbacks.onDeviceDisconnected();
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            isNUSServiceFound = false;
            if (status == BluetoothGatt.GATT_SUCCESS) {
                List<BluetoothGattService> services = gatt.getServices();
                for (BluetoothGattService service : services) {
                    if (service.getUuid().equals(LE_UART_SERVICE_UUID)) {
                        isNUSServiceFound = true;
                        leUartCharacteristic = service.getCharacteristic(LE_UART_CHAR_UUID);
                        enableNotification();
                        mCallbacks.onInitialized();
                    }
                }
                if (isNUSServiceFound) {
                    mCallbacks.onServiceFound();
                } else {
                    mCallbacks.onDeviceNotSupported();
                    gatt.disconnect();
                }
            } else {
                mCallbacks.onError(ERROR_DISCOVERY_SERVICE, status);
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt,
                                         BluetoothGattCharacteristic characteristic,
                                         int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                mCallbacks.onReceived(characteristic.getValue());
            }
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            mCallbacks.onSending();
            super.onCharacteristicWrite(gatt, characteristic, status);
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt,
                                            BluetoothGattCharacteristic characteristic) {
            mCallbacks.onReceived(characteristic.getValue());
        }
    };

    public void registerCallbacks(LeConnectorCallBacks callBacks) {
        mCallbacks = callBacks;
    }


    public interface LeConnectorCallBacks {
        void onDeviceConnected();

        void onDeviceDisconnected();

        void onServiceFound();

        void onReceived(byte[] data);

        void onInitialized();

        void onSending();

        void onError(String message, int errorCode);

        void onDeviceNotSupported();
    }

    public BluetoothDevice getDevice() {
        return mDevice;
    }

    public void connect(Context context, BluetoothDevice device) {
        if (device == null) {
            return;
        }
        mDevice = device;
        mBluetoothGatt = mDevice.connectGatt(context, false, mGattCallback);
    }

    public void disconnect() {
        Log.d(TAG, "Disconnecting device");
        if (mBluetoothGatt != null) {
            mBluetoothGatt.disconnect();
        }
    }

    /**
     * After using a given BLE device, the app must call this method to ensure resources are
     * released properly.
     */
    public void close() {
        if (mBluetoothGatt == null) {
            return;
        }
        mBluetoothGatt.close();
        mBluetoothGatt = null;
    }

    public void send(byte[] data) {
        if (leUartCharacteristic == null) {
            mCallbacks.onError(ERROR_WRITE_CHARACTERISTIC, 0);
            return;
        }
        leUartCharacteristic.setValue(data);
        mBluetoothGatt.writeCharacteristic(leUartCharacteristic);
    }

    public void enableNotification() {
        if (mBluetoothGatt == null) {
            return;
        }
        mBluetoothGatt.setCharacteristicNotification(leUartCharacteristic, true);

        List<BluetoothGattDescriptor> descriptors = leUartCharacteristic.getDescriptors();
        for (BluetoothGattDescriptor dp : descriptors) {
            dp.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            mBluetoothGatt.writeDescriptor(dp);
        }
    }

    public static LeConnector getInstance(Context context) {
        if (mContext == null) {
            mContext = context;
        }
        return getInstance();
    }

    public static LeConnector getInstance() {
        if (mContext == null) {
            return null;
        }
        if (mLeConnector == null) {
            mLeConnector = new LeConnector();
        }
        return mLeConnector;
    }


    public LeConnector() {
        // Initializes a Bluetooth adapter.  For API level 18 and above, get a reference to
        // BluetoothAdapter through BluetoothManager.
        final BluetoothManager bluetoothManager =
                (BluetoothManager) mContext.getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        // Checks if Bluetooth is supported on the device.
        if (mBluetoothAdapter == null) {
            Toast.makeText(mContext, R.string.error_bluetooth_not_supported, Toast.LENGTH_SHORT).show();
            return;
        }
        mBluetoothAdapter.startLeScan(new BluetoothAdapter.LeScanCallback() {
            @Override
            public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
                String deviceName = device.getName();
                if (deviceName == null) {
                    deviceName = LeAdvertiseParser.parseAdertisedData(scanRecord).getName();
                }
                if (deviceName != null) {
                    if (device.getName().equals("BT05")) {
                        connect(mContext, device);

                    }
                }
                connect(mContext, device);
                Log.d(MainActivity.DebugTag, "NAME:" + deviceName + "RSSI:" + rssi);
            }
        });
        Log.d(MainActivity.DebugTag, "Start Scan");
    }
}
