package com.atguigu.chinarallway.Bean;

import java.math.BigDecimal;
import java.sql.Date;

public class MonthData {
    Date sDate;
    BigDecimal concrete;

    public MonthData(){}

    public Date getsDate() {
        return sDate;
    }

    public void setsDate(Date sDate) {
        this.sDate = sDate;
    }

    public BigDecimal getConcrete() {
        return concrete;
    }

    public void setConcrete(BigDecimal concrete) {
        this.concrete = concrete;
    }
}
