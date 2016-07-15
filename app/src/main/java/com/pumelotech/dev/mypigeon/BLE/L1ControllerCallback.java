package com.pumelotech.dev.mypigeon.BLE;

/**
 * Created by Administrator on 2016/7/15.
 */
public interface L1ControllerCallback {
    void onTimeOut();
    void onResolved(int command,int key,byte[] data,int length);
}
