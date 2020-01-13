package com.cms.callmanager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.SocketTimeoutException;

import com.cms.callmanager.constants.Constants;
import com.cms.callmanager.dto.UserDTO;
import com.cms.callmanager.services.CallManagerAsyncTask;
import com.cms.callmanager.utils.ProgressUtil;
import com.cms.callmanager.utils.Utils;
import com.theartofdev.edmodo.cropper.CropImage;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import static com.cms.callmanager.utils.ProgressUtil.progressDialog;

/**
 * Created by monika on 8/1/17.
 */
public class AttachImageActivity extends AppCompatActivity {
    private static final String AUTHORITY = "Allow";
    Button upload,save;
    ImageView uploadImage;
    File outPutFile;
    private Uri mImageCaptureUri;
    private boolean flagImage;
    String requestXML;
    Bitmap photoUpload;
    int currentapiVersion;
    int PICK_IMAGE_PERMISSIONS_REQUEST_CODE=1,REQUEST_CAMERA=2;
    private static final int CAMERA_CODE = 1101, GALLERY_CODE = 2201, CROPING_CODE = 3301;
    EditText edtTxtTicketNumber,edtTxtComment;
    String docketNo;
    String userId;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attach_image);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.action_back);
        upload = (Button) findViewById(R.id.upload);
        save   = (Button) findViewById(R.id.save);
        currentapiVersion = android.os.Build.VERSION.SDK_INT;
        uploadImage = (ImageView) findViewById(R.id.imageView);
        edtTxtTicketNumber = (EditText) findViewById(R.id.edtTxtTicketNumber);
        edtTxtComment = (EditText) findViewById(R.id.edtTxtComment);
        preferences = getSharedPreferences("CMS", Context.MODE_PRIVATE);

        ProgressDialog progressDoalog;
        progressDialog = new ProgressDialog(AttachImageActivity.this);

        if(getIntent()!=null && getIntent().getStringExtra("docketNo")!=null){
            edtTxtTicketNumber.setText(getIntent().getStringExtra("docketNo"));
            docketNo = getIntent().getStringExtra("docketNo");
            edtTxtTicketNumber.setEnabled(false);
        }

        save.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {


                save.setEnabled(false);



                progressDialog.setMessage("Loading..."); // Setting Message
                progressDialog.setTitle("CMS"); // Setting Title
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
                progressDialog.show(); // Display Progress Dialog
                progressDialog.setCancelable(false);


                 userId = preferences.getString("userId" , null);
                String comment ="";
                String uploadedFile = "";
                if(edtTxtComment.getText() != null){
                    comment = String.valueOf(edtTxtComment.getText());
                }else {
                    comment = " ";
                }
                File imgFile = new File(String.valueOf(outPutFile));
                if (imgFile.exists() && imgFile.length() > 0) {
                    Bitmap bm = BitmapFactory.decodeFile(String.valueOf(imgFile));
                    ByteArrayOutputStream bOut = new ByteArrayOutputStream();
                    bm.compress(Bitmap.CompressFormat.JPEG, 50, bOut);
                    uploadedFile = Base64.encodeToString(bOut.toByteArray(), Base64.DEFAULT);


                    Log.d("", "onClick_uploadedFile: "+uploadedFile);



                    if (Utils.isInternetOn(AttachImageActivity.this)) {
                        UploadFileAsyncTask uploadFileAsyncTask = new UploadFileAsyncTask(Constants.FILE_ATTACHMENT + "?userId=" + userId, "POST", comment, uploadedFile, AttachImageActivity.this);
                        uploadFileAsyncTask.execute();
                        ProgressUtil.showProgressBar(AttachImageActivity.this,
                                findViewById(R.id.root), R.id.progressBar);
                    } else {
                        Utils.showAlertBox(getString(R.string.network_error), AttachImageActivity.this);
                    }
                }else {
                    Utils.showAlertBox("Please select/capture image.",AttachImageActivity.this);
                }


            }
        });
        upload.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
            choosePicture();

            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(AttachImageActivity.this,HomeActivity.class);
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

    private void getExternalStoragePermission(Uri imageUri){
        MarshMallowPermission marshMallowPermission = new MarshMallowPermission(AttachImageActivity.this);
        if (marshMallowPermission.isReadExternalStoragePermissionsRequired(this, imageUri)) {
            mImageCaptureUri = imageUri;
            requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, PICK_IMAGE_PERMISSIONS_REQUEST_CODE);
        } else {
            mImageCaptureUri = imageUri;
            CropingIMG();
        }
    }
    private void getCameraPermission(){

        MarshMallowPermission marshMallowPermission = new MarshMallowPermission(AttachImageActivity.this);
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
                File f = new File(android.os.Environment.getExternalStorageDirectory(), "winspireImageFile.jpg");
                mImageCaptureUri = FileProvider.getUriForFile(AttachImageActivity.this,
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
                            ActivityCompat.requestPermissions(AttachImageActivity.this,
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
    private void CropingIMG() {
        CropImage.activity(mImageCaptureUri)
                .start(AttachImageActivity.this);
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
                    CropImage.ActivityResult result = CropImage.getActivityResult(data);
                    Uri resultUri = result.getUri();
                    outPutFile =new File(resultUri.getPath());
                    photoUpload = decodeFile(outPutFile);
                    upload.setText("Change Image");
                    uploadImage.setImageBitmap(photoUpload);
                    uploadImage.setVisibility(View.VISIBLE);
                }
                else {
                    Toast.makeText(getApplicationContext(), "Error while save image", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private void choosePicture() {

        //final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
        final CharSequence[] options = { "Take Photo","Cancel" };


        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(AttachImageActivity.this);
        builder.setItems(options, new DialogInterface.OnClickListener() {

            @SuppressLint("SdCardPath")
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    dispatchTakePictureIntent();
                }
               /*  else if (options[item].equals("Choose from Gallery")) {
                    takePictureFromGallery();
                }*/ else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }

        });
        builder.show();
    }

    private void takePictureFromGallery() {
        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, GALLERY_CODE);
    }

    protected void dispatchTakePictureIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File f = new File(android.os.Environment.getExternalStorageDirectory(), "winspireImageFile.jpg");
      //  mImageCaptureUri = Uri.fromFile(f);
        mImageCaptureUri = FileProvider.getUriForFile(AttachImageActivity.this,
                BuildConfig.APPLICATION_ID + ".provider",f);

        if (currentapiVersion >= Build.VERSION_CODES.M) {
            getCameraPermission();
        } else {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
            startActivityForResult(intent, CAMERA_CODE);
        }

    }
    public class UploadFileAsyncTask extends CallManagerAsyncTask {

        String comment,uploadedFile;
        public UploadFileAsyncTask(String action, String reqType, String comment, String uploadedFile, Context context){
            super(action, reqType, context);
            this.comment = comment;
            this.uploadedFile = uploadedFile;

        }

        @Override
        protected JSONObject doInBackground(Object... params) {

            JSONObject postParamData = new JSONObject();
            try {
                postParamData.put("DocketNo",docketNo);
                postParamData.put("File",uploadedFile);
                postParamData.put("FileName",userId+String.valueOf(System.currentTimeMillis())+".jpg");
              //  postParamData.put("FileName","");

                postParamData.put("Comments",comment);
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
            ProgressUtil.showProgressBar(AttachImageActivity.this,
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
                        progressDialog.dismiss();
                        Toast.makeText(AttachImageActivity.this, json.getString("ErrorMessage").toString(), Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(AttachImageActivity.this, HomeActivity.class);
                        //i.putExtra("docketNo",docketNo);
                        startActivity(i);
                        finish();
                    }else if(json.has("Status") && json.get("Status").toString().equalsIgnoreCase("Failure")) {
                        Utils.showAlertBox(json.getString("ErrorMessage").toString(),AttachImageActivity.this);
                    }else {
                        Utils.showAlertBox(json.getString("ErrorMessage").toString(),AttachImageActivity.this);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else {
                Utils.showAlertBox("Something Went wrong!!",AttachImageActivity.this);
                Intent i = new Intent(AttachImageActivity.this,AttachmentViewActivity.class);
                startActivity(i);
            }
        }
    }
}
