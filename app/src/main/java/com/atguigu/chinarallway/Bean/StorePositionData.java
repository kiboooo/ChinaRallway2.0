package com.atguigu.chinarallway.Bean;

public class StorePositionData {
    short pedID;
    String pos;
    short size;
    String status;

    public StorePositionData(){}

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

    public short getSize() {
        return size;
    }

    public void setSize(short size) {
        this.size = size;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
