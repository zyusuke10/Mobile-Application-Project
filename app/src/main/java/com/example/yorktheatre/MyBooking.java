package com.example.yorktheatre;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.GridView;
import android.widget.Toast;

import java.util.List;

public class MyBooking extends AppCompatActivity {

    private GridView myBookingGridView;
    private MyBookingAdapter adapter;
    private DBHelper databaseHelper;
    private List<BookingItem> yourBookingData;
    private SessionManager sessionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_booking);
        // Initialize the GridView
        myBookingGridView = findViewById(R.id.myBookingGridView);
        sessionManager = new SessionManager(this);
        int user_id = sessionManager.getUserId();
        databaseHelper = new DBHelper(this);
        yourBookingData = databaseHelper.getBookingData(user_id);
        adapter = new MyBookingAdapter(this, yourBookingData);
        myBookingGridView.setAdapter(adapter);


        adapter.setOnCancelClickListener(new MyBookingAdapter.OnCancelClickListener() {
            @Override
            public void onCancelClick(int position) {
                showCancellationConfirmationDialog(position);
            }
        });
    }

    private void showCancellationConfirmationDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmation");
        builder.setMessage("Are you sure you want to cancel this booking?");
        builder.setPositiveButton("Cancel Booking", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                cancelBooking(position);
            }
        });
        builder.setNegativeButton("Keep Booking", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void cancelBooking(int position) {
        BookingItem bookingItem = yourBookingData.get(position);
        int bookingId = bookingItem.getId();
        boolean isCancelled = databaseHelper.cancelBooking(bookingId);
        if (isCancelled) {
            yourBookingData.remove(position);
            adapter.notifyDataSetChanged();
            Toast toast = Toast.makeText(MyBooking.this, "Booking cancelled", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
        } else {
            Toast toast = Toast.makeText(MyBooking.this, "Failed to cancel booking", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
        }
    }
}