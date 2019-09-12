package com.cms.callmanager.dto;

/**
 * Created by Bhavesh Chaudhari on 20-May-19.
 */

public class StockTransferList {

    String itemCode;
    String itemName;
    String paramName;
    String name;
    String Code;

    public StockTransferList(String itemCode, String paramName,String itemName) {
        this.itemCode = itemCode;
        this.paramName = paramName;
        this.itemName=itemName;
    }

    public StockTransferList(String itemCode, String itemName, String paramName, String name, String code) {
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.paramName = paramName;
        this.name = name;
        this.Code = code;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }
}
