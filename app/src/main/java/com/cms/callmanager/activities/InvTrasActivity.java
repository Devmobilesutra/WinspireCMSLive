package com.cms.callmanager.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.cms.callmanager.HomeActivity;
import com.cms.callmanager.LoginActivity;
import com.cms.callmanager.NearestATMActivity;
import com.cms.callmanager.R;
import com.cms.callmanager.RejectedCallList;
import com.cms.callmanager.searchCallActivity;
import com.cms.callmanager.utils.Utils;

public class InvTrasActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TextView userName;

    private SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inv_tras);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Inventory");
        setSupportActionBar(toolbar);



        preferences = getSharedPreferences("CMS", Context.MODE_PRIVATE);

        findViewById(R.id.cv_stock_transfer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InvTrasActivity.this,StockTransActivity.class));
            }
        });


        findViewById(R.id.llGoodHub).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InvTrasActivity.this,GoodHubActivity.class));
            }
        });


        findViewById(R.id.llDefectiveHub).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InvTrasActivity.this,DefectiveHubActivity.class));
            }
        });


        findViewById(R.id.llReplacement).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InvTrasActivity.this,ReplacementItemActivity.class));
            }
        });



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        View header = navigationView.getHeaderView(0);
        userName  = (TextView) header.findViewById(R.id.userName);
        String name = preferences.getString("UserName" , null);
        userName.setText(name);



        int isTLFlag = preferences.getInt("IsTLFlag" , 0);

        if (isTLFlag == 1) {
            Menu nav_Menu = navigationView.getMenu();
            nav_Menu.findItem(R.id.rejectedCalls).setVisible(true);
        }else {
            Utils.Log("in menu-------2--");
            Menu nav_Menu = navigationView.getMenu();
            nav_Menu.findItem(R.id.rejectedCalls).setVisible(false);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
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
                startActivity(new Intent(InvTrasActivity.this, InvTrasActivity.class));
                finish();
                break;
            case R.id.searchCall :
                Intent searchIntent = new Intent(InvTrasActivity.this , searchCallActivity.class);
                startActivity(searchIntent);
                finish();
                break;
            case R.id.nearestATM :
                //Toast.makeText()
                Intent intent = new Intent(InvTrasActivity.this , NearestATMActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.logout :
                Intent logoutIntent  = new Intent(InvTrasActivity.this , LoginActivity.class);
                logoutIntent.putExtra("finish", true);
                logoutIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
                        Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(logoutIntent);
                finish();
                break;
            case R.id.pendingCallList :
                Intent i  = new Intent(InvTrasActivity.this , HomeActivity.class);
                startActivity(i);
                finish();
                break;
            case R.id.rejectedCalls :
                Intent rejectedIntent  = new Intent(InvTrasActivity.this , RejectedCallList.class);
                startActivity(rejectedIntent);
                finish();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
