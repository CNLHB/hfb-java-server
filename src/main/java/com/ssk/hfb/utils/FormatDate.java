package com.ssk.hfb.utils;


import com.ssk.hfb.common.pojo.UserInfo;

import java.util.Calendar;

/**
 * 使用线程上下文在线程内共享用户信息
 */
public class FormatDate {
    public static String endTime1( ){
        Calendar now = Calendar.getInstance();
        int day = now.get(Calendar.DAY_OF_MONTH); //  System.out.println("日: " + now.get(Calendar.DAY_OF_MONTH));
        int month =(now.get(Calendar.MONTH) + 1);//  System.out.println("月: " + now.get(Calendar.MONTH));
        int year = now.get(Calendar.YEAR); //  System.out.println("年: " + now.get(Calendar.DAY_OF_MONTH));
        String endTime = String.valueOf(year)+"-"+String.valueOf(month)+"-"+String.valueOf(day);
        return endTime;
    }
    public static String endTime( ){
        Calendar now = Calendar.getInstance();
        int day = now.get(Calendar.DAY_OF_MONTH); //  System.out.println("日: " + now.get(Calendar.DAY_OF_MONTH));
        int month =(now.get(Calendar.MONTH) + 1);//  System.out.println("月: " + now.get(Calendar.MONTH));
        int year = now.get(Calendar.YEAR)+1; //  System.out.println("年: " + now.get(Calendar.DAY_OF_MONTH));
        String endTime = String.valueOf(year)+"-"+String.valueOf(month)+"-"+String.valueOf(day);
        return endTime;
    }
    public static String startTime(int y,int m,int d ){
        Calendar now = Calendar.getInstance();
        int day = now.get(Calendar.DAY_OF_MONTH); //  System.out.println("日: " + now.get(Calendar.DAY_OF_MONTH));
        int month =(now.get(Calendar.MONTH) + 1);//  System.out.println("月: " + now.get(Calendar.MONTH));
        int year = now.get(Calendar.YEAR); //  System.out.println("年: " + now.get(Calendar.DAY_OF_MONTH));
        year = year - y;
        if (day - d<0){
            month = month - 1;
            day=30+day-d;
        }else {
            day = day- d;
        }
        if (month - m<0){
            year = year - 1;
            month=12+month-m;
        }else {
            month = month - m;
        }
        String startTime = String.valueOf(year)+"-"+String.valueOf(month)+"-"+String.valueOf(day);
        return startTime;
    }

}
