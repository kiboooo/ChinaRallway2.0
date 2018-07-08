package com.atguigu.chinarallway.Bean;

import java.math.BigDecimal;
import java.sql.Date;

public class TaskWithBeam{
    /*
    * Task
    * */
    Date taskDate;
    String bName;
    String bID;
    String makeOrder;
    String makePosId;
    short pedID;
    String pos;
    boolean permit;


    /*
    * Beam
    * */
    String size;
    BigDecimal slope;
    BigDecimal concrete;
    BigDecimal steel;
    BigDecimal intensity;
    int len;
    int height;
    BigDecimal lsl;
    BigDecimal lsr;
    BigDecimal lbl;
    BigDecimal lbr;
    BigDecimal rsl;
    BigDecimal rsr;
    BigDecimal rbl;
    BigDecimal rbr;
    BigDecimal lls;
    BigDecimal llb;
    BigDecimal lld;
    BigDecimal rls;
    BigDecimal rlb;
    BigDecimal rld;
    BigDecimal endSize;
    BigDecimal down;
    BigDecimal top;
    BigDecimal bottom;
    BigDecimal proDown;
    BigDecimal proTop;
    BigDecimal proBottom;
    BigDecimal scale;
    BigDecimal hoisting;
    BigDecimal guardRail;
    BigDecimal air;
    short keep;
    BigDecimal upWarp;
    String status;
    String qrCode;
    short casts;
    short stretch;
    short grouting;

    public TaskWithBeam(){}

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

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public BigDecimal getSlope() {
        return slope;
    }

    public void setSlope(BigDecimal slope) {
        this.slope = slope;
    }

    public BigDecimal getConcrete() {
        return concrete;
    }

    public void setConcrete(BigDecimal concrete) {
        this.concrete = concrete;
    }

    public BigDecimal getSteel() {
        return steel;
    }

    public void setSteel(BigDecimal steel) {
        this.steel = steel;
    }

    public BigDecimal getIntensity() {
        return intensity;
    }

    public void setIntensity(BigDecimal intensity) {
        this.intensity = intensity;
    }

    public int getLen() {
        return len;
    }

    public void setLen(int len) {
        this.len = len;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public BigDecimal getLsl() {
        return lsl;
    }

    public void setLsl(BigDecimal lsl) {
        this.lsl = lsl;
    }

    public BigDecimal getLsr() {
        return lsr;
    }

    public void setLsr(BigDecimal lsr) {
        this.lsr = lsr;
    }

    public BigDecimal getLbl() {
        return lbl;
    }

    public void setLbl(BigDecimal lbl) {
        this.lbl = lbl;
    }

    public BigDecimal getLbr() {
        return lbr;
    }

    public void setLbr(BigDecimal lbr) {
        this.lbr = lbr;
    }

    public BigDecimal getRsl() {
        return rsl;
    }

    public void setRsl(BigDecimal rsl) {
        this.rsl = rsl;
    }

    public BigDecimal getRsr() {
        return rsr;
    }

    public void setRsr(BigDecimal rsr) {
        this.rsr = rsr;
    }

    public BigDecimal getRbl() {
        return rbl;
    }

    public void setRbl(BigDecimal rbl) {
        this.rbl = rbl;
    }

    public BigDecimal getRbr() {
        return rbr;
    }

    public void setRbr(BigDecimal rbr) {
        this.rbr = rbr;
    }

    public BigDecimal getLls() {
        return lls;
    }

    public void setLls(BigDecimal lls) {
        this.lls = lls;
    }

    public BigDecimal getLlb() {
        return llb;
    }

    public void setLlb(BigDecimal llb) {
        this.llb = llb;
    }

    public BigDecimal getLld() {
        return lld;
    }

    public void setLld(BigDecimal lld) {
        this.lld = lld;
    }

    public BigDecimal getRls() {
        return rls;
    }

    public void setRls(BigDecimal rls) {
        this.rls = rls;
    }

    public BigDecimal getRlb() {
        return rlb;
    }

    public void setRlb(BigDecimal rlb) {
        this.rlb = rlb;
    }

    public BigDecimal getRld() {
        return rld;
    }

    public void setRld(BigDecimal rld) {
        this.rld = rld;
    }

    public BigDecimal getEndSize() {
        return endSize;
    }

    public void setEndSize(BigDecimal endSize) {
        this.endSize = endSize;
    }

    public BigDecimal getDown() {
        return down;
    }

    public void setDown(BigDecimal down) {
        this.down = down;
    }

    public BigDecimal getTop() {
        return top;
    }

    public void setTop(BigDecimal top) {
        this.top = top;
    }

    public BigDecimal getBottom() {
        return bottom;
    }

    public void setBottom(BigDecimal bottom) {
        this.bottom = bottom;
    }

    public BigDecimal getProDown() {
        return proDown;
    }

    public void setProDown(BigDecimal proDown) {
        this.proDown = proDown;
    }

    public BigDecimal getProTop() {
        return proTop;
    }

    public void setProTop(BigDecimal proTop) {
        this.proTop = proTop;
    }

    public BigDecimal getProBottom() {
        return proBottom;
    }

    public void setProBottom(BigDecimal proBottom) {
        this.proBottom = proBottom;
    }

    public BigDecimal getScale() {
        return scale;
    }

    public void setScale(BigDecimal scale) {
        this.scale = scale;
    }

    public BigDecimal getHoisting() {
        return hoisting;
    }

    public void setHoisting(BigDecimal hoisting) {
        this.hoisting = hoisting;
    }

    public BigDecimal getGuardRail() {
        return guardRail;
    }

    public void setGuardRail(BigDecimal guardRail) {
        this.guardRail = guardRail;
    }

    public BigDecimal getAir() {
        return air;
    }

    public void setAir(BigDecimal air) {
        this.air = air;
    }

    public short getKeep() {
        return keep;
    }

    public void setKeep(short keep) {
        this.keep = keep;
    }

    public BigDecimal getUpWarp() {
        return upWarp;
    }

    public void setUpWarp(BigDecimal upWarp) {
        this.upWarp = upWarp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public short getCasts() {
        return casts;
    }

    public void setCasts(short casts) {
        this.casts = casts;
    }

    public short getStretch() {
        return stretch;
    }

    public void setStretch(short stretch) {
        this.stretch = stretch;
    }

    public short getGrouting() {
        return grouting;
    }

    public void setGrouting(short grouting) {
        this.grouting = grouting;
    }
}
