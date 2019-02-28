package org.seckill.dto;

import java.util.List;

/**
 * @program: seckill
 * @description: 所有ajax请求返回类型，封装json结果
 * @author: lm
 * @create: 2018-11-26 15:30
 **/
public class ResponseData<T> {
    boolean success;
    String info = "";
    T result;
    // 扩展信息
    private T data;
    Long totalCount;

    public ResponseData(){}
    public ResponseData(String failInfo){
        this.success = false;
        this.info = failInfo;
    }
    public ResponseData(String successInfo, T result){
        this.success = true;
        this.info = successInfo;
        this.result = result;
        if (result instanceof List){
            this.totalCount = (long) ((List) result).size();
        }
    }
    public ResponseData(String successInfo, T result, Long totalCount){
        this.success = true;
        this.info = successInfo;
        this.result = result;
        this.totalCount = totalCount;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }
}