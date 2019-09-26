package com.cms.callmanager;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.cms.callmanager.constants.Constants;
import com.cms.callmanager.dto.CallDTO;
import com.cms.callmanager.services.CallManagerAsyncTask;
import com.cms.callmanager.utils.Utils;
import com.cms.callmanager.utils.Validation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.EOFException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

/**
 * Created by Monika on 1/9/17.
 */
public class searchCallActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    EditText edtDateFrom, edtDateTo, bankName, enterAtmId;
    int year, month, day;
    Toolbar toolbar;
    Button searchCallBtn;
    Spinner spinnerCallType;
    Spinner searchCategory, spinnerOperator, searchCategoryValue;
    /*String []callTypeList = {"--Select--","PM" , "BREAKDOWN","BAD ACTOR","CHGC - DOWN","CHGC - OPERATIONAL","EJ INSTALLATION",
            "RECONCILIATION","INST/UPG","OPERATIONAL","SITE ISSUE"};*/
    String[] searchField = {"--Select--", "Sub Status Name", "Source", "Bank"};
    String[] operatorList = {"--Select--", "Equals", "Starts With", "Ends With"};
    // String []searchFieldList = {"--Select--","Engineer Reached","Engineer Started","Repair Started","Repair Completed","Hold"};
    ArrayList<String> searchFieldList = new ArrayList<String>();
    ArrayList<String> callTypeList = new ArrayList<String>();
    ArrayAdapter<String> searchFieldValueAdapter;
    ArrayAdapter<String> operatorAdapter;
    ArrayList<String> substatusList = new ArrayList<String>();

    ArrayList<String> sourceList = new ArrayList<String>();
    SharedPreferences preferences;
    TextInputLayout txtDateFrom, txtDateTo, txtBankNameLayout;
    LinearLayout txtValueLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_search_call);
        edtDateFrom = (EditText) findViewById(R.id.edtDateFrom);
        edtDateTo = (EditText) findViewById(R.id.edtDateTo);
        bankName = (EditText) findViewById(R.id.bankName);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        spinnerCallType = (Spinner) findViewById(R.id.spinnerCallType);
        searchCategory = (Spinner) findViewById(R.id.searchCategory);
        searchCategoryValue = (Spinner) findViewById(R.id.searchCategoryValue);
        spinnerOperator = (Spinner) findViewById(R.id.spinnerOperator);
        searchCallBtn = (Button) findViewById(R.id.searchCall);
        txtDateFrom = (TextInputLayout) findViewById(R.id.txtDateFrom);
        txtDateTo = (TextInputLayout) findViewById(R.id.txtDateTo);
        txtBankNameLayout = (TextInputLayout) findViewById(R.id.txtBankNameLayout);
        txtValueLayout = (LinearLayout) findViewById(R.id.txtValueLayout);

        enterAtmId = (EditText) findViewById(R.id.enterAtmId);

        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.action_back);

        edtDateFrom.setOnClickListener(this);
        edtDateTo.setOnClickListener(this);
        edtDateFrom.setKeyListener(null);
        edtDateTo.setKeyListener(null);

        searchFieldList.add("--Select--");
        ArrayAdapter<String> searchFieldAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, searchField);
        searchCategory.setAdapter(searchFieldAdapter);
        searchCategory.setOnItemSelectedListener(new SearchFieldOnItemSelectedListener());

        operatorAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, operatorList);

        setOperatorSpinner(operatorAdapter);


        setValueSpinner(searchFieldValueAdapter);


        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        searchCallBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (Utils.isInternetOn(searchCallActivity.this)) {
                    String type, colName, colValue;
                    String operatorName = null;
                    if (spinnerCallType.getSelectedItem() != null &&
                            !spinnerCallType.getSelectedItem().toString().equalsIgnoreCase("--Select--")) {
                        //Utils.Log("value-----------"+!spinnerCallType.getSelectedItem().toString().equalsIgnoreCase("--Select--"));
                        type = String.valueOf(spinnerCallType.getSelectedItem());
                    } else {
                        type = "";
                    }

                    if (searchCategory.getSelectedItem() != null &&
                            !searchCategory.getSelectedItem().toString().equalsIgnoreCase("--Select--")) {
                        colName = (String) searchCategory.getSelectedItem();
                    } else {
                        colName = "";
                    }
                    if (searchCategoryValue.getSelectedItem() != null
                            && !searchCategoryValue.getSelectedItem().toString().equalsIgnoreCase("--Select--")
                            && !searchCategoryValue.getSelectedItem().toString().equalsIgnoreCase("Bank")) {
                        colValue = (String) searchCategoryValue.getSelectedItem();
                    } else if (searchCategoryValue.getSelectedItem() != null && searchCategoryValue.getSelectedItem().toString().equalsIgnoreCase("Bank")
                            && bankName.getText().toString() != null
                            && !bankName.getText().toString().equalsIgnoreCase("")) {
                        colValue = bankName.getText().toString();
                    } else {
                        colValue = "";
                    }
                    if (spinnerOperator.getSelectedItem() != null
                            && !spinnerOperator.getSelectedItem().toString().equalsIgnoreCase("--Select--")) {
                        operatorName = (String) spinnerOperator.getSelectedItem();
                    } else {
                        operatorName = "";  //(String) spinnerOperator.getAdapter().getItem(0);
                    }
                    preferences = getSharedPreferences("CMS", Context.MODE_PRIVATE);
                    String userId = preferences.getString("userId", null);

                    JSONObject requestObject = new JSONObject();

					/*if(toCallDate.equals("") && fromCallDate.equals("")) {
						Utils.showAlertBox("Select Start and End Date", searchCallActivity.this);
					}else{*/
                    try {
                        requestObject.put("UserId", userId);
                        requestObject.put("CallType", type);
                        requestObject.put("ColumnName", colName);
                        requestObject.put("ColumnValue", colValue);
                        requestObject.put("Operators", operatorName);


                        if (edtDateFrom.getText().toString() != null && !edtDateFrom.getText().toString().equalsIgnoreCase("") &&
                                edtDateTo.getText().toString() != null && !edtDateTo.getText().toString().equalsIgnoreCase("")) {
                            if (!Validation.dateValidation(edtDateFrom.getText().toString(), edtDateTo.getText().toString(), "yyyy-MM-dd")) {
                                Validation.requestFocus(txtDateFrom, searchCallActivity.this);
                                Validation.requestFocus(txtDateTo, searchCallActivity.this);
                                txtDateFrom.setError("From date should be less than todate.");
                                txtDateTo.setError("to date should be greater than from date.");

                                //Utils.showAlertBox("Please Select Proper Date Range",searchCallActivity.this);

                            } else {
                                requestObject.put("CallDateTo", edtDateTo.getText().toString());
                                requestObject.put("CallDateFrom", edtDateFrom.getText().toString());
                                requestObject.put("AtmId", enterAtmId.getText().toString());
                                getCallList(requestObject);
                            }
                        } else {
                            requestObject.put("CallDateFrom", "1-1-0001".toString());
                            requestObject.put("CallDateTo", "1-1-0001".toString());
                            requestObject.put("AtmId", enterAtmId.getText().toString());
                            getCallList(requestObject);
                        }
							/*if(edtDateTo.getText().toString() != null && !edtDateTo.getText().toString().equalsIgnoreCase("")){
								Utils.Log("date======="+edtDateTo.getText().toString());
								requestObject.put("CallDateTo", edtDateTo.getText().toString());
							}else {
								Utils.Log("date=======2"+edtDateTo.getText().toString());
								requestObject.put("CallDateTo","1-1-0001".toString());
								Utils.Log("date======="+requestObject.get("CallDateTo").toString());
							}
							if(edtDateFrom.getText().toString() != null && !edtDateFrom.getText().toString().equalsIgnoreCase("")){
								Utils.Log("date=======From"+edtDateTo.getText().toString());
								requestObject.put("CallDateFrom",edtDateFrom.getText().toString());
							}else {
								Utils.Log("date=======From2"+edtDateTo.getText().toString());
								requestObject.put("CallDateFrom","1-1-0001".toString());
							}*/


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    //}

                } else {
                    Utils.showAlertBox("Internet Connection Problem", searchCallActivity.this);
                }
            }
        });
        new SubStatusAsyncTask(Constants.SUBSTATUS, "GET"
                , searchCallActivity.this).execute();
        new SourceAsyncTask(Constants.SOURCE, "GET"
                , searchCallActivity.this).execute();

        new CallTypesAsyncTask(Constants.CALLTYPES, "GET"
                , searchCallActivity.this).execute();
    }

    private void setValueSpinner(ArrayAdapter<String> searchFieldValueAdapter) {
        searchFieldList = new ArrayList<>();
        searchFieldList.add("--Select--");
        searchFieldValueAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, searchFieldList);
        searchCategoryValue.setAdapter(searchFieldValueAdapter);
    }

    private void setOperatorSpinner(ArrayAdapter<String> operatorAdapter) {

        spinnerOperator.setAdapter(operatorAdapter);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent searchIntent = new Intent(searchCallActivity.this, HomeActivity.class);
        startActivity(searchIntent);
        finish();
    }


    public void getCallList(JSONObject requestObject) {
        SearchCallAsyncTask searchCallAsyncTask = new SearchCallAsyncTask(Constants.SearchCall,
                "POST", requestObject, searchCallActivity.this);
        searchCallAsyncTask.execute();
    }


    public class SearchFieldOnItemSelectedListener implements AdapterView.OnItemSelectedListener {

        String selectedItem = String.valueOf(searchCategory.getSelectedItem());
        String selectedItemSelect = "--Select--";

        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            if (selectedItemSelect.equalsIgnoreCase(String.valueOf(searchCategory.getSelectedItem()))) {
                // ToDo when first item is selected
                //	Utils.showAlertBox("Select Search Category.",searchCallActivity.this);
                setOperatorSpinner(operatorAdapter);
                setValueSpinner(searchFieldValueAdapter);

            } else {
                Toast.makeText(parent.getContext(),
                        "You have selected : " + parent.getItemAtPosition(pos).toString(),
                        Toast.LENGTH_SHORT).show();
                if (parent.getItemAtPosition(pos).toString().equalsIgnoreCase("Sub Status Name")) {
                    txtBankNameLayout.setVisibility(View.GONE);
                    txtValueLayout.setVisibility(View.VISIBLE);
//                    searchFieldList = new String[]{"","Engineer Reached", "Engineer Started", "Repair Started"
//						, "Repair Completed", "Hold"};
                    spinnerOperator.setSelection(1);
                    searchFieldList = substatusList;
                    searchFieldValueAdapter =
                            new ArrayAdapter<String>(searchCallActivity.this, android.R.layout.simple_dropdown_item_1line
                                    , searchFieldList);
                    searchCategoryValue.setAdapter(searchFieldValueAdapter);
                } else if (parent.getItemAtPosition(pos).toString().equalsIgnoreCase("Sub Call Type")) {
                    txtBankNameLayout.setVisibility(View.GONE);
                    txtValueLayout.setVisibility(View.VISIBLE);
                    searchFieldList = new ArrayList(Arrays.asList("PM", "BREAK DOWN", "BAD ACTOR", "CHGC - DOWN", "CHGC - OPERATIONAL", "EJ INSTALLATION",
                            "RECONCILIATION", "INST/UPG", "OPERATIONAL", "SITE ISSUE"));
                    spinnerOperator.setSelection(1);
                    searchFieldValueAdapter =
                            new ArrayAdapter<String>(searchCallActivity.this, android.R.layout.simple_dropdown_item_1line
                                    , searchFieldList);
                    searchCategoryValue.setAdapter(searchFieldValueAdapter);
                } else if (parent.getItemAtPosition(pos).toString().equalsIgnoreCase("Bank")) {
                    txtBankNameLayout.setVisibility(View.VISIBLE);
                    txtValueLayout.setVisibility(View.GONE);
                    spinnerOperator.setSelection(1);
					/*	searchFieldValueAdapter =
							new ArrayAdapter<String>(searchCallActivity.this , android.R.layout.simple_dropdown_item_1line
									,searchFieldList);*/
                    /*searchCategoryValue.setAdapter(searchFieldValueAdapter);*/
                } else if (parent.getItemAtPosition(pos).toString().equalsIgnoreCase("Source")) {
                    txtBankNameLayout.setVisibility(View.GONE);
                    txtValueLayout.setVisibility(View.VISIBLE);
                    // searchFieldList = new String[]{"Help Desk"};
                    spinnerOperator.setSelection(1);
                    searchFieldList = sourceList;
                    searchFieldValueAdapter = new ArrayAdapter<String>(searchCallActivity.this, android.R.layout.simple_dropdown_item_1line
                            , searchFieldList);
                    searchCategoryValue.setAdapter(searchFieldValueAdapter);

                }/*else {
					Utils.showAlertBox("Select Search Category.",searchCallActivity.this);
				}*/
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg) {

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            Intent searchIntent = new Intent(searchCallActivity.this, HomeActivity.class);
            startActivity(searchIntent);
            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    private void showDate(int year, int month, int day, EditText editText) {
        String months = "", days = "";
        if (month < 10) {
            months = "0" + month;
        } else {
            months = String.valueOf(month);
        }

        if (day < 10) days = "0" + day;
        else days = String.valueOf(day);
/*
		editText.setText(new StringBuilder().append(days).append("/")
				.append(months).append("/").append(year));*/
        editText.setText((new StringBuilder().append(year).append("-").append(months).append("-").append(days)));

    }

    private void showDatePicker(final EditText editText, String datePickerType) {
        DatePickerDialog.OnDateSetListener timeSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int y, int monthOfYear, int dayOfMonth) {
                try {
                    year = view.getYear();
                    month = view.getMonth();
                    day = view.getDayOfMonth();
                    showDate(year, month, day, editText);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        DatePickerDialog timePickerDialog = new DatePickerDialog(searchCallActivity.this, timeSetListener, year, month, day);

        timePickerDialog.show();

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.edtDateFrom: //edtDateTo.setSelected(true);
                showDatePicker(edtDateFrom, "from");
                break;
            case R.id.edtDateTo:
                showDatePicker(edtDateTo, "to");
                break;
        }
    }

    public class SearchCallAsyncTask extends CallManagerAsyncTask {

        JSONObject requestObject = new JSONObject();

        public SearchCallAsyncTask(String action, String reqType, JSONObject requestObject, Context context) {
            super(action, reqType, context);
            this.requestObject = requestObject;
        }

        @Override
        protected JSONObject doInBackground(Object... params) {

            try {
                return doWork(requestObject);
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
                Utils.Log("JSON Response=========123" + json.toString());
                try {
                    if (json.has("Status") && json.get("Status").toString().equalsIgnoreCase("Success")) {
                        Utils.Log("response====" + json.toString());
                        Utils.Log(json.getJSONArray("PayLoad").get(0).toString());
                        JSONArray respCallList = json.getJSONArray("PayLoad");

                        ArrayList<CallDTO> callDTOs = new ArrayList<>();

                        if (json.getJSONArray("PayLoad").get(0).toString().equalsIgnoreCase("No record(s) to display.")) {
                            Utils.Log(json.getJSONArray("PayLoad").get(0).toString());
                            Utils.showAlertBox(json.getJSONArray("PayLoad").get(0).toString(), searchCallActivity.this);
                        } else {
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
                                    String resolTIme = Utils.convertToCurrentTimeZone(jsonData.getString("Target_Resolution_Time"));
                                    callDTO.setResolutionTime(resolTIme.toString());
                                }
                                if (jsonData.getString("Call_Type") != null && jsonData.getString("Call_Type") != "") {
                                    callDTO.setCallType(jsonData.getString("Call_Type"));
                                }
                                if (jsonData.getString("Dispatch_Date") != null && jsonData.getString("Dispatch_Date") != "") {
                                    callDTO.setDispatchDate(jsonData.getString("Dispatch_Date"));
                                }
                                if (jsonData.getString("Diagnosis") != null && jsonData.getString("Diagnosis") != "") {
                                    callDTO.setDiagnosis(jsonData.getString("Diagnosis"));
                                }
                                if (jsonData.getString("Status").equalsIgnoreCase("Open")) {
                                    Utils.Log("to titlecase====" + Utils.toTitleCase(jsonData.getString("Sub_Status_Name")));
                                    callDTO.setStatus(Utils.toTitleCase(jsonData.getString("Sub_Status_Name")));
                                    callDTO.setMobileActivity(Utils.toTitleCase(jsonData.getString("Mobile_Activity")));
                                    callDTO.setMainStatus("Open");


                                } else if (jsonData.getString("Status").equalsIgnoreCase("Hold")) {
                                    callDTO.setStatus(Utils.toTitleCase(jsonData.getString("Sub_Status_Name")));
                                    callDTO.setMobileActivity(Utils.toTitleCase(jsonData.getString("Mobile_Activity")));
                                    callDTO.setMainStatus("Hold");

                                } else {
                                    callDTO.setMobileActivity("Close");
                                    callDTO.setMainStatus("Close");
                                    callDTO.setStatus(Utils.toTitleCase(jsonData.getString("Sub_Status_Name")));

                                }

                                if (jsonData.getString("Mobile_Active") != null &&
                                        jsonData.getString("Mobile_Active").equalsIgnoreCase("0")) {
                                    callDTO.setActive(false);
                                } else {
                                    callDTO.setActive(true);
                                }
                                callDTOs.add(callDTO);
                            }
                        }
                        if (callDTOs.size() > 0) {
                            Utils.Log("size---------" + callDTOs.size());
                            Intent i = new Intent(searchCallActivity.this, HomeActivity.class);
                            i.putExtra("callList", callDTOs);
                            startActivity(i);
                            finish();
                        }


                    } else if (json.has("Status") && json.get("Status").toString().equalsIgnoreCase("Failure")) {
                        Utils.showAlertBox(json.getString("ErrorMessage").toString(), searchCallActivity.this);
                    } else {
                        Utils.showAlertBox(json.getString("ErrorMessage").toString(), searchCallActivity.this);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Utils.showAlertBox("Something Went wrong!!", searchCallActivity.this);
            }
        }
    }

    public class SubStatusAsyncTask extends CallManagerAsyncTask {

        JSONObject requestObject = new JSONObject();

        public SubStatusAsyncTask(String action, String reqType, Context context) {
            super(action, reqType, context);

        }

        @Override
        protected JSONObject doInBackground(Object... params) {

            try {
                return doWork(requestObject);
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
                Utils.Log("JSON Response=========234323" + json.toString());

                try {
                    if (json.has("Status") && json.get("Status").toString().equalsIgnoreCase("Success")) {
                        Utils.Log("response====data---" + json.toString());
                        Utils.Log(json.getJSONArray("PayLoad").get(0).toString());
                        JSONArray respCallList = json.getJSONArray("PayLoad");

                        if (json.getJSONArray("PayLoad").get(0).toString().equalsIgnoreCase("No record(s) to display.")) {
                            Utils.Log(json.getJSONArray("PayLoad").get(0).toString());
                            Utils.showAlertBox(json.getJSONArray("PayLoad").get(0).toString(), searchCallActivity.this);
                        } else {
                            for (int i = 0; i < respCallList.length(); i++) {

                                JSONObject jsonData = (JSONObject) respCallList.get(i);
                                if (jsonData.get("DisplayToCustomer").toString().equalsIgnoreCase("1") &&
                                        jsonData.getString("SubStatus") != null && !jsonData.getString("SubStatus").toString().equalsIgnoreCase("")) {
                                    substatusList.add(jsonData.get("SubStatus").toString());
                                }

                            }
                        }


                    } else if (json.has("Status") && json.get("Status").toString().equalsIgnoreCase("Failure")) {
                        Utils.showAlertBox(json.getString("ErrorMessage").toString(), searchCallActivity.this);
                    } else {
                        Utils.showAlertBox(json.getString("ErrorMessage").toString(), searchCallActivity.this);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Utils.showAlertBox("Problem in getting call type value.", searchCallActivity.this);
            }
        }
    }

    public class SourceAsyncTask extends CallManagerAsyncTask {

        JSONObject requestObject = new JSONObject();

        public SourceAsyncTask(String action, String reqType, Context context) {
            super(action, reqType, context);

        }

        @Override
        protected JSONObject doInBackground(Object... params) {

            try {
                return doWork(requestObject);
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
                        Utils.Log("response====" + json.toString());
                        Utils.Log(json.getJSONArray("PayLoad").get(0).toString());
                        JSONArray respCallList = json.getJSONArray("PayLoad");

                        if (json.getJSONArray("PayLoad").get(0).toString().equalsIgnoreCase("No record(s) to display.")) {
                            Utils.Log(json.getJSONArray("PayLoad").get(0).toString());
                            Utils.showAlertBox(json.getJSONArray("PayLoad").get(0).toString(), searchCallActivity.this);
                        } else {
                            for (int i = 0; i < respCallList.length(); i++) {

                                JSONObject jsonData = (JSONObject) respCallList.get(i);
                                if (jsonData.get("DisplayToCustomer").toString().equalsIgnoreCase("1") &&
                                        jsonData.getString("SourceName") != null &&
                                        !jsonData.getString("SourceName").toString().equalsIgnoreCase("")) {
                                    sourceList.add(jsonData.get("SourceName").toString());
                                }

                            }
                        }


                    } else if (json.has("Status") && json.get("Status").toString().equalsIgnoreCase("Failure")) {
                        Utils.showAlertBox(json.getString("ErrorMessage").toString(), searchCallActivity.this);
                    } else {
                        Utils.showAlertBox(json.getString("ErrorMessage").toString(), searchCallActivity.this);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Utils.showAlertBox("Problem in getting call type value.", searchCallActivity.this);
            }
        }
    }

    public class CallTypesAsyncTask extends CallManagerAsyncTask {

        JSONObject requestObject = new JSONObject();

        public CallTypesAsyncTask(String action, String reqType, Context context) {
            super(action, reqType, context);

        }

        @Override
        protected JSONObject doInBackground(Object... params) {

            try {
                return doWork(requestObject);
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
                Utils.Log("JSON Responsecalltypes =========" + json.toString());
                try {
                    if (json.has("Status") && json.get("Status").toString().equalsIgnoreCase("Success")) {
                        Utils.Log("response====" + json.toString());
                        Utils.Log(json.getJSONArray("PayLoad").get(0).toString());
                        JSONArray respCallList = json.getJSONArray("PayLoad");

                        if (json.getJSONArray("PayLoad").get(0).toString().equalsIgnoreCase("No record(s) to display.")) {
                            Utils.Log(json.getJSONArray("PayLoad").get(0).toString());
                            Utils.showAlertBox(json.getJSONArray("PayLoad").get(0).toString(), searchCallActivity.this);
                        } else {
                            callTypeList.add("--Select--");
                            for (int i = 0; i < respCallList.length(); i++) {

                                JSONObject jsonData = (JSONObject) respCallList.get(i);
                                if (!jsonData.getString("Name").toString().equalsIgnoreCase("Name")) {
                                    callTypeList.add(jsonData.get("Name").toString());

                                    Log.d("", "onPostExecutecallTypeList: " + jsonData.getString("Name").toString());

                                    //	Log.d("", "callTypeList: "+callTypeList.add(jsonData.get("Name").toString()));

                                    ArrayAdapter<String> callTypeAdapter = new ArrayAdapter<String>(searchCallActivity.this,
                                            android.R.layout.simple_dropdown_item_1line, callTypeList);

                                    spinnerCallType.setAdapter(callTypeAdapter);
                                }

                            }
                        }


                    } else if (json.has("Status") && json.get("Status").toString().equalsIgnoreCase("Failure")) {
                        Utils.showAlertBox(json.getString("ErrorMessage").toString(), searchCallActivity.this);
                    } else {
                        Utils.showAlertBox(json.getString("ErrorMessage").toString(), searchCallActivity.this);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                //Utils.showAlertBox("Problem in getting call type value.",searchCallActivity.this);
            }
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
