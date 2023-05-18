package com.example.yorktheatre;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.yorktheatre.Performance;
import com.example.yorktheatre.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PerformanceAdapter extends BaseAdapter {
    private Context context;
    private static List<Performance> performanceList;

    public PerformanceAdapter(Context context, List<Performance> performanceList) {
        this.context = context;
        this.performanceList = performanceList;
    }

    @Override
    public int getCount() {
        return performanceList.size();
    }

    @Override
    public Object getItem(int position) {
        return performanceList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.performance_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.nameTextView = convertView.findViewById(R.id.performanceNameTextView);
            viewHolder.imageView = convertView.findViewById(R.id.performanceImageView);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Performance performance = performanceList.get(position);
        viewHolder.nameTextView.setText(performance.getName());
        Picasso.get().load(performance.getImage()).into(viewHolder.imageView);

        return convertView;
    }


    private static class ViewHolder {
        TextView nameTextView;
        ImageView imageView;
    }

}
