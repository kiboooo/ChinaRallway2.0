package com.atguigu.chinarallway.Bean;

import java.math.BigDecimal;

public class CheckRecData {
    private String bName ;
    private String bID ;
    private BigDecimal rLen ;
    private BigDecimal rDown ;
    private BigDecimal rTop;
    private BigDecimal rBottom ;
    private BigDecimal rHeight;
    private BigDecimal proDown;
    private BigDecimal proTop ;
    private BigDecimal proBottom;
    private BigDecimal smooth;
    private BigDecimal upWard;
    private BigDecimal lls;
    private BigDecimal llb;
    private BigDecimal lld;
    private BigDecimal rls;
    private BigDecimal rlb;
    private  BigDecimal rld;
    private BigDecimal proScale;
    private BigDecimal strength;
    private  BigDecimal downRatio;
    private BigDecimal bottomRatio;
    private BigDecimal topRation;

    public CheckRecData(){
//        setbName(null);
//        setbID(null);
//        setProScale(null);
//        setProDown(null);
//        setProTop(null);
//        setBottomRatio(null);
//        setProBottom(null);
//        setDownRatio(null);
//        setLlb(null);
//        setLld(null);
//        setLls(null);
//        setrBottom(null);
//        setrDown(null);
//        setrHeight(null);
//        setRlb(null);
//        setRld(null);
//        setrLen(null);
//        setRls(null);
//        setSmooth(null);
//        setrTop(null);
//        setStrength(null);
//        setUpWard(null);
//        setTopRation(null);
    }

    public BigDecimal getrDown() {
        return rDown;
    }

    public void setrDown(BigDecimal rDown) {
        this.rDown = rDown;
    }

    public BigDecimal getProBottom() {
        return proBottom;
    }

    public void setProBottom(BigDecimal proBottom) {
        this.proBottom = proBottom;
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

    public BigDecimal getrLen() {
        return rLen;
    }

    public void setrLen(BigDecimal rLen) {
        this.rLen = rLen;
    }

    public BigDecimal getrTop() {
        return rTop;
    }

    public void setrTop(BigDecimal rTop) {
        this.rTop = rTop;
    }

    public BigDecimal getrBottom() {
        return rBottom;
    }

    public void setrBottom(BigDecimal rBottom) {
        this.rBottom = rBottom;
    }

    public BigDecimal getrHeight() {
        return rHeight;
    }

    public void setrHeight(BigDecimal rHeight) {
        this.rHeight = rHeight;
    }

    public BigDecimal getProTop() {
        return proTop;
    }

    public void setProTop(BigDecimal proTop) {
        this.proTop = proTop;
    }

    public BigDecimal getProDown() {
        return proDown;
    }

    public void setProDown(BigDecimal proDown) {
        this.proDown = proDown;
    }

    public BigDecimal getSmooth() {
        return smooth;
    }

    public void setSmooth(BigDecimal smooth) {
        this.smooth = smooth;
    }

    public BigDecimal getUpWard() {
        if (upWard==null)
            upWard = new BigDecimal(0);
        return upWard;
    }

    public void setUpWard(BigDecimal upWard) {
        this.upWard = upWard;
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

    public BigDecimal getProScale() {
        if (proScale==null)
            proScale = new BigDecimal(0);
        return proScale;
    }

    public void setProScale(BigDecimal proScale) {
        this.proScale = proScale;
    }

    public BigDecimal getStrength() {
        return strength;
    }

    public void setStrength(BigDecimal strength) {
        this.strength = strength;
    }

    public BigDecimal getDownRatio() {
        if (downRatio==null)
            downRatio = new BigDecimal(0);
        return downRatio;
    }

    public void setDownRatio(BigDecimal downRatio) {
        this.downRatio = downRatio;
    }

    public BigDecimal getBottomRatio() {
        if (bottomRatio==null)
            bottomRatio = new BigDecimal(0);
        return bottomRatio;
    }

    public void setBottomRatio(BigDecimal bottomRatio) {
        this.bottomRatio = bottomRatio;
    }

    public BigDecimal getTopRation() {
        if (topRation==null)
            topRation = new BigDecimal(0);
        return topRation;
    }

    public void setTopRation(BigDecimal topRation) {
        this.topRation = topRation;
    }
}
