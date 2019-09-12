package com.cms.callmanager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cms.callmanager.R;
import com.cms.callmanager.dto.ProblemFixDTO;
import com.cms.callmanager.dto.ResonseCategeoryDTO;
import com.cms.callmanager.dto.SolutionDTO;

import java.util.List;

public class CustomeSpinnerAdapter<T> extends BaseAdapter {
    Context context;
    List<T> modelDto;
    int flag;

    public CustomeSpinnerAdapter(Context context, List<T> modelDto, int flag) {
        this.context = context;
        this.modelDto = modelDto;
        this.flag = flag;
    }

    @Override
    public int getCount() {
        return modelDto.size();
    }

    @Override
    public T getItem(int i) {
        return this.modelDto.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {

        View view = null;

        if (convertView != null) {
            view = convertView;
        } else {
            view = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.item_type_list, null);
        }
        TextView name = (TextView) view.findViewById(R.id.txtAdapterName);
        String strName = "";
        if (flag == 0) {
            ResonseCategeoryDTO.PayLoad payLoad = (ResonseCategeoryDTO.PayLoad) modelDto.get(i);
            name.setText(payLoad.getName());
        } else if (flag == 1) {
            SolutionDTO.PayLoad payLoad = (SolutionDTO.PayLoad) modelDto.get(i);
            name.setText(payLoad.getName());
        } else if (flag == 2) {
            ProblemFixDTO.PayLoad payLoad = (ProblemFixDTO.PayLoad) modelDto.get(i);
            name.setText(payLoad.getName());
        }
        return view;
    }
}