package com.zybooks.mobile2app;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
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
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NotificationActivity extends AppCompatActivity {

    // Variables
    private static final String TAG = "PrintLog";
    public MaterialToolbar top_menu_toolbar;
    public ActionBar actionBarBackButton;
    public FloatingActionButton fab_button;
    public RecyclerView recyclerView;
    public List<TableRowData> tableData;
    private NotificationAdapter notificationAdapter;
    ConstraintLayout mActivityNotification;
    Intent intentToDatabaseActivity;
    // Set a constant for the SMS permission request code tag
    private static final int SMS_PERMISSION_CODE = 101;
    private static boolean smsPermissionGranted = false;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_notification);
        Log.d(TAG, "activity_notification Started");
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.ActivityNotification), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        /*  TODO: Create and show an AlertDialog only once when the activity starts
         *    to guide user how to use the app.
         * *****************************************************************************************/
        // Flag to ensure the dialog only appears once, the first time the activity starts
        SharedPreferences prefs = getSharedPreferences("app_notification_prefs", MODE_PRIVATE);

        // FIXME: To clear all preferences including (first time launch tip) activate the line below
        // prefs.edit().clear().apply();

        // Check if the tip has been shown
        boolean tip2Shown = prefs.getBoolean("tip_2_shown", false);

        if (!tip2Shown) {
            // Set the dialog message and button text
            new AlertDialog.Builder(this)
                    .setTitle("Usage Tip 2")
                    .setMessage("Set the minimum QTY field to alert you if inventory drops below " +
                            "the threshold.")
                    .setPositiveButton("OK!", (dialog, which) -> {
                        // Save that the tip was shown
                        prefs.edit().putBoolean("tip_2_shown", true).apply();
                    })
                    // Show the dialog
                    .show();
        }


        /* TODO: The checkSmsPermission() method checks for the SMS permission and query the
            user for permission if permission has not been granted.
        * *****************************************************************************************/
        // Check for SMS permission automatically as activity starts
        checkSmsPermission();


        /* TODO: Send SMS with low inventory report upon button click and user request after checking
        *   for SMS permission. If permission is not granted, a notification will be sent to the
        *   user instead.
        * *****************************************************************************************/
        // Find the button from the activity_notification.xml layout
        MaterialButton btnSendStatusReport = findViewById(R.id.btnSendStatusReport);
        // Set the button click listener to call the sendSMSNotification() method
        btnSendStatusReport.setOnClickListener(v -> sendSMSNotification());


        /*  TODO: Initialize the RecyclerView in the activity_notification.xml layout
         *   This block will query data from the database and inflate the RecyclerView in activity_notification.xml
         * ****************************************************************************************/
        // Find the RecyclerView from the activity_notification.xml layout
        RecyclerView recyclerView = findViewById(R.id.notificationRecyclerView);

        // Initialize the database helper class
        dbHelper = new DatabaseHelper(this);

        // Get the data from the database to display in the RecyclerView
        List<TableRowData> tableData = dbHelper.getItemImgNameMinQuantity();

        // Set layout manager and adapter
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // Create the adapter with the data and database helper class
        NotificationAdapter adapter = new NotificationAdapter(this, tableData, dbHelper);
        // Set the adapter
        recyclerView.setAdapter(adapter);


        /*  TODO: Create the top menu Notification Activity and enable the Back button
         * ****************************************************************************************/
        // Find the top menu ActionBar id from the activity_database.xml layout
        top_menu_toolbar = findViewById(R.id.topAppBar);
        // Set up the top menu ActionBar with the MaterialToolbar
        setSupportActionBar(top_menu_toolbar);

        // Enable the Back button in the ActionBar
        ActionBar actionBarBackButton = getSupportActionBar();
        if (actionBarBackButton != null) {
            actionBarBackButton.setDisplayShowHomeEnabled(true);
            // Hide the Back button in the ActionBar
            actionBarBackButton.setDisplayHomeAsUpEnabled(false);
        }
        Log.d(TAG, "In Notification Activity: Top Menu bar created successfully");


        /* ****************************************************************************************/
        /* TODO: Handler for the bottom navigation bar.
            Setup the bottom navigation view with the navigation controller
        * *****************************************************************************************/
        //  Find the NavHostFragment from the activity_database.xml layout
        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        // Update the bottom navigation view with the navigation controller
        bottomNav.setSelectedItemId(R.id.nav_notification);

        // Handle navigation item clicks from the navigation bar
        bottomNav.setOnItemSelectedListener(item -> {
            // Get the id of the item that triggered the event
            int itemId = item.getItemId();
            mActivityNotification = findViewById(R.id.ActivityNotification);

            // TODO: Go to Login Activity from Notification Activity
            if (itemId == R.id.nav_login) {
                Intent intent = new Intent(NotificationActivity.this, LoginActivity.class);

                // Put an extra data to the intent call to log the user out
                intent.putExtra("loggedIn", false);

                // Start new activity, pass the extra variable, and end current activity
                startActivity(intent);
                finish();
                Log.d(TAG, "In Notification Activity: Logged out button clicked");
                return true;

                // TODO: Go to Database Activity from Notification Activity
            } else if (itemId == R.id.nav_database) {
                intentToDatabaseActivity = new Intent(NotificationActivity.this, DatabaseActivity.class);

                // Log the prompt for debugging
                Log.d(TAG, "In Notification Activity: Database button clicked");

                // Start new activity and end current activity
                startActivity(intentToDatabaseActivity);
                finish();
                return true;

                // Go to Notification Activity from Notification Activity
            }  else if (itemId == R.id.nav_notification) {
                Toast.makeText(this, "On Notification screen", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "In Notification Activity: Notification button clicked");
            }
            return false;
        });
        Log.d(TAG, "In Notification Activity: Bottom navigation bar initialized successfully");
    }
    // -----------------------      onCreate ends here      -----------------------


    @Override
    protected void onResume() {
        super.onResume();

        if (notificationAdapter != null) {
            List<TableRowData> updatedList = dbHelper.getItemImgNameMinQuantity();
            if (updatedList == null) updatedList = new ArrayList<>();
            notificationAdapter.setData(updatedList);
        }
    }
    // -----------------------      onResume ends here      -----------------------


    /* *********************************************************************************************
     *  TODO: Handle the app_bar_menu navigation bar from the top menu. Implements functionality for:
     *   1. Back button, 2. Settings button, 3. About button
     * ********************************************************************************************/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        mActivityNotification = findViewById(R.id.ActivityNotification);

        // TODO: Handle the Back button from the top menu action bar
        if (id == android.R.id.home) {
            Log.d(TAG, "In Notification Activity: Back button clicked");
            finish();
            return true;

            // TODO: Handle the Settings button from the top menu action bar to display a toast
        } else if (id == R.id.menu_bar_settings_button) {
            Snackbar.make(mActivityNotification, "* Settings - Will be added later *",
                    Snackbar.LENGTH_SHORT).show();
            Log.d(TAG, "In Notification Activity: Settings button clicked");
            return true;

            // TODO: Handle the About button from the top menu action bar to display a toast
        } else if (id == R.id.menu_bar_about_button) {
            Snackbar.make(mActivityNotification, "* About - Will be added later *",
                    Snackbar.LENGTH_SHORT).show();
            Log.d(TAG, "In Notification Activity: About button clicked");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /* ********************************************************************************************/
    /* TODO: This method checks for the SMS permission. It will query the user for permission if
        permissions are not granted. Upon results, it will update the UI accordingly.
     * ********************************************************************************************/
    private void checkSmsPermission() {
        // Find the SMS permission status text view from the activity_notification.xml layout
        TextView smsStatusText = findViewById(R.id.smsStatusText);
        Log.d(TAG, "In Notification Activity: Checking for SMS permission...");

        // Check for SMS permission. If not granted, request it.
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {

            // If permission NOT granted - update the UI to request permission
            smsStatusText.setText("SMS permission not granted. Requesting...");

            // Request the permission from user by showing the system permission dialog and
            // automatically calling the onRequestPermissionsResult() method to update the UI
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SEND_SMS},
                    SMS_PERMISSION_CODE);

        } else {
            // If permission exists - update the UI to show permission granted
            Log.d(TAG, "In Notification Activity: SMS permission already exists");

            // Permission already granted
            smsStatusText.setText(R.string.sms_permission_already_granted);
            // Set the SMS permission flag to true
            smsPermissionGranted = true;
        }
    }


    /* *********************************************************************************************
     *  TODO: Automatic call by the Android system as response to ActivityCompat.requestPermissions()
     *   and user's response to output the permission results to the UI.
     * ********************************************************************************************/
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Check for the SMS permission request code to find the correct permission result
        if (requestCode == SMS_PERMISSION_CODE) {
            TextView smsStatusText = findViewById(R.id.smsStatusText);
            // If permission granted -> update the UI accordingly
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                smsStatusText.setText(R.string.sms_permission_granted);
                Log.d(TAG, "In Notification Activity: SMS permission granted");
                // Set the SMS permission flag to true
                smsPermissionGranted = true;
            } else {
                // If permission denied -> update the UI accordingly
                smsStatusText.setText(R.string.sms_permission_denied);
                Log.d(TAG, "In Notification Activity: SMS permission denied");
                // Set the SMS permission flag to false
                smsPermissionGranted = false;
            }
        }
    }


    /* *********************************************************************************************
     *  TODO: The sendSMSNotification() method is called when the user clicks the button to send
     *   the low inventory report from activity_notification.xml. If SMS permission is granted,
     *   a SMS will be sent, otherwise a notification will be sent to the user if notification
     *   permission is granted. Otherwise, no notification will be sent to the user.
     * ********************************************************************************************/
    public void sendSMSNotification() {
        Log.d(TAG, "In Notification Activity: sendSMSNotification() called");
        if (smsPermissionGranted) {
            Log.d(TAG, "In Notification Activity: SMS permission granted: " + smsPermissionGranted);

            // TODO: Call the sendLowStockSMS() method to Send the low stock SMS message.
            //  Call the lowStockMessageBuilder() method to build the low stock message
            sendLowStockSMS(NotificationActivity.this,
                    lowStockMessageBuilder(), 1778549);
        } else {
            Log.d(TAG, "In Notification Activity: SMS permission NOT granted." +
                    "Sending notification instead");
            // TODO: Send a notification to the user if SMS permission is not granted.
            //  the notification will be sent to the user if the new quantity is
            //  below the minimum quantity. The NotificationHelper() class is used
            //  to send the notification and the lowStockMessageBuilder() method to build the low stock message
            NotificationHelper.sendLowStockNotification(NotificationActivity.this,
                    lowStockMessageBuilder(), 1111, "Low Inventory Report");
        }
    }


    /* *********************************************************************************************
     *  TODO: The lowStockMessageBuilder query the database for the low stock items and
     *   builds the message to be sent to the user via SMS or notification.
     * ********************************************************************************************/
    public String lowStockMessageBuilder() {
        Log.d(TAG, "In Notification Activity: lowStockMessageBuilder() called\n");

        // Query the database for the low stock items
        List<TableRowData> lowStockItems = dbHelper.getLowStockItems();

        // Build the message to be sent to the user via SMS or notification
        StringBuilder messageBuilder = new StringBuilder("*** Low Stock Alert! ***\n" +
                "Please restock the following items which are below threshold:\n");

        // Loop through the low stock items from the database and add them to the message
        for (TableRowData item : lowStockItems) {
            messageBuilder.append("Item ")
                    .append(item.getColumn2())                          // item name
                    .append(" (Current: ").append(item.getColumn4())    // quantity
                    .append(", Min: ").append(item.getColumn5())        // quantity_min
                    .append(")\n");
        }
        return messageBuilder.toString();
    }


    /* *********************************************************************************************
     *  TODO: The sendLowStockNotification() message constructor
     * ********************************************************************************************/
    public static void sendLowStockSMS(Context context, String message, int requestCode) {
        // Phone number example.
        String phoneNumber = "17785491390";

        try {
            // Create a new SMS message
            SmsManager smsManager = SmsManager.getDefault();

            // Send the SMS message
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);

            // Log the SMS confirmation: the message sent to the user
            Log.d(TAG, "SMS sent to " + phoneNumber + ":\n" + message);
            // Display a toast to indicate that the SMS was sent
            Toast.makeText(context, "SMS sent to " + phoneNumber, Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Log.e(TAG, "SMS failed: " + e.getMessage());
            Toast.makeText(context, "Failed to send SMS", Toast.LENGTH_SHORT).show();
        }
    }
}