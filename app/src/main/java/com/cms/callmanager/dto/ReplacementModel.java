package com.cms.callmanager.dto;

/**
 * Created by Bhavesh Chaudhari on 23-May-19.
 */

public class ReplacementModel {

    String No_;
    String Docket_No_;
    String Posting_Date;
    String Resource;
    String Bank_Docket_No_;

    public ReplacementModel(String no_, String docket_No_, String posting_Date, String resource, String bank_Docket_No_) {
        No_ = no_;
        Docket_No_ = docket_No_;
        Posting_Date = posting_Date;
        Resource = resource;
        Bank_Docket_No_ = bank_Docket_No_;
    }

    public String getNo_() {
        return No_;
    }

    public void setNo_(String no_) {
        No_ = no_;
    }

    public String getDocket_No_() {
        return Docket_No_;
    }

    public void setDocket_No_(String docket_No_) {
        Docket_No_ = docket_No_;
    }

    public String getPosting_Date() {
        return Posting_Date;
    }

    public void setPosting_Date(String posting_Date) {
        Posting_Date = posting_Date;
    }

    public String getResource() {
        return Resource;
    }

    public void setResource(String resource) {
        Resource = resource;
    }

    public String getBank_Docket_No_() {
        return Bank_Docket_No_;
    }

    public void setBank_Docket_No_(String bank_Docket_No_) {
        Bank_Docket_No_ = bank_Docket_No_;
    }
}
