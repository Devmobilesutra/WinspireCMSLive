package com.cms.callmanager.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.cms.callmanager.R;
import com.cms.callmanager.adapter.CustomAdapter1;
import com.cms.callmanager.constants.Constants;
import com.cms.callmanager.dto.CodeAndNameModel;
import com.cms.callmanager.dto.ReplacementLine;
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
import java.util.List;

public class CreateReplacementActivity extends AppCompatActivity {

    private TextView tvNum,tvDocketNo,tvPocketDate,tvResource,tvBankDocket;

    RecyclerView rv_line;
    ArrayList<ReplacementLine> replacementLines;
    private ReplacementLineAdapter replacementLineAdapter;
    String newString;
    ListView listView1;
    private static CustomAdapter1 adapter1;
    SharedPreferences preferences;
    private String keyField,noField,docket_NoField,posting_DateField, resourceField,bank_Docket_NoField,posting_DateFieldSpecified;

    private boolean numFlag = true,lineFlag = true,infoFlag= true;
    LinearLayout llNum,llInfo;
    ImageView ivNumArrow,ivLinesArrow,ivVendorArrow;

    TextView tvItemCodeSub,tvItemSubDesc;
    String UpdateFlag = "";
    String codeField;
    String userId;
    boolean docketSelection = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_replacement);



        preferences = getSharedPreferences("CMS", Context.MODE_PRIVATE);
         userId = preferences.getString("userId", null);
        tvNum=(TextView)findViewById(R.id.tvNum);
        tvDocketNo= (TextView)findViewById(R.id.tvDocketNo);
        tvPocketDate = (TextView)findViewById(R.id.tvPocketDate);
        tvResource = (TextView)findViewById(R.id.tvResource);
        tvBankDocket = (TextView)findViewById(R.id.tvBankDocket);



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
            ProgressUtil.ShowBar(CreateReplacementActivity.this);
            ReplacementNewAPI replacementNewAPI = new ReplacementNewAPI(Constants.NewInvReplacement, "GET" ,
                    CreateReplacementActivity.this);
            replacementNewAPI.execute();
        }else{
            Utils.showAlertBox("Please Connect to internet",CreateReplacementActivity.this);

        }

        rv_line =(RecyclerView)findViewById(R.id.rv_line);
        // replacementLineAdapter=new ReplacementLineAdapter(replacementLines, getApplicationContext());
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(CreateReplacementActivity.this, LinearLayoutManager.VERTICAL, false);
        rv_line.setLayoutManager(horizontalLayoutManager);
        //rv_line.setAdapter(replacementLineAdapter);


        findViewById(R.id.tr_Docket).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (docketSelection)
                {
                    if(Utils.isInternetOn(CreateReplacementActivity.this)){
                        ProgressUtil.ShowBar(CreateReplacementActivity.this);
                        DocketAPI docketAPI = new DocketAPI(Constants.DocketNo +"&UserId="+userId, "GET" ,
                                CreateReplacementActivity.this);
                        docketAPI.execute();
                    }else{
                        Utils.showAlertBox("Please Connect to internet",CreateReplacementActivity.this);

                    }
                }

            }
        });


        findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // update
        findViewById(R.id.btnSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              if(Utils.isInternetOn(CreateReplacementActivity.this)){
               //   ProgressUtil.ShowBar(CreateReplacementActivity.this);
                            UpdateFlag = "update";
                            UpdateApi updateApi = new UpdateApi(Constants.InvSaveData+"/InvSaveData?ActionFor=Submit", "POST" ,
                                    CreateReplacementActivity.this);
                            updateApi.execute();
                        }else{
                            Utils.showAlertBox("Please Connect to internet",CreateReplacementActivity.this);

                        }
            }
        });



        llNum = (LinearLayout)findViewById(R.id.llNum);
        llNum.setVisibility(View.VISIBLE);
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

        rv_line.setVisibility(View.VISIBLE);
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

    }


    private class UpdateApi extends CallManagerAsyncTask {

        JSONObject requestJSON = new JSONObject();

        JSONArray arrr = new JSONArray();


        public UpdateApi(String action, String reqType, Context context) {
            super(action, reqType, context);

        }


        @Override
        protected JSONObject doInBackground(Object... params) {

            try {

                requestJSON.put("key",keyField);
                requestJSON.put("No",noField);
                requestJSON.put("docket_No",Utils.checkString(docket_NoField));
                requestJSON.put("posting_Date",posting_DateField);
                //requestJSON.put("Resource",resourceField);
                requestJSON.put("bank_Docket_No",Utils.checkString(bank_Docket_NoField));




                for (int i=0;i<replacementLines.size();i++)
                {
                    JSONObject invObject = new JSONObject();
                    invObject.put("key",replacementLines.get(i).getKeyField());
                    invObject.put("document_No",Utils.checkString(noField));
                    //invObject.put("line_no",Utils.checkString(replacementLines.get(i).getLine_noField()));
                    invObject.put("item_Code",Utils.checkString(replacementLines.get(i).getItem_CodeField()));
                    invObject.put("item_Code_Substitute",Utils.checkString(replacementLines.get(i).getItem_Code_SubstituteField()));

                    arrr.put(invObject);
                }



                requestJSON.put("ListInvReplacementPageLine",arrr);


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
                        Utils.showAlertBox(msg, CreateReplacementActivity.this);
                    }
                    else if (json.has("Status") && json.get("Status").toString().equalsIgnoreCase("Success"))
                    {
                        String msg =  json.get("ErrorMessage").toString();

                        Toast.makeText(CreateReplacementActivity.this, msg, Toast.LENGTH_SHORT).show();
                        Intent searchIntent = new Intent(CreateReplacementActivity.this , ReplacementItemActivity.class);
                        startActivity(searchIntent);
                        finish();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Utils.showAlertBox(e.getMessage(),CreateReplacementActivity.this);
                }

            }else {
                Utils.showAlertBox("Something Went wrong!!", CreateReplacementActivity.this);
            }
        }
    }

    private class ReplacementNewAPI extends CallManagerAsyncTask {

        JSONObject requestObject = new JSONObject();

        public ReplacementNewAPI(String action, String reqType, Context context) {
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

                    keyField =  json.get("keyField").toString();
                    noField = json.get("noField").toString();
                    docket_NoField = json.get("docket_NoField").toString();
                    posting_DateField = json.get("posting_DateField").toString();
                    resourceField = json.get("resourceField").toString();
                    bank_Docket_NoField = json.get("bank_Docket_NoField").toString();
                    posting_DateFieldSpecified = json.get("posting_DateFieldSpecified").toString();

                    if (Utils.checkStringIsEmpty(noField))
                        tvNum.setText(noField);

                    if (Utils.checkStringIsEmpty(docket_NoField))
                        tvDocketNo.setText(docket_NoField);

                    tvPocketDate.setText(Utils.ChangeDateFormat(posting_DateField));
                    if (Utils.checkStringIsEmpty(resourceField))
                        tvResource.setText(resourceField);
                    if (Utils.checkStringIsEmpty(bank_Docket_NoField))
                        tvBankDocket.setText(bank_Docket_NoField);


                    /*JSONArray jsonArray = json.getJSONArray("inv_Replacement_PageField");


                    replacementLines = new ArrayList<>();

                    for (int i=0;i<jsonArray.length();i++)
                    {
                        replacementLines.add(new ReplacementLine(
                                jsonArray.getJSONObject(i).getString("keyField"),
                                jsonArray.getJSONObject(i).getString("document_NoField"),
                                jsonArray.getJSONObject(i).getString("line_noField"),
                                jsonArray.getJSONObject(i).getString("line_noFieldSpecified"),
                                jsonArray.getJSONObject(i).getString("entry_TypeField"),
                                jsonArray.getJSONObject(i).getString("entry_TypeFieldSpecified"),
                                jsonArray.getJSONObject(i).getString("item_CodeField"),
                                jsonArray.getJSONObject(i).getString("item_DescriptionField"),
                                jsonArray.getJSONObject(i).getString("location_CodeField"),
                                jsonArray.getJSONObject(i).getString("quantityField"),
                                jsonArray.getJSONObject(i).getString("quantityFieldSpecified"),
                                jsonArray.getJSONObject(i).getString("unit_of_MeasurementField"),
                                jsonArray.getJSONObject(i).getString("reason_CodeField"),
                                jsonArray.getJSONObject(i).getString("entry_Type_SubstituteField"),
                                jsonArray.getJSONObject(i).getString("entry_Type_SubstituteFieldSpecified"),
                                jsonArray.getJSONObject(i).getString("item_Code_SubstituteField"),
                                jsonArray.getJSONObject(i).getString("item_Description_SubstituteField"),
                                jsonArray.getJSONObject(i).getString("reason_Code_SubstituteField"),
                                jsonArray.getJSONObject(i).getString("uOM_SubstituteField")

                        ));
                    }
                    replacementLineAdapter = new ReplacementLineAdapter(replacementLines, getApplicationContext());
                    rv_line.setAdapter(replacementLineAdapter);*/




                } catch (JSONException e) {
                    e.printStackTrace();
                    Utils.showAlertBox("JSONException : " + e.getMessage(), CreateReplacementActivity.this);
                }
            } else {
                Utils.showAlertBox("Something Went wrong!!", CreateReplacementActivity.this);
            }
        }
    }

    private class ReplacementDetailsAPI extends CallManagerAsyncTask {

        JSONObject requestObject = new JSONObject();

        public ReplacementDetailsAPI(String action, String reqType, Context context) {
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

                    keyField =  json.get("keyField").toString();
                    noField = json.get("noField").toString();
                    docket_NoField = json.get("docket_NoField").toString();
                    posting_DateField = json.get("posting_DateField").toString();
                    resourceField = json.get("resourceField").toString();
                    bank_Docket_NoField = json.get("bank_Docket_NoField").toString();
                    posting_DateFieldSpecified = json.get("posting_DateFieldSpecified").toString();

                    tvNum.setText(noField);
                    tvDocketNo.setText(docket_NoField);
                    tvPocketDate.setText(Utils.ChangeDateFormat(posting_DateField));
                    tvResource.setText(resourceField);
                    tvBankDocket.setText(bank_Docket_NoField);


                    JSONArray jsonArray = json.getJSONArray("inv_Replacement_PageField");


                    replacementLines = new ArrayList<>();

                    for (int i=0;i<jsonArray.length();i++)
                    {
                        replacementLines.add(new ReplacementLine(
                                jsonArray.getJSONObject(i).getString("keyField"),
                                jsonArray.getJSONObject(i).getString("document_NoField"),
                                jsonArray.getJSONObject(i).getString("line_noField"),
                                jsonArray.getJSONObject(i).getString("line_noFieldSpecified"),
                                jsonArray.getJSONObject(i).getString("entry_TypeField"),
                                jsonArray.getJSONObject(i).getString("entry_TypeFieldSpecified"),
                                jsonArray.getJSONObject(i).getString("item_CodeField"),
                                jsonArray.getJSONObject(i).getString("item_DescriptionField"),
                                jsonArray.getJSONObject(i).getString("location_CodeField"),
                                jsonArray.getJSONObject(i).getString("quantityField"),
                                jsonArray.getJSONObject(i).getString("quantityFieldSpecified"),
                                jsonArray.getJSONObject(i).getString("unit_of_MeasurementField"),
                                jsonArray.getJSONObject(i).getString("reason_CodeField"),
                                jsonArray.getJSONObject(i).getString("entry_Type_SubstituteField"),
                                jsonArray.getJSONObject(i).getString("entry_Type_SubstituteFieldSpecified"),
                                jsonArray.getJSONObject(i).getString("item_Code_SubstituteField"),
                                jsonArray.getJSONObject(i).getString("item_Description_SubstituteField"),
                                jsonArray.getJSONObject(i).getString("reason_Code_SubstituteField"),
                                jsonArray.getJSONObject(i).getString("uOM_SubstituteField"),
                                jsonArray.getJSONObject(i).getString("engineer_CommentsField")

                        ));
                    }
                    replacementLineAdapter = new ReplacementLineAdapter(replacementLines, getApplicationContext());
                    rv_line.setAdapter(replacementLineAdapter);


                    //  replacementLineAdapter=new ReplacementLineAdapter(replacementLines, getApplicationContext());
                    // rv_line.setAdapter(replacementLineAdapter);

                    //String aaa =  jsonArray.getJSONObject(0).getString("keyField");
                    //Log.d("", "jsonArray: "+jsonArray.toString());

                    // Toast.makeText(CreateReplacementActivity.this, aaa, Toast.LENGTH_SHORT).show();

                    //if(json.has("Status") && json.get("Status").toString().equalsIgnoreCase("Success")

                    /*ArrayList<stockTransfer> list = new ArrayList<>();
                    for (int i = 0; i < arr.length(); i++) {


                        list.add(new stockTransfer(arr.getJSONObject(i).getString("No_"),
                                arr.getJSONObject(i).getString("Transfer_from_Code"),
                                arr.getJSONObject(i).getString("Transfer_to_Code"),
                                arr.getJSONObject(i).getString("Status")));
                    }


                    stockTransferAdapter=new StockTransferAdapter(list, getApplicationContext());
                    vertical_recycler_view.setAdapter(stockTransferAdapter);*/

                } catch (JSONException e) {
                    e.printStackTrace();
                    Utils.showAlertBox("JSONException : " + e.getMessage(), CreateReplacementActivity.this);
                }
            } else {
                Utils.showAlertBox("Something Went wrong!!", CreateReplacementActivity.this);
            }
        }
    }


    private class DocketAPI extends CallManagerAsyncTaskArray {

        JSONArray requestArray = new JSONArray();

        public DocketAPI(String action, String reqType, Context context) {
            super(action, reqType, context);

        }

        @Override
        protected JSONArray doInBackground(Object... params) {

            try {
                return doWorkJSONArray(requestArray);
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
        protected void onPostExecute(JSONArray json) {
            super.onPostExecute(json);
            ProgressUtil.hideBar();
            if (json != null) {
                try {
                    JSONArray arr = json;

                    ArrayList<CodeAndNameModel> list = new ArrayList<CodeAndNameModel>();
                    for(int i = 0; i < arr.length(); i++){

                        if (arr.getJSONObject(i).has("keys") && arr.getJSONObject(i).has("avalue")) {
                            list.add(new CodeAndNameModel(
                                    arr.getJSONObject(i).getString("keys"),
                                    arr.getJSONObject(i).getString("avalue")));
                        }

                    }

                    preferences.edit().putString("flag", "docket").apply();
                    listDialog(list);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Utils.showAlertBox("JSONException : " + e.getMessage(), CreateReplacementActivity.this);
                }
            } else {
                Utils.showAlertBox("No record(s) to display.", CreateReplacementActivity.this);
            }
        }
    }

    private class ReplacementLineAdapter extends RecyclerView.Adapter<ReplacementLineAdapter.MyViewHolder> {


        List<ReplacementLine> replacementLineList;
        Context context;

        public ReplacementLineAdapter(List<ReplacementLine> replacementLineList, Context context) {
            this.replacementLineList = replacementLineList;
            this.context = context;
        }

        @Override
        public ReplacementLineAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rep_line_short, parent, false);

            return new ReplacementLineAdapter.MyViewHolder(itemView);
        }

        @Override
        public int getItemCount() {
            return replacementLineList == null ? 0 : replacementLineList.size();
        }

        @Override
        public void onBindViewHolder(final ReplacementLineAdapter.MyViewHolder holder, final int position) {

            holder.tvName.setText(replacementLineList.get(position).getItem_CodeField());
            holder.tvLineNo.setText(replacementLineList.get(position).getLine_noField());


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    preferences.edit().putInt("index", Integer.parseInt(String.valueOf(position))).apply();
                    preferences.edit().putString("flag", "line").apply();
                    lineDetailDialog(replacementLineList.get(position));
                }
            });
            // holder.tvItemDesc.setText(replacementLineList.get(position).getItem_DescriptionField());
            // holder.tvLocationCode.setText(replacementLineList.get(position).getLocation_CodeField());
            // holder.tvQty.setText(replacementLineList.get(position).getQuantityField());
            // holder.tvSubstitude.setText(replacementLineList.get(position).getReason_Code_SubstituteField());


            /*if (Utils.checkStringIsEmpty(replacementLineList.get(position).getItem_Code_SubstituteField()))
                holder.tvItemCodeSub.setText(replacementLineList.get(position).getItem_Code_SubstituteField());


            if (Utils.checkStringIsEmpty(replacementLineList.get(position).getItem_Description_SubstituteField()))
                holder.tvItemSubDesc.setText(replacementLineList.get(position).getItem_Description_SubstituteField());
            
            holder.tr_ItemCodeSub.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    preferences.edit().putInt("index", Integer.parseInt(String.valueOf(position))).apply();
                    preferences.edit().putString("flag", "line").apply();
                    ItemCodeSubstituteApi();
                }
            });*/
            //  holder.tv_itemCode.setText(replacementLineList.get(position).getItem_CodeName());
         /* holder.tvTolocation.setText(stockTransfers.get(position).getTo_code());

            if (stockTransfers.get(position).getStatus().equalsIgnoreCase("0"))
                holder.tvStatus.setText("open");
            else
                holder.tvStatus.setText("close");


            if (position % 2 == 0) {
                holder.itemView.setBackgroundColor(Color.parseColor("#EEF6F5"));
            } else {
                holder.itemView.setBackgroundColor(Color.parseColor("#ffffff"));
            }*/


           /* holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "hiii", Toast.LENGTH_SHORT).show();
                }
            });*/


        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            private TextView tvName,tv_itemCode,tvItemDesc,tvLocationCode,tvQty,tvItemCodeSub,tvSubstitude,tvItemSubDesc,tvLineNo;
            private ImageView ItemCodeSubstitute;
            private TableRow tr_ItemCodeSub;

            public MyViewHolder(View view) {
                super(view);
                tvName = (TextView) view.findViewById(R.id.tvName);
                tvLineNo = (TextView)view.findViewById(R.id.tvLineNo);
                /*tv_itemCode = (TextView) view.findViewById(R.id.tv_itemCode);
                tvItemDesc = (TextView) view.findViewById(R.id.tvItemDesc);
                tvLocationCode = (TextView) view.findViewById(R.id.tvLocationCode);
                tvQty = (TextView) view.findViewById(R.id.tvQty);
                tvItemCodeSub = (TextView) view.findViewById(R.id.tvItemCodeSub);
                tvSubstitude = (TextView) view.findViewById(R.id.tvSubstitude);
                tvItemSubDesc = (TextView) view.findViewById(R.id.tvItemSubDesc);
                ItemCodeSubstitute = (ImageView)view.findViewById(R.id.ItemCodeSubstitute);
                tr_ItemCodeSub = (TableRow) view.findViewById(R.id.tr_ItemCodeSub);*/
                //  tvStatus = (TextView) view.findViewById(R.id.tvStatus);
            }
        }
    }

    private void lineDetailDialog(final ReplacementLine replacementLine) {

        LayoutInflater layoutInflater = (LayoutInflater)getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        final View popupView = layoutInflater.inflate(R.layout.item_rep_line, null);
        final PopupWindow popupWindow = new PopupWindow(popupView,
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT, true);


        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);




        TextView tvName = (TextView) popupView.findViewById(R.id.tvName);
        TextView tvItemDesc = (TextView) popupView.findViewById(R.id.tvItemDesc);
        TextView tvLocationCode = (TextView) popupView.findViewById(R.id.tvLocationCode);
        TextView tvQty = (TextView) popupView.findViewById(R.id.tvQty);
        tvItemCodeSub = (TextView) popupView.findViewById(R.id.tvItemCodeSub);
        tvItemSubDesc = (TextView) popupView.findViewById(R.id.tvItemSubDesc);
        TextView tvSubstitude = (TextView) popupView.findViewById(R.id.tvSubstitude);
        TableRow tr_ItemCodeSub = (TableRow) popupView.findViewById(R.id.tr_ItemCodeSub);


        tvName.setText(replacementLine.getItem_CodeField());
        tvItemDesc.setText(replacementLine.getItem_DescriptionField());
        tvLocationCode.setText(replacementLine.getLocation_CodeField());
        tvQty.setText(replacementLine.getQuantityField());
        tvSubstitude.setText(replacementLine.getReason_Code_SubstituteField());

        if (Utils.checkStringIsEmpty(replacementLine.getItem_Code_SubstituteField()))
            tvItemCodeSub.setText(replacementLine.getItem_Code_SubstituteField());


        if (Utils.checkStringIsEmpty(replacementLine.getItem_Description_SubstituteField()))
            tvItemSubDesc.setText(replacementLine.getItem_Description_SubstituteField());

        tr_ItemCodeSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                codeField = replacementLine.getItem_CodeField();
                ItemCodeSubstituteApi();
            }
        });


        popupView.findViewById(R.id.btn_Submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replacementLine.setItem_Code_SubstituteField(tvItemCodeSub.getText().toString());
                replacementLine.setItem_Description_SubstituteField(tvItemSubDesc.getText().toString());
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

    private void ItemCodeSubstituteApi() {
        if(Utils.isInternetOn(this)){
            ProgressUtil.ShowBar(CreateReplacementActivity.this);
            ItemCodeSubstituteaApi itemCodeSubstituteaApi = new ItemCodeSubstituteaApi(Constants.ItemCodeSubstitute+"&UserId="+userId, "GET" ,
                    CreateReplacementActivity.this);
            itemCodeSubstituteaApi.execute();
        }else{
            Utils.showAlertBox("Please Connect to internet",CreateReplacementActivity.this);

        }
    }

    private class ItemCodeSubstituteaApi extends CallManagerAsyncTaskArray {

        JSONArray requestArray = new JSONArray();

        public ItemCodeSubstituteaApi(String action, String reqType, Context context) {
            super(action, reqType, context);

        }

        @Override
        protected JSONArray doInBackground(Object... params) {

            try {
                return doWorkJSONArray(requestArray);
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
        protected void onPostExecute(JSONArray json) {
            super.onPostExecute(json);
            ProgressUtil.hideBar();
            if (json != null) {
                try {
                    JSONArray arr = json;

                    ArrayList<CodeAndNameModel> list = new ArrayList<CodeAndNameModel>();
                    Log.d("", "codeField: "+codeField);
                    for(int i = 0; i < arr.length(); i++){

                        if (arr.getJSONObject(i).has("No_") && arr.getJSONObject(i).has("SN")) {


                            if (codeField.equalsIgnoreCase(arr.getJSONObject(i).getString("No_")))
                            {
                                list.add(new CodeAndNameModel(arr.getJSONObject(i).getString("No_"),
                                        arr.getJSONObject(i).getString("SN"),
                                        arr.getJSONObject(i).getString("Description")));
                            }
                        }
                    }

                   /* ArrayList<ItemCodeSubstituteModel> list = new ArrayList<>();
                    for (int i = 0; i < arr.length(); i++) {



                        list.add(new ItemCodeSubstituteModel(arr.getJSONObject(i).getString("No_"),
                                arr.getJSONObject(i).getString("SN")));
                    }*/

                    listDialog(list);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Utils.showAlertBox("JSONException : " + e.getMessage(), CreateReplacementActivity.this);
                }
            } else {
                Utils.showAlertBox("No record(s) to display.", CreateReplacementActivity.this);
            }
        }
    }

    private void listDialog(final ArrayList<CodeAndNameModel> list) {
        final Dialog dialog = new Dialog(CreateReplacementActivity.this);

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

        adapter1= new CustomAdapter1(list,getApplicationContext());

        listView1.setAdapter(adapter1);
        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                CodeAndNameModel dataModel= list.get(position);
                dialog.dismiss();

                int lastIndex = preferences.getInt("index" , 0);

                String flag = preferences.getString("flag" , "");

                if (flag.equalsIgnoreCase("line"))
                {
                    tvItemCodeSub.setText(dataModel.getName());
                    tvItemSubDesc.setText(dataModel.getName2());
                }
                else if (flag.equalsIgnoreCase("docket"))
                {
                    docket_NoField = dataModel.getCode();
                    tvDocketNo.setText(docket_NoField);

                    ApiForLinesDetails(dataModel.getCode());
                }

            }
        });


        dialog.show();

    }

    private void ApiForLinesDetails(String code) {
        if(Utils.isInternetOn(this)){
            ProgressUtil.ShowBar(CreateReplacementActivity.this);
           // Log.d("", "ApiForLinesDetails: "+Constants.RecDetails+"?DocketNo="+docket_NoField+"&RecNo="+noField);
            LineDetailsApi liensDetailsApi = new LineDetailsApi(Constants.RecDetails+"?DocketNo="+docket_NoField+"&RecNo="+noField, "GET" ,
                    CreateReplacementActivity.this);
            liensDetailsApi.execute();
        }else{
            Utils.showAlertBox("Please Connect to internet",CreateReplacementActivity.this);

        }
    }

    private class LineDetailsApi extends CallManagerAsyncTaskArray {

        JSONArray requestArray = new JSONArray();

        public LineDetailsApi(String action, String reqType, Context context) {
            super(action, reqType, context);

        }

        @Override
        protected JSONArray doInBackground(Object... params) {

            try {
                return doWorkJSONArray(requestArray);
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
        protected void onPostExecute(JSONArray json) {
            super.onPostExecute(json);
            ProgressUtil.hideBar();
            if (json != null) {
                try {

                    Log.d("", "onPostExecute: "+json.toString());
                    JSONArray jsonArray = json;

                    docketSelection = false;

                    if (jsonArray.getJSONObject(0).has("Resource"))
                        tvResource.setText(jsonArray.getJSONObject(0).getString("Resource"));

                    if (jsonArray.getJSONObject(0).has("BDN"))
                        tvBankDocket.setText(jsonArray.getJSONObject(0).getString("BDN"));

                    replacementLines = new ArrayList<>();


                    for (int i=0;i<jsonArray.length();i++)
                    {
                        replacementLines.add(new ReplacementLine(
                                jsonArray.getJSONObject(i).getString("Item_Code"),
                                jsonArray.getJSONObject(i).getString("Item_Description"),
                                jsonArray.getJSONObject(i).getString("Location_Code"),
                                jsonArray.getJSONObject(i).getString("Quantity"),
                                jsonArray.getJSONObject(i).getString("Unit_of_Measurement"),
                                jsonArray.getJSONObject(i).getString("Reason_Code"),
                                jsonArray.getJSONObject(i).getString("Entry_Type_Substitute"),
                                jsonArray.getJSONObject(i).getString("Item_Code_Substitute"),
                                jsonArray.getJSONObject(i).getString("Item_Description_Substitute"),
                                jsonArray.getJSONObject(i).getString("Reason_Code_Substitute"),
                                jsonArray.getJSONObject(i).getString("UOM_Substitute"),
                                jsonArray.getJSONObject(i).getString("engineer_CommentsField")
                        ));
                    }
                    replacementLineAdapter = new ReplacementLineAdapter(replacementLines, getApplicationContext());
                    rv_line.setAdapter(replacementLineAdapter);


                } catch (JSONException e) {
                    e.printStackTrace();
                    Utils.showAlertBox("JSONException : " + e.getMessage(), CreateReplacementActivity.this);
                    replacementLines.clear();

                }
            } else {
                Utils.showAlertBox("Inv. Replacement Jnl Already created for this docket no.", CreateReplacementActivity.this);
              //  replacementLines.clear();
            }
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent searchIntent = new Intent(CreateReplacementActivity.this , ReplacementItemActivity.class);
        startActivity(searchIntent);
        finish();
    }
}