package com.cms.callmanager.dto;

/**
 * Created by Bhavesh Chaudhari on 23-May-19.
 */

public class LinesModel {

    String item_no;
    String quantity;
    String qty_ship;
    String qty_received;
    String quantity_name;
    String qty_ship_name;
    String qty_received_name;

    public LinesModel(String item_no, String quantity_name, String qty_ship_name, String qty_received_name) {
        this.item_no = item_no;
        this.quantity_name = quantity_name;
        this.qty_ship_name = qty_ship_name;
        this.qty_received_name = qty_received_name;
    }

    public String getItem_no() {
        return item_no;
    }

    public void setItem_no(String item_no) {
        this.item_no = item_no;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getQty_ship() {
        return qty_ship;
    }

    public void setQty_ship(String qty_ship) {
        this.qty_ship = qty_ship;
    }

    public String getQty_received() {
        return qty_received;
    }

    public void setQty_received(String qty_received) {
        this.qty_received = qty_received;
    }

    public String getQuantity_name() {
        return quantity_name;
    }

    public void setQuantity_name(String quantity_name) {
        this.quantity_name = quantity_name;
    }

    public String getQty_ship_name() {
        return qty_ship_name;
    }

    public void setQty_ship_name(String qty_ship_name) {
        this.qty_ship_name = qty_ship_name;
    }

    public String getQty_received_name() {
        return qty_received_name;
    }

    public void setQty_received_name(String qty_received_name) {
        this.qty_received_name = qty_received_name;
    }
}
