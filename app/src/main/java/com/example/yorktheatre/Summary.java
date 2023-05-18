package com.example.yorktheatre;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Summary extends AppCompatActivity {

    private TextView performance_name;
    private TextView performance_time;
    private TextView ticket_qty;
    private TextView total_price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        performance_name = findViewById(R.id.p_name);
        performance_time = findViewById(R.id.p_time);
        ticket_qty = findViewById(R.id.ticket_qty);
        total_price = findViewById(R.id.total_price);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        BookingItem bookingItem = (BookingItem) intent.getSerializableExtra("bookingItem_detail");
        int selected_quantity = bookingItem.getSelectedQuantity();
        if (bookingItem != null) {
            performance_name.setText(bookingItem.getTitle());
            performance_time.setText(bookingItem.getTime());
            ticket_qty.setText("Ticket quantity: " + selected_quantity);
            total_price.setText("£" + String.format("%.2f", bookingItem.getPrice() * selected_quantity));
        }
    }
}
