package com.pumelotech.dev.mypigeon.DataType;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by x7che on 2016/6/26.
 */
public class PigeonInfo implements Parcelable {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.Name);
        dest.writeString(this.ID);
        dest.writeString(this.BirthDate);
        dest.writeString(this.OwnerID);
        dest.writeString(this.ShedID);
        dest.writeString(this.Status);
        dest.writeValue(this.TotalDistance);
        dest.writeValue(this.TotalMinutes);
        dest.writeValue(this.FlyTimes);
    }

    protected PigeonInfo(Parcel in) {
        this.Name = in.readString();
        this.ID = in.readString();
        this.BirthDate = in.readString();
        this.OwnerID = in.readString();
        this.ShedID = in.readString();
        this.Status = in.readString();
        this.TotalDistance = (Integer) in.readValue(Integer.class.getClassLoader());
        this.TotalMinutes = (Integer) in.readValue(Integer.class.getClassLoader());
        this.FlyTimes = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Parcelable.Creator<PigeonInfo> CREATOR = new Parcelable.Creator<PigeonInfo>() {
        @Override
        public PigeonInfo createFromParcel(Parcel source) {
            return new PigeonInfo(source);
        }

        @Override
        public PigeonInfo[] newArray(int size) {
            return new PigeonInfo[size];
        }
    };
}
