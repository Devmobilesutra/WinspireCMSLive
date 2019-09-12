package com.cms.callmanager.model;

/**
 * Created by Bhavesh Chaudhari on 31-May-19.
 */

public class TransferOrderTransferType {

    String Value,Texts;

    public TransferOrderTransferType(String value, String texts) {
        Value = value;
        Texts = texts;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        Value = value;
    }

    public String getTexts() {
        return Texts;
    }

    public void setTexts(String texts) {
        Texts = texts;
    }
}
