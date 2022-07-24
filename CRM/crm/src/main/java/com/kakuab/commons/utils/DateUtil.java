package com.kakuab.commons.utils;

import java.text.SimpleDateFormat;
import java.util.Date;


public class DateUtil {
    /**
     * 封装公共的、共享的、通用的日期实体类
     * @param date
     * @return
     */
    public static String formatDateTime(Date date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(date);
    }
}
