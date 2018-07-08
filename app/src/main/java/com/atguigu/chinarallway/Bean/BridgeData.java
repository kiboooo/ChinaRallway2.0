package com.atguigu.chinarallway.Bean;

public class BridgeData {
    String bName;
    String fromToNum;
    String memo;

    public BridgeData(){}

    public String getbName() {
        return bName;
    }

    public void setbName(String bName) {
        this.bName = bName;
    }

    public String getFromToNum() {
        return fromToNum;
    }

    public void setFromToNum(String fromToNum) {
        this.fromToNum = fromToNum;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public BridgeData(String bName, String fromToNum, String memo) {
        this.bName = bName;
        this.fromToNum = fromToNum;
        this.memo = memo;
    }
}
