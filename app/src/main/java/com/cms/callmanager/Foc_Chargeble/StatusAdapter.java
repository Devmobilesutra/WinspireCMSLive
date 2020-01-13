package com.cms.callmanager.Foc_Chargeble;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.cms.callmanager.R;
import com.cms.callmanager.RepairDetailsActivity;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class StatusAdapter extends BaseAdapter implements SpinnerAdapter {
    ArrayList<String> brands;
    Context context;

    public StatusAdapter(Context context, ArrayList<String> brands) {
        this.brands = brands;
        this.context = context;
    }

    public static void hideSpinnerDropDown(Spinner spinner) {
        try {
            Method method = Spinner.class.getDeclaredMethod("onDetachedFromWindow");
            method.setAccessible(true);
            method.invoke(spinner);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getCount() {
        return brands.size();
    }

    @Override
    public Object getItem(int position) {
        return brands.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view =  View.inflate(context, R.layout.status_dropdown, null);
        TextView textView = (TextView) view.findViewById(R.id.TV_status);
        textView.setText(brands.get(position));
        return textView;
    }

    @Override
    public View getDropDownView(final int position, View convertView, ViewGroup parent) {

        View view;
        view =  View.inflate(context, R.layout.status_dropdown1, null);
        final TextView textView = (TextView) view.findViewById(R.id.status_id);
        textView.setText(brands.get(position));

        final int positionss = position;

        if(RepairDetailsActivity.selectedColor == positionss){
            textView.setTextColor(Color.parseColor("#FFAEADAD"));
        }

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(RepairDetailsActivity.selectedColor == positionss){

                }else {
                 //   RepairDetailsActivity.statusSpinnerCategory.setSelection(positionss);
                   // hideSpinnerDropDown(RepairDetailsActivity.statusSpinnerCategory);
                }
            }
        });


        return view;
    }
}
