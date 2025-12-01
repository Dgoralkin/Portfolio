package com.zybooks.mobile2app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


/* *********************************************************************************************
 *  TODO: The DatabaseHelper() class acts as an interface between the application and the SQLite database.
 *   It will store the data for the users and items in the database in two tables.
 * ********************************************************************************************/
public class DatabaseHelper extends SQLiteOpenHelper {

    // Variables
    private static final String TAG = "PrintLog";
    private static final String DATABASE_NAME = "mobile2app.db";
    private static final int DATABASE_VERSION = 1;

    // User table variables
    public static final String TABLE_USERS = "users";
    public static final String COLUMN_USER_ID = "id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";

    // Items table variables
    public static final String TABLE_ITEMS = "items";
    public static final String COLUMN_ITEM_ID = "id";
    public static final String COLUMN_IMAGE_PATH = "image_path";
    public static final String COLUMN_SKU = "sku";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_QUANTITY = "quantity";
    public static final String COLUMN_QUANTITY_MIN = "quantity_min";
    public static final int ITEM_QUANTITY_MIN = 1;


    // Constructor
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    // The onCreate() method is called when the database is created for the first time.
    // It is used to create the tables that will store the data.
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create table for users if it doesn't exist
        try {
            String createUsersTable = "CREATE TABLE IF NOT EXISTS " + TABLE_USERS + " (" +
                    COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_USERNAME + " TEXT NOT NULL UNIQUE, " +
                    COLUMN_PASSWORD + " TEXT NOT NULL)";

            // Create table for items if it doesn't exist
            String createItemsTable = "CREATE TABLE IF NOT EXISTS " + TABLE_ITEMS + " (" +
                    COLUMN_ITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_IMAGE_PATH + " TEXT, " +
                    COLUMN_SKU + " TEXT NOT NULL UNIQUE, " +
                    COLUMN_NAME + " TEXT NOT NULL, " +
                    COLUMN_QUANTITY + " INTEGER NOT NULL, " +
                    COLUMN_QUANTITY_MIN + " INTEGER NOT NULL" + ")";

            // Execute SQL statements
            db.execSQL(createUsersTable);
            db.execSQL(createItemsTable);

            Log.d(TAG, "Tables created successfully.");
        } catch (Exception e) {
            Log.e(TAG, "Database creation failed", e);
        }
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // For development only; in production use ALTER TABLE
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
        onCreate(db);
    }


    // ---------- USER TABLE METHODS ----------

    // Check user credentials in the database
    // Returns true if credentials are valid and user exists, false otherwise
    public boolean checkUserCredentials(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS,
                new String[]{COLUMN_USER_ID},
                COLUMN_USERNAME + "=?",
                new String[]{username},
                null, null, null);

        boolean valid = cursor.moveToFirst();
        cursor.close();
        return valid;
    }


    // Check user credentials in the database
    // Returns true if credentials are valid and user exists, false otherwise
    public boolean validateUserCredentials(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS,
                new String[]{COLUMN_USER_ID},
                COLUMN_USERNAME + "=? AND " + COLUMN_PASSWORD + "=?",
                new String[]{username, password},
                null, null, null);

        boolean valid = cursor.moveToFirst();
        cursor.close();
        return valid;
    }

    // Register a new user
    public boolean registerUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD, password);

        long result = db.insert(TABLE_USERS, null, values);
        db.close();

        // Return true if insert was successful
        return result != -1;
    }
    // ---------- END OF USER TABLE METHODS ----------


    // ---------- ITEMS TABLE METHODS ----------
    // This method will return a TableRowData list of all the items in the database
    public List<TableRowData> getAllItems() {
        List<TableRowData> items = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM items order by name", null);

        if (cursor.moveToFirst()) {
            do {
                String imagePath = cursor.getString(cursor.getColumnIndexOrThrow("image_path"));
                String sku = cursor.getString(cursor.getColumnIndexOrThrow("sku"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                int quantity = cursor.getInt(cursor.getColumnIndexOrThrow("quantity"));

                TableRowData item = new TableRowData(imagePath, sku, name, quantity);
                items.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return items;
    }

    // This method will return a TableRowData list of all the items in the database
    public List<TableRowData> getItemImgNameMinQuantity() {
        List<TableRowData> items = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM items order by name", null);

        if (cursor.moveToFirst()) {
            do {
                String imagePath = cursor.getString(cursor.getColumnIndexOrThrow("image_path"));
                String sku = cursor.getString(cursor.getColumnIndexOrThrow("sku"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                int quantity_min = cursor.getInt(cursor.getColumnIndexOrThrow("quantity_min"));

                TableRowData item = new TableRowData(imagePath, sku, name, 0 , quantity_min);
                items.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return items;
    }

    // This method will return an integer value of the minimum quantity of the item in the database
    public int getMinQuantity(String sku) {
        // Default value if not found
        int quantityMin = -1;

        // Get the database
        SQLiteDatabase db = this.getReadableDatabase();
        // Query the database for the minimum quantity
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_QUANTITY_MIN + " FROM items WHERE sku = ?", new String[]{sku});


        // Get the value of the minimum quantity
        if (cursor != null && cursor.moveToFirst()) {
            quantityMin = cursor.getInt(0);
            cursor.close();
        } else if (cursor != null) {
            cursor.close();
        }
        return quantityMin;
    }


    // This method will add a new item to the database
    public boolean addItem(String imagePath, String sku, String name, int quantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_IMAGE_PATH, imagePath);
        values.put(COLUMN_SKU, sku);
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_QUANTITY, quantity);
        values.put(COLUMN_QUANTITY_MIN, ITEM_QUANTITY_MIN);

        try {
            long result = db.insertOrThrow(TABLE_ITEMS, null, values);
            return result != -1;
        } catch (SQLiteConstraintException e) {
            Log.e("DB_ERROR", "Duplicate SKU insertion attempt: " + sku, e);
            return false;
        }
    }

    // Delete item by SKU
    public boolean deleteItem(String sku) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete(TABLE_ITEMS, "sku = ?", new String[]{sku});
        db.close();
        return rowsDeleted > 0;
    }

    // Search the table for an item by SKU, return True if found, False otherwise
    public boolean isSKUExists(String sku) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_ITEMS,
                new String[]{COLUMN_SKU},
                COLUMN_SKU + " = ?",
                new String[]{sku},
                null, null, null
        );

        // True if at least one row matches
        boolean exists = cursor.moveToFirst();
        cursor.close();
        return exists;
    }


    // This method will update the quantity of an item in the database based on its unique UPC
    // Returns true if the update was successful, false otherwise
    // Reads commands from the TableAdapter class in the showQuantityDialog
    public boolean updateItemQuantity(String sku, int newQuantity) {
        // Get the database
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        // Set the new quantity
        values.put(COLUMN_QUANTITY, newQuantity);

        // Execute the query and update the item in the database
        int rowsAffected = db.update(TABLE_ITEMS, values, "sku = ?", new String[]{sku});
        // Return true if updated
        return rowsAffected > 0;
    }


    // This method will update the minimum allowed quantity of an item in the database based on its
    // unique UPC. Returns true if the update was successful, false otherwise
    // Reads commands from the NotificationAdapter class in the updateMinQuantity() method
    public boolean updateItemMinQuantity(String sku, int newMinQuantity) {
        // Get the database
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        // Set the new quantity
        values.put(COLUMN_QUANTITY_MIN, newMinQuantity);

        // Execute the query and update the item in the database
        int rowsAffected = db.update(TABLE_ITEMS, values, "sku = ?", new String[]{sku});
        // Return true if updated
        return rowsAffected > 0;
    }

    // This method will return a TableRowData list of all the items in the database that have a value
    // of item quantity on hand < defined quantity allowed.
    public List<TableRowData> getLowStockItems() {

        List<TableRowData> lowStockList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM items WHERE quantity < quantity_min ORDER BY name", null);

        if (cursor.moveToFirst()) {
            do {
                String imagePath = cursor.getString(cursor.getColumnIndexOrThrow("image_path"));
                String sku = cursor.getString(cursor.getColumnIndexOrThrow("sku"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                int quantity = cursor.getInt(cursor.getColumnIndexOrThrow("quantity"));
                int quantity_min = cursor.getInt(cursor.getColumnIndexOrThrow("quantity_min"));

                TableRowData item = new TableRowData(imagePath, sku, name, quantity, quantity_min);
                lowStockList.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return lowStockList;
    }
    // ---------- END OF THE ITEMS TABLE METHODS ----------
}