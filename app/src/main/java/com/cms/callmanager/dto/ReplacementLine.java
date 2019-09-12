package com.cms.callmanager.dto;

/**
 * Created by Bhavesh Chaudhari on 27-May-19.
 */

public class ReplacementLine {


    String keyField,document_NoField,line_noField,line_noFieldSpecified,entry_TypeField,entry_TypeFieldSpecified,
            item_CodeField,item_DescriptionField,location_CodeField,quantityField,quantityFieldSpecified,
            unit_of_MeasurementField,reason_CodeField,entry_Type_SubstituteField,entry_Type_SubstituteFieldSpecified,
            item_Code_SubstituteField,item_Description_SubstituteField,reason_Code_SubstituteField,uOM_SubstituteField,enginner_comments;


    public ReplacementLine(String item_CodeField, String item_DescriptionField, String location_CodeField, String quantityField, String unit_of_MeasurementField, String reason_CodeField, String entry_Type_SubstituteField, String item_Code_SubstituteField, String item_Description_SubstituteField, String reason_Code_SubstituteField, String uOM_SubstituteField, String enginner_comments) {
        this.item_CodeField = item_CodeField;
        this.item_DescriptionField = item_DescriptionField;
        this.location_CodeField = location_CodeField;
        this.quantityField = quantityField;
        this.unit_of_MeasurementField = unit_of_MeasurementField;
        this.reason_CodeField = reason_CodeField;
        this.entry_Type_SubstituteField = entry_Type_SubstituteField;
        this.item_Code_SubstituteField = item_Code_SubstituteField;
        this.item_Description_SubstituteField = item_Description_SubstituteField;
        this.reason_Code_SubstituteField = reason_Code_SubstituteField;
        this.uOM_SubstituteField = uOM_SubstituteField;
        this.enginner_comments = enginner_comments;
    }

    public ReplacementLine(String keyField, String document_NoField,
                           String line_noField, String line_noFieldSpecified,
                           String entry_TypeField, String entry_TypeFieldSpecified,
                           String item_CodeField, String item_DescriptionField,
                           String location_CodeField, String quantityField,
                           String quantityFieldSpecified, String unit_of_MeasurementField,
                           String reason_CodeField, String entry_Type_SubstituteField,
                           String entry_Type_SubstituteFieldSpecified, String item_Code_SubstituteField,
                           String item_Description_SubstituteField, String reason_Code_SubstituteField,
                           String uOM_SubstituteField,
                           String enginner_Comments) {

        this.keyField = keyField;
        this.document_NoField = document_NoField;
        this.line_noField = line_noField;
        this.line_noFieldSpecified = line_noFieldSpecified;
        this.entry_TypeField = entry_TypeField;
        this.entry_TypeFieldSpecified = entry_TypeFieldSpecified;
        this.item_CodeField = item_CodeField;
        this.item_DescriptionField = item_DescriptionField;
        this.location_CodeField = location_CodeField;
        this.quantityField = quantityField;
        this.quantityFieldSpecified = quantityFieldSpecified;
        this.unit_of_MeasurementField = unit_of_MeasurementField;
        this.reason_CodeField = reason_CodeField;
        this.entry_Type_SubstituteField = entry_Type_SubstituteField;
        this.entry_Type_SubstituteFieldSpecified = entry_Type_SubstituteFieldSpecified;
        this.item_Code_SubstituteField = item_Code_SubstituteField;
        this.item_Description_SubstituteField = item_Description_SubstituteField;
        this.reason_Code_SubstituteField = reason_Code_SubstituteField;
        this.uOM_SubstituteField = uOM_SubstituteField;
        this.enginner_comments = enginner_Comments;
    }

    public String getKeyField() {
        return keyField;
    }

    public void setKeyField(String keyField) {
        this.keyField = keyField;
    }

    public String getDocument_NoField() {
        return document_NoField;
    }

    public void setDocument_NoField(String document_NoField) {
        this.document_NoField = document_NoField;
    }

    public String getLine_noField() {
        return line_noField;
    }

    public void setLine_noField(String line_noField) {
        this.line_noField = line_noField;
    }

    public String getLine_noFieldSpecified() {
        return line_noFieldSpecified;
    }

    public void setLine_noFieldSpecified(String line_noFieldSpecified) {
        this.line_noFieldSpecified = line_noFieldSpecified;
    }

    public String getEntry_TypeField() {
        return entry_TypeField;
    }

    public void setEntry_TypeField(String entry_TypeField) {
        this.entry_TypeField = entry_TypeField;
    }

    public String getEntry_TypeFieldSpecified() {
        return entry_TypeFieldSpecified;
    }

    public void setEntry_TypeFieldSpecified(String entry_TypeFieldSpecified) {
        this.entry_TypeFieldSpecified = entry_TypeFieldSpecified;
    }

    public String getItem_CodeField() {
        return item_CodeField;
    }

    public void setItem_CodeField(String item_CodeField) {
        this.item_CodeField = item_CodeField;
    }

    public String getItem_DescriptionField() {
        return item_DescriptionField;
    }

    public void setItem_DescriptionField(String item_DescriptionField) {
        this.item_DescriptionField = item_DescriptionField;
    }

    public String getLocation_CodeField() {
        return location_CodeField;
    }

    public void setLocation_CodeField(String location_CodeField) {
        this.location_CodeField = location_CodeField;
    }

    public String getQuantityField() {
        return quantityField;
    }

    public void setQuantityField(String quantityField) {
        this.quantityField = quantityField;
    }

    public String getQuantityFieldSpecified() {
        return quantityFieldSpecified;
    }

    public void setQuantityFieldSpecified(String quantityFieldSpecified) {
        this.quantityFieldSpecified = quantityFieldSpecified;
    }

    public String getUnit_of_MeasurementField() {
        return unit_of_MeasurementField;
    }

    public void setUnit_of_MeasurementField(String unit_of_MeasurementField) {
        this.unit_of_MeasurementField = unit_of_MeasurementField;
    }

    public String getReason_CodeField() {
        return reason_CodeField;
    }

    public void setReason_CodeField(String reason_CodeField) {
        this.reason_CodeField = reason_CodeField;
    }

    public String getEntry_Type_SubstituteField() {
        return entry_Type_SubstituteField;
    }

    public void setEntry_Type_SubstituteField(String entry_Type_SubstituteField) {
        this.entry_Type_SubstituteField = entry_Type_SubstituteField;
    }

    public String getEntry_Type_SubstituteFieldSpecified() {
        return entry_Type_SubstituteFieldSpecified;
    }

    public void setEntry_Type_SubstituteFieldSpecified(String entry_Type_SubstituteFieldSpecified) {
        this.entry_Type_SubstituteFieldSpecified = entry_Type_SubstituteFieldSpecified;
    }

    public String getItem_Code_SubstituteField() {
        return item_Code_SubstituteField;
    }

    public void setItem_Code_SubstituteField(String item_Code_SubstituteField) {
        this.item_Code_SubstituteField = item_Code_SubstituteField;
    }

    public String getItem_Description_SubstituteField() {
        return item_Description_SubstituteField;
    }

    public void setItem_Description_SubstituteField(String item_Description_SubstituteField) {
        this.item_Description_SubstituteField = item_Description_SubstituteField;
    }

    public String getReason_Code_SubstituteField() {
        return reason_Code_SubstituteField;
    }

    public void setReason_Code_SubstituteField(String reason_Code_SubstituteField) {
        this.reason_Code_SubstituteField = reason_Code_SubstituteField;
    }

    public String getuOM_SubstituteField() {
        return uOM_SubstituteField;
    }

    public void setuOM_SubstituteField(String uOM_SubstituteField) {
        this.uOM_SubstituteField = uOM_SubstituteField;
    }

    public String getEnginner_comments() {
        return enginner_comments;
    }

    public void setEnginner_comments(String enginner_comments) {
        this.enginner_comments = enginner_comments;
    }
}
