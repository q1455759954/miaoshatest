package com.example.miaoshatest.entity;

import java.io.Serializable;

public class ResultGeekQ<T> extends AbstractResult implements Serializable {

    private T data;
    private Integer count;

    protected ResultGeekQ(ResultStatus status, String message) {
        super(status, message);
    }

    protected ResultGeekQ(ResultStatus status) {
        super(status);
    }

    public static ResultGeekQ build() {
        return new ResultGeekQ(ResultStatus.SUCCESS, null);
    }


    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
