package com.cms.callmanager.dto;

/**
 * Created by Bhavesh Chaudhari on 22-May-19.
 */

public class CodeAndNameModel {

    String Code;
    String Name;
    String Name2;

    public CodeAndNameModel(String code, String name) {
        Code = code;
        Name = name;
    }

    public CodeAndNameModel(String code, String name, String name2) {
        Code = code;
        Name = name;
        Name2 = name2;
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

    public String getName2() {
        return Name2;
    }

    public void setName2(String name2) {
        Name2 = name2;
    }
}
