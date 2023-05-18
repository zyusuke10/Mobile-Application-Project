package com.example.yorktheatre;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;

import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.List;

public class PerformanceDetail extends AppCompatActivity {
    private DBHelper databaseHelper;
    private SessionManager sessionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_performance_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        databaseHelper = new DBHelper(this);
        sessionManager = new SessionManager(this);
        Intent intent = getIntent();
        Performance performance = (Performance) intent.getSerializableExtra("performance_detail");

        ImageView imageUrl = findViewById(R.id.imageView);
        TextView titleTextView = findViewById(R.id.title_p);
        TextView locationTextView = findViewById(R.id.location);
        TextView timeTextView = findViewById(R.id.time);
        TextView accessibilityTextView = findViewById(R.id.accessibility);
        TextView priceTextView = findViewById(R.id.price);
        TextView ticketQtyTextView = findViewById(R.id.ticket_qty);
        Button bookBtn = findViewById(R.id.bookBtn);
        Spinner quantitySpinner = findViewById(R.id.quantitySpinner);

        List<String> quantityList = Arrays.asList("1", "2", "3", "4", "5","6","7","8","9","10");
        QuantityAdapter quantityAdapter = new QuantityAdapter(this, quantityList);
        quantitySpinner.setAdapter(quantityAdapter);

        if (performance != null) {
            Picasso.get().load(performance.getImage()).into(imageUrl);
            titleTextView.setText(performance.getName());
            locationTextView.setText(performance.getLocation());
            timeTextView.setText(performance.getTime());
            accessibilityTextView.setText(performance.getAccessibility());
            priceTextView.setText("£" + performance.getPrice());
            ticketQtyTextView.setText("(" + performance.getTicketQuantity() + " " + "more tickets left" + ")");
        }


        bookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedQuantity = Integer.parseInt(quantitySpinner.getSelectedItem().toString());
                showConfirmationDialog(selectedQuantity);
            }
        });

        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.quantity_values, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        quantitySpinner.setAdapter(spinnerAdapter);
    }

    private void bookPerformance(int ticket_quantity) {
        Performance performance = (Performance) getIntent().getSerializableExtra("performance_detail");

        int ticket_id = performance.getTicket_id();
        int user_id = sessionManager.getUserId();
        int ticket_qty = ticket_quantity;

        if (ticket_qty > performance.getTicketQuantity()) {
            Toast toast = Toast.makeText(this, "Insufficient tickets available.", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
            return;
        }

        Boolean checkBooking = databaseHelper.bookPerformance(this, user_id, ticket_id, ticket_qty);

        if (checkBooking) {
            Toast toast = Toast.makeText(this, "Booked Successfully", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();

            Intent intentToBooking = new Intent(PerformanceDetail.this, MyBooking.class);
            startActivity(intentToBooking);
        } else {
            Toast toast = Toast.makeText(this, "Error occurred", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
        }
    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) { // Handle the arrow button press
            onBackPressed(); // Go back to the previous activity
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showConfirmationDialog(final int ticketQuantity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmation");
        builder.setMessage("Are you sure you want to book " + ticketQuantity + " ticket(s) for this performance?");
        builder.setPositiveButton("Book", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                bookPerformance(ticketQuantity);
            }
        });
        builder.setNegativeButton("Cancel", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}