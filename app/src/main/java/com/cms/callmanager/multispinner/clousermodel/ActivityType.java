package com.cms.callmanager.multispinner.clousermodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Bhavesh Chaudhari on 03-Jan-20.
 */
public class ActivityType {
    @SerializedName("Code")
    @Expose
    String Code;

    @SerializedName("Type")
    @Expose
    String Type;

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }
}
