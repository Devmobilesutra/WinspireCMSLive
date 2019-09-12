package com.cms.callmanager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.method.LinkMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cms.callmanager.constants.Constants;
import com.cms.callmanager.dto.ATMDetailsDTO;
import com.cms.callmanager.dto.CallDTO;
import com.cms.callmanager.services.CallManagerAsyncTask;
import com.cms.callmanager.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.EOFException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

/**
 * Created by monika on 14/11/17.
 */

public class CallDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    TextView ticketNumber, callDate, atmID,attachments ,customerName, currentStatus,callType, subCallTYpe, comment, sparesUsed;
    String docketNo;
    Toolbar toolbar;
    TextView engineerReachedDateOnSite,diagnosis,targetRespTime,tagetReqTime,actionTaken,modifiedDate, address,Custodian_name,Custodian_no;
    CallDTO callDTO;
    LinearLayout attachmentLayout,attachmentDataLayout;
    ArrayList<Integer> attachmentsID;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calldetaills);
        initUI();
        getCallDetails();

        /*attachment.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
             Intent i = new Intent(CallDetailsActivity.this,AttachmentViewActivity.class);
                i.putExtra("docketNo",getIntent().getExtras().getString("docketNo"));
                String url = "C:\\\\\\\\Ticketing\\\\\\\\Files\\\\RAKESHKUMAR.jpg";
                Utils.Log("url==================="+url);
                i.putExtra("url",url);
                startActivity(i);
            }});*/
    }

    private void initUI() {
        toolbar       = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.action_back);
        docketNo = getIntent().getExtras().getString("docketNo");
        Utils.Log("CallDetails=====" + docketNo);
        callDate = (TextView) findViewById(R.id.callDate);
        atmID = (TextView) findViewById(R.id.atmID);
        attachmentsID = new ArrayList<>();
       // attachment = (TextView) findViewById(R.id.attachment);
        ticketNumber = (TextView) findViewById(R.id.ticketNumber);
        attachmentLayout = (LinearLayout) findViewById(R.id.attachmentLayout);
        attachmentDataLayout = (LinearLayout) findViewById(R.id.attachmentDataLayout);
        atmID = (TextView) findViewById(R.id.atmID);
        //attachments = (TextView) findViewById(R.id.attachments);
        customerName = (TextView) findViewById(R.id.customerName);
        callType = (TextView) findViewById(R.id.callType);
        subCallTYpe = (TextView) findViewById(R.id.subCallTYpe);
        currentStatus = (TextView) findViewById(R.id.currentStatus);
       // engineerReachedDateOnSite = (TextView) findViewById(R.id.engineerReachedDateOnSite);
        actionTaken = (TextView) findViewById(R.id.actionTaken);
        modifiedDate = (TextView) findViewById(R.id.modifiedDate);
        diagnosis = (TextView) findViewById(R.id.diagnosis);
        targetRespTime = (TextView) findViewById(R.id.targetRespTime);
        tagetReqTime = (TextView) findViewById(R.id.tagetReqTime);

        comment = (TextView) findViewById(R.id.comment);
        address =(TextView) findViewById(R.id.address);
        Custodian_name=(TextView) findViewById(R.id.Custodian_name);
        Custodian_no =(TextView)findViewById(R.id.Custodian_no);
       // sparesUsed = (TextView) findViewById(R.id.sparesUsed);
        ticketNumber.setText(docketNo.toString());
       // attachments.setOnClickListener(CallDetailsActivity.this);

    }

    private void getCallDetails() {
        if (Utils.isInternetOn(this)) {
            CallDetailsAsyncTask callDetailsAsyncTask = new CallDetailsAsyncTask(Constants.CALLDETAILS+"?docketno="+docketNo, "GET", CallDetailsActivity.this);
            callDetailsAsyncTask.execute();
        } else {
            Utils.Log("No Details==");

        }
    }

    @Override
    public void onClick(View v) {
        if(attachmentsID.indexOf(v.getId()) > -1){
            Intent i = new Intent(CallDetailsActivity.this,AttachmentViewActivity.class);
            i.putExtra("docketNo",getIntent().getExtras().getString("docketNo"));
            Utils.Log("url==================="+callDTO.getAttachment().get(attachmentsID.indexOf(v.getId())));
            i.putExtra("url",callDTO.getAttachment().get(attachmentsID.indexOf(v.getId())));
            startActivity(i);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //  return super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case android.R.id.home:
                //finish();
                onBackPressed();
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i=new Intent(CallDetailsActivity.this,HomeActivity.class);
        startActivity(i);
    }

    public class CallDetailsAsyncTask extends CallManagerAsyncTask {

        public CallDetailsAsyncTask(String action, String reqType, Context context){
            super(action, reqType, context);
        }

        @Override
        protected JSONObject doInBackground(Object... params) {

            JSONObject postParamData = new JSONObject();
            try {
                return doWork(postParamData);
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
          //  ProgressUtil.hideProgressBar(findViewById(R.id.root), R.id.progressBar);
            if(json != null){
                Utils.Log("JSON Response========="+json.toString());
                try {
                    if(json.has("Status") && json.get("Status").toString().equalsIgnoreCase("Success")){
                        Utils.Log("response===="+json.toString());
                        if(json.getJSONArray("PayLoad").get(0).toString().equalsIgnoreCase("No record(s) to display.") ) {

                        }else {
                            Utils.Log(json.getJSONArray("PayLoad").get(0).toString());
                            JSONObject call = (JSONObject) json.getJSONArray("PayLoad").get(0);
                            if (call.has("Call_Date") && call.getString("Call_Date") != null && call.getString("Call_Date") != "") {
                                callDate.setText(Utils.DateFormater(Utils.convertToCurrentTimeZone(call.getString("Call_Date"))));
                            }
                            if (call.has("Target_Resolution_Time") && call.getString("Target_Resolution_Time") != null && call.getString("Target_Resolution_Time") != "") {
                                tagetReqTime.setText(Utils.DateFormater(Utils.convertToCurrentTimeZone(call.getString("Target_Resolution_Time"))));
                            }
                            if (call.has("Target_Response_Time") && call.getString("Target_Response_Time") != null && call.getString("Target_Response_Time") != "") {
                                targetRespTime.setText(Utils.DateFormater(Utils.convertToCurrentTimeZone(call.getString("Target_Response_Time"))));
                            }
                            if (call.has("ATM_Id") && call.getString("ATM_Id") != null && call.getString("ATM_Id") != "") {
                                atmID.setText(call.getString("ATM_Id"));
                            }
                            if (call.has("Bank") && call.getString("Bank") != null && call.getString("Bank") != "") {
                                customerName.setText(call.getString("Bank"));
                            }
                            if (call.has("Call_Type") && call.getString("Call_Type") != null && call.getString("Call_Type") != "") {
                                callType.setText(call.getString("Call_Type"));
                            }
                            if (call.has("Sub_Call_Type_Value") && call.getString("Sub_Call_Type_Value") != null && call.getString("Sub_Call_Type_Value") != "") {
                                subCallTYpe.setText(call.getString("Sub_Call_Type_Value"));
                            }

                            if (call.has("Diagnosis") && call.getString("Diagnosis") != null && call.getString("Diagnosis") != "") {
                                diagnosis.setText(call.getString("Diagnosis"));
                            }



                            if (call.has("Address") && call.getString("Address") != null && call.getString("Address") != "") {
                                address.setText(call.getString("Address"));
                            }


                            if (call.has("Phone") && call.getString("Phone") != null && call.getString("Phone") != "") {
                                Custodian_no.setText(call.getString("Phone"));
                            }

                            if (call.has("Contact") && call.getString("Contact") != null && call.getString("Contact") != "") {
                                Custodian_name.setText(call.getString("Contact"));
                            }




                            /*if (call.has("Spare_Call_1") && call.getString("Spare_Call_1") != null && call.getString("Spare_Call_1") != "") {
                                if (call.getString("Spare_Call_1").equalsIgnoreCase("0")) {
                                    sparesUsed.setText("No");
                                } else if (call.getString("Spare_Call_1").equalsIgnoreCase("0")) {
                                    sparesUsed.setText("Yes");
                                } else {
                                    sparesUsed.setText("-");
                                }
                            }*/
//

//                            if (call.has("Start_Date") && call.getString("Start_Date") != null && call.getString("Start_Date") != "") {
//                                String startDate = Utils.convertToCurrentTimeZone(call.getString("Start_Date"));
//                                Utils.Log("Start Date==" + startDate);
//                                engineerReachedDateOnSite.setText(Utils.DateFormater(startDate));
//                            }
                            if (call.has("Modified_Date") && call.getString("Modified_Date") != null && call.getString("Modified_Date") != "") {
                                String date = Utils.convertToCurrentTimeZone(call.getString("Modified_Date"));
                                Utils.Log("Modified_Date==" + date);
                                modifiedDate.setText(Utils.DateFormater(date));
                            }
                            if (call.has("Action_Taken") && call.getString("Action_Taken") != null && call.getString("Action_Taken") != "") {
                                actionTaken.setText(call.getString("Action_Taken"));
                            }
                            if (call.has("Status_1") && call.getString("Status_1") != null && call.getString("Status_1") != "") {
                                Utils.Log("status" + call.getString("Status_1"));
                                if(call.getString("Status_1").equalsIgnoreCase("0")){
                                    currentStatus.setText("Open");
                                }else if(call.getString("Status_1").equalsIgnoreCase("1")){
                                    currentStatus.setText("Close");
                                }else {
                                    currentStatus.setText("Hold");
                                }

                            }
                            callDTO = new CallDTO();
                            if(call.has("Attachments") && call.getJSONArray("Attachments") !=null){
                                JSONArray attachmentArray = call.getJSONArray("Attachments");
                                ArrayList<String> tempArray = new ArrayList<String>();
                                String attachmentArr = "";
                                for (int i = 0 ; i<attachmentArray.length();i++){
                                    Utils.Log(attachmentArray.get(i).toString());
                                    JSONObject obj = (JSONObject) attachmentArray.get(i);
                                    tempArray.add(obj.getString("Attachment"));

                                    attachmentArr = attachmentArr + "Attachment"+i;
                                }
                                callDTO.setAttachment(tempArray);
                              //  attachment.setText(attachmentArr);
                                final int N = attachmentArray.length(); // total number of textviews to add

                                //final TextView[] myTextViews = new TextView[N]; // create an empty array;

                                for (int i = 0; i < N; i++) {
//                                    attachments.setPaintFlags(attachments.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
//                                    attachments.append("Attachment - "+(i+1)+"\n");
//                                    attachments.setClickable(true);
//                                    attachments.setMovementMethod(LinkMovementMethod.getInstance());
                                    // create a new textview
                                  final TextView rowTextView = new TextView(CallDetailsActivity.this);
//
//                                    // set some properties of rowTextView or something
                                    rowTextView.setTextColor(Color.parseColor("#0000EE"));
                                    rowTextView.setId(i);
//
// rowTextView.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                                    rowTextView.setText("Attachment - " + (i+1)+"\n");
                                    attachmentsID.add(i);
                                    rowTextView.setPaintFlags(rowTextView.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
//                                    Utils.Log("===========");
//                                    // add the textview to the linearlayout
                                    rowTextView.setOnClickListener(CallDetailsActivity.this);
                                    attachmentDataLayout.addView(rowTextView);

//                                    // save a reference to the textview for later
//                                    myTextViews[i] = rowTextView;
                                }
                            }


                        }

                    }else if(json.has("Status") && json.get("Status").toString().equalsIgnoreCase("Failure")) {
                        Utils.showAlertBox(json.getString("ErrorMessage").toString(),CallDetailsActivity.this);
                    }else {
                        Utils.showAlertBox(json.getString("ErrorMessage").toString(),CallDetailsActivity.this);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else {
                Utils.showAlertBox("Something Went wrong!!",CallDetailsActivity.this);
            }
        }
    }
}

