package com.example.amar.mycar;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Amar on 9/27/16.
 */
public class ListArrayAdapter extends ArrayAdapter<list_item_BT_Device> {

    ArrayList<list_item_BT_Device> bt_devices;
    Context mContext;

    public ListArrayAdapter(Context context, int resource, ArrayList<list_item_BT_Device> bt_devices) {
        super(context, resource, bt_devices);
        this.mContext = context;
        this.bt_devices = bt_devices;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = View.inflate(mContext, R.layout.list_item_bt_device, null);
        }

        TextView name = (TextView) convertView.findViewById(R.id.device_name);
        name.setText(bt_devices.get(position).name);
        return convertView;
    }
}
