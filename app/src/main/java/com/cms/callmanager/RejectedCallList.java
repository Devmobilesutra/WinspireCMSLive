package com.cms.callmanager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.cms.callmanager.adapter.ATMListAdapter;
import com.cms.callmanager.adapter.CallListAdapter;
import com.cms.callmanager.constants.Constants;
import com.cms.callmanager.dto.ATMDetailsDTO;
import com.cms.callmanager.dto.CallDTO;
import com.cms.callmanager.services.CallManagerAsyncTask;
import com.cms.callmanager.services.GPSTracker;
import com.cms.callmanager.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.EOFException;
import java.lang.reflect.GenericArrayType;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

/**
 * Created by zogato on 11/2/18.
 */

public class RejectedCallList extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    SharedPreferences preferences;
    ArrayList<ATMDetailsDTO> atmDetailsDTOs;

    private static final int ACCESS_FINE_LOCATION = 3;
    TextView errorTxt;
    Toolbar toolbar;
    FloatingActionButton atmsNearMeFabBtn;
    SwipeRefreshLayout mSwipeRefreshLayout;
    GPSTracker gps ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home2);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.action_back);
        initUI();

        getPendingCallList();
		/*gpsTracker = new GPSTracker(NearestATMActivity.this);
		Utils.Log("lat==="+gpsTracker.getLatitude());
		Utils.Log("long==="+gpsTracker.getLongitude());*/

    }

    private void getPendingCallList() {
        if(Utils.isInternetOn(this)){
            String userId = preferences.getString("userId" , null);

            RejectedCallsAsyncTask rejectedCallsAsyncTask = new RejectedCallsAsyncTask(Constants.REJECTEDCALLS+"?userid="+userId+"&skip=0&take=5",
                    "GET",RejectedCallList.this);
            rejectedCallsAsyncTask.execute();
         }else{
            mSwipeRefreshLayout.setRefreshing(false);
            mRecyclerView.setVisibility(View.GONE);
            errorTxt.setVisibility(View.VISIBLE);
            errorTxt.setText(getString(R.string.network_error));

        }


    }


    private void initUI() {
        mRecyclerView = (RecyclerView) findViewById(R.id.calllist);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(mLayoutManager);
        errorTxt  = (TextView)findViewById(R.id.errorTxt);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_main_swipe_refresh_layout);

        preferences = getSharedPreferences("CMS", Context.MODE_PRIVATE);
        String userId = preferences.getString("userId" , null);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            errorTxt.setVisibility(View.GONE);
                            mRecyclerView.setVisibility(View.VISIBLE);
                            getPendingCallList();
                        } catch (Exception e) {
                            mSwipeRefreshLayout.setRefreshing(false);
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
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

    public class RejectedCallsAsyncTask extends CallManagerAsyncTask {

        JSONObject postParamData = new JSONObject();
        public RejectedCallsAsyncTask(String action, String reqType, Context context) {
            super(action, reqType, context);
        }

        @Override
        protected JSONObject doInBackground(Object... params) {
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
            mSwipeRefreshLayout.setRefreshing(false);
            if(json != null){
                Utils.Log("JSON Response========="+json.toString());
                try {
                    if(json.has("Status") && json.get("Status").toString().equalsIgnoreCase("Success")) {
                        // Utils.Log("response====" + json.toString() + "----");

                        //Utils.Log(json.getJSONArray("PayLoad").get(0).toString()+"====");
                        JSONArray respCallList = json.getJSONArray("PayLoad");
                        ArrayList<CallDTO> callDTOs = new ArrayList<>();
                        if(json.getJSONArray("PayLoad").get(0).toString().equalsIgnoreCase("No record(s) to display.") ) {

                        }else if(respCallList.length() > 0) {
                            for (int i = 0; i < respCallList.length(); i++) {
                                CallDTO callDTO = new CallDTO();
                                JSONObject jsonData = (JSONObject) respCallList.get(i);
                                if (jsonData.getString("Docket_No") != null && jsonData.getString("Docket_No") != "") {
                                    callDTO.setDocketNo(jsonData.getString("Docket_No"));
                                }
                                if (jsonData.getString("ATM_Id") != null && jsonData.getString("ATM_Id") != "") {
                                    callDTO.setAtmID(jsonData.getString("ATM_Id"));
                                }
                                if (jsonData.getString("Call_Date") != null && jsonData.getString("Call_Date") != "") {
                                    String callDate = Utils.convertToCurrentTimeZone(jsonData.getString("Call_Date"));
                                    callDTO.setCallDate(callDate.toString());
                                }
                                if (jsonData.getString("Bank") != null && jsonData.getString("Bank") != "") {
                                    callDTO.setBankName(jsonData.getString("Bank"));
                                }
                                if (jsonData.getString("Target_Response_Time") != null && jsonData.getString("Target_Response_Time") != "") {
                                    String respTime = Utils.convertToCurrentTimeZone(jsonData.getString("Target_Response_Time"));
                                    callDTO.setResponseTime(respTime.toString());
                                }
                                if (jsonData.getString("Target_Resolution_Time") != null && jsonData.getString("Target_Resolution_Time") != "") {
                                    String resolTIme = Utils.convertToCurrentTimeZone(jsonData.getString("Target_Response_Time"));
                                    callDTO.setResolutionTime(resolTIme.toString());
                                }

                                if (jsonData.getString("Status").equalsIgnoreCase("Open")) {
                                    Utils.Log("to titlecase===="+Utils.toTitleCase(jsonData.getString("Sub_Status_Name")));
                                    callDTO.setStatus(Utils.toTitleCase(jsonData.getString("Sub_Status_Name")));
                                    callDTO.setMainStatus("Open");


                                } else if (jsonData.getString("Status").equalsIgnoreCase("Hold")) {
                                    callDTO.setStatus(Utils.toTitleCase(jsonData.getString("Sub_Status_Name")));
                                    callDTO.setMainStatus("Hold");

                                } else {
                                    callDTO.setMobileActivity("Close");
                                    callDTO.setMainStatus("Close");
                                    callDTO.setStatus(Utils.toTitleCase(jsonData.getString("Sub_Status_Name")));

                                }

                                callDTOs.add(callDTO);
                            }

                        }else {
                            Utils.showAlertBox("No records to display",RejectedCallList.this);
                        }

                        if(callDTOs.size() > 0){
                            // callDTOs.get(0).setStatus("DISPATCHED ENGINEER");
                            mAdapter = new CallListAdapter(callDTOs , RejectedCallList.this,"Rejected");
                            mRecyclerView.setAdapter(mAdapter);
                        }else {
                            mRecyclerView.setVisibility(View.GONE);
                            errorTxt.setVisibility(View.VISIBLE);
                        }

                    }else if(json.has("Status") && json.get("Status").toString().equalsIgnoreCase("Failure")) {
                        Utils.showAlertBox(json.getString("ErrorMessage"),RejectedCallList.this);
                    }else {
                        Utils.showAlertBox(json.getString("ErrorMessage"),RejectedCallList.this);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else {
                Utils.showAlertBox("Something Went wrong!!",RejectedCallList.this);
            }
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent searchIntent = new Intent(RejectedCallList.this , HomeActivity.class);
        startActivity(searchIntent);
        finish();
    }
}