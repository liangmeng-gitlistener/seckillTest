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

    public ResponseError(String failInfo, T result){
        this.success = false;
        this.info = failInfo;
        this.result = result;
    }

    public ResponseError(T result){
        this.success = false;
        this.result = result;
    }
}