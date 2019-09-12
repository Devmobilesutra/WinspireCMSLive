package com.cms.callmanager.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Base64;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Monika on 18/1/17.
 */
public class Utils {
    public static boolean LOG_DEBUG = true;

    public static void Log(String message){
        if(LOG_DEBUG){
            Log.d("CMS" , message);
        }
    }

    public static boolean isInternetOn(Context context) {
        ConnectivityManager conn = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conn.getActiveNetworkInfo();
        if (networkInfo != null) {
            return networkInfo.isConnectedOrConnecting();
        }
        return false;
    }

    public static void  SetDefautltFont(TextView view, String style , Context ctx){
        if(style == "Regular"){
            Typeface custom_font = Typeface.createFromAsset(ctx.getAssets(), "Roboto-Regular.ttf");
            view.setTypeface(custom_font);
        }else if (style == "Bold"){
            Typeface custom_font = Typeface.createFromAsset(ctx.getAssets(), "Roboto-Bold.ttf");
            view.setTypeface(custom_font);
        }else {
            Typeface custom_font = Typeface.createFromAsset(ctx.getAssets(), "Roboto-Thin.ttf");
            view.setTypeface(custom_font);
        }

    }


    public static long dateToMiliSeconds(String date){
        Date datea=null;
        String test[] =date.split("T");
        date = test[0] + " "+ test[1];

        try {
            String myDate = date;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SS");

            datea = sdf.parse(myDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //long millis = datea.getTime();
        return datea.getTime();
    }


    public static String DateFormater(String timeStr){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss" , Locale.ENGLISH);
        Date myDate = null;
        try {
            myDate = dateFormat.parse(timeStr);
			dateFormat.setTimeZone(TimeZone.getTimeZone("GMT") );

        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat timeFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        String finalDate = timeFormat.format(myDate);
        return finalDate;
    }

    public static void showAlertBox(String message , Activity activity){

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder.setTitle("CMS");
        builder.setMessage(message).setNegativeButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
            }
        });
        builder.create().show();
    }


    public static String convertToCurrentTimeZone(String date) {
        String converted_date = null;
        try {

            DateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date myDate = utcFormat.parse(date);
            DateFormat currentTFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            currentTFormat.setTimeZone(Calendar.getInstance().getTimeZone());
            converted_date =  currentTFormat.format(myDate);
        }catch (Exception e){
            e.printStackTrace();}
        return converted_date;
    }
    public static String dateToString(Date date){


        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = null;
        try {
            dateStr = dateFormat.format(date);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return dateStr;
    }

    public static String toTitleCase(String str){
        Utils.Log("string ============"+str);
        String[] strArray = str.split(" ");
        StringBuilder builder = new StringBuilder();
        for (String s : strArray) {
            String cap = s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
            builder.append(cap + " ");
        }

        Utils.Log("==============="+builder.toString());
        return builder.toString().trim();
    }

    public static boolean checkStringIsEmpty(String name) {

        if (name != null && !name.isEmpty() && !name.equals("null") && !name.equalsIgnoreCase("0001-01-01T00:00:00"))
            return true;
        else
            return false;

    }

    public static String ChangeDateFormat(String date) {

        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat output = new SimpleDateFormat("dd-MMM-yyyy");

        Date d = null;
        try
        {
            d = input.parse(date);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        String formatted = output.format(d);

        return formatted;
    }


    public static String ChangeDatePickerFormat(String date) {

        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat output = new SimpleDateFormat("dd-MMM-yyyy");

        Date d = null;
        try
        {
            d = input.parse(date);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        String formatted = output.format(d);

        return formatted;
    }


    public static String ChangeDateTimeFormat(String date) {

        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
        input.setTimeZone(TimeZone.getTimeZone("UTC"));
        SimpleDateFormat output = new SimpleDateFormat("dd-MMM-yyyy HH:mm");

        Date d = null;
        try
        {
            d = input.parse(date);
            output.setTimeZone(TimeZone.getDefault());
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        String formatted = output.format(d);

        return formatted;
    }

    public static String perfectDecimal(String str, int MAX_BEFORE_POINT, int MAX_DECIMAL){
        if(str.charAt(0) == '.') str = "0"+str;
        int max = str.length();

        String rFinal = "";
        boolean after = false;
        int i = 0, up = 0, decimal = 0; char t;
        while(i < max){
            t = str.charAt(i);
            if(t != '.' && after == false){
                up++;
                if(up > MAX_BEFORE_POINT) return rFinal;
            }else if(t == '.'){
                after = true;
            }else{
                decimal++;
                if(decimal > MAX_DECIMAL)
                    return rFinal;
            }
            rFinal = rFinal + t;
            i++;
        }return rFinal;
    }

    public static String checkString(String name) {
        if (name != null && !name.isEmpty() && !name.equalsIgnoreCase("null") && !name.equalsIgnoreCase("0001-01-01T00:00:00"))
            return name;
        else
            return "";
    }


    public static void disableEditText(EditText editText) {
        editText.setFocusable(false);
        editText.setEnabled(false);
        editText.setCursorVisible(false);
        editText.setKeyListener(null);
        editText.setHint("");

    }

    public static String checkQty(String name) {
        if (name != null && !name.isEmpty() && !name.equalsIgnoreCase("null"))
            return name;
        else
            return "0";
    }
}
