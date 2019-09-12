package com.cms.callmanager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.cms.callmanager.adapter.ATMListAdapter;
import com.cms.callmanager.constants.Constants;
import com.cms.callmanager.dto.ATMDetailsDTO;
import com.cms.callmanager.services.CallManagerAsyncTask;
import com.cms.callmanager.services.GPSTracker;
import com.cms.callmanager.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.EOFException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

/**
 * Created by zogato on 1/7/17.
 */
public class NearestATMActivity extends AppCompatActivity {
	private RecyclerView mRecyclerView;
	private RecyclerView.Adapter mAdapter;
	private RecyclerView.LayoutManager mLayoutManager;
	SharedPreferences preferences;
	ArrayList<ATMDetailsDTO> atmDetailsDTOs;
	private double latitude;
	private double longitude;

	private static final int ACCESS_FINE_LOCATION = 3;
	TextView errorTxt;
	Toolbar toolbar;
	FloatingActionButton atmsNearMeFabBtn;
	SwipeRefreshLayout mSwipeRefreshLayout;
	GPSTracker gps ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nearest_atm);
		toolbar       = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeAsUpIndicator(R.mipmap.action_back);
		initUI();

		gps = new GPSTracker();
		Utils.Log("lat==="+gps.getLatitude());
		Utils.Log("long==="+gps.getLongitude());

		if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
				!= PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(NearestATMActivity.this,
				android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(NearestATMActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);

		} else {
			//Toast.makeText(this,"You need have granted permission",Toast.LENGTH_SHORT).show();
			 gps = new GPSTracker(this, NearestATMActivity.this);

			// Check if GPS enabled
			if (gps.canGetLocation()) {

				latitude = gps.getLatitude();
				longitude = gps.getLongitude();

				// \n is for new line
			//	Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
			} else {
				// Can't get location.
				// GPS or network is not enabled.
				// Ask user to enable GPS/network in settings.
				gps.showSettingsAlert();
			}
		}
		getNearestATMList();

	}


	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);

		switch (requestCode) {
			case 1: {
				// If request is cancelled, the result arrays are empty.
				if (grantResults.length > 0
						&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {

					// permission was granted, yay! Do the

					// contacts-related task you need to do.

					gps = new GPSTracker(this, NearestATMActivity.this);

					// Check if GPS enabled
					if (gps.canGetLocation()) {

						latitude = gps.getLatitude();
						longitude = gps.getLongitude();

						// \n is for new line
						Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
					} else {
						// Can't get location.
						// GPS or network is not enabled.
						// Ask user to enable GPS/network in settings.
						gps.showSettingsAlert();
					}

				} else {

					// permission denied, boo! Disable the
					// functionality that depends on this permission.

					Toast.makeText(this, "You need to grant permission", Toast.LENGTH_SHORT).show();
				}
				return;
			}
		}
	}


	private void initUI() {


		mRecyclerView  = (RecyclerView) findViewById(R.id.nearestAtmList);
		atmsNearMeFabBtn  = (FloatingActionButton) findViewById(R.id.atmsNearMe);
		mLayoutManager = new LinearLayoutManager(getApplicationContext());
		mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_main_swipe_refresh_layout);

		mRecyclerView.setItemAnimator(new DefaultItemAnimator());
		mRecyclerView.setLayoutManager(mLayoutManager);

		mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {

				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						try {
							errorTxt.setVisibility(View.GONE);
							mRecyclerView.setVisibility(View.VISIBLE);
							getNearestATMList();
						} catch (Exception e) {
							mSwipeRefreshLayout.setRefreshing(false);
							e.printStackTrace();
						}
					}
				});
			}
		});
		atmsNearMeFabBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(atmDetailsDTOs != null && atmDetailsDTOs.size() > 0){
					Intent atmIntent = new Intent(NearestATMActivity.this , MapsActivity.class);
					atmIntent.putExtra("atms",atmDetailsDTOs);
					startActivity(atmIntent);
				}else {
					Utils.showAlertBox("No ATM to show on map",NearestATMActivity.this);
				}

			}
		});

		preferences    = getSharedPreferences("CMS", Context.MODE_PRIVATE);
		errorTxt       = (TextView) findViewById(R.id.errorTxt);
		// GPSTracker gpsTracker = new GPSTracker(this);

		/*if (gpsTracker.getIsGPSTrackingEnabled()) {
			String latitude = String.valueOf(gpsTracker.latitude);
			Utils.Log("latitude"+latitude);
			String longitude = String.valueOf(gpsTracker.longitude);
			Utils.Log("longitude"+longitude);

		}else {
			gpsTracker.showSettingsAlert();
		}*/
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem) {
		if (menuItem.getItemId() == android.R.id.home) {
			Intent searchIntent = new Intent(NearestATMActivity.this , HomeActivity.class);
			startActivity(searchIntent);
			finish();
		}
		return super.onOptionsItemSelected(menuItem);
	}

	private void getNearestATMList(){
		if(Utils.isInternetOn(this)){
			/*ProgressUtil.showProgressBar(NearestATMActivity.this,
			findViewById(R.id.root), R.id.progressBar);*/
			preferences    = getSharedPreferences("CMS", Context.MODE_PRIVATE);
			String userId = preferences.getString("userId" , null);
			String urlQueryParam = Constants.ATM+"?userId="+userId+"&latitude="+latitude+"&longitude="+longitude;
			 new NearestATMAsyncTask(urlQueryParam, "GET" ,
					NearestATMActivity.this ).execute();
		}else{
			mSwipeRefreshLayout.setRefreshing(false);
			mRecyclerView.setVisibility(View.GONE);
			errorTxt.setVisibility(View.VISIBLE);
			errorTxt.setText(getString(R.string.network_error));
		}


	}

	public class NearestATMAsyncTask extends CallManagerAsyncTask{

		JSONObject postParamData = new JSONObject();
		public NearestATMAsyncTask(String action, String reqType, Context context) {
			super(action, reqType, context);
		}

		@Override
		protected JSONObject doInBackground(Object... params) {
			try {

				return doWork(postParamData);
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
			if(json != null){
				Utils.Log("JSON Response========="+json.toString());
				try {
					if(json.has("Status") && json.get("Status").toString().equalsIgnoreCase("Success")) {

						if(json.getJSONArray("PayLoad").get(0).toString().equalsIgnoreCase("No record(s) to display.") ) {

						} else {
							JSONArray respATMList =  json.getJSONArray("PayLoad");
							atmDetailsDTOs = new ArrayList<>();
							Utils.Log("respATMList=="+respATMList.get(0).toString());
							for (int i = 0 ; i< respATMList.length();i++){
								ATMDetailsDTO atm = new ATMDetailsDTO();
								JSONObject jsonData = (JSONObject) respATMList.get(i);
								Utils.Log("respATMList=="+jsonData.toString());

								if(jsonData.getString("No_") != null && jsonData.getString("No_") != ""){
									Utils.Log(" resp==ID----"+jsonData.getString("No_").toString());
									atm.setAtmID(jsonData.getString("No_").toString());
								}
								if(jsonData.getString("Bank_Name") !=null && jsonData.getString("Bank_Name") != ""){
									atm.setBankName(jsonData.getString("Bank_Name").toString());
								}
								if(jsonData.getString("Distance_in_KM_from_Branch") !=null &&
										jsonData.getString("Distance_in_KM_from_Branch") != ""){
									atm.setDistance(jsonData.getString("Distance_in_KM_from_Branch").toString());
								}
								if(jsonData.getString("Latitude") !=null &&
										jsonData.getString("Latitude") != ""){
									atm.setLatitude(jsonData.getString("Latitude").toString());
								}
								if(jsonData.getString("Longitude") !=null &&
										jsonData.getString("Longitude") != ""){
									atm.setLongitude(jsonData.getString("Longitude").toString());
								}
								Utils.Log("ATM==="+atm.getAtmID().toString());
								Utils.Log("ATM"+atm.toString());

								atmDetailsDTOs.add(atm);

							}
							Utils.Log("Size======"+atmDetailsDTOs.size());
							if(atmDetailsDTOs.size() > 0){
								mAdapter = new ATMListAdapter(atmDetailsDTOs , NearestATMActivity.this);
								mRecyclerView.setAdapter(mAdapter);
							}else {
								mRecyclerView.setVisibility(View.GONE);
								errorTxt.setVisibility(View.VISIBLE);
							}
					}


					}else if(json.has("Status") && json.get("Status").toString().equalsIgnoreCase("Failure")) {
						Utils.showAlertBox(json.getString("ErrorMessage").toString(),NearestATMActivity.this );
					}else {
						Utils.showAlertBox(json.getString("ErrorMessage").toString(), NearestATMActivity.this);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}else {
				Utils.showAlertBox("Something Went wrong!!", NearestATMActivity.this);
			}
		}
	}


	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Intent searchIntent = new Intent(NearestATMActivity.this , HomeActivity.class);
		startActivity(searchIntent);
		finish();
	}
}