package com.cms.callmanager;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.cms.callmanager.adapter.CustomeSpinnerAdapter;
import com.cms.callmanager.constants.Constant;
import com.cms.callmanager.constants.Constants;
import com.cms.callmanager.dto.ProblemFixDTO;
import com.cms.callmanager.dto.ResonseCategeoryDTO;
import com.cms.callmanager.dto.SolutionDTO;
import com.cms.callmanager.services.CallManagerAsyncTask;
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

/**
 * Created by Monika zogato on 7/1/17.
 */
public class RepairDetailsActivity extends AppCompatActivity {

    private Spinner spinnerCategory, spinnerSparesUsed, statusSpinnerCategory, spinnerSolution, spinnerProblemFix;
    Button saveRepairDetails;
    EditText edtTxtTicketNumber, edtTxtDistance, edtTxtComment;
    SharedPreferences preferences;
    Toolbar toolbar;
    CustomeSpinnerAdapter spinnerAdapter = null;
    List<ResonseCategeoryDTO.PayLoad> responseCategoryoadList = null;
    List<SolutionDTO.PayLoad> solutionLoadList = null;
    List<ProblemFixDTO.PayLoad> problemLoadList = new ArrayList<>();
    ArrayList<String> searchField = new ArrayList<String>();
    /*= {"Select Status", "Engineer Reached", "Engineer Started", "Repair Started", "Repair Completed", "Hold"};
     */
    LinearLayout spinnerStatusLayout;
    CheckBox checkboxEngineerOnSite, checkboxSparesUsed, checkboxUnproductive, checkboxReturnVisit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair_details);

       /* toolbar       = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.action_back);
        spinnerCategory = (Spinner) findViewById(R.id.spinnerCategory);
        spinnerProblemFix = (Spinner) findViewById(R.id.spinnerProblem);
        spinnerSolution = (Spinner) findViewById(R.id.spinnerSolution);
        statusSpinnerCategory = (Spinner) findViewById(R.id.statusSpinnerCategory);
        saveRepairDetails = (Button) findViewById(R.id.save_repair_detail);
        edtTxtTicketNumber = (EditText) findViewById(R.id.edtTxtTicketNumber);
        edtTxtDistance = (EditText) findViewById(R.id.edtTxtDistance);
        edtTxtComment = (EditText) findViewById(R.id.edtTxtComment);
        checkboxEngineerOnSite = (CheckBox) findViewById(R.id.checkboxEngineerOnSite);
        checkboxSparesUsed = (CheckBox) findViewById(R.id.checkboxSparesUsed);
        checkboxUnproductive = (CheckBox) findViewById(R.id.checkboxUnproductive);
        checkboxReturnVisit = (CheckBox) findViewById(R.id.checkboxReturnVisit);
        spinnerStatusLayout = (LinearLayout) findViewById(R.id.spinnerStatusLayout);


        searchField.add("Select");
        ArrayAdapter<String> searchFieldAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, searchField);
        statusSpinnerCategory.setAdapter(searchFieldAdapter);


        if (getIntent() != null && getIntent().getStringExtra("docketNo") != null) {
            edtTxtTicketNumber.setText(getIntent().getStringExtra("docketNo"));
            edtTxtTicketNumber.setEnabled(false);
        }
        spinnerCategory.setOnItemSelectedListener(new CustomOnItemSelectedListener());
        // statusSpinnerCategory.setOnItemSelectedListener(new CustomOnItemSelectedListener());
        checkboxUnproductive.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    spinnerStatusLayout.setVisibility(View.VISIBLE);
                } else {
                    spinnerStatusLayout.setVisibility(View.GONE);
                }

            }
        });
        spinnerSolution.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinnerProblemFix.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (problemLoadList != null && problemLoadList.size() > 0) {
                    ProblemFixDTO.PayLoad payLoad = problemLoadList.get(i);


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        saveRepairDetails.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (getIntent() != null && getIntent().getStringExtra("docketNo") != null) {

                    ProblemFixDTO.PayLoad payLoad = (ProblemFixDTO.PayLoad) spinnerProblemFix.getSelectedItem();
                    String payLoadId = payLoad.getCode();

                    SolutionDTO.PayLoad sPayLoad = (SolutionDTO.PayLoad) spinnerSolution.getSelectedItem();
                    String sPayLoadId = sPayLoad.getCode();

                    ResonseCategeoryDTO.PayLoad rCPayLoad = (ResonseCategeoryDTO.PayLoad) spinnerCategory.getSelectedItem();
                    String rCPayLoadId = rCPayLoad.getCode();

                    JSONObject postParamData = new JSONObject();
                    preferences = getSharedPreferences("CMS", Context.MODE_PRIVATE);
                    String userId = preferences.getString("userId", null);
                    try {
                        postParamData.put("Docket_No", getIntent().getStringExtra("docketNo"));
                        postParamData.put("Problem_Fix", payLoadId);
                        postParamData.put("Solution", sPayLoadId);

                        //      postParamData.put("responsecategory", rCPayLoadId); prob fix, sol, response
                        if (statusSpinnerCategory.getSelectedItem().toString() != null &&
                                !statusSpinnerCategory.getSelectedItem().toString().equals("Select Status")) {
                            postParamData.put("Follow_up_Status", statusSpinnerCategory.getSelectedItem().toString());
                            postParamData.put("Action_Taken", statusSpinnerCategory.getSelectedItem().toString());
                        } else {
                            postParamData.put("Follow_up_Status", "REPAIR DETAILS");
                            postParamData.put("Action_Taken", "REPAIR COMPLETED");
                        }
                        postParamData.put("Last_modified_By", userId);

                        if (spinnerCategory.getSelectedItem().toString() != null &&
                                !spinnerCategory.getSelectedItem().toString().equals("Select Category")) {
                            postParamData.put("Response_Category", rCPayLoadId);
                        } else {
                            postParamData.put("Response_Category", "");
                        }
                        postParamData.put("Distance", "");
                        if (checkboxEngineerOnSite.isChecked()) {
                            postParamData.put("Engineer_On_Site", "1");
                        } else {
                            postParamData.put("Engineer_On_Site", "0");
                        }
                        if (checkboxSparesUsed.isChecked()) {
                            postParamData.put("Spare_Call_1", "1");
                        } else {
                            postParamData.put("Spare_Call_1", "0");
                        }
                        if (checkboxReturnVisit.isChecked()) {
                            postParamData.put("Return_Visit", "1");
                        } else {
                            postParamData.put("Return_Visit", "0");
                        }
                        if (checkboxUnproductive.isChecked()) {
                            Utils.Log("In checked-----------");
                            postParamData.put("Un_Productive_Visit", "1");
                            if (statusSpinnerCategory.getSelectedItem().toString() != null) {
                                Utils.Log("======" + statusSpinnerCategory.getSelectedItem().toString());
                                postParamData.put("Sub_Status_Name", statusSpinnerCategory.getSelectedItem().toString());

                            }
                        } else {
                            Utils.Log("In Unchecked-----------");
                            postParamData.put("Un_Productive_Visit", "0");
                            postParamData.put("Sub_Status_Name", "REPAIR COMPLETED");
                        }

                        postParamData.put("Follow_up_Status", "repair details");

                        if (edtTxtDistance.getText() != null) {
                            postParamData.put("Distance", edtTxtDistance.getText().toString());
                        }
                        if (edtTxtComment.getText() != null) {
                            postParamData.put("Comments", edtTxtComment.getText().toString());
                            postParamData.put("Action_Taken", edtTxtComment.getText().toString());

                            postParamData.put("Latitude", Prefs.with(RepairDetailsActivity.this).getString(Constant.LATI, ""));
                            postParamData.put("Longitude", Prefs.with(RepairDetailsActivity.this).getString(Constant.LONGI, ""));
                            postParamData.put("Mobile_Device_Id", Prefs.with(RepairDetailsActivity.this).getString(Constant.DEVICE_ID, ""));
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

//                    postParamData.put("problemfix", payLoadId);
                    //                  postParamData.put("solution", sPayLoadId);

                    if (payLoadId.equalsIgnoreCase("-1") || sPayLoadId.equalsIgnoreCase("-1") || rCPayLoadId.equalsIgnoreCase("-1")) {
                        Utils.showAlertBox("Please Enter All Values", RepairDetailsActivity.this);
                    } else {
                        new RepairDetailsAsyncTask(Constants.FOLLOWUP, "POST"
                                , RepairDetailsActivity.this, postParamData).execute();
                    }
                }

            }
        });
        new SubstatusAsyncTask(Constants.SUBSTATUS, "GET"
                , RepairDetailsActivity.this).execute();


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(RepairDetailsActivity.this, HomeActivity.class);
        startActivity(intent);
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

    public class CustomOnItemSelectedListener implements AdapterView.OnItemSelectedListener {

        String firstItem = String.valueOf(spinnerCategory.getSelectedItem());

        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            if (firstItem.equals(String.valueOf(spinnerCategory.getSelectedItem()))) {
                // ToDo when first item is selected
            } else {
               /* Toast.makeText(parent.getContext(),
                        "You have selected : " + parent.getItemAtPosition(pos).toString(),
                        Toast.LENGTH_SHORT).show();*/
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg) {

        }
    }

    private class RepairDetailsAsyncTask extends CallManagerAsyncTask {

        JSONObject postParamData = new JSONObject();

        public RepairDetailsAsyncTask(String action, String reqType, Context context
                , JSONObject postParamData) {
            super(action, reqType, context);
            this.postParamData = postParamData;
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
            if (json != null) {
                Utils.Log("JSON Response=========" + json.toString());
                try {
                    if (json.has("Status") && json.get("Status").toString().equalsIgnoreCase("Success")) {
                        Toast.makeText(RepairDetailsActivity.this,
                                "Repair Details Updated Successfully",
                                Toast.LENGTH_SHORT).show();
                        showRepairCompleteAlert()
                        ;
                    } else if (json.has("Status") && json.get("Status").toString().equalsIgnoreCase("Failure")) {
                        Utils.showAlertBox(json.getString("ErrorMessage").toString(), RepairDetailsActivity.this);
                    } else {
                        Utils.showAlertBox(json.getString("ErrorMessage").toString(), RepairDetailsActivity.this);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Utils.showAlertBox("Something Went wrong!!", RepairDetailsActivity.this);
            }
        }

        private void showRepairCompleteAlert() {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(RepairDetailsActivity.this);

// set title
            alertDialogBuilder.setTitle("CMS");

// set dialog message
            alertDialogBuilder
                    .setMessage("Do you want to attach the file?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent i = new Intent(RepairDetailsActivity.this, AttachImageActivity.class);
                            i.putExtra("docketNo", edtTxtTicketNumber.getText().toString());
                            startActivity(i);
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            Intent i = new Intent(RepairDetailsActivity.this, HomeActivity.class);
                            startActivity(i);

                        }
                    });
            AlertDialog alertDialog = alertDialogBuilder.create();

// show it
            alertDialog.show();
        }

    }

    private class SubstatusAsyncTask extends CallManagerAsyncTask {
        JSONObject postParamData = new JSONObject();

        public SubstatusAsyncTask(String action, String reqType, Context context
        ) {
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
            if (json != null) {
                Utils.Log("JSON Response=========" + json.toString());
                try {
                    if (json.has("Status") && json.get("Status").toString().equalsIgnoreCase("Success")) {
                        if (json.getJSONArray("PayLoad").get(0).toString().equalsIgnoreCase("No record(s) to display.")) {
                            Utils.Log(json.getJSONArray("PayLoad").get(0).toString());
                            Utils.showAlertBox(json.getJSONArray("PayLoad").get(0).toString(), RepairDetailsActivity.this);
                        } else {
                            JSONArray respSubstatusList = json.getJSONArray("PayLoad");

                            for (int i = 0; i < respSubstatusList.length(); i++) {
                                JSONObject jsonData = (JSONObject) respSubstatusList.get(i);
                                if (jsonData.get("DisplayToCustomer").toString().equalsIgnoreCase("1") &&
                                        jsonData.getString("SubStatus") != null && !jsonData.getString("SubStatus").toString().equalsIgnoreCase("")) {
                                    searchField.add(jsonData.get("SubStatus").toString());
                                }
                            }
                        }

                        new ResponseCategeoryTask(Constants.ResponseCategory,
                                "GET", RepairDetailsActivity.this, 0).execute();
                    } else if (json.has("Status") && json.get("Status").toString().equalsIgnoreCase("Failure")) {
                        Utils.showAlertBox(json.getString("ErrorMessage").toString(), RepairDetailsActivity.this);
                    } else {
                        Utils.showAlertBox(json.getString("ErrorMessage").toString(), RepairDetailsActivity.this);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Utils.showAlertBox("Something Went wrong!!", RepairDetailsActivity.this);
            }
        }

    }

    private class ResponseCategeoryTask extends CallManagerAsyncTask {
        JSONObject postParamData = new JSONObject();
        int flag = -1;

        public ResponseCategeoryTask(String action, String reqType, Context context, int flag
        ) {
            super(action, reqType, context);
            this.flag = flag;
            ProgressUtil.showProgressBar(RepairDetailsActivity.this,
                    findViewById(R.id.root), R.id.progressBar);

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
            ProgressUtil.hideProgressBar(findViewById(R.id.root), R.id.progressBar);
            if (json != null) {
                Utils.Log("JSON Response=========" + json.toString());
                try {

                    if (json.has("Status") && json.get("Status").toString().equalsIgnoreCase("Success")) {
                        if (json.getJSONArray("PayLoad").get(0).toString().equalsIgnoreCase("No record(s) to display.")) {
                            Utils.Log(json.getJSONArray("PayLoad").get(0).toString());
                            Utils.showAlertBox(json.getJSONArray("PayLoad").get(0).toString(), RepairDetailsActivity.this);
                        } else {
                            JSONArray respSubstatusList = json.getJSONArray("PayLoad");
                            if (flag == 0) {
                                responseCategoryoadList = new ArrayList<>();

                                ResonseCategeoryDTO.PayLoad payLoad = new ResonseCategeoryDTO.PayLoad();
                                payLoad.setCode("-1");
                                payLoad.setDisplayToCustomer(-1);
                                payLoad.setName("--Select--");
                                responseCategoryoadList.add(payLoad);

                                for (int i = 0; i < respSubstatusList.length(); i++) {
                                    JSONObject jsonData = (JSONObject) respSubstatusList.get(i);
                                    payLoad = new ResonseCategeoryDTO.PayLoad();
                                    payLoad.setCode(jsonData.getString("Code"));
                                    payLoad.setDisplayToCustomer(Integer.parseInt(jsonData.getString("DisplayToCustomer")));
                                    payLoad.setName(jsonData.getString("Name"));
                                    responseCategoryoadList.add(payLoad);

                                }
                            }
                        }
                        setResponseCategeorySpinner(responseCategoryoadList);
                        new ResponseSolution(Constants.Solution,
                                "GET", RepairDetailsActivity.this, 1).execute();


                    } else if (json.has("Status") && json.get("Status").toString().equalsIgnoreCase("Failure")) {
                        ProgressUtil.hideProgressBar(findViewById(R.id.root), R.id.progressBar);
                        Utils.showAlertBox(json.getString("ErrorMessage").toString(), RepairDetailsActivity.this);
                    } else {
                        ProgressUtil.hideProgressBar(findViewById(R.id.root), R.id.progressBar);
                        Utils.showAlertBox(json.getString("ErrorMessage").toString(), RepairDetailsActivity.this);
                    }
                } catch (JSONException e) {
                    ProgressUtil.hideProgressBar(findViewById(R.id.root), R.id.progressBar);
                    e.printStackTrace();
                }
            } else {
                ProgressUtil.hideProgressBar(findViewById(R.id.root), R.id.progressBar);
                Utils.showAlertBox("Something Went wrong!!", RepairDetailsActivity.this);
            }
        }

    }

    private class ResponseSolution extends CallManagerAsyncTask {
        JSONObject postParamData = new JSONObject();
        int flag = -1;

        public ResponseSolution(String action, String reqType, Context context, int flag
        ) {
            super(action, reqType, context);
            this.flag = flag;
            ProgressUtil.showProgressBar(RepairDetailsActivity.this,
                    findViewById(R.id.root), R.id.progressBar);
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
            ProgressUtil.hideProgressBar(findViewById(R.id.root), R.id.progressBar);
            if (json != null) {
                Utils.Log("JSON Response=========" + json.toString());
                try {

                    if (json.has("Status") && json.get("Status").toString().equalsIgnoreCase("Success")) {
                        if (json.getJSONArray("PayLoad").get(0).toString().equalsIgnoreCase("No record(s) to display.")) {
                            Utils.Log(json.getJSONArray("PayLoad").get(0).toString());
                            Utils.showAlertBox(json.getJSONArray("PayLoad").get(0).toString(), RepairDetailsActivity.this);
                        } else {
                            JSONArray respSubstatusList = json.getJSONArray("PayLoad");
                            if (flag == 1) {
                                solutionLoadList = new ArrayList<>();


                                SolutionDTO.PayLoad payLoad = new SolutionDTO.PayLoad();
                                payLoad.setCode("-1");
                                payLoad.setDisplayToCustomer(-1);
                                payLoad.setName("--Select--");
                                payLoad.setCreatedBy("-1");
                                payLoad.setCreatedDate("-1");

                                solutionLoadList.add(payLoad);


                                for (int i = 0; i < respSubstatusList.length(); i++) {
                                    JSONObject jsonData = (JSONObject) respSubstatusList.get(i);
                                    payLoad = new SolutionDTO.PayLoad();
                                    payLoad.setCode(jsonData.getString("Code"));
                                    payLoad.setDisplayToCustomer(Integer.parseInt(jsonData.getString("Display_To_Customer")));
                                    payLoad.setName(jsonData.getString("Name"));
                                    payLoad.setCreatedBy(jsonData.getString("Created_By"));
                                    payLoad.setCreatedDate(jsonData.getString("Created_Date"));

                                    solutionLoadList.add(payLoad);
                                }
                            }
                        }

                        setSolutionSpinner(solutionLoadList);
                        new ResponseProblem(Constants.ProblemFix,
                                "GET", RepairDetailsActivity.this, 2).execute();

                    } else if (json.has("Status") && json.get("Status").toString().equalsIgnoreCase("Failure")) {
                        ProgressUtil.hideProgressBar(findViewById(R.id.root), R.id.progressBar);
                        Utils.showAlertBox(json.getString("ErrorMessage").toString(), RepairDetailsActivity.this);
                    } else {
                        ProgressUtil.hideProgressBar(findViewById(R.id.root), R.id.progressBar);
                        Utils.showAlertBox(json.getString("ErrorMessage").toString(), RepairDetailsActivity.this);
                    }
                } catch (JSONException e) {
                    ProgressUtil.hideProgressBar(findViewById(R.id.root), R.id.progressBar);
                    e.printStackTrace();
                }
            } else {
                ProgressUtil.hideProgressBar(findViewById(R.id.root), R.id.progressBar);
                Utils.showAlertBox("Something Went wrong!!", RepairDetailsActivity.this);
            }
        }

    }

    private class ResponseProblem extends CallManagerAsyncTask {
        JSONObject postParamData = new JSONObject();
        int flag = -1;

        public ResponseProblem(String action, String reqType, Context context, int flag
        ) {

            super(action, reqType, context);
            this.flag = flag;
            ProgressUtil.showProgressBar(RepairDetailsActivity.this,
                    findViewById(R.id.root), R.id.progressBar);
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
            ProgressUtil.hideProgressBar(findViewById(R.id.root), R.id.progressBar);
            if (json != null) {
                Utils.Log("JSON Response=========" + json.toString());
                try {

                    if (json.has("Status") && json.get("Status").toString().equalsIgnoreCase("Success")) {
                        if (json.getJSONArray("PayLoad").get(0).toString().equalsIgnoreCase("No record(s) to display.")) {
                            Utils.Log(json.getJSONArray("PayLoad").get(0).toString());
                            Utils.showAlertBox(json.getJSONArray("PayLoad").get(0).toString(), RepairDetailsActivity.this);
                        } else {
                            JSONArray respSubstatusList = json.getJSONArray("PayLoad");
                            if (flag == 2) {
                                ProblemFixDTO.PayLoad payLoad = new ProblemFixDTO.PayLoad();
                                payLoad.setCode("-1");
                                payLoad.setDisplayToCustomer(-1);
                                payLoad.setName("--Select--");
                                payLoad.setProblemCategory("-1");
                                problemLoadList.add(payLoad);
                                for (int i = 0; i < respSubstatusList.length(); i++) {
                                    JSONObject jsonData = (JSONObject) respSubstatusList.get(i);
                                    payLoad = new ProblemFixDTO.PayLoad();
                                    payLoad.setCode(jsonData.getString("Code"));
                                    payLoad.setDisplayToCustomer(Integer.parseInt(jsonData.getString("DisplayToCustomer")));
                                    payLoad.setName(jsonData.getString("Name"));
                                    payLoad.setProblemCategory(jsonData.getString("ProblemCategory"));
                                    problemLoadList.add(payLoad);


                                }
                            }
                        }

                        setProblemSpinner(problemLoadList);
                    } else if (json.has("Status") && json.get("Status").toString().equalsIgnoreCase("Failure")) {
                        ProgressUtil.hideProgressBar(findViewById(R.id.root), R.id.progressBar);
                        Utils.showAlertBox(json.getString("ErrorMessage").toString(), RepairDetailsActivity.this);
                    } else {
                        ProgressUtil.hideProgressBar(findViewById(R.id.root), R.id.progressBar);
                        Utils.showAlertBox(json.getString("ErrorMessage").toString(), RepairDetailsActivity.this);
                    }
                } catch (JSONException e) {
                    ProgressUtil.hideProgressBar(findViewById(R.id.root), R.id.progressBar);
                    e.printStackTrace();
                }
            } else {
                ProgressUtil.hideProgressBar(findViewById(R.id.root), R.id.progressBar);
                Utils.showAlertBox("Something Went wrong!!", RepairDetailsActivity.this);
            }
        }

    }


    private void setProblemSpinner(List<ProblemFixDTO.PayLoad> loadList) {
        spinnerAdapter = new CustomeSpinnerAdapter(this, loadList, 2);
        spinnerProblemFix.setAdapter(spinnerAdapter);
    }

    private void setSolutionSpinner(List<SolutionDTO.PayLoad> loadList) {
        spinnerAdapter = new CustomeSpinnerAdapter(this, loadList, 1);
        spinnerSolution.setAdapter(spinnerAdapter);
    }

    private void setResponseCategeorySpinner(List<ResonseCategeoryDTO.PayLoad> loadList) {
        spinnerAdapter = new CustomeSpinnerAdapter(this, loadList, 0);
        spinnerCategory.setAdapter(spinnerAdapter);

    }


}
