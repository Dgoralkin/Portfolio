package com.zybooks.mobile2app;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private final List<TableRowData> dataList;
    private final DatabaseHelper dbHelper;
    private final Context context;


    /* *********************************************************************************************
     *  TODO: This method initializes the adapter with the context, data list, and database helper
     * ********************************************************************************************/
    public NotificationAdapter(Context context, List<TableRowData> dataList, DatabaseHelper dbHelper) {
        this.context = context;
        this.dataList = dataList;
        this.dbHelper = dbHelper;
    }


    /* *********************************************************************************************
     *  TODO: This method initializes the view holder for the adapter.
     * ********************************************************************************************/
    public static class NotificationViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView nameTextView;
        EditText quantityEditViewText;
        Button incrementBtn, decrementBtn;

        // Get all the views in the view holder to be used in onBindViewHolder and onClickListeners
        public NotificationViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.item_image);
            nameTextView = itemView.findViewById(R.id.Notification_item_name);
            quantityEditViewText = itemView.findViewById(R.id.Notification_QTY_view);
            incrementBtn = itemView.findViewById(R.id.Notification_btn_increment);
            decrementBtn = itemView.findViewById(R.id.Notification_btn_decrement);
        }
    }


    /* *********************************************************************************************
     *  TODO: This method initializes the view holder for the adapter and inflates the view.
     * ********************************************************************************************/
    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.table_row_notification, parent, false);
        return new NotificationViewHolder(view);
    }


    /* *********************************************************************************************
     *  TODO: This method binds the data to the view holder and sets the onClickListeners for the
     *   increment / decrement buttons. Also, updates the min quantity for the item in the database.
     * ********************************************************************************************/
    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        TableRowData item = dataList.get(position);

        // Load image into the image view, or fallback
        if (item.getColumn1() != null && !item.getColumn1().isEmpty()) {
            File imgFile = new File(item.getColumn1());
            if (imgFile.exists()) {
                Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                holder.imageView.setImageBitmap(bitmap);
            } else {
                holder.imageView.setImageResource(R.drawable.product_image);
            }
        } else {
            holder.imageView.setImageResource(R.drawable.product_image);
        }

        // Set Name text field
        holder.nameTextView.setText(item.getColumn3());

        // Set quantity text field
        holder.quantityEditViewText.setText(String.valueOf(item.getColumn5()));

        // Enable direct editing to allow user to change quantity directly from the edit text field
        holder.quantityEditViewText.setEnabled(false);

        // Increment minimum quantity for item in database when + button is clicked.
        holder.incrementBtn.setOnClickListener(v -> {
            int currentQty = item.getColumn5();
            int newQty = currentQty + 1;
            updateItemMinQuantity(item, newQty, holder.getAdapterPosition());
        });

        // Decrement minimum quantity for item in database when + button is clicked.
        holder.decrementBtn.setOnClickListener(v -> {
            int currentQty = item.getColumn5();
            if (currentQty > 0) {
                int newQty = currentQty - 1;
                updateItemMinQuantity(item, newQty, holder.getAdapterPosition());
            } else {
                Toast.makeText(context, "Quantity cannot be less than 0", Toast.LENGTH_SHORT).show();
            }
        });
    }


    /* *********************************************************************************************
     *  TODO: This method updates the min quantity for an item, and updates the UI and database
     * ********************************************************************************************/
    private void updateItemMinQuantity(TableRowData item, int newQuantity, int position) {

        Log.d("PrintLog", "In Notification Activity: Updating min quantity for: "
                + item.getColumn2() + " to " + newQuantity);

        // Debug log
        /* Log.d("PrintLog", "getColumn1: " + item.getColumn1()
                + " getColumn2: " + item.getColumn2()
                + " getColumn3: " + item.getColumn3()
                + " getColumn4: " + item.getColumn4()
                + " getColumn5: " + item.getColumn5());
        Log.d("PrintLog", "Database: " + dbHelper.getItemImgNameMinQuantity()); */

        // Update the in memory quantity for item's quantity_min variable
        item.setColumn5(newQuantity);
        // Update UI with new quantity
        notifyItemChanged(position);

        // Update database
        boolean success = dbHelper.updateItemMinQuantity(item.getColumn2(), newQuantity);

        // Output update status message.
        if (!success) {
            Toast.makeText(context, "Failed to update database", Toast.LENGTH_SHORT).show();
            Log.w("PrintLog", "DB update failed for SKU: " + item.getColumn2());
        } else {
            Toast.makeText(context, "Min Quantity Updated", Toast.LENGTH_SHORT).show();
            Log.d("PrintLog", "Min Quantity Updated Successfully");
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    // Method to update the data in the adapter. Used by the onResult() method in the NotificationActivity.
    public void setData(List<TableRowData> newData) {
        dataList.clear();
        dataList.addAll(newData);
        notifyDataSetChanged();
    }
}
