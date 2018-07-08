package com.atguigu.chinarallway.Bean;

import java.io.Serializable;
import java.sql.Date;

public class BuildPlan implements Serializable{
    Integer buildID;
    String bID;
    String bName;
    String dir;
    short seq;
    Date bFromDate;
    Date bToDate;
    String side;

    public BuildPlan(){}

    public int getBuildID() {
        return buildID;
    }

    public void setBuildID(int buildID) {
        this.buildID = buildID;
    }

    public String getbID() {
        return bID;
    }

    public void setbID(String bID) {
        this.bID = bID;
    }

    public String getbName() {
        return bName;
    }

    public void setbName(String bName) {
        this.bName = bName;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public short getSeq() {
        return seq;
    }

    public void setSeq(short seq) {
        this.seq = seq;
    }

    public Date getbFromDate() {
        return bFromDate;
    }

    public void setbFromDate(Date bFromDate) {
        this.bFromDate = bFromDate;
    }

    public Date getbToDate() {
        return bToDate;
    }

    public void setbToDate(Date bToDate) {
        this.bToDate = bToDate;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }


}
