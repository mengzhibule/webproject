package com.test.dao.common.test;

import java.util.HashMap;

public class Test extends HashMap<String,Object> {
    public String getId() {
        return super.get("id").toString();
    }

    public void setId(String id) {
        super.put("id", id);
    }

    public String getUsername() {
        return super.get("username").toString();
    }

    public void setUsername(String username) {
        super.put("username", username);
    }

    public String getPassword() {
        return super.get("password").toString();
    }

    public void setPassword(String password) {
        super.put("password", password);
    }

}
