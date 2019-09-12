package com.cms.callmanager.utils;


import android.app.Activity;
import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.cms.callmanager.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Validation{


	public static boolean validateField(EditText inputNameFirst, TextInputLayout inputLayoutNameFirst,
										Activity activity , String message) {

		String name = inputNameFirst.getText().toString().trim();
		String whiteSpacePattern = "(?=\\\\S+$)";
		if (name.isEmpty()) {
			inputLayoutNameFirst.setError(message);
			requestFocus(inputNameFirst , activity);
			return false;
		}else if(name.matches(whiteSpacePattern)){
			inputLayoutNameFirst.setError("Whitespace is not allowed.");
			return false;
		}else{
			inputLayoutNameFirst.setError(null);
		}

		return true;
	}

	public static void requestFocus(View view , Activity activity) {
		view.requestFocus();
		InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
	}

	public static boolean validatePassword(EditText inputPassword , TextInputLayout inputLayoutPassword , Activity activity) {
		String pwdPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
		String whiteSpacePattern = "(?=\\\\S+$)";
		if (inputPassword.getText().toString().trim().isEmpty()) {
			inputLayoutPassword.setError(activity.getString(R.string.err_msg_password));
			requestFocus(inputPassword , activity);
			return false;
		} if (inputPassword.getText().toString().trim().length() < 4) {
			inputLayoutPassword.setError(activity.getString(R.string.err_msg_password8dig));
			requestFocus(inputPassword , activity);
			return false;
		}else if(inputPassword.getText().toString().trim().matches(whiteSpacePattern)){
			inputLayoutPassword.setError("Whitespace is not allowed.");
			return false;
		} else {
			inputLayoutPassword.setError(null);
		}

		return true;
	}

	public static boolean dateValidation(String startDate,String endDate,String dateFormat )
	{
		try
		{

			SimpleDateFormat df = new SimpleDateFormat(dateFormat);
			Date date1 = df.parse(endDate);
			Date startingDate = df.parse(startDate);

			if (date1.after(startingDate))
				return true;
			else
				return false;
		}
		catch (Exception e)
		{
			return false;
		}
	}

	public static boolean timeValidation(Date df)
	{
		try
		{
			Calendar c = Calendar.getInstance();
			Utils.Log("df.getTime() "+df.getTime() );
			Utils.Log("c.getTimeInMillis()"+c.getTimeInMillis());
			if (df.getTime() > c.getTimeInMillis())
				return false;
			else
				return true;
		}
		catch (Exception e)
		{
			return false;
		}
	}
}