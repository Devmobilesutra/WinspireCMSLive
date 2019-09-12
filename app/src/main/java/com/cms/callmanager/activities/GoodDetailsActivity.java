package com.cms.callmanager.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.cms.callmanager.R;
import com.cms.callmanager.adapter.CustomAdapter1;
import com.cms.callmanager.adapter.NameCodeAdapter;
import com.cms.callmanager.adapter.ShipmentMethodAdapter;
import com.cms.callmanager.adapter.ShippingAgentCodeAdapter;
import com.cms.callmanager.adapter.StructureAdapter;
import com.cms.callmanager.adapter.TransferOrderTransferTypeAdapter;
import com.cms.callmanager.constants.Constants;
import com.cms.callmanager.dto.CodeAndNameModel;
import com.cms.callmanager.dto.InventoryLineModel;
import com.cms.callmanager.model.ShippingAgentCode;
import com.cms.callmanager.model.ShippingMethodCode;
import com.cms.callmanager.model.Structure;
import com.cms.callmanager.model.TransferOrderTransferType;
import com.cms.callmanager.services.CallManagerAsyncTask;
import com.cms.callmanager.services.CallManagerAsyncTaskArray;
import com.cms.callmanager.utils.ProgressUtil;
import com.cms.callmanager.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.EOFException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Calendar;

public class GoodDetailsActivity extends AppCompatActivity {

    String newString;
    String APIFLAG;
    String userId,name;

    String userid_name;
    int Index;

    String transfer_from_CodeField,noField,transfer_from_Name_1Field,transfer_to_CodeField,transfer_to_Name_1Field,
            in_Transit_CodeField,posting_DateField,pickList_NoField,hub_DimField,structureField,transfer_TypeField,
            statusField,BusinessUnit,SubBusinessUnit,keyField,vendor_Invoice_NoField,vendor_Invoice_DateField,transported_by,
            shipment_Method_Code,shipping_Agent_Code,vehicle_No,lR_RR_No,lR_RR_DateField,transfer_Order_ETAField;
    TextView tvFromCode,tvNoField,tvFromName,tvToCode,tvTrasitCode,tvPostingDate,tvPiackListNo,tvLRDate,
            tvBusinessUnit,tvHubDim,tvToName,tvInTrasit,tvStructure,tvTrasnferType,tvStatus,tvSubBusiness,tvOrderETA;
    TextView tvVendorInvoiceDate,tvShipingAgent,tvShipMethodCode;
    EditText etVendorInvoiceNum,etTransportedBy,etVehicleNo,etLLRRNo;

    private LinesAdapter linesAdapter;
    TableRow trInvoiceDate,trLLRRDate,trShipmentMehodCode,trShipAgentCode,trStructure,trETLDate,trTransferType;

    LinearLayout llNew,llold;
    ArrayList<InventoryLineModel> linesModels;
    ImageView ivNumArrow,ivLinesArrow,ivVendorArrow;
    LinearLayout llNum,llInfo;
    private boolean numFlag = true,lineFlag = true,infoFlag= true;
    RecyclerView rv_line;


    ArrayList<TransferOrderTransferType> transferOrderTransferTypes;
    ArrayList<Structure> structures;
    ArrayList<ShippingMethodCode> shippingMethodCodes;
    ArrayList<ShippingAgentCode> shippingAgentCodes;
    ArrayList<CodeAndNameModel> codeAndName;
    TransferOrderTransferTypeAdapter transferOrderTransferTypeAdapter;
    StructureAdapter structureAdapter;
    ShipmentMethodAdapter shipmentMethodAdapter;
    ShippingAgentCodeAdapter shippingAgentCodeAdapter;
    SharedPreferences preferences;


    ListView listView1;
    private static CustomAdapter1 adapter1;
    NameCodeAdapter nameCodeAdapter;


    // adapter
    TextView itemCodeText,tvDescText,tvUomText,tvEngCodeText ;
     ImageView img_arrow_spinner;
    LinearLayout llPostReopen,llReleaseUpdate;
    String UpdateFlag = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_good_details);


        llNew=(LinearLayout)findViewById(R.id.llNew);
        llold=(LinearLayout)findViewById(R.id.llold);

        tvFromCode= (TextView)findViewById(R.id.tvFromCode);
        tvNoField = (TextView)findViewById(R.id.tvNoField);

        tvFromName = (TextView)findViewById(R.id.tvFromName);
        tvToCode = (TextView)findViewById(R.id.tvToCode);
        // tvTrasitCode = (TextView)findViewById(R.id.tvTrasitCode);
        tvPostingDate = (TextView)findViewById(R.id.tvPostingDate);
        tvPiackListNo = (TextView)findViewById(R.id.tvPiackListNo);
        tvBusinessUnit = (TextView)findViewById(R.id.tvBusinessUnit);
        tvHubDim = (TextView)findViewById(R.id.tvHubDim);
        tvToName =(TextView)findViewById(R.id.tvToName);
        tvInTrasit = (TextView)findViewById(R.id.tvInTrasit);
        tvStructure = (TextView)findViewById(R.id.tvStructure);
        tvTrasnferType = (TextView)findViewById(R.id.tvTrasnferType);
        tvStatus= (TextView)findViewById(R.id.tvStatus);
        tvSubBusiness = (TextView)findViewById(R.id.tvSubBusiness);
        tvOrderETA = (TextView)findViewById(R.id.tvOrderETA);
        tvVendorInvoiceDate = (TextView)findViewById(R.id.tvVendorInvoiceDate);
        tvShipingAgent = (TextView)findViewById(R.id.tvShipingAgent);
        tvLRDate = (TextView)findViewById(R.id.tvLRDate);



        preferences = getSharedPreferences("CMS", Context.MODE_PRIVATE);
        userId = preferences.getString("userId", null);
        name = preferences.getString("UserName", null);



        etVendorInvoiceNum=(EditText)findViewById(R.id.etVendorInvoiceNum);

        etTransportedBy=(EditText)findViewById(R.id.etTransportedBy);
        tvShipMethodCode=(TextView) findViewById(R.id.tvShipMethodCode);
        etVehicleNo=(EditText)findViewById(R.id.etVehicleNo);
        etLLRRNo=(EditText)findViewById(R.id.etLLRRNo);

        llPostReopen = (LinearLayout)findViewById(R.id.llPostReopen);
        llReleaseUpdate = (LinearLayout)findViewById(R.id.llReleaseUpdate);

        rv_line= (RecyclerView)findViewById(R.id.rv_line);
        linesAdapter=new LinesAdapter(linesModels, getApplicationContext());
        LinearLayoutManager horizontalLayout = new LinearLayoutManager(GoodDetailsActivity.this, LinearLayoutManager.VERTICAL, false);
        rv_line.setLayoutManager(horizontalLayout);
        rv_line.setAdapter(linesAdapter);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                newString= null;
            } else {
                newString= extras.getString("number");
            }
        } else {
            newString= (String) savedInstanceState.getSerializable("number");
        }


        etVendorInvoiceNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                vendor_Invoice_NoField = etVendorInvoiceNum.getText().toString();
            }
        });

        etVehicleNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                vehicle_No = etVehicleNo.getText().toString();
            }
        });


        etTransportedBy.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                transported_by = etTransportedBy.getText().toString();
            }
        });

        etLLRRNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                lR_RR_No = etLLRRNo.getText().toString();
            }
        });


        if (newString.equalsIgnoreCase("New"))
        {
            llNew.setVisibility(View.VISIBLE);
            if(Utils.isInternetOn(this)){
                ProgressUtil.ShowBar(GoodDetailsActivity.this);
                GoodNewAPI goodNewAPI = new GoodNewAPI(Constants.AddNewGood, "GET" ,
                        GoodDetailsActivity.this);
                goodNewAPI.execute();
            }else{
                Utils.showAlertBox("Please Connect to internet",GoodDetailsActivity.this);

            }
        }
        else
        {
            llold.setVisibility(View.VISIBLE);
            if(Utils.isInternetOn(this)){
                ProgressUtil.ShowBar(GoodDetailsActivity.this);
                GoodDetailsAPI goodDetailsAPI = new GoodDetailsAPI(Constants.HeaderDetailsGood+"?RecNo="+newString, "GET" ,
                        GoodDetailsActivity.this);
                goodDetailsAPI.execute();
            }else{
                Utils.showAlertBox("Please Connect to internet",GoodDetailsActivity.this);

            }
        }

        findViewById(R.id.btnPost).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Utils.isInternetOn(GoodDetailsActivity.this)){
                    ProgressUtil.ShowBar(GoodDetailsActivity.this);
                    UpdateFlag = "post";
                    UpdateApi updateApi = new UpdateApi(Constants.SaveDataGood, "POST" ,
                            GoodDetailsActivity.this);
                    updateApi.execute();
                }else{
                    Utils.showAlertBox("Please Connect to internet",GoodDetailsActivity.this);

                }
            }
        });

        findViewById(R.id.btnUpdate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Utils.isInternetOn(GoodDetailsActivity.this)){
                    ProgressUtil.ShowBar(GoodDetailsActivity.this);
                    UpdateFlag = "update";
                    UpdateApi updateApi = new UpdateApi(Constants.SaveDataGood, "POST" ,
                            GoodDetailsActivity.this);
                    updateApi.execute();
                }else{
                    Utils.showAlertBox("Please Connect to internet",GoodDetailsActivity.this);

                }
            }
        });

        findViewById(R.id.btnRelease).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Utils.isInternetOn(GoodDetailsActivity.this)){
                    ProgressUtil.ShowBar(GoodDetailsActivity.this);
                    UpdateFlag = "release";
                    UpdateApi updateApi = new UpdateApi(Constants.SaveDataGood, "POST" ,
                            GoodDetailsActivity.this);
                    updateApi.execute();
                }else{
                    Utils.showAlertBox("Please Connect to internet",GoodDetailsActivity.this);

                }
            }
        });

        findViewById(R.id.btnReopen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Utils.isInternetOn(GoodDetailsActivity.this)){
                    ProgressUtil.ShowBar(GoodDetailsActivity.this);
                    ReopenApi reopenApi = new ReopenApi(Constants.ReopenDataGood+"?TransferOrderNo="+newString, "GET" ,
                            GoodDetailsActivity.this);
                    reopenApi.execute();
                }else{
                    Utils.showAlertBox("Please Connect to internet",GoodDetailsActivity.this);

                }
               // Toast.makeText(GoodDetailsActivity.this, "btnReopen", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.btnSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Utils.isInternetOn(GoodDetailsActivity.this)){
                    ProgressUtil.ShowBar(GoodDetailsActivity.this);
                    UpdateFlag = "save";
                    UpdateApi updateApi = new UpdateApi(Constants.SaveDataGood, "POST" ,
                            GoodDetailsActivity.this);
                    updateApi.execute();
                }else{
                    Utils.showAlertBox("Please Connect to internet",GoodDetailsActivity.this);

                }
            }
        });

        findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });




        findViewById(R.id.llAddLine).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if (statusField.equalsIgnoreCase("0")) {

                    linesModels.add(new InventoryLineModel("",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            userId+"-"+name,
                            ""

                    ));

                    linesAdapter = new LinesAdapter(linesModels, getApplicationContext());
                    rv_line.setAdapter(linesAdapter);
                    rv_line.setVisibility(View.VISIBLE);
                }
            }
        });


        llNum = (LinearLayout)findViewById(R.id.llNum);
        llNum.setVisibility(View.GONE);
        ivNumArrow = (ImageView)findViewById(R.id.ivNumArrow);
        ivNumArrow.setBackgroundResource(R.drawable.ic_expand_more_white_24dp);
        findViewById(R.id.rlItemField).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (numFlag)
                {
                    llNum.setVisibility(View.VISIBLE);
                    ivNumArrow.setBackgroundResource(R.drawable.ic_expand_less_white_24dp);
                    numFlag = false;
                }
                else
                {
                    llNum.setVisibility(View.GONE);
                    ivNumArrow.setBackgroundResource(R.drawable.ic_expand_more_white_24dp);
                    numFlag = true;
                }
            }
        });

        rv_line.setVisibility(View.GONE);
        ivLinesArrow = (ImageView)findViewById(R.id.ivLinesArrow);
        ivLinesArrow.setBackgroundResource(R.drawable.ic_expand_more_white_24dp);


        findViewById(R.id.trFromCode).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (statusField.equalsIgnoreCase("0"))
                {
                    APIFLAG = "FromCode";
                    // ProgressUtil.ShowBar(GoodDetailsActivity.this);
                    GetSpinnerApi transferFromCodeCallAsyncTask = new GetSpinnerApi(Constants.TransferFromCode+"?id=TransferFromCode"+"&UserId="+userId, "GET" ,
                            GoodDetailsActivity.this);
                    transferFromCodeCallAsyncTask.execute();
                }
            }
        });


        findViewById(R.id.trToCode).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (statusField.equalsIgnoreCase("0")) {
                    APIFLAG = "ToCode";
                    // ProgressUtil.ShowBar(GoodDetailsActivity.this);
                    GetSpinnerApi transferFromCodeCallAsyncTask = new GetSpinnerApi(Constants.TransferFromCode + "?id=TransferToCode"+"&UserId="+userId, "GET",
                            GoodDetailsActivity.this);
                    transferFromCodeCallAsyncTask.execute();
                }
            }
        });

        rv_line.setVisibility(View.GONE);
        ivLinesArrow = (ImageView)findViewById(R.id.ivLinesArrow);
        ivLinesArrow.setBackgroundResource(R.drawable.ic_expand_more_white_24dp);
        findViewById(R.id.rlLines).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (lineFlag)
                {
                    rv_line.setVisibility(View.VISIBLE);
                    ivLinesArrow.setBackgroundResource(R.drawable.ic_expand_less_white_24dp);
                    lineFlag = false;
                }
                else
                {
                    rv_line.setVisibility(View.GONE);
                    ivLinesArrow.setBackgroundResource(R.drawable.ic_expand_more_white_24dp);
                    lineFlag = true;
                }
            }
        });



        llInfo = (LinearLayout)findViewById(R.id.llInfo);
        llInfo.setVisibility(View.GONE);
        ivVendorArrow = (ImageView)findViewById(R.id.ivVendorArrow);
        ivVendorArrow.setBackgroundResource(R.drawable.ic_expand_more_white_24dp);
        findViewById(R.id.rlOtherInfo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (infoFlag)
                {
                    llInfo.setVisibility(View.VISIBLE);
                    ivVendorArrow.setBackgroundResource(R.drawable.ic_expand_less_white_24dp);
                    infoFlag = false;
                }
                else
                {
                    llInfo.setVisibility(View.GONE);
                    ivVendorArrow.setBackgroundResource(R.drawable.ic_expand_more_white_24dp);
                    infoFlag = true;
                }
            }
        });
        
        
        
        trShipmentMehodCode=(TableRow)findViewById(R.id.trShipmentMehodCode);
        trShipmentMehodCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (statusField.equalsIgnoreCase("0")) {
                    // GetSpinnerData("ShipmentMehodCode");
                    final Dialog dialog = new Dialog(GoodDetailsActivity.this);
                    dialog.setCancelable(true);
                    dialog.setTitle("Select Name");
                    dialog.setContentView(R.layout.dialog_listview);


                    listView1 = (ListView) dialog.findViewById(R.id.listView1);
                    shipmentMethodAdapter = new ShipmentMethodAdapter(shippingMethodCodes, getApplicationContext());
                    listView1.setAdapter(shipmentMethodAdapter);
                    listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            dialog.dismiss();
                            shipment_Method_Code = shippingMethodCodes.get(position).getCode();
                            tvShipMethodCode.setText(shippingMethodCodes.get(position).getDescription());

                        }
                    });
                    dialog.show();
                }
            }
        });



        trShipAgentCode=(TableRow)findViewById(R.id.trShipAgentCode);
        trShipAgentCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //GetSpinnerData("ShipAgentCode");
                if (statusField.equalsIgnoreCase("0")) {
                    final Dialog dialog = new Dialog(GoodDetailsActivity.this);
                    dialog.setCancelable(true);
                    dialog.setTitle("Select Name");
                    dialog.setContentView(R.layout.dialog_listview);


                    listView1 = (ListView) dialog.findViewById(R.id.listView1);
                    shippingAgentCodeAdapter = new ShippingAgentCodeAdapter(shippingAgentCodes, getApplicationContext());
                    listView1.setAdapter(shippingAgentCodeAdapter);
                    listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            dialog.dismiss();
                            shipping_Agent_Code = shippingAgentCodes.get(position).getCode();
                            tvShipingAgent.setText(shippingAgentCodes.get(position).getName());

                        }
                    });
                    dialog.show();
                }
            }
        });

        trStructure=(TableRow)findViewById(R.id.trStructure);
        trStructure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // GetSpinnerData("Structure");
                if (statusField.equalsIgnoreCase("0")) {
                    final Dialog dialog = new Dialog(GoodDetailsActivity.this);
                    dialog.setCancelable(true);
                    dialog.setTitle("Select Name");
                    dialog.setContentView(R.layout.dialog_listview);

                    listView1 = (ListView) dialog.findViewById(R.id.listView1);
                    structureAdapter = new StructureAdapter(structures, getApplicationContext());
                    listView1.setAdapter(structureAdapter);
                    listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            dialog.dismiss();
                            structureField = structures.get(position).getCode();
                            tvStructure.setText(structures.get(position).getCode());

                        }
                    });
                    dialog.show();
                }
            }
        });

        trTransferType=(TableRow)findViewById(R.id.trTransferType);
        trTransferType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (statusField.equalsIgnoreCase("0")) {
                    final Dialog dialog = new Dialog(GoodDetailsActivity.this);
                    dialog.setCancelable(true);
                    dialog.setTitle("Select Name");
                    dialog.setContentView(R.layout.dialog_listview);

                    listView1 = (ListView) dialog.findViewById(R.id.listView1);
                    transferOrderTransferTypeAdapter = new TransferOrderTransferTypeAdapter(transferOrderTransferTypes, getApplicationContext());
                    listView1.setAdapter(transferOrderTransferTypeAdapter);
                    listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            transfer_TypeField = transferOrderTransferTypes.get(position).getValue();
                            tvTrasnferType.setText(transferOrderTransferTypes.get(position).getTexts());
                            dialog.dismiss();

                        }
                    });
                    dialog.show();
                }
            }
        });


        trInvoiceDate=(TableRow)findViewById(R.id.trInvoiceDate);
        trInvoiceDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (statusField.equalsIgnoreCase("0")) {
                    DateDialog("InvoiceDate");
                }
            }
        });

        /*trETLDate=(TableRow)findViewById(R.id.trETLDate);
        trETLDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateDialog("ETLDate");
            }
        });*/



        trLLRRDate=(TableRow)findViewById(R.id.trLLRRDate);
        trLLRRDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (statusField.equalsIgnoreCase("0")) {
                    DateDialog("LLRRDate");
                }
            }
        });
    }



    private class UpdateApi extends CallManagerAsyncTask{

        JSONObject requestJSON = new JSONObject();

        JSONArray arrr = new JSONArray();

        String holdSatus = null;
        int position;
        String comment;
        String docketNo;

        public UpdateApi(String action, String reqType, Context context) {
            super(action, reqType, context);

        }


        @Override
        protected JSONObject doInBackground(Object... params) {
            //preferences    = (context).getSharedPreferences("CMS", Context.MODE_PRIVATE);
            //String userId = preferences.getString("userId" , null);
            try {

                requestJSON.put("key",keyField);
                requestJSON.put("structure",Utils.checkString(structureField));
                requestJSON.put("no",Utils.checkString(noField));
                requestJSON.put("transfer_From_Code",Utils.checkString(transfer_from_CodeField));
                requestJSON.put("transfer_To_Code",Utils.checkString(transfer_to_CodeField));
                requestJSON.put("transfer_Type",Utils.checkString(transfer_TypeField));
                requestJSON.put("vendor_Invoice_No",Utils.checkString(vendor_Invoice_NoField));
                requestJSON.put("vendor_Invoice_Date",vendor_Invoice_DateField);
                requestJSON.put("transported_by",Utils.checkString(transported_by));
                requestJSON.put("shipment_Method_Code",Utils.checkString(shipment_Method_Code));
                requestJSON.put("shipping_Agent_Code",Utils.checkString(shipping_Agent_Code));
                requestJSON.put("vehicle_No",Utils.checkString(vehicle_No));
                requestJSON.put("lR_RR_No",Utils.checkString(lR_RR_No));
                requestJSON.put("lR_RR_Date",lR_RR_DateField);
                requestJSON.put("transfer_Order_ETA",transfer_Order_ETAField);


                    for (int i=0;i<linesModels.size();i++) {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("key", linesModels.get(i).getKeyField());
                        jsonObject.put("reason_for_Partial_Receipt",Utils.checkString(linesModels.get(i).getReason_for_Partial_Receipt()));
                        jsonObject.put("Item_No",Utils.checkString(linesModels.get(i).getItem_NoField()));
                        jsonObject.put("quantity",Utils.checkQty(linesModels.get(i).getQuantityField()) );
                        jsonObject.put("ticket_No",Utils.checkString(linesModels.get(i).getTicket_NoField()) );
                        jsonObject.put("bank_Docket_No",Utils.checkString(linesModels.get(i).getBank_Docket_NoField()) );
                        jsonObject.put("resource",Utils.checkString(linesModels.get(i).getResourceField()) );
                        jsonObject.put("reason_for_Partial_Receipt",Utils.checkString(linesModels.get(i).getReason_for_Partial_Receipt()) );
                        jsonObject.put("qty_to_Ship",Utils.checkQty(linesModels.get(i).getQty_to_ShipField()) );
                        jsonObject.put("qty_to_Receive",Utils.checkQty(linesModels.get(i).getQty_to_ReceiveField()));

                        arrr.put(jsonObject);
                    }

                requestJSON.put("ListTransferOrdderLineForGood",arrr);


                Log.d("", "requestJSON is : "+requestJSON.toString());

                return doWorkPOST(requestJSON);
            } catch (ConnectException e) {
                e.printStackTrace();
            } catch (EOFException e) {
                e.printStackTrace();
            } catch (SocketTimeoutException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            super.onPostExecute(json);
           ProgressUtil.hideBar();
            if(json != null){
                try {

                    Utils.Log("JSON Response iss... "+json.toString());

                    if (json.has("Status") && json.get("Status").toString().equalsIgnoreCase("Failure"))
                    {
                        String msg =  json.get("ErrorMessage").toString();
                        //Toast.makeText(GoodDetailsActivity.this, msg, Toast.LENGTH_SHORT).show();
                        Utils.showAlertBox(msg, GoodDetailsActivity.this);

                    }
                    else if (json.has("Message"))
                    {
                        String msg =  json.get("Message").toString();
                        Utils.showAlertBox(msg, GoodDetailsActivity.this);
                    }
                    else if (json.has("Status") && json.get("Status").toString().equalsIgnoreCase("Success"))
                    {
                        String msg =  json.get("ErrorMessage").toString();


                        if (UpdateFlag.equalsIgnoreCase("update"))
                        {
                            Toast.makeText(GoodDetailsActivity.this, msg, Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(GoodDetailsActivity.this,GoodHubActivity.class));
                            finish();
                        }
                        else if (UpdateFlag.equalsIgnoreCase("release"))
                        {
                            ProgressUtil.ShowBar(GoodDetailsActivity.this);
                            Log.d("", "newString: "+newString);
                            ReleaseApi releaseApi = new ReleaseApi(  Constants.ReleaseDataGood+"?TransferOrderNo="+newString, "GET" ,
                                    GoodDetailsActivity.this);
                            releaseApi.execute();
                        }
                        else if (UpdateFlag.equalsIgnoreCase("post"))
                        {
                            RadioDialog();

                        }
                        else if (UpdateFlag.equalsIgnoreCase("save"))
                        {
                            Toast.makeText(GoodDetailsActivity.this, msg, Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(GoodDetailsActivity.this,GoodHubActivity.class));
                            finish();
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Utils.showAlertBox("JSONException : "+ e.getMessage(),GoodDetailsActivity.this);
                }

            }else {
                Utils.showAlertBox("Something Went wrong!!", GoodDetailsActivity.this);
            }
        }
    }


    private void RadioDialog() {
        LayoutInflater layoutInflater = (LayoutInflater)getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        final View popupView = layoutInflater.inflate(R.layout.dialog_radio_hub, null);
        final PopupWindow popupWindow = new PopupWindow(popupView,
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT, true);

        // popupWindow.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        // popupWindow.setTouchable(true);


        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);


        RadioButton radioShip = (RadioButton)popupView.findViewById(R.id.radioShip);
        RadioButton radioReceived = (RadioButton)popupView.findViewById(R.id.radioReceived);

        final RadioGroup rgPost = (RadioGroup)popupView.findViewById(R.id.rgPost);

        radioReceived.setChecked(true);
                /*for(int i=0;i<linesModels.size();i++)
                {
                    *//*if (linesModels.get(i).getQuantity_ShippedField() == "")
                        linesModels.get(i).setQty_to_ShipField("0");

                    if (linesModels.get(i).getQuantity_ReceivedField() == "")
                        linesModels.get(i).setQuantity_ReceivedField("0");*//*


                    if (Double.parseDouble(linesModels.get(i).getQuantity_ShippedField())
                            < Double.parseDouble(linesModels.get(i).getQuantityField()))
                    {
                        radioShip.setChecked(true);
                    }
                    else if (Double.parseDouble(linesModels.get(i).getQuantity_ReceivedField())
                        < Double.parseDouble(linesModels.get(i).getQuantityField()))
                    {
                        radioReceived.setChecked(true);
                    }
                    else
                    {
                        radioShip.setChecked(true);
                    }
                }*/

        popupView.findViewById(R.id.btn_Submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = rgPost.getCheckedRadioButtonId();

                // find the radiobutton by returned id
                RadioButton radioButton = (RadioButton) popupView.findViewById(selectedId);
                String num = "2";
                
                if (radioButton.getText().toString().equalsIgnoreCase("Ship"))
                {
                    num = "1";
                }

                else
                if (radioButton.getText().toString().equalsIgnoreCase("Received"))
                {
                    num = "2";
                }

                popupWindow.dismiss();
                ProgressUtil.ShowBar(GoodDetailsActivity.this);
                PostApi postApi = new PostApi(Constants.PostingDataGood+"?TransferOrderNo="+noField+"&ShipReceive="+num, "GET" ,
                        GoodDetailsActivity.this);
                postApi.execute();

            }
        });
    }

    public class PostApi extends CallManagerAsyncTask{

        JSONObject requestJSON = new JSONObject();


        public PostApi(String action, String reqType, Context context) {
            super(action, reqType, context);

        }


        @Override
        protected JSONObject doInBackground(Object... params) {
            //preferences    = (context).getSharedPreferences("CMS", Context.MODE_PRIVATE);
            //String userId = preferences.getString("userId" , null);
            try {
                return doWorkPOST(requestJSON);
            } catch (ConnectException e) {
                e.printStackTrace();
            } catch (EOFException e) {
                e.printStackTrace();
            } catch (SocketTimeoutException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            super.onPostExecute(json);
            ProgressUtil.hideBar();
            if(json != null){

                try {
                    Utils.Log("JSON Response iss... "+json.toString());

                    if (json.has("Status") && json.get("Status").toString().equalsIgnoreCase("Failure"))
                    {
                        String msg =  json.get("ErrorMessage").toString();
                        Utils.showAlertBox(msg, GoodDetailsActivity.this);
                    }
                    else if (json.has("Status") && json.get("Status").toString().equalsIgnoreCase("Success"))
                    {
                        String msg =  json.get("ErrorMessage").toString();
                        Toast.makeText(GoodDetailsActivity.this, msg, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(GoodDetailsActivity.this,GoodHubActivity.class));
                        finish();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Utils.showAlertBox(e.getMessage(),GoodDetailsActivity.this);
                }

            }else {
                Utils.showAlertBox("Something Went wrong!!", GoodDetailsActivity.this);
            }
        }
    }

    public class ReleaseApi extends CallManagerAsyncTask{

        JSONObject requestJSON = new JSONObject();


        public ReleaseApi(String action, String reqType, Context context) {
            super(action, reqType, context);

        }


        @Override
        protected JSONObject doInBackground(Object... params) {
            //preferences    = (context).getSharedPreferences("CMS", Context.MODE_PRIVATE);
            //String userId = preferences.getString("userId" , null);
            try {
                return doWorkPOST(requestJSON);
            } catch (ConnectException e) {
                e.printStackTrace();
            } catch (EOFException e) {
                e.printStackTrace();
            } catch (SocketTimeoutException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            super.onPostExecute(json);
            ProgressUtil.hideBar();
            if(json != null){

                try {
                    Utils.Log("JSON Response iss... "+json.toString());

                    //String msg =  json.get("ErrorMessage").toString();
                    if (json.has("Status") && json.get("Status").toString().equalsIgnoreCase("Failure"))
                    {
                        String msg =  json.get("ErrorMessage").toString();
                        Utils.showAlertBox(msg, GoodDetailsActivity.this);
                    }
                    else if (json.has("Status") && json.get("Status").toString().equalsIgnoreCase("Success"))
                    {
                        String msg =  json.get("ErrorMessage").toString();
                        Toast.makeText(GoodDetailsActivity.this, msg, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(GoodDetailsActivity.this,GoodHubActivity.class));
                        finish();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Utils.showAlertBox(e.getMessage(),GoodDetailsActivity.this);
                }

            }else {
                Utils.showAlertBox("Something Went wrong!!", GoodDetailsActivity.this);
            }
        }
    }

    public class ReopenApi extends CallManagerAsyncTask{

        JSONObject requestJSON = new JSONObject();


        public ReopenApi(String action, String reqType, Context context) {
            super(action, reqType, context);

        }


        @Override
        protected JSONObject doInBackground(Object... params) {
            //preferences    = (context).getSharedPreferences("CMS", Context.MODE_PRIVATE);
            //String userId = preferences.getString("userId" , null);
            try {
                return doWorkPOST(requestJSON);
            } catch (ConnectException e) {
                e.printStackTrace();
            } catch (EOFException e) {
                e.printStackTrace();
            } catch (SocketTimeoutException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            super.onPostExecute(json);
            ProgressUtil.hideBar();
            if(json != null){

                try {
                    Utils.Log("JSON Response iss... "+json.toString());

                    //String msg =  json.get("ErrorMessage").toString();
                    if (json.has("Status") && json.get("Status").toString().equalsIgnoreCase("Failure"))
                    {
                        String msg =  json.get("ErrorMessage").toString();
                        Utils.showAlertBox(msg, GoodDetailsActivity.this);
                    }
                    else if (json.has("Status") && json.get("Status").toString().equalsIgnoreCase("Success"))
                    {
                        String msg =  json.get("ErrorMessage").toString();
                        tvStatus.setText("Open");
                        statusField = "0";

                        if (statusField.equalsIgnoreCase("0"))
                        {
                            llPostReopen.setVisibility(View.GONE);
                            llReleaseUpdate.setVisibility(View.VISIBLE);
                           // Utils.disableEditText(etLLRRNo);
                           // Utils.disableEditText(etTransportedBy);
                           // Utils.disableEditText(etVendorInvoiceNum);
                           // Utils.disableEditText(etVehicleNo);
                        }


                        // for refrence
                        /*if(statusField.equalsIgnoreCase("0")) {
                            tvStatus.setText("Open");
                            llPostReopen.setVisibility(View.GONE);
                        }
                        else if (statusField.equalsIgnoreCase("0")) {
                            tvStatus.setText("Released");
                            llReleaseUpdate.setVisibility(View.GONE);
                        }*/


                        Utils.showAlertBox(msg, GoodDetailsActivity.this);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Utils.showAlertBox(e.getMessage(),GoodDetailsActivity.this);
                }

            }else {
                Utils.showAlertBox("Something Went wrong!!", GoodDetailsActivity.this);
            }
        }
    }

    private void DateDialog(final String flag) {
        int mYear, mMonth, mDay;
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(GoodDetailsActivity.this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        int month = monthOfYear + 1;
                        String fm = "" + month;
                        String fd = "" + dayOfMonth;
                        if (month < 10) {
                            fm = "0" + month;
                        }
                        if (dayOfMonth < 10) {
                            fd = "0" + dayOfMonth;
                        }

                        if (flag.equalsIgnoreCase("invoiceDate"))
                        {
                            vendor_Invoice_DateField = year + "-" + fm + "-" + fd;
                            tvVendorInvoiceDate.setText(Utils.ChangeDatePickerFormat(year + "-" + fm + "-" + fd));
                        }
                        else if (flag.equalsIgnoreCase("LLRRDate"))
                        {
                            lR_RR_DateField  = year + "-" + fm + "-" + fd;
                            tvLRDate.setText(Utils.ChangeDatePickerFormat(year + "-" + fm + "-" + fd));
                        }
                        else if (flag.equalsIgnoreCase("ETLDate"))
                        {
                            transfer_Order_ETAField = year + "-" + fm + "-" + fd;
                            tvOrderETA.setText(Utils.ChangeDatePickerFormat(year + "-" + fm + "-" + fd));
                        }
                    }
                }, mYear, mMonth, mDay);

        long now = System.currentTimeMillis() - 1000;
        datePickerDialog.getDatePicker().setMinDate(now);
        // datePickerDialog.getDatePicker().setMaxDate(now);
        datePickerDialog.show();
    }


    private class GoodNewAPI extends CallManagerAsyncTask {

        JSONObject requestObject = new JSONObject();

        public GoodNewAPI(String action, String reqType, Context context) {
            super(action, reqType, context);

        }

        @Override
        protected JSONObject doInBackground(Object... params) {

            try {
                return doWork(requestObject);
            } catch (ConnectException e) {
                e.printStackTrace();
                Log.d("", "doInBackground: " + e.getMessage());
            } catch (EOFException e) {
                e.printStackTrace();
                Log.d("", "doInBackground: " + e.getMessage());
            } catch (SocketTimeoutException e) {
                e.printStackTrace();
                Log.d("", "doInBackground: " + e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected void onPostExecute(JSONObject json) {
            super.onPostExecute(json);
            ProgressUtil.hideBar();
            if (json != null) {
                try {

                    Utils.Log("JSON Response========="+json.toString());
                    if (json.has("Status") && json.get("Status").toString().equalsIgnoreCase("Failure"))
                    {
                        Utils.showAlertBox(json.getString("ErrorMessage").toString(),GoodDetailsActivity.this);
                    }
                    else {
                        keyField = json.get("keyField").toString();
                        vendor_Invoice_NoField = json.get("vendor_Invoice_NoField").toString();
                        if (Utils.checkStringIsEmpty(vendor_Invoice_NoField))
                            etVendorInvoiceNum.setText(vendor_Invoice_NoField);


                        vendor_Invoice_DateField = json.get("vendor_Invoice_DateField").toString();
                        if (Utils.checkStringIsEmpty(vendor_Invoice_DateField))
                            tvVendorInvoiceDate.setText(Utils.ChangeDateFormat(vendor_Invoice_DateField));

                        transported_by = json.get("transported_byField").toString();
                        if (Utils.checkStringIsEmpty(transported_by))
                            etTransportedBy.setText(transported_by);

                        shipment_Method_Code = json.get("shipment_Method_CodeField").toString();
                        if (shipment_Method_Code.equalsIgnoreCase("null"))
                            shipment_Method_Code = "";
                        //tvShipMethodCode.setText(shipment_Method_Code);

                        shipping_Agent_Code = json.get("shipping_Agent_CodeField").toString();
                        if (shipping_Agent_Code.equalsIgnoreCase("null"))
                            shipping_Agent_Code = "";

                        Log.d("", "shipping_Agent_Code: " + shipping_Agent_Code);
                        // tvShipingAgent.setText(shipping_Agent_Code);


                        vehicle_No = json.get("vehicle_NoField").toString();
                        if (Utils.checkStringIsEmpty(vehicle_No))
                            etVehicleNo.setText(vehicle_No);

                        lR_RR_No = json.get("lR_RR_NoField").toString();
                        if (Utils.checkStringIsEmpty(lR_RR_No))
                            etLLRRNo.setText(lR_RR_No);

                        lR_RR_DateField = json.get("lR_RR_DateField").toString();
                        if (Utils.checkStringIsEmpty(lR_RR_DateField))
                            tvLRDate.setText(Utils.ChangeDateFormat(lR_RR_DateField));


                            /*transfer_Order_ETAField =  json.get("transfer_Order_ETAField").toString();
                            if (Utils.checkStringIsEmpty(transfer_Order_ETAField))
                                tvOrderETA.setText(Utils.ChangeDateTimeFormat(transfer_Order_ETAField));*/

                        transfer_from_CodeField = json.get("transfer_from_CodeField").toString();
                        if (Utils.checkStringIsEmpty(transfer_from_CodeField))
                            tvFromCode.setText(transfer_from_CodeField);

                        noField = json.get("noField").toString();
                        if (Utils.checkStringIsEmpty(noField))
                            tvNoField.setText(noField);


                        transfer_from_Name_1Field = json.get("transfer_from_Name_1Field").toString();
                        if (Utils.checkStringIsEmpty(transfer_from_Name_1Field))
                            tvFromName.setText(transfer_from_Name_1Field);


                        transfer_to_CodeField = json.get("transfer_to_CodeField").toString();
                        if (Utils.checkStringIsEmpty(transfer_to_CodeField))
                            tvToCode.setText(transfer_to_CodeField);


                        transfer_to_Name_1Field = json.get("transfer_to_Name_1Field").toString();
                        if (Utils.checkStringIsEmpty(transfer_to_Name_1Field))
                            tvToName.setText(transfer_to_Name_1Field);


                        in_Transit_CodeField = json.get("in_Transit_CodeField").toString();
                        if (Utils.checkStringIsEmpty(in_Transit_CodeField))
                            tvInTrasit.setText(in_Transit_CodeField);


                        posting_DateField = json.get("posting_DateField").toString();
                        if (Utils.checkStringIsEmpty(posting_DateField))
                            tvPostingDate.setText(Utils.ChangeDateFormat(posting_DateField));

                            /*pickList_NoField = json.get("pickList_NoField").toString();
                            if(Utils.checkStringIsEmpty(pickList_NoField))
                                tvPiackListNo.setText(pickList_NoField);*/


                        hub_DimField = json.get("hub_DimField").toString();
                        if (Utils.checkStringIsEmpty(hub_DimField))
                            tvHubDim.setText(hub_DimField);


                        BusinessUnit = json.get("shortcut_Dimension_1_CodeField").toString();
                        if (Utils.checkStringIsEmpty(BusinessUnit))
                            tvBusinessUnit.setText(BusinessUnit);

                        SubBusinessUnit = json.get("shortcut_Dimension_2_CodeField").toString();
                        if (Utils.checkStringIsEmpty(SubBusinessUnit))
                            tvSubBusiness.setText(SubBusinessUnit);


                        structureField = json.get("structureField").toString();
                        if (Utils.checkStringIsEmpty(structureField))
                            tvStructure.setText(structureField);


                        transfer_TypeField = json.get("transfer_TypeField").toString();
                        if (Utils.checkStringIsEmpty(transfer_TypeField))
                            //tvTrasnferType.setText(transfer_TypeField);

                            statusField = json.get("statusField").toString();
                        if (statusField.equalsIgnoreCase("0")) {
                            tvStatus.setText("Open");
                            llPostReopen.setVisibility(View.GONE);
                        } else if (statusField.equalsIgnoreCase("0")) {
                            tvStatus.setText("Released");
                            llReleaseUpdate.setVisibility(View.GONE);
                        }

                        linesModels = new ArrayList<>();


                        /*JSONArray jsonArray = json.getJSONArray("transferLinesField");




                        for (int i=0;i<jsonArray.length();i++)
                        {
                            linesModels.add(new InventoryLineModel(
                                    jsonArray.getJSONObject(i).getString("keyField"),
                                    jsonArray.getJSONObject(i).getString("item_NoField"),
                                    jsonArray.getJSONObject(i).getString("descriptionField"),
                                    jsonArray.getJSONObject(i).getString("quantityField"),
                                    jsonArray.getJSONObject(i).getString("unit_of_MeasureField"),
                                    jsonArray.getJSONObject(i).getString("qty_to_ShipField"),
                                    jsonArray.getJSONObject(i).getString("quantity_ShippedField"),
                                    jsonArray.getJSONObject(i).getString("qty_to_ReceiveField"),
                                    jsonArray.getJSONObject(i).getString("quantity_ReceivedField"),
                                    jsonArray.getJSONObject(i).getString("reason_for_Partial_ReceiptField"),
                                    jsonArray.getJSONObject(i).getString("ticket_NoField"),
                                    jsonArray.getJSONObject(i).getString("bank_Docket_NoField"),
                                    jsonArray.getJSONObject(i).getString("resourceField"),
                                    jsonArray.getJSONObject(i).getString("line_NoField")

                            ));
                        }

                        linesAdapter=new LinesAdapter(linesModels, getApplicationContext());
                        rv_line.setAdapter(linesAdapter);*/

                        APIFLAG = "TransferType";
                        // ProgressUtil.ShowBar(GoodDetailsActivity.this);
                        GetSpinnerApi transferFromCodeCallAsyncTask = new GetSpinnerApi(Constants.TransferOrderTransferType+"?UserId="+userId, "GET",
                                GoodDetailsActivity.this);
                        transferFromCodeCallAsyncTask.execute();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Utils.showAlertBox("JSONException : " + e.getMessage(), GoodDetailsActivity.this);
                }
            } else {
                Utils.showAlertBox("Something Went wrong!!", GoodDetailsActivity.this);
            }
        }
    }




    private class GoodDetailsAPI extends CallManagerAsyncTask {

        JSONObject requestObject = new JSONObject();

        public GoodDetailsAPI(String action, String reqType, Context context) {
            super(action, reqType, context);

        }

        @Override
        protected JSONObject doInBackground(Object... params) {

            try {
                return doWork(requestObject);
            } catch (ConnectException e) {
                e.printStackTrace();
                Log.d("", "doInBackground: " + e.getMessage());
            } catch (EOFException e) {
                e.printStackTrace();
                Log.d("", "doInBackground: " + e.getMessage());
            } catch (SocketTimeoutException e) {
                e.printStackTrace();
                Log.d("", "doInBackground: " + e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected void onPostExecute(JSONObject json) {
            super.onPostExecute(json);
            ProgressUtil.hideBar();
            if (json != null) {
                if (json != null) {
                    try {

                        Utils.Log("JSON Response=== "+json.toString());


                        if (json.has("Status") && json.get("Status").toString().equalsIgnoreCase("Failure"))
                        {
                            Utils.showAlertBox(json.getString("ErrorMessage").toString(),GoodDetailsActivity.this);
                        }
                        else
                        {
                            keyField =  json.get("keyField").toString();
                            vendor_Invoice_NoField = json.get("vendor_Invoice_NoField").toString();
                            if(Utils.checkStringIsEmpty(vendor_Invoice_NoField))
                                etVendorInvoiceNum.setText(vendor_Invoice_NoField);


                            vendor_Invoice_DateField = json.get("vendor_Invoice_DateField").toString();
                            if(Utils.checkStringIsEmpty(vendor_Invoice_DateField))
                                tvVendorInvoiceDate.setText(Utils.ChangeDateFormat(vendor_Invoice_DateField));

                            transported_by = json.get("transported_byField").toString();
                            if (Utils.checkStringIsEmpty(transported_by))
                                etTransportedBy.setText(transported_by);

                            shipment_Method_Code = json.get("shipment_Method_CodeField").toString();
                            if (shipment_Method_Code.equalsIgnoreCase("null"))
                                shipment_Method_Code = "";
                            //tvShipMethodCode.setText(shipment_Method_Code);

                            shipping_Agent_Code = json.get("shipping_Agent_CodeField").toString();
                            if (shipping_Agent_Code.equalsIgnoreCase("null"))
                                shipping_Agent_Code = "";

                            Log.d("", "shipping_Agent_Code: "+shipping_Agent_Code);
                            // tvShipingAgent.setText(shipping_Agent_Code);


                            vehicle_No = json.get("vehicle_NoField").toString();
                            if(Utils.checkStringIsEmpty(vehicle_No))
                                etVehicleNo.setText(vehicle_No);

                            lR_RR_No = json.get("lR_RR_NoField").toString();
                            if(Utils.checkStringIsEmpty(lR_RR_No))
                                etLLRRNo.setText(lR_RR_No);

                            lR_RR_DateField = json.get("lR_RR_DateField").toString();
                            if (Utils.checkStringIsEmpty(lR_RR_DateField))
                                tvLRDate.setText(Utils.ChangeDateFormat(lR_RR_DateField));


                            /*transfer_Order_ETAField =  json.get("transfer_Order_ETAField").toString();
                            if (Utils.checkStringIsEmpty(transfer_Order_ETAField))
                                tvOrderETA.setText(Utils.ChangeDateTimeFormat(transfer_Order_ETAField));*/

                            transfer_from_CodeField =  json.get("transfer_from_CodeField").toString();
                            if(Utils.checkStringIsEmpty(transfer_from_CodeField))
                                tvFromCode.setText(transfer_from_CodeField);

                            noField =  json.get("noField").toString();
                            if(Utils.checkStringIsEmpty(noField))
                                tvNoField.setText(noField);


                            transfer_from_Name_1Field=json.get("transfer_from_Name_1Field").toString();
                            if(Utils.checkStringIsEmpty(transfer_from_Name_1Field))
                                tvFromName.setText(transfer_from_Name_1Field);


                            transfer_to_CodeField = json.get("transfer_to_CodeField").toString();
                            if(Utils.checkStringIsEmpty(transfer_to_CodeField))
                                tvToCode.setText(transfer_to_CodeField);


                            transfer_to_Name_1Field = json.get("transfer_to_Name_1Field").toString();
                            if(Utils.checkStringIsEmpty(transfer_to_Name_1Field))
                                tvToName.setText(transfer_to_Name_1Field);


                            in_Transit_CodeField = json.get("in_Transit_CodeField").toString();
                            if(Utils.checkStringIsEmpty(in_Transit_CodeField))
                                tvInTrasit.setText(in_Transit_CodeField);


                            posting_DateField = json.get("posting_DateField").toString();
                            if (Utils.checkStringIsEmpty(posting_DateField))
                                tvPostingDate.setText(Utils.ChangeDateFormat(posting_DateField));

                            /*pickList_NoField = json.get("pickList_NoField").toString();
                            if(Utils.checkStringIsEmpty(pickList_NoField))
                                tvPiackListNo.setText(pickList_NoField);*/


                            hub_DimField = json.get("hub_DimField").toString();
                            if(Utils.checkStringIsEmpty(hub_DimField))
                                tvHubDim.setText(hub_DimField);



                            BusinessUnit = json.get("shortcut_Dimension_1_CodeField").toString();
                            if (Utils.checkStringIsEmpty(BusinessUnit))
                                tvBusinessUnit.setText(BusinessUnit);

                            SubBusinessUnit = json.get("shortcut_Dimension_2_CodeField").toString();
                            if(Utils.checkStringIsEmpty(SubBusinessUnit))
                                tvSubBusiness.setText(SubBusinessUnit);


                            structureField = json.get("structureField").toString();
                            if (Utils.checkStringIsEmpty(structureField))
                                tvStructure.setText(structureField);



                            transfer_TypeField = json.get("transfer_TypeField").toString();
                            if (Utils.checkStringIsEmpty(transfer_TypeField))
                                //tvTrasnferType.setText(transfer_TypeField);

                                statusField = json.get("statusField").toString();
                            if(statusField.equalsIgnoreCase("0")) {
                                tvStatus.setText("Open");
                                llPostReopen.setVisibility(View.GONE);
                            }
                            else if (statusField.equalsIgnoreCase("1")) {
                                tvStatus.setText("Released");
                                llReleaseUpdate.setVisibility(View.GONE);
                            }


                            JSONArray jsonArray = json.getJSONArray("transferLinesField");


                            linesModels = new ArrayList<>();

                            for (int i=0;i<jsonArray.length();i++)
                            {
                                linesModels.add(new InventoryLineModel(
                                        jsonArray.getJSONObject(i).getString("keyField"),
                                        jsonArray.getJSONObject(i).getString("item_NoField"),
                                        jsonArray.getJSONObject(i).getString("descriptionField"),
                                        jsonArray.getJSONObject(i).getString("quantityField"),
                                        jsonArray.getJSONObject(i).getString("unit_of_MeasureField"),
                                        jsonArray.getJSONObject(i).getString("qty_to_ShipField"),
                                        jsonArray.getJSONObject(i).getString("quantity_ShippedField"),
                                        jsonArray.getJSONObject(i).getString("qty_to_ReceiveField"),
                                        jsonArray.getJSONObject(i).getString("quantity_ReceivedField"),
                                        jsonArray.getJSONObject(i).getString("reason_for_Partial_ReceiptField"),
                                        jsonArray.getJSONObject(i).getString("ticket_NoField"),
                                        jsonArray.getJSONObject(i).getString("bank_Docket_NoField"),
                                        jsonArray.getJSONObject(i).getString("resourceField"),
                                        jsonArray.getJSONObject(i).getString("line_NoField")

                                ));
                            }

                            linesAdapter=new LinesAdapter(linesModels, getApplicationContext());
                            rv_line.setAdapter(linesAdapter);

                            APIFLAG = "TransferType";
                            // ProgressUtil.ShowBar(GoodDetailsActivity.this);
                            GetSpinnerApi transferFromCodeCallAsyncTask = new GetSpinnerApi(Constants.TransferOrderTransferType+"?UserId="+userId, "GET" ,
                                    GoodDetailsActivity.this);
                            transferFromCodeCallAsyncTask.execute();
                        }




                    } catch (JSONException e) {
                        e.printStackTrace();
                        Utils.showAlertBox("JSONException : " + e.getMessage(), GoodDetailsActivity.this);
                    }
                } else {
                    Toast.makeText(GoodDetailsActivity.this, "Something Went wrong!!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(GoodDetailsActivity.this,GoodHubActivity.class));
                    finish();
                    //Utils.showAlertBox("Something Went wrong!!", GoodDetailsActivity.this);
                }
            } else {
                Toast.makeText(GoodDetailsActivity.this, "Something Went wrong!!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(GoodDetailsActivity.this,GoodHubActivity.class));
                finish();
                //Utils.showAlertBox("Something Went wrong!!", GoodDetailsActivity.this);
            }
        }
    }


    private class LinesAdapter extends RecyclerView.Adapter<LinesAdapter.MyViewHolder> {
        ArrayList<InventoryLineModel> linesModels;
        Context context;

        public LinesAdapter(ArrayList<InventoryLineModel> linesModels, Context context) {
            this.linesModels = linesModels;
            this.context = context;
        }

        @Override
        public LinesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lines_short, parent, false);

            return new LinesAdapter.MyViewHolder(itemView);
        }

        @Override
        public int getItemCount() {
            return linesModels == null ? 0 : linesModels.size();
        }

        @Override
        public void onBindViewHolder(final LinesAdapter.MyViewHolder holder, final int position) {

            InventoryLineModel linesModel = linesModels.get(position);

            holder.tvName.setText(linesModel.getItem_NoField());
            holder.tvDesc.setText(linesModel.getDescriptionField());
            if(Utils.checkStringIsEmpty(linesModel.getTicket_NoField()))
                holder.tvTicket.setText(linesModel.getTicket_NoField());
            holder.tvQty.setText(linesModel.getQuantityField());
            holder.tvShipped.setText(linesModel.getQuantity_ShippedField());
            holder.tvReceived.setText(linesModel.getQuantity_ReceivedField());



            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Index = position;
                    lineDetailDialog(linesModels.get(position));

                }
            });


            /*
            if(Utils.checkStringIsEmpty(linesModel.getUnit_of_MeasureField()))
                holder.tvUom.setText(linesModel.getUnit_of_MeasureField());





            if(Utils.checkStringIsEmpty(linesModel.getBank_Docket_NoField()))
                holder.tvBankNo.setText(linesModel.getBank_Docket_NoField());

            if(Utils.checkStringIsEmpty(linesModel.getResourceField()))
                holder.tvEngCode.setText(linesModel.getResourceField());


            if(Utils.checkStringIsEmpty(linesModel.getQty_to_ShipField()))
                holder.etQtyToShip.setText(linesModel.getQty_to_ShipField());

            if(Utils.checkStringIsEmpty(linesModel.getQty_to_ReceiveField()))
                holder.etReceive.setText(linesModel.getQty_to_ReceiveField());

            if(Utils.checkStringIsEmpty(linesModel.getReason_for_Partial_Receipt()))
                holder.etReason.setText(linesModel.getReason_for_Partial_Receipt());


            holder.etReason.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    // if (s.toString().length() > 0)
                    linesModels.get(position).setReason_for_Partial_Receipt(s.toString());

                }
            });

            holder.etQtyToShip.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {

                        linesModels.get(position).setQty_to_ShipField(s.toString());
                   // Toast.makeText(GoodDetailsActivity.this, s.toString() +"  "+linesModels.get(position).getQty_to_ShipField(), Toast.LENGTH_SHORT).show();

                }
            });

            holder.etReceive.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {

                    if (s.toString().length() > 0)
                        linesModels.get(position).setQty_to_ReceiveField(s.toString());

                }
            });*/

        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            private TextView tvName, tvQty, tvDesc,tvUom,tvShipped,tvReceived,tvTicket,tvBankNo,tvEngCode;
            private ImageView ivEdit;
            private EditText etQtyShipped,etReceivedQty,etQtyToShip,etReceive,etReason;

            public MyViewHolder(View view) {
                super(view);
                tvName = (TextView) view.findViewById(R.id.tvName);
                tvDesc = (TextView)view.findViewById(R.id.tvDesc);
                tvTicket = (TextView)view.findViewById(R.id.tvTicket);
                tvQty = (TextView) view.findViewById(R.id.tvQty);
                tvShipped = (TextView)view.findViewById(R.id.tvShipped);
                tvReceived = (TextView)view.findViewById(R.id.tvReceived);
                /*ivEdit = (ImageView) view.findViewById(R.id.ivEdit);



                tvUom = (TextView)view.findViewById(R.id.tvUom);


                tvBankNo = (TextView)view.findViewById(R.id.tvBankNo);
                tvEngCode = (TextView)view.findViewById(R.id.tvEngCode);


                etQtyToShip = (EditText)view.findViewById(R.id.etQtyToShip);
                etReceive = (EditText)view.findViewById(R.id.etReceive);
                etReason = (EditText)view.findViewById(R.id.etReason);*/



            }
        }
    }


    public void lineDetailDialog(final InventoryLineModel inventoryLineModel) {

        LayoutInflater layoutInflater = (LayoutInflater)getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        final View popupView = layoutInflater.inflate(R.layout.item_lines_good, null);
        final PopupWindow popupWindow = new PopupWindow(popupView,
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT, true);


        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);
       // popupWindow.setTouchable(false);
        //popupWindow.setOutsideTouchable(true);

         img_arrow_spinner=(ImageView)popupView.findViewById(R.id.img_arrow_spinner);
        /* if(inventoryLineModel.getItem_NoField().equalsIgnoreCase("")){
             img_arrow_spinner.setVisibility(View.GONE);
         }else{
             img_arrow_spinner.setVisibility(View.VISIBLE);
         }*/



        itemCodeText = (TextView) popupView.findViewById(R.id.tvName);
        itemCodeText.setText(inventoryLineModel.getItem_NoField());

        tvDescText = (TextView)popupView.findViewById(R.id.tvDesc);
        final EditText etTicket = (EditText) popupView.findViewById(R.id.etTicket);
        final EditText tvQty = (EditText) popupView.findViewById(R.id.tvQty);
        TextView tvShipped = (TextView)popupView.findViewById(R.id.tvShipped);
        TextView tvReceived = (TextView)popupView.findViewById(R.id.tvReceived);
        tvUomText = (TextView)popupView.findViewById(R.id.tvUom);
        final EditText etBankNo = (EditText) popupView.findViewById(R.id.etBankNo);
        tvEngCodeText = (TextView)popupView.findViewById(R.id.tvEngCode);
       // img_arrow_spinner=(ImageView)popupView.findViewById(R.id.img_arrow_spinner);
        final EditText etQtyToShip = (EditText)popupView.findViewById(R.id.etQtyToShip);
        final EditText etReceive = (EditText)popupView.findViewById(R.id.etReceive);
        final EditText etReason = (EditText)popupView.findViewById(R.id.etReason);

       // popupView.findViewById(R.id.trItemCode).setClickable(true);


        tvDescText.setText(inventoryLineModel.getDescriptionField());
        if(Utils.checkStringIsEmpty(inventoryLineModel.getTicket_NoField()))
            etTicket.setText(inventoryLineModel.getTicket_NoField());
        tvQty.setText(inventoryLineModel.getQuantityField());
        tvShipped.setText(inventoryLineModel.getQuantity_ShippedField());
        tvReceived.setText(inventoryLineModel.getQuantity_ReceivedField());



        if (!statusField.equalsIgnoreCase("0"))
        {
            Utils.disableEditText(etBankNo);
            Utils.disableEditText(tvQty);
            Utils.disableEditText(etQtyToShip);
            Utils.disableEditText(etReceive);
            Utils.disableEditText(etReason);
            Utils.disableEditText(etTicket);

        }

        if(Utils.checkStringIsEmpty(inventoryLineModel.getUnit_of_MeasureField()))
            tvUomText.setText(inventoryLineModel.getUnit_of_MeasureField());


        if(Utils.checkStringIsEmpty(inventoryLineModel.getBank_Docket_NoField()))
            etBankNo.setText(inventoryLineModel.getBank_Docket_NoField());

/*
                if(Utils.checkStringIsEmpty(userId))
                    tvEngCodeText.setText(userId +"-"+name);*/





       /*  if(userid_name.equalsIgnoreCase(inventoryLineModel.getResourceField())){
             popupView.findViewById(R.id.img_arrow_spinner).setVisibility(View.GONE);
         }
*/

/*
         if (Utils.checkStringIsEmpty(inventoryLineModel.getResourceField())) //{
             tvEngCodeText.setText(inventoryLineModel.getResourceField());
          */

        if(Utils.checkStringIsEmpty(inventoryLineModel.getResourceField())){
            tvEngCodeText.setText(inventoryLineModel.getResourceField());
            popupView.findViewById(R.id.img_arrow_spinner).setVisibility(View.GONE);

        }else {
            popupView.findViewById(R.id.img_arrow_spinner).setVisibility(View.GONE);
        }







        if(Utils.checkStringIsEmpty(inventoryLineModel.getQty_to_ShipField()))
            etQtyToShip.setText(inventoryLineModel.getQty_to_ShipField());

        if(Utils.checkStringIsEmpty(inventoryLineModel.getQty_to_ReceiveField()))
            etReceive.setText(inventoryLineModel.getQty_to_ReceiveField());

        if(Utils.checkStringIsEmpty(inventoryLineModel.getReason_for_Partial_Receipt()))
            etReason.setText(inventoryLineModel.getReason_for_Partial_Receipt());


        popupView.findViewById(R.id.trItemCode).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (statusField.equalsIgnoreCase("0")) {
                    APIFLAG = "ItemCode";
                    // ProgressUtil.ShowBar(GoodDetailsActivity.this);
                    GetSpinnerApi transferFromCodeCallAsyncTask = new GetSpinnerApi(Constants.LineItemCode+"&UserId="+userId, "GET",
                            GoodDetailsActivity.this);
                    transferFromCodeCallAsyncTask.execute();
                }
            }
        });


        img_arrow_spinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (statusField.equalsIgnoreCase("0")) {
                    APIFLAG = "EngCode";
                    // ProgressUtil.ShowBar(GoodDetailsActivity.this);
                    GetSpinnerApi transferFromCodeCallAsyncTask = new GetSpinnerApi(Constants.LineEngineerCode+"&UserId="+userId, "GET",
                            GoodDetailsActivity.this);
                    transferFromCodeCallAsyncTask.execute();
                }
            }
        });

        tvQty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String str = tvQty.getText().toString();
                if (str.isEmpty()) return;
                String str2 = Utils.perfectDecimal(str, 10, 2);
                if (!str2.equals(str)) {
                    tvQty.setText(str2);
                    int pos = tvQty.getText().length();
                    tvQty.setSelection(pos);
                }
            }
        });

        /*etReason.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // if (s.toString().length() > 0)
                inventoryLineModel.setReason_for_Partial_Receipt(s.toString());

            }
        });*/

        etQtyToShip.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }


            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String str = etQtyToShip.getText().toString();
                if (str.isEmpty()) return;
                String str2 = Utils.perfectDecimal(str, 10, 2);
                if (!str2.equals(str)) {
                    etQtyToShip.setText(str2);
                    int pos = etQtyToShip.getText().length();
                    etQtyToShip.setSelected(true);
                }
            }
        });

        etReceive.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String str = etReceive.getText().toString();
                if (str.isEmpty()) return;
                String str2 = Utils.perfectDecimal(str, 10, 2);
                if (!str2.equals(str)) {
                    etReceive.setText(str2);
                    int pos = etReceive.getText().length();
                    etReceive.setSelection(pos);
                }

            }
        });


        popupView.findViewById(R.id.btn_Delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                if (Utils.checkStringIsEmpty(inventoryLineModel.getLine_NoField()))
                {
                    if(Utils.isInternetOn(GoodDetailsActivity.this)){
                        ProgressUtil.ShowBar(GoodDetailsActivity.this);

                        DeleteLineForGoodBadAPI deleteLineForGoodBadAPI = new DeleteLineForGoodBadAPI(Constants.DeleteLineForGoodBad+"?No="+noField+"&LineNo="+inventoryLineModel.getLine_NoField(), "GET" ,
                                GoodDetailsActivity.this);
                        deleteLineForGoodBadAPI.execute();
                    }else{
                        Utils.showAlertBox("Please Connect to internet",GoodDetailsActivity.this);

                    }
                }
                else
                {
                    linesModels.remove(Index);
                    linesAdapter=new LinesAdapter(linesModels, getApplicationContext());
                    rv_line.setAdapter(linesAdapter);
                    llNum.setVisibility(View.VISIBLE);


                }

            }
        });


        popupView.findViewById(R.id.btn_Submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // for get code only
                String[] namesList = tvEngCodeText.getText().toString().split("-",0);
                inventoryLineModel.setResourceField(namesList [0]);
                inventoryLineModel.setTicket_NoField(etTicket.getText().toString());
                inventoryLineModel.setBank_Docket_NoField(etBankNo.getText().toString());
                inventoryLineModel.setQuantityField(tvQty.getText().toString());
                inventoryLineModel.setUnit_of_MeasureField(tvUomText.getText().toString());
                inventoryLineModel.setItem_NoField(itemCodeText.getText().toString());
                inventoryLineModel.setDescriptionField(tvDescText.getText().toString());
                inventoryLineModel.setQty_to_ShipField(etQtyToShip.getText().toString());
                inventoryLineModel.setReason_for_Partial_Receipt(etReason.getText().toString());
                inventoryLineModel.setQty_to_ReceiveField(etReceive.getText().toString());


                linesAdapter=new LinesAdapter(linesModels, getApplicationContext());
                rv_line.setAdapter(linesAdapter);
                popupWindow.dismiss();
            }
        });


        popupView.findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }


    private class DeleteLineForGoodBadAPI extends CallManagerAsyncTask {

        JSONObject requestObject = new JSONObject();

        public DeleteLineForGoodBadAPI(String action, String reqType, Context context) {
            super(action, reqType, context);

        }

        @Override
        protected JSONObject doInBackground(Object... params) {

            try {
                return doWork(requestObject);
            } catch (ConnectException e) {
                e.printStackTrace();
                Log.d("", "doInBackground: " + e.getMessage());
            } catch (EOFException e) {
                e.printStackTrace();
                Log.d("", "doInBackground: " + e.getMessage());
            } catch (SocketTimeoutException e) {
                e.printStackTrace();
                Log.d("", "doInBackground: " + e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected void onPostExecute(JSONObject json) {
            super.onPostExecute(json);
            String msg = null;
            ProgressUtil.hideBar();
            if (json != null) {
                try {

                    Utils.Log("JSON Response iss... "+json.toString());

                    if (json.has("Status") && json.get("Status").toString().equalsIgnoreCase("Failure"))
                    {
                        msg =  json.get("ErrorMessage").toString();
                        Utils.showAlertBox(msg, GoodDetailsActivity.this);
                    }
                    if (json.has("Message"))
                    {
                         msg =  json.get("Message").toString();
                        Utils.showAlertBox(msg, GoodDetailsActivity.this);
                    }
                    else if (json.has("Status") && json.get("Status").toString().equalsIgnoreCase("Success"))
                    {
                         msg =  json.get("ErrorMessage").toString();
                        Utils.showAlertBox(msg,GoodDetailsActivity.this);
                        if (msg.contains("Delete"))
                        {
                            linesModels.remove(Index);
                            linesAdapter=new LinesAdapter(linesModels, getApplicationContext());
                            rv_line.setAdapter(linesAdapter);
                            llNum.setVisibility(View.VISIBLE);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Utils.showAlertBox(e.getMessage(),GoodDetailsActivity.this);
                }
            } else {
                Toast.makeText(GoodDetailsActivity.this, msg, Toast.LENGTH_SHORT).show();
                startActivity(new Intent(GoodDetailsActivity.this,GoodHubActivity.class));
                finish();
                //Utils.showAlertBox("Something Went wrong!!", GoodDetailsActivity.this);
            }
        }
    }

    private class ItemWiseDataAPI extends CallManagerAsyncTaskArray {

        JSONArray requestArray = new JSONArray();
        String flag;

        public ItemWiseDataAPI(String action, String reqType, Context context) {
            super(action, reqType, context);

        }


        @Override
        protected JSONArray doInBackground(Object... params) {

            try {
                return doWorkJSONArray(requestArray);
            } catch (ConnectException e) {
                e.printStackTrace();
                Log.d("", "doInBackground: "+e.getMessage());
            } catch (EOFException e) {
                e.printStackTrace();
                Log.d("", "doInBackground: "+e.getMessage());
            } catch (SocketTimeoutException e) {
                e.printStackTrace();
                Log.d("", "doInBackground: "+e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected void onPostExecute(JSONArray json) {
            super.onPostExecute(json);
            ProgressUtil.hideBar();
            if(json != null){
                try {

                    itemCodeText.setText(json.getJSONObject(0).getString("No_"));
                    tvDescText.setText(json.getJSONObject(0).getString("Description"));
                    tvUomText.setText(json.getJSONObject(0).getString("Base_Unit_of_Measure"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else {
                Utils.showAlertBox("Something Went wrong!!",GoodDetailsActivity.this);
            }
        }
    }


    public class GetSpinnerApi extends CallManagerAsyncTaskArray {

        JSONArray requestArray = new JSONArray();
        String flag;

        public GetSpinnerApi(String action, String reqType, Context context) {
            super(action, reqType, context);

        }


        @Override
        protected JSONArray doInBackground(Object... params) {

            try {
                return doWorkJSONArray(requestArray);
            } catch (ConnectException e) {
                e.printStackTrace();
                Log.d("", "doInBackground: "+e.getMessage());
            } catch (EOFException e) {
                e.printStackTrace();
                Log.d("", "doInBackground: "+e.getMessage());
            } catch (SocketTimeoutException e) {
                e.printStackTrace();
                Log.d("", "doInBackground: "+e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected void onPostExecute(JSONArray json) {
            super.onPostExecute(json);
            //  ProgressUtil.hideBar();

            if(json != null){
                try {
                    JSONArray arr = json;

                    //list = new ArrayList<>();

                    if (APIFLAG.equalsIgnoreCase("TransferType"))
                    {
                        transferOrderTransferTypes = new ArrayList<>();
                        for(int i = 0; i < arr.length(); i++){
                            if(arr.getJSONObject(i).has("Value") && arr.getJSONObject(i).has("Texts")) {

                                //  if (Utils.checkStringIsEmpty(arr.getJSONObject(i).getString("Value")))
                                // {
                                if (transfer_TypeField != null && transfer_TypeField.equalsIgnoreCase(arr.getJSONObject(i).getString("Value")))
                                {
                                    tvTrasnferType.setText(arr.getJSONObject(i).getString("Texts"));
                                }
                                // }

                                transferOrderTransferTypes.add(new TransferOrderTransferType(
                                        arr.getJSONObject(i).getString("Value"),
                                        arr.getJSONObject(i).getString("Texts")));

                            }


                        }

                        APIFLAG = "Structure";
                        //  ProgressUtil.ShowBar(GoodDetailsActivity.this);
                        GetSpinnerApi transferFromCodeCallAsyncTask = new GetSpinnerApi(Constants.Structure+"&UserId="+userId, "GET" ,
                                GoodDetailsActivity.this);
                        transferFromCodeCallAsyncTask.execute();
                    }
                    else if (APIFLAG.equalsIgnoreCase("Structure"))
                    {

                        structures = new ArrayList<>();
                        //  structures.add(0,new Structure("","None"));
                        for(int i = 0; i < arr.length(); i++){

                            // if (!Utils.checkStringIsEmpty(arr.getJSONObject(i).getString("Code"))
                            if (arr.getJSONObject(i).getString("Code").equalsIgnoreCase(structureField))
                            {
                                tvStructure.setText(arr.getJSONObject(i).getString("Code"));
                            }

                            structures.add(new Structure(arr.getJSONObject(i).getString("Code"),
                                    arr.getJSONObject(i).getString("Description")));

                        }

                        APIFLAG = "ShipmentMehodCode";
                        // ProgressUtil.ShowBar(GoodDetailsActivity.this);
                        GetSpinnerApi transferFromCodeCallAsyncTask = new GetSpinnerApi(Constants.ShippingMethodCode+"&UserId="+userId, "GET" ,
                                GoodDetailsActivity.this);
                        transferFromCodeCallAsyncTask.execute();

                    }
                    else if (APIFLAG.equalsIgnoreCase("ShipmentMehodCode"))
                    {
                        shippingMethodCodes = new ArrayList<>();
                        for(int i = 0; i < arr.length(); i++){

                            if (arr.getJSONObject(i).getString("Code").equalsIgnoreCase(shipment_Method_Code))
                            {
                                tvShipMethodCode.setText(arr.getJSONObject(i).getString("Description"));
                            }

                            shippingMethodCodes.add(new ShippingMethodCode(arr.getJSONObject(i).getString("Code"),
                                    arr.getJSONObject(i).getString("Description")));

                        }

                        APIFLAG = "ShipAgentCode";
                        // ProgressUtil.ShowBar(GoodDetailsActivity.this);
                        GetSpinnerApi transferFromCodeCallAsyncTask = new GetSpinnerApi(Constants.ShippingAgentCode+"&UserId="+userId, "GET" ,
                                GoodDetailsActivity.this);
                        transferFromCodeCallAsyncTask.execute();
                    }
                    else if (APIFLAG.equalsIgnoreCase("ShipAgentCode"))
                    {
                        shippingAgentCodes = new ArrayList<>();
                        for(int i = 0; i < arr.length(); i++){
                            if (shipping_Agent_Code.equalsIgnoreCase(arr.getJSONObject(i).getString("Code")))
                            {
                                tvShipingAgent.setText(arr.getJSONObject(i).getString("Name"));
                            }

                            shippingAgentCodes.add(new ShippingAgentCode(arr.getJSONObject(i).getString("Code"),
                                    arr.getJSONObject(i).getString("Name")));

                        }
                    }
                    else if (APIFLAG.equalsIgnoreCase("FromCode"))
                    {
                        codeAndName = new ArrayList<>();
                        for(int i = 0; i < arr.length(); i++){

                            if (arr.getJSONObject(i).has("Code") && arr.getJSONObject(i).has("Name")) {
                                codeAndName.add(new CodeAndNameModel(arr.getJSONObject(i).getString("Code"),
                                        arr.getJSONObject(i).getString("Name_2"),
                                        arr.getJSONObject(i).getString("Name")));
                            }
                        }
                        listDialog(codeAndName);
                    }
                    else if (APIFLAG.equalsIgnoreCase("ToCode"))
                    {
                        codeAndName = new ArrayList<>();
                        for(int i = 0; i < arr.length(); i++){

                            if (arr.getJSONObject(i).has("Code") && arr.getJSONObject(i).has("Name")) {
                                codeAndName.add(new CodeAndNameModel(arr.getJSONObject(i).getString("Code"),
                                        arr.getJSONObject(i).getString("Name_2"),
                                        arr.getJSONObject(i).getString("Name")));
                            }
                        }
                        listDialog(codeAndName);
                    }
                    else if (APIFLAG.equalsIgnoreCase("ItemCode"))
                    {
                        codeAndName = new ArrayList<>();
                        for(int i = 0; i < arr.length(); i++){

                            if (arr.getJSONObject(i).has("Code1") && arr.getJSONObject(i).has("No_")) {
                                codeAndName.add(new CodeAndNameModel(arr.getJSONObject(i).getString("No_"),
                                        arr.getJSONObject(i).getString("Code1")));
                            }
                        }
                        listDialog(codeAndName);
                    }
                    else if (APIFLAG.equalsIgnoreCase("EngCode"))
                    {
                        codeAndName = new ArrayList<>();
                        for(int i = 0; i < arr.length(); i++){

                           // if (arr.getJSONObject(i).has("No_") && arr.getJSONObject(i).has("Name"))

                            if (arr.getJSONObject(i).has("No_") && arr.getJSONObject(i).has("Name"))                            {
                                codeAndName.add(new CodeAndNameModel(arr.getJSONObject(i).getString("No_"),
                                        arr.getJSONObject(i).getString("Name")));


                            }
                        }
                        listDialog(codeAndName);
                    }


                    // listDialog(list);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Utils.showAlertBox("JSONException : "+e.getMessage(),GoodDetailsActivity.this);
                }
            }else {
                Utils.showAlertBox("No record(s) to display.",GoodDetailsActivity.this);
            }
        }
    }


    private void listDialog(final ArrayList<CodeAndNameModel> list) {
        final Dialog dialog = new Dialog(GoodDetailsActivity.this);

        dialog.setCancelable(true);
        dialog.setTitle("Select Name");
        dialog.setContentView(R.layout.dialog_listview);

       /* Button btndialog = (Button) dialog.findViewById(R.id.btndialog);
        btndialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });*/
        listView1=(ListView)dialog.findViewById(R.id.listView1);
        EditText etSearch = (EditText)dialog.findViewById(R.id.etSearch);

        if (APIFLAG.equalsIgnoreCase("FromCode") ||
                APIFLAG.equalsIgnoreCase("ToCode") ||
                APIFLAG.equalsIgnoreCase("EngCode") ||
                APIFLAG.equalsIgnoreCase("ItemCode"))
        {
            etSearch.setVisibility(View.VISIBLE);
            nameCodeAdapter = new NameCodeAdapter(list,getApplicationContext());
            listView1.setAdapter(nameCodeAdapter);
        }
        else
        {
            adapter1= new CustomAdapter1(list,getApplicationContext());
            listView1.setAdapter(adapter1);
        }


        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count < before) {
                    // We're deleting char so we need to reset the adapter data
                    nameCodeAdapter.resetData();
                }

                nameCodeAdapter.getFilter().filter(s.toString());

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                CodeAndNameModel dataModel= list.get(position);

                 //  CodeAndNameModel

                dialog.dismiss();

                if (APIFLAG.equalsIgnoreCase("FromCode"))
                {
                    tvFromName.setText(dataModel.getName2());
                    tvFromCode.setText(dataModel.getCode());

                    transfer_from_CodeField = codeAndName.get(position).getCode();
                    transfer_from_Name_1Field = codeAndName.get(position).getName2();

                }else if (APIFLAG.equalsIgnoreCase("ToCode"))
                {
                    tvToCode.setText(dataModel.getCode());
                    tvToName.setText(dataModel.getName2());
                    transfer_to_CodeField = dataModel.getCode();
                    transfer_to_Name_1Field = dataModel.getName2();
                }
                else if (APIFLAG.equalsIgnoreCase("ItemCode"))
                {
                    if(Utils.isInternetOn(GoodDetailsActivity.this)){
                        ProgressUtil.ShowBar(GoodDetailsActivity.this);
                        ItemWiseDataAPI itemWiseDataAPI = new ItemWiseDataAPI(Constants.ItemWiseDataGood+"?ItemCode="+dataModel.getCode(), "GET" ,
                                GoodDetailsActivity.this);
                        itemWiseDataAPI.execute();
                    }else{
                        Utils.showAlertBox("Please Connect to internet",GoodDetailsActivity.this);

                    }
                }
                else if (APIFLAG.equalsIgnoreCase("EngCode"))
                {


                       tvEngCodeText.setText(dataModel.getCode() + "-" + dataModel.getName());

                          //tvEngCodeText.setText(userId + dataModel.getName());

                    userid_name = dataModel.getCode() + "-" + dataModel.getName();

                }

            }
        });


        dialog.show();

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent searchIntent = new Intent(GoodDetailsActivity.this , GoodHubActivity.class);
        startActivity(searchIntent);
        finish();
    }
}
