package org.seckill.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: seckill
 * @description:
 * @author: lm
 * @create: 2018-11-20 16:59
 **/
public enum SysUtil {
    // MD5盐值字符串，混淆MD5
    SLAT("sdhshkjdsfnjk!@#$@$#@53153_%$#"),
    ENCODING_UTF8("utf-8"),
    // API 接口根路径
    API_BASE("/api"),
    // 处理成功
    SUCCESS_MESSAGE("操作成功"),
    // 处理失败
    ERROR_TITLE("操作失败：");

    public static final String API = "/api";

    private String sysUtil;

    SysUtil(String sysUtil) {
        this.sysUtil = sysUtil;
    }
    public String getText() {
        return this.sysUtil;
    }
    @Override
    public String toString(){
        return this.sysUtil;
    }

    // Implementing a fromString method on an enum type
    private static final Map<String, SysUtil> stringToEnum = new HashMap<String, SysUtil>();

    static {
        // Initialize map from constant name to enum constant
        for(SysUtil sysUtil : values()) {
            stringToEnum.put(sysUtil.toString(), sysUtil);
        }
    }

    // Returns Blah for string, or null if string is invalid
    public static SysUtil fromString(String symbol) {
        return stringToEnum.get(symbol);
    }
}
