package com.example.miaoshatest.exception;

import com.example.miaoshatest.entity.ResultStatus;

public class UserException extends  RuntimeException{

    private ResultStatus status;

    public UserException(ResultStatus status){
        super();
        this.status = status;
    }

    public ResultStatus getStatus() {
        return status;
    }

    public void setStatus(ResultStatus status) {
        this.status = status;
    }

}
