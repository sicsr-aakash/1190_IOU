package com.project.iou;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomAdapter extends ArrayAdapter<String> {
	
	private final Context context;
	private final String[] names;
	private final String[] amount;
	private final Integer[] image_id;
	

	public CustomAdapter(Context context, String[] names, String[] amount, Integer[] image_id) {
		super(context, R.layout.activity_list_item, names);
	    this.context = context;
	    this.names = names;
	    this.amount = amount;
	    this.image_id = image_id;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	  
	    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    
	    View rowView = inflater.inflate(R.layout.activity_list_item, null, true);
	    
	    TextView firstLine = (TextView) rowView.findViewById(R.id.firstLine);
	    TextView secondLine = (TextView) rowView.findViewById(R.id.secondLine);
	    ImageView icon = (ImageView) rowView.findViewById(R.id.itemIcon);
	    
	    firstLine.setText(names[position]);
	    secondLine.setText(amount[position]);
	    icon.setImageResource(image_id[position]);
    
		return rowView;
	}
}