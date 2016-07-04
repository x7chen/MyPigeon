package com.pumelotech.dev.mypigeon;

/**
 * Created by x7che on 2016/6/26.
 */
public class PigeonInfo {
    public String Name;
    public String ID;
    public String OwnerID;
    public String ShedID;
    public String Status;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getOwnerID() {
        return OwnerID;
    }

    public void setOwnerID(String ownerID) {
        OwnerID = ownerID;
    }

    public String getShedID() {
        return ShedID;
    }

    public void setShedID(String shedID) {
        ShedID = shedID;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}
