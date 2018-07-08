package com.atguigu.chinarallway.Bean;

public class MakePosition {
    String makePosID;
    short size;
    boolean idle;

    public MakePosition(){}

    public String getMakePosID() {
        return makePosID;
    }

    public void setMakePosID(String makePosID) {
        this.makePosID = makePosID;
    }

    public short getSize() {
        return size;
    }

    public void setSize(short size) {
        this.size = size;
    }

    public boolean isIdle() {
        return idle;
    }

    public void setIdle(boolean idle) {
        this.idle = idle;
    }
}
