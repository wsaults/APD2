package com.fullsail.couple;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
 
public class ListViewAdapter extends BaseAdapter {
 
    Context context;
    String[] message;
    String[] time;
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
        TextView txtMessage;
        TextView txtTime;
 
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
 
        View itemView = inflater.inflate(R.layout.listview_item, parent, false);
 
        // Locate the TextViews in listview_item.xml
        txtMessage = (TextView) itemView.findViewById(R.id.message);
        txtTime = (TextView) itemView.findViewById(R.id.time);
		
        txtMessage.setText(message[position]);
        txtTime.setText(time[position]);
 
        return itemView;
    }
}
