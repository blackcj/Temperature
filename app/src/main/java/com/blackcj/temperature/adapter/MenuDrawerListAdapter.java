package com.blackcj.temperature.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.blackcj.temperature.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * List adapter used to inflate a custom view for the menu items. Items contain an image and text.
 *
 * @author Chris Black (blackcj2@gmail.com)
 */
public class MenuDrawerListAdapter extends ArrayAdapter<String>{

    private final Integer[] mIcons;
    private final Context mContext;

    public MenuDrawerListAdapter(Context context, int textViewResourceId, String[] objects, Integer[] icons) {
        super(context, textViewResourceId, objects);
        mContext = context;
        mIcons = icons;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder;
        if (view != null) {
            holder = (ViewHolder) view.getTag();
        } else {
            view = LayoutInflater.from(mContext).inflate(R.layout.lv_drawer_row, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }

        holder.name.setText(getItem(position));
        int imageResource = mIcons[position];
        holder.icon.setImageResource(imageResource);
        return view;
    }

    static class ViewHolder {
        @InjectView(R.id.drawer_row_text) TextView name;
        @InjectView(R.id.drawer_row_icon) ImageView icon;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }

}