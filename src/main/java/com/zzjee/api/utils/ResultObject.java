package com.zzjee.api.utils;

import com.google.gson.Gson;

public class ResultObject {
    private String status;
    private String message;
    private Object result;

    public ResultObject(String status, String messsage, Object result){
        this.status = status;
        this.message = messsage;
        this.result = result;
    }

    public static ResultObject success(){
        return new ResultObject("200", "操作成功", null);
    }

    public static ResultObject error(String msg){
        return new ResultObject("400",msg, null);
    }

    public static ResultObject success(String msg){
        return new ResultObject("200", msg, null);
    }

    public static ResultObject success(String msg, Object result){
        return new ResultObject("200", msg, result);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public String generateJsonStr(){
        Gson gson = new Gson();
        String jsonStr = gson.toJson(this);
        return jsonStr;
    }
}
