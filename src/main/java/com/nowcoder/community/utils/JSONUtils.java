package com.nowcoder.community.utils;


import com.alibaba.fastjson.JSONObject;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 陈进松
 * @Date: 2021/10/05/0:10
 * @Description: JSON工具类
 */
public class JSONUtils {
    public static String getJSONString(int code, String msg, Map<String,Object> map){
        //创建JSON对象转换为String返回
        JSONObject object = new JSONObject();
        object.put("code",code);
        object.put("msg",msg);
        if(map != null){
            for (String key: map.keySet()){
                object.put(key,map.get(key));
            }
        }
        return object.toJSONString();
    }
    public static String getJSONString(int code, String msg){
        return getJSONString(code,msg,null);
    }
    public static String getJSONString(int code){
        return getJSONString(code,null,null);
    }
}