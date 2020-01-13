package com.cms.callmanager.Foc_Chargeble;

import java.io.Serializable;

public class ModelClassForSavedData_Charge  implements Serializable {
    String selectedItem;
    String qty;

    public int getSelectedItemPosition() {
        return selectedItemPosition;
    }

    public void setSelectedItemPosition(int selectedItemPosition) {
        this.selectedItemPosition = selectedItemPosition;
    }

    int selectedItemPosition;
    public String getSelectedItem() {
        return selectedItem;
    }

    public ModelClassForSavedData_Charge(String selectedItem, String qty, String description) {
        this.selectedItem = selectedItem;
        this.qty = qty;
        this.description = description;
    }

    public void setSelectedItem(String selectedItem) {
        this.selectedItem = selectedItem;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    String description;
}
