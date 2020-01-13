package com.cms.callmanager.multispinner.clousermodel;

/**
 * Created by Bhavesh Chaudhari on 07-Jan-20.
 */
public class IdealHours_model {

    public IdealHours_model(String startDate, String endDate, String reason) {
        StartDate = startDate;
        EndDate = endDate;
        Reason = reason;
    }

    String StartDate,EndDate,Reason;
    public String getStartDate() {
        return StartDate;
    }

    public void setStartDate(String startDate) {
        StartDate = startDate;
    }

    public String getEndDate() {
        return EndDate;
    }

    public void setEndDate(String endDate) {
        EndDate = endDate;
    }

    public String getReason() {
        return Reason;
    }

    public void setReason(String reason) {
        Reason = reason;
    }


}
