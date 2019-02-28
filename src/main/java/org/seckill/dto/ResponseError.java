package org.seckill.dto;

/**
 * @program: seckill
 * @description:
 * @author: lm
 * @create: 2018-11-30 10:24
 **/
public class ResponseError<T> extends ResponseData<T> {

    public ResponseError(){
        this.success = false;
    }

    public ResponseError(String failInfo){
        this.success = false;
        this.info = failInfo;
    }
}