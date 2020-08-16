package com.example.miaoshatest.exception;


import com.example.miaoshatest.entity.ResultStatus;

/**
 * @author 邱润泽 bullock
 */
public class MqOrderException extends RuntimeException {

    private ResultStatus status;

    public MqOrderException(ResultStatus status){
        super();
        this.status = status;
    }

    public MqOrderException(Integer code , String msg){
        super();
    }

    public ResultStatus getStatus() {
        return status;
    }

    public void setStatus(ResultStatus status) {
        this.status = status;
    }
}
