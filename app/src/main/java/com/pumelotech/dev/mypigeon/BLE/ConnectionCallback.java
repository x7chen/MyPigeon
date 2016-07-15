package com.pumelotech.dev.mypigeon.BLE;

/**
 * Created by x7che on 2016/7/14.
 */
public interface ConnectionCallback {
    void onConnectionStateChange(int newState);
    void onError(String message, int errorCode);
    void onDeviceNotSupported();
}
