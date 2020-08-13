package com.example.miaoshatest.entity;

public enum ResultStatus {

    SUCCESS(0, "成功"),
    FAILD(-1, "失败"),
    MIAOSHA_FAIL(40003,"秒杀失败"),
    SYSTEM_ERROR(10001, "系统错误"),

    /**
     * 注册登录
     */
    RESIGETR_SUCCESS(20000,"注册成功!"),
    RESIGETER_FAIL(200001,"注册失败!"),
    CODE_FAIL(200002,"验证码不一致!"),
    PASSWORD_ERROR(20003,"密码错误!"),
    MOBILE_NOT_EXIST(20004,"用户不存在!"),
    DATA_NOT_EXISTS(20005, "商品不存在!"),
    USER_NOT_EXIST(20008,"用户不存在"),
    YURE_FAILD(20006, "商品预热失败！");

    /**
     * 商品模块
     */
    private int code;
    private String message;

    private ResultStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getName() {
        return this.name();
    }

    public String getOutputName() {
        return this.name();
    }

    public String toString() {
        return this.getName();
    }

    private ResultStatus(Object... args) {
        this.message = String.format(this.message, args);
    }
}
