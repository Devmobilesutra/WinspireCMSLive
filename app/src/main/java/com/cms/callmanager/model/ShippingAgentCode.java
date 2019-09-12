package com.cms.callmanager.model;

/**
 * Created by Bhavesh Chaudhari on 31-May-19.
 */

public class ShippingAgentCode {

    String Code,Name;

    public ShippingAgentCode(String code, String name) {
        Code = code;
        Name = name;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
