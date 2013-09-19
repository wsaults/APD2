package com.fullsail.couple;

import java.net.URI;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GridViewAdapter extends BaseAdapter{
	
	Context context;
    int[] imageResource;
    LayoutInflater inflater;
	
	public GridViewAdapter(Context context, int[] imageResource) {
        this.context = context;
        this.imageResource = imageResource;
    }

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		
		// Declare Variables
    	ImageView imageView;
 
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
 
        View gridView = inflater.inflate(R.layout.gridview_item, arg2, false);
 
        // Locate the TextViews in listview_item.xml
        imageView = (ImageView) gridView.findViewById(R.id.imageView);
		
        imageView.setImageResource(imageResource[arg0]);
 
        return gridView;
	}
}
