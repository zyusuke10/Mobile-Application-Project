package com.example.yorktheatre;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class QuantityAdapter extends ArrayAdapter<String> {
    private Context context;
    private List<String> quantityList;
    private int selectedQuantity;

    public QuantityAdapter(Context context, List<String> quantityList) {
        super(context, 0, quantityList);
        this.context = context;
        this.quantityList = quantityList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(android.R.layout.simple_spinner_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.quantityTextView = convertView.findViewById(android.R.id.text1);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        String quantity = quantityList.get(position);
        viewHolder.quantityTextView.setText(quantity);

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        // Customize the appearance of the dropdown view if needed
        return getView(position, convertView, parent);
    }

    public void setSelectedQuantity(int selectedQuantity) {
        this.selectedQuantity = selectedQuantity;
    }

    private static class ViewHolder {
        TextView quantityTextView;
    }
}