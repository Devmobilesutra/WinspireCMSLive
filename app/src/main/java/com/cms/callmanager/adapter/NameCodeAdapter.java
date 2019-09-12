package com.cms.callmanager.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.cms.callmanager.R;
import com.cms.callmanager.dto.CodeAndNameModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bhavesh Chaudhari on 08-Jun-19.
 */

public class NameCodeAdapter extends ArrayAdapter<CodeAndNameModel> implements Filterable {

    private ArrayList<CodeAndNameModel> dataSet;
    Context mContext;
    private Filter planetFilter;
    private List<CodeAndNameModel> planetList;
    // View lookup cache
    private static class ViewHolder {
        TextView txtName;
    }



    public NameCodeAdapter(ArrayList<CodeAndNameModel> data, Context context) {
        super(context, R.layout.row_item1, data);
        this.planetList = data;
        this.mContext=context;
        this.dataSet = data;
    }


      /*  @Override
        public void onClick(View v) {
            int position=(Integer) v.getTag();
            Object object= getItem(position);
            CodeAndNameModel dataModel=(CodeAndNameModel)object;

            Toast.makeText(mContext, dataModel.getName(), Toast.LENGTH_SHORT).show();

        }*/

    private int lastPosition = -1;

    public int getCount() {
        return planetList.size();
    }


    public CodeAndNameModel getItem(int position) {
        return planetList.get(position);
    }

    public long getItemId(int position) {
        return planetList.get(position).hashCode();
    }


    @Override
    public Filter getFilter() {
        if (planetFilter == null)
            planetFilter = new PlanetFilter();

        return planetFilter;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        CodeAndNameModel dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        NameCodeAdapter.ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {


            viewHolder = new NameCodeAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_item1, parent, false);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.name);


            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (NameCodeAdapter.ViewHolder) convertView.getTag();
            result=convertView;
        }

           /* Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
            result.startAnimation(animation);
            lastPosition = position;*/



        viewHolder.txtName.setText(dataModel.getCode() +" - "+dataModel.getName());
        // viewHolder.txtName.setOnClickListener(this);
        viewHolder.txtName.setTag(position);
        // Return the completed view to render on screen
        return convertView;
    }

    public void resetData() {
        planetList = dataSet;
    }

    private class PlanetFilter extends Filter {



        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            // We implement here the filter logic


            if (constraint == null || constraint.length() == 0) {
                // No filter implemented we return all the list
                results.values = dataSet;
                results.count = dataSet.size();
            }
            else {
                // We perform filtering operation
                List<CodeAndNameModel> nPlanetList = new ArrayList<CodeAndNameModel>();

                for (CodeAndNameModel p : planetList) {

                    if (p.getName().toUpperCase().startsWith(constraint.toString().toUpperCase()) ||
                            p.getCode().toUpperCase().startsWith(constraint.toString().toUpperCase())
                           )
                    {
                        nPlanetList.add(p);
                    }
                }

                results.values = nPlanetList;
                results.count = nPlanetList.size();

            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {

            // Now we have to inform the adapter about the new list filtered
            /*if (results.count == 0)
                notifyDataSetInvalidated();
            else {
                planetList = (List<CodeAndNameModel>) results.values;
                Log.d("", "onTextChanged: "+results.values);
                notifyDataSetChanged();
            }*/

            planetList = (List<CodeAndNameModel>) results.values;
            dataSet.clear();
            dataSet.addAll(planetList);
            Log.d("", "onTextChanged: "+results.values);
            notifyDataSetChanged();

        }

    }

}