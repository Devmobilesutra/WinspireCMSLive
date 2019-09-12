package com.cms.callmanager.dto;


import java.util.List;

public class ProblemFixDTO {


    private String status;

    private String errorMessage;

    private String errorCode;

    private List<PayLoad> payLoad = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public List<PayLoad> getPayLoad() {
        return payLoad;
    }

    public void setPayLoad(List<PayLoad> payLoad) {
        this.payLoad = payLoad;
    }


    static public class PayLoad {


        private String code;

        private String name;

        private String problemCategory;

        private Integer displayToCustomer;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getProblemCategory() {
            return problemCategory;
        }

        public void setProblemCategory(String problemCategory) {
            this.problemCategory = problemCategory;
        }

        public Integer getDisplayToCustomer() {
            return displayToCustomer;
        }

        public void setDisplayToCustomer(Integer displayToCustomer) {
            this.displayToCustomer = displayToCustomer;
        }

    }
}
