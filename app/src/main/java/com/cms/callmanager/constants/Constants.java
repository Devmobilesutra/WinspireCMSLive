package com.cms.callmanager.constants;

import android.content.Context;

/**
 * Created by Monika on 15/1/17.
 */
public class Constants {




   // public static String Base_url= "http://115.248.31.151:90/cms/";
  //  public static String Base_url= "http://52.172.203.199/webapi/";  //old

    public static String Base_url= "https://naverp.cms.com:8085/webapi/";  //new
    public static boolean LOG_DEBUG = true;

    public static String key = "OiB2LU+KWGnkI1j5nlkkcA==";

    public static String secret_key = "ju6tygh7u7tdg554k098ujd5468o" ;
	public static String LOGIN= "cms/login/getuser";
	public static String UPDATE_STATUS= "cms/updatestatus.asmx/postStatus";
	public static String ATM= "cms/Ticketing/Atms";
	public static String OPEN_CALLS= "OpenCalls.asmx/Calls";
	public static String FILE_ATTACHMENT= "cms/Ticketing/UploadFile";
	public static String ACK_FILE_ATTACHMENT= "cms/Ticketing/Acknowledgement";
	public static String PENDINGCALLS= "cms/ticketing/pendingcalls";
	public static String CALLDETAILS= "cms/ticketing/CallDetails";
	public static String FOLLOWUP = "cms/Ticketing/FollowUp";
    public static String HoldCall = "cms/ticketing/CallStatusHold";
    public static String SearchCall = "cms/Ticketing/SearchCall";
    public static String VIEWATTACHMENT = "cms/ticketing/ViewFile";
    public static String SOURCE = "cms/ticketing/Source";
    public static String SUBSTATUS = "cms/ticketing/SubStatus";
    public static String CALLACCEPTORREJECT = "cms/Ticketing/CallAcceptorReject";
    public static String REJECTEDCALLS = "cms/ticketing/RejectedCalls";
    public static String CALLTYPES = "cms/ticketing/CallTypes";

   // public static String EngineertoHub = "Inventory/GetData";
    public static String InvReplacementOrder = "InvReplacementOrder/ListDetails";
    public static String InvReplacementDetails = "InvReplacementOrder/HeaderDetails";

    public static String DocketNo = "InvReplacementOrder/GetDropData?id=DocketNo";
    public static String GetInventoryTransferList = "CMS/TransferOrdersBad/GetInventoryTransferList";
    public static String GetInventoryTransferDetails = "CMS/TransferOrdersBad/GetInventoryTransferDetails";
    public static String TransferHeaderSave = "/CMS/TransferOrdersBad/TransferHeaderSave";




    public static String TransferHeaderRelease = "/CMS/TransferOrdersBad/TransferHeaderRelease";
    public static String TransferHeaderPosting = "/CMS/TransferOrdersBad/TransferHeaderPosting";


    // replacement
    public static String RecDetails = "/InvReplacementOrder/RecDetails";
    public static String InvSaveData = "InvReplacementOrder";
    public static String NewInvReplacement = "InvReplacementOrder/NewInvReplacement";
    public static String InvPostingData = "InvReplacementOrder/InvPostingData";
    public static String ItemCodeSubstitute = "InvReplacementOrder/GetDropData?id=ItemCodeSubstitute";



    // good
    public static String AddNewGood = "Inventory/AddNewGood";
    public static String TransferFromCode = "Inventory/GetDataGood";
    public static String EngineertoHubGood = "Inventory/GetDataGood";
    public static String HeaderDetailsGood = "Inventory/HeaderDetailsGood";
    public static String LineItemCode = "Inventory/GetDataGood?id=LineItemCode";
    public static String ItemWiseDataGood = "Inventory/ItemWiseDataGood";
    public static String SaveDataGood = "Inventory/SaveDataGood?ActionFor=Update";
    public static String ReleaseDataGood = "Inventory/ReleaseDataGood";
    public static String PostingDataGood = "Inventory/PostingDataGood";
    public static String ReopenDataGood = "Inventory/ReopenDataGood";
    public static String LineEngineerCode = "Inventory/GetDataGood?id=LineEngineerCode";
    public static String TransferOrderTransferType = "/CMS/TransferOrdersBad/TransferOrderTransferType";
    public static String Structure = "/Inventory/GetDataGood?id=Structure";
    public static String ShippingMethodCode = "/Inventory/GetDataGood?id=ShippingMethodCode";
    public static String ShippingAgentCode = "/Inventory/GetDataGood?id=ShippingAgentCode";

    // Defective

  String Userid = Constant.UserId;

    public static String TransferFromBad = "Inventory/GetDataBad";
    public static String AddNewBad = "Inventory/AddNewDefective";
    public static String HeaderDetailsBad = "Inventory/HeaderDetailsBad";
    public static String ItemWiseDataBad = "Inventory/ItemWiseDataBad";
    public static String SaveDataBad = "Inventory/SaveDataBad?ActionFor=Update";
    public static String ReopenDataBad = "Inventory/ReopenDataBad";
    public static String ReleaseDataBad = "Inventory/ReleaseDataBad";
    public static String PostingDataBad = "Inventory/PostingDataBad";
    public static String LineItemCodeBad = "Inventory/GetDataBad?ActionFor=LineItemCode";
    public static String LineEngineerCodeBad = "Inventory/GetDataBad?ActionFor=LineEngineerCode";
    public static String StructureBad = "/Inventory/GetDataBad?ActionFor=Structure";
    public static String ShippingMethodCodeBad = "/Inventory/GetDataBad?ActionFor=ShippingMethodCode";
    public static String ShippingAgentCodeBad = "/Inventory/GetDataBad?ActionFor=ShippingAgentCode";


    // Good and Defective
    public static String DeleteLineForGoodBad = "Inventory/DeleteLineForGoodBad";


    // pahse 1
    public static String ResponseCategory = "cms/ticketing/responsecategory";
    public static String ProblemFix = "cms/ticketing/problemfix";
    public static String Solution = "cms/ticketing/solution";

}
