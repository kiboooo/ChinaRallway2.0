package com.atguigu.chinarallway.Bean;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class StoreData {
    String bName;
    String bID;
    short pedID;
    String pos;

    /*
    * inTime原始类型为 datetime
    * outTime原始类型为 datetime
    * */
    String inTime;
    String outTime;

    public StoreData(){}

    public String getbName() {
        return bName;
    }

    public void setbName(String bName) {
        this.bName = bName;
    }

    public String getbID() {
        return bID;
    }

    public void setbID(String bID) {
        this.bID = bID;
    }

    public short getPedID() {
        return pedID;
    }

    public void setPedID(short pedID) {
        this.pedID = pedID;
    }

    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }

    public String getInTime() {
        return inTime.substring(0,inTime.indexOf("."));
    }

    public void setInTime(String inTime) {
        this.inTime = inTime;
    }

    public String getOutTime() {
        if (outTime==null)
            return getNowDate(System.currentTimeMillis());
        else {
        Log.e("getOutTime", outTime.substring(0, outTime.indexOf(".")) + " " + outTime);
            return outTime.substring(0, outTime.indexOf("."));
        }
    }

    public void setOutTime(String outTime) {
        this.outTime = outTime;
    }

    private String getNowDate(long nowTime) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss", Locale.CHINA);
        return simpleDateFormat.format(new Date(nowTime));
    }
}
