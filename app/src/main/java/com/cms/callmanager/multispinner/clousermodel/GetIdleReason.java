package com.cms.callmanager.multispinner.clousermodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Bhavesh Chaudhari on 03-Jan-20.
 */
public class GetIdleReason {

    @SerializedName("Reason_Code")
    @Expose
    String Reason_Code;

    @SerializedName("Idle_Reason")
    @Expose
    String Idle_Reason;

    public String getReason_Code() {
        return Reason_Code;
    }

    public void setCode(String reason_Code) {
        Reason_Code = reason_Code;
    }

    public String getIdle_Reason() {
        return Idle_Reason;
    }

    public void setIdle_Reason(String idle_Reason) {
        Idle_Reason = idle_Reason;
    }

}
