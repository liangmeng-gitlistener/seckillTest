package org.seckill.utils.date;

/**
 * @program: seckill
 * @description:
 * @author: lm
 * @create: 2018-11-20 15:52
 **/
public enum DateFormats {
    DEFAULT("yyyy-MM-dd HH:mm:ss"),
    YMDHM("yyyy-MM-dd HH:mm"),
    MD_CN("M月d日"),
    YMD_NUM("yyyyMMdd"),
    YMDHMS_NUM("yyyyMMddHHmmss");

    private String formate;

    DateFormats(String formate) {
        this.formate = formate;
    }
    public String toString(){
        return this.formate;
    }
}
