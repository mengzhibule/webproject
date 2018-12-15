package com.test.action;

import net.sf.json.JSONObject;

public class IndexAction {

    public String fun1(){
        return "hello world";
    }

    public String fun2(){
        JSONObject obj = new JSONObject();
        obj.put("name","shaoshuai");
        obj.put("password","112233");
        return obj.toString();
    }
}
