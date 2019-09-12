package com.cms.callmanager.model;

/**
 * Created by Bhavesh Chaudhari on 31-May-19.
 */

public class ShippingMethodCode {

    String Code,Description;

    public ShippingMethodCode(String code, String description) {
        Code = code;
        Description = description;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }
}
