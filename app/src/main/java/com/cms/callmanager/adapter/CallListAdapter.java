package com.cms.callmanager.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.cms.callmanager.AcknowledgementActivity;
import com.cms.callmanager.AttachImageActivity;
import com.cms.callmanager.CallDetailsActivity;
import com.cms.callmanager.HomeActivity;
import com.cms.callmanager.Prefs;
import com.cms.callmanager.R;
import com.cms.callmanager.RangeTimePickerDialog;
import com.cms.callmanager.RepairDetailsActivity;
import com.cms.callmanager.constants.Constant;
import com.cms.callmanager.constants.Constants;
import com.cms.callmanager.dto.CallDTO;
import com.cms.callmanager.services.CallManagerAsyncTask;
import com.cms.callmanager.utils.Utils;
import com.cms.callmanager.utils.Validation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.EOFException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

//import java.time.LocalDateTime;

/**
 * Created by Monika on 1/7/17.
 */
public class CallListAdapter extends RecyclerView.Adapter<CallListAdapter.ViewHolder> {
    public static final String TAG = CallListAdapter.class.getName();
    private ArrayList<CallDTO> callList;
    int year, month, day;
    Context context;
    TextView editTxtTime;
    EditText scheduleTime, c_date, timePicker1, remarks;
    private int hour;
    private int minute;
    private String screen;
    SharedPreferences preferences;
    private String format = "";
    String selectedHr = null;
    int selectedSec = 0;
    LocationManager locationManager;
    String selectedMin = null;
    TextInputLayout txtCustodianDate, txtTimeLayout;
    boolean GpsStatus;
    //String lati, longi;
   // LocationManager locationManager;
    String mprovider;





    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case

        public TextView atmID, callDate, bankName, responseTime, resolutionTime, status, viewAtmID, viewBankName, viewCallDate,
                viewResponseTime, viewRequestTime, viewStatus, docketNo, viewMainStatus, Diagnosis, Call_type;
        Button checkStatusBtn;
        private ItemClickListener mListener;

       // String lati = callList.get(i)




        public ViewHolder(View v) {
            super(v);

            atmID = (TextView) v.findViewById(R.id.atmID);
            callDate = (TextView) v.findViewById(R.id.callDate);
            bankName = (TextView) v.findViewById(R.id.bankName);
            responseTime = (TextView) v.findViewById(R.id.responseTime);
            resolutionTime = (TextView) v.findViewById(R.id.resolutionTime);
            status = (TextView) v.findViewById(R.id.status);
            docketNo = (TextView) v.findViewById(R.id.docketNo);
            viewAtmID = (TextView) v.findViewById(R.id.viewAtmID);
            viewBankName = (TextView) v.findViewById(R.id.viewBankName);
            viewCallDate = (TextView) v.findViewById(R.id.viewCallDate);
            viewResponseTime = (TextView) v.findViewById(R.id.viewResponseTime);
            viewRequestTime = (TextView) v.findViewById(R.id.viewRequestTime);
            viewStatus = (TextView) v.findViewById(R.id.viewStatus);
            viewMainStatus = (TextView) v.findViewById(R.id.viewMainStatus);
            Diagnosis = (TextView) v.findViewById(R.id.Diagnosis);
            Call_type = (TextView) v.findViewById(R.id.Call_type);

            //checkStatusBtn = (Button) v.findViewById(R.id.checkStatus);
            //checkStatusBtn.setOnClickListener(this);




            v.setOnClickListener(this);


        }

        public void setClickListener(ItemClickListener listener) {
            this.mListener = listener;
        }

        public void setOnLongClickListener(ItemClickListener listener) {
            this.mListener = listener;
        }

        @Override
        public void onClick(View view) {
            mListener.onClickItem(getLayoutPosition());
        }
    }


    // Provide a suitable constructor (depends on the kind of dataset)
    public CallListAdapter(ArrayList<CallDTO> callLists, Context context, String screen) {
        callList = callLists;
        this.context = context;
        this.screen = screen;

    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_call_list, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element


        final CallDTO callDto = callList.get(position);






        Utils.SetDefautltFont(holder.atmID, "Bold", context);
        Utils.SetDefautltFont(holder.bankName, "Regular", context);
        Utils.SetDefautltFont(holder.callDate, "Regular", context);
        Utils.SetDefautltFont(holder.responseTime, "Regular", context);
        Utils.SetDefautltFont(holder.resolutionTime, "Regular", context);
        Utils.SetDefautltFont(holder.status, "Regular", context);
        Utils.SetDefautltFont(holder.viewAtmID, "Bold", context);
        Utils.SetDefautltFont(holder.viewBankName, "Regular", context);
        Utils.SetDefautltFont(holder.viewCallDate, "Regular", context);
        Utils.SetDefautltFont(holder.viewResponseTime, "Regular", context);
        Utils.SetDefautltFont(holder.viewRequestTime, "Regular", context);
        Utils.SetDefautltFont(holder.viewStatus, "Regular", context);
        Utils.SetDefautltFont(holder.docketNo, "Regular", context);
        Utils.SetDefautltFont(holder.viewMainStatus, "Regular", context);

        holder.atmID.setText(callDto.getAtmID());

        Utils.Log("BankName==" + callDto.getBankName());
        holder.bankName.setText(callDto.getBankName());
        if (callDto.getCallDate() != null && callDto.getCallDate() != "") {
            String callDate = Utils.DateFormater(callDto.getCallDate());
            holder.callDate.setText(callDate);
        }
        if (callDto.getResponseTime() != null && callDto.getResponseTime() != "") {
            String responseTime = Utils.DateFormater(callDto.getResponseTime());
            holder.responseTime.setText(responseTime);
        }



/*


        Log.d("", "responseTime123: "+callDto.getResponseTime());
        String res_times = callDto.getResponseTime().toString();



		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//final String received_date ="2019/06/03 12:05";
		long timeInMilliseconds = 0;

		try {
			Date date = sdf.parse(res_times);
			timeInMilliseconds = date.getTime();
			//System.out.println(date);
		} catch (ParseException e) {
			e.printStackTrace();


		}

		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE); //Please note that context is "this" if you are inside an Activity

		Intent intent = new Intent(context, BroadcastManager.class);
		intent.putExtra("date",res_times);
		intent.putExtra("atmid",callDto.getAtmID());
		PendingIntent event = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
		//alarmManager.setInexactRepeating (AlarmManager.RTC_WAKEUP, timeInMilliseconds, event);
		//alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeInMilliseconds, event);
		alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,timeInMilliseconds, AlarmManager.INTERVAL_DAY, event);




*/


        if (callDto.getResolutionTime() != null && callDto.getResolutionTime() != "") {
            String resolutionTime = Utils.DateFormater(callDto.getResolutionTime());
            holder.resolutionTime.setText(resolutionTime);
        }

        holder.status.setText(callDto.getStatus());
        holder.viewMainStatus.setText(callDto.getMainStatus());
        holder.docketNo.setText(callDto.getDocketNo());
        holder.Diagnosis.setText(callDto.getDiagnosis());
        Log.d("", "getDiagnosis: " + callDto.getDiagnosis());

        holder.Call_type.setText((callDto.getCallType()));

        Log.d("", "getCallType: " + callDto.getCallType());


        holder.setClickListener(new ItemClickListener() {

            @Override
            public void onClickItem(int pos) {
                preferences = (context).getSharedPreferences("CMS", Context.MODE_PRIVATE);
                int isTLFlag = preferences.getInt("IsTLFlag", 0);
                Log.d("", "onClickItemisTLFlag: " + isTLFlag);

                if (isTLFlag == 1 && screen.equalsIgnoreCase("Rejected")) {
                    Intent i = new Intent(context, CallDetailsActivity.class);
                    i.putExtra("docketNo", callList.get(pos).getDocketNo().toString());
                    context.startActivity(i);
                } else {

                    showStatusAlertDialog(pos);
                }
            }

            @Override
            public void onLongClickItem(int pos) {

            }
        });
/*
            holder.checkStatusBtn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					showStatusAlertDialog(pos);
				}


			});*/
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return callList.size();
    }


    private void showStatusAlertDialog(final int pos) {
        Utils.Log("showStatusAlertDialog===");
        CharSequence[] alertBoxOptions = null;
        String holdCallStatus = "Hold";
        if (callList.get(pos).getMainStatus().trim().equalsIgnoreCase("Hold")) {
            holdCallStatus = "Unhold";
        }

        if (callList.get(pos).getMainStatus().trim().equalsIgnoreCase("Hold")) {

            Utils.Log("showStatusAlertDialog===2");
            alertBoxOptions = new CharSequence[]{holdCallStatus, "Call Details"};

        } else if (callList.get(pos).getMainStatus().trim().equalsIgnoreCase("Open")) {

            if (callList.get(pos).getActive() && callList.get(pos).getMobileActivity().trim().equalsIgnoreCase("DISPATCHED ENGINEER")) {
                alertBoxOptions = new CharSequence[]{"Accept", "Reject"};
            } else if (callList.get(pos).getActive() && callList.get(pos).getMobileActivity().trim().equalsIgnoreCase("Call Accepted")) {
                Utils.Log("showStatusAlertDialog===1");
                //alertBoxOptions.clone();

                alertBoxOptions = new CharSequence[]{"Engineer Started", holdCallStatus, "Call Details"};
            } else if (callList.get(pos).getActive() && callList.get(pos).getMobileActivity().trim().equalsIgnoreCase("Engineer Reached")
            ) {
                Utils.Log("showStatusAlertDialog===2");
                alertBoxOptions = new CharSequence[]{"Repair Started", holdCallStatus, "Call Details"};
            } else if (callList.get(pos).getActive() && callList.get(pos).getMobileActivity().trim().equalsIgnoreCase("Engineer Started")) {
                Utils.Log("showStatusAlertDialog===3");
                alertBoxOptions = new CharSequence[]{"Engineer Reached", holdCallStatus, "Attach File", "Call Details"};
            } else if (callList.get(pos).getActive() && callList.get(pos).getMobileActivity().trim().equalsIgnoreCase("Repair Started")
            ) {
                Utils.Log("showStatusAlertDialog===4");
                alertBoxOptions = new CharSequence[]{"Repair Completed", holdCallStatus, "Attach File", "Call Details"};
            } else if (callList.get(pos).getActive() && callList.get(pos).getMobileActivity().equalsIgnoreCase("Repair Completed")) {
                Utils.Log("showStatusAlertDialog===5");
                alertBoxOptions = new CharSequence[]{"Attach File", "Acknowledge", "Call Details"};
            } else if (callList.get(pos).getActive() && callList.get(pos).getMobileActivity().equalsIgnoreCase("Hold")) {
                Utils.Log("showStatusAlertDialog===6");
                alertBoxOptions = new CharSequence[]{holdCallStatus, "Attach File", "Call Details"};
            } else if (callList.get(pos).getActive() && callList.get(pos).getMobileActivity().equalsIgnoreCase("Unhold")) {
                Utils.Log("showStatusAlertDialog===7");
                alertBoxOptions = new CharSequence[]{holdCallStatus, "Attach File", "Call Details"};
            } else if (callList.get(pos).getActive() && callList.get(pos).getMobileActivity().equalsIgnoreCase("Close")) {
                Utils.Log("showStatusAlertDialog===7");
                alertBoxOptions = new CharSequence[]{"Call Details"};
            } else {
                alertBoxOptions = new CharSequence[]{"Call Details"};
            }
        } else {
            alertBoxOptions = new CharSequence[]{"Call Details"};
        }

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        Utils.Log("alertBoxOptions===" + alertBoxOptions.toString());
        final CharSequence[] finalAlertBoxOptions = alertBoxOptions;
        Utils.Log("showStatusAlertDialog===8" + finalAlertBoxOptions.toString());
        builder.setItems(finalAlertBoxOptions, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (finalAlertBoxOptions[item].equals("Call Accepted") ||
                        finalAlertBoxOptions[item].equals("Engineer Reached") ||
                        finalAlertBoxOptions[item].equals("Engineer Started")) {
                    StatusAsyncTask statusAsyncTask = new StatusAsyncTask(Constants.FOLLOWUP, "PUT",
                            context, (String) finalAlertBoxOptions[item], pos);
                    statusAsyncTask.execute();


                } else if (finalAlertBoxOptions[item].equals("Hold") ||
                        finalAlertBoxOptions[item].equals("Unhold")) {
                    preferences = (context).getSharedPreferences("CMS", Context.MODE_PRIVATE);
                    String userId = preferences.getString("userId", null);
                    String postURLData = Constants.HoldCall; /*+ "?docketno=" + callList.get(pos).getDocketNo().toString() +
                            "?Latitude=" + Prefs.with(context).getString(Constant.LATI, "") +
                            "?Longitude=" + Prefs.with(context).getString(Constant.LONGI, "") +
                            "?Mobile_Device_Id=" + Prefs.with(context).getString(Constant.DEVICE_ID, "") +
                            "&userid=" + userId + "&comments=" + "Call On Hold";*/


                    JSONObject jsonObject = new JSONObject();
                    try {
                        //	 jsonObject = new JSONObject(postURLData);
                        jsonObject.put("DocketNo", callList.get(pos).getDocketNo().toString());
                        jsonObject.put("Latitude", Prefs.with(context).getString(Constant.LATI, ""));
                        jsonObject.put("Longitude", Prefs.with(context).getString(Constant.LONGI, ""));

                        Log.d(TAG, "getLongitude123: "+Prefs.with(context).getString(Constant.LATI, ""));

                        jsonObject.put("Mobile_Device_Id", Prefs.with(context).getString(Constant.DEVICE_ID, ""));
                        jsonObject.put("Comments", "Call On Hold");
                        jsonObject.put("UserId", userId);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    StatusHoldCall statusHoldCall = new StatusHoldCall(postURLData, "POST", pos, (String) finalAlertBoxOptions[item], context, jsonObject);
                    statusHoldCall.execute();

                } else if (finalAlertBoxOptions[item].equals("Attach File")) {
                    Intent i = new Intent(context, AttachImageActivity.class);
                    i.putExtra("docketNo", callList.get(pos).getDocketNo().toString());
                    context.startActivity(i);
                } else if (finalAlertBoxOptions[item].equals("Acknowledge")) {
                    Intent i = new Intent(context, AcknowledgementActivity.class);
                    i.putExtra("docketNo", callList.get(pos).getDocketNo().toString());
                    context.startActivity(i);
                } else if (finalAlertBoxOptions[item].equals("Repair Completed") || finalAlertBoxOptions[item].equals("Repair Details")) {
                    Intent i = new Intent(context, RepairDetailsActivity.class);
                    i.putExtra("docketNo", callList.get(pos).getDocketNo().toString());
                    context.startActivity(i);
                } else if (finalAlertBoxOptions[item].equals("Call Details")) {
                    Intent i = new Intent(context, CallDetailsActivity.class);
                    i.putExtra("docketNo", callList.get(pos).getDocketNo().toString());
                    context.startActivity(i);
                } else if (finalAlertBoxOptions[item].equals("Accept")) {
                    StatusAcceptOrRejectCall statusAcceptOrRejectCall =
                            new StatusAcceptOrRejectCall(Constants.CALLACCEPTORREJECT, "POST", pos, (String) finalAlertBoxOptions[item], callList.get(pos).getDocketNo().toString(), "", context);
                    statusAcceptOrRejectCall.execute();
                } else if (finalAlertBoxOptions[item].equals("Reject")) {
                    showRejectDialog(pos);
                } else if (finalAlertBoxOptions[item].equals("Repair Started")) {
                    showCustodianDialog(pos);
                }

            }
        });
        builder.show();
    }


    private void showRejectDialog(final int pos) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("Reject Call");
        alertDialog.setMessage("Why do you want to reject call?");

        final EditText input = new EditText(context);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        lp.setMargins(5, 0, 5, 0);
        input.setLayoutParams(lp);
        alertDialog.setView(input);
        //alertDialog.setIcon(R.drawable.key);

        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        StatusAcceptOrRejectCall statusAcceptOrRejectCall = new StatusAcceptOrRejectCall(Constants.CALLACCEPTORREJECT, "POST", pos, "Reject", callList.get(pos).getDocketNo().toString(), input.getText().toString(), context);
                        statusAcceptOrRejectCall.execute();

                    }
                });

        alertDialog.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();
    }

    private void showCustodianDialog(final int pos) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View promptView = layoutInflater.inflate(R.layout.dialog_custodian_date, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        // set prompts.xml to be the layout file of the alertdialog builder
        alertDialogBuilder.setView(promptView);
        alertDialogBuilder.setTitle("Repair Started date");

        remarks = (EditText) promptView.findViewById(R.id.remarks);
        c_date = (EditText) promptView.findViewById(R.id.edtCustodianDate);
        timePicker1 = (EditText) promptView.findViewById(R.id.timePicker);
        txtCustodianDate = (TextInputLayout) promptView.findViewById(R.id.txtCustodianDate);
        txtTimeLayout = (TextInputLayout) promptView.findViewById(R.id.txtTimeLayout);

        // Show a datepicker when the dateButton is clicked
        c_date.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View v) {
                                          //Calendar now = Calendar.getInstance();
                                          final Calendar c = Calendar.getInstance();

                                          DatePickerDialog dpd = new DatePickerDialog(context,
                                                  new DatePickerDialog.OnDateSetListener() {

                                                      @Override
                                                      public void onDateSet(DatePicker view, int year,
                                                                            int monthOfYear, int dayOfMonth) {
                                                          String my = "";
                                                          String dm = "";

                                                          if (monthOfYear > 0 && monthOfYear < 9) {
                                                              my = "0" + (monthOfYear + 1);
                                                          } else {
                                                              my = String.valueOf(monthOfYear);
                                                          }
                                                          if (dayOfMonth > 0 && dayOfMonth < 9) {
                                                              dm = "0" + dayOfMonth;
                                                          } else {
                                                              dm = String.valueOf(dayOfMonth);
                                                          }
                                                          c_date.setText(year + "-"
                                                                  + (my) + "-" + dm);

                                                      }
                                                  }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE));
                                          //  dpd.getDatePicker().setMinDate(System.currentTimeMillis());
                                          dpd.getDatePicker().setMaxDate(System.currentTimeMillis());
                                          try {
                                              dpd.getDatePicker().setMinDate(Utils.dateToMiliSeconds(callList.get(pos).getDispatchDate()));


                                          } catch (Exception e) {
                                              Log.i("JARVIS", "date not found dispatch date");
                                          }
                                          dpd.show();
                                      }
                                  }

        );

        timePicker1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Calendar now = Calendar.getInstance();
                Calendar datetime = null;

                final Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);

                String time_date = callList.get(pos).getDispatchDate();
                String[] test = time_date.split("T");
                String[] hrs = test[1].split("\\:");
                String hours = "0", minutes = "0";

                Calendar cal = Calendar.getInstance();
                SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
                try {
                    cal.setTime(sdf1.parse(time_date));// all done
                    datetime = cal;
                } catch (ParseException e) {
                    e.printStackTrace();
                }
//                                               mcurrentTime.setTime(hrs[0]);
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                try {
                    Date date = format.parse(time_date);
                    Log.e(TAG, "onClick: after change date " + date);
                    System.out.println(date);
                    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
                    String currentDateTimeString = sdf.format(date);
                    Log.e(TAG, "onClick: date " + currentDateTimeString);

                    if (currentDateTimeString.contains("pm")||currentDateTimeString.contains("PM")) {
                        currentDateTimeString = currentDateTimeString.replace("pm", "");
                        currentDateTimeString = currentDateTimeString.replace("PM", "");
                        Log.e(TAG, "onClick: cureent time pm " + currentDateTimeString);
                        String splitTime[] = currentDateTimeString.split(":");
                        hours = splitTime[0];
                        minutes = splitTime[1];

                        int hourInt = Integer.parseInt(hours);
                        hourInt = hourInt + 12;
                        Log.e(TAG, "onClick: after adding 12 " + hourInt);
                        hours = String.valueOf(hourInt).trim();
                        Log.e(TAG, "onClick: time in pm " + hours + " " + minutes);

                    } else if(currentDateTimeString.contains("am")||currentDateTimeString.contains("AM")) {
                        currentDateTimeString = currentDateTimeString.replace("am", "");
                        currentDateTimeString = currentDateTimeString.replace("AM", "");
                        String splitTime[] = currentDateTimeString.split(":");
                        hours = splitTime[0].trim();
                        minutes = splitTime[1].trim();
                        Log.e(TAG, "onClick: time in am " + hours + " " + minutes);
                    }        //which is from server;
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Log.e(TAG, "onClick: date and time " + callList.get(pos).getDispatchDate());
                Log.e(TAG, "onClick: hr " + test);

                Date d = new Date();


              /*  TimePickerDialog mTimePicker;
              //  final Calendar finalMcurrentTime = mcurrentTime;
              //  final Calendar finalDatetime = datetime;
                final Calendar finalDatetime = datetime;
                mTimePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        Utils.Log("=====" + finalDatetime.getTimeInMillis());
                        Utils.Log("=====" + mcurrentTime.getTimeInMillis());
                        if (finalDatetime.getTimeInMillis() > mcurrentTime.getTimeInMillis()) {//finalDatetime.getTimeInMillis() > mcurrentTime.getTimeInMillis() //mcurrentTime.getTimeInMillis() <finalDatetime.getTimeInMillis()
                            Utils.Log("=========");
                            Toast.makeText(context, "Time should be less than current time.",
                                    Toast.LENGTH_LONG).show();

                        } else {
                            Utils.Log("=====******");
                            timePicker1.setText("" + selectedHour + ":" + selectedMinute);
                            if (selectedHour > 0 && selectedHour < 9) {
                                selectedHr = "0" + selectedHour;
                            } else {
                                selectedHr = String.valueOf(selectedHour);
                            }
                            if (selectedMinute > 0 && selectedMinute < 9) {
                                selectedMin = "0" + selectedMinute;
                            } else {
                                selectedMin = String.valueOf(selectedMinute);
                            }

                            }


//												  selectedHr = selectedHour;
//												  selectedMin =selectedMinute;
                        }
                    },hour,minute,true);
                                               mTimePicker.setTitle("Select Time");
                                              // mTimePicker.setMin(hour,minute);
                                               mTimePicker.show();*/
                                               final RangeTimePickerDialog mTimePicker;
                                               mTimePicker = new RangeTimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {

                                                   @RequiresApi(api = Build.VERSION_CODES.M)
                                                   @Override
                                                   public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                                                      // if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                         //  Log.e("TAG", "inside OnTimeSetListener hr "+timePicker.getHour()+ " min "+timePicker.getMinute());
                                                  //     Log.e(TAG, "onTimeSet: hr "+selectedHr +" min "+selectedMin );
                                                           if (selectedHour > 0 && selectedHour < 9) {
                                                               selectedHr = "0" + selectedHour;
                                                           } else {
                                                               selectedHr = ""+selectedHour;
                                                           }
                                                           if (selectedMinute > 0 && selectedMinute < 9) {
                                                               selectedMin = "0" + selectedMinute;
                                                           } else {
                                                               selectedMin = String.valueOf(selectedMinute);
                                                           }
                                                           timePicker1.setText("" + selectedHr + ":" + selectedMin);
                                                     //  }

                                                   }
                                               }, hour, minute, true);//true = 24 hour time
                                               mTimePicker.setTitle("Select Time");
                                               //mTimePicker.setMin(Integer.parseInt(hours),Integer.parseInt(minutes));
                Log.e(TAG, "onClick: hors "+hours +" min "+minutes.trim() );
                                               mTimePicker.show();

                }
            }

        );


        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("OK",new DialogInterface.OnClickListener()

            {
                @Override
                public void onClick (DialogInterface dialog,int id){

            }
            }
        );
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener()

            {
                public void onClick (DialogInterface dialog,int id){
                dialog.cancel();
            }
            });

            // create an alert dialog
            AlertDialog alertD = alertDialogBuilder.create();

        alertD.show();
            Button theButton = alertD.getButton(DialogInterface.BUTTON_POSITIVE);
        Utils.Log("remarks"+remarks.getText().
            toString());
        theButton.setOnClickListener(new
            CustomListener(alertD, pos, c_date.getText().
            toString(),
                timePicker1.getText().
            toString(),remarks.
            getText().
            toString()));


        }

        class CustomListener implements View.OnClickListener {
            private final Dialog dialog;
            String cdd, time1, rm;
            int pos;

            public CustomListener(Dialog dialog, int pos, String c_date, String timepicker1, String rm) {
                this.dialog = dialog;
                this.cdd = c_date;
                this.pos = pos;
                this.time1 = timepicker1;
                this.rm = rm;
            }

            @Override
            public void onClick(View v) {
                // put your code here

                if (c_date.getText().toString().trim().length() > 0 &&
                        timePicker1.getText().toString().trim().length() > 0) {
                    String cd = c_date.getText().toString();
                    String tm = timePicker1.getText().toString();
                    String nowAsISO = null;
                    Date date = null;
                    try {
                        cd = cd + "T" + selectedHr + ":" + selectedMin + ":" + "00Z";
                        Utils.Log("cd" + cd);
                        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                        date = formatter.parse(cd);

                        System.out.println("Today is " + date);

                        TimeZone tz = TimeZone.getTimeZone("UTC");
                        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                        df.setTimeZone(tz);
                        nowAsISO = df.format(date);
                        Utils.Log("rm===" + rm);
                        Date d = df.parse(nowAsISO);
                        if (Validation.timeValidation(d)) {
                            StatusRepairAsyncTask statusRepairAsyncTask = new StatusRepairAsyncTask(Constants.FOLLOWUP, "PUT",
                                    context, "Repair Started", pos, nowAsISO, remarks.getText().toString());
                            statusRepairAsyncTask.execute();
                            dialog.dismiss();
                        } else {
                            txtTimeLayout.setError("Time should be less than current time.");
                        }

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(context, "Date and time is required",
                            Toast.LENGTH_LONG).show();
                    if (!Validation.validateField(c_date, txtCustodianDate, (Activity) context,
                            "Date is required")) {
                        // return;
                    } else if (!Validation.validateField(timePicker1, txtTimeLayout, (Activity) context,
                            "Time is required")) {

                    }


                }
            }
        }


        public interface ItemClickListener {
            void onClickItem(int pos);

            void onLongClickItem(int pos);
        }

        private class StatusAsyncTask extends CallManagerAsyncTask {

            String currentStatus;
            int position;

            public StatusAsyncTask(String action, String reqType, Context context
                    , String status, int position) {
                super(action, reqType, context);
                this.currentStatus = status;
                this.position = position;
            }

            @Override
            protected JSONObject doInBackground(Object... params) {

                JSONObject postParamData = new JSONObject();
                preferences = (context).getSharedPreferences("CMS", Context.MODE_PRIVATE);
                String userId = preferences.getString("userId", null);
                //	Toast.makeText(context, userId, Toast.LENGTH_SHORT).show();


                try {
                    postParamData.put("Docket_No", callList.get(position).getDocketNo().toString());
                    postParamData.put("Follow_up_Status", currentStatus);
                    //postParamData.put("Custodian_Arrival_Time","2018-09-04T15:30:00");

                    postParamData.put("Action_Taken", currentStatus);
                    postParamData.put("Last_modified_By", userId);
                    postParamData.put("Sub_Status_Name", currentStatus);
                    postParamData.put("Latitude", Prefs.with(context).getString(Constant.LATI, ""));
                    postParamData.put("Longitude", Prefs.with(context).getString(Constant.LONGI, ""));
                    postParamData.put("Mobile_Device_Id", Prefs.with(context).getString(Constant.DEVICE_ID, ""));


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

                try {
                    json.get("Status").toString();
                    Log.d("", "onPostExecuteStatus: " + json.get("Status").toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (json != null) {
                    Utils.Log("JSON Response123=========" + json.toString());

                    try {
                        if (json.has("Status") && json.get("Status").toString().equalsIgnoreCase("Success")) {
                            Utils.Log("status-----------" + callList.get(position).getStatus() + "------");
                            if (callList.get(position).getMobileActivity().equalsIgnoreCase("Call Accepted")) {
                                callList.get(position).setStatus("Engineer Started");
                                callList.get(position).setMobileActivity("Engineer Started");
                                notifyDataSetChanged();
                            } else if (callList.get(position).getMobileActivity().equalsIgnoreCase("Engineer Started")) {
                                callList.get(position).setStatus("Engineer Reached");
                                callList.get(position).setMobileActivity("Engineer Reached");
                                notifyDataSetChanged();
                            } else if (callList.get(position).getMobileActivity().equalsIgnoreCase("Engineer Reached")) {
                                callList.get(position).setStatus("Repair Started");
                                callList.get(position).setMobileActivity("Repair Started");
                                notifyDataSetChanged();
                            } else if (callList.get(position).getMobileActivity().equalsIgnoreCase("Repair Started")) {
                                callList.get(position).setStatus("Repair Completed");
                                callList.get(position).setMobileActivity("Repair Completed");
                                notifyDataSetChanged();
                            } else {
                                Utils.showAlertBox("Error in updating", (Activity) context);
                            }

                        } else if (json.has("Status") && json.get("Status").toString().equalsIgnoreCase("Failure")) {
                            Utils.showAlertBox(json.getString("ErrorMessage").toString(), (Activity) context);
                        } else {
                            Utils.showAlertBox(json.getString("ErrorMessage").toString(), (Activity) context);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Utils.showAlertBox("Something Went wrong!!", (Activity) context);
                }
            }

        }


        private class StatusHoldCall extends CallManagerAsyncTask {

            JSONObject requestJSON = new JSONObject();
            String holdSatus = null;
            int position;
            String action;

            protected StatusHoldCall(String action, String reqType, int position, String holdSatus, Context context, JSONObject jsonObject) {
                super(action, reqType, context);
                this.holdSatus = holdSatus;
                this.position = position;

                this.requestJSON = jsonObject;
            }

            @Override
            protected JSONObject doInBackground(Object... params) {
                try {


                    return doWork(requestJSON);
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
                            if (json.has("PayLoad")) {
                                JSONArray jsonArray = json.getJSONArray("PayLoad");
                                try {
                                    JSONObject status = (JSONObject) jsonArray.get(0);
                                    String statuas = status.getString("Status");
                                    Utils.Log("response====1111" + json.toString());
                                    callList.get(position).setMainStatus(statuas);
                                    // callList.get(position).setMobileActivity(this.holdSatus);

                                    notifyDataSetChanged();
                                    Utils.showAlertBox(json.getString("ErrorMessage").toString(), (Activity) context);
                                } catch (JSONException e) {

                                }
                            }

                        } else if (json.has("Status") && json.get("Status").toString().equalsIgnoreCase("Failure")) {
                            Utils.showAlertBox(json.getString("ErrorMessage").toString(), (Activity) context);
                        } else {
                            Utils.showAlertBox(json.getString("ErrorMessage").toString(), (Activity) context);
                        }
                        notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Utils.showAlertBox("Something Went wrong!!", (Activity) context);
                }
            }
        }

        private class StatusAcceptOrRejectCall extends CallManagerAsyncTask {

            JSONObject requestJSON = new JSONObject();
            String holdSatus = null;
            int position;
            String comment;
            String docketNo;

            protected StatusAcceptOrRejectCall(String action, String reqType, int position, String holdSatus, String docketNo, String comment, Context context) {
                super(action, reqType, context);
                this.holdSatus = holdSatus;
                this.position = position;
                this.comment = comment;
                this.docketNo = docketNo;
            }

            @Override
            protected JSONObject doInBackground(Object... params) {
                preferences = (context).getSharedPreferences("CMS", Context.MODE_PRIVATE);
                String userId = preferences.getString("userId", null);
                try {

                    requestJSON.put("DocketNo", docketNo);
                    requestJSON.put("Engineer", userId);
                    requestJSON.put("Status", holdSatus);
                    requestJSON.put("ActionTaken", comment);

                    requestJSON.put("Latitude", Prefs.with(context).getString(Constant.LATI, ""));
                    requestJSON.put("Longitude", Prefs.with(context).getString(Constant.LONGI, ""));
                    requestJSON.put("Mobile_Device_Id", Prefs.with(context).getString(Constant.DEVICE_ID, ""));

                    return doWork(requestJSON);
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
                if (json != null) {
                    Utils.Log("JSON Response=========" + json.toString());
                    try {
                        if (json.has("Status") && json.get("Status").toString().equalsIgnoreCase("Success")) {
                            Utils.Log("response====" + json.toString());

                            if (callList.get(position).getMobileActivity().equalsIgnoreCase("DISPATCHED ENGINEER")) {
                                if (holdSatus.equalsIgnoreCase("Reject")) {
                                    // callList.get(position).setMainStatus("Reject");
                                    callList.get(position).setMobileActivity("Reject");
                                    callList.get(position).setStatus("Reject");
                                } else {
                                    // callList.get(position).setMainStatus("Call Accepted");
                                    callList.get(position).setMobileActivity("Call Accepted");
                                    callList.get(position).setStatus("Call Accepted");
                                }
                                notifyDataSetChanged();
                            }

                        } else if (json.has("Status") && json.get("Status").toString().equalsIgnoreCase("Failure")) {
                            Utils.showAlertBox(json.getString("ErrorMessage").toString(), (Activity) context);
                        } else {
                            Utils.showAlertBox(json.getString("ErrorMessage").toString(), (Activity) context);
                        }
                        notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Utils.showAlertBox("Something Went wrong!!", (Activity) context);
                }
            }
        }

        private class StatusRepairAsyncTask extends CallManagerAsyncTask {

            String currentStatus, cd, rm;
            Date c_date;
            int position;

            public StatusRepairAsyncTask(String action, String reqType, Context context
                    , String status, int position, String c_date, String rm) {
                super(action, reqType, context);
                this.currentStatus = status;
                this.position = position;
                this.cd = c_date;
                this.rm = rm;
            }

            @Override
            protected JSONObject doInBackground(Object... params) {

                JSONObject postParamData = new JSONObject();
                preferences = (context).getSharedPreferences("CMS", Context.MODE_PRIVATE);
                String userId = preferences.getString("userId", null);
                try {
                    postParamData.put("Docket_No", callList.get(position).getDocketNo().toString());
                    postParamData.put("ATM_Id", callList.get(position).getAtmID().toString());
                    postParamData.put("Follow_up_Status", currentStatus);
                    postParamData.put("Last_modified_By", userId);
                    postParamData.put("Sub_Status_Name", currentStatus);
                    postParamData.put("Custodian_Arrival_Time", cd);

                    Log.d(TAG, "doInBackgroundATM_Id: "+callList.get(position).getAtmID().toString());
                    Log.d(TAG, "Custodian_Arrival_Time: "+cd);
                    Log.d(TAG, "doInBackgroundLatitude: "+Prefs.with(context).getString(Constant.LATI, ""));
                    Log.d(TAG, "doInBackgroundLongitude: "+ Prefs.with(context).getString(Constant.LONGI, ""));

                    postParamData.put("Action_Taken", rm);
                    postParamData.put("Latitude", Prefs.with(context).getString(Constant.LATI, ""));
                    postParamData.put("Longitude", Prefs.with(context).getString(Constant.LONGI, ""));
                    postParamData.put("Mobile_Device_Id", Prefs.with(context).getString(Constant.DEVICE_ID, ""));

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
                if (json != null) {
                    Utils.Log("JSON Response=========" + json.toString());
                    try {
                        if (json.has("Status") && json.get("Status").toString().equalsIgnoreCase("Success")) {
                            Utils.Log("status-----------" + callList.get(position).getStatus() + "------");
                            if (callList.get(position).getMobileActivity().equalsIgnoreCase("Call Accepted")) {
                                callList.get(position).setStatus("Engineer Started");
                                callList.get(position).setMobileActivity("Engineer Started");
                                notifyDataSetChanged();
                            } else if (callList.get(position).getMobileActivity().equalsIgnoreCase("Engineer Started")) {
                                callList.get(position).setStatus("Engineer Reached");
                                callList.get(position).setMobileActivity("Engineer Reached");
                                notifyDataSetChanged();
                            } else if (callList.get(position).getMobileActivity().equalsIgnoreCase("Engineer Reached")) {
                                callList.get(position).setStatus("Repair Started");
                                callList.get(position).setMobileActivity("Repair Started");
                                notifyDataSetChanged();
                            } else if (callList.get(position).getMobileActivity().equalsIgnoreCase("Repair Started")) {
                                callList.get(position).setStatus("Repair Completed");
                                callList.get(position).setMobileActivity("Repair Completed");
                                notifyDataSetChanged();
                            } else {
                                Utils.showAlertBox("Error in updating", (Activity) context);
                            }
                            Toast.makeText(context, "Updated Successfully", Toast.LENGTH_SHORT).show();


                        } else if (json.has("Status") && json.get("Status").toString().equalsIgnoreCase("Failure")) {
                            Utils.showAlertBox(json.getString("ErrorMessage").toString(), (Activity) context);
                        } else {
                            Utils.showAlertBox(json.getString("ErrorMessage").toString(), (Activity) context);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Utils.showAlertBox("Something Went wrong!!", (Activity) context);
                }
            }

        }


        public void CheckGpsStatus () {

            locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

            GpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        }

    }


