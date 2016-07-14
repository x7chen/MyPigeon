package com.pumelotech.dev.mypigeon.DataType;

/**
 * Created by x7che on 2016/6/26.
 */
public class PigeonInfo {
    public String Name;
    public String ID;
    public String BirthDate;
    public String OwnerID;
    public String ShedID;
    public String Status;
    public Integer TotalDistance;
    public Integer TotalMinutes;
    public Integer FlyTimes;

    public PigeonInfo() {
        Name = "无名";
        this.ID = "错误";
        BirthDate = "未知";
        OwnerID = "未知";
        ShedID = "未知";
        Status = "未知";
        TotalDistance = 0;
        TotalMinutes = 0;
        FlyTimes = 0;
    }
}
