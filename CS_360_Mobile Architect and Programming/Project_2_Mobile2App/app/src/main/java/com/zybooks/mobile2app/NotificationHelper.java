package com.zybooks.mobile2app;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import android.Manifest;
import android.content.pm.PackageManager;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

/* *********************************************************************************************
 * TODO: The NotificationHelper is a utility class used to simplify the creation and display of
 *  notifications and handle Android's notification channel requirement on the newer API levels.
 * ********************************************************************************************/
public class NotificationHelper {

    private static final String CHANNEL_ID = "inventory_channel_id";
    private static final String CHANNEL_NAME = "Inventory Notifications";


    // sendLowStockNotification() sends a notification when an item is below the minimum quantity
    public static void sendLowStockNotification(Context context, String itemName, int quantity, String alertType) {
        Log.d("PrintLog", "In NotificationHelper: Checking notification permissions");

        // Check notification permission for Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            int permissionCheck = ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS);
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                Log.d("PrintLog", "Notification permission NOT granted. Notification will not be sent.");
                return;
            } else {
                Log.d("PrintLog", "Notification permission granted.");
            }
        }

        // Create a notification channel for Android TIRAMISU and above
        createNotificationChannel(context);

        // Check if notification channel is blocked by the user for versions (Android 8+) to (Android 13)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            NotificationManager checkManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel existingChannel = checkManager.getNotificationChannel(CHANNEL_ID);
            if (existingChannel != null && existingChannel.getImportance() == NotificationManager.IMPORTANCE_NONE) {
                Log.d("PrintLog", "Notification channel is BLOCKED by the user. Notification not visible.");
            } else {
                Log.d("PrintLog", "Notification channel is active.");
            }
        }

        // Todo: Reuse notification method to send notification if permissions granted

        // Notification template #1 - Sent from DatabaseActivity if user updates item quantity and
        // it is below the defined minimum quantity
        if ("Low Inventory Alert".equals(alertType)) {
            // Build the notification and display it
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.outline_circle_notification)
                    .setContentTitle("Low Inventory Alert")
                    .setContentText("Item " + itemName + " is below minimum quantity: " + quantity)
                    .setPriority(NotificationCompat.PRIORITY_HIGH);
            // Get the system notification manager and send the notification
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            // Send the notification with a unique ID
            manager.notify((int) System.currentTimeMillis(), builder.build());
        }

        // Notification template #2 - Sent from Notification activity if user clicks on notification
        // button to send the Low Inventory Report and SMS permissions are not granted
        if ("Low Inventory Report".equals(alertType)) {
            // Build the notification and display it
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.outline_circle_notification)
                    .setContentTitle("Low Inventory Report")
                    .setContentText("*** SMS permissions blocked ***\n" +
                            "Grant SMS permissions to view the detailed report!")
                    .setPriority(NotificationCompat.PRIORITY_HIGH);

            // Get the system notification manager and send the notification
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            // Send the notification with a unique ID
            manager.notify((int) System.currentTimeMillis(), builder.build());
        }
        Log.d("PrintLog", "In NotificationHelper: Notification sent if permissions granted");
    }

    // createNotificationChannel() creates a notification channel.
    private static void createNotificationChannel(Context context) {
        // Check if the Android version is Oreo or above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    // Set the channel ID, name, and importance
                    CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH
            );
            // Set the channel description
            channel.setDescription("Alerts when items are below minimum quantity");

            // Get the system notification manager and create the notification channel
            NotificationManager manager = context.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }
}
