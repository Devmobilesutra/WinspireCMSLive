package com.cms.callmanager.dto;

/**
 * Created by Bhavesh Chaudhari on 18-May-19.
 */

public class stockTransfer {

    public String number;
    public String from_code;
    public String to_code;
    public String status;
    public String PickList_No;


    public stockTransfer(String number, String from_code) {
        this.number = number;
        this.from_code = from_code;
    }

    public stockTransfer(String number, String from_code, String to_code, String status) {
        this.number = number;
        this.from_code = from_code;
        this.to_code = to_code;
        this.status = status;
    }

    public stockTransfer(String number, String from_code, String to_code, String status, String PickList_No) {
        this.number = number;
        this.from_code = from_code;
        this.to_code = to_code;
        this.status = status;
        this.PickList_No=PickList_No;
    }

    public String getPickList_No() {
        return PickList_No;
    }

    public void setPickList_No(String pickList_No) {
        PickList_No = pickList_No;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getFrom_code() {
        return from_code;
    }

    public void setFrom_code(String from_code) {
        this.from_code = from_code;
    }

    public String getTo_code() {
        return to_code;
    }

    public void setTo_code(String to_code) {
        this.to_code = to_code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
