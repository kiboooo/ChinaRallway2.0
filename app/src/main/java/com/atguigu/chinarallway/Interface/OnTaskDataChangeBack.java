package com.atguigu.chinarallway.Interface;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.sql.Date;

public interface OnTaskDataChangeBack {
    void changeBackTaskData(int orderNum, String orderLocation, String storeNum, String storeLocation, CalendarDay date);
}
