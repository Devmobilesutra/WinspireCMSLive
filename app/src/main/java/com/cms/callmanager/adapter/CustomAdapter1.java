package com.cms.callmanager.adapter;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cms.callmanager.R;
import com.cms.callmanager.dto.CodeAndNameModel;

import java.util.ArrayList;

/**
 * Created by Bhavesh Chaudhari on 22-May-19.
 */

public class CustomAdapter1 extends ArrayAdapter<CodeAndNameModel>{

        private ArrayList<CodeAndNameModel> dataSet;
        Context mContext;

        // View lookup cache
        private static class ViewHolder {
            TextView txtName;
        }



    public CustomAdapter1(ArrayList<CodeAndNameModel> data, Context context) {
            super(context, R.layout.row_item1, data);
            this.dataSet = data;
            this.mContext=context;

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
            CodeAndNameModel dataModel = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            CustomAdapter1.ViewHolder viewHolder; // view lookup cache stored in tag

            final View result;

            if (convertView == null) {


                viewHolder = new CustomAdapter1.ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.row_item1, parent, false);
                viewHolder.txtName = (TextView) convertView.findViewById(R.id.name);


                result=convertView;

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (CustomAdapter1.ViewHolder) convertView.getTag();
                result=convertView;
            }

           /* Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
            result.startAnimation(animation);
            lastPosition = position;*/


            viewHolder.txtName.setText(dataModel.getName());
           // viewHolder.txtName.setOnClickListener(this);
            viewHolder.txtName.setTag(position);
            // Return the completed view to render on screen
            return convertView;
        }

    }
