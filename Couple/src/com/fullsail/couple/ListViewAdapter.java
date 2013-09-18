package com.fullsail.couple;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
 
public class ListViewAdapter extends BaseAdapter {
 
    // Declare Variables
    Context context;
    String[] message;
    String[] time;
    int[] flag;
    LayoutInflater inflater;
 
    public ListViewAdapter(Context context, String[] message, String[] time) {
        this.context = context;
        this.message = message;
        this.time = time;
    }
 
    public int getCount() {
        return message.length;
    }
 
    public Object getItem(int position) {
        return null;
    }
 
    public long getItemId(int position) {
        return 0;
    }
 
    public View getView(int position, View convertView, ViewGroup parent) {
 
        // Declare Variables
        TextView txtrank;
        TextView txtcountry;
        TextView txtpopulation;
 
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
 
        View itemView = inflater.inflate(R.layout.listview_item, parent, false);
 
        // Locate the TextViews in listview_item.xml
        txtrank = (TextView) itemView.findViewById(R.id.message);
        txtcountry = (TextView) itemView.findViewById(R.id.time);
 
        // Capture position and set to the TextViews
        txtrank.setText(message[position]);
        txtcountry.setText(time[position]);
 
        return itemView;
    }
}
