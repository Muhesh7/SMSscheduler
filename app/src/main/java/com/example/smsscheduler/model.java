package com.example.smsscheduler;

public class model {

    String number;
    String msg;
    long  time;

    public model() {
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
