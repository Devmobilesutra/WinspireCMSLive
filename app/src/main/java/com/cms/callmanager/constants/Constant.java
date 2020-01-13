package com.cms.callmanager.constants;


import com.cms.callmanager.adapter.LocationModel;
import com.cms.callmanager.dto.CallDTO;

public interface Constant  {

 //   LocationModel call = new LocationModel();
    CallDTO call = new CallDTO();
     String LONGI = "LONGI";
     String LATI = "LATI";
     String UserId= "UserId";
     String Pass= "Password";

     //*******images key
    String FCRAttachment ="FCRAttachment";
    String InstallationCertificate ="InstallationCertificate";
    String ATMImages1 ="ATMImages1";
    String ATMImages2 ="ATMImages2";
    String TransactionImage1 ="TransactionImage1";
    String TransactionImage2 ="TransactionImage2";
    String ErrorHistory ="ErrorHistory";



    /* String LONGI = call.getLongitude();
     String LATI = call.getLatitude();*/

    String DEVICE_ID = "DEVICE_ID";
    String PASSWORD = "P@ssw0rd@123";
    String response_date = "response_dates";
}
