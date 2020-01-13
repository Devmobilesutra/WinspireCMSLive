package com.cms.callmanager.Foc_Chargeble;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import com.cms.callmanager.constants.Constants;
import com.cms.callmanager.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.URL;

/**
 * Created by Bhavesh Chaudhari on 21-May-19.
 */

public abstract class CallManagerAsyncTaskArray1 extends AsyncTask<Object, String, JSONArray> {

    HttpURLConnection urlConnection = null;
    URL url;
    String action;
    private static final int CONNECTION_TIMEOUT = 50000;
    private static final String AuthorizationKey = "Basic V0lOU1BJUkVBRE1JTjp3aW5zcGlyZUAxMjM=";

    static String reqType;
    public CallManagerAsyncTaskArray1(String action , String reqType , Context context){

        try {
            this.reqType = reqType;
            this.action  = action;
            url = new URL(Constants.Base_url1 + action);
            Log.d("Complete Url :" , url.toString());
            urlConnection = (HttpURLConnection) url.openConnection();
            if(reqType.equalsIgnoreCase("POST")){
                doPost();
            }else if(reqType.equalsIgnoreCase("GET")){
                doGet();
            }else if(reqType.equalsIgnoreCase("PUT")){
                doPut();
            }

        }catch(IOException e){
            e.printStackTrace();
        }
    }

    private void doGet() throws ProtocolException {
        urlConnection.setDoInput(true);
        urlConnection.setRequestMethod("GET");
        urlConnection.setConnectTimeout(CONNECTION_TIMEOUT);
        urlConnection.setRequestProperty("Content-length", "0");
        urlConnection.setUseCaches(false);
        urlConnection.setRequestProperty("Connection", "close");
        urlConnection.setRequestProperty("Authorization", AuthorizationKey);
    }

    private void doPost() throws ProtocolException {//access_token = sharedPreferences.getString("_id" , null);
        urlConnection.setRequestProperty("Content-Type", "application/json");
        urlConnection.setRequestMethod("POST");
        urlConnection.addRequestProperty("Cache-Control", "no-cache");
        urlConnection.setConnectTimeout(CONNECTION_TIMEOUT);
        urlConnection.setUseCaches(false);
        urlConnection.setRequestProperty("Authorization", AuthorizationKey);

        urlConnection.setDoOutput(true);

        Utils.Log("URL DATA==="+urlConnection.toString());
        if (Build.VERSION.SDK != null && Build.VERSION.SDK_INT < 13) {
            urlConnection.setRequestProperty("connection", "close");
        }
    }

    private void doPut() throws ProtocolException {
        urlConnection.setRequestProperty("Content-Type", "application/json");
        urlConnection.setRequestMethod("PUT");
        urlConnection.setConnectTimeout(CONNECTION_TIMEOUT);

        urlConnection.setUseCaches(false);
        urlConnection.setDoOutput(true);
        urlConnection.setRequestProperty("Authorization", AuthorizationKey);

        Utils.Log("URL DATA==="+urlConnection.toString());
        if (Build.VERSION.SDK != null && Build.VERSION.SDK_INT < 13) {
            urlConnection.setRequestProperty("connection", "close");
        }
    }


    protected JSONObject doWork(JSONObject inputJSONDATA) throws ConnectException, EOFException, SocketTimeoutException {

        JSONObject responseJSON = null;
        try {

            urlConnection.connect();
            if(reqType.equalsIgnoreCase("POST") || reqType.equalsIgnoreCase("PUT")){
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream()));
                System.out.println(" requesting string==================" + inputJSONDATA.toString());
                writer.write(inputJSONDATA.toString());
                writer.flush();
                writer.close();
            }

            int responseCode = urlConnection.getResponseCode();
            Utils.Log("before response === " + urlConnection.getResponseMessage().toString());
            responseJSON = convertResponseToJSON(urlConnection.getInputStream());
          /*  if(responseCode == 200 ){

            }else {
               // send error with response code.
                responseJSON = new JSONObject();
            }*/
        }catch (SocketTimeoutException se){
            se.printStackTrace();
            throw se;
        }catch (ConnectException ee){
            ee.printStackTrace();
            throw ee;
        }catch (EOFException ee){
            ee.printStackTrace();
            throw ee;
        }catch (Exception e) {
            e.printStackTrace();
        }finally {

        }
//        Utils.Log("Response==="+responseJSON.toString());
        return responseJSON;
    }


    public static JSONObject convertResponseToJSON(InputStream respInputStream) throws EOFException {

        JSONObject responseObject = null;
        Utils.Log("convertResponseToJSON");
        try {
            InputStreamReader isr = new InputStreamReader(respInputStream);
            StringBuilder sb = new StringBuilder();
            BufferedReader br = new BufferedReader(isr);

            String read = br.readLine();

            while(read != null){
                sb.append(read);
                read = br.readLine();
            }

            responseObject = new JSONObject(sb.toString());

        } catch (EOFException ee){
            ee.printStackTrace();
            throw ee;
        }catch (Exception e) {
            e.printStackTrace();
        }
        Utils.Log("parsing========"+responseObject.toString());
        return responseObject;
    }





    protected JSONArray doWorkJSONArray(JSONArray inputJSONDATA) throws ConnectException, EOFException, SocketTimeoutException {

        JSONArray responseArray = null;
        try {

            urlConnection.connect();
            if(reqType.equalsIgnoreCase("POST") || reqType.equalsIgnoreCase("PUT")){
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream()));
                System.out.println("requesting string==================" + inputJSONDATA.toString());
                writer.write(inputJSONDATA.toString());
                writer.flush();
                writer.close();
            }

            int responseCode = urlConnection.getResponseCode();
            Utils.Log("before response === " + urlConnection.getResponseMessage().toString());


            if (urlConnection.getResponseMessage().toString().equalsIgnoreCase("Ok"))
            {
                responseArray = convertResponseToJSONArray(urlConnection.getInputStream());
            }


          /*  if(responseCode == 200 ){

            }else {
               // send error with response code.
                responseJSON = new JSONObject();
            }*/
        }catch (SocketTimeoutException se){
            se.printStackTrace();
            throw se;
        }catch (ConnectException ee){
            ee.printStackTrace();
            throw ee;
        }catch (EOFException ee){
            ee.printStackTrace();
            throw ee;
        }catch (Exception e) {
            e.printStackTrace();
        }finally {

        }
//        Utils.Log("Response==="+responseJSON.toString());
        return responseArray;
    }

    public static JSONArray convertResponseToJSONArray(InputStream respInputStream) throws EOFException {

        JSONArray responseArray = null;
        Utils.Log("convertResponseToJSON");
        try {
            InputStreamReader isr = new InputStreamReader(respInputStream);
            StringBuilder sb = new StringBuilder();
            BufferedReader br = new BufferedReader(isr);

            String read = br.readLine();

            while(read != null){
                sb.append(read);
                read = br.readLine();
            }

            responseArray = new JSONArray(sb.toString());

        } catch (EOFException ee){
            ee.printStackTrace();
            throw ee;
        }catch (Exception e) {
            e.printStackTrace();
        }
        Utils.Log("parsing========"+responseArray.toString());
        return responseArray;
    }
}