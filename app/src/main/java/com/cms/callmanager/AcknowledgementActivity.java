package com.cms.callmanager;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cms.callmanager.R;
import com.cms.callmanager.constants.Constants;
import com.cms.callmanager.services.CallManagerAsyncTask;
import com.cms.callmanager.utils.ProgressUtil;
import com.cms.callmanager.utils.Utils;
import com.github.gcacace.signaturepad.views.SignaturePad;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.SocketTimeoutException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by monika zogato on 8/1/17.
 */
public class AcknowledgementActivity extends AppCompatActivity implements View.OnClickListener {
    Button save_acknowledgement;
    SignaturePad mSignaturePad;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
    Button mClearButton;
    EditText edtTxtTicketNumber;
    String status;
    String docketNo;
    SharedPreferences preferences;
    String encodedSignature;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        verifyStoragePermissions(this);
        setContentView(R.layout.activity_acknowledgement);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.action_back);
        save_acknowledgement = (Button) findViewById(R.id.save_acknowledgement);
        edtTxtTicketNumber = (EditText) findViewById(R.id.edtTxtTicketNumber);
        save_acknowledgement.setOnClickListener(this);
        Utils.Log("CallDetails====="+docketNo);
        preferences = getSharedPreferences("CMS", Context.MODE_PRIVATE);
        if(getIntent()!=null  && getIntent().getStringExtra("docketNo")!=null) {
            docketNo = getIntent().getExtras().getString("docketNo");
            edtTxtTicketNumber.setText(docketNo);
            edtTxtTicketNumber.setEnabled(false);
        }

        mSignaturePad = (SignaturePad) findViewById(R.id.signature_pad);
        mClearButton = (Button) findViewById(R.id.clear_button);
        mClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSignaturePad.clear();
                status="";

            }
        });
        mSignaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {

            @Override
            public void onStartSigning() {
                //Event triggered when the pad is touched
            }

            @Override
            public void onSigned() {
                mClearButton.setEnabled(true);
                //Event triggered when the pad is signed
                Bitmap signatureBitmap = mSignaturePad.getSignatureBitmap();
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                signatureBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream .toByteArray();
                encodedSignature = Base64.encodeToString(byteArray, Base64.DEFAULT);

            }

            @Override
            public void onClear() {
                mClearButton.setEnabled(false);
                encodedSignature = "";
                //Event triggered when the pad is cleared
            }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.save_acknowledgement :
                    if(encodedSignature != null){
                        saveAcknowledgement();
                    }else {
                        Utils.showAlertBox("Please provide the acknowledgent." , AcknowledgementActivity.this);
                        encodedSignature = "";
                    }

        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(AcknowledgementActivity.this,HomeActivity.class);
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

    private void saveAcknowledgement(){
        String userId = preferences.getString("userId" , null);
        if (Utils.isInternetOn(AcknowledgementActivity.this)) {
            new AcknowledgeAsyncTask(Constants.ACK_FILE_ATTACHMENT+"?userId="+userId, "POST" , encodedSignature,AcknowledgementActivity.this ).execute();
            ProgressUtil.showProgressBar(AcknowledgementActivity.this,
                    findViewById(R.id.root), R.id.progressBar);
        }else {
            Utils.showAlertBox(getString(R.string.network_error), AcknowledgementActivity.this);
        }

       }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length <= 0
                        || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(AcknowledgementActivity.this, "Cannot write images to external storage", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public class AcknowledgeAsyncTask extends CallManagerAsyncTask {

        String signature;
        public AcknowledgeAsyncTask(String action, String reqType, String uploadedFile, Context context){
            super(action, reqType, context);
            this.signature = uploadedFile;

        }

        @Override
        protected JSONObject doInBackground(Object... params) {

            JSONObject postParamData = new JSONObject();
            try {
                postParamData.put("DocketNo",docketNo);
                postParamData.put("File",signature);
                //postParamData.put("FileName","acknowledgement.jpg");
                postParamData.put("Comments","acknowledgement");
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
            if(json != null){
                Utils.Log("JSON Response========="+json.toString());
                try {
                    if(json.has("Status") && json.get("Status").toString().equalsIgnoreCase("Success")){
                       Toast.makeText(AcknowledgementActivity.this,"Uploaded Succefully",Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(AcknowledgementActivity.this,HomeActivity.class);
                        startActivity(i);
                        finish();
                    }else if(json.has("Status") && json.get("Status").toString().equalsIgnoreCase("Failure")) {
                        Utils.showAlertBox(json.getString("ErrorMessage").toString(),AcknowledgementActivity.this);
                    }else {
                        Utils.showAlertBox(json.getString("ErrorMessage").toString(),AcknowledgementActivity.this);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else {
                Utils.showAlertBox("Something Went wrong!!",AcknowledgementActivity.this);
            }
        }
    }
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

}
