package com.example.yorktheatre;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {
    private SessionManager sessionManager;
    private DBHelper databaseHelper;
    private GridView performanceGrid;
    private PerformanceAdapter performanceAdapter;
    private List<Performance> performanceList;
    String[] items = {"Wheelchair access", "flashing lights"};
    AutoCompleteTextView autoCompleteTxt;
    TextInputEditText dataInputEditText;
    ArrayAdapter<String> adapterItems;

    Button resetBtn;

    Button filterBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sessionManager = new SessionManager(this);
        databaseHelper = new DBHelper(this);
        TextInputLayout accessibilityInput = findViewById(R.id.accessibilityInput);
        autoCompleteTxt = findViewById(R.id.auto_complete_txt);
        dataInputEditText = findViewById(R.id.dateInputEditText);
        performanceGrid = findViewById(R.id.performanceGridView);
        resetBtn = findViewById(R.id.resetBtn);
        filterBtn = findViewById(R.id.filterBtn);

        if (!sessionManager.isSessionActive()) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        adapterItems = new ArrayAdapter<>(this, R.layout.selector_item,items);
        autoCompleteTxt.setAdapter(adapterItems);
        autoCompleteTxt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                accessibilityInput.setHint("");
            }
        });

        performanceList = getPerformances();
        performanceAdapter = new PerformanceAdapter(this, performanceList);
        performanceGrid.setAdapter(performanceAdapter);
        performanceGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Performance performance = performanceList.get(position);
                Intent intent = new Intent(MainActivity.this,PerformanceDetail.class);
                intent.putExtra("performance_detail", performance);
                startActivity(intent);
            }
        });

        filterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterPerformances();
            }
        });

        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetFilters();
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.booking) {
            Intent intent = new Intent(this,MyBooking.class);
            startActivity(intent);
            return true;
        } else if (itemId == R.id.logout) {
            logoutUser();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logoutUser() {
        sessionManager.endSession(); // End the session
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    private List<Performance> getPerformances() {
        databaseHelper = new DBHelper(this);
        return databaseHelper.getPerformance();
    }

    private void filterPerformances() {
        String dateInput = dataInputEditText.getText().toString();
        String accessibility = autoCompleteTxt.getText().toString();

        if (dateInput.equals("") && accessibility.equals("")) {
            performanceList = databaseHelper.getPerformance();
            performanceAdapter = new PerformanceAdapter(this, performanceList);
            performanceGrid.setAdapter(performanceAdapter);
        } else if (dateInput.equals("")) {
            List<Performance> filteredPerformances = databaseHelper.filterPerformance(0, 0, 0, accessibility);
            performanceList = filteredPerformances; // Update class-level variable
            performanceAdapter = new PerformanceAdapter(this, performanceList);
            performanceGrid.setAdapter(performanceAdapter);
            performanceAdapter.notifyDataSetChanged();
        } else if (accessibility.equals("")) {
            if (isValidDate(dateInput)) {
                int year, month, day;
                try {
                    String[] parts = dateInput.split("/");
                    day = Integer.parseInt(parts[0]);
                    month = Integer.parseInt(parts[1]);
                    year = Integer.parseInt(parts[2]);

                    List<Performance> filteredPerformances = databaseHelper.filterPerformance(year, month, day, "");
                    performanceList = filteredPerformances; // Update class-level variable
                    performanceAdapter = new PerformanceAdapter(this, performanceList);
                    performanceGrid.setAdapter(performanceAdapter);
                    performanceAdapter.notifyDataSetChanged();
                } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                    Toast toast = Toast.makeText(this, "Invalid date format. Please enter a valid date (dd/MM/yyyy)", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();
                    return;
                }
            }
        } else {
            if (isValidDate(dateInput)) {
                int year, month, day;
                try {
                    String[] parts = dateInput.split("/");
                    day = Integer.parseInt(parts[0]);
                    month = Integer.parseInt(parts[1]);
                    year = Integer.parseInt(parts[2]);

                    List<Performance> filteredPerformances = databaseHelper.filterPerformance(year, month, day, accessibility);
                    performanceList = filteredPerformances; // Update class-level variable
                    performanceAdapter = new PerformanceAdapter(this, performanceList);
                    performanceGrid.setAdapter(performanceAdapter);
                    performanceAdapter.notifyDataSetChanged();
                } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                    Toast toast = Toast.makeText(this, "Invalid date format. Please enter a valid date (dd/MM/yyyy)", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();
                    return;
                }
            }
        }
    }

    private boolean isValidDate(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.UK);
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    private void resetFilters() {
        dataInputEditText.setText("");
        autoCompleteTxt.setText("");

        performanceList = databaseHelper.getPerformance();
        performanceAdapter = new PerformanceAdapter(this, performanceList);
        performanceGrid.setAdapter(performanceAdapter);
    }
}