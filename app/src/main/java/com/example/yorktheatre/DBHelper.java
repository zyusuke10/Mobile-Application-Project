package com.example.yorktheatre;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;


import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper  {

    public static final String DBNAME = "york_theatre.db";
    public DBHelper(@Nullable Context context) {
        super(context,
                DBNAME, null, 13);
    }

    @Override
    public void onCreate(SQLiteDatabase MyDatabase) {
        try {
            MyDatabase.execSQL("CREATE TABLE IF NOT EXISTS users(id INTEGER PRIMARY KEY AUTOINCREMENT, email TEXT NOT NULL, password TEXT NOT NULL)");
            MyDatabase.execSQL("CREATE TABLE IF NOT EXISTS performance(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, image TEXT NOT NULL, location TEXT NOT NULL, time DATETIME NOT NULL, accessibility TEXT NOT NULL)");
            MyDatabase.execSQL("CREATE TABLE IF NOT EXISTS ticket(id INTEGER PRIMARY KEY AUTOINCREMENT, quantity INTEGER NOT NULL, price DECIMAL(10,2) NOT NULL, performance_id INTEGER, FOREIGN KEY(performance_id) REFERENCES performance(id))");
            MyDatabase.execSQL("CREATE TABLE IF NOT EXISTS booking(id INTEGER PRIMARY KEY AUTOINCREMENT, user_id INTEGER, ticket_id INTEGER, ticket_qty INTEGER, FOREIGN KEY(user_id) REFERENCES users(id), FOREIGN KEY(ticket_id) REFERENCES ticket(id))");


            MyDatabase.execSQL("INSERT INTO performance (name, image, location, time, accessibility) VALUES " +
                    "('The Merchant of Venice by William Shakespeare', 'https://m.media-amazon.com/images/M/MV5BY2NhZjIyMTMtYmExNi00YmZmLWJhYTItMzRkOTEzN2QxM2JlXkEyXkFqcGdeQXVyMTk0MjQ3Nzk@._V1_.jpg', 'Merchant Adventurer''s Hall, York', '2023-04-23 20:00:00', 'wheelchair access')," +
                    "('Hamlet by William Shakespeare', 'https://www.theoldglobe.org/link/eef7cd97e3644827b9cec622aa27b275.aspx?id=27296', 'Clifford''s Tower, York', '2023-05-19 18:00:00', 'flashing lights')," +
                    "('A Midsummer Night''s Dream by William Shakespeare', 'https://m.media-amazon.com/images/I/719Tm0izTYL._AC_SY679_.jpg', 'Dean''s Park, York', '2023-06-03 19:00:00', 'wheelchair access and flash lights')," +
                    "('Oedipus the King by Sophocles', 'https://www.benchtheatre.org.uk/plays90s/oedipuskingimage.jpg', 'St Mary''s Abbey, Museum Gardens, York', '2023-07-28 20:00:00', 'wheelchair access')," +
                    "('The Tempest by William Shakespeare', 'https://mdtheatreguide.com/wp-content/uploads/2014/12/temp.jpg', 'Milleneum Bridge, York', '2023-08-19 14:00:00', 'wheelchair access')," +
                    "('Antigone by Sophocles', 'https://assets.cambridge.org/97805211/34781/cover/9780521134781.jpg', 'Crypt, York Minster, York', '2023-09-20 21:00:00', 'Must be able to climb 20 steps')");

            MyDatabase.execSQL("INSERT INTO ticket (quantity, price, performance_id) VALUES " +
                    "(17, 8.00 ,(SELECT id FROM performance WHERE name = 'The Merchant of Venice by William Shakespeare'))," +
                    "(12, 8.00,(SELECT id FROM performance WHERE name = 'Hamlet by William Shakespeare'))," +
                    "(29, 4.00,(SELECT id FROM performance WHERE name = 'A Midsummer Night''s Dream by William Shakespeare'))," +
                    "(12, 9.00,(SELECT id FROM performance WHERE name = 'Oedipus the King by Sophocles'))," +
                    "(6, 7.00,(SELECT id FROM performance WHERE name = 'The Tempest by William Shakespeare'))," +
                    "(5, 16.00,(SELECT id FROM performance WHERE name = 'Antigone by Sophocles'))");


        } catch (SQLiteException e) {
            // Handle SQLite exception
            e.printStackTrace(); // Print the exception details for debugging purposes
        } catch (Exception e) {
            // Handle other exceptions
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase MyDatabase, int i, int i1) {
        MyDatabase.execSQL("drop Table if exists users");
        MyDatabase.execSQL("drop Table if exists performance");
        MyDatabase.execSQL("drop Table if exists ticket");
        MyDatabase.execSQL("drop Table if exists booking");
        onCreate(MyDatabase);
    }


    public boolean registerUser(String email, String password ){
        SQLiteDatabase MyDataBase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("email", email);
        contentValues.put("password", password);
        try{
            long result = MyDataBase.insert("users",null, contentValues);
            return result != -1;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }

    }

    public Boolean checkEmail(String email){
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        Cursor cursor = MyDatabase.rawQuery("SELECT * FROM users WHERE email = ?", new String[]{email});
        try{
            if(cursor.getCount() > 0){
                cursor.close();
                return true;
            }else{
                cursor.close();
                return false;
            }
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }

    }

    public int loginUser(String email, String password) {
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        Cursor cursor = MyDatabase.rawQuery("SELECT id FROM users WHERE email = ? AND password = ?", new String[]{email, password});
        try {
            if (cursor.moveToFirst()) {
                int user_id = cursor.getInt(0);
                cursor.close();
                return user_id;

            } else {
                cursor.close();
                return -1;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public List<Performance> getPerformance() {
        List<Performance> performanceList = new ArrayList<>();
        SQLiteDatabase database = this.getReadableDatabase();

        Cursor cursor = database.rawQuery("SELECT *, ticket.id AS ticket_id FROM performance JOIN ticket ON performance.id = ticket.performance_id", null);
        try{
            int idIndex = cursor.getColumnIndex("id");
            int nameIndex = cursor.getColumnIndex("name");
            int imageIndex = cursor.getColumnIndex("image");
            int locationIndex = cursor.getColumnIndex("location");
            int timeIndex = cursor.getColumnIndex("time");
            int accessibilityIndex = cursor.getColumnIndex("accessibility");
            int priceIndex = cursor.getColumnIndex("price");
            int quantityIndex = cursor.getColumnIndex("quantity");
            int ticketIndex = cursor.getColumnIndex("ticket_id");

            while (cursor.moveToNext()) {
                int id = cursor.getInt(idIndex);
                String name = cursor.getString(nameIndex);
                String image = cursor.getString(imageIndex);
                String location = cursor.getString(locationIndex);
                String time = cursor.getString(timeIndex);
                String accessibility = cursor.getString(accessibilityIndex);
                String price = cursor.getString(priceIndex);
                int quantity = cursor.getInt(quantityIndex);
                int ticket_id = cursor.getInt(ticketIndex);

                if (quantity > 0) {
                    Performance performance = new Performance(id, name, image, location, time, accessibility, price, quantity, ticket_id);
                    performanceList.add(performance);
                }
            }
            cursor.close();
        }catch(Exception e){
            e.printStackTrace();
        }

        return performanceList;
    }

    public List<Performance> filterPerformance(int year, int month, int day, String accessibility) {
        List<Performance> filteredPerformanceList = new ArrayList<>();
        SQLiteDatabase database = this.getReadableDatabase();
        StringBuilder query = new StringBuilder("SELECT *, ticket.id AS ticket_id FROM performance JOIN ticket ON performance.id = ticket.performance_id WHERE ");

        try {
            if (year != 0 || month != 0 || day != 0) {
                if (year != 0 && month != 0 && day != 0) {
                    // Date filter
                    String dateString = String.format("%04d-%02d-%02d", year, month, day);
                    query.append("date(time) = '").append(dateString).append("' ");
                } else {
                    return filteredPerformanceList;
                }
            }

            if (accessibility != null && !accessibility.isEmpty()) {
                if (year != 0 || month != 0 || day != 0) {
                    query.append("AND ");
                }
                // Accessibility filter
                query.append("accessibility LIKE '%").append(accessibility).append("%'");
            }

            Cursor cursor = database.rawQuery(query.toString(), null);

            int idIndex = cursor.getColumnIndex("id");
            int nameIndex = cursor.getColumnIndex("name");
            int imageIndex = cursor.getColumnIndex("image");
            int locationIndex = cursor.getColumnIndex("location");
            int timeIndex = cursor.getColumnIndex("time");
            int accessibilityIndex = cursor.getColumnIndex("accessibility");
            int priceIndex = cursor.getColumnIndex("price");
            int quantityIndex = cursor.getColumnIndex("quantity");
            int ticketIndex = cursor.getColumnIndex("ticket_id");

            while (cursor.moveToNext()) {
                int id = cursor.getInt(idIndex);
                String name = cursor.getString(nameIndex);
                String image = cursor.getString(imageIndex);
                String location = cursor.getString(locationIndex);
                String time = cursor.getString(timeIndex);
                String accessibilityValue = cursor.getString(accessibilityIndex);
                String price = cursor.getString(priceIndex);
                int quantity = cursor.getInt(quantityIndex);
                int ticket_id = cursor.getInt(ticketIndex);

                Performance performance = new Performance(id, name, image, location, time, accessibilityValue, price, quantity, ticket_id);
                filteredPerformanceList.add(performance);
            }

            cursor.close();
        } catch (Exception e) {
            // Handle the exception here
            e.printStackTrace();
        }

        return filteredPerformanceList;
    }
    public boolean bookPerformance(Context context, int user_id, int ticket_id, int ticket_qty) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("user_id", user_id);
        contentValues.put("ticket_id", ticket_id);
        contentValues.put("ticket_qty", ticket_qty);

        try {
            database.beginTransaction();

            long result = database.insert("booking", null, contentValues);
            if (result == -1) {
                return false;
            }

            // Get the current quantity of tickets
            Cursor cursor = database.rawQuery("SELECT quantity FROM ticket WHERE id = ?", new String[]{String.valueOf(ticket_id)});
            int currentQuantity = 0;
            int quantityIndex = cursor.getColumnIndex("quantity");
            if (cursor.moveToFirst()) {
                currentQuantity = cursor.getInt(quantityIndex);
            }
            cursor.close();

            // Check if the quantity will go below 0
            if (currentQuantity - ticket_qty < 0) {
                database.endTransaction();
                Toast toast = Toast.makeText(context, "Insufficient tickets available.", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.show();
                return false;
            }

            // Update the quantity of tickets
            String updateQuery = "UPDATE ticket SET quantity = (quantity - " + ticket_qty + ") WHERE id = ?";
            database.execSQL(updateQuery, new String[]{String.valueOf(ticket_id)});

            database.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (database.inTransaction()) {
                database.endTransaction();
            }
        }
    }





    public List<BookingItem> getBookingData(int user_id){
        List<BookingItem> bookingList = new ArrayList<>();
        SQLiteDatabase database = this.getReadableDatabase();
        String query = "SELECT booking.id AS id, performance.name AS name, performance.image AS image, performance.time AS time, ticket.quantity AS quantity, ticket.price AS price, booking.ticket_qty AS selected_quantity " +
                "FROM booking " +
                "JOIN ticket ON booking.ticket_id = ticket.id " +
                "JOIN performance ON ticket.performance_id = performance.id " +
                "WHERE booking.user_id = ?";
        String[] selectionArgs = {String.valueOf(user_id)};
        Cursor cursor = database.rawQuery(query, selectionArgs);

        try{
            int idIndex = cursor.getColumnIndexOrThrow("id");
            int nameIndex = cursor.getColumnIndexOrThrow("name");
            int imageIndex = cursor.getColumnIndexOrThrow("image");
            int quantityIndex = cursor.getColumnIndexOrThrow("quantity");
            int priceIndex = cursor.getColumnIndexOrThrow("price");
            int timeIndex = cursor.getColumnIndexOrThrow("time");
            int selectedQtyIndex = cursor.getColumnIndexOrThrow("selected_quantity");
            while (cursor.moveToNext()) {
                int id = cursor.getInt(idIndex);
                String name = cursor.getString(nameIndex);
                String image = cursor.getString(imageIndex);
                int quantity = cursor.getInt(quantityIndex);
                float price = cursor.getFloat(priceIndex);
                String time = cursor.getString(timeIndex);
                int selectedQty = cursor.getInt(selectedQtyIndex);

                BookingItem bookingItem = new BookingItem(id,name, image,time,price,quantity,selectedQty);
                bookingList.add(bookingItem);
            }

            cursor.close();
        }catch(Exception e){
            // Handle the exception here
            e.printStackTrace();
        }
        return bookingList;
    }

    public boolean cancelBooking(int bookingId) {
        SQLiteDatabase database = this.getWritableDatabase();
        try {
            database.beginTransaction();

            String[] columns = {"ticket_id", "ticket_qty"};
            String selection = "id = ?";
            String[] selectionArgs = {String.valueOf(bookingId)};
            Cursor cursor = database.query("booking", columns, selection, selectionArgs, null, null, null);
            int ticketId = -1;
            int bookedQuantity = 0;
            if (cursor.moveToFirst()) {
                ticketId = cursor.getInt(cursor.getColumnIndexOrThrow("ticket_id"));
                bookedQuantity = cursor.getInt(cursor.getColumnIndexOrThrow("ticket_qty"));
            }
            cursor.close();

            if (ticketId != -1) {
                // Retrieve the current quantity value from the ticket
                String[] ticketColumns = {"quantity"};
                String ticketSelection = "id = ?";
                String[] ticketSelectionArgs = {String.valueOf(ticketId)};
                Cursor ticketCursor = database.query("ticket", ticketColumns, ticketSelection, ticketSelectionArgs, null, null, null);
                int currentQuantity = 0;
                if (ticketCursor.moveToFirst()) {
                    currentQuantity = ticketCursor.getInt(ticketCursor.getColumnIndexOrThrow("quantity"));
                }
                ticketCursor.close();

                // Increment the ticket quantity by the booked quantity
                ContentValues values = new ContentValues();
                values.put("quantity", currentQuantity + bookedQuantity);
                int rowsUpdated = database.update("ticket", values, "id = ?", new String[]{String.valueOf(ticketId)});
                if (rowsUpdated == 0) {
                    return false;
                }
            }

            // Delete the booking
            int rowsDeleted = database.delete("booking", "id = ?", new String[]{String.valueOf(bookingId)});
            if (rowsDeleted == 0) {
                return false;
            }

            // Commit the transaction
            database.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            // Handle the exception here
            e.printStackTrace();
            return false;
        } finally {
            // End the transaction
            database.endTransaction();
        }
    }



}
