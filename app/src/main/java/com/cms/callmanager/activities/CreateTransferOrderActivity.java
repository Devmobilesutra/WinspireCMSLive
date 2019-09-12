package com.cms.callmanager.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cms.callmanager.R;
import com.cms.callmanager.adapter.CustomAdapter1;
import com.cms.callmanager.constants.Constants;
import com.cms.callmanager.dto.CodeAndNameModel;
import com.cms.callmanager.dto.LinesModel;
import com.cms.callmanager.dto.StockTransferList;
import com.cms.callmanager.dto.VendorModel;
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

public class CreateTransferOrderActivity extends AppCompatActivity {

    ArrayList<StockTransferList> stockTransferLists;
    RecyclerView vertical_recycler_view,rv_vendor,rv_line;
    private StockTransfListAdapter stockTransfListAdapter;
    ListView listView1;
    private static CustomAdapter1 adapter1;
   // ArrayList<DataModel1> dataModels1;
   SharedPreferences preferences;
   ArrayList<VendorModel> vendorModels;
   private VendorAdapter vendorAdapter;
 String userId;
   ArrayList<LinesModel> linesModels;
   private LinesAdapter linesAdapter;
   private Boolean transNum = true,Lines= true,vendor=true;
   private ImageView ivNumArrow,ivLinesArrow,ivVendorArrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_transfer_order);


        preferences = getSharedPreferences("CMS", Context.MODE_PRIVATE);

        userId = preferences.getString("userId", null);

        stockTransferLists = getData();
        vertical_recycler_view= (RecyclerView)findViewById(R.id.vertical_recycler_view);
        stockTransfListAdapter=new StockTransfListAdapter(stockTransferLists, getApplicationContext());
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(CreateTransferOrderActivity.this, LinearLayoutManager.VERTICAL, false);
        vertical_recycler_view.setLayoutManager(horizontalLayoutManager);
        vertical_recycler_view.setAdapter(stockTransfListAdapter);


        vendorModels = getVendorData();
        rv_vendor= (RecyclerView)findViewById(R.id.rv_vendor);
        vendorAdapter=new VendorAdapter(vendorModels, getApplicationContext());
        LinearLayoutManager horizontalLayoutManager1 = new LinearLayoutManager(CreateTransferOrderActivity.this, LinearLayoutManager.VERTICAL, false);
        rv_vendor.setLayoutManager(horizontalLayoutManager1);
        rv_vendor.setAdapter(vendorAdapter);


        linesModels = getLinesModelData();
        rv_line= (RecyclerView)findViewById(R.id.rv_line);
        linesAdapter=new LinesAdapter(linesModels, getApplicationContext());
        LinearLayoutManager horizontalLayout = new LinearLayoutManager(CreateTransferOrderActivity.this, LinearLayoutManager.VERTICAL, false);
        rv_line.setLayoutManager(horizontalLayout);
        rv_line.setAdapter(linesAdapter);



        ivNumArrow=(ImageView)findViewById(R.id.ivNumArrow);
        ivNumArrow.setImageResource(R.drawable.ic_expand_less_white_24dp);
        ivNumArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (transNum)
                {
                    vertical_recycler_view.setVisibility(View.GONE);
                    transNum = false;
                    ivNumArrow.setImageResource(R.drawable.ic_expand_more_white_24dp);
                }
                else
                {
                    transNum = true;
                    ivNumArrow.setImageResource(R.drawable.ic_expand_less_white_24dp);
                    vertical_recycler_view.setVisibility(View.VISIBLE);
                }

            }
        });


        ivLinesArrow=(ImageView)findViewById(R.id.ivLinesArrow);
        ivLinesArrow.setImageResource(R.drawable.ic_expand_less_white_24dp);
        ivLinesArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Lines)
                {
                    rv_line.setVisibility(View.GONE);
                    Lines = false;
                    ivLinesArrow.setImageResource(R.drawable.ic_expand_more_white_24dp);
                }
                else
                {   rv_line.setVisibility(View.VISIBLE);
                    Lines = true;
                    ivLinesArrow.setImageResource(R.drawable.ic_expand_less_white_24dp);

                }

            }
        });

        ivVendorArrow=(ImageView)findViewById(R.id.ivVendorArrow);
        ivVendorArrow.setImageResource(R.drawable.ic_expand_less_white_24dp);
        ivVendorArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(vendor) {
                    rv_vendor.setVisibility(View.GONE);
                    vendor = false;
                    ivVendorArrow.setImageResource(R.drawable.ic_expand_more_white_24dp);
                }
                else {
                    rv_vendor.setVisibility(View.VISIBLE);
                    vendor = true;
                    ivVendorArrow.setImageResource(R.drawable.ic_expand_less_white_24dp);
                }
            }
        });





        findViewById(R.id.tvNumSelect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CreateTransferOrderActivity.this, "Coming Soon..!", Toast.LENGTH_SHORT).show();
            }
        });


        findViewById(R.id.llAddLine).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CreateTransferOrderActivity.this, "Coming Soon..!", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.llVendorSelect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private ArrayList<LinesModel> getLinesModelData() {

        ArrayList<LinesModel> linesModels = new ArrayList<>();

        linesModels.add(new LinesModel("Item No.","Quantity","Qty Shipped","Qty Received"));

        return linesModels;

    }

    private ArrayList<VendorModel> getVendorData() {
        ArrayList<VendorModel> vendorModels = new ArrayList<>();

        vendorModels.add(new VendorModel("Shipping Agent Code","ShippingAgentCode"));
        vendorModels.add(new VendorModel("Shipping Method Code","ShippingMethodCode"));

        return vendorModels;
    }

    private ArrayList<StockTransferList> getData() {

        ArrayList<StockTransferList> stockTransferLists = new ArrayList<>();

        stockTransferLists.add(new StockTransferList("Transfer From Code","TransferFromCode","Transfer From Name"));
        stockTransferLists.add(new StockTransferList("Transfer To Code","TransferToCode","Transfer To Name"));
        stockTransferLists.add(new StockTransferList("Transit Code","Transitcode","Posting Date"));
        stockTransferLists.add(new StockTransferList("Structure","Structure","Status"));
        stockTransferLists.add(new StockTransferList("Hub Dim","HubDim","xyz"));

        return stockTransferLists;
    }

    private class StockTransfListAdapter extends RecyclerView.Adapter<StockTransfListAdapter.MyViewHolder> {
        List<StockTransferList> stockTransferLists;
        Context context;

        public StockTransfListAdapter(List<StockTransferList> stockTransferLists, Context context) {
            this.stockTransferLists = stockTransferLists;
            this.context = context;
        }

        @Override
        public StockTransfListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transfer_order_list, parent, false);

            return new StockTransfListAdapter.MyViewHolder(itemView);
        }

        @Override
        public int getItemCount() {
            return stockTransferLists == null ? 0 : stockTransferLists.size();
        }

        @Override
        public void onBindViewHolder(final StockTransfListAdapter.MyViewHolder holder, final int position) {

            holder.tvName.setText(stockTransferLists.get(position).getItemName());
            holder.itemQuantity.setText(stockTransferLists.get(position).getName());
            holder.tvNames.setText(stockTransferLists.get(position).getItemCode());
            holder.itemQuantity1.setText(stockTransferLists.get(position).getCode());


            /*if (position % 2 == 0) {
                holder.itemView.setBackgroundColor(Color.parseColor("#EEF6F5"));
            } else {
                holder.itemView.setBackgroundColor(Color.parseColor("#ffffff"));
            }*/



            holder.ivEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(Utils.isInternetOn(context)) {
                        preferences.edit().putInt("index", Integer.parseInt(String.valueOf(position))).apply();
                        preferences.edit().putString("flag", "stock").apply();

                        FromCodeApis(stockTransferLists.get(position).getParamName());
                    }
                    else
                        Utils.showAlertBox("Please connect to Internet",CreateTransferOrderActivity.this);

                }
            });

        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            private TextView tvName,itemQuantity,tvNames,itemQuantity1;
            private ImageView ivEdit;

            public MyViewHolder(View view) {
                super(view);
                tvName = (TextView) view.findViewById(R.id.tvName);
                tvNames=(TextView)view.findViewById(R.id.tvNames);
                itemQuantity1=(TextView)view.findViewById(R.id.itemQuantity1);
                ivEdit = (ImageView) view.findViewById(R.id.ivEdit);
                itemQuantity=(TextView)view.findViewById(R.id.itemQuantity);
            }
        }
    }

    private void FromCodeApis(String paramName) {
        ProgressUtil.ShowBar(CreateTransferOrderActivity.this);
        TransferFromCodeCallAsyncTask transferFromCodeCallAsyncTask = new TransferFromCodeCallAsyncTask(Constants.TransferFromCode+"?id="+paramName+"&UserId="+userId, "GET" ,
                CreateTransferOrderActivity.this);
        transferFromCodeCallAsyncTask.execute();
    }


    public class TransferFromCodeCallAsyncTask extends CallManagerAsyncTaskArray {

        JSONArray requestArray = new JSONArray();

        public TransferFromCodeCallAsyncTask(String action, String reqType, Context context) {
            super(action, reqType, context);

        }

        @Override
        protected JSONArray doInBackground(Object... params) {

            try {
                return doWorkJSONArray(requestArray);
            } catch (ConnectException e) {
                e.printStackTrace();
                Log.d("", "doInBackground: "+e.getMessage());
            } catch (EOFException e) {
                e.printStackTrace();
                Log.d("", "doInBackground: "+e.getMessage());
            } catch (SocketTimeoutException e) {
                e.printStackTrace();
                Log.d("", "doInBackground: "+e.getMessage());
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

            if(json != null){
                try {
                    JSONArray arr = json;
                    
                    ArrayList<CodeAndNameModel> list = new ArrayList<CodeAndNameModel>();
                    for(int i = 0; i < arr.length(); i++){

                        if (arr.getJSONObject(i).has("Code") && arr.getJSONObject(i).has("Name")) {
                            list.add(new CodeAndNameModel(arr.getJSONObject(i).getString("Code"),
                                    arr.getJSONObject(i).getString("Name")));
                        }
                        else if (arr.getJSONObject(i).has("Code") && arr.getJSONObject(i).has("Description")) {
                            list.add(new CodeAndNameModel(arr.getJSONObject(i).getString("Code"),
                                    arr.getJSONObject(i).getString("Description")));
                        }
                    }

                    listDialog(list);
                   // Utils.showAlertBox(list.toString(),CreateTransferOrderActivity.this);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Utils.showAlertBox("JSONException : "+e.getMessage(),CreateTransferOrderActivity.this);
                }
            }else {
                Utils.showAlertBox("Something Went wrong!!",CreateTransferOrderActivity.this);
            }
        }
    }


    private void listDialog(final ArrayList<CodeAndNameModel> list) {
        final Dialog dialog = new Dialog(CreateTransferOrderActivity.this);

        dialog.setCancelable(true);
        dialog.setTitle("Select Name");
        dialog.setContentView(R.layout.dialog_listview);

       /* Button btndialog = (Button) dialog.findViewById(R.id.btndialog);
        btndialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });*/
        listView1=(ListView)dialog.findViewById(R.id.listView1);

        adapter1= new CustomAdapter1(list,getApplicationContext());

        listView1.setAdapter(adapter1);
        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                CodeAndNameModel dataModel= list.get(position);
                dialog.dismiss();

                int lastIndex = preferences.getInt("index" , 0);

                String flag = preferences.getString("flag" , "");


                if (flag.equalsIgnoreCase("stock"))
                {
                    StockTransferList stockTransferList = new StockTransferList(
                            stockTransferLists.get(lastIndex).getItemCode(),
                            stockTransferLists.get(lastIndex).getItemName(),
                            stockTransferLists.get(lastIndex).getParamName(),
                            dataModel.getName(),
                            dataModel.getCode()
                    );

                    stockTransferLists.set(lastIndex,stockTransferList);
                    stockTransfListAdapter = new StockTransfListAdapter(stockTransferLists, getApplicationContext());
                    vertical_recycler_view.setAdapter(stockTransfListAdapter);
                }
                else if (flag.equalsIgnoreCase("vendor"))
                {
                    VendorModel vendorModel = new VendorModel(
                            vendorModels.get(lastIndex).getName(),
                            vendorModels.get(lastIndex).getParamName(),
                            dataModel.getCode()
                    );

                    vendorModels.set(lastIndex,vendorModel);
                    vendorAdapter = new VendorAdapter(vendorModels, getApplicationContext());
                    rv_vendor.setAdapter(vendorAdapter);
                }
            }
        });


        dialog.show();

    }

    private class VendorAdapter extends RecyclerView.Adapter<VendorAdapter.MyViewHolder> {
        List<VendorModel> vendorModels;
        Context context;

        public VendorAdapter(List<VendorModel> vendorModels, Context context) {
            this.vendorModels = vendorModels;
            this.context = context;
        }

        @Override
        public VendorAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vendor, parent, false);

            return new VendorAdapter.MyViewHolder(itemView);
        }

        @Override
        public int getItemCount() {
            return vendorModels == null ? 0 : vendorModels.size();
        }

        @Override
        public void onBindViewHolder(final VendorAdapter.MyViewHolder holder, final int position) {

            holder.tvName.setText(vendorModels.get(position).getName());
           holder.itemQuantity.setText(vendorModels.get(position).getCode());
           /*  holder.tvNames.setText(stockTransferLists.get(position).getItemCode());
            holder.itemQuantity1.setText(stockTransferLists.get(position).getCode());*/


           /* if (position % 2 == 0) {
                holder.itemView.setBackgroundColor(Color.parseColor("#EEF6F5"));
            } else {
                holder.itemView.setBackgroundColor(Color.parseColor("#ffffff"));
            }*/


            holder.ivEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Utils.isInternetOn(context)) {
                        TransferFromCodeCallAsyncTask transferFromCodeCallAsyncTask = new TransferFromCodeCallAsyncTask(Constants.TransferFromCode + "?id=" + stockTransferLists.get(position).getParamName()+"?UserId="+userId, "GET",
                                CreateTransferOrderActivity.this);
                        transferFromCodeCallAsyncTask.execute();

                        preferences.edit().putInt("index", Integer.parseInt(String.valueOf(position))).apply();
                        preferences.edit().putString("flag", "vendor").apply();
                    } else
                        Utils.showAlertBox("Please connect to Internet", CreateTransferOrderActivity.this);

                }
            });

        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            private TextView tvName, itemQuantity, tvNames, itemQuantity1;
            private ImageView ivEdit;

            public MyViewHolder(View view) {
                super(view);
                tvName = (TextView) view.findViewById(R.id.tvName);
                ivEdit = (ImageView) view.findViewById(R.id.ivEdit);
                itemQuantity=(TextView)view.findViewById(R.id.itemQuantity);
              /*  tvNames=(TextView)view.findViewById(R.id.tvNames);
                itemQuantity1=(TextView)view.findViewById(R.id.itemQuantity1);


                */
            }
        }
    }

    private class LinesAdapter extends RecyclerView.Adapter<LinesAdapter.MyViewHolder> {
        List<LinesModel> linesModels;
        Context context;

        public LinesAdapter(List<LinesModel> linesModels, Context context) {
            this.linesModels = linesModels;
            this.context = context;
        }

        @Override
        public LinesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lines, parent, false);

            return new LinesAdapter.MyViewHolder(itemView);
        }

        @Override
        public int getItemCount() {
            return linesModels == null ? 0 : linesModels.size();
        }

        @Override
        public void onBindViewHolder(final LinesAdapter.MyViewHolder holder, final int position) {

            holder.tvName.setText(linesModels.get(position).getItem_no());
            holder.tv_quantity_name.setText(linesModels.get(position).getQuantity_name());
            holder.tv_qty_ship_name.setText(linesModels.get(position).getQty_ship_name());
            holder.tv_qty_received_name.setText(linesModels.get(position).getQty_received_name());




            //  holder.itemQuantity.setText(stockTransferLists.get(position).getCode());
           /*  holder.tvNames.setText(stockTransferLists.get(position).getItemCode());
            holder.itemQuantity1.setText(stockTransferLists.get(position).getCode());*/


           /* if (position % 2 == 0) {
                holder.itemView.setBackgroundColor(Color.parseColor("#EEF6F5"));
            } else {
                holder.itemView.setBackgroundColor(Color.parseColor("#ffffff"));
            }*/



           /* holder.ivEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Utils.isInternetOn(context)) {

                        TransferFromCodeCallAsyncTask transferFromCodeCallAsyncTask = new TransferFromCodeCallAsyncTask(Constants.TransferFromCode + "?id=" + stockTransferLists.get(position).getParamName(), "GET",
                                CreateTransferOrderActivity.this);
                        transferFromCodeCallAsyncTask.execute();

                        preferences.edit().putInt("index", Integer.parseInt(String.valueOf(position))).apply();
                        preferences.edit().putString("flag", "line").apply();
                    } else
                        Utils.showAlertBox("Please connect to Internet", CreateTransferOrderActivity.this);

                }
            });*/

        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            private TextView tvName, itemQuantity, tvNames, itemQuantity1,tv_quantity_name,tv_qty_ship_name,tv_qty_received_name;
            private ImageView ivEdit;

            public MyViewHolder(View view) {
                super(view);
                tvName = (TextView) view.findViewById(R.id.tvName);
                ivEdit = (ImageView) view.findViewById(R.id.ivEdit);
              //  itemQuantity = (TextView) view.findViewById(R.id.itemQuantity);
               /// tv_quantity_name = (TextView) view.findViewById(R.id.tv_quantity_name);
               // tv_qty_ship_name = (TextView) view.findViewById(R.id.tv_qty_ship_name);
               // tv_qty_received_name = (TextView) view.findViewById(R.id.tv_qty_received_name);
              /*  tvNames=(TextView)view.findViewById(R.id.tvNames);
                itemQuantity1=(TextView)view.findViewById(R.id.itemQuantity1);


                */
            }
        }
    }
}
