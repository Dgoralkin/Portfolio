package com.zybooks.mobile2app;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class DatabaseActivity extends AppCompatActivity {

    // Variables
    private static final String TAG = "PrintLog";
    public MaterialToolbar top_menu_toolbar;
    public FloatingActionButton fab_button;
    public RecyclerView recyclerView;
    // public List<TableRowData> tableData;
    ConstraintLayout mActivityDatabase;
    Intent intentToLoginActivity, intentFromAddItemActivity, intentToNotificationActivity;

    // Database variables to be used in the LoginActivity
    private DatabaseHelper dbHelper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_database);
        Log.d(TAG, "activity_database Started");

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_database), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        // TODO: Create the database if it doesn't exist
        // Items stored at /data/data/com.zybooks.mobile2app/databases/mobile2app.db
        // Create the database helper object
        dbHelper = new DatabaseHelper(this);

        // Check if the database exists
        try{
            // TODO: Try to open the readable/writable database
            db = dbHelper.getReadableDatabase();
            Log.d(TAG, "DATABASE Check - Database exist and ready for reading: " + db.isOpen());
            // Close the database
            db.close();
        } catch (Exception e) {
            Log.d(TAG, "DATABASE Check - Database does not exist: failed!");
            e.printStackTrace();
        }


        /*  TODO: Create and show an AlertDialog only once when the activity starts
        *    to guide user how to use the app.
        * *****************************************************************************************/
        // Flag to ensure the dialog only appears once, the first time the activity starts
        SharedPreferences prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);

        // FIXME: To clear all preferences including (first time launch tip) activate the line below
        // prefs.edit().clear().apply();

        // Check if the tip has been shown
        boolean tipShown = prefs.getBoolean("tip_shown", false);

        if (!tipShown) {
            // Set the dialog message and button text
            new AlertDialog.Builder(this)
                    .setTitle("Usage Tip")
                    .setMessage("Tap and hold an item to adjust quantity")
                    .setPositiveButton("Got it", (dialog, which) -> {
                        // Save that the tip was shown
                        prefs.edit().putBoolean("tip_shown", true).apply();
                    })
                    // Show the dialog
                    .show();

            // TODO: If the database is empty, populate it with three example items
            for (int i = 1; i <= 3; i++) {
                String imagePath = "imagePath" + i;
                String sku = "SKU123-" + i;
                String name = "Item " + i;
                int quantity = 5 + i; // example: 6, 7, 8
                // Add the example item to the database
                dbHelper.addItem(null, sku, name, quantity);
            }
        }


        /*  TODO: Request user notification permission for Android 13 and above to send notifications
         *   Notifications will be sent from the NotificationActivity class and from this class when
         *   user adjust the quantity of an item in the database below the preset threshold.
         * *****************************************************************************************/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS}, 101);
            }
        }

        /*  TODO: Create the top menu ActionBar and enable the Back button
        * *****************************************************************************************/
        // Find the top menu ActionBar id from the activity_database.xml layout
        top_menu_toolbar = findViewById(R.id.topAppBar);
        // Set up the top menu ActionBar with the MaterialToolbar
        setSupportActionBar(top_menu_toolbar);
        // Enable the Back button in the ActionBar
        ActionBar actionBarBackButton = getSupportActionBar();
        if (actionBarBackButton != null) {
            actionBarBackButton.setDisplayShowHomeEnabled(true);
            actionBarBackButton.setDisplayHomeAsUpEnabled(true);
        }
        Log.d(TAG, "In Database Activity: Top Menu bar created successfully");

        /* ****************************************************************************************/
        /* TODO: Handler for the bottom navigation bar.
            Setup the bottom navigation view with the navigation controller
        * *****************************************************************************************/

        //  Find the NavHostFragment from the activity_database.xml layout
        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        // Update the bottom navigation view with the navigation controller
        bottomNav.setSelectedItemId(R.id.nav_database);

        // Handle navigation item clicks from the navigation bar
        bottomNav.setOnItemSelectedListener(item -> {
            // Get the id of the item that triggered the event
            int itemId = item.getItemId();
            mActivityDatabase = findViewById(R.id.activity_database);

            // TODO: Go to Login Activity from DatabaseActivity
            if (itemId == R.id.nav_login) {
                intentToLoginActivity = new Intent(DatabaseActivity.this, LoginActivity.class);

                // Put an extra data to the intent call to log the user out
                intentToLoginActivity.putExtra("loggedIn", false);

                Log.d(TAG, "In Database Activity: Logged out button clicked");

                // Start new activity, pass the extra variable, and end current activity
                startActivity(intentToLoginActivity);
                finish();
                return true;

            // TODO: Go to Database Activity from DatabaseActivity
            } else if (itemId == R.id.nav_database) {
                Toast.makeText(this, "On Database screen", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "In Database Activity: Database button clicked");
                return true;

            // Go to Notification Activity from DatabaseActivity
            }  else if (itemId == R.id.nav_notification) {
                intentToNotificationActivity = new Intent(DatabaseActivity.this, NotificationActivity.class);

                Log.d(TAG, "In Database Activity: Notification button clicked");

                // Start new activity and end current activity
                startActivity(intentToNotificationActivity);
                finish();
                return true;
            }
            return false;
        });
        Log.d(TAG, "In Database Activity: Bottom navigation bar initialized successfully");

        /* ****************************************************************************************/
        /* TODO: Handler for the floating action button in the bottom navigation bar
        * *****************************************************************************************/
        // Find the floating action button id from the activity_database.xml layout
        fab_button = findViewById(R.id.fab);
        // Set up the floating action button with the click listener
        fab_button.setOnClickListener(view -> {

            Log.d(TAG, "In Database Activity: FAB button clicked");
            // Todo: Action when clicked: Create an Intent to start the AddItemActivity view
            startActivity(new Intent(this, AddItemActivity.class));
        });
        Log.d(TAG, "In Database Activity: FAB button created");

        /* ****************************************************************************************/
        /*  TODO: Handler for the RecyclerView to inflate the database content
         * ****************************************************************************************/
        // Find the RecyclerView by its ID in the activity_database.xml layout
        recyclerView = findViewById(R.id.tableRecyclerView);

        // Set a layout through the RecyclerView linear layout manager
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        /* *****************************************************************************************
         *  TODO: Check if data was retrieved from AddItemActivity through an intent pass.
         *   If so, add it to the tableData list.
         * ****************************************************************************************/
        // Catch the Intent and extract the extras (SKU, Name, Quantity) if intent came from
        // AddItemActivity and carries the extras
        intentFromAddItemActivity = getIntent();
        if (intentFromAddItemActivity != null && intentFromAddItemActivity.hasExtra("itemSku")) {
            String itemImage = intentFromAddItemActivity.getStringExtra("itemImage");
            String itemSku = intentFromAddItemActivity.getStringExtra("itemSku");
            String itemName = intentFromAddItemActivity.getStringExtra("itemName");
            int itemQuantity = intentFromAddItemActivity.getIntExtra("itemQuantity", 0);

            // Log the data and check it was retrieved correctly
            Log.d(TAG, "Intent Received from AddItemActivity - Image: "
                    + itemImage + ", SKU: " + itemSku + ", Name: "
                    + itemName + ", Quantity: " + itemQuantity);

            // TODO: Add the retrieved data to the tableData list
            // Check if the SKU already exists in the database
            if (!dbHelper.isSKUExists(itemSku)) {
                // Add the retrieved data to the table data list in the database before it sent to RecyclerView
                boolean itemAdded = dbHelper.addItem(itemImage, itemSku, itemName, itemQuantity);
                if (itemAdded) {
                    Log.d(TAG, "Item " + itemName + " added to database successfully");
                    // Clear intent extras after processing to avoid re-adding
                    setIntent(new Intent());
                } else {
                    Snackbar.make(mActivityDatabase, getString(R.string.add_item_error),
                            Snackbar.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this, R.string.sku_exist_error, Toast.LENGTH_LONG).show();
                Log.d(TAG, "Can't add Item " + itemName + ": already exists in database - SKU is UNIQUE");
            }
            // Clear intent extras after processing to avoid re-adding
            setIntent(new Intent());
        }

        /* ****************************************************************************************/
        /*  TODO: Set the adapter for the RecyclerView with delete click listener
         *   This method will call the getAllItems() method from the DatabaseHelper class
         *   and populate the tableData list with the retrieved data through the TableAdapter() constructor
         * ****************************************************************************************/
        TableAdapter adapter = new TableAdapter(dbHelper.getAllItems(), dbHelper, (position, rowData) -> {
            boolean deleted = dbHelper.deleteItem(rowData.getColumn2());

            if (deleted) {
                Log.d(TAG, "Item " + rowData.getColumn3() + " deleted from database successfully");
                Toast.makeText(this, "Item " + rowData.getColumn3()
                        + " deleted successfully", Toast.LENGTH_SHORT).show();
                // Reload activity
                recreate();
            } else {
                Log.d(TAG, "Failed to delete item " + rowData.getColumn3() + " from database");
            }
        });

        /* ****************************************************************************************/
        /*  TODO: Initialize the RecyclerView
         * ****************************************************************************************/
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        Log.d(TAG, "In Database Activity: Recycler view created and populated");
    }
    // -----------------------      onCreate ends here      -----------------------


    /* *********************************************************************************************
     *  TODO: Populate the menu in the ActionBar
     * ********************************************************************************************/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // tell Android to use your res/menu/app_bar_menu.xml to populate the toolbar menu.
        getMenuInflater().inflate(R.menu.app_bar_menu, menu);
        Log.d(TAG, "In Database Activity: Top app_bar_menu inflated");
        return true;
    }
    /* *********************************************************************************************
     *  TODO: Handle the app_bar_menu navigation bar from the top menu. Implements functionality for:
     *   1. Back button, 2. Settings button, 3. About button
     * ********************************************************************************************/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        mActivityDatabase = findViewById(R.id.activity_database);

        // TODO: Handle the Back button from the top menu action bar
        if (id == android.R.id.home) {
            Log.d(TAG, "In Database Activity: Back button clicked");
            finish();
            return true;

        // TODO: Handle the Settings button from the top menu action bar
        } else if (id == R.id.menu_bar_settings_button) {
            Snackbar.make(mActivityDatabase, "* Settings - Will be added later *",
                    Snackbar.LENGTH_SHORT).show();
            Log.d(TAG, "In Database Activity: Settings button clicked");
            return true;

        // TODO: Handle the About button from the top menu action bar
        } else if (id == R.id.menu_bar_about_button) {
            Snackbar.make(mActivityDatabase, "* About - Will be added later *",
                    Snackbar.LENGTH_SHORT).show();
            Log.d(TAG, "In Database Activity: About button clicked");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}