package com.cms.callmanager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.cms.callmanager.constants.Constant;
import com.cms.callmanager.constants.Constants;
import com.cms.callmanager.dto.UserDTO;
import com.cms.callmanager.services.CallManagerAsyncTask;
import com.cms.callmanager.utils.ProgressUtil;
import com.cms.callmanager.utils.Utils;
import com.cms.callmanager.utils.Validation;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.EOFException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.SocketTimeoutException;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static com.cms.callmanager.constants.Constant.Pass;
import static com.cms.callmanager.constants.Constant.UserId;

public class LoginActivity extends AppCompatActivity {

    EditText name;
    EditText passwordTxt;

    TextInputLayout txtUserNameLayout;
    TextInputLayout txtPwdLayout;

    Button loginBtn;
    String[] perms = {"android.permission.FINE_LOCATION"};
    SharedPreferences preferences;
    int permsRequestCode = 200;
    private static final int PERMISSION_REQUEST_CODE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ActionBar actionBar = getSupportActionBar();
        if(!checkPermission())
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(perms, permsRequestCode);
            }
        actionBar.hide();

        initUI();
        /*name.setText("9606300");
        passwordTxt.setText("1234");*/
        loginBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (Utils.isInternetOn(LoginActivity.this)) {
                    if (!Validation.validateField(name, txtUserNameLayout, LoginActivity.this,
                            getString(R.string.InvalidName))) {
                        return;
                    } else if (!Validation.validatePassword(passwordTxt, txtPwdLayout, LoginActivity.this)) {
                        return;
                    } else {
                        ProgressUtil.showProgressBar(LoginActivity.this, findViewById(R.id.root), R.id.progressBar);
                        login();
                    }
                } else {
                    Utils.showAlertBox(getString(R.string.network_error), LoginActivity.this);
                }
            }
        });



    }

    private boolean checkPermission() {

        int result = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION);
        //  int result1 = ContextCompat.checkSelfPermission(getApplicationContext());

        return result == PackageManager.PERMISSION_GRANTED;


    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {

                    boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
            //        boolean cameraAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    //   if (locationAccepted && cameraAccepted)
                    //       Snackbar.make(view, "Permission Granted, Now you can access location data and camera.", Snackbar.LENGTH_LONG).show();
                    //   else {

                    //    Snackbar.make(view, "Permission Denied, You cannot access location data and camera.", Snackbar.LENGTH_LONG).show();
//
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION)) {
                            showMessageOKCancel("You need to allow access to both the permissions",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions(new String[]{ACCESS_FINE_LOCATION},
                                                        PERMISSION_REQUEST_CODE);
                                            }
                                        }
                                    });
                            return;
                        }
                    }

                }
                break;
        }
        }



    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(LoginActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }



    private void initUI() {

        loginBtn    = (Button) findViewById(R.id.signIn);
        name        = (EditText) findViewById(R.id.edtTxtUserName);
        passwordTxt = (EditText) findViewById(R.id.edTxtPassword);

        /*name.setText(Prefs.with(this).getString(UserId, ""));
        passwordTxt.setText(Prefs.with(this).getString(Pass, ""));*/


        preferences = getSharedPreferences("CMS", Context.MODE_PRIVATE);

        txtUserNameLayout = (TextInputLayout) findViewById(R.id.txtUserNameLayout);
        txtPwdLayout  = (TextInputLayout) findViewById(R.id.txtPwdLayout);




        name.addTextChangedListener(new MyTextWatcher(name));
        passwordTxt.addTextChangedListener(new MyTextWatcher(passwordTxt));


    }

    public void login(){
        String userName = name.getText().toString();
        String password = passwordTxt.getText().toString();

        UserAsyncTask userAsyncTask = null;
        userAsyncTask = new UserAsyncTask(Constants.LOGIN, "POST", userName, password, LoginActivity.this );
        userAsyncTask.execute();

    }

    public class UserAsyncTask extends CallManagerAsyncTask {
        InputStream responseXML = null;
        String userName;
        String password;


        public UserAsyncTask(String action, String reqType, String userName, String password, Context context){
            super(action, reqType, context);
            this.userName = userName;
            this.password = password;
        }

        @Override
        protected JSONObject doInBackground(Object... params) {

            JSONObject postParamData = new JSONObject();
            try {
                postParamData.put("User_ID",userName);
                postParamData.put("Password",password);
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
                        Utils.Log("response===="+json.toString());
                            Utils.Log(json.getJSONArray("PayLoad").get(0).toString());
                            JSONObject user = (JSONObject) json.getJSONArray("PayLoad").get(0);
                            UserDTO userDTO = new UserDTO();
                            if(user.getString("UserId") != null && user.getString("UserId") != ""){
                                userDTO.setUserId(user.getString("UserId"));
                            }
                            if(user.getString("UserName") != null && user.getString("UserName") != ""){
                                userDTO.setFirstName(user.getString("UserName"));
                            }
                        if(user.getString("IsTl") != null){
                            userDTO.setTLFlag(Boolean.parseBoolean(user.getString("IsTl")));
                        }
                        preferences.edit().putInt("IsTLFlag", Integer.parseInt(user.getString("IsTl"))).commit();
                        preferences.edit().putString("UserName", user.getString("FullName").toString()).commit();

                        preferences.edit().putString("userId" , user.getString("UserId").toString()).commit();


                        Prefs.with(LoginActivity.this).save(UserId, userName.toString());
                        Prefs.with(LoginActivity.this).save(Pass, password.toString());


                        Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                            startActivity(intent);
                            finish();
                    }else if(json.has("Status") && json.get("Status").toString().equalsIgnoreCase("Failure")) {
                        Utils.showAlertBox(json.getString("ErrorMessage"),LoginActivity.this);
                    }else {
                        Utils.showAlertBox(json.getString("ErrorMessage"),LoginActivity.this);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else {
                try {
                    Utils.showAlertBox(json.getString("ErrorMessage"),LoginActivity.this);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }


	private class MyTextWatcher implements TextWatcher {

		private View view;

		private MyTextWatcher(View view) {
			this.view = view;
		}

		public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
		}

		public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

		}

		public void afterTextChanged(Editable editable) {
			switch (view.getId()) {

				case  R.id.edtTxtUserName:
					Validation.validateField(name, txtUserNameLayout, LoginActivity.this , getString(R.string.InvalidName));
					break;
				case  R.id.edTxtPassword:
					Validation.validateField(passwordTxt, txtPwdLayout, LoginActivity.this , getString(R.string.InvalidPassword));
					break;
			}
		}
	}
}
