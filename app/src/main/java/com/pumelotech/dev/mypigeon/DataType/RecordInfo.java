package com.pumelotech.dev.mypigeon.DataType;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/6/27.
 */
public class RecordInfo implements Parcelable {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.PigeonID);
        dest.writeString(this.StartTime);
        dest.writeString(this.ArriveTime);
        dest.writeString(this.StartShedID);
        dest.writeString(this.ArriveShedID);
        dest.writeString(this.Status);
        dest.writeValue(this.DistanceMeter);
        dest.writeValue(this.ElapsedMinutes);
    }

    protected RecordInfo(Parcel in) {
        this.PigeonID = in.readString();
        this.StartTime = in.readString();
        this.ArriveTime = in.readString();
        this.StartShedID = in.readString();
        this.ArriveShedID = in.readString();
        this.Status = in.readString();
        this.DistanceMeter = (Integer) in.readValue(Integer.class.getClassLoader());
        this.ElapsedMinutes = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Parcelable.Creator<RecordInfo> CREATOR = new Parcelable.Creator<RecordInfo>() {
        @Override
        public RecordInfo createFromParcel(Parcel source) {
            return new RecordInfo(source);
        }

        @Override
        public RecordInfo[] newArray(int size) {
            return new RecordInfo[size];
        }
    };
}
