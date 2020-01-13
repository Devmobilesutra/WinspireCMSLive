package com.cms.callmanager.multispinner.clousermodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Bhavesh Chaudhari on 03-Jan-20.
 */
public class GetSubModuleAffected {

    @SerializedName("Code")
    @Expose
    String Code;

    @SerializedName("Description")
    @Expose
    String Description;

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
