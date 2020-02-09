package com.neuman.brutus.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.neuman.brutus.R;

import java.util.ArrayList;


public class SupplyListAdapter extends BaseAdapter {

    Context context;
    private final ArrayList<String> values;
    private final ArrayList<String> numbers;
    private final ArrayList<String> images;

    public SupplyListAdapter(Context context, ArrayList<String> values, ArrayList<String> numbers, ArrayList<String> images){
        this.context = context;
        this.values = values;
        this.numbers = numbers;
        this.images = images;
    }

    @Override
    public int getCount() {
        return values.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ViewHolder viewHolder;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.item_supplies, parent, false);

            viewHolder.txtName = convertView.findViewById(R.id.aNametxt);
            viewHolder.txtVersion = convertView.findViewById(R.id.aVersiontxt);
            viewHolder.icon = convertView.findViewById(R.id.appIconIV);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.txtName.setText(values.get(position));
        viewHolder.txtVersion.setText(numbers.get(position));

        if (images.get(position)==null) {
            Glide.with(parent.getContext()).load(R.drawable.placeholder).error(R.drawable.placeholder).into(viewHolder.icon);
        } else {
            Glide.with(parent.getContext()).load(images.get(position)).error(R.drawable.placeholder).into(viewHolder.icon);
        }

        return convertView;
    }

    private static class ViewHolder {
        TextView txtName;
        TextView txtVersion;
        ImageView icon;
    }
}