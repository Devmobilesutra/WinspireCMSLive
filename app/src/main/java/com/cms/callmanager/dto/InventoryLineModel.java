package com.cms.callmanager.dto;

/**
 * Created by Bhavesh Chaudhari on 30-May-19.
 */

public class InventoryLineModel {

    String keyField;
    String item_NoField;
    String descriptionField;
    String quantityField;
    String unit_of_MeasureField;
    String qty_to_ShipField;
    String quantity_ShippedField;
    String qty_to_ReceiveField;
    String quantity_ReceivedField;
    String Reason_for_Partial_Receipt;
    String ticket_NoField;
    String bank_Docket_NoField;
    String resourceField;
    String line_NoField;


    public InventoryLineModel(String keyField, String item_NoField, String descriptionField,
                              String quantityField, String unit_of_MeasureField, String qty_to_ShipField,
                              String quantity_ShippedField, String qty_to_ReceiveField, String quantity_ReceivedField,
                              String reason_for_Partial_Receipt, String ticket_NoField, String bank_Docket_NoField,
                              String resourceField, String line_NoField) {
        this.keyField = keyField;
        this.item_NoField = item_NoField;
        this.descriptionField = descriptionField;
        this.quantityField = quantityField;
        this.unit_of_MeasureField = unit_of_MeasureField;
        this.qty_to_ShipField = qty_to_ShipField;
        this.quantity_ShippedField = quantity_ShippedField;
        this.qty_to_ReceiveField = qty_to_ReceiveField;
        this.quantity_ReceivedField = quantity_ReceivedField;
        Reason_for_Partial_Receipt = reason_for_Partial_Receipt;
        this.ticket_NoField = ticket_NoField;
        this.bank_Docket_NoField = bank_Docket_NoField;
        this.resourceField = resourceField;
        this.line_NoField=line_NoField;
    }

    public InventoryLineModel(String keyField, String item_NoField, String descriptionField,
                              String quantityField, String unit_of_MeasureField, String qty_to_ShipField,
                              String quantity_ShippedField, String qty_to_ReceiveField, String quantity_ReceivedField,
                              String reason_for_Partial_Receipt, String ticket_NoField, String bank_Docket_NoField,
                              String resourceField) {
        this.keyField = keyField;
        this.item_NoField = item_NoField;
        this.descriptionField = descriptionField;
        this.quantityField = quantityField;
        this.unit_of_MeasureField = unit_of_MeasureField;
        this.qty_to_ShipField = qty_to_ShipField;
        this.quantity_ShippedField = quantity_ShippedField;
        this.qty_to_ReceiveField = qty_to_ReceiveField;
        this.quantity_ReceivedField = quantity_ReceivedField;
        Reason_for_Partial_Receipt = reason_for_Partial_Receipt;
        this.ticket_NoField = ticket_NoField;
        this.bank_Docket_NoField = bank_Docket_NoField;
        this.resourceField = resourceField;

    }

    public String getDescriptionField() {
        return descriptionField;
    }

    public void setDescriptionField(String descriptionField) {
        this.descriptionField = descriptionField;
    }

    public String getQuantityField() {
        return quantityField;
    }

    public void setQuantityField(String quantityField) {
        this.quantityField = quantityField;
    }

    public String getKeyField() {
        return keyField;
    }

    public void setKeyField(String keyField) {
        this.keyField = keyField;
    }

    public String getItem_NoField() {
        return item_NoField;
    }

    public void setItem_NoField(String item_NoField) {
        this.item_NoField = item_NoField;
    }

    public String getReason_for_Partial_Receipt() {
        return Reason_for_Partial_Receipt;
    }

    public void setReason_for_Partial_Receipt(String reason_for_Partial_Receipt) {
        Reason_for_Partial_Receipt = reason_for_Partial_Receipt;
    }

    public String getQty_to_ShipField() {
        return qty_to_ShipField;
    }

    public void setQty_to_ShipField(String qty_to_ShipField) {
        this.qty_to_ShipField = qty_to_ShipField;
    }

    public String getQty_to_ReceiveField() {
        return qty_to_ReceiveField;
    }

    public void setQty_to_ReceiveField(String qty_to_ReceiveField) {
        this.qty_to_ReceiveField = qty_to_ReceiveField;
    }

    public String getUnit_of_MeasureField() {
        return unit_of_MeasureField;
    }

    public void setUnit_of_MeasureField(String unit_of_MeasureField) {
        this.unit_of_MeasureField = unit_of_MeasureField;
    }

    public String getQuantity_ShippedField() {
        return quantity_ShippedField;
    }

    public void setQuantity_ShippedField(String quantity_ShippedField) {
        this.quantity_ShippedField = quantity_ShippedField;
    }

    public String getQuantity_ReceivedField() {
        return quantity_ReceivedField;
    }

    public void setQuantity_ReceivedField(String quantity_ReceivedField) {
        this.quantity_ReceivedField = quantity_ReceivedField;
    }

    public String getTicket_NoField() {
        return ticket_NoField;
    }

    public void setTicket_NoField(String ticket_NoField) {
        this.ticket_NoField = ticket_NoField;
    }

    public String getBank_Docket_NoField() {
        return bank_Docket_NoField;
    }

    public void setBank_Docket_NoField(String bank_Docket_NoField) {
        this.bank_Docket_NoField = bank_Docket_NoField;
    }

    public String getResourceField() {
        return resourceField;
    }

    public void setResourceField(String resourceField) {
        this.resourceField = resourceField;
    }

    public String getLine_NoField() {
        return line_NoField;
    }

    public void setLine_NoField(String line_NoField) {
        this.line_NoField = line_NoField;
    }
}
