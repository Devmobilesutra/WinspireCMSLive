package com.cms.callmanager.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Selection;
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
import com.cms.callmanager.adapter.ShipmentMethodAdapter;
import com.cms.callmanager.adapter.ShippingAgentCodeAdapter;
import com.cms.callmanager.adapter.StructureAdapter;
import com.cms.callmanager.adapter.TransferOrderTransferTypeAdapter;
import com.cms.callmanager.constants.Constants;
import com.cms.callmanager.dto.CodeAndNameModel;
import com.cms.callmanager.dto.InventoryLineModel;
import com.cms.callmanager.dto.LinesModel;
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
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class StockTransferDetailActivity extends AppCompatActivity {


    String newString;
    TextView tvFromCode,tvNoField,tvFromName,tvToCode,tvTrasitCode,tvPostingDate,tvPiackListNo,tvLRDate,
            tvBusinessUnit,tvHubDim,tvToName,tvInTrasit,tvStructure,tvTrasnferType,tvStatus,tvSubBusiness,tvOrderETA;
    String transfer_from_CodeField,noField,transfer_from_Name_1Field,transfer_to_CodeField,transfer_to_Name_1Field,
            in_Transit_CodeField,posting_DateField,pickList_NoField,hub_DimField,structureField,transfer_TypeField,
            statusField,BusinessUnit,SubBusinessUnit,keyField,vendor_Invoice_NoField,vendor_Invoice_DateField,transported_by,
            shipment_Method_Code,shipping_Agent_Code,vehicle_No,lR_RR_No,lR_RR_DateField,transfer_Order_ETAField;

    TextView tvVendorInvoiceDate,tvShipingAgent,tvShipMethodCode;
    ArrayList<InventoryLineModel> linesModels;
    private LinesAdapter linesAdapter;
    RecyclerView rv_line;
    EditText etVendorInvoiceNum,etTransportedBy,etVehicleNo,etLLRRNo;
    TableRow trInvoiceDate,trLLRRDate,trShipmentMehodCode,trShipAgentCode,trStructure,trETLDate,trTransferType;
    private static CustomAdapter1 adapter1;
    ListView listView1;
    String APIFLAG ;
    ArrayList<CodeAndNameModel> list;

    ArrayList<TransferOrderTransferType> transferOrderTransferTypes;
    TransferOrderTransferTypeAdapter transferOrderTransferTypeAdapter;

    ArrayList<Structure> structures;
    StructureAdapter structureAdapter;

    ArrayList<ShippingMethodCode> shippingMethodCodes;
    ShipmentMethodAdapter shipmentMethodAdapter;

    ArrayList<ShippingAgentCode> shippingAgentCodes;
    ShippingAgentCodeAdapter shippingAgentCodeAdapter;

    AlertDialog levelDialog;

    String userId;
    private SharedPreferences preferences;

    private boolean numFlag = true,lineFlag = true,infoFlag= true;
    LinearLayout llNum,llInfo;
    ImageView ivNumArrow,ivLinesArrow,ivVendorArrow;


    String UpdateFlag = "";
    String num="";



    EditText etQtyToShip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_transfer_detail);

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

        if(Utils.isInternetOn(this)){
            ProgressUtil.ShowBar(StockTransferDetailActivity.this);
            StockDetails replacementDetailsAPI = new StockDetails(Constants.GetInventoryTransferDetails+"?ActionName="+newString, "GET" ,
                    StockTransferDetailActivity.this);
            replacementDetailsAPI.execute();
        }else{
            Utils.showAlertBox("Please Connect to internet",StockTransferDetailActivity.this);

        }

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

        //linesModels = getLinesModelData();
        rv_line= (RecyclerView)findViewById(R.id.rv_line);
        linesAdapter=new LinesAdapter(linesModels, getApplicationContext());
        LinearLayoutManager horizontalLayout = new LinearLayoutManager(StockTransferDetailActivity.this, LinearLayoutManager.VERTICAL, false);
        rv_line.setLayoutManager(horizontalLayout);
        rv_line.setAdapter(linesAdapter);

        etVendorInvoiceNum=(EditText)findViewById(R.id.etVendorInvoiceNum);

        etTransportedBy=(EditText)findViewById(R.id.etTransportedBy);
        etTransportedBy.setEnabled(false);
        tvShipMethodCode=(TextView) findViewById(R.id.tvShipMethodCode);
        etVehicleNo=(EditText)findViewById(R.id.etVehicleNo);
        etVehicleNo.setEnabled(false);
        etLLRRNo=(EditText)findViewById(R.id.etLLRRNo);
        etLLRRNo.setEnabled(false);

        tvVendorInvoiceDate = (TextView)findViewById(R.id.tvVendorInvoiceDate);
        tvShipingAgent = (TextView)findViewById(R.id.tvShipingAgent);
        tvLRDate = (TextView)findViewById(R.id.tvLRDate);


        trInvoiceDate=(TableRow)findViewById(R.id.trInvoiceDate);
        trInvoiceDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateDialog("InvoiceDate");
            }
        });

        preferences = getSharedPreferences("CMS", Context.MODE_PRIVATE);
        userId = preferences.getString("userId", null);

        /*trETLDate=(TableRow)findViewById(R.id.trETLDate);
        trETLDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateDialog("ETLDate");
            }
        });*/



        /*trLLRRDate=(TableRow)findViewById(R.id.trLLRRDate);
        trLLRRDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateDialog("LLRRDate");
            }
        });*/

        /*trShipmentMehodCode=(TableRow)findViewById(R.id.trShipmentMehodCode);
        trShipmentMehodCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // GetSpinnerData("ShipmentMehodCode");
                final Dialog dialog = new Dialog(StockTransferDetailActivity.this);
                dialog.setCancelable(true);
                dialog.setTitle("Select Name");
                dialog.setContentView(R.layout.dialog_listview);


                listView1=(ListView)dialog.findViewById(R.id.listView1);
                shipmentMethodAdapter= new ShipmentMethodAdapter(shippingMethodCodes,getApplicationContext());
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
        });*/



        /*trShipAgentCode=(TableRow)findViewById(R.id.trShipAgentCode);
        trShipAgentCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //GetSpinnerData("ShipAgentCode");

                final Dialog dialog = new Dialog(StockTransferDetailActivity.this);
                dialog.setCancelable(true);
                dialog.setTitle("Select Name");
                dialog.setContentView(R.layout.dialog_listview);


                listView1=(ListView)dialog.findViewById(R.id.listView1);
                shippingAgentCodeAdapter= new ShippingAgentCodeAdapter(shippingAgentCodes,getApplicationContext());
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
        });*/

        /*trStructure=(TableRow)findViewById(R.id.trStructure);
        trStructure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // GetSpinnerData("Structure");
                final Dialog dialog = new Dialog(StockTransferDetailActivity.this);
                dialog.setCancelable(true);
                dialog.setTitle("Select Name");
                dialog.setContentView(R.layout.dialog_listview);

                listView1=(ListView)dialog.findViewById(R.id.listView1);
                structureAdapter= new StructureAdapter(structures,getApplicationContext());
                listView1.setAdapter(structureAdapter);
                listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        dialog.dismiss();
                        structureField = structures.get(position).getCode();
                        tvStructure.setText(structures.get(position).getDescription());

                    }
                });
                dialog.show();
            }
        });*/

        /*trTransferType=(TableRow)findViewById(R.id.trTransferType);
        trTransferType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(StockTransferDetailActivity.this);
                dialog.setCancelable(true);
                dialog.setTitle("Select Name");
                dialog.setContentView(R.layout.dialog_listview);

                listView1=(ListView)dialog.findViewById(R.id.listView1);
                transferOrderTransferTypeAdapter= new TransferOrderTransferTypeAdapter(transferOrderTransferTypes,getApplicationContext());
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
        });*/







        etVendorInvoiceNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
              //  if (s.toString().length() > 0)
                    vendor_Invoice_NoField = etVendorInvoiceNum.getText().toString();
            }
        });

        /*etTransportedBy.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
               // if (s.toString().length() > 0)
                    transported_by = etTransportedBy.getText().toString();
            }
        });*/


        etLLRRNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // if (s.toString().length() > 0)
                lR_RR_No = etLLRRNo.getText().toString();
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
                // if (s.toString().length() > 0)
                vehicle_No = etVehicleNo.getText().toString();
            }
        });







        findViewById(R.id.btnUpdate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Utils.isInternetOn(StockTransferDetailActivity.this)){
                    ProgressUtil.ShowBar(StockTransferDetailActivity.this);
                    UpdateFlag = "update";
                    UpdateApi updateApi = new UpdateApi(Constants.TransferHeaderSave, "POST" ,
                            StockTransferDetailActivity.this);
                    updateApi.execute();
                }else{
                    Utils.showAlertBox("Please Connect to internet",StockTransferDetailActivity.this);

                }
            }
        });


        findViewById(R.id.btnRelease).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressUtil.ShowBar(StockTransferDetailActivity.this);
                if(Utils.isInternetOn(StockTransferDetailActivity.this)){
                    ProgressUtil.ShowBar(StockTransferDetailActivity.this);
                    UpdateFlag = "release";
                    UpdateApi updateApi = new UpdateApi(Constants.TransferHeaderSave, "POST" ,
                            StockTransferDetailActivity.this);
                    updateApi.execute();
                }else{
                    Utils.showAlertBox("Please Connect to internet",StockTransferDetailActivity.this);

                }
            }
        });


        findViewById(R.id.btnPost).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Utils.isInternetOn(StockTransferDetailActivity.this)){
                    ProgressUtil.ShowBar(StockTransferDetailActivity.this);
                    UpdateFlag = "post";
                    UpdateApi updateApi = new UpdateApi(Constants.TransferHeaderSave, "POST" ,
                            StockTransferDetailActivity.this);
                    updateApi.execute();
                }else{
                    Utils.showAlertBox("Please Connect to internet",StockTransferDetailActivity.this);

                }

               /* ProgressUtil.ShowBar(StockTransferDetailActivity.this);
                PostApi postApi = new PostApi(Constants.TransferHeaderPosting+"?TransferOrderNo="+noField+"&ShipReceive=1", "GET" ,
                        StockTransferDetailActivity.this);
                postApi.execute();*/
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


    }

    private void RadioDialog() {
        LayoutInflater layoutInflater = (LayoutInflater)getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        final View popupView = layoutInflater.inflate(R.layout.dialog_radio, null);
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


                /*if (radioButton.getText().toString().equalsIgnoreCase("Ship"))
                {
                    num = "1";
                }*/

                //else
                    if (radioButton.getText().toString().equalsIgnoreCase("Received"))
                {
                    num = "2";
                }

                popupWindow.dismiss();
                ProgressUtil.ShowBar(StockTransferDetailActivity.this);
                PostApi postApi = new PostApi(Constants.TransferHeaderPosting+"?TransferOrderNo="+noField+"&ShipReceive="+num, "GET" ,
                        StockTransferDetailActivity.this);
                postApi.execute();

            }
        });
    }

    private void GetSpinnerData(String flag) {

        if (flag.equalsIgnoreCase("ShipmentMehodCode"))
        {
            APIFLAG = flag;
           // ProgressUtil.ShowBar(StockTransferDetailActivity.this);
            GetSpinnerApi transferFromCodeCallAsyncTask = new GetSpinnerApi(Constants.ShippingMethodCode+"&UserId="+userId, "GET" ,
                    StockTransferDetailActivity.this);
            transferFromCodeCallAsyncTask.execute();
        }
        else if (flag.equalsIgnoreCase("ShipAgentCode"))
        {
            APIFLAG = flag;
           // ProgressUtil.ShowBar(StockTransferDetailActivity.this);
            GetSpinnerApi transferFromCodeCallAsyncTask = new GetSpinnerApi(Constants.ShippingAgentCode+"&UserId="+userId, "GET" ,
                    StockTransferDetailActivity.this);
            transferFromCodeCallAsyncTask.execute();
        }
        else if (flag.equalsIgnoreCase("Structure"))
        {
            APIFLAG = flag;
           // ProgressUtil.ShowBar(StockTransferDetailActivity.this);
            GetSpinnerApi transferFromCodeCallAsyncTask = new GetSpinnerApi(Constants.Structure+"&UserId="+userId, "GET" ,
                    StockTransferDetailActivity.this);
            transferFromCodeCallAsyncTask.execute();
        }
        else if (flag.equalsIgnoreCase("TransferType"))
        {
            APIFLAG = flag;
            //ProgressUtil.ShowBar(StockTransferDetailActivity.this);
            GetSpinnerApi transferFromCodeCallAsyncTask = new GetSpinnerApi(Constants.TransferOrderTransferType+"?UserId="+userId, "GET" ,
                    StockTransferDetailActivity.this);
            transferFromCodeCallAsyncTask.execute();
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

                    list = new ArrayList<>();

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
                      //  ProgressUtil.ShowBar(StockTransferDetailActivity.this);
                        GetSpinnerApi transferFromCodeCallAsyncTask = new GetSpinnerApi(Constants.Structure+"&UserId="+userId, "GET" ,
                                StockTransferDetailActivity.this);
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
                       // ProgressUtil.ShowBar(StockTransferDetailActivity.this);
                        GetSpinnerApi transferFromCodeCallAsyncTask = new GetSpinnerApi(Constants.ShippingMethodCode+"&UserId="+userId, "GET" ,
                                StockTransferDetailActivity.this);
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
                       // ProgressUtil.ShowBar(StockTransferDetailActivity.this);
                        GetSpinnerApi transferFromCodeCallAsyncTask = new GetSpinnerApi(Constants.ShippingAgentCode+"&UserId="+userId, "GET" ,
                                StockTransferDetailActivity.this);
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
                    else
                    {
                        for(int i = 0; i < arr.length(); i++){

                            if (arr.getJSONObject(i).has("Code") && arr.getJSONObject(i).has("Name")) {
                                list.add(new CodeAndNameModel(arr.getJSONObject(i).getString("Code"),
                                        arr.getJSONObject(i).getString("Name")));
                            }
                            else if (arr.getJSONObject(i).has("Code") && arr.getJSONObject(i).has("Description")) {
                                list.add(new CodeAndNameModel(arr.getJSONObject(i).getString("Code"),
                                        arr.getJSONObject(i).getString("Description")));
                            }

                        }
                    }


                   // listDialog(list);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Utils.showAlertBox("JSONException : "+e.getMessage(),StockTransferDetailActivity.this);
                }
            }else {
                Utils.showAlertBox("No record(s) to display.",StockTransferDetailActivity.this);
            }
        }
    }

    private void listDialog(final ArrayList<CodeAndNameModel> list) {
        final Dialog dialog = new Dialog(StockTransferDetailActivity.this);
        dialog.setCancelable(true);
        dialog.setTitle("Select Name");
        dialog.setContentView(R.layout.dialog_listview);


        listView1=(ListView)dialog.findViewById(R.id.listView1);

        adapter1= new CustomAdapter1(list,getApplicationContext());

        listView1.setAdapter(adapter1);
        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                CodeAndNameModel dataModel= list.get(position);
                dialog.dismiss();


                if (APIFLAG.equalsIgnoreCase("ShipmentMehodCode"))
                {
                    shipment_Method_Code = dataModel.getCode();
                    tvShipMethodCode.setText(dataModel.getName());
                }
                else if (APIFLAG.equalsIgnoreCase("ShipAgentCode"))
                {
                    shipping_Agent_Code =  dataModel.getCode();
                    tvShipingAgent.setText(dataModel.getName());
                }
                else if (APIFLAG.equalsIgnoreCase("Structure"))
                {
                    structureField = dataModel.getCode();
                    tvStructure.setText(dataModel.getName());
                }

                else if (APIFLAG.equalsIgnoreCase("TransferType"))
                {
                    transfer_TypeField = dataModel.getCode();
                    tvTrasnferType.setText(dataModel.getName());
                }

            }
        });

        dialog.show();

    }

    private void DateDialog(final String flag) {
        int mYear, mMonth, mDay;
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(StockTransferDetailActivity.this,
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
                    jsonObject.put("Reason_for_Partial_Receipt",Utils.checkString(linesModels.get(i).getReason_for_Partial_Receipt()) );
                    jsonObject.put("qty_to_Ship",Utils.checkQty(linesModels.get(i).getQty_to_ShipField()) );
                    jsonObject.put("qty_to_Receive",Utils.checkQty(linesModels.get(i).getQty_to_ReceiveField()));


                    arrr.put(jsonObject);

                }

                requestJSON.put("TransferOrderHeaderLine",arrr);


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
                        Utils.showAlertBox(msg, StockTransferDetailActivity.this);
                    }
                    if (json.has("Message"))
                    {
                        String msg =  json.get("Message").toString();
                        Utils.showAlertBox(msg, StockTransferDetailActivity.this);
                    }
                    else if (json.has("Status") && json.get("Status").toString().equalsIgnoreCase("Success"))
                    {
                        String msg =  json.get("ErrorMessage").toString();


                        if (UpdateFlag.equalsIgnoreCase("update"))
                        {
                            Toast.makeText(StockTransferDetailActivity.this, msg, Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(StockTransferDetailActivity.this,StockTransActivity.class));
                            finish();
                        }
                        else if (UpdateFlag.equalsIgnoreCase("release"))
                        {
                            ProgressUtil.ShowBar(StockTransferDetailActivity.this);
                            ReleaseApi releaseApi = new ReleaseApi(Constants.TransferHeaderRelease+"?TransferOrderNo="+noField, "GET" ,
                                    StockTransferDetailActivity.this);
                            releaseApi.execute();
                        }
                        else if (UpdateFlag.equalsIgnoreCase("post"))
                        {
                            RadioDialog();

                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Utils.showAlertBox(e.getMessage(),StockTransferDetailActivity.this);
                }

            }else {
                Utils.showAlertBox("Something Went wrong!!", StockTransferDetailActivity.this);
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

                    if (json.has("Status") && json.get("Status").toString().equalsIgnoreCase("Failure"))
                    {
                        String msg =  json.get("ErrorMessage").toString();
                        Utils.showAlertBox(msg, StockTransferDetailActivity.this);
                    }
                    else if (json.has("Status") && json.get("Status").toString().equalsIgnoreCase("Success"))
                    {
                        String msg =  json.get("ErrorMessage").toString();
                        Toast.makeText(StockTransferDetailActivity.this, msg, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(StockTransferDetailActivity.this,StockTransActivity.class));
                        finish();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Utils.showAlertBox(e.getMessage(),StockTransferDetailActivity.this);
                }

            }else {
                Utils.showAlertBox("Something Went wrong!!", StockTransferDetailActivity.this);
            }
        }
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
                        Utils.showAlertBox(msg, StockTransferDetailActivity.this);
                    }
                    else if (json.has("Status") && json.get("Status").toString().equalsIgnoreCase("Success"))
                    {
                        String msg =  json.get("ErrorMessage").toString();
                        Toast.makeText(StockTransferDetailActivity.this, msg, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(StockTransferDetailActivity.this,StockTransActivity.class));
                        finish();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Utils.showAlertBox(e.getMessage(),StockTransferDetailActivity.this);
                }

            }else {
                Utils.showAlertBox("Something Went wrong!!", StockTransferDetailActivity.this);
            }
        }
    }


    private class StockDetails extends CallManagerAsyncTask {

        JSONObject requestObject = new JSONObject();

        public StockDetails(String action, String reqType, Context context) {
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

                    Utils.Log("JSON Response=== "+json.toString());


                    if (json.has("Status") && json.get("Status").toString().equalsIgnoreCase("Failure"))
                    {
                        Utils.showAlertBox(json.getString("ErrorMessage").toString(),StockTransferDetailActivity.this);
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


                        transfer_Order_ETAField =  json.get("transfer_Order_ETAField").toString();
                        if (Utils.checkStringIsEmpty(transfer_Order_ETAField))
                            tvOrderETA.setText(Utils.ChangeDateTimeFormat(transfer_Order_ETAField));

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

                        pickList_NoField = json.get("pickList_NoField").toString();
                        if(Utils.checkStringIsEmpty(pickList_NoField))
                            tvPiackListNo.setText(pickList_NoField);


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
                        if(statusField.equalsIgnoreCase("0"))
                            tvStatus.setText("Open");
                        else if (statusField.equalsIgnoreCase("1"))
                            tvStatus.setText("Released");


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
                                    jsonArray.getJSONObject(i).getString("resourceField")


                            ));
                        }

                        linesAdapter=new LinesAdapter(linesModels, getApplicationContext());
                        rv_line.setAdapter(linesAdapter);

                        APIFLAG = "TransferType";
                        // ProgressUtil.ShowBar(StockTransferDetailActivity.this);
                        GetSpinnerApi transferFromCodeCallAsyncTask = new GetSpinnerApi(Constants.TransferOrderTransferType+"?UserId="+userId, "GET" ,
                                StockTransferDetailActivity.this);
                        transferFromCodeCallAsyncTask.execute();
                    }




                } catch (JSONException e) {
                    e.printStackTrace();
                    Utils.showAlertBox("JSONException : " + e.getMessage(), StockTransferDetailActivity.this);
                }
            } else {
                Toast.makeText(StockTransferDetailActivity.this, "Something Went wrong!!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(StockTransferDetailActivity.this,StockTransActivity.class));
                finish();
                //Utils.showAlertBox("Something Went wrong!!", StockTransferDetailActivity.this);
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
                   // Toast.makeText(StockTransferDetailActivity.this, s.toString() +"  "+linesModels.get(position).getQty_to_ShipField(), Toast.LENGTH_SHORT).show();

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

    private void lineDetailDialog(final InventoryLineModel inventoryLineModel) {

            LayoutInflater layoutInflater = (LayoutInflater)getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
            final View popupView = layoutInflater.inflate(R.layout.item_lines, null);
            final PopupWindow popupWindow = new PopupWindow(popupView,
                    RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT, true);


            popupWindow.setFocusable(true);
            popupWindow.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);


        TextView tvName = (TextView) popupView.findViewById(R.id.tvName);
        tvName.setText(inventoryLineModel.getItem_NoField());

        TextView tvDesc = (TextView)popupView.findViewById(R.id.tvDesc);
        TextView tvTicket = (TextView)popupView.findViewById(R.id.tvTicket);
        TextView tvQty = (TextView) popupView.findViewById(R.id.tvQty);
        TextView tvShipped = (TextView)popupView.findViewById(R.id.tvShipped);
        TextView tvReceived = (TextView)popupView.findViewById(R.id.tvReceived);
        TextView tvUom = (TextView)popupView.findViewById(R.id.tvUom);
        TextView tvBankNo = (TextView)popupView.findViewById(R.id.tvBankNo);
        TextView tvEngCode = (TextView)popupView.findViewById(R.id.tvEngCode);
        etQtyToShip = (EditText)popupView.findViewById(R.id.etQtyToShip);
        final EditText etReceive = (EditText)popupView.findViewById(R.id.etReceive);
        final EditText etReason = (EditText)popupView.findViewById(R.id.etReason);



        tvDesc.setText(inventoryLineModel.getDescriptionField());
        if(Utils.checkStringIsEmpty(inventoryLineModel.getTicket_NoField()))
            tvTicket.setText(inventoryLineModel.getTicket_NoField());
        tvQty.setText(inventoryLineModel.getQuantityField());
        tvShipped.setText(inventoryLineModel.getQuantity_ShippedField());
        tvReceived.setText(inventoryLineModel.getQuantity_ReceivedField());

        if(Utils.checkStringIsEmpty(inventoryLineModel.getUnit_of_MeasureField()))
            tvUom.setText(inventoryLineModel.getUnit_of_MeasureField());


        if(Utils.checkStringIsEmpty(inventoryLineModel.getBank_Docket_NoField()))
            tvBankNo.setText(inventoryLineModel.getBank_Docket_NoField());

        if(Utils.checkStringIsEmpty(inventoryLineModel.getResourceField()))
            tvEngCode.setText(inventoryLineModel.getResourceField());


        if(Utils.checkStringIsEmpty(inventoryLineModel.getQty_to_ShipField()))
            etQtyToShip.setText(inventoryLineModel.getQty_to_ShipField());

        if(Utils.checkStringIsEmpty(inventoryLineModel.getQty_to_ReceiveField()))
            etReceive.setText(inventoryLineModel.getQty_to_ReceiveField());

        if(Utils.checkStringIsEmpty(inventoryLineModel.getReason_for_Partial_Receipt()))
            etReason.setText(inventoryLineModel.getReason_for_Partial_Receipt());


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
                    etQtyToShip.setSelection(pos);
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


        popupView.findViewById(R.id.btn_Submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                inventoryLineModel.setQty_to_ShipField(etQtyToShip.getText().toString());
                inventoryLineModel.setReason_for_Partial_Receipt(etReason.getText().toString());
                inventoryLineModel.setQty_to_ReceiveField(etReceive.getText().toString());

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



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent searchIntent = new Intent(StockTransferDetailActivity.this , StockTransActivity.class);
        startActivity(searchIntent);
        finish();
    }


}
