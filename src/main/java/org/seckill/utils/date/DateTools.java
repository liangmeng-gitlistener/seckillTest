package org.seckill.utils.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @program: seckill
 * @description:
 * @author: lm
 * @create: 2018-11-20 15:59
 **/
public class DateTools {
    public static final long HOUR_MILLS = 1000*60*60;

    public synchronized static int daysSub(Date end, Date begin){
        //加8小时，是因为初始化的计时时间是1970-01-01 08:00:00,改成从0点计时，相除得到已经过去的天数
        long endDays = (end.getTime()+8*HOUR_MILLS)/(24*HOUR_MILLS);
        long startDays = (begin.getTime()+8*HOUR_MILLS)/(24*HOUR_MILLS);
        return (int) (endDays-startDays);
    }

    public static Date getDate(DateFormats format,String date) throws ParseException {
        return getFormatter(format).parse(date);
    }

    public static String format(DateFormats format,Date date){
        return getFormatter(format).format(date);
    }

    private static SimpleDateFormat getFormatter(DateFormats format){
        return new SimpleDateFormat(format.toString());
    }

    public static String parseLongToStr(long datelong){
        Date date = new Date(datelong);
        SimpleDateFormat sd = new SimpleDateFormat("yyyy年MM月dd日");
        return sd.format(date);
    }
}