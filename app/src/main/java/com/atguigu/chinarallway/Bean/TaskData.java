package com.atguigu.chinarallway.Bean;

import java.sql.Date;

public class TaskData implements Cloneable {
    Date taskDate;
    String bName;
    String bID;
    String makeOrder;
    String makePosId;
    short pedID;
    String pos;
    boolean permit;

    public TaskData(){}

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public Date getTaskDate() {
        return taskDate;
    }

    public void setTaskDate(Date taskDate) {
        this.taskDate = taskDate;
    }

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

    public String getMakeOrder() {
        return makeOrder;
    }

    public void setMakeOrder(String makeOrder) {
        this.makeOrder = makeOrder;
    }

    public String getMakePosId() {
        return makePosId;
    }

    public void setMakePosId(String makePosId) {
        this.makePosId = makePosId;
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

    public boolean isPermit() {
        return permit;
    }

    public void setPermit(boolean permit) {
        this.permit = permit;
    }
}
