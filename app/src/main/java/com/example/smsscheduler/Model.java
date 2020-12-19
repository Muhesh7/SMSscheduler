package com.example.smsscheduler;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "content_table")
public class Model {
    @ColumnInfo(name = "number")
    String number;
    @ColumnInfo(name = "content")
    String msg;
    @ColumnInfo(name = "time string")
    String timestring;
    @ColumnInfo(name = "time")
    long  time;
    @ColumnInfo(name = "status")
    String status;
    @PrimaryKey(autoGenerate = false)
    public int key;

    public String getTimestring() {
        return timestring;
    }

    public void setTimestring(String timestring) {
        this.timestring = timestring;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public Model() {
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getNumber() {
        return number;
    }

    public String getMsg() {
        return msg;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
