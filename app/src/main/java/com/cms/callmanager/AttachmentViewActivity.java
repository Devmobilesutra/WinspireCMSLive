package com.cms.callmanager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.Toast;

import com.cms.callmanager.constants.Constants;
import com.cms.callmanager.services.CallManagerAsyncTask;
import com.cms.callmanager.utils.ProgressUtil;
import com.cms.callmanager.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.EOFException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;

/**
 * Created by Monika on 8/12/17.
 */

public class AttachmentViewActivity extends AppCompatActivity {

    String docketNo,url;
    SharedPreferences preferences;
    ImageView attachmentView;
    Toolbar toolbar;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attachment_view);
        attachmentView = (ImageView) findViewById(R.id.attachmentView);
        toolbar       = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.action_back);
        if(getIntent()!=null && getIntent().getStringExtra("docketNo")!=null &&
                getIntent().getStringExtra("url")!=null){
            docketNo = getIntent().getStringExtra("docketNo");
            url = getIntent().getStringExtra("url");
        }
        if(url != null){
            preferences    = getSharedPreferences("CMS", Context.MODE_PRIVATE);
            String userId = preferences.getString("userId" , null);
            ProgressUtil.showProgressBar(AttachmentViewActivity.this,
                    findViewById(R.id.root), R.id.progressBar);
            new AttachementViewAsyncTask(Constants.VIEWATTACHMENT+"?userid="+userId,"POST",AttachmentViewActivity.this,url).execute();
        }else {
            Intent i=new Intent(AttachmentViewActivity.this,CallDetailsActivity.class);
            i.putExtra("docketNo",docketNo);
            startActivity(i);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i=new Intent(AttachmentViewActivity.this,CallDetailsActivity.class);
        i.putExtra("docketNo",docketNo);
        startActivity(i);
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

    private class AttachementViewAsyncTask extends CallManagerAsyncTask {

        JSONObject postParamData = new JSONObject();
        String fileURL;
        public AttachementViewAsyncTask(String action, String reqType, Context context
                , String url) {
            super(action, reqType, context);
            this.fileURL = url;
        }

        @Override
        protected JSONObject doInBackground(Object... params) {

            try {
                postParamData.put("FileName",url);
                Utils.Log("postParamData==="+postParamData.toString());
                return doWork(postParamData);
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
            ProgressUtil.hideProgressBar(findViewById(R.id.root), R.id.progressBar);
            if(json != null){
                Utils.Log("JSON Response========="+json.toString());
                try {
                    if(json.has("Status") && json.get("Status").toString().equalsIgnoreCase("Success")){
                        Utils.Log("======"+json.getJSONArray("PayLoad").get(0).toString());
                        String attachment =  json.getJSONArray("PayLoad").get(0).toString();
                        byte[] decodedString = Base64.decode(attachment, Base64.DEFAULT);
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        attachmentView.setImageBitmap(decodedByte);
                    }else if(json.has("Status") && json.get("Status").toString().equalsIgnoreCase("Failure")) {
                        Utils.showAlertBox(json.getString("ErrorMessage").toString(),AttachmentViewActivity.this );
                    }else {
                        Utils.showAlertBox(json.getString("ErrorMessage").toString(), AttachmentViewActivity.this);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else {
                Utils.showAlertBox("Something Went wrong!!", AttachmentViewActivity.this);
            }
        }

    }

}
