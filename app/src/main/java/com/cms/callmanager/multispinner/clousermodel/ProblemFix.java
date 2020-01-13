package com.cms.callmanager.multispinner.clousermodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Bhavesh Chaudhari on 31-Dec-19.
 */
public class ProblemFix {
    @SerializedName("Code")
    @Expose
    String Code;

    @SerializedName("Name")
    @Expose
    String Name;

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
