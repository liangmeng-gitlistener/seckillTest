package org.seckill.utils;

import org.seckill.enums.SysUtil;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

/**
 * @program: seckill
 * @description:
 * @author: lm
 * @create: 2018-11-20 16:52
 **/
@Component
public final class MD5Util {
    public String getMD5 (long key){
        String base = key + "/" + SysUtil.SLAT.toString();
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }
}