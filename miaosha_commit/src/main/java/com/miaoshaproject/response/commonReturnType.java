package com.miaoshaproject.response;

public class commonReturnType {
    private String status;
    private Object data;

    public static commonReturnType create(Object data){
       return commonReturnType.create(data,"success");
    }
    public static commonReturnType create(Object data,String status){
        commonReturnType commonReturnType1 = new commonReturnType();
        commonReturnType1.setData(data);
        commonReturnType1.setStatus(status);
        return commonReturnType1;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
