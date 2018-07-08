package com.atguigu.chinarallway.Bean

import java.sql.Date
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Kiboooo on 2017/10/14.
 */
class GetWeekStartAndEnd {
    private fun getDayDistanceMonDay(calendar: Calendar):Int{
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        return if(dayOfWeek==1)
            -6
        else
            2-dayOfWeek
    }

     fun getWeekStartAndEnd():String{
         val simpleDataFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)
        val date = java.util.Date(System.currentTimeMillis())
        val calender = Calendar.getInstance()
        calender.time = date
        calender.firstDayOfWeek = Calendar.MONDAY
        calender.add(Calendar.DAY_OF_WEEK,7)
        val distance = getDayDistanceMonDay(calender)
        calender.add(Calendar.DAY_OF_WEEK,distance)
        val monday = calender.time
        calender.add(Calendar.DAY_OF_WEEK,6)
        val sunday = calender.time
        return "${simpleDataFormatter.format(Date((monday.time/100000)*100000))}_${simpleDataFormatter.format(Date((sunday.time/100000)*100000))}"
    }
}