package com.cms.callmanager.Foc_Chargeble;

import java.io.Serializable;

/**
 * Created by Amol Nage 24,December,2019
 * Xtensible Software Technologies Pvt. Ltd.,
 * Pune, India.
 */
public class ModelClassForSavedData  implements Serializable {
    String selectedItem;

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }

    int selectedPosition=-1;
    String qty;

    public String getSelectedItem() {
        return selectedItem;
    }

    public ModelClassForSavedData(String selectedItem, String qty, String description) {
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
