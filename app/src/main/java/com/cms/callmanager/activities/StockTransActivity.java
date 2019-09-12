package com.cms.callmanager.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.cms.callmanager.HomeActivity;
import com.cms.callmanager.LoginActivity;
import com.cms.callmanager.NearestATMActivity;
import com.cms.callmanager.R;
import com.cms.callmanager.RejectedCallList;
import com.cms.callmanager.constants.Constants;
import com.cms.callmanager.dto.stockTransfer;
import com.cms.callmanager.searchCallActivity;
import com.cms.callmanager.services.CallManagerAsyncTaskArray;
import com.cms.callmanager.utils.ProgressUtil;
import com.cms.callmanager.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.EOFException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

public class StockTransActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private TextView userName;
    private SharedPreferences preferences;
    private StockTransferAdapter stockTransferAdapter;
    RecyclerView vertical_recycler_view;
    ArrayList<stockTransfer> stockTransfers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_trans);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.inventory_to_be_received_stock_transfer));
        setSupportActionBar(toolbar);


        preferences = getSharedPreferences("CMS", Context.MODE_PRIVATE);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               startActivity(new Intent(StockTransActivity.this,CreateTransferOrderActivity.class));
                /* Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();  */
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        View header = navigationView.getHeaderView(0);
        userName  = (TextView) header.findViewById(R.id.userName);
        String name = preferences.getString("UserName" , null);
        String userId = preferences.getString("userId", null);

        userName.setText(name);

        navigationView.setNavigationItemSelectedListener(this);


        int isTLFlag = preferences.getInt("IsTLFlag" , 0);

        if (isTLFlag == 1) {
            Menu nav_Menu = navigationView.getMenu();
            nav_Menu.findItem(R.id.rejectedCalls).setVisible(true);
        }else {
            Utils.Log("in menu-------2--");
            Menu nav_Menu = navigationView.getMenu();
            nav_Menu.findItem(R.id.rejectedCalls).setVisible(false);
        }


        if(Utils.isInternetOn(this)){
            ProgressUtil.ShowBar(StockTransActivity.this);
            StockListAPI engineertoHubGoodAPI = new StockListAPI(Constants.GetInventoryTransferList+"?UserId="+userId, "GET" ,
                    StockTransActivity.this);
            engineertoHubGoodAPI.execute();
        }else{
            Utils.showAlertBox("Please Connect to internet",StockTransActivity.this);

        }


        // recycler view
      //  stockTransfers = getData();
        vertical_recycler_view= (RecyclerView)findViewById(R.id.vertical_recycler_view);
        stockTransferAdapter=new StockTransferAdapter(stockTransfers, getApplicationContext());
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(StockTransActivity.this, LinearLayoutManager.VERTICAL, false);
        vertical_recycler_view.setLayoutManager(horizontalLayoutManager);
        vertical_recycler_view.setAdapter(stockTransferAdapter);
    }

    private class StockListAPI extends CallManagerAsyncTaskArray {

        JSONArray requestArray = new JSONArray();

        public StockListAPI(String action, String reqType, Context context) {
            super(action, reqType, context);

        }

        @Override
        protected JSONArray doInBackground(Object... params) {

            try {
                return doWorkJSONArray(requestArray);
            } catch (ConnectException e) {
                e.printStackTrace();
                Log.d("", "doInBackground: " + e.getMessage());
            } catch (EOFException e) {
                e.printStackTrace();
                Log.d("", "doInBackground: " + e.getMessage());
            } catch (SocketTimeoutException e) {
                e.printStackTrace();
                Log.d("", "doInBackground: " + e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected void onPostExecute(JSONArray json) {
            super.onPostExecute(json);
            ProgressUtil.hideBar();
            if (json != null) {
                try {
                    JSONArray arr = json;

                    ArrayList<stockTransfer> list = new ArrayList<>();
                    for (int i = 0; i < arr.length(); i++) {


                        list.add(new stockTransfer(arr.getJSONObject(i).getString("No_"),
                                arr.getJSONObject(i).getString("Transfer_from_Code"),
                                arr.getJSONObject(i).getString("Transfer_to_Code"),
                                arr.getJSONObject(i).getString("Status"),
                                arr.getJSONObject(i).getString("PickList_No")
                                )
                        );
                    }


                    stockTransferAdapter=new StockTransferAdapter(list, getApplicationContext());
                    vertical_recycler_view.setAdapter(stockTransferAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Utils.showAlertBox("JSONException : " + e.getMessage(), StockTransActivity.this);
                }
            } else {
                Utils.showAlertBox("No record(s) to display.", StockTransActivity.this);

            }
        }
    }



    private class StockTransferAdapter extends RecyclerView.Adapter<StockTransferAdapter.MyViewHolder> implements Filterable {


        ArrayList<stockTransfer> mArrayList;
        ArrayList<stockTransfer> mFilteredList;
        Context context;


        public StockTransferAdapter(ArrayList<stockTransfer> arrayList, Context context) {
            this.mArrayList = arrayList;
            this.mFilteredList = arrayList;
            this.context = context;
        }

        @Override
        public StockTransferAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_stock_transfer, parent, false);

            return new StockTransferAdapter.MyViewHolder(itemView);
        }


        @Override
        public void onBindViewHolder(final StockTransferAdapter.MyViewHolder holder, final int position) {


            holder.tvName.setText(mFilteredList.get(position).getNumber());
          holder.tvFromLocation.setText(mFilteredList.get(position).getFrom_code());
          holder.tvTolocation.setText(mFilteredList.get(position).getTo_code());
          holder.tvPickListNo.setText(mFilteredList.get(position).getPickList_No());

            if (mFilteredList.get(position).getStatus().equalsIgnoreCase("0"))
                holder.tvStatus.setText("Open");

            else
                holder.tvStatus.setText("Released");



            if (position % 2 == 0) {
                holder.itemView.setBackgroundColor(Color.parseColor("#EEF6F5"));
            } else {
                holder.itemView.setBackgroundColor(Color.parseColor("#ffffff"));
            }


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent i = new Intent(StockTransActivity.this, StockTransferDetailActivity.class);
                    i.putExtra("number", mFilteredList.get(position).getNumber());
                    startActivity(i);

                   // startActivity(new Intent(StockTransActivity.this,StockTransferDetailActivity.class));
                   // Toast.makeText(context, "hiii", Toast.LENGTH_SHORT).show();
                }
            });


        }

        @Override
        public int getItemCount() {
            return mFilteredList == null ? 0 : mFilteredList.size();
        }


        @Override
        public Filter getFilter() {

            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence charSequence) {

                    String charString = charSequence.toString();

                    if (charString.isEmpty()) {

                        mFilteredList = mArrayList;
                    } else {

                        ArrayList<stockTransfer> filteredList = new ArrayList<>();

                        for (stockTransfer androidVersion : mArrayList) {


                            if (androidVersion.getNumber().toLowerCase().contains(charString) ||
                                    androidVersion.getFrom_code().toLowerCase().contains(charString) ||
                                    androidVersion.getTo_code().toLowerCase().contains(charString) ||
                                    androidVersion.getPickList_No().toLowerCase().contains(charString))
                            {
                                filteredList.add(androidVersion);
                            }
                        }

                        mFilteredList = filteredList;
                    }

                    FilterResults filterResults = new FilterResults();
                    filterResults.values = mFilteredList;
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                    mFilteredList = (ArrayList<stockTransfer>) filterResults.values;
                    notifyDataSetChanged();
                }
            };
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            private TextView tvName,tvFromLocation,tvTolocation,tvStatus,tvPickListNo;

            public MyViewHolder(View view) {
                super(view);
                tvName = (TextView) view.findViewById(R.id.tvName);
                tvFromLocation = (TextView) view.findViewById(R.id.tvFromLocation);
                tvTolocation = (TextView) view.findViewById(R.id.tvTolocation);
                tvStatus = (TextView) view.findViewById(R.id.tvStatus);
                tvPickListNo = (TextView)view.findViewById(R.id.tvPickListNo);
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            Intent intent = new Intent(StockTransActivity.this , InvTrasActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.stock_trans, menu);

        MenuItem search = menu.findItem(R.id.menu_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
        search(searchView);
        return true;


    }


    private void search(SearchView searchView) {

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                Log.d("", "onQueryTextChange: "+newText);
                stockTransferAdapter.getFilter().filter(newText);
                return true;
            }
        });
    }

    /*@Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //use the query to search
            Toast.makeText(this, query, Toast.LENGTH_SHORT).show();
            Log.d("", "handleIntent: "+query);
        }
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id){
            case R.id.inventoryTransfer :
                startActivity(new Intent(StockTransActivity.this, InvTrasActivity.class));
                finish();
                break;
            case R.id.searchCall :
                Intent searchIntent = new Intent(StockTransActivity.this , searchCallActivity.class);
                startActivity(searchIntent);
                finish();
                break;
            case R.id.nearestATM :
                //Toast.makeText()
                Intent intent = new Intent(StockTransActivity.this , NearestATMActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.logout :
                Intent logoutIntent  = new Intent(StockTransActivity.this , LoginActivity.class);
                logoutIntent.putExtra("finish", true);
                logoutIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
                        Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(logoutIntent);
                finish();
                break;
            case R.id.pendingCallList :
                Intent i  = new Intent(StockTransActivity.this , HomeActivity.class);
                startActivity(i);
                finish();
                break;
            case R.id.rejectedCalls :
                Intent rejectedIntent  = new Intent(StockTransActivity.this , RejectedCallList.class);
                startActivity(rejectedIntent);
                finish();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}