package com.zzjee.api.utils;

import java.util.HashMap;

public class AjaxResult extends HashMap<String, Object> {
    public AjaxResult(){}

    public static AjaxResult error(){
        return error("操作失败");
    }

    public static AjaxResult error(String msg){
        return  error("400",msg);
    }

    public static AjaxResult error(String code, String msg){
        AjaxResult json = new AjaxResult();
        json.put("status",code);
        json.put("message",msg);
        json.put("result",null);
        return json;
    }

    public static AjaxResult success(String msg){
        AjaxResult json = new AjaxResult();
        json.put("status","200");
        json.put("message",msg);
        json.put("result",null);
        return json;
    }

    public static AjaxResult success(){
        AjaxResult json = new AjaxResult();
        json.put("status","200");
        json.put("message","操作成功");
        json.put("result",null);
        return json;
    }

    public AjaxResult put(String key, Object value){
        super.put(key, value);
        return this;
    }
}
