package com.fullsail.couple;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
//import android.widget.GridView;
//import android.widget.ImageView;

public class GridViewAdapter extends BaseAdapter{
	
	Context context;
    LayoutInflater inflater;
    ArrayList<Bitmap> imageResource;
	
	public GridViewAdapter(Context context, ArrayList<Bitmap> imageResource) {
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
	public View getView(int position, View convertView, ViewGroup parent) {		
		// Declare Variables
//    	ImageView imageView;
 
//        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
 
        View gridView = inflater.inflate(R.layout.gridview_item, parent, false);
     // Instance of ImageAdapter Class
 
//        // Locate the TextViews in listview_item.xml
//        imageView = (ImageView) gridView.findViewById(R.id.imageView);
//        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//        imageView.setPadding(8, 8, 8, 8);
//        imageView.setImageResource(imageResource[position]);
 
        return gridView;
	}
}
