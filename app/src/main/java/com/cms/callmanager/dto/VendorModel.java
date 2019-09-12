package com.cms.callmanager.dto;

/**
 * Created by Bhavesh Chaudhari on 22-May-19.
 */

public class VendorModel {

    String name;
    String paramName;
    String code;

    public VendorModel(String name, String paramName) {
        this.name = name;
        this.paramName = paramName;
    }

    public VendorModel(String name, String paramName, String code) {
        this.name = name;
        this.paramName = paramName;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
