package com.cms.callmanager;

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

import com.cms.callmanager.Foc_Chargeble.APIListner;
import com.cms.callmanager.Foc_Chargeble.ListActivity;
import com.cms.callmanager.Foc_Chargeble.ModelClassForSavedData;
import com.cms.callmanager.Foc_Chargeble.ModelClassForSavedData_Charge;
import com.cms.callmanager.Foc_Chargeble.StatusAdapter;
import com.cms.callmanager.adapter.CustomeSpinnerAdapter;
import com.cms.callmanager.constants.Constant;
import com.cms.callmanager.constants.Constants;
import com.cms.callmanager.dto.ProblemFixDTO;
import com.cms.callmanager.dto.ResonseCategeoryDTO;
import com.cms.callmanager.dto.SolutionDTO;
import com.cms.callmanager.dto.UserDTO;
import com.cms.callmanager.multispinner.CallClloserActivity;
import com.cms.callmanager.multispinner.QuestionairActivity;
import com.cms.callmanager.services.CallManagerAsyncTask;
import com.cms.callmanager.utils.ProgressUtil;
import com.cms.callmanager.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.EOFException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import static com.cms.callmanager.constants.Constant.Pass;
import static com.cms.callmanager.constants.Constant.UserId;
import static com.cms.callmanager.utils.ProgressUtil.progressDialog;

/**
 * Created by Monika zogato on 7/1/17.
 */
public class RepairDetailsActivity extends AppCompatActivity {

    private Spinner spinnerCategory, spinnerSparesUsed,statusSpinnerCategory, spinnerSolution, spinnerProblemFix;
    Button saveRepairDetails;
    // public  static Spinner  statusSpinnerCategory;
    EditText edtTxtTicketNumber, edtTxtDistance, edtTxtComment;
    SharedPreferences preferences;
    Toolbar toolbar;
    CustomeSpinnerAdapter spinnerAdapter = null;
    List<ResonseCategeoryDTO.PayLoad> responseCategoryoadList = null;
    List<SolutionDTO.PayLoad> solutionLoadList = null;
    List<ProblemFixDTO.PayLoad> problemLoadList = new ArrayList<>();
    ArrayList<String> searchField = new ArrayList<String>();

    int status_possiton;
   public static String userId;
    /*= {"Select Status", "Engineer Reached", "Engineer Started", "Repair Started", "Repair Completed", "Hold"};
     */


    // vishnu
    String str_jsonArray_image;
    String str_jsonArray_foc;
    String str_jsonArray_charge;

    String Status_values;
    JSONArray jsonArray_foc;
    JSONArray jsonArray_image;
    JSONArray jsonArray_charge;
    Bundle bundle;

    final int LIST_REQUEST = 1;
    final int LIST_RESULT = 100;
    String key = null;
    ArrayList<ModelClassForSavedData> list;
    ArrayList<ModelClassForSavedData_Charge> list1;
   public static String atm_id;
   public static String docket_no;
    Button btn_view;
    public static int selectedColor = 0;
    String subStatusId;

    LinearLayout spinnerStatusLayout;
    CheckBox checkboxEngineerOnSite, checkboxSparesUsed, checkboxUnproductive, checkboxReturnVisit;
    private Button btn_view_callClosour;

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
        btn_view =(Button)findViewById(R.id.view);
        btn_view_callClosour =(Button)findViewById(R.id.view_Closour);

        btn_view_callClosour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RepairDetailsActivity.this, CallClloserActivity.class);
                startActivity(intent);
            }
        });


        searchField.add("Select");
        ArrayAdapter<String> searchFieldAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, searchField);
        statusSpinnerCategory.setAdapter(searchFieldAdapter);

    /*    StatusAdapter adapter = new StatusAdapter(RepairDetailsActivity.this, searchField);
       // spColors = (Spinner) findViewById(R.id.spColors);
        statusSpinnerCategory.setAdapter(adapter);
*/

        if (getIntent() != null && getIntent().getStringExtra("docketNo") != null) {

            docket_no =(getIntent().getStringExtra("docketNo"));
            edtTxtTicketNumber.setText(getIntent().getStringExtra("docketNo"));
            edtTxtTicketNumber.setEnabled(false);
        }
        if (getIntent() != null && getIntent().getStringExtra("ATM_id") != null) {
            atm_id = (getIntent().getStringExtra("ATM_id"));
            edtTxtTicketNumber.setEnabled(false);
        }

        Log.d("", "docket_no: "+ docket_no);
        Log.d("", "atm_id: "+ atm_id);



        //  Toast.makeText(this, "" + atm_id, Toast.LENGTH_SHORT).show();
        spinnerCategory.setOnItemSelectedListener(new CustomOnItemSelectedListener());
        // statusSpinnerCategory.setOnItemSelectedListener(new CustomOnItemSelectedListener());
        checkboxUnproductive.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    spinnerStatusLayout.setVisibility(View.VISIBLE);

                            if(Status_values != "TSS APPROVAL PENDING")
                            {
                                btn_view_callClosour.setVisibility(View.VISIBLE);
                            } else {
                                btn_view_callClosour.setVisibility(View.GONE);
                            }

                    try {
                        if(list == null || list1 == null || list.size() == 0 || list1.size() == 0){
                            btn_view.setVisibility(View.GONE);

                        }else{
                            btn_view.setVisibility(View.VISIBLE);

                        }
                    }catch (Exception e){}


                    //  view.setVisibility(View.VISIBLE);
                } else {
                    spinnerStatusLayout.setVisibility(View.GONE);
                    btn_view.setVisibility(View.GONE);
                    btn_view_callClosour.setVisibility(View.VISIBLE);
                }

            }
        });


        btn_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(RepairDetailsActivity.this, ListActivity.class);

                if (list == null || list1 == null) {
                    list = new ArrayList<>();
                    list1 = new ArrayList<>();
                }
                i.putExtra("list", list);
                i.putExtra("list1", list1);
                i.putExtra("ATM_ID", atm_id);
                i.putExtra("DOCKET_NO",docket_no );
                i.putExtra("subStatusId",subStatusId);

                startActivityForResult(i, LIST_REQUEST);


            }
        });

        statusSpinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                selectedColor = position;

                statusSpinnerCategory.setSelection(position);

                status_possiton =  statusSpinnerCategory.getSelectedItemPosition();


                Log.d("", "onItemSelected_status_possiton: "+status_possiton);

                Status_values = statusSpinnerCategory.getItemAtPosition(position).toString();
                //  Toast.makeText(RepairDetailsActivity.this, Status_values, Toast.LENGTH_SHORT).show();
                if(statusSpinnerCategory.getItemAtPosition(position).toString() != "Select") {

                    if ("TSS APPROVAL PENDING".equalsIgnoreCase(Status_values)) {


                        Intent i = new Intent(RepairDetailsActivity.this, ListActivity.class);

                        if (list == null || list1 == null) {
                            list = new ArrayList<>();
                            list1 = new ArrayList<>();
                        }
                        i.putExtra("list", list);
                        i.putExtra("list1", list1);
                        i.putExtra("ATM_ID", atm_id);
                        i.putExtra("DOCKET_NO", docket_no);
                        i.putExtra("subStatusId", subStatusId);

                        startActivityForResult(i, LIST_REQUEST);

                        btn_view_callClosour.setVisibility(View.GONE);

                    } else {

                        try {
                            if (list == null || list1 == null) {
                                btn_view.setVisibility(View.GONE);

                            } else {
                                btn_view.setVisibility(View.VISIBLE);

                            }
                            //******** added by vibha
                            btn_view_callClosour.setVisibility(View.VISIBLE);
                            Intent intent = new Intent(RepairDetailsActivity.this, CallClloserActivity.class);
                            startActivity(intent);
                        } catch (Exception e) {

                        }


                        btn_view.setVisibility(View.GONE);

                    }
                }
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
                    userId = preferences.getString("userId", null);
                    try {
                        postParamData.put("Docket_No", getIntent().getStringExtra("docketNo"));
                        postParamData.put("Problem_Fix", payLoadId);
                        postParamData.put("Solution", sPayLoadId);

                        //      postParamData.put("responsecategory", rCPayLoadId); prob fix, sol, response
                        if (statusSpinnerCategory.getSelectedItem().toString() != null &&
                                !statusSpinnerCategory.getSelectedItem().toString().equals("Select")) {
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
                       /* if (checkboxUnproductive.isChecked()) {

                            Utils.Log("In checked-----------");
                            postParamData.put("Un_Productive_Visit", "1");
                            if (statusSpinnerCategory.getSelectedItem().toString() != null) {
                                Utils.Log("======" + statusSpinnerCategory.getSelectedItem().toString());
                                postParamData.put("Sub_Status_Name", statusSpinnerCategory.getSelectedItem().toString());



                                // ===================== Call FOC_SAVE API Serices ===========================

                                if ("TSS APPROVAL PENDING".equalsIgnoreCase(Status_values)) {

                                    if (key.equalsIgnoreCase("foc")) {

                                        new Save_foc_list_asyntask(Constants.SAVE_FOC_URL, "POST"
                                                , RepairDetailsActivity.this).execute();
                                    } else {

                                     //   if( userId != null) {
                                            new Save_charge_images_asyntask(Constants.SAVE_CHARGE_IMAGES_URL + " ?userId=" + userId, "POST"
                                                    , RepairDetailsActivity.this).execute();
                                     *//*   }else{

                                        }*//*


                                        new Save_charge_list_asyntask(Constants.SAVE_CHARGE_URL, "POST"
                                                , RepairDetailsActivity.this).execute();
                                    }
                                }else{

                                }


                            }
                        } else {
                            Utils.Log("In Unchecked-----------");
                            postParamData.put("Un_Productive_Visit", "0");
                            postParamData.put("Sub_Status_Name", "REPAIR COMPLETED");
                        }
*/
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
                        /*new RepairDetailsAsyncTask(Constants.FOLLOWUP, "POST"
                                , RepairDetailsActivity.this, postParamData).execute();*/

                        ///////////////////////////////////////////////////////////////
                        try {
                            if (checkboxUnproductive.isChecked()) {

                                Utils.Log("In checked-----------");
                                postParamData.put("Un_Productive_Visit", "1");
                                if (statusSpinnerCategory.getSelectedItem().toString() != null) {
                                    if(statusSpinnerCategory.getSelectedItem().toString() == "TSS APPROVAL PENDING" && list1.size() > 0)
                                    {
                                        postParamData.put("Sub_Status_Name", "103");
                                    }else {
                                        Utils.Log("======" + statusSpinnerCategory.getSelectedItem().toString());
                                        postParamData.put("Sub_Status_Name", statusSpinnerCategory.getSelectedItem().toString());
                                    }

                                }
                            } else {
                                Utils.Log("In Unchecked-----------");
                                postParamData.put("Un_Productive_Visit", "0");
                                postParamData.put("Sub_Status_Name", "REPAIR COMPLETED");
                            }
                        }catch (Exception e){
                        }
                        if(checkboxUnproductive.isChecked())
                        {
                            if(statusSpinnerCategory.getSelectedItem().toString().equals("TSS APPROVAL PENDING") && list1.size() > 0)
                            {
                                if(CallClloserActivity.CallClosurejson !=null)
                                {
                                    new RepairDetailsAsyncTask(Constants.FOLLOWUP, "POST"
                                            , RepairDetailsActivity.this, postParamData).execute();
                                }else
                                {
                                    Utils.showAlertBox("Please enter values for Call Closure",RepairDetailsActivity.this);
                                }

                            }else if(statusSpinnerCategory.getSelectedItem().toString().equals("TSS APPROVAL PENDING") && list.size() > 0)
                            {
                                new RepairDetailsAsyncTask(Constants.FOLLOWUP, "POST"
                                        , RepairDetailsActivity.this, postParamData).execute();

                            }else if(!statusSpinnerCategory.getSelectedItem().toString().equals("Select") && !statusSpinnerCategory.getSelectedItem().toString().equals("TSS APPROVAL PENDING")){
                                if(CallClloserActivity.CallClosurejson !=null)
                                {
                                    new RepairDetailsAsyncTask(Constants.FOLLOWUP, "POST"
                                            , RepairDetailsActivity.this, postParamData).execute();
                                }else
                                {
                                    Utils.showAlertBox("Please enter values for Call Closure",RepairDetailsActivity.this);
                                }
                            }
                            else {
                                Utils.showAlertBox("Please enter FOC or Chargable data",RepairDetailsActivity.this);
                            }
                        }else {
                            if(CallClloserActivity.CallClosurejson !=null)
                            {
                                new RepairDetailsAsyncTask(Constants.FOLLOWUP, "POST"
                                        , RepairDetailsActivity.this, postParamData).execute();
                            }else
                            {
                                Utils.showAlertBox("Please enter values for Call Closure",RepairDetailsActivity.this);
                            }
                        }




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
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);

                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();

// show it
        alertDialog.show();
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
                                json.getString("ErrorMessage"),
                                Toast.LENGTH_SHORT).show();
                      //  Utils.showAlertBox(json.getString("ErrorMessage"), RepairDetailsActivity.this);
                       // showRepairCompleteAlert();
                        if(checkboxUnproductive.isChecked())
                        {

                            if ("TSS APPROVAL PENDING".equalsIgnoreCase(Status_values)) {
                                if(key != null)
                                {
                                    if (key.equalsIgnoreCase("foc")) {

                                        // ============== FOC SAVE=====
                                        new Save_foc_list_asyntask(Constants.SAVE_FOC_URL, "POST"
                                                , RepairDetailsActivity.this).execute();
                                    } else {

                                        // Call Closour
                                        /*if(CallClloserActivity.CallClosurejson != null)
                                        {
                                            if(CallClloserActivity.CallClosurejson.length() > 0)
                                            {
                                                SaveCallClouser saveCallClouser = new SaveCallClouser(Constants.SAVE_CALL_CLOSOUR + "?DocketNo="+docket_no , "POST", CallClloserActivity.CallClosurejson, RepairDetailsActivity.this);
                                                saveCallClouser.execute();
                                            }
                                            new Save_charge_list_asyntask(Constants.SAVE_CHARGE_URL, "POST"
                                                    , RepairDetailsActivity.this).execute();

                                            UploadFileChargeImage uploadFileChargeImage = new UploadFileChargeImage(Constants.SAVE_CHARGE_IMAGES_URL +"?userId=" + userId , "POST", ListActivity.jsonArray_for_images, RepairDetailsActivity.this);
                                            uploadFileChargeImage.execute();

                                        }*/


                                        if(CallClloserActivity.IdleHoursjson != null)
                                        {
                                            if(CallClloserActivity.IdleHoursjson.length() > 0)
                                            {
                                                SaveIdleHours saveIdleHours = new SaveIdleHours(Constants.SAVE_CALL_CLOSOUR_IdealHours + "?DocketNo="+docket_no , "POST", CallClloserActivity.IdleHoursjson, RepairDetailsActivity.this);
                                                saveIdleHours.execute();
                                            }
                                        }else {

                                            if(CallClloserActivity.ImagesJsonArray != null)
                                            {
                                                if( CallClloserActivity.ImagesJsonArray.length() > 0)
                                                {
                                                    UploadFileCallClosour uploadFileCallClosour = new UploadFileCallClosour(Constants.SAVE_CALL_CLOSOUR_IMAGES + "?UserId="+userId , "POST", CallClloserActivity.ImagesJsonArray, RepairDetailsActivity.this);
                                                    uploadFileCallClosour.execute();
                                                }

                                            }

                                            if(QuestionairActivity.QuestionJsonArray != null)
                                            {
                                                if( QuestionairActivity.QuestionJsonArray.length() > 0)
                                                {
                                                    SaveQuestionCallClouser saveQuestionCallClouser = new SaveQuestionCallClouser(Constants.SAVE_CALL_CLOSOUR_QUESTION + "?DocketNo="+docket_no , "POST", QuestionairActivity.QuestionJsonArray, RepairDetailsActivity.this);
                                                    saveQuestionCallClouser.execute();
                                                }
                                            }
                                            if(CallClloserActivity.CallClosurejson != null)
                                            {
                                                if(CallClloserActivity.CallClosurejson.length() > 0)
                                                {
                                                    SaveCallClouser saveCallClouser = new SaveCallClouser(Constants.SAVE_CALL_CLOSOUR + "?DocketNo="+docket_no , "POST", CallClloserActivity.CallClosurejson, RepairDetailsActivity.this);
                                                    saveCallClouser.execute();
                                                }
                                            }

                                            new Save_charge_list_asyntask(Constants.SAVE_CHARGE_URL, "POST"
                                                    , RepairDetailsActivity.this).execute();

                                            UploadFileChargeImage uploadFileChargeImage = new UploadFileChargeImage(Constants.SAVE_CHARGE_IMAGES_URL +"?userId=" + userId , "POST", ListActivity.jsonArray_for_images, RepairDetailsActivity.this);
                                            uploadFileChargeImage.execute();



                                        }

                                        // ============== CHARGE SAVE=====
                                    }
                                }

                            } else {

                                // Call Closour
                                if(CallClloserActivity.IdleHoursjson != null)
                                {
                                    if(CallClloserActivity.IdleHoursjson.length() > 0)
                                    {
                                        SaveIdleHours saveIdleHours = new SaveIdleHours(Constants.SAVE_CALL_CLOSOUR_IdealHours + "?DocketNo="+docket_no , "POST", CallClloserActivity.IdleHoursjson, RepairDetailsActivity.this);
                                        saveIdleHours.execute();
                                    }
                                }else {

                                    if(CallClloserActivity.ImagesJsonArray != null)
                                    {
                                        if( CallClloserActivity.ImagesJsonArray.length() > 0)
                                        {
                                            UploadFileCallClosour uploadFileCallClosour = new UploadFileCallClosour(Constants.SAVE_CALL_CLOSOUR_IMAGES + "?UserId="+userId , "POST", CallClloserActivity.ImagesJsonArray, RepairDetailsActivity.this);
                                            uploadFileCallClosour.execute();
                                        }

                                    }

                                    if(QuestionairActivity.QuestionJsonArray != null)
                                    {
                                        if( QuestionairActivity.QuestionJsonArray.length() > 0)
                                        {
                                            SaveQuestionCallClouser saveQuestionCallClouser = new SaveQuestionCallClouser(Constants.SAVE_CALL_CLOSOUR_QUESTION + "?DocketNo="+docket_no , "POST", QuestionairActivity.QuestionJsonArray, RepairDetailsActivity.this);
                                            saveQuestionCallClouser.execute();
                                        }
                                    }
                                    if(CallClloserActivity.CallClosurejson != null)
                                    {
                                        if(CallClloserActivity.CallClosurejson.length() > 0)
                                        {
                                            SaveCallClouser saveCallClouser = new SaveCallClouser(Constants.SAVE_CALL_CLOSOUR + "?DocketNo="+docket_no , "POST", CallClloserActivity.CallClosurejson, RepairDetailsActivity.this);
                                            saveCallClouser.execute();
                                        }
                                    }


                                }



                            }
                        }else {
                          //  Utils.showAlertBox(json.getString("ErrorMessage"), RepairDetailsActivity.this);
                            // Call Closour
                         /*   if(CallClloserActivity.CallClosurejson != null)
                            {
                                if(CallClloserActivity.CallClosurejson.length() > 0)
                                {
                                    SaveCallClouser saveCallClouser = new SaveCallClouser(Constants.SAVE_CALL_CLOSOUR + "?DocketNo="+docket_no , "POST", CallClloserActivity.CallClosurejson, RepairDetailsActivity.this);
                                    saveCallClouser.execute();
                                }
                            }*/
                            if(CallClloserActivity.IdleHoursjson != null)
                            {
                                if(CallClloserActivity.IdleHoursjson.length() > 0)
                                {
                                    SaveIdleHours saveIdleHours = new SaveIdleHours(Constants.SAVE_CALL_CLOSOUR_IdealHours + "?DocketNo="+docket_no , "POST", CallClloserActivity.IdleHoursjson, RepairDetailsActivity.this);
                                    saveIdleHours.execute();
                                }
                            }else {

                                if(CallClloserActivity.ImagesJsonArray != null)
                                {
                                    if( CallClloserActivity.ImagesJsonArray.length() > 0)
                                    {
                                        UploadFileCallClosour uploadFileCallClosour = new UploadFileCallClosour(Constants.SAVE_CALL_CLOSOUR_IMAGES + "?UserId="+userId , "POST", CallClloserActivity.ImagesJsonArray, RepairDetailsActivity.this);
                                        uploadFileCallClosour.execute();
                                    }

                                }

                                if(QuestionairActivity.QuestionJsonArray != null)
                                {
                                    if( QuestionairActivity.QuestionJsonArray.length() > 0)
                                    {
                                        SaveQuestionCallClouser saveQuestionCallClouser = new SaveQuestionCallClouser(Constants.SAVE_CALL_CLOSOUR_QUESTION + "?DocketNo="+docket_no , "POST", QuestionairActivity.QuestionJsonArray, RepairDetailsActivity.this);
                                        saveQuestionCallClouser.execute();
                                    }
                                }
                                if(CallClloserActivity.CallClosurejson != null)
                                {
                                    if(CallClloserActivity.CallClosurejson.length() > 0)
                                    {
                                        SaveCallClouser saveCallClouser = new SaveCallClouser(Constants.SAVE_CALL_CLOSOUR + "?DocketNo="+docket_no , "POST", CallClloserActivity.CallClosurejson, RepairDetailsActivity.this);
                                        saveCallClouser.execute();
                                    }
                                }


                            }

                            //    showRepairCompleteAlert();
                        }



                    } else if (json.has("Status") && json.get("Status").toString().equalsIgnoreCase("Failure")) {
                        Utils.showAlertBox(json.getString("ErrorMessage").toString(), RepairDetailsActivity.this);
                    } /*else {
                        Utils.showAlertBox(json.getString("ErrorMessage").toString(), RepairDetailsActivity.this);
                    }*/
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Utils.showAlertBox("Something Went wrong!!", RepairDetailsActivity.this);
            }
        }

/*        private void showRepairCompleteAlert() {
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
        }*/

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
                                    subStatusId = jsonData.get("SubStatusId").toString();
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LIST_REQUEST && resultCode == LIST_RESULT)

            try {

                btn_view.setVisibility(View.VISIBLE);

                str_jsonArray_foc = data.getStringExtra("jsonArrayfor_foc");
                str_jsonArray_charge = data.getStringExtra("jsonArrayfor_charge");
                jsonArray_image = ListActivity.jsonArray_for_images;

                Log.d("", "onActivityResult__jsonArray_image: "+ jsonArray_image);


//            jsonArray_foc = new JSONArray(str_jsonArray_foc);
                //           Log.d("str_jsonArray_foc", str_jsonArray_foc);

                jsonArray_charge = new JSONArray(str_jsonArray_charge);
                if(jsonArray_charge != null)
                {
                    if(jsonArray_charge.length() > 0)
                    {
                        btn_view_callClosour.setVisibility(View.VISIBLE);
                    }
                    Log.d("str_jsonArray_charge", jsonArray_charge.toString());
                }



           /* jsonArray_image = new JSONArray(str_jsonArray_image);
            Log.d("str_jsonArray_foc", str_jsonArray_image);*/


            } catch (Exception e) {
                e.printStackTrace();
            }


        try {
            key = data.getStringExtra("key");

            if (key != null || (list != null) || (list1 != null)) {


                if (key.equalsIgnoreCase("foc")) {
                    list = (ArrayList<ModelClassForSavedData>) data.getSerializableExtra("list");

                } else if (key.equalsIgnoreCase("charge")) {


                    list1 = (ArrayList<ModelClassForSavedData_Charge>) data.getSerializableExtra("list1");


                }
            } else {

            }
        } catch (Exception e) {

        }
    }


    //  new UserAsyncTask(Constants.SAVE_FOC_URL, "POST", RepairDetailsActivity.this ));execute();


    class Save_foc_list_asyntask extends CallManagerAsyncTask {



        public Save_foc_list_asyntask(String action, String reqType, Context context) {
            super(action, reqType, context);

        }

        @Override
        protected JSONObject doInBackground(Object... params) {

            JSONObject postParamData = new JSONObject();
            try {
                postParamData.put("ListFocPartRequestModel", ListActivity.jsonArrayfor_foc);


                Log.d("", "doInBackground: "+postParamData);

                //   postParamData.put("Password", password);
                return doWork(postParamData);
            } catch (JSONException e) {
                e.printStackTrace();
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
                Utils.Log("JSON Response=========123456789" + json.toString());
                try {
                    if (json.has("Status") && json.get("Status").toString().equalsIgnoreCase("Success")) {

                        Utils.Log("response==== Save FOC" + json.toString());
                      //  showRepairCompleteAlert();
                        Utils.Log(json.getJSONArray("PayLoad").get(0).toString());
                        JSONObject user = (JSONObject) json.getJSONArray("PayLoad").get(0);

                        //  Toast.makeText(RepairDetailsActivity.this, "Save DATA", Toast.LENGTH_SHORT).show();



                        // store shared preference

                    } else if (json.has("Status") && json.get("Status").toString().equalsIgnoreCase("Failure")) {
                        Utils.showAlertBox(json.getString("ErrorMessage"), RepairDetailsActivity.this);
                    } else {
                        Utils.showAlertBox(json.getString("ErrorMessage"), RepairDetailsActivity.this);
                      //  showRepairCompleteAlert();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                finally {
                    showRepairCompleteAlert();
                }
            } else {
                try {
                    Utils.showAlertBox(json.getString("ErrorMessage"), RepairDetailsActivity.this);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }


    }













    class Save_charge_list_asyntask extends CallManagerAsyncTask {


        public Save_charge_list_asyntask(String action, String reqType, Context context) {
            super(action, reqType, context);

        }

        @Override
        protected JSONObject doInBackground(Object... params) {

            JSONObject postParamData = new JSONObject();
            try {


                postParamData.put("DocketNo", docket_no);
                postParamData.put("ATMId", atm_id);
                postParamData.put("EngineerId", Prefs.with(RepairDetailsActivity.this).getString(UserId, ""));
                postParamData.put("Longitude", Prefs.with(RepairDetailsActivity.this).getString(Constant.LONGI, ""));
                postParamData.put("Latitude", Prefs.with(RepairDetailsActivity.this).getString(Constant.LATI, ""));
                postParamData.put("MobileDeviceId", Prefs.with(RepairDetailsActivity.this).getString(Constant.DEVICE_ID, ""));
                postParamData.put("ResponseType","Chargeable");
                postParamData.put("SubStatus",subStatusId);
                postParamData.put("PartRequestList", ListActivity.jsonArrayfor_charge);

                Log.d("", "doInBackgroundsubStatusId: "+subStatusId);
                Log.d("", "doInBackground12345: "+postParamData);

                //   postParamData.put("Password", password);
                return doWork(postParamData);
            } catch (JSONException e) {
                e.printStackTrace();
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
                Utils.Log("JSON Response=========123456789" + json.toString());
                try {
                    if (json.has("Status") && json.get("Status").toString().equalsIgnoreCase("Success")) {
                        Utils.Log("response==== Save FOC" + json.toString());

                        Utils.showAlertBox(json.getString("ErrorMessage"), RepairDetailsActivity.this);

                        Log.d("", "onPostExecuteSUCCESS: "+json.getString("ErrorMessage"));
                        Utils.Log(json.getJSONArray("PayLoad").get(0).toString());
                        JSONObject user = (JSONObject) json.getJSONArray("PayLoad").get(0);




                        //  Toast.makeText(RepairDetailsActivity.this, "Save DATA", Toast.LENGTH_SHORT).show();

                        /*if( userId != null) {
                            new Save_charge_images_asyntask(Constants.SAVE_CHARGE_IMAGES_URL + " ?userId=" + userId, "POST"
                                    , RepairDetailsActivity.this).execute();
                        }else{

                        }*/

                        // store shared preference

                    } else if (json.has("Status") && json.get("Status").toString().equalsIgnoreCase("Failure")) {
                        Utils.showAlertBox(json.getString("ErrorMessage"), RepairDetailsActivity.this);
                    } else {
                        Utils.showAlertBox(json.getString("ErrorMessage"), RepairDetailsActivity.this);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    Utils.showAlertBox(json.getString("ErrorMessage"), RepairDetailsActivity.this);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }


    }




    class Save_charge_images_asyntask extends CallManagerAsyncTask {


        public Save_charge_images_asyntask(String action, String reqType, Context context) {
            super(action, reqType, context);

        }

        @Override
        protected JSONObject doInBackground(Object... params) {

            JSONObject postParamData = new JSONObject();
            try {
                postParamData.put("IMAGES", jsonArray_image);


                Log.d("", "doInBackgroundimagesssss: "+postParamData);

                //   postParamData.put("Password", password);
                return doWork(postParamData);
            } catch (JSONException e) {
                e.printStackTrace();
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
            //   ProgressUtil.hideProgressBar(findViewById(R.id.root), R.id.progressBar);
            if (json != null) {
                Utils.Log("JSON Response=========12345678911111" + json.toString());
                try {
                    if (json.has("Status") && json.get("Status").toString().equalsIgnoreCase("Success")) {

                        Utils.Log("response==== Save FOC" + json.toString());
                        Utils.Log(json.getJSONArray("PayLoad").get(0).toString());
                        JSONObject user = (JSONObject) json.getJSONArray("PayLoad").get(0);

                        //  Toast.makeText(RepairDetailsActivity.this, "Save DATA", Toast.LENGTH_SHORT).show();


                    } else if (json.has("Status") && json.get("Status").toString().equalsIgnoreCase("Failure")) {
                        Utils.showAlertBox(json.getString("ErrorMessage"), RepairDetailsActivity.this);
                    } else {
                        Utils.showAlertBox(json.getString("ErrorMessage"), RepairDetailsActivity.this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    Utils.showAlertBox(json.getString("ErrorMessage"), RepairDetailsActivity.this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }


    }



    public class UploadFileChargeImage extends CallManagerAsyncTask {

        String comment,uploadedFile;
        JSONArray Charge_image;
        public UploadFileChargeImage(String action, String reqType, JSONArray charge_image, Context context){
            super(action, reqType, context);
            this.Charge_image = charge_image;
            // this.uploadedFile = uploadedFile;

        }

        @Override
        protected JSONObject doInBackground(Object... params) {

            JSONObject postParamData = new JSONObject();
            try {

                return doWorkJSONArray(Charge_image);
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
           /* ProgressUtil.showProgressBar(CallClloserActivity.this,
                    findViewById(R.id.root), R.id.progressBar);*/
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            super.onPostExecute(json);
            // ProgressUtil.hideProgressBar(findViewById(R.id.root), R.id.progressBar);
            if(json != null){
                Utils.Log("JSON Response========="+json.toString());
                try {
                    if(json.has("Status") && json.get("Status").toString().equalsIgnoreCase("Success")){

                        Utils.showAlertBox(json.getString("ErrorMessage").toString(),RepairDetailsActivity.this);
                      //  showRepairCompleteAlert();


                    }else if(json.has("Status") && json.get("Status").toString().equalsIgnoreCase("Failure")) {
                        Utils.showAlertBox(json.getString("ErrorMessage").toString(),RepairDetailsActivity.this);
                    }else {
                        Utils.showAlertBox(json.getString("ErrorMessage").toString(),RepairDetailsActivity.this);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                finally {
                    showRepairCompleteAlert();
                }
            }else {
                Utils.showAlertBox("Something Went wrong!!",RepairDetailsActivity.this);
               /* Intent i = new Intent(CallClloserActivity.this,CallClloserActivity.class);
                startActivity(i);*/
            }
        }
    }



    // Call closour aync task
    public class SaveCallClouser extends CallManagerAsyncTask {

        String comment,uploadedFile;
        JSONObject callClosour;
        public SaveCallClouser(String action, String reqType, JSONObject CallClosur, Context context){
            super(action, reqType, context);
            this.callClosour = CallClosur;
            // this.uploadedFile = uploadedFile;

        }

        @Override
        protected JSONObject doInBackground(Object... params) {

            JSONObject postParamData = new JSONObject();
            try {
              /*  postParamData.put("DocketNo",docketNo);
                postParamData.put("File",uploadedFile);
                postParamData.put("FileName",userId+String.valueOf(System.currentTimeMillis())+".jpg");
                //  postParamData.put("FileName","");

                postParamData.put("Comments",comment);*/
                return doWork(callClosour);
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
            ProgressUtil.showProgressBar(RepairDetailsActivity.this,
                    findViewById(R.id.root), R.id.progressBar);
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            super.onPostExecute(json);
            ProgressUtil.hideProgressBar(findViewById(R.id.root), R.id.progressBar);
            if(json != null){
                Utils.Log("JSON Response========="+json.toString());
                try {
                    if(json.has("Status") && json.get("Status").toString().equalsIgnoreCase("Success")){

                        Utils.showAlertBox(json.getString("ErrorMessage").toString(),RepairDetailsActivity.this);
                       /* if(list1 != null)
                        {

                        }else {
                            showRepairCompleteAlert();
                        }*/

                 /*       if(CallClloserActivity.IdleHoursjson != null)
                        {
                            if(CallClloserActivity.IdleHoursjson.length() > 0)
                            {
                                SaveIdleHours saveIdleHours = new SaveIdleHours(Constants.SAVE_CALL_CLOSOUR_IdealHours + "?DocketNo="+docket_no , "POST", CallClloserActivity.IdleHoursjson, RepairDetailsActivity.this);
                                saveIdleHours.execute();
                            }
                        }

                    if(CallClloserActivity.ImagesJsonArray != null)
                    {
                        if( CallClloserActivity.ImagesJsonArray.length() > 0)
                        {
                            UploadFileCallClosour uploadFileCallClosour = new UploadFileCallClosour(Constants.SAVE_CALL_CLOSOUR_IMAGES + "?UserId="+userId , "POST", CallClloserActivity.ImagesJsonArray, RepairDetailsActivity.this);
                            uploadFileCallClosour.execute();
                        }

                    }

                    if(QuestionairActivity.QuestionJsonArray != null)
                    {
                        if( QuestionairActivity.QuestionJsonArray.length() > 0)
                        {
                            SaveQuestionCallClouser saveQuestionCallClouser = new SaveQuestionCallClouser(Constants.SAVE_CALL_CLOSOUR_QUESTION + "?DocketNo="+docket_no , "POST", QuestionairActivity.QuestionJsonArray, RepairDetailsActivity.this);
                            saveQuestionCallClouser.execute();
                        }
                    }*/
                    //    showRepairCompleteAlert();

         /*               android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(RepairDetailsActivity.this);

                        builder.setTitle("CMS");
                        builder.setMessage(json.getString("ErrorMessage").toString()).setNegativeButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User clicked OK button
                                dialog.dismiss();
                                if(CallClloserActivity.IdleHoursjson.length() > 0)
                                {
                                    SaveIdleHours saveIdleHours = new SaveIdleHours(Constants.SAVE_CALL_CLOSOUR_IdealHours + "?DocketNo="+docket_no , "POST", CallClloserActivity.IdleHoursjson, RepairDetailsActivity.this);
                                    saveIdleHours.execute();
                                }

                                if( CallClloserActivity.ImagesJsonArray.length() > 0)
                                {
                                    UploadFileCallClosour uploadFileCallClosour = new UploadFileCallClosour(Constants.SAVE_CALL_CLOSOUR_IMAGES + "?UserId="+userId , "POST", CallClloserActivity.ImagesJsonArray, RepairDetailsActivity.this);
                                    uploadFileCallClosour.execute();
                                }

                                if( QuestionairActivity.QuestionJsonArray.length() > 0)
                                {
                                    SaveQuestionCallClouser saveQuestionCallClouser = new SaveQuestionCallClouser(Constants.SAVE_CALL_CLOSOUR_QUESTION + "?DocketNo="+docket_no , "POST", QuestionairActivity.QuestionJsonArray, RepairDetailsActivity.this);
                                    saveQuestionCallClouser.execute();
                                }
                            }
                        });
                        builder.create().show();*/



                       //   Toast.makeText(RepairDetailsActivity.this, json.getString("ErrorMessage").toString(), Toast.LENGTH_SHORT).show();


                    }else if(json.has("Status") && json.get("Status").toString().equalsIgnoreCase("Failure")) {
                        Utils.showAlertBox(json.getString("ErrorMessage").toString(),RepairDetailsActivity.this);
                    }else {
                        Utils.showAlertBox(json.getString("ErrorMessage").toString(),RepairDetailsActivity.this);
                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }
               finally {

                        showRepairCompleteAlert();

                }
            }else {
                Utils.showAlertBox("Something Went wrong!!",RepairDetailsActivity.this);
               /* Intent i = new Intent(CallClloserActivity.this,CallClloserActivity.class);
                startActivity(i);*/
            }
        }
    }

    public class SaveIdleHours extends CallManagerAsyncTask {

        String comment,uploadedFile;
        JSONObject callClosour_idealHours;
        public SaveIdleHours(String action, String reqType, JSONObject CallClosur_ideal, Context context){
            super(action, reqType, context);
            this.callClosour_idealHours = CallClosur_ideal;
            // this.uploadedFile = uploadedFile;

        }

        @Override
        protected JSONObject doInBackground(Object... params) {

            try {

                return doWork(callClosour_idealHours);
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
            ProgressUtil.showProgressBar(RepairDetailsActivity.this,
                    findViewById(R.id.root), R.id.progressBar);
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            super.onPostExecute(json);
            ProgressUtil.hideProgressBar(findViewById(R.id.root), R.id.progressBar);
            if(json != null){
                Utils.Log("JSON Response========="+json.toString());
                try {
                    if(json.has("Status") && json.get("Status").toString().equalsIgnoreCase("Success")){
                        /*Intent i = new Intent(AttachImageActivity.this, AcknowledgementActivity.class);
                        i.putExtra("docketNo",docketNo);
                        startActivity(i);
                        finish();*/
                        //  progressDialog.dismiss();
                      //  Utils.showAlertBox(json.getString("ErrorMessage").toString(),RepairDetailsActivity.this);
                          Toast.makeText(RepairDetailsActivity.this, json.getString("ErrorMessage").toString(), Toast.LENGTH_LONG).show();


                        if ("TSS APPROVAL PENDING".equalsIgnoreCase(Status_values)) {
                            if(key != null)
                            {
                                if (key.equalsIgnoreCase("foc")) {

                                    // ============== FOC SAVE=====
                                   /* new Save_foc_list_asyntask(Constants.SAVE_FOC_URL, "POST"
                                            , RepairDetailsActivity.this).execute();*/
                                } else {

                                    // Call Closour
                                    if(CallClloserActivity.CallClosurejson != null)
                                    {
                                      /*  if(CallClloserActivity.CallClosurejson.length() > 0)
                                        {
                                            SaveCallClouser saveCallClouser = new SaveCallClouser(Constants.SAVE_CALL_CLOSOUR + "?DocketNo="+docket_no , "POST", CallClloserActivity.CallClosurejson, RepairDetailsActivity.this);
                                            saveCallClouser.execute();
                                        }*/
                                        new Save_charge_list_asyntask(Constants.SAVE_CHARGE_URL, "POST"
                                                , RepairDetailsActivity.this).execute();

                                        UploadFileChargeImage uploadFileChargeImage = new UploadFileChargeImage(Constants.SAVE_CHARGE_IMAGES_URL +"?userId=" + userId , "POST", ListActivity.jsonArray_for_images, RepairDetailsActivity.this);
                                        uploadFileChargeImage.execute();

                                    }
                                }
                            }

                        }

                        if(CallClloserActivity.ImagesJsonArray != null)
                        {
                            if( CallClloserActivity.ImagesJsonArray.length() > 0)
                            {
                                UploadFileCallClosour uploadFileCallClosour = new UploadFileCallClosour(Constants.SAVE_CALL_CLOSOUR_IMAGES + "?UserId="+userId , "POST", CallClloserActivity.ImagesJsonArray, RepairDetailsActivity.this);
                                uploadFileCallClosour.execute();
                            }

                        }

                        if(QuestionairActivity.QuestionJsonArray != null)
                        {
                            if( QuestionairActivity.QuestionJsonArray.length() > 0)
                            {
                                SaveQuestionCallClouser saveQuestionCallClouser = new SaveQuestionCallClouser(Constants.SAVE_CALL_CLOSOUR_QUESTION + "?DocketNo="+docket_no , "POST", QuestionairActivity.QuestionJsonArray, RepairDetailsActivity.this);
                                saveQuestionCallClouser.execute();
                            }
                        }
                        if(CallClloserActivity.CallClosurejson != null)
                        {
                            if(CallClloserActivity.CallClosurejson.length() > 0)
                            {
                                SaveCallClouser saveCallClouser = new SaveCallClouser(Constants.SAVE_CALL_CLOSOUR + "?DocketNo="+docket_no , "POST", CallClloserActivity.CallClosurejson, RepairDetailsActivity.this);
                                saveCallClouser.execute();
                            }
                        }


                    }else if(json.has("Status") && json.get("Status").toString().equalsIgnoreCase("Failure")) {
                        Utils.showAlertBox(json.getString("ErrorMessage").toString(),RepairDetailsActivity.this);
                    }else {
                        Utils.showAlertBox(json.getString("ErrorMessage").toString(),RepairDetailsActivity.this);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                finally {
                    showRepairCompleteAlert();
                }
            }else {
                Utils.showAlertBox("Something Went wrong!!",RepairDetailsActivity.this);
               /* Intent i = new Intent(CallClloserActivity.this,CallClloserActivity.class);
                startActivity(i);*/
            }
        }
    }

    public class UploadFileCallClosour extends CallManagerAsyncTask {

        String comment,uploadedFile;
        JSONArray callClosour_images;
        public UploadFileCallClosour(String action, String reqType, JSONArray CallClosour_images, Context context){
            super(action, reqType, context);
            this.callClosour_images = CallClosour_images;
            // this.uploadedFile = uploadedFile;

        }

        @Override
        protected JSONObject doInBackground(Object... params) {

            JSONObject postParamData = new JSONObject();
            try {
              /*  postParamData.put("DocketNo",docketNo);
                postParamData.put("File",uploadedFile);
                postParamData.put("FileName",userId+String.valueOf(System.currentTimeMillis())+".jpg");
                //  postParamData.put("FileName","");

                postParamData.put("Comments",comment);*/
                return doWorkJSONArray(callClosour_images);
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
            ProgressUtil.showProgressBar(RepairDetailsActivity.this,
                    findViewById(R.id.root), R.id.progressBar);
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            super.onPostExecute(json);
             ProgressUtil.hideProgressBar(findViewById(R.id.root), R.id.progressBar);
            if(json != null){
                Utils.Log("JSON Response========="+json.toString());
                try {
                    if(json.has("Status") && json.get("Status").toString().equalsIgnoreCase("Success")){
                        /*Intent i = new Intent(AttachImageActivity.this, AcknowledgementActivity.class);
                        i.putExtra("docketNo",docketNo);
                        startActivity(i);
                        finish();*/
                        // progressDialog.dismiss();
                          Toast.makeText(RepairDetailsActivity.this, json.getString("ErrorMessage").toString(), Toast.LENGTH_LONG).show();
                      //  Utils.showAlertBox(json.getString("ErrorMessage").toString(),RepairDetailsActivity.this);


                    }else if(json.has("Status") && json.get("Status").toString().equalsIgnoreCase("Failure")) {
                        Utils.showAlertBox(json.getString("ErrorMessage").toString(),RepairDetailsActivity.this);
                    }else {
                        Utils.showAlertBox(json.getString("ErrorMessage").toString(),RepairDetailsActivity.this);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else {
                Utils.showAlertBox("Something Went wrong!!",RepairDetailsActivity.this);
               /* Intent i = new Intent(CallClloserActivity.this,CallClloserActivity.class);
                startActivity(i);*/
            }
        }
    }

    public class SaveQuestionCallClouser extends CallManagerAsyncTask {

        String comment,uploadedFile;
        JSONArray callClosour_question;
        public SaveQuestionCallClouser(String action, String reqType, JSONArray CallClosour_question, Context context){
            super(action, reqType, context);
            this.callClosour_question = CallClosour_question;
            // this.uploadedFile = uploadedFile;

        }

        @Override
        protected JSONObject doInBackground(Object... params) {

            try {
                return doWorkJSONArray(callClosour_question);
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
            ProgressUtil.showProgressBar(RepairDetailsActivity.this,
                    findViewById(R.id.root), R.id.progressBar);
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            super.onPostExecute(json);
              ProgressUtil.hideProgressBar(findViewById(R.id.root), R.id.progressBar);
            if(json != null){
                Utils.Log("JSON Response========="+json.toString());
                try {
                    if(json.has("Status") && json.get("Status").toString().equalsIgnoreCase("Success")){

                        //   progressDialog.dismiss();
                        Toast.makeText(RepairDetailsActivity.this, json.getString("ErrorMessage").toString(), Toast.LENGTH_SHORT).show();
                     //   Utils.showAlertBox(json.getString("ErrorMessage").toString(),RepairDetailsActivity.this);

                    }else if(json.has("Status") && json.get("Status").toString().equalsIgnoreCase("Failure")) {
                        Utils.showAlertBox(json.getString("ErrorMessage").toString(),RepairDetailsActivity.this);
                    }else {
                        Utils.showAlertBox(json.getString("ErrorMessage").toString(),RepairDetailsActivity.this);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else {
                Utils.showAlertBox("Something Went wrong!!",RepairDetailsActivity.this);
               /* Intent i = new Intent(CallClloserActivity.this,CallClloserActivity.class);
                startActivity(i);*/
            }
        }
    }





}
