package com.itcfox.Hafil2_gard;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class GardAdapter extends ArrayAdapter<GardHelper>{


	private final Activity activity;
	List<GardHelper> fleetNumber = new ArrayList<GardHelper>();
	List<GardHelper> gateName = new ArrayList<GardHelper>();
	List<GardHelper> type = new ArrayList<GardHelper>();
	List<GardHelper> date = new ArrayList<GardHelper>();

	Context context;

	public GardAdapter(Activity activity, Context context, List<GardHelper> fleetNumber, List<GardHelper> gateName, List<GardHelper> type, List<GardHelper> date){
		super(activity, R.layout.maingard,fleetNumber);
		this.activity=activity;
        this.context=context;
		this.fleetNumber=fleetNumber;
		this.gateName=gateName;
		this.type=type;
		this.date=date;
	}
	

        
	class ViewHolder {
		protected TextView fleetNumberView;
		protected TextView gateNameView;
		protected TextView typeView;
        protected TextView  dateView;
		protected TableRow row;
		
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

        View view = null;

        if(convertView == null) {

            LayoutInflater inflator = activity.getLayoutInflater();
            view = inflator.inflate(R.layout.repgard, null);
            ViewHolder viewHolder = new ViewHolder();


            viewHolder.fleetNumberView = (TextView) view.findViewById(R.id.TextView1);

            viewHolder.gateNameView = (TextView) view.findViewById(R.id.textView3);

            viewHolder.typeView = (TextView) view
                    .findViewById(R.id.textView2);

            viewHolder.dateView = (TextView) view
                    .findViewById(R.id.textView4);


            view.setTag(viewHolder);


        } else {
            view = convertView;


            //((ViewHolder) view.getTag()).row.setTag(busIDs
            //	.get(position));

        }


        final ViewHolder holder = (ViewHolder) view.getTag();
		/*
		holder.row.setOnClickListener(new OnClickListener() {
			
		       @Override
		        public void onClick(View v) {
		            // TODO: do your logic here

		        }   
		});*/


       // holder.fleetNumberView.setText(fleetNumber.get(position).getDate());
       // holder.gateNameView.setText(gateName.get(position).getDate());


        if(gateName.get(position).getDate().equals("1")) {
            holder.gateNameView.setText("المطار");
        } else if(gateName.get(position).getDate().equals("2")) {
            holder.gateNameView.setText("الشرائع");
        }
        else if(gateName.get(position).getDate().equals("3")){
                holder.gateNameView.setText("الترددية");
            }

//--------------------------------------

        holder.dateView.setText(date.get(position).getDate());
//------------------------
        if(type.get(position).getDate().equals("0")) {
            holder.typeView.setText("دخول");
        } else if(type.get(position).getDate().equals("1")) {
            holder.typeView.setText("خروج");
    }
//---------------------------------------------
        if(fleetNumber.get(position).getDate().equals("0")) {
            holder.fleetNumberView.setText("NA");
        }
        else {
            holder.fleetNumberView.setText(fleetNumber.get(position).getDate());
        }


		return view;
}

}


