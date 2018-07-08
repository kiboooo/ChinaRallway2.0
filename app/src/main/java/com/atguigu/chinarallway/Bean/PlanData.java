package com.atguigu.chinarallway.Bean;

/**
 * Created by 陈雨田 on 2017/10/12.
 */

public class PlanData {

    private int seq;
    private String bName;
    private String side;
    private int bID;



    public  PlanData(int seq,String bName,String side,int bID){
        this.seq = seq;
        this.bName = bName;
        this.side = side;
        this.bID = bID;
    }


    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public String getbName() {
        return bName;
    }

    public void setbName(String bName) {
        this.bName = bName;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public int getbID() {
        return bID;
    }

    public void setbID(int bID) {
        this.bID = bID;
    }

    @Override
    public String toString() {
        return "PlanData{" +
                "seq=" + seq +
                ", bName='" + bName + '\'' +
                ", side='" + side + '\'' +
                ", bID=" + bID +
                '}';
    }
}
