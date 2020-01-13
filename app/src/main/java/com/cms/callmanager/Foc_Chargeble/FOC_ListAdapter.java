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

import java.util.ArrayList;


public class FOC_ListAdapter extends RecyclerView.Adapter<FOC_ListAdapter.ViewHolder> {

    Context context;
    ArrayList<ModelClassForSavedData> list_foc = new ArrayList();
    int Spinner_values;
    int Spinner_posstion;
    int i;
    String str_item;
    public static String qty_value;
    ProgressDialog progDailog;
    ArrayList<String> item = new ArrayList<>();




    ArrayList<Foc_list_Model> foc_list_models = new ArrayList<>();
    ArrayList<String> Foc_NO = new ArrayList<String>();



    ArrayList<String> plantsList = new ArrayList();


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageButton plus, minus;
        EditText step;
        TextView discription;
        Spinner spinner;

        public ViewHolder(View itemView) {
            super(itemView);
            plus = (ImageButton) itemView.findViewById(R.id.plus);
            minus = (ImageButton) itemView.findViewById(R.id.minus_foc);
            step = (EditText) itemView.findViewById(R.id.step);
            spinner = (Spinner) itemView.findViewById(R.id.spinner);
            discription = (TextView) itemView.findViewById(R.id.discription);

         /*   minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    try {
                        list_foc.remove(position);
                        notifyItemRemoved(position);
                    } catch (ArrayIndexOutOfBoundsException e) {
                        e.printStackTrace();
                    }
                }
            });*/


/*
            step.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    list_foc.set(getAdapterPosition(), new ModelClassForSavedData("",s.toString(),""));
                    qty_value = step.getText().toString();
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });*/
        }
    }


    public FOC_ListAdapter(ArrayList<ModelClassForSavedData> list_foc1, final Context context) {
        this.list_foc = list_foc1;
        this.context = context;



        // call foc_asyntask method

/*

*/


    }


    public ArrayList<ModelClassForSavedData> getList_foc() {
        return list_foc;
    }

    void setFoc_NO(ArrayList<String> foc_NO, ArrayList<Foc_list_Model> foc_list_models ){
        this.Foc_NO=foc_NO;
        this.foc_list_models=foc_list_models;
    }

    @Override
    public int getItemCount() {
        return list_foc.size();
    }


    @Override
    public FOC_ListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item, viewGroup, false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(final FOC_ListAdapter.ViewHolder holder, final int possition) {


        if (possition==0){
            holder.minus.setVisibility(View.INVISIBLE);
        } else {
            holder.minus.setVisibility(View.VISIBLE);

        }
        if(possition==Foc_NO.size()-2){
            holder.plus.setVisibility(View.GONE);

        } else {
            holder.plus.setVisibility(View.VISIBLE);

        }



        plantsList = Foc_NO;
        int x =possition;
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                 context, R.layout.simple_spinner_dropdown_item,plantsList) {


             @Override
             public boolean isEnabled(int position) {

                 for (i = 0; i < list_foc.size(); i++) {

                     if (position == list_foc.get(i).getSelectedPosition()) {

                         return false;
                     }

                 }
                return  true;
             }



                 @Override
             public View getDropDownView(int position, View convertView ,
                                         ViewGroup parent) {



                         View view = super.getDropDownView(position, convertView, parent);
                         TextView tv = (TextView) view;
                         String str= (String) tv.getText();



                        for (i = 0; i < list_foc.size(); i++) {
                             if (position == list_foc.get(i).getSelectedPosition()) {


                                 tv.setText("Selected-"+list_foc.get(i).getSelectedItem());

                                 // tv.setEnabled(false);

                             }


                         }
                         return view;


                 }


             };





        spinnerArrayAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        holder.spinner.setAdapter(spinnerArrayAdapter);

      /*  int x =possition;

        ArrayAdapter<String> dataAdapter;
        dataAdapter = new ArrayAdapter<String>(context, R.layout.simple_spinner_dropdown_item, Foc_NO);
        dataAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        holder.spinner.setAdapter(dataAdapter);
        //holder.spinner.setPrompt("--Select--");

*/
        holder.step.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    if (Integer.parseInt(s.toString())==0){
                        holder.step.setText("");
                    }
                    else
                    {
                        list_foc.get(possition).setQty(s.toString());

                    }
                } catch (Exception e){

                }
            }
        });

     //   if(list_foc != null ){
        if(list_foc.get(x).getQty().length() > 0 && list_foc.get(x).getDescription().length() > 0 && list_foc.get(x).getSelectedItem().length() > 0 ) {
            holder.spinner.setSelection(getIndex(holder.spinner, list_foc.get(x).getSelectedItem()));
            holder.step.setText(list_foc.get(x).getQty());
            holder.discription.setText(list_foc.get(x).getDescription());

           // holder.minus.setVisibility(View.VISIBLE);


                if (list_foc.size()== 1) {

                    holder.minus.setVisibility(View.GONE);

                } else {
                    holder.minus.setVisibility(View.VISIBLE);

                }


                //   holder.minus.setVisibility(View.VISIBLE);

        }
        else{
            holder.step.setText(null);
            holder.step.requestFocus();
        }


        holder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position1, long id) {

                holder.discription.setText(foc_list_models.get(position1).getDiscription());


               // Toast.makeText(context, ""+Spinner_values, Toast.LENGTH_SHORT).show();

                str_item = (holder.spinner.getSelectedItem().toString());
                item.add(str_item);
                list_foc.get(possition).setSelectedPosition(holder.spinner.getSelectedItemPosition());
                list_foc.get(possition).setSelectedItem(holder.spinner.getSelectedItem().toString());
                list_foc.get(possition).setDescription(holder.discription.getText().toString());

            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (list_foc.size()< Foc_NO.size()-1) {
                    try {
                        if (holder.discription.getText().toString().equalsIgnoreCase("") ) {
                            Toast.makeText(context, "please enter Description", Toast.LENGTH_SHORT).show();
                        }
                        else if (holder.step.getText().toString().equalsIgnoreCase("") ) {

                            Toast.makeText(context, "please enter QTY", Toast.LENGTH_SHORT).show();


                        }
                        else {

                            holder.minus.setVisibility(View.VISIBLE);

                            //    dataAdapter.remove((String)spinner.getSelectedItem());
                            list_foc.add(possition + 1, new ModelClassForSavedData("", "", ""));
                            notifyItemInserted(possition + 1);

                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                        e.printStackTrace();
                    }
                }
            }
        });



           holder.minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   // int position = getAdapterPosition();
                    try {
                        holder.step.clearFocus();
                        holder.discription.clearFocus();
                        list_foc.remove(possition);
                        notifyItemRemoved(possition);
                        notifyDataSetChanged();

                    /*    if (list_foc.size()== 1) {
                            // notifyDataSetChanged();
                            holder.minus.setVisibility(View.GONE);

                        } else {
                            holder.minus.setVisibility(View.VISIBLE);

                        }*/


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });






        /*if(list_foc.get(x).length() > 0) {
            holder.step.setText(list_foc.get(x));
        }
        else{
            holder.step.setText(null);
            holder.step.requestFocus();
        }

    }


    public ArrayList<String> getStepList(){
        return list_foc;
    }*/

    }


    public ArrayList<ModelClassForSavedData> getStepList(){
        return list_foc;
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


