package com.cms.callmanager.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cms.callmanager.MapsActivity;
import com.cms.callmanager.R;
import com.cms.callmanager.dto.ATMDetailsDTO;
import com.cms.callmanager.utils.Utils;

import java.util.ArrayList;

/**
 * Created by Monika on 1/8/17.
 */
public class ATMListAdapter extends RecyclerView.Adapter<ATMListAdapter.ViewHolder> {
	private ArrayList<ATMDetailsDTO> nearestATMList;
	Context context;

	// Provide a reference to the views for each data item
	// Complex data items may need more than one view per item, and
	// you provide access to all the views for a data item in a view holder
	public static class ViewHolder extends RecyclerView.ViewHolder {
		// each data item is just a string in this case
		public TextView nameText;
		public TextView bankNameTxt;
		public TextView distanceTxt;
		public ViewHolder(View v) {
			super(v);
			nameText = (TextView) v.findViewById(R.id.ATMName);
			bankNameTxt = (TextView) v.findViewById(
					R.id.bankName);
			distanceTxt = (TextView) v.findViewById(R.id.distance);
		}
	}

	// Provide a suitable constructor (depends on the kind of dataset)
	public ATMListAdapter(ArrayList<ATMDetailsDTO> atmDetailsDTOs, Context context) {
		Utils.Log("SIze in adapter==="+atmDetailsDTOs.size());
		this.nearestATMList = atmDetailsDTOs;
		this.context = context;
	}

	// Create new views (invoked by the layout manager)
	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent,
														int viewType) {
		// create a new view
		View v = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.layout_nearest_atm, parent, false);
		// set the view's size, margins, paddings and layout parameters

		ViewHolder vh = new ViewHolder(v);
		return vh;
	}

	// Replace the contents of a view (invoked by the layout manager)
	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		// - get element from your dataset at this position
		// - replace the contents of the view with that element
		Utils.Log("Position=="+position);
		Utils.Log("nearesst atm lidt : =" + nearestATMList.size());

		ATMDetailsDTO atmDetailsDTO = nearestATMList.get(position);
		Log.d("name=====" , atmDetailsDTO.getAtmID().toString());
		holder.nameText.setText(atmDetailsDTO.getAtmID().toString());
		holder.bankNameTxt.setText(atmDetailsDTO.getBankName().toString());
		holder.distanceTxt.setText(atmDetailsDTO.getDistance().toString()+"KM");
		Utils.SetDefautltFont(holder.nameText, "Bold", context);
		Utils.SetDefautltFont(holder.bankNameTxt,"Regular",context);
		Utils.SetDefautltFont(holder.distanceTxt,"Regular",context);

		holder.itemView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent atmIntent = new Intent(context , MapsActivity.class);
				atmIntent.putExtra("atms",nearestATMList);
				context.startActivity(atmIntent);
			}
		});
	}


	// Return the size of your dataset (invoked by the layout manager)
	@Override
	public int getItemCount() {
		return nearestATMList.size();
	}


}


