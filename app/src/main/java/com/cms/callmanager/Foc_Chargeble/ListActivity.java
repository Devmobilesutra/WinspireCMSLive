package com.cms.callmanager.Foc_Chargeble;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cms.callmanager.Prefs;
import com.cms.callmanager.R;
import com.cms.callmanager.constants.Constants;
import com.cms.callmanager.services.CallManagerAsyncTaskArray;
import com.cms.callmanager.utils.ProgressUtil;
import com.cms.callmanager.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.util.ArrayList;

import static com.cms.callmanager.constants.Constant.UserId;


public class ListActivity extends AppCompatActivity {
    public static String SELECTED_POSITIONS = "selectedPositions";
    final int LIST_RESULT = 100;
    private static final int PICK_FILE_REQUEST = 1;
    private static final int PICK_FILE_REQUEST1 = 2;
    private static final int STORAGE_PERMISSION_CODE = 123;
    PowerManager.WakeLock wakeLock;
    private String selectedFilePath_fcr,selectedFilePath_valtage;
    private static final String TAG = ListActivity.class.getSimpleName();

    // JSONObject finalObject_foc;
   public static JSONArray jsonArrayfor_foc;
    Bitmap FixBitmap,FixBitmap1;
    byte[] byteArray,byteArray1;
    String FCRIMAGE_BASE64, VOLTAGE_IMAGE_BASE64 ;
    ByteArrayOutputStream byteArrayOutputStream ;


    File outPutFile,outPutFile1;;



    // shared preference

    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String fcr_path = "foc";
    public static final String voltage_img_path= "charge";
    SharedPreferences sharedpreferences;



    ArrayList<ModelClassForSavedData> foc_list;
    ArrayList<ModelClassForSavedData_Charge> charge_list;
    //  ArrayList<String> chargeList;
    public  static JSONObject jsonObj_valtage_images;
    public  static JSONObject jsonObject_fcr_images;
    ArrayList<String> Foc_NO = new ArrayList<String>();
    ArrayList<String> Charge_NO = new ArrayList<String>();
    RecyclerView recyclerView,charge_rv;
    FOC_ListAdapter FOCListAdapter;
    ChargeListAdapter charge_listAdapter;
    LinearLayoutManager llm;
    LinearLayoutManager llm1;
    Button submit_button_foc,submit_button_charge,
            foc_btn,charge_btn,fcr_doc,voltage;
    TextView fcr_file,valtage_file;
    LinearLayout charge_layout;
    public static String qty_value;
    String ATM_ID;
    String DOCKET_NO;
    String subStatusId;
    TextView NodataText;

    //   public static JSONArray jsonArrayfor_foc = new JSONArray();
    public static JSONArray jsonArrayfor_charge ;


    public static JSONArray jsonArray_for_images ;
    JSONObject json_obj_for_image = new JSONObject();
    private ProgressDialog progDailog;
    private ArrayList<Foc_list_Model> foc_list_models;

    private ArrayList<Charge_list_Model> charge_list_models;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        setFinishOnTouchOutside(false);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        NodataText = (TextView)findViewById(R.id.NodataText);

        requestStoragePermission();
        init();
        OnClickEvent();

        byteArrayOutputStream = new ByteArrayOutputStream();
        ATM_ID = (getIntent().getStringExtra("ATM_ID"));
        DOCKET_NO = (getIntent().getStringExtra("DOCKET_NO"));
        subStatusId = (getIntent().getStringExtra("subStatusId"));

        Log.d(TAG, "onCreate_ATM_ID: "+ ATM_ID);
        Log.d(TAG, "onCreate_DOCKET_NO: "+ DOCKET_NO);
        Log.d(TAG, "onCreate_subStatusId: "+ subStatusId);

        /*------------------- get foc list------------------*/

        foc_list = (ArrayList<ModelClassForSavedData>) getIntent().getSerializableExtra("list");
        ArrayList<Integer> selectedPositions = (ArrayList<Integer>) getIntent().getSerializableExtra(SELECTED_POSITIONS);
        foc_list_models=new ArrayList<>();
        //To show at least one row
        if(foc_list == null || foc_list.size() == 0) {
            foc_list = new ArrayList<>();
            foc_list.add(new ModelClassForSavedData("","",""));
        }else{


            /*-------------- FOC Asyntask ------------*/
            new FOC_list_asyntask(Constants.FOC_LIST_URL1 + "?AtmId=" + ATM_ID, "GET", ListActivity.this, new APIListner() {
                @Override
                public void onSuccess() {
                    //  Foc_NO.add("--select--");

                    FOCListAdapter.setFoc_NO(Foc_NO,foc_list_models);
                    FOCListAdapter.notifyDataSetChanged();
                }
                @Override
                public void onErrors() {

                }
            }).execute();



            recyclerView.setVisibility(View.VISIBLE);
            submit_button_foc.setVisibility(View.VISIBLE);
            // charge_btn.setVisibility(View.GONE);
            charge_btn.setEnabled(false);
            foc_btn.setEnabled(false);
            charge_btn.setBackgroundColor(getResources().getColor(R.color.red_light));


        }

        /*--------------- FOC Adapter -----------------*/
        FOCListAdapter = new FOC_ListAdapter(foc_list, this);
        llm = new LinearLayoutManager(this);
        recyclerView.setAdapter(FOCListAdapter);
        recyclerView.setLayoutManager(llm);




        /*------------------- get charge list------------------*/
        charge_list = (ArrayList<ModelClassForSavedData_Charge>) getIntent().getSerializableExtra("list1");
        charge_list_models=new ArrayList<>();

        //Toast.makeText(this, ""+charge_list, Toast.LENGTH_SHORT).show();
        if(charge_list == null || charge_list.size() == 0) {
            charge_list = new ArrayList<>();
            charge_list.add(new ModelClassForSavedData_Charge("","",""));
        }

        else {

            /* ------------- Charge List Asyntask ------------*/

            new Charge_list_asyntask(Constants.CHARGE_LIST_URL1 + "?AtmId=" + ATM_ID, "GET", ListActivity.this, new APIListner() {
                @Override
                public void onSuccess() {
                    //  Foc_NO.add("--select--");
                    if(charge_listAdapter == null){

                    }else {
                        charge_listAdapter.setCharge_NO(Charge_NO, charge_list_models);
                        charge_listAdapter.notifyDataSetChanged();
                    }
                }
                @Override
                public void onErrors() {

                }
            }).execute();

            recyclerView.setVisibility(View.GONE);
            charge_rv.setVisibility(View.VISIBLE);
            submit_button_foc.setVisibility(View.GONE);
            charge_btn.setVisibility(View.VISIBLE);
            submit_button_charge.setVisibility(View.VISIBLE);
            charge_layout.setVisibility(View.VISIBLE);
            charge_btn.setEnabled(false);
            foc_btn.setEnabled(false);
            foc_btn.setBackgroundColor(getResources().getColor(R.color.red_light));

            /* get images path from shared preference*/

            String fcr_str = sharedpreferences.getString(fcr_path, "");
            String vol_str = sharedpreferences.getString(voltage_img_path, "");

            if((fcr_str != null)|| (vol_str != null)){
                fcr_file.setText(fcr_str);
                valtage_file.setText(vol_str);
            }else {}

        }


        /*---------------------- Cahrge Adapter ---------------*/
        charge_listAdapter = new ChargeListAdapter(charge_list, ListActivity.this);
        llm1 = new LinearLayoutManager(ListActivity.this);
        charge_rv.setAdapter(charge_listAdapter);
        charge_rv.setLayoutManager(llm1);



    }


    void init(){
        submit_button_foc = (Button) findViewById(R.id.submit_button_foc);
        submit_button_charge = (Button) findViewById(R.id.submit_button_charge);
        recyclerView = (RecyclerView) findViewById(R.id.rv);
        charge_rv=(RecyclerView)findViewById(R.id.charge_rv);
        foc_btn= (Button)findViewById(R.id.foc_btn);
        charge_btn=(Button)findViewById(R.id.charge_btn);
        fcr_doc=(Button)findViewById(R.id.fcr_doc);
        voltage=(Button) findViewById(R.id.voltage);
        charge_layout =(LinearLayout)findViewById(R.id.chaege_layout);
        fcr_file =(TextView)findViewById(R.id.fcr_file);
        valtage_file =(TextView)findViewById(R.id.valtage_file);


    }

    void OnClickEvent(){


        foc_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //   + "?AtmId=" + ATM_ID

                /*-------------- FOC Asyntask ------------*/
                new FOC_list_asyntask(Constants.FOC_LIST_URL1 + "?AtmId=" + ATM_ID, "GET", ListActivity.this, new APIListner() {
                    @Override
                    public void onSuccess() {
                        //  Foc_NO.add("--select--");
                        FOCListAdapter.setFoc_NO(Foc_NO,foc_list_models);
                        FOCListAdapter.notifyDataSetChanged();
                    }
                    @Override
                    public void onErrors() {

                    }
                }).execute();






                recyclerView.setVisibility(View.VISIBLE);
                charge_rv.setVisibility(View.GONE);
                charge_layout.setVisibility(View.GONE);
                // charge_btn.setVisibility(View.GONE);
                foc_btn.setEnabled(false);
                submit_button_foc.setVisibility(View.VISIBLE);
                charge_btn.setEnabled(false);
                charge_btn.setBackgroundColor(getResources().getColor(R.color.red_light));


            }
        });



        charge_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //     + "?AtmId=" + ATM_ID

                new Charge_list_asyntask(Constants.CHARGE_LIST_URL1  + "?AtmId=" + ATM_ID, "GET" , ListActivity.this, new APIListner() {
                    @Override
                    public void onSuccess() {
                        //  Foc_NO.add("--select--");
                        if(charge_listAdapter == null){
                        }else {
                            charge_listAdapter.setCharge_NO(Charge_NO, charge_list_models);
                            charge_listAdapter.notifyDataSetChanged();
                        }
                    }
                    @Override
                    public void onErrors() {
                    }
                }).execute();

                recyclerView.setVisibility(View.GONE);

                ////////////////////////////////////////////////////////////////////////////////////////////

                //   charge_rv.setVisibility(View.VISIBLE);
                //   submit_button_charge.setVisibility(View.VISIBLE);// validation part


                //  foc_btn.setVisibility(View.GONE);
                submit_button_foc.setVisibility(View.GONE);
                charge_layout.setVisibility(View.VISIBLE);
                // submit_button_charge.setVisibility(View.VISIBLE);
                charge_btn.setVisibility(View.VISIBLE);
                charge_btn.setEnabled(false);

                foc_btn.setEnabled(false);
                foc_btn.setBackgroundColor(getResources().getColor(R.color.red_light));

                //To show at least one row
                if(charge_list == null || charge_list.size() == 0) {
                    charge_list = new ArrayList<>();
                    charge_list.add(new ModelClassForSavedData_Charge("","",""));
                }
                //Setting the adapter
                charge_listAdapter = new ChargeListAdapter(charge_list, ListActivity.this);
                llm1 = new LinearLayoutManager(ListActivity.this);
                charge_rv.setAdapter(charge_listAdapter);
                charge_rv.setLayoutManager(llm1);

            }
        });

        fcr_doc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showFileChooser();
            }
        });

        voltage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooser_valtage();

            }
        });



        submit_button_foc.setOnClickListener(new View.OnClickListener() {


            //  JSONArray jsonArrayfor_foc = new JSONArray();

            EditText editText = null;
            Spinner spinnervalues = null;
            FOC_ListAdapter.ViewHolder viewHolder = null;

            @Override
            public void onClick(View v) {

                //finalObject_foc = new JSONObject();
                jsonArrayfor_foc = new JSONArray();



                for (int i = 0; i < FOCListAdapter.getList_foc().size(); i++) {

                    if (FOCListAdapter.getList_foc().get(i).getQty().equalsIgnoreCase("")){

                        Toast.makeText(ListActivity.this, "FOC qty is empty", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(FOCListAdapter.getList_foc().get(i).getDescription().equalsIgnoreCase("")){
                        Toast.makeText(ListActivity.this, "Description is empty", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }


                if (FOCListAdapter == null) {
                    Toast.makeText(ListActivity.this, "FOC item is empty", Toast.LENGTH_SHORT).show();

                } else {
                    for(ModelClassForSavedData item:FOCListAdapter.getList_foc()) {
                    /*for (int i = 0; i < FOCListAdapter.getItemCount(); i++) {
                        viewHolder = (FOC_ListAdapter.ViewHolder) recyclerView.findViewHolderForAdapterPosition(i);
                        editText = viewHolder.step;
                        spinnervalues = viewHolder.spinner;
                        TextView dis = viewHolder.discription;*/
                        JSONObject jsonObject = new JSONObject();

                        try {
                            jsonObject.put("DocketNo_", DOCKET_NO);

                            jsonObject.put("No_", item.getSelectedItem());
                            jsonObject.put("Name", item.getDescription());
                            jsonObject.put("Quantity", item.getQty());
                            jsonObject.put("RequestType", "FOC");
                            jsonObject.put("SubStatus", subStatusId);
                            jsonObject.put("EngineerId",Prefs.with(ListActivity.this).getString(UserId, ""));





                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        jsonArrayfor_foc.put(jsonObject);

                    }


/*

                        try {
                            finalObject_foc.put("ListFocPartRequestModel", jsonArrayfor_foc);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Log.d("", "ListFocPartRequestModel123456: " + finalObject_foc);
*/

                }




                Intent i = new Intent();
                foc_list = FOCListAdapter.getStepList();
                i.putExtra("list", foc_list);
                i.putExtra("key", "foc");
                i.putExtra("jsonArrayfor_foc" ,jsonArrayfor_foc.toString());
                setResult(LIST_RESULT, i);
                finish();


                Toast.makeText(ListActivity.this, "Saved FOC Part", Toast.LENGTH_SHORT).show();


            }
        });





        /*============  SUBMIT CHARGE BUTTON ========*/

        submit_button_charge.setOnClickListener(new View.OnClickListener() {


            EditText editText1 = null;
            Spinner spinnervalues1 = null;
            ChargeListAdapter.ViewHolder viewHolder1 = null;

            @Override
            public void onClick(View v) {


                // Toast.makeText(ListActivity.this, "submit_button_charge", Toast.LENGTH_SHORT).show();
                jsonArrayfor_charge = new JSONArray();
                jsonArray_for_images = new JSONArray();

                for (int i = 0; i < charge_listAdapter.getList_foc().size(); i++) {

                    if (charge_listAdapter.getList_foc().get(i).getQty().equalsIgnoreCase("")){

                        Toast.makeText(ListActivity.this, "CHARGE qty is empty", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (charge_listAdapter.getList_foc().get(i).getDescription().equalsIgnoreCase("")){

                        Toast.makeText(ListActivity.this, "Description is empty", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }



                jsonArray_for_images.put(jsonObject_fcr_images);
                jsonArray_for_images.put(jsonObj_valtage_images);
                Log.d("images : ",jsonArray_for_images.toString());

                try {
                    json_obj_for_image.put("Images",jsonArray_for_images);

                    // Log.d(TAG, "onClick_json_obj_for_image: "+ json_obj_for_image);

                } catch (JSONException e) {

                    e.printStackTrace();
                }


                ///////////////////////////////////////////////////////////////////////////////////

/*

                if((jsonObject_fcr_images == null)) {

                    Toast.makeText(ListActivity.this, "Please Upload FCR image", Toast.LENGTH_SHORT).show();

                }else if((jsonObj_valtage_images == null)) {

                    Toast.makeText(ListActivity.this, "Please Upload Voltage image", Toast.LENGTH_SHORT).show();

                } else {
*/



                if (charge_listAdapter == null) {

                    Toast.makeText(ListActivity.this, "Charge item is empty", Toast.LENGTH_SHORT).show();

                } else {
                    for(ModelClassForSavedData_Charge item:charge_listAdapter.getList_foc()) {

             /*       for (int ii = 0; ii < charge_listAdapter.getItemCount(); ii++) {
                        viewHolder1 = (ChargeListAdapter.ViewHolder)
                                charge_rv.findViewHolderForAdapterPosition(ii);
                        editText1 = viewHolder1.et_qty;
                        spinnervalues1 = viewHolder1.spinner;

                        TextView dis = viewHolder1.discription;*/

                        JSONObject jsonObject1 = new JSONObject();

                        try {
                            jsonObject1.put("ItemNumber", item.selectedItem);
                            jsonObject1.put("ItemName", item.getDescription());
                            jsonObject1.put("Qty", item.getQty());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        jsonArrayfor_charge.put(jsonObject1);


                        //  Log.d("", "jsonArrayfor_charge: " + jsonArrayfor_charge);
                    }
                }



                Intent i = new Intent();
                charge_list = charge_listAdapter.getStepList();
                i.putExtra("list1", charge_list);
                i.putExtra("key", "charge");
                //  i.putExtra("jsonArray_for_images",jsonArray_for_images.toString());
                i.putExtra("jsonArrayfor_charge",jsonArrayfor_charge.toString());
                setResult(LIST_RESULT, i);
                finish();

                Toast.makeText(ListActivity.this, "Saved CHGC Part", Toast.LENGTH_SHORT).show();



            }
            //////////////////////////////////// // }
        });

    }







    private void showFileChooser() {
        // Intent intent = new Intent();
        //sets the select file to all types of files

        //   intent.setType("image/*");;
        // intent.setAction(Intent.ACTION_GET_CONTENT);
        //   startActivityForResult(Intent.createChooser(intent, "Choose File to Upload.."), PICK_FILE_REQUEST);


        /*Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_FILE_REQUEST);*/

        Intent intent = new Intent();
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Choose File to Upload.."), PICK_FILE_REQUEST);




    }

    private void showFileChooser_valtage() {

        // Intent intent = new Intent();
        //sets the select file to all types of files
        // intent.setType("image/*");
        // intent.setAction(Intent.ACTION_GET_CONTENT);
        // intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        // startActivityForResult(Intent.createChooser(intent, "Choose File to Upload.."), PICK_FILE_REQUEST1);

       /* Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_FILE_REQUEST1);
*/

        Intent intent = new Intent();
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Choose File to Upload.."), PICK_FILE_REQUEST1);


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PICK_FILE_REQUEST) {
                if (data == null) {
                    //no data present
                    return;
                }

                PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
                wakeLock = powerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, TAG);
                wakeLock.acquire();
                String image_name= null;
                Uri selectedFileUri = data.getData();


            /*    Bitmap bitmap = null;
                try {

                    Bitmap selectedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedFileUri);
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    selectedImage.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
                    byte[] byteArray = byteArrayOutputStream .toByteArray();
                    //  ivProductImage.setImageBitmap(selectedImage);
                    FCRIMAGE_BASE64 = Base64.encodeToString(byteArray, Base64.DEFAULT);



                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }*/






                try {


                    selectedFilePath_fcr = FilePath.getPath(this, selectedFileUri);
                    image_name = selectedFilePath_fcr.substring(selectedFilePath_fcr.lastIndexOf("/"), selectedFilePath_fcr.length());




                    File imagefile = new File(selectedFilePath_fcr);

                    int size = (int) imagefile.length();
                    byte[] bytes = new byte[size];
                    try {
                        BufferedInputStream buf = new BufferedInputStream(new FileInputStream(imagefile));
                        buf.read(bytes, 0, bytes.length);
                        buf.close();
                    } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    FCRIMAGE_BASE64 = Base64.encodeToString(bytes, Base64.DEFAULT);




                    Log.d("", "onActivityResult123: "+FCRIMAGE_BASE64);


                }catch (Exception e){

                }

                // selectedFilePath_fcr = FilePath.getPath(this, selectedFileUri);



                // Log.d(TAG, "onActivityResult_stringYouWant: "+image_name);

                Log.i("", "Selected File Path FCR:" + selectedFilePath_fcr);

                if (selectedFilePath_fcr != null && !selectedFilePath_fcr.equals("")) {
                    fcr_file.setText(image_name);





                    // save fcr path
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString(fcr_path, image_name);
                    editor.commit();


                    jsonObject_fcr_images = new JSONObject();
                    try {
                        jsonObject_fcr_images.put("File", FCRIMAGE_BASE64);// selectedFilePath_fcr
                        jsonObject_fcr_images.put("FileName", image_name);
                        jsonObject_fcr_images.put("Comments", "FCR Attachment");
                        jsonObject_fcr_images.put("DocketNo", DOCKET_NO);


                        Log.d(TAG, "jsonObject_for_fcr_images: "+ jsonObject_fcr_images);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    //Toast.makeText(this, ""+selectedFilePath_fcr, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Cannot upload file to server", Toast.LENGTH_SHORT).show();
                }
            }


            if (requestCode == PICK_FILE_REQUEST1) {
                if (data == null) {
                    //no data present
                    return;
                }

                PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
                wakeLock = powerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, TAG);
                wakeLock.acquire();
                String image_name1 = null;

                Uri selectedFileUri = data.getData();
          /*      Bitmap bitmap = null;
                try {

                    Bitmap selectedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedFileUri);
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    selectedImage.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
                    byte[] byteArray = byteArrayOutputStream .toByteArray();
                    //  ivProductImage.setImageBitmap(selectedImage);
                    VOLTAGE_IMAGE_BASE64 = Base64.encodeToString(byteArray, Base64.DEFAULT);



                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }*/

                try {
                    selectedFilePath_valtage = FilePath.getPath(this, selectedFileUri);
                    image_name1 = selectedFilePath_valtage.substring(selectedFilePath_valtage.lastIndexOf("/"), selectedFilePath_valtage.length());

                    File imagefile1 = new File(selectedFilePath_valtage);

                    int size = (int) imagefile1.length();
                    byte[] bytes = new byte[size];
                    try {
                        BufferedInputStream buf = new BufferedInputStream(new FileInputStream(imagefile1));
                        buf.read(bytes, 0, bytes.length);
                        buf.close();
                    } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    VOLTAGE_IMAGE_BASE64 = Base64.encodeToString(bytes, Base64.DEFAULT);




                    Log.d("", "onActivityResult123123444444: "+VOLTAGE_IMAGE_BASE64);

                }catch (Exception e){

                }
                Log.i("", "Selected File Name Voltage:" + selectedFilePath_valtage);

                if (selectedFilePath_valtage != null && !selectedFilePath_valtage.equals("")) {
                    valtage_file.setText(image_name1);

                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString(voltage_img_path, image_name1);
                    editor.commit();


                    jsonObj_valtage_images = new JSONObject();
                    try {
                        jsonObj_valtage_images.put("File", VOLTAGE_IMAGE_BASE64);  // selectedFilePath_valtage
                        jsonObj_valtage_images.put("FileName", image_name1);
                        jsonObj_valtage_images.put("Comments", "FCR Attachment");
                        jsonObj_valtage_images.put("DocketNo", DOCKET_NO);

                        Log.d(TAG, "Valtage_images: "+ jsonObj_valtage_images);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else {
                    Toast.makeText(this, "Cannot upload file to server", Toast.LENGTH_SHORT).show();
                }
            }

        }
        // show list when fcr and valtage image is not empty

        if ((selectedFilePath_valtage != null) & (selectedFilePath_fcr != null)) {
            charge_rv.setVisibility(View.VISIBLE);
            submit_button_charge.setVisibility(View.VISIBLE);

        }else {

        }







    }











    //Requesting permission
    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }


    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }


    private class FOC_list_asyntask extends CallManagerAsyncTaskArray {
        JSONArray postParamData = new JSONArray();
        APIListner apiListner;

        public FOC_list_asyntask(String action, String reqType, Context context, APIListner apiListner) {
            super(action, reqType, context);
            this.apiListner= apiListner;
        }

        @Override
        protected JSONArray doInBackground(Object... params) {

            try {

                return doWorkJSONArray(postParamData);
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

            progDailog = new ProgressDialog(ListActivity.this);
            progDailog.setMessage("Loading...");
            progDailog.setIndeterminate(false);
            progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progDailog.setCancelable(true);
            progDailog.show();

        }

        @Override
        protected void onPostExecute(JSONArray json) {
            super.onPostExecute(json);
            if (json != null) {
                Utils.Log("JSON Response=========" + json.toString());
                try {

                    for (int i = 0; i < json.length(); i++) {
                        Foc_list_Model spinnerModel = new Foc_list_Model();
                        JSONObject dataobj = json.getJSONObject(i);

                        spinnerModel.setFoc_item(dataobj.getString("No_"));
                        spinnerModel.setDiscription(dataobj.getString("Description"));

                        foc_list_models.add(spinnerModel);

                    }
                    Foc_list_Model modelSelect =new Foc_list_Model();
                    modelSelect.setFoc_item("-Select Item-");
                    modelSelect.setDiscription("");
                    foc_list_models.add(0,modelSelect);
                    //  Foc_NO.add("--Select--");
                    for (int i = 0; i < foc_list_models.size(); i++) {
                        Foc_NO.add(foc_list_models.get(i).getFoc_item());
                        Utils.Log("FocNooo=========" + Foc_NO);


                    }

                    apiListner.onSuccess();
               /*     dataAdapter = new ArrayAdapter<String>(context, R.layout.simple_spinner_dropdown_item, Foc_NO);
                    dataAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);*/

                    progDailog.dismiss();


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                // Utils.showAlertBox("Something Went wrong!!",context );
                progDailog.dismiss();
                Toast.makeText(ListActivity.this, "Something Went wrong!!", Toast.LENGTH_SHORT).show();
                recyclerView.setVisibility(View.GONE);
                NodataText.setText("There is no FOC part for this ATM "+ATM_ID);
            }
        }

    }












    private class Charge_list_asyntask extends CallManagerAsyncTaskArray {
        JSONArray postParamData = new JSONArray();
        APIListner apiListner;

        public Charge_list_asyntask(String action, String reqType, Context context, APIListner apiListner) {
            super(action, reqType, context);
            this.apiListner= apiListner;
        }

        @Override
        protected JSONArray doInBackground(Object... params) {

            try {

                return doWorkJSONArray(postParamData);
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

            progDailog = new ProgressDialog(ListActivity.this);
            progDailog.setMessage("Loading...");
            progDailog.setIndeterminate(false);
            progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progDailog.setCancelable(true);
            progDailog.show();
        }

        @Override
        protected void onPostExecute(JSONArray json) {
            super.onPostExecute(json);
            if (json != null) {
                Utils.Log("JSON Response121222222=========" + json.toString());
                try {

                    for (int i = 0; i < json.length(); i++) {
                        Charge_list_Model charge_list_model = new Charge_list_Model();
                        JSONObject dataobj = json.getJSONObject(i);

                        charge_list_model.setFoc_item(dataobj.getString("No_"));
                        charge_list_model.setDiscription(dataobj.getString("Description"));

                        charge_list_models.add(charge_list_model);

                    }
                    Charge_list_Model modelSelect =new Charge_list_Model();
                    modelSelect.setFoc_item("-Select Item-");
                    modelSelect.setDiscription("");
                    charge_list_models.add(0,modelSelect);
                    //  Charge_NO.add("--Select--");
                    for (int i = 0; i < charge_list_models.size(); i++) {
                        Charge_NO.add(charge_list_models.get(i).getFoc_item());
                        Utils.Log("1122212" + Charge_NO);


                    }

                    apiListner.onSuccess();
               /*     dataAdapter = new ArrayAdapter<String>(context, R.layout.simple_spinner_dropdown_item, Foc_NO);
                    dataAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);*/

                    progDailog.dismiss();


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                // Utils.showAlertBox("Something Went wrong!!",context );
                progDailog.dismiss();
                charge_rv.setVisibility(View.GONE);
                charge_layout.setVisibility(View.GONE);
                submit_button_charge.setVisibility(View.GONE);
                Toast.makeText(ListActivity.this, "Something Went wrong!!", Toast.LENGTH_SHORT).show();
                NodataText.setText("There is no Chargable part for this ATM "+ATM_ID);
            }
        }

    }



}
