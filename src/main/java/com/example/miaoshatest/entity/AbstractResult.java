package com.example.miaoshatest.entity;

public  class AbstractResult {

    private ResultStatus status;
    private int code;
    private String message;

    protected AbstractResult(ResultStatus status, String message) {
        this.code = status.getCode();
        this.status = status;
        this.message = message;
    }

    protected AbstractResult(ResultStatus status) {
        this.code = status.getCode();
        this.message = status.getMessage();
        this.status = status;
    }

    public static boolean isSuccess(AbstractResult result) {
        return result!=null&&result.status==ResultStatus.SUCCESS&&result.code==ResultStatus.SUCCESS.getCode();
    }

    public AbstractResult withErrorCodeAndMessage(ResultStatus status) {
        this.status = status;
        this.code = status.getCode();
        this.message = status.getMessage();
        return this;
    }

    public static <T> ResultGeekQ<T> error(ResultStatus status) {
        return new ResultGeekQ<T>(status);
    }

    public AbstractResult withError(int code, String message) {
        this.code = code;
        this.message = message;
        return this;
    }

    public AbstractResult success() {
        this.status = ResultStatus.SUCCESS;
        return this;
    }
    public ResultStatus getStatus() {
        return this.status;
    }

    public String getMessage() {
        return this.message == null ? this.status.getMessage() : this.message;
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }

}
