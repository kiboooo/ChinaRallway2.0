package com.atguigu.chinarallway.Bean;

import android.support.annotation.NonNull;

/**
 * Created by Kiboooo on 2017/9/19.
 * 库存在图表中显示的数据基础
 *
 */

public class StoreChartData implements Comparable<StoreChartData> {
    private Integer XDay = 0;
    private Integer YAmount =0;

    public void setXDay(int XDay) {
        this.XDay = XDay;
    }

    public void setYAmount(int YAmount) {
        this.YAmount = YAmount;
    }

    public Integer getXDay() {
        return XDay;
    }

    public Integer getYAmount() {
        return YAmount;
    }

    @Override
    public int compareTo(@NonNull StoreChartData storeChartData) {
        return this.getXDay().compareTo(storeChartData.getXDay());
    }

}
