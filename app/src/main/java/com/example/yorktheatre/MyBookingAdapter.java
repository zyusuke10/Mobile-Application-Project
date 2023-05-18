package com.example.yorktheatre;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MyBookingAdapter extends BaseAdapter {
    private Context context;
    private List<BookingItem> bookingItemList;

    private String image;
    private OnCancelClickListener onCancelClickListener;

    public MyBookingAdapter(Context context, List<BookingItem> bookingItemList) {
        this.context = context;
        this.bookingItemList = bookingItemList;
    }

    @Override
    public int getCount() {
        if (bookingItemList.isEmpty()) {
            return 1;
        } else {
            return bookingItemList.size();
        }
    }

    @Override
    public Object getItem(int position) {
        return bookingItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.booking_item, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.bookingTitleTextView = convertView.findViewById(R.id.booking_title);
            viewHolder.imageView = convertView.findViewById(R.id.booking_image);
            viewHolder.cancelBtn = convertView.findViewById(R.id.cancelBtn);
            viewHolder.confirmBtn = convertView.findViewById(R.id.confirmationBtn);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (bookingItemList.isEmpty()) {
            convertView = LayoutInflater.from(context).inflate(R.layout.no_bookings_item, parent, false);
            TextView noBookingsTextView = convertView.findViewById(R.id.noBookingsTextView);
            noBookingsTextView.setText("No bookings available");
        } else {
            BookingItem bookingItem = bookingItemList.get(position);
            viewHolder.bookingTitleTextView.setText(bookingItem.getTitle());
            Picasso.get().load(bookingItem.getImage()).into(viewHolder.imageView);

            viewHolder.cancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onCancelClickListener != null) {
                        onCancelClickListener.onCancelClick(position);
                    }
                }
            });

            viewHolder.confirmBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    Intent intent = new Intent(context, Summary.class);
                    intent.putExtra("bookingItem_detail", bookingItem);
                    context.startActivity(intent);
                }
            });
        }

        return convertView;
    }

    private static class ViewHolder {
        Button cancelBtn;
        Button confirmBtn;
        ImageView imageView;
        TextView bookingTitleTextView;
    }

    public interface OnCancelClickListener {
        void onCancelClick(int bookingId);
    }

    public void setOnCancelClickListener(OnCancelClickListener listener) {
        onCancelClickListener = listener;
    }
}
