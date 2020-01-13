package com.cms.callmanager.Foc_Chargeble;

 public  class Charge_list_Model {
    String foc_item;
    String discription;
    String qty ;


    public String getFoc_item() {
        return foc_item;
    }

    public void setFoc_item(String foc_item) {
        this.foc_item = foc_item;
    }

    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

   /* public static List<Foc_list_Model> getUserList() {
        List<Foc_list_Model> userList = new ArrayList<>();
        for (int i = 0; i < userList.size(); i++) {
            Foc_list_Model foc_list_Model = new Foc_list_Model();
            //   foc_list_Model.setUserId(0+i);
            foc_list_Model.getFoc_item(""+);
            userList.add(foc_list_Model);
        }
        return userList;
    }*/
}
