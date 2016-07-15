package com.pumelotech.dev.mypigeon.BLE;

/**
 * Created by Administrator on 2016/7/15.
 */
public interface BleProfileCallback {
    void onSending();
    void onInitialized();
    void onReceived(byte[] data);
}
