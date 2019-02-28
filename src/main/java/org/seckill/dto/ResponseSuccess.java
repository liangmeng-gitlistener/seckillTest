package org.seckill.dto;

import java.util.List;

/**
 * @program: seckill
 * @description:
 * @author: lm
 * @create: 2018-11-30 10:16
 **/
public class ResponseSuccess<T> extends ResponseData<T> {
    public ResponseSuccess() {
        this.success = true;
    }
    public ResponseSuccess(String successInfo) {
        this.success = true;
        this.info = successInfo;
    }

    public ResponseSuccess(String successInfo, T result) {
        this.success = true;
        this.info = successInfo;
        this.result = result;
        if(result instanceof List) {
            this.totalCount = Long.valueOf(((List) result).size());
        }
    }
    public ResponseSuccess(String successInfo, T result, Long totalCount) {
        this.success = true;
        this.info = successInfo;
        this.result = result;
        this.totalCount = totalCount;
    }
}