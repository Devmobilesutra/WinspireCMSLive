package com.cms.callmanager.dto;

/**
 * Created by Bhavesh Chaudhari on 28-May-19.
 */

public class ItemCodeSubstituteModel {

    String No_;
    String SN;

    public ItemCodeSubstituteModel(String no_, String SN) {
        No_ = no_;
        this.SN = SN;
    }

    public String getNo_() {
        return No_;
    }

    public void setNo_(String no_) {
        No_ = no_;
    }

    public String getSN() {
        return SN;
    }

    public void setSN(String SN) {
        this.SN = SN;
    }
}
