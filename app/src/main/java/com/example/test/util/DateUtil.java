package com.example.test.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil
{
    public static String getNowTime()
    {
        SimpleDateFormat sdf=new SimpleDateFormat("MM.dd");
        return sdf.format(new Date());
    }
}
