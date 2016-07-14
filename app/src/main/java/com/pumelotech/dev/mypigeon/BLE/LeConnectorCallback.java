package com.pumelotech.dev.mypigeon.BLE;

/**
 * Created by x7che on 2016/7/14.
 */
public interface LeConnectorCallback {
    void onConnectionStateChange(int newState);

    void onReceived(byte[] data);

    void onSending();

    void onError(String message, int errorCode);

    void onDeviceNotSupported();
}
