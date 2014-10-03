package com.blackcj.temperature.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.blackcj.temperature.R;

public class MenuDrawerListAdapter extends ArrayAdapter<String>{

    private Integer[] mIcons;
    private Context mContext;

    public MenuDrawerListAdapter(Context context, int textViewResourceId, String[] objects, Integer[] icons) {
        super(context, textViewResourceId, objects);
        mContext = context;
        mIcons = icons;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	View row = LayoutInflater.from(mContext).inflate(R.layout.lv_drawer_row, parent, false);
        TextView label = (TextView) row.findViewById(R.id.drawer_row_text);
        label.setText(getItem(position));

        ImageView icon = (ImageView)row.findViewById(R.id.drawer_row_icon);

        int imageResource = mIcons[position];
        icon.setImageResource(imageResource);

        return row;
    }

}