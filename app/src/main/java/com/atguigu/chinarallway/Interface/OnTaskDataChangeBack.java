package com.atguigu.chinarallway.Interface;

import com.prolificinteractive.materialcalendarview.CalendarDay;

public interface OnTaskDataChangeBack {
    void changeBackTaskData(String bName, String bId, int orderNum, String orderLocation, String storeNum, String storeLocation, CalendarDay date, int position);
}
