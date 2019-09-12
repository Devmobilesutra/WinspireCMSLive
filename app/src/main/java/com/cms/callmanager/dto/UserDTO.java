package com.cms.callmanager.dto;

/**
 * Created by zogato on 22/11/17.
 */

public class UserDTO {

    String userId;
    String firstName;
    String lastName;

    public boolean isTLFlag() {
        return isTLFlag;
    }

    public void setTLFlag(boolean TLFlag) {
        isTLFlag = TLFlag;
    }

    boolean isTLFlag;
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
