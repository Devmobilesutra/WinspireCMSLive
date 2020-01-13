package com.cms.callmanager.multispinner;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import com.androidbuts.multispinnerfilter.KeyPairBoolData;
import com.androidbuts.multispinnerfilter.MultiSpinnerSearch;
import com.androidbuts.multispinnerfilter.SpinnerListener;
import com.bumptech.glide.Glide;
import com.cms.callmanager.AttachImageActivity;
import com.cms.callmanager.BuildConfig;
import com.cms.callmanager.MarshMallowPermission;
import com.cms.callmanager.Prefs;
import com.cms.callmanager.R;
import com.cms.callmanager.RangeTimePickerDialog;
import com.cms.callmanager.RepairDetailsActivity;
import com.cms.callmanager.activities.ReplacementDetailsActivity;
import com.cms.callmanager.adapter.CallListAdapter;
import com.cms.callmanager.constants.Constant;
import com.cms.callmanager.constants.Constants;
import com.cms.callmanager.multispinner.clousermodel.IdealHours_model;
import com.cms.callmanager.services.CallManagerAsyncTask;
import com.cms.callmanager.utils.ProgressUtil;
import com.cms.callmanager.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.cms.callmanager.multispinner.clousermodel.ActivityType;
import com.cms.callmanager.multispinner.clousermodel.ErrorCode;
import com.cms.callmanager.multispinner.clousermodel.GetFuturePartReplace;
import com.cms.callmanager.multispinner.clousermodel.GetIdleReason;
import com.cms.callmanager.multispinner.clousermodel.GetSubModuleAffected;
import com.cms.callmanager.multispinner.clousermodel.ModuleAffected;
import com.cms.callmanager.multispinner.clousermodel.ProblemFix;
import com.cms.callmanager.multispinner.clousermodel.Questionnaire;
import com.cms.callmanager.multispinner.clousermodel.QuestionnaireAnswer;
import com.cms.callmanager.multispinner.clousermodel.ResponseCategory;
import com.cms.callmanager.multispinner.clousermodel.Solution;
import com.theartofdev.edmodo.cropper.CropImage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.File;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


// worked on vibha

public class CallClloserActivity extends AppCompatActivity {
    MultiSpinnerSearch searchMultiSpinnerUnlimited,SpinnerSolution,SpinnerRespCategory,SpinnerErrorCode,SpinnerModuleAffected,SpinnerSubModuleAffected,SpinnerFuturePart;
   Spinner spinnerActivityType,spinnerTATMissReason;
    private ProgressDialog progressDialog;
    private Uri mImageCaptureUri;
    private Button Save_button;
  public static String problemFixstr,solutionstr,RespCategorystr,ErrorCodestr,ModuleAffectedstr,SubModuleAffectedstr,FuturePartstr;
   public static String ActivityTypestr,TATMissReasonstr;
    private Button btnPageOne;

    private Button btnPagetwo;
    private Button btnPagethree;
    private LinearLayout RepairDetailsPage1_layout,RepairDetailsPage2_layout,RepairDetailsPage3_layout;
    private EditText edtTxtEJDocketNo;
    private TextView tvTransactionDateTime;
    private EditText edtTxtFCRNo;
    int currentapiVersion;
    File outPutFile;

    boolean clickedflag_page1 = true;
    boolean clickedflag_page2 = true;
    boolean clickedflag_page3 = true;
    private ImageView FCRAttachmentimageView;
    private ImageView InstallationCertificateImageview;
    private ImageView ATMImagesImageview_1;
    private ImageView ATMImagesImageview_2;
    private ImageView TransactionImageImageview_1;
    private ImageView TransactionImageImageview_2;
    private ImageView ErrorHistoryImageview;
    private Button btnFCRAttachmentupload,btnInstallationCertificateupload,btnATMImagesupload_1,btnATMImagesupload_2,
            btnTransactionImageupload_1,btnTransactionImageupload_2,btnErrorHistoryimgupload;
    int PICK_IMAGE_PERMISSIONS_REQUEST_CODE=1,REQUEST_CAMERA=2;
    private static final int CAMERA_CODE = 1101, GALLERY_CODE = 2201, CROPING_CODE = 3301;
    private Bitmap photoUpload;
    String UploadBtnValue = null;
   // JSONObject postParamData = new JSONObject();
    public static JSONArray ImagesJsonArray ;
    JSONArray QuestionJsonArray = new JSONArray();
    private Button btnQuestionnaire;
    private RecyclerView questionarRecyclerview;
    public static List<Questionnaire> Questionnairelist;
    public static List<QuestionnaireAnswer> QuestionnaireAnswerlist;
    private Button QuesSubmitBtn,QuesCloseBtn,btnIdealHours;
    public static JSONObject CallClosurejson;
    private String currentDate;
    private String currentDate_ActCompDate,currentDate_NextActDate;
    public static JSONObject IdleHoursjson;
    private EditText edtstartDateDate;
    private EditText edtendDateDate;
    private Spinner idealReason_spinner;
    private ArrayList<String> problenfisArray;
    private Button ideal_cancelBtn;
    private Button ideal_okBtn;
    private ArrayList<String> ReasonArrayArray;
    public static ArrayList<IdealHours_model> data_ideal;
    public static String EJDocketNo,TransactioDateTime,FcrNo;
   /* private String TransactioDateTime;
    private String FcrNo;*/
    //  public static ArrayList<KeyPairBoolData> spinnerProblemFixArray;
  //  public static ArrayList<KeyPairBoolData> spinnerSolutionArray;
    //  private JSONObject IdleHoursjson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.call_closure_activity);

        Log.d("atm id: ", RepairDetailsActivity.atm_id);
        currentDate = getTodayDate();
        if(ImagesJsonArray != null)
        {

        }else {
            ImagesJsonArray = new JSONArray();
        }


        String[] arrOfStr_date = currentDate.split(",");
        currentDate_ActCompDate = arrOfStr_date[0];
        currentDate_NextActDate  = arrOfStr_date[1];
        currentapiVersion = Build.VERSION.SDK_INT;
        //****** page one ids
         searchMultiSpinnerUnlimited = (MultiSpinnerSearch) findViewById(R.id.ProblemFixSpinner);
        SpinnerSolution = (MultiSpinnerSearch) findViewById(R.id.SpinnerSolution);
        SpinnerRespCategory = (MultiSpinnerSearch) findViewById(R.id.SpinnerRespCategory);
        spinnerActivityType = (Spinner) findViewById(R.id.spinnerActivityType);
        spinnerTATMissReason = (Spinner) findViewById(R.id.spinnerTATMissReason);
        btnQuestionnaire  = (Button) findViewById(R.id.btnQuestionnaire);
        btnIdealHours = (Button)findViewById(R.id.btnIdealHours);
        btnIdealHours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(CallClloserActivity.this);
                LayoutInflater inflater = getLayoutInflater();

                View dialog = (View) inflater.inflate(R.layout.idal_hours_dialog, null);
                // request keyboard

                edtstartDateDate=(EditText)dialog.findViewById(R.id.edtstartDateDate);
                edtendDateDate=(EditText)dialog.findViewById(R.id.edtEndtDateDate);
                idealReason_spinner=(Spinner)dialog.findViewById(R.id.spinnerTATMissReason_1);
                ideal_cancelBtn=(Button)dialog.findViewById(R.id.ideal_cancelBtn);
                ArrayAdapter<String> spinnerArrayAdapter1 = new ArrayAdapter<String>(CallClloserActivity.this, android.R.layout.simple_spinner_item, ReasonArrayArray); //selected item will look like a spinner set from XML
                spinnerArrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                idealReason_spinner.setAdapter(spinnerArrayAdapter1);

                if(data_ideal != null )
                {
                    if(data_ideal.size() > 0)
                    {
                        edtstartDateDate.setText(data_ideal.get(0).getStartDate());
                        edtendDateDate.setText(data_ideal.get(0).getEndDate());

                        String batchno = data_ideal.get(0).getReason();
                        System.out.println("batch No : "+batchno);
                        if (batchno != null) {
                            //  holder.spnBatch.setText(batchno);
                            int spinnerPosition = spinnerArrayAdapter1.getPosition(batchno);
                            idealReason_spinner.setSelection(spinnerPosition);
                            //  holder.commentEditext.setText(fillQuesArray.get(position).getComment());
                            // holder.spnBatch.setEnabled(false);
                        }
                    }

                }

                alertDialog.setView(dialog);
                //  alertDialog.setTitle("Questionnaire");
                alertDialog.setCancelable(true);
                final AlertDialog ad = alertDialog.create();
                ideal_cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ad.dismiss();
                    }
                });
                ideal_okBtn=(Button)dialog.findViewById(R.id.ideal_okBtn);
                ideal_okBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                      //  ad.dismiss();
                        data_ideal=new ArrayList<IdealHours_model>();
                        if(!TextUtils.isEmpty(edtstartDateDate.getText().toString()) || !TextUtils.isEmpty(edtendDateDate.getText().toString()) || idealReason_spinner.getSelectedItem().toString() != "--Select--" )
                        {
                            ad.dismiss();
                            IdealHours_model idealModel = null;
                            IdleHoursjson = new JSONObject();
                            try {
                                IdleHoursjson.put("StartDate",edtstartDateDate.getText().toString());
                                IdleHoursjson.put("EndDate",edtendDateDate.getText().toString());
                                IdleHoursjson.put("LastModifiedBy",RepairDetailsActivity.userId);
                                IdleHoursjson.put("Reason",idealReason_spinner.getSelectedItem().toString());
                                IdleHoursjson.put("Dependancy","1");

                             //   Log.d("", "order is : 1 "+IdleHoursjson.toString());
                            idealModel = new IdealHours_model(edtstartDateDate.getText().toString(),edtendDateDate.getText().toString(),idealReason_spinner.getSelectedItem().toString());
                                data_ideal.add(idealModel);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        else {
                            Toast.makeText(getApplicationContext(),"Please select value",Toast.LENGTH_LONG).show();
                        }

                    }
                });



                edtstartDateDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        try {
                            Calendar calendar = Calendar.getInstance();
                            final int mYear = calendar.get(Calendar.YEAR);
                            final int mMonth = calendar.get(Calendar.MONTH);
                            final int mDay = calendar.get(Calendar.DAY_OF_MONTH);
                            final int hour = calendar.get(Calendar.HOUR_OF_DAY);
                            final int minute = calendar.get(Calendar.MINUTE);


                            DatePickerDialog datePickerDialog = new DatePickerDialog(CallClloserActivity.this,
                                    new DatePickerDialog.OnDateSetListener() {

                                        @Override
                                        public void onDateSet(DatePicker datepicker, final int year, int month, final int day) {
                                            month++;
                                            final int finalMonth = month;
                                            TimePickerDialog timePickerDialog = new TimePickerDialog(CallClloserActivity.this, new TimePickerDialog.OnTimeSetListener() {
                                                @Override
                                                public void onTimeSet(TimePicker view, int hourOfDay, int minute1) {
                                                    String dateTime = new StringBuilder().append(finalMonth).append("-").append(day).append("-").append(year).append(" ").append(hourOfDay).append(":").append(minute1).append(":00").toString();
                                                    try {
                                                        String dt = new SimpleDateFormat("MM/dd/yyyy HH:mm").format(new SimpleDateFormat("M-d-yyyy H:m:ss", Locale.ENGLISH).parse(dateTime));
                                                        edtstartDateDate.setText(dt);
                                                    } catch (ParseException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            }, hour, minute, true);
                                            timePickerDialog.show();
                                        }
                                    }, mYear, mMonth, mDay);

                            datePickerDialog.show();
                        } catch (WindowManager.BadTokenException ex) {
                            ex.printStackTrace();
                        }

                    }
                });




                edtendDateDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        try {
                            Calendar calendar = Calendar.getInstance();
                            final int mYear = calendar.get(Calendar.YEAR);
                            final int mMonth = calendar.get(Calendar.MONTH);
                            final int mDay = calendar.get(Calendar.DAY_OF_MONTH);
                            final int hour = calendar.get(Calendar.HOUR_OF_DAY);
                            final int minute = calendar.get(Calendar.MINUTE);


                            DatePickerDialog datePickerDialog = new DatePickerDialog(CallClloserActivity.this,
                                    new DatePickerDialog.OnDateSetListener() {

                                        @Override
                                        public void onDateSet(DatePicker datepicker, final int year, int month, final int day) {
                                            month++;
                                            final int finalMonth = month;
                                            TimePickerDialog timePickerDialog = new TimePickerDialog(CallClloserActivity.this, new TimePickerDialog.OnTimeSetListener() {
                                                @Override
                                                public void onTimeSet(TimePicker view, int hourOfDay, int minute1) {
                                                    String dateTime = new StringBuilder().append(finalMonth).append("-").append(day).append("-").append(year).append(" ").append(hourOfDay).append(":").append(minute1).append(":00").toString();
                                                    try {
                                                        String dt = new SimpleDateFormat("MM/dd/yyyy HH:mm").format(new SimpleDateFormat("M-d-yyyy H:m:ss", Locale.ENGLISH).parse(dateTime));
                                                        edtendDateDate.setText(dt);
                                                    } catch (ParseException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            }, hour, minute, true);
                                            timePickerDialog.show();
                                        }
                                    }, mYear, mMonth, mDay);

                            datePickerDialog.show();
                        } catch (WindowManager.BadTokenException ex) {
                            ex.printStackTrace();
                        }
                    }
                });

                ad.show();



            }
        });

        // page 2 ids
        edtTxtEJDocketNo =(EditText) findViewById(R.id.edtTxtEJDocketNo);
        tvTransactionDateTime = (TextView) findViewById(R.id.tvTransactionDateTime);
        SpinnerErrorCode = (MultiSpinnerSearch) findViewById(R.id.SpinnerErrorCode);
        SpinnerModuleAffected = (MultiSpinnerSearch) findViewById(R.id.SpinnerModuleAffected);
        SpinnerSubModuleAffected = (MultiSpinnerSearch) findViewById(R.id.SpinnerSubModuleAffected);

        //Page 2 actions ***********
        tvTransactionDateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                Calendar calendar = Calendar.getInstance();
                final int mYear = calendar.get(Calendar.YEAR);
                final int mMonth = calendar.get(Calendar.MONTH);
                final int mDay = calendar.get(Calendar.DAY_OF_MONTH);
                final int hour = calendar.get(Calendar.HOUR_OF_DAY);
                final int minute = calendar.get(Calendar.MINUTE);


                DatePickerDialog datePickerDialog = new DatePickerDialog(CallClloserActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datepicker, final int year, int month, final int day) {
                                month++;
                                final int finalMonth = month;
                                TimePickerDialog timePickerDialog = new TimePickerDialog(CallClloserActivity.this, new TimePickerDialog.OnTimeSetListener() {
                                    @Override
                                    public void onTimeSet(TimePicker view, int hourOfDay, int minute1) {
                                        String dateTime = new StringBuilder().append(finalMonth).append("-").append(day).append("-").append(year).append(" ").append(hourOfDay).append(":").append(minute1).append(":00").toString();
                                        try {
                                            String dt = new SimpleDateFormat("MM/dd/yyyy HH:mm").format(new SimpleDateFormat("M-d-yyyy H:m:ss", Locale.ENGLISH).parse(dateTime));
                                            tvTransactionDateTime.setText(dt);
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }, hour, minute, true);
                                timePickerDialog.show();
                            }
                        }, mYear, mMonth, mDay);

               /* Calendar c = Calendar.getInstance();
                c.set(mYear, mMonth -1, mDay);//Year,Mounth -1,Day
                long now = c.getTimeInMillis() - 1000;
                datePickerDialog.getDatePicker().setMinDate(now - (1000*60*60*24*2));
                datePickerDialog.getDatePicker().setMaxDate(now);*/

                datePickerDialog.show();
            } catch (WindowManager.BadTokenException ex) {
                    ex.printStackTrace();
                }

            }
        });

        // page 3 ids
        edtTxtFCRNo = (EditText) findViewById(R.id.edtTxtFCRNo);
        FCRAttachmentimageView = (ImageView) findViewById(R.id.FCRAttachmentimageView);
        InstallationCertificateImageview = (ImageView) findViewById(R.id.InstallationCertificateImageview);
        ATMImagesImageview_1 = (ImageView) findViewById(R.id.ATMImagesImageview1);
        ATMImagesImageview_2 = (ImageView) findViewById(R.id.ATMImagesImageview2);
        TransactionImageImageview_1 = (ImageView) findViewById(R.id.TransactionImageImageview1);
        TransactionImageImageview_2 = (ImageView) findViewById(R.id.TransactionImageImageview2);
        ErrorHistoryImageview = (ImageView) findViewById(R.id.ErrorHistoryImageview1);

        btnFCRAttachmentupload = (Button) findViewById(R.id.FCRAttachmentupload);
        btnInstallationCertificateupload = (Button)findViewById(R.id.InstallationCertificateupload);
        btnATMImagesupload_1 = (Button)findViewById(R.id.ATMImagesupload1);
        btnATMImagesupload_2 = (Button)findViewById(R.id.ATMImagesupload2);
        btnTransactionImageupload_1 = (Button)findViewById(R.id.TransactionImageupload1);
        btnTransactionImageupload_2 = (Button)findViewById(R.id.TransactionImageupload2);
        btnErrorHistoryimgupload = (Button)findViewById(R.id.ErrorHistoryimgupload1);
        SpinnerFuturePart = (MultiSpinnerSearch) findViewById(R.id.SpinnerFuturePart);

        btnFCRAttachmentupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePicture();
                UploadBtnValue = "FCRAttachment";
            }
        });
        btnInstallationCertificateupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePicture();
                UploadBtnValue = "InstallationCertificate";
            }
        });
        btnATMImagesupload_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePicture();
                UploadBtnValue = "ATMImages1";
            }
        });
        btnATMImagesupload_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePicture();
                UploadBtnValue = "ATMImages2";
            }
        });
        btnTransactionImageupload_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePicture();
                UploadBtnValue = "TransactionImage1";
            }
        });
        btnTransactionImageupload_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePicture();
                UploadBtnValue = "TransactionImage2";
            }
        });
        btnErrorHistoryimgupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePicture();
                UploadBtnValue = "ErrorHistory";
            }
        });


        // ********** repair page buttons +++++++++
        btnPageOne = (Button)findViewById(R.id.btnPageOne);
        btnPagetwo = (Button)findViewById(R.id.btnPagetwo);
        btnPagethree = (Button)findViewById(R.id.btnPagethree);

        //********* repair page layouts**********
        RepairDetailsPage1_layout = (LinearLayout) findViewById(R.id.RepairDetailsPage1_layout);
        RepairDetailsPage2_layout = (LinearLayout) findViewById(R.id.RepairDetailsPage2_layout);
        RepairDetailsPage3_layout = (LinearLayout) findViewById(R.id.RepairDetailsPage3_layout);

        if(EJDocketNo != null)
        {
            edtTxtEJDocketNo.setText(EJDocketNo);
        }
        if(TransactioDateTime != null)
        {
            tvTransactionDateTime.setText(TransactioDateTime);
        }
        if(FcrNo != null)
        {
            edtTxtFCRNo.setText(FcrNo);
        }


        btnPageOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (RepairDetailsPage1_layout.getVisibility() == View.GONE)
                {
                    RepairDetailsPage1_layout.setVisibility(View.VISIBLE);
                    RepairDetailsPage2_layout.setVisibility(View.GONE);
                    RepairDetailsPage3_layout.setVisibility(View.GONE);
                   // clickedflag_page1= false;
                }
                else {
                    RepairDetailsPage1_layout.setVisibility(View.GONE);

                   // clickedflag_page1 = true;
                }

            }
        });
        btnPagetwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (RepairDetailsPage2_layout.getVisibility() == View.GONE)
                {
                    RepairDetailsPage2_layout.setVisibility(View.VISIBLE);
                    RepairDetailsPage1_layout.setVisibility(View.GONE);
                    RepairDetailsPage3_layout.setVisibility(View.GONE);

                   // clickedflag_page2= false;
                }
                else {
                    RepairDetailsPage2_layout.setVisibility(View.GONE);
                  //  clickedflag_page2 = true;
                }

            }
        });
        btnPagethree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (RepairDetailsPage3_layout.getVisibility() == View.GONE)
                {
                    RepairDetailsPage3_layout.setVisibility(View.VISIBLE);
                    RepairDetailsPage1_layout.setVisibility(View.GONE);
                    RepairDetailsPage2_layout.setVisibility(View.GONE);
                  //  clickedflag_page3= false;
                }
                else {
                    RepairDetailsPage3_layout.setVisibility(View.GONE);
                  //  clickedflag_page3 = true;
                }

            }
        });


        Save_button = (Button) findViewById(R.id.save_repair_detail);
        getProblemFixFromServer();
        SetSelectedImages();
        Save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validation()){
                    EJDocketNo = edtTxtEJDocketNo.getText().toString();
                    TransactioDateTime = tvTransactionDateTime.getText().toString();
                    FcrNo = edtTxtFCRNo.getText().toString();


                    CallClosurejson = new JSONObject();
                    try {
                        CallClosurejson.put("ProblemFix",problemFixstr);
                        CallClosurejson.put("Solution",solutionstr);
                        CallClosurejson.put("ResponseCategory",RespCategorystr);
                        CallClosurejson.put("ActivityType",ActivityTypestr);
                        CallClosurejson.put("TATMissReason",TATMissReasonstr);
                        CallClosurejson.put("TransactioDateTime",tvTransactionDateTime.getText().toString());
                        CallClosurejson.put("EJDocketNo",edtTxtEJDocketNo.getText().toString()); // put docket no
                        CallClosurejson.put("ErrorCode",ErrorCodestr);

                        CallClosurejson.put("ModuleAffcted",ModuleAffectedstr);
                        CallClosurejson.put("SubModuleAffected",SubModuleAffectedstr);
                        CallClosurejson.put("FcrNo",edtTxtFCRNo.getText().toString());
                        CallClosurejson.put("FutureParttobeReplaced",FuturePartstr);
                        CallClosurejson.put("AtmId",RepairDetailsActivity.atm_id); // put ATM id
                        CallClosurejson.put("ActCompDate",currentDate_ActCompDate);
                        CallClosurejson.put("ActionTaken","");
                        CallClosurejson.put("NextActDate",currentDate_NextActDate);

                        Log.d("", "order is : 1 "+CallClosurejson.toString());




                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    finish();

                }


            }
        });

        btnQuestionnaire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(CallClloserActivity.this,QuestionairActivity.class);
                startActivity(intent);
            }
        });


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
                        /*Intent i = new Intent(AttachImageActivity.this, AcknowledgementActivity.class);
                        i.putExtra("docketNo",docketNo);
                        startActivity(i);
                        finish();*/
                        // progressDialog.dismiss();
                        //  Toast.makeText(AttachImageActivity.this, json.getString("ErrorMessage").toString(), Toast.LENGTH_SHORT).show();


                    }else if(json.has("Status") && json.get("Status").toString().equalsIgnoreCase("Failure")) {
                        Utils.showAlertBox(json.getString("ErrorMessage").toString(),CallClloserActivity.this);
                    }else {
                        Utils.showAlertBox(json.getString("ErrorMessage").toString(),CallClloserActivity.this);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else {
                Utils.showAlertBox("Something Went wrong!!",CallClloserActivity.this);
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
            /*ProgressUtil.showProgressBar(CallClloserActivity.this,
                    findViewById(R.id.root), R.id.progressBar);*/
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            super.onPostExecute(json);
          //   ProgressUtil.hideProgressBar(findViewById(R.id.root), R.id.progressBar);
            if(json != null){
                Utils.Log("JSON Response========="+json.toString());
                try {
                    if(json.has("Status") && json.get("Status").toString().equalsIgnoreCase("Success")){

                      //   progressDialog.dismiss();
                          Toast.makeText(CallClloserActivity.this, json.getString("ErrorMessage").toString(), Toast.LENGTH_SHORT).show();


                    }else if(json.has("Status") && json.get("Status").toString().equalsIgnoreCase("Failure")) {
                        Utils.showAlertBox(json.getString("ErrorMessage").toString(),CallClloserActivity.this);
                    }else {
                        Utils.showAlertBox(json.getString("ErrorMessage").toString(),CallClloserActivity.this);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else {
                Utils.showAlertBox("Something Went wrong!!",CallClloserActivity.this);
               /* Intent i = new Intent(CallClloserActivity.this,CallClloserActivity.class);
                startActivity(i);*/
            }
        }
    }


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
                        /*Intent i = new Intent(AttachImageActivity.this, AcknowledgementActivity.class);
                        i.putExtra("docketNo",docketNo);
                        startActivity(i);
                        finish();*/
                       // progressDialog.dismiss();
                      //  Toast.makeText(AttachImageActivity.this, json.getString("ErrorMessage").toString(), Toast.LENGTH_SHORT).show();


                    }else if(json.has("Status") && json.get("Status").toString().equalsIgnoreCase("Failure")) {
                        Utils.showAlertBox(json.getString("ErrorMessage").toString(),CallClloserActivity.this);
                    }else {
                        Utils.showAlertBox(json.getString("ErrorMessage").toString(),CallClloserActivity.this);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else {
                Utils.showAlertBox("Something Went wrong!!",CallClloserActivity.this);
               /* Intent i = new Intent(CallClloserActivity.this,CallClloserActivity.class);
                startActivity(i);*/
            }
        }
    }


    private void SendQuestionPart(JSONArray data) {
        progressDialog = ProgressDialog.show(CallClloserActivity.this,
                getApplicationContext().getResources().getString(R.string.app_name),
                "Please wait...", false, false);
        progressDialog.setCancelable(false);



        IRetrofit jsonPostService = ServiceGenerator.createService(IRetrofit.class,"WINSPIREADMIN","winspire@123");
        Call<JsonObject> call = jsonPostService.SendQuestionpart("011119/1077",data);
        call.enqueue(new Callback<JsonObject>() {

            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try{

                    // Log.e("response-code", response.body().toString());
                    Log.e("response-code", String.valueOf(response.code()));
                    Log.e("response-url", response.raw().request().url().toString());
                    Log.e("warehouse response", new Gson().toJson(response.body())); // to print json response

                    if (response.isSuccessful())
                    {


                        //   Toast.makeText(MainActivity.this, "Production order added successfully.", Toast.LENGTH_LONG).show();
                        Toast.makeText(CallClloserActivity.this, "Successful", Toast.LENGTH_LONG).show();

                    }

                    if (progressDialog != null)
                        if (progressDialog.isShowing())
                            progressDialog.dismiss();

                    // Log.e("response-success", response.body().toString());
                }catch (Exception e){
                    if (progressDialog != null)
                        if (progressDialog.isShowing())
                            progressDialog.dismiss();
                    e.printStackTrace();
                    Toast.makeText(CallClloserActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("response-Exception", e.getMessage());
                }
                finally {
                    if (progressDialog != null)
                        if (progressDialog.isShowing())
                            progressDialog.dismiss();


                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                if (progressDialog != null)
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                Toast.makeText(CallClloserActivity.this,t.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("response Throwable",""+t.getMessage());
            }
        });
    }

    private void SendSpinnerPart(JSONObject data) {
        progressDialog = ProgressDialog.show(CallClloserActivity.this,
                getApplicationContext().getResources().getString(R.string.app_name),
                "Please wait...", false, false);
        progressDialog.setCancelable(false);



        IRetrofit jsonPostService = ServiceGenerator.createService(IRetrofit.class,"WINSPIREADMIN","winspire@123");
        Call<JsonObject> call = jsonPostService.SendSpinnerpart("011119/1077",data);
        call.enqueue(new Callback<JsonObject>() {

            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try{

                    // Log.e("response-code", response.body().toString());
                    Log.e("response-code", String.valueOf(response.code()));
                    Log.e("response-url", response.raw().request().url().toString());
                    Log.e("warehouse response", new Gson().toJson(response.body())); // to print json response

                    if (response.isSuccessful())
                    {


                        //   Toast.makeText(MainActivity.this, "Production order added successfully.", Toast.LENGTH_LONG).show();
                        Toast.makeText(CallClloserActivity.this, "Successful", Toast.LENGTH_LONG).show();

                    }

                    if (progressDialog != null)
                        if (progressDialog.isShowing())
                            progressDialog.dismiss();

                    // Log.e("response-success", response.body().toString());
                }catch (Exception e){
                    if (progressDialog != null)
                        if (progressDialog.isShowing())
                            progressDialog.dismiss();
                    e.printStackTrace();
                    Toast.makeText(CallClloserActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("response-Exception", e.getMessage());
                }
                finally {
                    if (progressDialog != null)
                        if (progressDialog.isShowing())
                            progressDialog.dismiss();


                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                if (progressDialog != null)
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                Toast.makeText(CallClloserActivity.this,t.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("response Throwable",""+t.getMessage());
            }
        });
    }

    private void SendImagesPart(String data) {
        progressDialog = ProgressDialog.show(CallClloserActivity.this,
                getApplicationContext().getResources().getString(R.string.app_name),
                "Please wait...", false, false);
        progressDialog.setCancelable(false);



        IRetrofit jsonPostService = ServiceGenerator.createService(IRetrofit.class,"WINSPIREADMIN","winspire@123");
        Call<JsonObject> call = jsonPostService.SendImagespart(data);
        call.enqueue(new Callback<JsonObject>() {

            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try{

                    // Log.e("response-code", response.body().toString());
                    Log.e("response-code", String.valueOf(response.code()));
                    Log.e("response-url", response.raw().request().url().toString());
                    Log.e("warehouse response", new Gson().toJson(response.body())); // to print json response

                    if (response.isSuccessful())
                    {


                        //   Toast.makeText(MainActivity.this, "Production order added successfully.", Toast.LENGTH_LONG).show();
                        Toast.makeText(CallClloserActivity.this, "Successful", Toast.LENGTH_LONG).show();

                    }

                    if (progressDialog != null)
                        if (progressDialog.isShowing())
                            progressDialog.dismiss();

                    // Log.e("response-success", response.body().toString());
                }catch (Exception e){
                    if (progressDialog != null)
                        if (progressDialog.isShowing())
                            progressDialog.dismiss();
                    e.printStackTrace();
                    Toast.makeText(CallClloserActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("response-Exception", e.getMessage());
                }
                finally {
                    if (progressDialog != null)
                        if (progressDialog.isShowing())
                            progressDialog.dismiss();


                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                if (progressDialog != null)
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                Toast.makeText(CallClloserActivity.this,t.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("response Throwable",""+t.getMessage());
            }
        });
    }

    private void getExternalStoragePermission(Uri imageUri){
        MarshMallowPermission marshMallowPermission = new MarshMallowPermission(CallClloserActivity.this);
        if (marshMallowPermission.isReadExternalStoragePermissionsRequired(this, imageUri)) {
            mImageCaptureUri = imageUri;
            requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, PICK_IMAGE_PERMISSIONS_REQUEST_CODE);
        } else {
            mImageCaptureUri = imageUri;
            CropingIMG();
        }
    }

    private void getCameraPermission(){

        MarshMallowPermission marshMallowPermission = new MarshMallowPermission(CallClloserActivity.this);
        Log.d("RegistrationActivity", "Show camera button pressed. Checking permission.");
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            requestCameraPermission();
        } else {
            if (!marshMallowPermission.checkPermissionForExternalStorage()) {
                marshMallowPermission.requestPermissionForExternalStorage();
            }else {
                Log.d("RegistrationActivity",
                        "CAMERA permission has already been granted. Displaying camera preview.");

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File f = new File(Environment.getExternalStorageDirectory(), "winspireImageFile.jpg");
                mImageCaptureUri = FileProvider.getUriForFile(CallClloserActivity.this,
                        BuildConfig.APPLICATION_ID + ".provider",f);

                intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
                startActivityForResult(intent, CAMERA_CODE);
            }
        }
    }

    private void requestCameraPermission() {
        Log.d("Main", "CAMERA permission has NOT been granted. Requesting permission.");
        // BEGIN_INCLUDE(camera_permission_request)
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                android.Manifest.permission.CAMERA)) {
            Snackbar.make(findViewById(R.id.root), "Camera permission is needed to show the camera preview",
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ActivityCompat.requestPermissions(CallClloserActivity.this,
                                    new String[]{android.Manifest.permission.CAMERA},
                                    REQUEST_CAMERA);
                        }
                    })
                    .show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA},
                    REQUEST_CAMERA);
        }
    }

    private Bitmap decodeFile(File f) {
        try {
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            Bitmap mBitmap = BitmapFactory.decodeFile(f.getAbsolutePath());
            int width = mBitmap.getWidth();
            int height = mBitmap.getHeight();

            Double newWidth = 400.00;
            Double newHeight = 400.00;
            Double mf = 1.00;

            if(width >= height){
                mf = width/newWidth;
                newWidth = Math.floor(width/mf);
                newHeight = Math.floor(height/mf);
            }else{
                mf = height/newHeight;
                newWidth = Math.floor(width/mf);
                newHeight = Math.floor(height/mf);
            }

            return Bitmap.createScaledBitmap(mBitmap, newWidth.intValue(), newHeight.intValue(), false);

        } catch (Exception e) {
        }
        return null;
    }
    private void CropingIMG() {
        CropImage.activity(mImageCaptureUri)
                .start(CallClloserActivity.this);
    }


    public void SetSelectedImages()
    {

        String FCRAttachment = Prefs.with(CallClloserActivity.this).getString(Constant.FCRAttachment, "");
        if(FCRAttachment != null && FCRAttachment != "")
        {
            byte[] decodedString = Base64.decode(FCRAttachment, Base64.DEFAULT);
           // Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            Glide.with(CallClloserActivity.this).load(decodedString).crossFade().fitCenter().into(FCRAttachmentimageView);
            btnFCRAttachmentupload.setText("Change Image");
        }

        String InstallationCertificate = Prefs.with(CallClloserActivity.this).getString(Constant.InstallationCertificate, "");
        if(InstallationCertificate != null && InstallationCertificate != "")
        {
            byte[] decodedString = Base64.decode(InstallationCertificate, Base64.DEFAULT);
            // Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            Glide.with(CallClloserActivity.this).load(decodedString).crossFade().fitCenter().into(InstallationCertificateImageview);
            btnInstallationCertificateupload.setText("Change Image");
        }

        String ATMImages1 = Prefs.with(CallClloserActivity.this).getString(Constant.ATMImages1, "");
        if(ATMImages1 != null && ATMImages1 != "")
        {
            byte[] decodedString = Base64.decode(ATMImages1, Base64.DEFAULT);
            // Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            Glide.with(CallClloserActivity.this).load(decodedString).crossFade().fitCenter().into(ATMImagesImageview_1);
            btnATMImagesupload_1.setText("Change Image");

        }
        String ATMImages2 = Prefs.with(CallClloserActivity.this).getString(Constant.ATMImages2, "");
        if(ATMImages2 != null && ATMImages2 != "")
        {
            byte[] decodedString = Base64.decode(ATMImages2, Base64.DEFAULT);
            // Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            Glide.with(CallClloserActivity.this).load(decodedString).crossFade().fitCenter().into(ATMImagesImageview_2);
            btnATMImagesupload_2.setText("Change Image");

        }

        String TransactionImage1 = Prefs.with(CallClloserActivity.this).getString(Constant.TransactionImage1, "");
        if(TransactionImage1 != null && TransactionImage1 != "")
        {
            byte[] decodedString = Base64.decode(TransactionImage1, Base64.DEFAULT);
            // Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            Glide.with(CallClloserActivity.this).load(decodedString).crossFade().fitCenter().into(TransactionImageImageview_1);
            btnTransactionImageupload_1.setText("Change Image");

        }

        String TransactionImage2 = Prefs.with(CallClloserActivity.this).getString(Constant.TransactionImage2, "");
        if(TransactionImage2 != null && TransactionImage2 != "")
        {
            byte[] decodedString = Base64.decode(TransactionImage2, Base64.DEFAULT);
            // Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            Glide.with(CallClloserActivity.this).load(decodedString).crossFade().fitCenter().into(TransactionImageImageview_2);
            btnTransactionImageupload_2.setText("Change Image");

        }

        String ErrorHistory = Prefs.with(CallClloserActivity.this).getString(Constant.ErrorHistory, "");
        if(ErrorHistory != null && ErrorHistory != "")
        {
            byte[] decodedString = Base64.decode(ErrorHistory, Base64.DEFAULT);
            // Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            Glide.with(CallClloserActivity.this).load(decodedString).crossFade().fitCenter().into(ErrorHistoryImageview);
            btnErrorHistoryimgupload.setText("Change Image");
        }



  /*      if(ImagesJsonArray != null)
        {
            for (int i = 0;i < ImagesJsonArray.length();i++)
            {
                try {
                    JSONObject json = ImagesJsonArray.getJSONObject(i);
                    String comment = json.getString("Comments");
                    String imageBase = json.getString("File");
                    byte[] decodedString = Base64.decode(imageBase, Base64.URL_SAFE);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    if(comment.equals("FCRAttachment"))
                    {
                        btnFCRAttachmentupload.setText("Change Image");
                        FCRAttachmentimageView.setImageBitmap(decodedByte);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }*/

    }

    private void choosePicture() {

        final CharSequence[] options = { "Take Photo","Cancel" };
        dispatchTakePictureIntent();

      /*  AlertDialog.Builder builder = new AlertDialog.Builder(CallClloserActivity.this);
        builder.setItems(options, new DialogInterface.OnClickListener() {

            @SuppressLint("SdCardPath")
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    dispatchTakePictureIntent();
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }

        });
        builder.show();*/
    }
    protected void dispatchTakePictureIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File f = new File(Environment.getExternalStorageDirectory(), "winspireImageFile.jpg");
        //  mImageCaptureUri = Uri.fromFile(f);
        mImageCaptureUri = FileProvider.getUriForFile(CallClloserActivity.this,
                BuildConfig.APPLICATION_ID + ".provider",f);

        if (currentapiVersion >= Build.VERSION_CODES.M) {
            getCameraPermission();
        } else {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
            startActivityForResult(intent, CAMERA_CODE);
        }

    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_CODE && resultCode == Activity.RESULT_OK && null != data) {
            mImageCaptureUri = data.getData();
            if (currentapiVersion >= Build.VERSION_CODES.M) {
                getExternalStoragePermission(mImageCaptureUri);
            } else {
                CropingIMG();
            }


        }else if (requestCode == CAMERA_CODE && resultCode == Activity.RESULT_OK) {
            if (currentapiVersion >= Build.VERSION_CODES.M) {
                getExternalStoragePermission(mImageCaptureUri);
            } else {
                CropingIMG();
            }
        }else if ( requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            try {
                if(resultCode == RESULT_OK){
                    String uploadedFile = "";
                    JSONObject postParamData = new JSONObject();
                    CropImage.ActivityResult result = CropImage.getActivityResult(data);
                    Uri resultUri = result.getUri();
                    outPutFile =new File(resultUri.getPath());
                    photoUpload = decodeFile(outPutFile);
                    if(UploadBtnValue == "FCRAttachment")
                    {
                        btnFCRAttachmentupload.setText("Change Image");
                        FCRAttachmentimageView.setImageBitmap(photoUpload);

                        File imgFile = new File(String.valueOf(outPutFile));
                        if (imgFile.exists() && imgFile.length() > 0) {
                            Bitmap bm = BitmapFactory.decodeFile(String.valueOf(imgFile));
                            ByteArrayOutputStream bOut = new ByteArrayOutputStream();
                            bm.compress(Bitmap.CompressFormat.JPEG, 30, bOut);
                            uploadedFile = Base64.encodeToString(bOut.toByteArray(), Base64.DEFAULT);
                            Prefs.with(CallClloserActivity.this).save(Constant.FCRAttachment, uploadedFile);
                           // encodedImage= URLEncoder.encode(Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT), "UTF-8");
                        if(ImagesJsonArray != null)
                        {
                            for (int i = 0; i < ImagesJsonArray.length(); i++) {
                                JSONObject testObj = ImagesJsonArray.getJSONObject(i);
                                String name = testObj.getString("Comments");
                                if (name.equals(UploadBtnValue)) {
                                    ImagesJsonArray.remove(i);
                                }
                            }
                        }

                            try {
                                postParamData.put("DocketNo", RepairDetailsActivity.docket_no);
                                postParamData.put("File", uploadedFile.trim());
                                postParamData.put("FileName", RepairDetailsActivity.userId + String.valueOf(System.currentTimeMillis()) + ".jpg");
                                postParamData.put("Comments", UploadBtnValue);
                                //  return doWork(postParamData);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            ImagesJsonArray.put(postParamData);
                        }else {

                        }
                    }else if(UploadBtnValue == "InstallationCertificate")
                    {
                        btnInstallationCertificateupload.setText("Change Image");
                        InstallationCertificateImageview.setImageBitmap(photoUpload);
                        File imgFile = new File(String.valueOf(outPutFile));
                        if (imgFile.exists() && imgFile.length() > 0) {
                            Bitmap bm = BitmapFactory.decodeFile(String.valueOf(imgFile));
                            ByteArrayOutputStream bOut = new ByteArrayOutputStream();
                            bm.compress(Bitmap.CompressFormat.JPEG, 30, bOut);
                            uploadedFile = Base64.encodeToString(bOut.toByteArray(), Base64.DEFAULT);
                            Prefs.with(CallClloserActivity.this).save(Constant.InstallationCertificate, uploadedFile);
                            if(ImagesJsonArray != null)
                            {
                                for (int i = 0; i < ImagesJsonArray.length(); i++) {
                                    JSONObject testObj = ImagesJsonArray.getJSONObject(i);
                                    String name = testObj.getString("Comments");
                                    if (name.equals(UploadBtnValue)) {
                                        ImagesJsonArray.remove(i);
                                    }
                                }
                            }


                            try {
                                postParamData.put("DocketNo", RepairDetailsActivity.docket_no);
                                postParamData.put("File", uploadedFile.trim());
                                postParamData.put("FileName", RepairDetailsActivity.userId + String.valueOf(System.currentTimeMillis()) + ".jpg");
                                postParamData.put("Comments", UploadBtnValue);
                                //  return doWork(postParamData);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            ImagesJsonArray.put(postParamData);
                        }else {

                        }
                    }else if(UploadBtnValue == "ATMImages1")
                    {
                        btnATMImagesupload_1.setText("Change Image");
                        ATMImagesImageview_1.setImageBitmap(photoUpload);
                        File imgFile = new File(String.valueOf(outPutFile));
                        if (imgFile.exists() && imgFile.length() > 0) {
                            Bitmap bm = BitmapFactory.decodeFile(String.valueOf(imgFile));
                            ByteArrayOutputStream bOut = new ByteArrayOutputStream();
                            bm.compress(Bitmap.CompressFormat.JPEG, 50, bOut);
                            uploadedFile = Base64.encodeToString(bOut.toByteArray(), Base64.DEFAULT);
                            Prefs.with(CallClloserActivity.this).save(Constant.ATMImages1, uploadedFile);
                            if(ImagesJsonArray != null)
                            {
                                for (int i = 0; i < ImagesJsonArray.length(); i++) {
                                    JSONObject testObj = ImagesJsonArray.getJSONObject(i);
                                    String name = testObj.getString("Comments");
                                    if (name.equals(UploadBtnValue)) {
                                        ImagesJsonArray.remove(i);
                                    }
                                }
                            }


                            try {
                                postParamData.put("DocketNo", RepairDetailsActivity.docket_no);
                                postParamData.put("File", uploadedFile);
                                postParamData.put("FileName", RepairDetailsActivity.userId + String.valueOf(System.currentTimeMillis()) + ".jpg");
                                postParamData.put("Comments", UploadBtnValue);
                                //  return doWork(postParamData);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            ImagesJsonArray.put(postParamData);
                        }else {

                        }
                    }else if(UploadBtnValue == "ATMImages2")
                    {
                        btnATMImagesupload_2.setText("Change Image");
                        ATMImagesImageview_2.setImageBitmap(photoUpload);
                        File imgFile = new File(String.valueOf(outPutFile));
                        if (imgFile.exists() && imgFile.length() > 0) {
                            Bitmap bm = BitmapFactory.decodeFile(String.valueOf(imgFile));
                            ByteArrayOutputStream bOut = new ByteArrayOutputStream();
                            bm.compress(Bitmap.CompressFormat.JPEG, 50, bOut);
                            uploadedFile = Base64.encodeToString(bOut.toByteArray(), Base64.DEFAULT);
                            Prefs.with(CallClloserActivity.this).save(Constant.ATMImages2, uploadedFile);
                            if(ImagesJsonArray != null)
                            {
                                for (int i = 0; i < ImagesJsonArray.length(); i++) {
                                    JSONObject testObj = ImagesJsonArray.getJSONObject(i);
                                    String name = testObj.getString("Comments");
                                    if (name.equals(UploadBtnValue)) {
                                        ImagesJsonArray.remove(i);
                                    }
                                }
                            }

                            try {
                                postParamData.put("DocketNo", RepairDetailsActivity.docket_no);
                                postParamData.put("File", uploadedFile);
                                postParamData.put("FileName", RepairDetailsActivity.userId + String.valueOf(System.currentTimeMillis()) + ".jpg");
                                postParamData.put("Comments", UploadBtnValue);
                                //  return doWork(postParamData);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            ImagesJsonArray.put(postParamData);
                        }else {

                        }
                    }else if(UploadBtnValue == "TransactionImage1")
                    {
                        btnTransactionImageupload_1.setText("Change Image");
                        TransactionImageImageview_1.setImageBitmap(photoUpload);
                        File imgFile = new File(String.valueOf(outPutFile));
                        if (imgFile.exists() && imgFile.length() > 0) {
                            Bitmap bm = BitmapFactory.decodeFile(String.valueOf(imgFile));
                            ByteArrayOutputStream bOut = new ByteArrayOutputStream();
                            bm.compress(Bitmap.CompressFormat.JPEG, 50, bOut);
                            uploadedFile = Base64.encodeToString(bOut.toByteArray(), Base64.DEFAULT);
                            Prefs.with(CallClloserActivity.this).save(Constant.TransactionImage1, uploadedFile);
                            if(ImagesJsonArray != null)
                            {
                                for (int i = 0; i < ImagesJsonArray.length(); i++) {
                                    JSONObject testObj = ImagesJsonArray.getJSONObject(i);
                                    String name = testObj.getString("Comments");
                                    if (name.equals(UploadBtnValue)) {
                                        ImagesJsonArray.remove(i);
                                    }
                                }
                            }

                            try {
                                postParamData.put("DocketNo", RepairDetailsActivity.docket_no);
                                postParamData.put("File", uploadedFile);
                                postParamData.put("FileName", RepairDetailsActivity.userId + String.valueOf(System.currentTimeMillis()) + ".jpg");
                                postParamData.put("Comments", UploadBtnValue);
                                //  return doWork(postParamData);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            ImagesJsonArray.put(postParamData);
                        }else {

                        }
                    }else if(UploadBtnValue == "TransactionImage2")
                    {
                        btnTransactionImageupload_2.setText("Change Image");
                        TransactionImageImageview_2.setImageBitmap(photoUpload);
                        File imgFile = new File(String.valueOf(outPutFile));
                        if (imgFile.exists() && imgFile.length() > 0) {
                            Bitmap bm = BitmapFactory.decodeFile(String.valueOf(imgFile));
                            ByteArrayOutputStream bOut = new ByteArrayOutputStream();
                            bm.compress(Bitmap.CompressFormat.JPEG, 50, bOut);
                            uploadedFile = Base64.encodeToString(bOut.toByteArray(), Base64.DEFAULT);
                            Prefs.with(CallClloserActivity.this).save(Constant.TransactionImage2, uploadedFile);
                            if(ImagesJsonArray != null)
                            {
                                for (int i = 0; i < ImagesJsonArray.length(); i++) {
                                    JSONObject testObj = ImagesJsonArray.getJSONObject(i);
                                    String name = testObj.getString("Comments");
                                    if (name.equals(UploadBtnValue)) {
                                        ImagesJsonArray.remove(i);
                                    }
                                }
                            }

                            try {
                                postParamData.put("DocketNo", RepairDetailsActivity.docket_no);
                                postParamData.put("File", uploadedFile);
                                postParamData.put("FileName", RepairDetailsActivity.userId + String.valueOf(System.currentTimeMillis()) + ".jpg");
                                postParamData.put("Comments", UploadBtnValue);
                                //  return doWork(postParamData);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            ImagesJsonArray.put(postParamData);
                        }else {

                        }
                    }else if(UploadBtnValue == "ErrorHistory")
                    {
                        btnErrorHistoryimgupload.setText("Change Image");
                        ErrorHistoryImageview.setImageBitmap(photoUpload);
                        File imgFile = new File(String.valueOf(outPutFile));
                        if (imgFile.exists() && imgFile.length() > 0) {
                            Bitmap bm = BitmapFactory.decodeFile(String.valueOf(imgFile));
                            ByteArrayOutputStream bOut = new ByteArrayOutputStream();
                            bm.compress(Bitmap.CompressFormat.JPEG, 50, bOut);
                            uploadedFile = Base64.encodeToString(bOut.toByteArray(), Base64.DEFAULT);
                            Prefs.with(CallClloserActivity.this).save(Constant.ErrorHistory, uploadedFile);
                            if(ImagesJsonArray != null)
                            {
                                for (int i = 0; i < ImagesJsonArray.length(); i++) {
                                    JSONObject testObj = ImagesJsonArray.getJSONObject(i);
                                    String name = testObj.getString("Comments");
                                    if (name.equals(UploadBtnValue)) {
                                        ImagesJsonArray.remove(i);
                                    }
                                }
                            }

                            try {
                                postParamData.put("DocketNo", RepairDetailsActivity.docket_no);
                                postParamData.put("File", uploadedFile);
                                postParamData.put("FileName", RepairDetailsActivity.userId + String.valueOf(System.currentTimeMillis()) + ".jpg");
                                postParamData.put("Comments", UploadBtnValue);
                                //  return doWork(postParamData);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            ImagesJsonArray.put(postParamData);
                        }else {

                        }
                    }

                    Log.d("Json Image : ",ImagesJsonArray.toString());

                   // uploadImage.setVisibility(View.VISIBLE);
                }
                else {
                    Toast.makeText(getApplicationContext(), "Error while save image", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean validation()
    {
        if(problemFixstr == null || solutionstr == null || RespCategorystr == null || ActivityTypestr == null || TextUtils.isEmpty(tvTransactionDateTime.getText().toString()))
        {
            Toast.makeText(getApplicationContext(),"Please select all values",Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    public static String getTodayDate() {

        Calendar c = Calendar.getInstance();   // this takes current date
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        String currDate = dateFormat.format(c.getTime());
        c.add(Calendar.MINUTE,5);
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        String currDate_addFive = dateFormat1.format(c.getTime());

        //  String currDate = Prefs.with(BaseActivity.this).getString(DataSyncToServer, "");

        //Ex currDate = 2019 12 09
        return currDate+","+currDate_addFive;
    }

    private void getProblemFixFromServer() {
        progressDialog = ProgressDialog.show(CallClloserActivity.this, getApplicationContext().getResources().getString(R.string.app_name),
                "Please wait...", false, false);
        progressDialog.setCancelable(false);
        // Using the Retrofi
        IRetrofit jsonPostService = ServiceGenerator.createService(IRetrofit.class,"WINSPIREADMIN","winspire@123");
        Call<List<ProblemFix>> call = jsonPostService.getProblemFix();
        call.enqueue(new Callback<List<ProblemFix>>() {

            @Override
            public void onResponse(Call<List<ProblemFix>> call, Response<List<ProblemFix>> response) {
                try{
                    Log.e("response-code", String.valueOf(response.code()));
                    Log.e("response-url", response.raw().request().url().toString());
                    Log.e("warehouse response", new Gson().toJson(response.body())); // to print json response


                    if (response.isSuccessful())
                    {
                        List<ProblemFix> problemfixlist = response.body();
                        ArrayList<String> problenfisArray = new ArrayList<>();

                        for (int i=0;i<problemfixlist.size();i++)
                        {
                          //  problenfisArray.add(problemfixlist.get(i).getName());

                        }

               /*       List<KeyPairBoolData>  spinnerProblemFixArray = new ArrayList<>();

                        for (int i = 0; i < problemfixlist.size(); i++) {
                            Log.d("val ",problemfixlist.get(i).getName());
                            KeyPairBoolData h = new KeyPairBoolData();
                            h.setId(i + 1);
                            h.setName(problemfixlist.get(i).getName());
                            h.setCode(problemfixlist.get(i).getCode());
                            h.setSelected(false);
                            spinnerProblemFixArray.add(h);
                        }*/

                        searchMultiSpinnerUnlimited.setEmptyTitle("Not Data Found!");
                        searchMultiSpinnerUnlimited.setSearchHint("Find Data");
               /*         searchMultiSpinnerUnlimited.setItems(spinnerProblemFixArray, -1, new SpinnerListener() {

                            @Override
                            public void onItemsSelected(List<KeyPairBoolData> items) {
                                StringBuilder spinnerBuffer = new StringBuilder();
                                for (int i = 0; i < items.size(); i++) {
                                    if (items.get(i).isSelected()) {
                                        spinnerBuffer.append(items.get(i).getCode());
                                        spinnerBuffer.append(",");
                                        Log.i("", i + " : " + items.get(i).getName() + " : " + items.get(i).isSelected());
                                    }
                                }
                                problemFixstr = spinnerBuffer.toString();
                                if (problemFixstr.length() > 2)
                                    problemFixstr = problemFixstr.substring(0, problemFixstr.length() - 1);
                                else
                                    problemFixstr = "";
                                Log.d("selected data : " ,problemFixstr);
                            }
                        });*/

                        if(problemFixstr != null)
                        {
                            final List<KeyPairBoolData> spinnerProblemFixArray = new ArrayList<>();
                            String[] arr = problemFixstr.split(",");

                            for (int i = 0; i < problemfixlist.size(); i++) {
                                Log.d("val ",problemfixlist.get(i).getName());
                                KeyPairBoolData h = new KeyPairBoolData();
                                h.setId(i + 1);
                                h.setName(problemfixlist.get(i).getName());
                                h.setCode(problemfixlist.get(i).getCode());
                                for (int j = 0;j < arr.length;j++)
                                {
                                    if(problemfixlist.get(i).getCode().equals(arr[j]))
                                    {
                                        h.setSelected(true);
                                        break;
                                    }else {
                                        h.setSelected(false);
                                    }
                                }

                                spinnerProblemFixArray.add(h);
                            }

                            searchMultiSpinnerUnlimited.setItems(spinnerProblemFixArray, -1, new SpinnerListener() {

                                @Override
                                public void onItemsSelected(List<KeyPairBoolData> items) {
                                    StringBuilder spinnerBuffer = new StringBuilder();
                                    for (int i = 0; i < items.size(); i++) {
                                        if (items.get(i).isSelected()) {
                                            spinnerBuffer.append(items.get(i).getCode());
                                            spinnerBuffer.append(",");
                                            Log.i("", i + " : " + items.get(i).getName() + " : " + items.get(i).isSelected());
                                        }
                                    }
                                    problemFixstr = spinnerBuffer.toString();
                                    if (problemFixstr.length() > 2)
                                        problemFixstr = problemFixstr.substring(0, problemFixstr.length() - 1);
                                    else
                                        problemFixstr = "";
                                    Log.d("selected data : " ,problemFixstr);
                                }
                            });

                        }else {

                            final List<KeyPairBoolData> spinnerProblemFixArray = new ArrayList<>();

                            for (int i = 0; i < problemfixlist.size(); i++) {
                                Log.d("val ",problemfixlist.get(i).getName());
                                KeyPairBoolData h = new KeyPairBoolData();
                                h.setId(i + 1);
                                h.setName(problemfixlist.get(i).getName());
                                h.setCode(problemfixlist.get(i).getCode());
                                h.setSelected(false);
                                spinnerProblemFixArray.add(h);
                            }

                            searchMultiSpinnerUnlimited.setItems(spinnerProblemFixArray, -1, new SpinnerListener() {

                                @Override
                                public void onItemsSelected(List<KeyPairBoolData> items) {
                                    StringBuilder spinnerBuffer = new StringBuilder();
                                    for (int i = 0; i < items.size(); i++) {
                                        if (items.get(i).isSelected()) {
                                            spinnerBuffer.append(items.get(i).getCode());
                                            spinnerBuffer.append(",");
                                            Log.i("", i + " : " + items.get(i).getName() + " : " + items.get(i).isSelected());
                                        }
                                    }
                                    problemFixstr = spinnerBuffer.toString();
                                    if (problemFixstr.length() > 2)
                                        problemFixstr = problemFixstr.substring(0, problemFixstr.length() - 1);
                                    else
                                        problemFixstr = "";
                                    Log.d("selected data : " ,problemFixstr);
                                }
                            });

                        }




                    }
                    if (progressDialog != null)
                        if (progressDialog.isShowing())
                            progressDialog.dismiss();
                    getSolutionFromServer();

                }catch (Exception e){
                    if (progressDialog != null)
                        if (progressDialog.isShowing())
                            progressDialog.dismiss();
                    e.printStackTrace();
                    Toast.makeText(CallClloserActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("response-Exception", e.getMessage());
                }

            }

            @Override
            public void onFailure(Call<List<ProblemFix>> call, Throwable t) {
                if (progressDialog != null)
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                Toast.makeText(CallClloserActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("response-failure", t.getMessage());
            }


        });
    }
    private void getSolutionFromServer() {
        progressDialog = ProgressDialog.show(CallClloserActivity.this, getApplicationContext().getResources().getString(R.string.app_name),
                "Please wait...", false, false);
        progressDialog.setCancelable(false);
        // Using the Retrofi
        IRetrofit jsonPostService = ServiceGenerator.createService(IRetrofit.class,"WINSPIREADMIN","winspire@123");
        Call<List<Solution>> call = jsonPostService.getSolution();
        call.enqueue(new Callback<List<Solution>>() {

            @Override
            public void onResponse(Call<List<Solution>> call, Response<List<Solution>> response) {
                try{
                    Log.e("response-code", String.valueOf(response.code()));
                    Log.e("response-url", response.raw().request().url().toString());
                    Log.e("warehouse response", new Gson().toJson(response.body())); // to print json response


                    if (response.isSuccessful())
                    {
                        List<Solution> Solutionlist = response.body();
                       /* ArrayList<String> problenfisArray = new ArrayList<>();

                        for (int i=0;i<Solutionlist.size();i++)
                        {
                            problenfisArray.add(Solutionlist.get(i).getCode());

                        }*/

                 /*       List<KeyPairBoolData> spinnerSolutionArray = new ArrayList<>();

                        for (int i = 0; i < Solutionlist.size(); i++) {
                            KeyPairBoolData h = new KeyPairBoolData();
                            h.setId(i + 1);
                            h.setName(Solutionlist.get(i).getName());
                            h.setCode(Solutionlist.get(i).getCode());
                            h.setSelected(false);
                            spinnerSolutionArray.add(h);
                        }*/

                        SpinnerSolution.setEmptyTitle("Not Data Found!");
                        SpinnerSolution.setSearchHint("Find Data");

                  /*      SpinnerSolution.setItems(spinnerSolutionArray, -1, new SpinnerListener() {

                            @Override
                            public void onItemsSelected(List<KeyPairBoolData> items) {

                                StringBuilder spinnerBuffer = new StringBuilder();
                                for (int i = 0; i < items.size(); i++) {
                                    if (items.get(i).isSelected()) {
                                        spinnerBuffer.append(items.get(i).getCode());
                                        spinnerBuffer.append(",");
                                        Log.i("", i + " : " + items.get(i).getName() + " : " + items.get(i).isSelected());
                                    }
                                }
                                solutionstr = spinnerBuffer.toString();
                                if (solutionstr.length() > 2)
                                    solutionstr = solutionstr.substring(0, solutionstr.length() - 1);
                                else
                                    solutionstr = "";
                                Log.d("solutionstr data : " ,solutionstr);
                            }
                        });*/


                        if(solutionstr != null)
                        {
                            final List<KeyPairBoolData> spinnerSolutionArray = new ArrayList<>();
                            String[] arr = solutionstr.split(",");

                            for (int i = 0; i < Solutionlist.size(); i++) {
                            //    Log.d("val ",Solutionlist.get(i).getName());
                                KeyPairBoolData h = new KeyPairBoolData();
                                h.setId(i + 1);
                                h.setName(Solutionlist.get(i).getName());
                                h.setCode(Solutionlist.get(i).getCode());
                                for (int j = 0;j < arr.length;j++)
                                {
                                    if(Solutionlist.get(i).getCode().equals(arr[j]))
                                    {
                                        h.setSelected(true);
                                        break;
                                    }else {
                                        h.setSelected(false);
                                    }
                                }

                                spinnerSolutionArray.add(h);
                            }

                            SpinnerSolution.setItems(spinnerSolutionArray, -1, new SpinnerListener() {

                                @Override
                                public void onItemsSelected(List<KeyPairBoolData> items) {
                                    StringBuilder spinnerBuffer = new StringBuilder();
                                    for (int i = 0; i < items.size(); i++) {
                                        if (items.get(i).isSelected()) {
                                            spinnerBuffer.append(items.get(i).getCode());
                                            spinnerBuffer.append(",");
                                            Log.i("", i + " : " + items.get(i).getName() + " : " + items.get(i).isSelected());
                                        }
                                    }
                                    solutionstr = spinnerBuffer.toString();
                                    if (solutionstr.length() > 2)
                                        solutionstr = solutionstr.substring(0, solutionstr.length() - 1);
                                    else
                                        solutionstr = "";
                                    Log.d("selected data : " ,solutionstr);
                                }
                            });

                        }else {

                            final List<KeyPairBoolData> spinnerSolutionArray = new ArrayList<>();

                            for (int i = 0; i < Solutionlist.size(); i++) {
                                Log.d("val ",Solutionlist.get(i).getName());
                                KeyPairBoolData h = new KeyPairBoolData();
                                h.setId(i + 1);
                                h.setName(Solutionlist.get(i).getName());
                                h.setCode(Solutionlist.get(i).getCode());
                                h.setSelected(false);
                                spinnerSolutionArray.add(h);
                            }

                            SpinnerSolution.setItems(spinnerSolutionArray, -1, new SpinnerListener() {

                                @Override
                                public void onItemsSelected(List<KeyPairBoolData> items) {
                                    StringBuilder spinnerBuffer = new StringBuilder();
                                    for (int i = 0; i < items.size(); i++) {
                                        if (items.get(i).isSelected()) {
                                            spinnerBuffer.append(items.get(i).getCode());
                                            spinnerBuffer.append(",");
                                            Log.i("", i + " : " + items.get(i).getName() + " : " + items.get(i).isSelected());
                                        }
                                    }
                                    solutionstr = spinnerBuffer.toString();
                                    if (solutionstr.length() > 2)
                                        solutionstr = solutionstr.substring(0, solutionstr.length() - 1);
                                    else
                                        solutionstr = "";
                                    Log.d("selected data : " ,solutionstr);
                                }
                            });

                        }


                    }
                    if (progressDialog != null)
                        if (progressDialog.isShowing())
                            progressDialog.dismiss();
                    getResponseCategoryFromServer();

                }catch (Exception e){
                    if (progressDialog != null)
                        if (progressDialog.isShowing())
                            progressDialog.dismiss();
                    e.printStackTrace();
                    Toast.makeText(CallClloserActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("response-Exception", e.getMessage());
                }

            }

            @Override
            public void onFailure(Call<List<Solution>> call, Throwable t) {
                if (progressDialog != null)
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                Toast.makeText(CallClloserActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("response-failure", t.getMessage());
            }


        });
    }
    private void getResponseCategoryFromServer() {
        progressDialog = ProgressDialog.show(CallClloserActivity.this, getApplicationContext().getResources().getString(R.string.app_name),
                "Please wait...", false, false);
        progressDialog.setCancelable(false);
        // Using the Retrofi
        IRetrofit jsonPostService = ServiceGenerator.createService(IRetrofit.class,"WINSPIREADMIN","winspire@123");
        Call<List<ResponseCategory>> call = jsonPostService.getResponseCategory();
        call.enqueue(new Callback<List<ResponseCategory>>() {

            @Override
            public void onResponse(Call<List<ResponseCategory>> call, Response<List<ResponseCategory>> response) {
                try{
                    Log.e("response-code", String.valueOf(response.code()));
                    Log.e("response-url", response.raw().request().url().toString());
                    Log.e("warehouse response", new Gson().toJson(response.body())); // to print json response


                    if (response.isSuccessful())
                    {
                        List<ResponseCategory> ResponseCategorylist = response.body();
                        ArrayList<String> problenfisArray = new ArrayList<>();

                        for (int i=0;i<ResponseCategorylist.size();i++)
                        {
                            problenfisArray.add(ResponseCategorylist.get(i).getCode());

                        }

                     /*   final List<KeyPairBoolData> spinnerProblemFixArray = new ArrayList<>();

                        for (int i = 0; i < ResponseCategorylist.size(); i++) {
                            KeyPairBoolData h = new KeyPairBoolData();
                            h.setId(i + 1);
                            h.setName(ResponseCategorylist.get(i).getName());
                            h.setCode(ResponseCategorylist.get(i).getCode());
                            h.setSelected(false);
                            spinnerProblemFixArray.add(h);
                        }*/

                        SpinnerRespCategory.setEmptyTitle("Not Data Found!");
                        SpinnerRespCategory.setSearchHint("Find Data");

               /*         SpinnerRespCategory.setItems(spinnerProblemFixArray, -1, new SpinnerListener() {

                            @Override
                            public void onItemsSelected(List<KeyPairBoolData> items) {

                                StringBuilder spinnerBuffer = new StringBuilder();
                                for (int i = 0; i < items.size(); i++) {
                                    if (items.get(i).isSelected()) {
                                        spinnerBuffer.append(items.get(i).getCode());
                                        spinnerBuffer.append(",");
                                        Log.i("", i + " : " + items.get(i).getName() + " : " + items.get(i).isSelected());
                                    }
                                }
                                RespCategorystr = spinnerBuffer.toString();
                                if (RespCategorystr.length() > 2)
                                    RespCategorystr = RespCategorystr.substring(0, RespCategorystr.length() - 1);
                                else
                                    RespCategorystr = "";
                                Log.d("solutionstr data : " ,RespCategorystr);
                            }
                        });*/

                        if(RespCategorystr != null)
                        {
                            final List<KeyPairBoolData> spinnerRespCategorysArray = new ArrayList<>();
                            String[] arr = RespCategorystr.split(",");

                            for (int i = 0; i < ResponseCategorylist.size(); i++) {
                                //    Log.d("val ",Solutionlist.get(i).getName());
                                KeyPairBoolData h = new KeyPairBoolData();
                                h.setId(i + 1);
                                h.setName(ResponseCategorylist.get(i).getName());
                                h.setCode(ResponseCategorylist.get(i).getCode());
                                for (int j = 0;j < arr.length;j++)
                                {
                                    if(ResponseCategorylist.get(i).getCode().equals(arr[j]))
                                    {
                                        h.setSelected(true);
                                        break;
                                    }else {
                                        h.setSelected(false);
                                    }
                                }

                                spinnerRespCategorysArray.add(h);
                            }

                            SpinnerRespCategory.setItems(spinnerRespCategorysArray, -1, new SpinnerListener() {

                                @Override
                                public void onItemsSelected(List<KeyPairBoolData> items) {
                                    StringBuilder spinnerBuffer = new StringBuilder();
                                    for (int i = 0; i < items.size(); i++) {
                                        if (items.get(i).isSelected()) {
                                            spinnerBuffer.append(items.get(i).getCode());
                                            spinnerBuffer.append(",");
                                            Log.i("", i + " : " + items.get(i).getName() + " : " + items.get(i).isSelected());
                                        }
                                    }
                                    RespCategorystr = spinnerBuffer.toString();
                                    if (RespCategorystr.length() > 2)
                                        RespCategorystr = RespCategorystr.substring(0, RespCategorystr.length() - 1);
                                    else
                                        RespCategorystr = "";
                                    Log.d("selected data : " ,RespCategorystr);
                                }
                            });

                        }else {

                            final List<KeyPairBoolData> spinnerRespCategorysArray = new ArrayList<>();

                            for (int i = 0; i < ResponseCategorylist.size(); i++) {
                                Log.d("val ",ResponseCategorylist.get(i).getName());
                                KeyPairBoolData h = new KeyPairBoolData();
                                h.setId(i + 1);
                                h.setName(ResponseCategorylist.get(i).getName());
                                h.setCode(ResponseCategorylist.get(i).getCode());
                                h.setSelected(false);
                                spinnerRespCategorysArray.add(h);
                            }

                            SpinnerRespCategory.setItems(spinnerRespCategorysArray, -1, new SpinnerListener() {

                                @Override
                                public void onItemsSelected(List<KeyPairBoolData> items) {
                                    StringBuilder spinnerBuffer = new StringBuilder();
                                    for (int i = 0; i < items.size(); i++) {
                                        if (items.get(i).isSelected()) {
                                            spinnerBuffer.append(items.get(i).getCode());
                                            spinnerBuffer.append(",");
                                            Log.i("", i + " : " + items.get(i).getName() + " : " + items.get(i).isSelected());
                                        }
                                    }
                                    RespCategorystr = spinnerBuffer.toString();
                                    if (RespCategorystr.length() > 2)
                                        RespCategorystr = RespCategorystr.substring(0, RespCategorystr.length() - 1);
                                    else
                                        RespCategorystr = "";
                                    Log.d("selected data : " ,RespCategorystr);
                                }
                            });

                        }


                    }
                    if (progressDialog != null)
                        if (progressDialog.isShowing())
                            progressDialog.dismiss();
                    getActivityTypeFromServer();

                }catch (Exception e){
                    if (progressDialog != null)
                        if (progressDialog.isShowing())
                            progressDialog.dismiss();
                    e.printStackTrace();
                    Toast.makeText(CallClloserActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("response-Exception", e.getMessage());
                }

            }

            @Override
            public void onFailure(Call<List<ResponseCategory>> call, Throwable t) {
                if (progressDialog != null)
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                Toast.makeText(CallClloserActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("response-failure", t.getMessage());
            }


        });
    }
    private void getActivityTypeFromServer() {
        progressDialog = ProgressDialog.show(CallClloserActivity.this, getApplicationContext().getResources().getString(R.string.app_name),
                "Please wait...", false, false);
        progressDialog.setCancelable(false);
        // Using the Retrofi
        IRetrofit jsonPostService = ServiceGenerator.createService(IRetrofit.class,"WINSPIREADMIN","winspire@123");
        Call<List<ActivityType>> call = jsonPostService.getActivityType();
        call.enqueue(new Callback<List<ActivityType>>() {

            @Override
            public void onResponse(Call<List<ActivityType>> call, Response<List<ActivityType>> response) {
                try{
                    Log.e("response-code", String.valueOf(response.code()));
                    Log.e("response-url", response.raw().request().url().toString());
                    Log.e("warehouse response", new Gson().toJson(response.body())); // to print json response


                    if (response.isSuccessful())
                    {
                        List<ActivityType> ActivityTypelist = response.body();
                        List<String> problenfisArray = new ArrayList<>();
                        problenfisArray.add("--Select--");

                        for (int i=0;i<ActivityTypelist.size();i++)
                        {
                            problenfisArray.add(ActivityTypelist.get(i).getCode());

                        }

                        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, problenfisArray); //selected item will look like a spinner set from XML
                        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerActivityType.setAdapter(spinnerArrayAdapter);
                        if(ActivityTypestr != null)
                        {
                            int spinnerPosition = spinnerArrayAdapter.getPosition(ActivityTypestr);
                            spinnerActivityType.setSelection(spinnerPosition);
                        }
                        spinnerActivityType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                ActivityTypestr = spinnerActivityType.getSelectedItem().toString();
                                Log.d("ActivityTypestr ",ActivityTypestr);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });


                    }
                    if (progressDialog != null)
                        if (progressDialog.isShowing())
                            progressDialog.dismiss();
                 //   getERRORCODEFromServer();
                    getIdleReasonFromServer();

                }catch (Exception e){
                    if (progressDialog != null)
                        if (progressDialog.isShowing())
                            progressDialog.dismiss();
                    e.printStackTrace();
                    Toast.makeText(CallClloserActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("response-Exception", e.getMessage());
                }

            }

            @Override
            public void onFailure(Call<List<ActivityType>> call, Throwable t) {
                if (progressDialog != null)
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                Toast.makeText(CallClloserActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("response-failure", t.getMessage());
            }


        });
    }

    private void getIdleReasonFromServer() {
        progressDialog = ProgressDialog.show(CallClloserActivity.this, getApplicationContext().getResources().getString(R.string.app_name),
                "Please wait...", false, false);
        progressDialog.setCancelable(false);
        // Using the Retrofi
        IRetrofit jsonPostService = ServiceGenerator.createService(IRetrofit.class,"WINSPIREADMIN","winspire@123");
        Call<List<GetIdleReason>> call = jsonPostService.getGetIdleReason();
        call.enqueue(new Callback<List<GetIdleReason>>() {

            @Override
            public void onResponse(Call<List<GetIdleReason>> call, Response<List<GetIdleReason>> response) {
                try{
                    Log.e("response-code", String.valueOf(response.code()));
                    Log.e("response-url", response.raw().request().url().toString());
                    Log.e("warehouse response", new Gson().toJson(response.body())); // to print json response


                    if (response.isSuccessful())
                    {
                        List<GetIdleReason> GetIdleReasonlist = response.body();
                        ReasonArrayArray = new ArrayList<>();
                        ReasonArrayArray.add("--Select--");
                        for (int i=0;i<GetIdleReasonlist.size();i++)
                        {
                            ReasonArrayArray.add(GetIdleReasonlist.get(i).getIdle_Reason());

                        }

                        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, ReasonArrayArray); //selected item will look like a spinner set from XML
                        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerTATMissReason.setAdapter(spinnerArrayAdapter);
                        if(TATMissReasonstr != null)
                        {
                            int spinnerPosition = spinnerArrayAdapter.getPosition(TATMissReasonstr);
                            spinnerTATMissReason.setSelection(spinnerPosition);
                        }

                        spinnerTATMissReason.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                TATMissReasonstr = spinnerTATMissReason.getSelectedItem().toString();
                                Log.d("TATMissReasonstr ",TATMissReasonstr);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });


                    }
                    if (progressDialog != null)
                        if (progressDialog.isShowing())
                            progressDialog.dismiss();
                    getQuestionnaire();


                }catch (Exception e){
                    if (progressDialog != null)
                        if (progressDialog.isShowing())
                            progressDialog.dismiss();
                    e.printStackTrace();
                    Toast.makeText(CallClloserActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("response-Exception", e.getMessage());
                }

            }

            @Override
            public void onFailure(Call<List<GetIdleReason>> call, Throwable t) {
                if (progressDialog != null)
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                Toast.makeText(CallClloserActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("response-failure", t.getMessage());
            }


        });
    }
    private void getQuestionnaire() {
        progressDialog = ProgressDialog.show(CallClloserActivity.this, getApplicationContext().getResources().getString(R.string.app_name),
                "Please wait...", false, false);
        progressDialog.setCancelable(false);
        // Using the Retrofi
        IRetrofit jsonPostService = ServiceGenerator.createService(IRetrofit.class,"WINSPIREADMIN","winspire@123");
        Call<List<Questionnaire>> call = jsonPostService.getQuestionnaire();
        call.enqueue(new Callback<List<Questionnaire>>() {

            @Override
            public void onResponse(Call<List<Questionnaire>> call, Response<List<Questionnaire>> response) {
                try{
                    Log.e("response-code", String.valueOf(response.code()));
                    Log.e("response-url", response.raw().request().url().toString());
                    Log.e("warehouse response", new Gson().toJson(response.body())); // to print json response


                    if (response.isSuccessful())
                    {
                        Questionnairelist = response.body();
                        List<String> problenfisArray = new ArrayList<>();

                        /*for (int i=0;i<Questionnairelist.size();i++)
                        {
                            problenfisArray.add(Questionnairelist.get(i).getQuestion());

                        }*/




                    }
                    if (progressDialog != null)
                        if (progressDialog.isShowing())
                            progressDialog.dismiss();
                    getQuestionnaireAnswer();

                }catch (Exception e){
                    if (progressDialog != null)
                        if (progressDialog.isShowing())
                            progressDialog.dismiss();
                    e.printStackTrace();
                    Toast.makeText(CallClloserActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("response-Exception", e.getMessage());
                }

            }

            @Override
            public void onFailure(Call<List<Questionnaire>> call, Throwable t) {
                if (progressDialog != null)
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                Toast.makeText(CallClloserActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("response-failure", t.getMessage());
            }


        });
    }
    private void getQuestionnaireAnswer() {
        progressDialog = ProgressDialog.show(CallClloserActivity.this, getApplicationContext().getResources().getString(R.string.app_name),
                "Please wait...", false, false);
        progressDialog.setCancelable(false);
        // Using the Retrofi
        IRetrofit jsonPostService = ServiceGenerator.createService(IRetrofit.class,"WINSPIREADMIN","winspire@123");
        Call<List<QuestionnaireAnswer>> call = jsonPostService.getQuestionnaireAnswer();
        call.enqueue(new Callback<List<QuestionnaireAnswer>>() {

            @Override
            public void onResponse(Call<List<QuestionnaireAnswer>> call, Response<List<QuestionnaireAnswer>> response) {
                try{
                    Log.e("response-code", String.valueOf(response.code()));
                    Log.e("response-url", response.raw().request().url().toString());
                    Log.e("warehouse response", new Gson().toJson(response.body())); // to print json response


                    if (response.isSuccessful())
                    {
                        QuestionnaireAnswerlist = response.body();
                        List<String> problenfisArray = new ArrayList<>();

                        /*for (int i=0;i<Questionnairelist.size();i++)
                        {
                            problenfisArray.add(Questionnairelist.get(i).getQuestion());

                        }*/




                    }
                    if (progressDialog != null)
                        if (progressDialog.isShowing())
                            progressDialog.dismiss();
                  //  getGetFuturePartReplace();
                    getERRORCODEFromServer();

                }catch (Exception e){
                    if (progressDialog != null)
                        if (progressDialog.isShowing())
                            progressDialog.dismiss();
                    e.printStackTrace();
                    Toast.makeText(CallClloserActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("response-Exception", e.getMessage());
                }

            }

            @Override
            public void onFailure(Call<List<QuestionnaireAnswer>> call, Throwable t) {
                if (progressDialog != null)
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                Toast.makeText(CallClloserActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("response-failure", t.getMessage());
            }


        });
    }

    private void getERRORCODEFromServer() {
        progressDialog = ProgressDialog.show(CallClloserActivity.this, getApplicationContext().getResources().getString(R.string.app_name),
                "Please wait...", false, false);
        progressDialog.setCancelable(false);
        // Using the Retrofi
        IRetrofit jsonPostService = ServiceGenerator.createService(IRetrofit.class,"WINSPIREADMIN","winspire@123");
        Call<List<ErrorCode>> call = jsonPostService.getErrorCode();
        call.enqueue(new Callback<List<ErrorCode>>() {

            @Override
            public void onResponse(Call<List<ErrorCode>> call, Response<List<ErrorCode>> response) {
                try{
                    Log.e("response-code", String.valueOf(response.code()));
                    Log.e("response-url", response.raw().request().url().toString());
                    Log.e("warehouse response", new Gson().toJson(response.body())); // to print json response


                    if (response.isSuccessful())
                    {
                        List<ErrorCode>ErrorCodelist = response.body();
                        ArrayList<String> problenfisArray = new ArrayList<>();

                        for (int i=0;i<ErrorCodelist.size();i++)
                        {
                            problenfisArray.add(ErrorCodelist.get(i).getCode());

                        }

                     /*   final List<KeyPairBoolData> spinnerProblemFixArray = new ArrayList<>();

                        for (int i = 0; i < problenfisArray.size(); i++) {
                            KeyPairBoolData h = new KeyPairBoolData();
                            h.setId(i + 1);
                            h.setName(ErrorCodelist.get(i).getDescription());
                            h.setCode(ErrorCodelist.get(i).getCode());
                            h.setSelected(false);
                            spinnerProblemFixArray.add(h);
                        }*/

                        SpinnerErrorCode.setEmptyTitle("Not Data Found!");
                        SpinnerErrorCode.setSearchHint("Find Data");

             /*           SpinnerErrorCode.setItems(spinnerProblemFixArray, -1, new SpinnerListener() {

                            @Override
                            public void onItemsSelected(List<KeyPairBoolData> items) {

                                StringBuilder spinnerBuffer = new StringBuilder();
                                for (int i = 0; i < items.size(); i++) {
                                    if (items.get(i).isSelected()) {
                                        spinnerBuffer.append(items.get(i).getCode());
                                        spinnerBuffer.append(",");
                                        Log.i("", i + " : " + items.get(i).getName() + " : " + items.get(i).isSelected());
                                    }
                                }
                                ErrorCodestr = spinnerBuffer.toString();
                                if (ErrorCodestr.length() > 2)
                                    ErrorCodestr = ErrorCodestr.substring(0, ErrorCodestr.length() - 1);
                                else
                                    ErrorCodestr = "";
                                Log.d("solutionstr data : " ,ErrorCodestr);

                            }
                        });*/



                        if(ErrorCodestr != null)
                        {
                            final List<KeyPairBoolData> spinnerErrorCodeArray = new ArrayList<>();
                            String[] arr = ErrorCodestr.split(",");

                            for (int i = 0; i < ErrorCodelist.size(); i++) {
                                //    Log.d("val ",Solutionlist.get(i).getName());
                                KeyPairBoolData h = new KeyPairBoolData();
                                h.setId(i + 1);
                                h.setName(ErrorCodelist.get(i).getDescription());
                                h.setCode(ErrorCodelist.get(i).getCode());
                                for (int j = 0;j < arr.length;j++)
                                {
                                    if(ErrorCodelist.get(i).getCode().equals(arr[j]))
                                    {
                                        h.setSelected(true);
                                        break;
                                    }else {
                                        h.setSelected(false);
                                    }
                                }

                                spinnerErrorCodeArray.add(h);
                            }

                            SpinnerErrorCode.setItems(spinnerErrorCodeArray, -1, new SpinnerListener() {

                                @Override
                                public void onItemsSelected(List<KeyPairBoolData> items) {
                                    StringBuilder spinnerBuffer = new StringBuilder();
                                    for (int i = 0; i < items.size(); i++) {
                                        if (items.get(i).isSelected()) {
                                            spinnerBuffer.append(items.get(i).getCode());
                                            spinnerBuffer.append(",");
                                            Log.i("", i + " : " + items.get(i).getName() + " : " + items.get(i).isSelected());
                                        }
                                    }
                                    ErrorCodestr = spinnerBuffer.toString();
                                    if (ErrorCodestr.length() > 2)
                                        ErrorCodestr = ErrorCodestr.substring(0, ErrorCodestr.length() - 1);
                                    else
                                        ErrorCodestr = "";
                                    Log.d("selected data : " ,ErrorCodestr);
                                }
                            });

                        }else {

                            final List<KeyPairBoolData> spinnerErrorCodeArray = new ArrayList<>();

                            for (int i = 0; i < ErrorCodelist.size(); i++) {
                               // Log.d("val ",ErrorCodelist.get(i).getName());
                                KeyPairBoolData h = new KeyPairBoolData();
                                h.setId(i + 1);
                                h.setName(ErrorCodelist.get(i).getDescription());
                                h.setCode(ErrorCodelist.get(i).getCode());
                                h.setSelected(false);
                                spinnerErrorCodeArray.add(h);
                            }

                            SpinnerErrorCode.setItems(spinnerErrorCodeArray, -1, new SpinnerListener() {

                                @Override
                                public void onItemsSelected(List<KeyPairBoolData> items) {
                                    StringBuilder spinnerBuffer = new StringBuilder();
                                    for (int i = 0; i < items.size(); i++) {
                                        if (items.get(i).isSelected()) {
                                            spinnerBuffer.append(items.get(i).getCode());
                                            spinnerBuffer.append(",");
                                            Log.i("", i + " : " + items.get(i).getName() + " : " + items.get(i).isSelected());
                                        }
                                    }
                                    ErrorCodestr = spinnerBuffer.toString();
                                    if (ErrorCodestr.length() > 2)
                                        ErrorCodestr = ErrorCodestr.substring(0, ErrorCodestr.length() - 1);
                                    else
                                        ErrorCodestr = "";
                                    Log.d("selected data : " ,ErrorCodestr);
                                }
                            });

                        }



                    }
                    if (progressDialog != null)
                        if (progressDialog.isShowing())
                            progressDialog.dismiss();
                    getModuleAffectedFromServer();
                    getGetFuturePartReplace();

                }catch (Exception e){
                    if (progressDialog != null)
                        if (progressDialog.isShowing())
                            progressDialog.dismiss();
                    e.printStackTrace();
                    Toast.makeText(CallClloserActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("response-Exception", e.getMessage());
                }

            }

            @Override
            public void onFailure(Call<List<ErrorCode>> call, Throwable t) {
                if (progressDialog != null)
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                Toast.makeText(CallClloserActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("response-failure", t.getMessage());
            }


        });
    }

    private void getModuleAffectedFromServer() {
       /* progressDialog = ProgressDialog.show(CallClloserActivity.this, getApplicationContext().getResources().getString(R.string.app_name),
                "Please wait...", false, false);
        progressDialog.setCancelable(false);*/
        // Using the Retrofi
        IRetrofit jsonPostService = ServiceGenerator.createService(IRetrofit.class,"WINSPIREADMIN","winspire@123");
        Call<List<ModuleAffected>> call = jsonPostService.getModuleAffected();
        call.enqueue(new Callback<List<ModuleAffected>>() {

            @Override
            public void onResponse(Call<List<ModuleAffected>> call, Response<List<ModuleAffected>> response) {
                try{
                    Log.e("response-code", String.valueOf(response.code()));
                    Log.e("response-url", response.raw().request().url().toString());
                    Log.e("warehouse response", new Gson().toJson(response.body())); // to print json response


                    if (response.isSuccessful())
                    {
                        List<ModuleAffected> ModuleAffectedlist = response.body();
                        ArrayList<String> problenfisArray = new ArrayList<>();

                        for (int i=0;i<ModuleAffectedlist.size();i++)
                        {
                            problenfisArray.add(ModuleAffectedlist.get(i).getCode());

                        }

                   /*     final List<KeyPairBoolData> spinnerProblemFixArray = new ArrayList<>();

                        for (int i = 0; i < ModuleAffectedlist.size(); i++) {
                            KeyPairBoolData h = new KeyPairBoolData();
                            h.setId(i + 1);
                            h.setName(ModuleAffectedlist.get(i).getDescription());
                            h.setCode(ModuleAffectedlist.get(i).getCode());
                            h.setSelected(false);
                            spinnerProblemFixArray.add(h);
                        }*/

                        SpinnerModuleAffected.setEmptyTitle("Not Data Found!");
                        SpinnerModuleAffected.setSearchHint("Find Data");

                        if(ModuleAffectedstr != null)
                        {
                            final List<KeyPairBoolData> spinnerModuleAffectedArray = new ArrayList<>();
                            String[] arr = ModuleAffectedstr.split(",");

                            for (int i = 0; i < ModuleAffectedlist.size(); i++) {
                                //    Log.d("val ",Solutionlist.get(i).getName());
                                KeyPairBoolData h = new KeyPairBoolData();
                                h.setId(i + 1);
                                h.setName(ModuleAffectedlist.get(i).getDescription());
                                h.setCode(ModuleAffectedlist.get(i).getCode());
                                for (int j = 0;j < arr.length;j++)
                                {
                                    if(ModuleAffectedlist.get(i).getCode().equals(arr[j]))
                                    {
                                        h.setSelected(true);
                                        break;
                                    }else {
                                        h.setSelected(false);
                                    }
                                }

                                spinnerModuleAffectedArray.add(h);
                            }

                            SpinnerModuleAffected.setItems(spinnerModuleAffectedArray, -1, new SpinnerListener() {

                                @Override
                                public void onItemsSelected(List<KeyPairBoolData> items) {
                                 //   SubModuleAffectedstr = null;
                                    StringBuilder spinnerBuffer = new StringBuilder();
                                    for (int i = 0; i < items.size(); i++) {
                                        if (items.get(i).isSelected()) {
                                            spinnerBuffer.append(items.get(i).getCode());
                                            spinnerBuffer.append(",");
                                            Log.i("", i + " : " + items.get(i).getName() + " : " + items.get(i).isSelected());
                                        }
                                    }
                                    ModuleAffectedstr = spinnerBuffer.toString();
                                    if (ModuleAffectedstr.length() > 2)
                                        ModuleAffectedstr = ModuleAffectedstr.substring(0, ModuleAffectedstr.length() - 1);
                                    else
                                        ModuleAffectedstr = "";
                                    Log.d("selected data : " ,ModuleAffectedstr);
                                    getSubModuleAffectedFromServer(ModuleAffectedstr);
                                }
                            });

                        }else {

                            final List<KeyPairBoolData> spinnerModuleAffectedArray = new ArrayList<>();

                            for (int i = 0; i < ModuleAffectedlist.size(); i++) {
                                // Log.d("val ",ErrorCodelist.get(i).getName());
                                KeyPairBoolData h = new KeyPairBoolData();
                                h.setId(i + 1);
                                h.setName(ModuleAffectedlist.get(i).getDescription());
                                h.setCode(ModuleAffectedlist.get(i).getCode());
                                h.setSelected(false);
                                spinnerModuleAffectedArray.add(h);
                            }

                            SpinnerModuleAffected.setItems(spinnerModuleAffectedArray, -1, new SpinnerListener() {

                                @Override
                                public void onItemsSelected(List<KeyPairBoolData> items) {
                                  //  SubModuleAffectedstr = null;
                                    StringBuilder spinnerBuffer = new StringBuilder();
                                    for (int i = 0; i < items.size(); i++) {
                                        if (items.get(i).isSelected()) {
                                            spinnerBuffer.append(items.get(i).getCode());
                                            spinnerBuffer.append(",");
                                            Log.i("", i + " : " + items.get(i).getName() + " : " + items.get(i).isSelected());
                                        }
                                    }
                                    ModuleAffectedstr = spinnerBuffer.toString();
                                    if (ModuleAffectedstr.length() > 2)
                                        ModuleAffectedstr = ModuleAffectedstr.substring(0, ModuleAffectedstr.length() - 1);
                                    else
                                        ModuleAffectedstr = "";
                                    Log.d("selected data : " ,ModuleAffectedstr);
                                    getSubModuleAffectedFromServer(ModuleAffectedstr);
                                }
                            });

                        }

                        if(SubModuleAffectedstr != null)
                        {
                            getSubModuleAffectedFromServer(ModuleAffectedstr);
                        }/*else {
                            getGetFuturePartReplace();

                        }*/

                     /*   SpinnerModuleAffected.setItems(spinnerProblemFixArray, -1, new SpinnerListener() {

                            @Override
                            public void onItemsSelected(List<KeyPairBoolData> items) {

                                StringBuilder spinnerBuffer = new StringBuilder();
                                for (int i = 0; i < items.size(); i++) {
                                    if (items.get(i).isSelected()) {
                                        spinnerBuffer.append(items.get(i).getCode());
                                        spinnerBuffer.append(",");
                                        Log.i("", i + " : " + items.get(i).getName() + " : " + items.get(i).isSelected());
                                    }
                                }
                                ModuleAffectedstr = spinnerBuffer.toString();
                                if (ModuleAffectedstr.length() > 2)
                                    ModuleAffectedstr = ModuleAffectedstr.substring(0, ModuleAffectedstr.length() - 1);
                                else
                                    ModuleAffectedstr = "";
                                Log.d("solutionstr data : " ,ModuleAffectedstr);
                                getSubModuleAffectedFromServer(ModuleAffectedstr);
                            }
                        });*/


                    }
                  /*  if (progressDialog != null)
                        if (progressDialog.isShowing())
                            progressDialog.dismiss();*/
                 //   getSubModuleAffectedFromServer(ModuleAffectedstr);
                //    getIdleReasonFromServer();
                  //  getGetFuturePartReplace();

                }catch (Exception e){
                   /* if (progressDialog != null)
                        if (progressDialog.isShowing())
                            progressDialog.dismiss();*/
                    e.printStackTrace();
                    Toast.makeText(CallClloserActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("response-Exception", e.getMessage());
                }

            }

            @Override
            public void onFailure(Call<List<ModuleAffected>> call, Throwable t) {
               /* if (progressDialog != null)
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();*/
                Toast.makeText(CallClloserActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("response-failure", t.getMessage());
            }


        });
    }
    private void getSubModuleAffectedFromServer(String modulselected) {
        progressDialog = ProgressDialog.show(CallClloserActivity.this, getApplicationContext().getResources().getString(R.string.app_name),
                "Please wait...", false, false);
        progressDialog.setCancelable(false);
        // Using the Retrofi
        IRetrofit jsonPostService = ServiceGenerator.createService(IRetrofit.class,"WINSPIREADMIN","winspire@123");
        Call<List<GetSubModuleAffected>> call = jsonPostService.getGetSubModuleAffected(modulselected);
        call.enqueue(new Callback<List<GetSubModuleAffected>>() {

            @Override
            public void onResponse(Call<List<GetSubModuleAffected>> call, Response<List<GetSubModuleAffected>> response) {
                try{
                    Log.e("response-code", String.valueOf(response.code()));
                    Log.e("response-url", response.raw().request().url().toString());
                    Log.e("warehouse response", new Gson().toJson(response.body())); // to print json response


                    if (response.isSuccessful())
                    {
                        List<GetSubModuleAffected> GetSubModuleAffectedlist = response.body();
                        ArrayList<String> problenfisArray = new ArrayList<>();

                        for (int i=0;i<GetSubModuleAffectedlist.size();i++)
                        {
                            problenfisArray.add(GetSubModuleAffectedlist.get(i).getCode());

                        }

                    /*    final List<KeyPairBoolData> spinnerProblemFixArray = new ArrayList<>();

                        for (int i = 0; i < GetSubModuleAffectedlist.size(); i++) {
                            KeyPairBoolData h = new KeyPairBoolData();
                            h.setId(i + 1);
                            h.setName(GetSubModuleAffectedlist.get(i).getDescription());
                            h.setCode(GetSubModuleAffectedlist.get(i).getCode());
                            h.setSelected(false);
                            spinnerProblemFixArray.add(h);
                        }*/

                        SpinnerSubModuleAffected.setEmptyTitle("Not Data Found!");
                        SpinnerSubModuleAffected.setSearchHint("Find Data");


                        if(SubModuleAffectedstr != null)
                        {
                            final List<KeyPairBoolData> spinnerSubModuleAffectedArray = new ArrayList<>();
                            String[] arr = SubModuleAffectedstr.split(",");

                            for (int i = 0; i < GetSubModuleAffectedlist.size(); i++) {
                                //    Log.d("val ",Solutionlist.get(i).getName());
                                KeyPairBoolData h = new KeyPairBoolData();
                                h.setId(i + 1);
                                h.setName(GetSubModuleAffectedlist.get(i).getDescription());
                                h.setCode(GetSubModuleAffectedlist.get(i).getCode());
                                for (int j = 0;j < arr.length;j++)
                                {
                                    if(GetSubModuleAffectedlist.get(i).getCode().equals(arr[j]))
                                    {
                                        h.setSelected(true);
                                        break;
                                    }else {
                                        h.setSelected(false);
                                    }
                                }

                                spinnerSubModuleAffectedArray.add(h);
                            }

                            SpinnerSubModuleAffected.setItems(spinnerSubModuleAffectedArray, -1, new SpinnerListener() {

                                @Override
                                public void onItemsSelected(List<KeyPairBoolData> items) {
                                    StringBuilder spinnerBuffer = new StringBuilder();
                                    for (int i = 0; i < items.size(); i++) {
                                        if (items.get(i).isSelected()) {
                                            spinnerBuffer.append(items.get(i).getCode());
                                            spinnerBuffer.append(",");
                                            Log.i("", i + " : " + items.get(i).getName() + " : " + items.get(i).isSelected());
                                        }
                                    }
                                    SubModuleAffectedstr = spinnerBuffer.toString();
                                    if (SubModuleAffectedstr.length() > 2)
                                        SubModuleAffectedstr = SubModuleAffectedstr.substring(0, SubModuleAffectedstr.length() - 1);
                                    else
                                        SubModuleAffectedstr = "";
                                    Log.d("selected data : " ,SubModuleAffectedstr);

                                }
                            });

                        }else {

                            final List<KeyPairBoolData> spinnerSubModuleAffectedArray = new ArrayList<>();

                            for (int i = 0; i < GetSubModuleAffectedlist.size(); i++) {
                                // Log.d("val ",ErrorCodelist.get(i).getName());
                                KeyPairBoolData h = new KeyPairBoolData();
                                h.setId(i + 1);
                                h.setName(GetSubModuleAffectedlist.get(i).getDescription());
                                h.setCode(GetSubModuleAffectedlist.get(i).getCode());
                                h.setSelected(false);
                                spinnerSubModuleAffectedArray.add(h);
                            }

                            SpinnerSubModuleAffected.setItems(spinnerSubModuleAffectedArray, -1, new SpinnerListener() {

                                @Override
                                public void onItemsSelected(List<KeyPairBoolData> items) {
                                    StringBuilder spinnerBuffer = new StringBuilder();
                                    for (int i = 0; i < items.size(); i++) {
                                        if (items.get(i).isSelected()) {
                                            spinnerBuffer.append(items.get(i).getCode());
                                            spinnerBuffer.append(",");
                                            Log.i("", i + " : " + items.get(i).getName() + " : " + items.get(i).isSelected());
                                        }
                                    }
                                    SubModuleAffectedstr = spinnerBuffer.toString();
                                    if (SubModuleAffectedstr.length() > 2)
                                        SubModuleAffectedstr = SubModuleAffectedstr.substring(0, SubModuleAffectedstr.length() - 1);
                                    else
                                        SubModuleAffectedstr = "";
                                    Log.d("selected data : " ,SubModuleAffectedstr);

                                }
                            });

                        }

                  /*      SpinnerSubModuleAffected.setItems(spinnerProblemFixArray, -1, new SpinnerListener() {

                            @Override
                            public void onItemsSelected(List<KeyPairBoolData> items) {

                                StringBuilder spinnerBuffer = new StringBuilder();
                                for (int i = 0; i < items.size(); i++) {
                                    if (items.get(i).isSelected()) {
                                        spinnerBuffer.append(items.get(i).getCode());
                                        spinnerBuffer.append(",");
                                        Log.i("", i + " : " + items.get(i).getName() + " : " + items.get(i).isSelected());
                                    }
                                }
                                SubModuleAffectedstr = spinnerBuffer.toString();
                                if (SubModuleAffectedstr.length() > 2)
                                    SubModuleAffectedstr = SubModuleAffectedstr.substring(0, SubModuleAffectedstr.length() - 1);
                                else
                                    SubModuleAffectedstr = "";
                                Log.d("solutionstr data : " ,SubModuleAffectedstr);
                            }
                        });*/


                    }
                    if (progressDialog != null)
                        if (progressDialog.isShowing())
                            progressDialog.dismiss();
                       /* if(SubModuleAffectedstr != null)
                        {
                            getGetFuturePartReplace();
                        }*/
                  //  getIdleReasonFromServer();

                }catch (Exception e){
                    if (progressDialog != null)
                        if (progressDialog.isShowing())
                            progressDialog.dismiss();
                    e.printStackTrace();
                    Toast.makeText(CallClloserActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("response-Exception", e.getMessage());
                }

            }

            @Override
            public void onFailure(Call<List<GetSubModuleAffected>> call, Throwable t) {
               if (progressDialog != null)
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                Toast.makeText(CallClloserActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("response-failure", t.getMessage());
            }


        });
    }




    private void getGetFuturePartReplace() {
      /*  progressDialog = ProgressDialog.show(CallClloserActivity.this, getApplicationContext().getResources().getString(R.string.app_name),
                "Please wait...", false, false);
        progressDialog.setCancelable(false);*/
        // Using the Retrofi
        IRetrofit jsonPostService = ServiceGenerator.createService(IRetrofit.class,"WINSPIREADMIN","winspire@123");
        Call<List<GetFuturePartReplace>> call = jsonPostService.getGetFuturePartReplace();
        call.enqueue(new Callback<List<GetFuturePartReplace>>() {

            @Override
            public void onResponse(Call<List<GetFuturePartReplace>> call, Response<List<GetFuturePartReplace>> response) {
                try{
                    Log.e("response-code", String.valueOf(response.code()));
                    Log.e("response-url", response.raw().request().url().toString());
                    Log.e("warehouse response", new Gson().toJson(response.body())); // to print json response


                    if (response.isSuccessful())
                    {
                        List<GetFuturePartReplace> GetFuturePartReplacelist = response.body();
                        ArrayList<String> problenfisArray = new ArrayList<>();

                        /*for (int i=0;i<GetFuturePartReplacelist.size();i++)
                        {
                            problenfisArray.add(GetFuturePartReplacelist.get(i).getCode());

                        }*/

                       /* final List<KeyPairBoolData> spinnerProblemFixArray = new ArrayList<>();

                        for (int i = 0; i < GetFuturePartReplacelist.size(); i++) {
                            KeyPairBoolData h = new KeyPairBoolData();
                            h.setId(i + 1);
                            h.setName(GetFuturePartReplacelist.get(i).getName());
                            h.setCode(GetFuturePartReplacelist.get(i).getNo_());
                            h.setSelected(false);
                            spinnerProblemFixArray.add(h);
                        }*/

                        SpinnerFuturePart.setEmptyTitle("Not Data Found!");
                        SpinnerFuturePart.setSearchHint("Find Data");


                        if(FuturePartstr != null)
                        {
                            final List<KeyPairBoolData> spinnerSubModuleAffectedArray = new ArrayList<>();
                            String[] arr = FuturePartstr.split(",");

                            for (int i = 0; i < GetFuturePartReplacelist.size(); i++) {
                                //    Log.d("val ",Solutionlist.get(i).getName());
                                KeyPairBoolData h = new KeyPairBoolData();
                                h.setId(i + 1);
                                h.setName(GetFuturePartReplacelist.get(i).getName());
                                h.setCode(GetFuturePartReplacelist.get(i).getNo_());
                                for (int j = 0;j < arr.length;j++)
                                {
                                    if(GetFuturePartReplacelist.get(i).getNo_().equals(arr[j]))
                                    {
                                        h.setSelected(true);
                                        break;
                                    }else {
                                        h.setSelected(false);
                                    }
                                }

                                spinnerSubModuleAffectedArray.add(h);
                            }

                            SpinnerFuturePart.setItems(spinnerSubModuleAffectedArray, -1, new SpinnerListener() {

                                @Override
                                public void onItemsSelected(List<KeyPairBoolData> items) {
                                    StringBuilder spinnerBuffer = new StringBuilder();
                                    for (int i = 0; i < items.size(); i++) {
                                        if (items.get(i).isSelected()) {
                                            spinnerBuffer.append(items.get(i).getCode());
                                            spinnerBuffer.append(",");
                                            Log.i("", i + " : " + items.get(i).getName() + " : " + items.get(i).isSelected());
                                        }
                                    }
                                    FuturePartstr = spinnerBuffer.toString();
                                    if (FuturePartstr.length() > 2)
                                        FuturePartstr = FuturePartstr.substring(0, FuturePartstr.length() - 1);
                                    else
                                        FuturePartstr = "";
                                    Log.d("selected data : " ,FuturePartstr);

                                }
                            });

                        }else {

                            final List<KeyPairBoolData> spinnerSubModuleAffectedArray = new ArrayList<>();

                            for (int i = 0; i < GetFuturePartReplacelist.size(); i++) {
                                // Log.d("val ",ErrorCodelist.get(i).getName());
                                KeyPairBoolData h = new KeyPairBoolData();
                                h.setId(i + 1);
                                h.setName(GetFuturePartReplacelist.get(i).getName());
                                h.setCode(GetFuturePartReplacelist.get(i).getNo_());
                                h.setSelected(false);
                                spinnerSubModuleAffectedArray.add(h);
                            }

                            SpinnerFuturePart.setItems(spinnerSubModuleAffectedArray, -1, new SpinnerListener() {

                                @Override
                                public void onItemsSelected(List<KeyPairBoolData> items) {
                                    StringBuilder spinnerBuffer = new StringBuilder();
                                    for (int i = 0; i < items.size(); i++) {
                                        if (items.get(i).isSelected()) {
                                            spinnerBuffer.append(items.get(i).getCode());
                                            spinnerBuffer.append(",");
                                            Log.i("", i + " : " + items.get(i).getName() + " : " + items.get(i).isSelected());
                                        }
                                    }
                                    FuturePartstr = spinnerBuffer.toString();
                                    if (FuturePartstr.length() > 2)
                                        FuturePartstr = FuturePartstr.substring(0, FuturePartstr.length() - 1);
                                    else
                                        FuturePartstr = "";
                                    Log.d("selected data : " ,FuturePartstr);

                                }
                            });

                        }


                  /*      SpinnerFuturePart.setItems(spinnerProblemFixArray, -1, new SpinnerListener() {

                            @Override
                            public void onItemsSelected(List<KeyPairBoolData> items) {

                                StringBuilder spinnerBuffer = new StringBuilder();
                                for (int i = 0; i < items.size(); i++) {
                                    if (items.get(i).isSelected()) {
                                        spinnerBuffer.append(items.get(i).getCode());
                                        spinnerBuffer.append(",");
                                        Log.i("", i + " : " + items.get(i).getName() + " : " + items.get(i).isSelected());
                                    }
                                }
                                FuturePartstr = spinnerBuffer.toString();
                                if (FuturePartstr.length() > 2)
                                    FuturePartstr = FuturePartstr.substring(0, FuturePartstr.length() - 1);
                                else
                                    FuturePartstr = "";
                                Log.d("solutionstr data : " ,FuturePartstr);
                            }
                        });*/


                    }
                   /* if (progressDialog != null)
                        if (progressDialog.isShowing())
                            progressDialog.dismiss();*/
                  //  getIdleReasonFromServer();

                }catch (Exception e){
                   /* if (progressDialog != null)
                        if (progressDialog.isShowing())
                            progressDialog.dismiss();*/
                    e.printStackTrace();
                    Toast.makeText(CallClloserActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("response-Exception", e.getMessage());
                }

            }

            @Override
            public void onFailure(Call<List<GetFuturePartReplace>> call, Throwable t) {
               /* if (progressDialog != null)
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();*/
                Toast.makeText(CallClloserActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("response-failure", t.getMessage());
            }


        });
    }
}
