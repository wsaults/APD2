package com.fullsail.couple;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
 
public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    ArrayList<Bitmap> bitmapArray;
 
    // Constructor
    public ImageAdapter(Context c, ArrayList<Bitmap> imageResource){
        mContext = c;
        this.bitmapArray = imageResource;
    }
 
    @Override
    public int getCount() {
        return bitmapArray.size();
    }
 
    @Override
    public Object getItem(int position) {
        return bitmapArray.get(position);
    }
 
    @Override
    public long getItemId(int position) {
        return 0;
    }
 
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView = new ImageView(mContext);
        imageView.setImageBitmap(bitmapArray.get(position));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(new GridView.LayoutParams(210, 210));
        return imageView;
    }
 
}
