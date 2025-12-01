package com.zybooks.mobile2app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.snackbar.Snackbar;

public class LoginActivity extends AppCompatActivity {

    // Variables for the class
    private static final String TAG = "PrintLog";
    private static final String mName = "Daniel";
    private static final String mPassword = "Gorelkin";
    private static boolean loggedIn = false;
    // Variables for the widgets
    private EditText mUserName, mUserPassword;
    private Button mLoginButton;
    private ConstraintLayout mActivityLogin;
    private TextView loginTempTextOutput;

    // Database variables to be used in the LoginActivity
    private DatabaseHelper dbHelper;
    SQLiteDatabase db;
    boolean dbUser;
    private static final String DATABASE_NAME = "mobile2app.db";

    @SuppressLint("SdCardPath")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activityLoginContainer),
                (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Log.d(TAG, "onCreate: Activity Login started...");


        // TODO: Create the database if it doesn't exist
        // Credentials stored at /data/data/com.zybooks.mobile2app/databases/mobile2app.db
        // Create the database helper object
        dbHelper = new DatabaseHelper(this);

        // Check if the database exists
        try{
            // TODO: If the database doesn't exist, create it and open the readable/writable database
            db = dbHelper.getReadableDatabase();
        } catch (Exception e) {
            Log.d(TAG, "DATABASE Check - Database does not exist: failed!");
            e.printStackTrace();
        }
        Log.d(TAG, "DATABASE Check - Database exist and ready for reading: " + db.isOpen());
        // Close the database
        db.close();

        /* *****************************************************************************************
        // TODO: Register the "admin" user into the database
        / *****************************************************************************************/
        // Check if the user admin exists in the database
        dbUser = dbHelper.checkUserCredentials(mName);
        // Add the admin user if it doesn't exist
        if (!dbUser) {
            boolean userRegistered = dbHelper.registerUser(mName, mPassword);
            if (userRegistered) {
                Log.d(TAG, "Admin user registered successfully");
            }
        }


        // Retrieve the "loggedIn" boolean from the bottom_nav_menu.xml Intent and log user out
        loggedIn = getIntent().getBooleanExtra("loggedIn", false);
        Log.d(TAG, "User Login status: " + loggedIn);

        // Identify the EditTexts in the layout file
        mUserName = findViewById(R.id.username);
        mUserPassword = findViewById(R.id.password);
        // Identify the Button in the layout file
        mLoginButton = findViewById(R.id.loginButton);
        // Identify the ConstraintLayout in the layout file
        mActivityLogin = findViewById(R.id.activityLoginContainer);
        // Identify the TextView in the layout file
        loginTempTextOutput = findViewById(R.id.loginTempTextOutput);

        // Add a TextWatcher to the username and password EditTexts
        TextWatcher loginTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Check if both username and password fields are not empty
                String usernameInput = mUserName.getText().toString().trim();
                String passwordInput = mUserPassword.getText().toString().trim();

                // Enable the login button if both fields are not empty
                mLoginButton.setEnabled(!usernameInput.isEmpty() && !passwordInput.isEmpty());
            }

            @Override
            public void afterTextChanged(Editable s) { }
        };

        // Attach the same watcher to both username and password fields
        // and trigger the onTextChanged method when the user types in the fields
        // and enable the login button if both fields are not empty
        mUserName.addTextChangedListener(loginTextWatcher);
        mUserPassword.addTextChangedListener(loginTextWatcher);
    }


    /* *********************************************************************************************
    *  This method is called when the user clicks the "Login" button
    *  It retrieves the user input from the username and password EditTexts
    * and displays it in the temporary TextView
    * *********************************************************************************************/
    public void LoginClick(View view) {
        // If view exists
        if (view != null) {
            Log.d(TAG, "Login button clicked");                 // Log a message to the console

            // get user input
            String mUserNameInput = mUserName.getText().toString();
            String mPasswordInput = mUserPassword.getText().toString();
            Log.d(TAG, "User Login Input: " + mUserNameInput + " / " + mPasswordInput);

            // TODO: modify to check if the EditTexts mUserName and mPassword exist in the database


            // Check if the user credentials are valid in the database
            dbUser = dbHelper.validateUserCredentials(mUserNameInput, mPasswordInput);
            Log.d(TAG, "User and Password are valid in the database: " + dbUser);

            // If the user credentials are valid, log them in and start the Database activity
            if (dbUser) {
                // Show the loginTempTextOutput TextView and set its text
                loginTempTextOutput.setVisibility(View.VISIBLE);
                String mOutputMessage = getString(R.string.welcome) + mUserNameInput + "!";

                // Display the user input in the TextView
                loginTempTextOutput.setText(mOutputMessage);

                // Set the loggedIn variable to true
                loggedIn = true;
                Log.d(TAG, "User Login Status: " + loggedIn);

                // Create an Intent to start the DatabaseActivity after the one second delay
                view.postDelayed(() -> {
                    // Start the Database activity view
                    startActivity(new Intent(this, DatabaseActivity.class));
                }, 1250);

            } else {
                // TODO: implement login logic if username or password is incorrect
                Snackbar.make(mActivityLogin, getString(R.string.invalid_username_password),
                        Snackbar.LENGTH_SHORT).show();
                loginTempTextOutput.setVisibility(View.VISIBLE);
                loginTempTextOutput.setText(R.string.login_failed_message);
                // return;
            }
        }
    }


    /* *********************************************************************************************
     *  TODO: Implement the RegisterClick method
     * ********************************************************************************************/
    public void RegisterClick(View view) {
        // If view exists
        if (view != null) {
            Log.d(TAG, "Register button clicked");              // Log a message to the console

            // get user input
            String mUserNameInput = mUserName.getText().toString();
            String mPasswordInput = mUserPassword.getText().toString();
            Log.d(TAG, "User Register Input: " + mUserNameInput + "/" + mPasswordInput);

            // Validate the user input that is not empty
            if (!mUserNameInput.trim().isEmpty() && !mPasswordInput.trim().isEmpty()) {

                // Check if the user admin exists in the database
                dbUser = dbHelper.checkUserCredentials(mUserNameInput);
                // Add the admin user if it doesn't exist
                if (!dbUser) {
                    dbHelper.registerUser(mUserNameInput, mPasswordInput);
                    loginTempTextOutput.setVisibility(View.VISIBLE);
                    loginTempTextOutput.setText(R.string.register_success_message);
                    Log.d(TAG, "New user registered successfully");
                } else {
                    // Display the failed add user message in the TextView
                    loginTempTextOutput.setVisibility(View.VISIBLE);
                    loginTempTextOutput.setText(R.string.register_user_exists_message);
                    Log.d(TAG, "User already exists in the database...");
                }
            } else {
                // TODO: implement register logic if username or password is incorrect
                loginTempTextOutput.setVisibility(View.VISIBLE);
                loginTempTextOutput.setText(R.string.register_failed_message);
            }
        }
    }
}