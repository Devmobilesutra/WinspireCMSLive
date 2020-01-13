package com.cms.callmanager.Foc_Chargeble;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.cms.callmanager.R;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class ChargeListAdapter extends RecyclerView.Adapter<ChargeListAdapter.ViewHolder>{

    Context context;
    //ArrayList<String> list_foc;
    ArrayList<ModelClassForSavedData_Charge> list_charge = new ArrayList();
    ArrayList<String> plantsList = new ArrayList();
    int i=0; //used for for loop having some error so declare hare

    String Spinner_values;
    JSONArray jsonArray = new JSONArray();
    public static String qty_value;
    ProgressDialog progDailog;


    List<String> categories;

    ArrayList<Charge_list_Model> charge_list_models = new ArrayList<>();
    ArrayList<String> charge_NO = new ArrayList<String>();



    ArrayAdapter<String> charge_dataAdapter;


    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageButton plus, minus;
        Spinner spinner;
        EditText et_qty;
        TextView discription;

        public ViewHolder(View itemView) {
            super(itemView);
            plus = (ImageButton) itemView.findViewById(R.id.plus);
            minus = (ImageButton) itemView.findViewById(R.id.minus);
            et_qty = (EditText) itemView.findViewById(R.id.step);
            spinner =(Spinner)itemView.findViewById(R.id.spinner);
            discription=(TextView)itemView.findViewById(R.id.discription);

            minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    try {
                        list_charge.remove(position);
                        notifyItemRemoved(position);
                    }catch (ArrayIndexOutOfBoundsException e){e.printStackTrace();}
                }
            });

        }
    }


    public ChargeListAdapter(ArrayList<ModelClassForSavedData_Charge> list_foc1, final Context context) {
        this.list_charge = list_foc1;
        this.context = context;

        // call foc_asyntask method

        /*

         */


    }


    public ArrayList<ModelClassForSavedData_Charge> getList_foc() {
        return list_charge;
    }

    void setCharge_NO(ArrayList<String> charge_NO, ArrayList<Charge_list_Model> charge_list_Model ){
        this.charge_NO=charge_NO;
        this.charge_list_models=charge_list_Model;
    }

    @Override
    public int getItemCount() {
        return list_charge.size();
    }

    @Override
    public ChargeListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.charge_list_item, viewGroup, false);
        return new ChargeListAdapter.ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(final ChargeListAdapter.ViewHolder holder, final int possition) {



        if (possition==0){
            holder.minus.setVisibility(View.INVISIBLE);

        } else {
            holder.minus.setVisibility(View.VISIBLE);

        }
        if(possition==charge_NO.size()-2){
            holder.plus.setVisibility(View.GONE);

        } else {
            holder.plus.setVisibility(View.VISIBLE);

        }


        plantsList = charge_NO;
        int x =possition;
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                context, R.layout.simple_spinner_dropdown_item,plantsList) {


            @Override
            public boolean isEnabled(int position) {

                for (i = 0; i < list_charge.size(); i++) {

                    if (position == list_charge.get(i).getSelectedItemPosition()) {

                        return false;
                    }
                }
                return  true;
            }



            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view =  super.getDropDownView(position, convertView, parent);;
                TextView tv = (TextView) view;;

                for(i = 0; i< list_charge.size(); i++) {

                    if(position == list_charge.get(i).getSelectedItemPosition()) {
                        //selectedPositions.get(i) ;

                        tv.setText("Selected-"+list_charge.get(i).getSelectedItem());


                        // tv.setEnabled(false);

                    }else {
                        // tv.setTextColor(Color.BLACK);


                    }
                }
                return view;

            }




        };





        spinnerArrayAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        holder.spinner.setAdapter(spinnerArrayAdapter);


/*

        int x =possition;
        ArrayAdapter<String> dataAdapter;
        dataAdapter = new ArrayAdapter<String>(context, R.layout.simple_spinner_dropdown_item, charge_NO);
        dataAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        holder.spinner.setAdapter(dataAdapter);
*/

       // holder.spinner.setVisibility
        holder.et_qty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
              //  list_charge.get(possition).setQty(s.toString());


                try {
                    if (Integer.parseInt(s.toString())==0){
                        holder.et_qty.setText("");
                    }
                    else
                    {
                        list_charge.get(possition).setQty(s.toString());

                    }
                } catch (Exception e){

                }

            }
        });

        if(list_charge.get(x).getQty().length() > 0 && list_charge.get(x).getDescription().length() > 0 && list_charge.get(x).getSelectedItem().length() > 0 ) {
            holder.spinner.setSelection(getIndex(holder.spinner, list_charge.get(x).getSelectedItem()));
            holder.et_qty.setText(list_charge.get(x).getQty());
            holder.discription.setText(list_charge.get(x).getDescription());


            if (list_charge.size()== 1) {

                holder.minus.setVisibility(View.GONE);

            } else {
                holder.minus.setVisibility(View.VISIBLE);

            }


        }
        else{
            holder.et_qty.setText(null);
            holder.et_qty.requestFocus();
        }



     /*   holder.et_qty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
               // onEditTextChanged.onTextChanged(count, s.toString());

                qty_value = holder.et_qty.getText().toString();

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
*/

        //spinner


        holder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                holder.discription.setText(charge_list_models.get(position).getDiscription());

                list_charge.get(possition).setSelectedItemPosition(position);
                list_charge.get(possition).setSelectedItem(holder.spinner.getSelectedItem().toString());
               //  String item = holder.spinner.setvi
                list_charge.get(possition).setDescription(holder.discription.getText().toString());

            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try {

                    if (holder.discription.getText().toString().equalsIgnoreCase("")) {

                        Toast.makeText(context, "Please Enter description", Toast.LENGTH_SHORT).show();

                    } else if (holder.et_qty.getText().toString().equalsIgnoreCase("")) {

                        Toast.makeText(context, "Please Enter QTY", Toast.LENGTH_SHORT).show();

                    } else {

                        holder.minus.setVisibility(View.VISIBLE);

                        //    dataAdapter.remove((String)spinner.getSelectedItem());
                        list_charge.add(possition + 1, new ModelClassForSavedData_Charge("","",""));
                        notifyItemInserted(possition + 1);

                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
            }
        });



        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // int position = getAdapterPosition();
                try {
                    holder.et_qty.clearFocus();
                    holder.discription.clearFocus();
                    list_charge.remove(possition);
                    notifyItemRemoved(possition);
                    notifyDataSetChanged();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


    }

    public ArrayList<ModelClassForSavedData_Charge> getStepList(){
        return list_charge;
    }

    private int getIndex(Spinner spinner, String myString){
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                return i;
            }
        }

        return 0;
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }



}
