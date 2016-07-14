package com.pumelotech.dev.mypigeon.DataType;

/**
 * Created by Administrator on 2016/6/27.
 */
public class RecordInfo {
    public String PigeonID;
    public String StartTime;
    public String ArriveTime;
    public String StartShedID;
    public String ArriveShedID;
    public String Status;
    public Integer DistanceMeter;
    public Integer ElapsedMinutes;

    public RecordInfo() {
        PigeonID = "--";
        StartTime = "--";
        ArriveTime = "--";
        StartShedID = "--";
        ArriveShedID = "--";
        Status = "--";
        DistanceMeter = 0;
        ElapsedMinutes = 0;
    }
}
