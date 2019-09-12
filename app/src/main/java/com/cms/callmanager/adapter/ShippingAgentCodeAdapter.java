package com.cms.callmanager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.cms.callmanager.R;
import com.cms.callmanager.model.ShippingAgentCode;

import java.util.ArrayList;

/**
 * Created by Bhavesh Chaudhari on 01-Jun-19.
 */

public class ShippingAgentCodeAdapter extends ArrayAdapter<ShippingAgentCode> {

    private ArrayList<ShippingAgentCode> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView txtName;
    }


    public ShippingAgentCodeAdapter(ArrayList<ShippingAgentCode> data, Context context) {
        super(context, R.layout.row_item1, data);
        this.dataSet = data;
        this.mContext = context;

    }


      /*  @Override
        public void onClick(View v) {
            int position=(Integer) v.getTag();
            Object object= getItem(position);
            CodeAndNameModel dataModel=(CodeAndNameModel)object;

            Toast.makeText(mContext, dataModel.getName(), Toast.LENGTH_SHORT).show();

        }*/

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        ShippingAgentCode dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {


            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_item1, parent, false);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.name);


            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }


        viewHolder.txtName.setText(dataModel.getName());
        // viewHolder.txtName.setOnClickListener(this);
        viewHolder.txtName.setTag(position);
        // Return the completed view to render on screen
        return convertView;
    }
}