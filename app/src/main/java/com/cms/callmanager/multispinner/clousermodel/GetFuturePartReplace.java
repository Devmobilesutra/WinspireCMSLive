package com.cms.callmanager.multispinner.clousermodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Bhavesh Chaudhari on 03-Jan-20.
 */
public class GetFuturePartReplace {
    @SerializedName("No_")
    @Expose
    String No_;

    @SerializedName("Name")
    @Expose
    String Name;

    public String getNo_() {
        return No_;
    }

    public void setNo_(String no_) {
        No_ = no_;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
