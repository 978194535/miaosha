package com.miaoshaproject.error;

public enum EmBusinessError implements  CommonError {
    //您的手机号已经被注册,请直接登录
    TELEPHONE_IS_EXIST(10003,"您的手机号已经被注册,请直接登录"),
    //通用错误类型10001
    PARAMETER_VALIDATION_ERROR(10001,"参数不合法"),
    //未知错误
    UNKNOW_ERROR(10002,"未知错误"),
    //20000开头的为用户错误类型
    USER_NOT_EXIST(20001,"用户不存在"),
    LOGIN_FAIL(20001,"登录失败"),
    STCOK_NOT_ENOUGH(30001,"库存不足"),
    TIME_NO(30001,"登录次数过多"),
    USER_NOT_LOGIN(20003,"用户还没有登录"),
    REGISTER_ERROR(20005,"注册失败"),
    OUTLINE_ERROR(20006,"注销失败"),
    NOT_LOGIN(20007,"用户未登陆"),
    ERRORPASSWOED(20004,"账号密码错误")

    ;


    private EmBusinessError(int errCode,String errMsg){
        this.errCode = errCode;
        this.errMsg = errMsg;
    }
    private int errCode;
    private String errMsg;
    @Override
    public int getErrorCode() {
        return this.errCode;
    }

    @Override
    public String getErrorMsg() {
        return this.errMsg;
    }

    @Override
    public CommonError setErrorMsg(String errMsg) {
        this.errMsg = errMsg;
        return this;
    }
}
