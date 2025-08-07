package com.zybooks.mobile2app;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.List;

/* *********************************************************************************************
 * TODO: This class defines the adapter for the RecyclerView in the activity_database.xml layout
 * *********************************************************************************************/
public class TableAdapter extends RecyclerView.Adapter<TableAdapter.TableViewHolder> {

    private final List<TableRowData> DATALIST;
    private final DatabaseHelper dbHelper;
    private final OnDeleteClickListener deleteClickListener;

    // *********************************************************************************************
    /* TODO: Constructor - takes in a list of TableRowData objects and an OnDeleteClickListener and
     *  sets them as instance variables. The OnDeleteClickListener is used to handle delete button
     *  clicks, and the DatabaseHelper is used to update the database.
     **********************************************************************************************/
    public TableAdapter(List<TableRowData> dataList, DatabaseHelper dbHelper, OnDeleteClickListener listener) {
        this.DATALIST = dataList;
        this.dbHelper = dbHelper;
        this.deleteClickListener = listener;
    }


    // *********************************************************************************************
    /* TODO: This class defines the ViewHolder for each row in the RecyclerView
     **********************************************************************************************/
    public static class TableViewHolder extends RecyclerView.ViewHolder {
        ImageView col1Image;
        TextView col2, col3, col4;
        ImageView deleteItem;

        // *********************************************************************************************
        /* TODO: Read the text view ids from the table_row_item.xml layout
        // and set them to the corresponding variables. Columns 1 and 5 are kept empty for image rendering
         **********************************************************************************************/
        public TableViewHolder(View itemView) {
            super(itemView);
            col1Image = itemView.findViewById(R.id.col1_image);
            col2 = itemView.findViewById(R.id.col2);
            col3 = itemView.findViewById(R.id.col3);
            col4 = itemView.findViewById(R.id.col4);
            // Find the delete button in the table_row_item.xml layout
            deleteItem = itemView.findViewById(R.id.delete_item);
        }
    }


    /* *********************************************************************************************
     * Todo: This Interface will be used for handling delete button clicks coming from each row in
     * the RecyclerView (table_row_item.xml) through a click listener in the activity_database.xml
     * ********************************************************************************************/
    public interface OnDeleteClickListener {
        void onDeleteClick(int position, TableRowData rowData);
    }

    /* *********************************************************************************************
     * Todo: Create a new ViewHolder for each row in the RecyclerView and inflate
     *  the table_row_item.xml layout
     * ********************************************************************************************/
    @Override
    public TableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.table_row_database, parent, false);
        return new TableViewHolder(view);
    }

    /* *********************************************************************************************
     * Todo: Bind the data from the TableRowData object to the corresponding text views in the ViewHolder
     * ********************************************************************************************/
    @Override
    public void onBindViewHolder(TableViewHolder holder, int position) {

        TableRowData row = DATALIST.get(position);

        // Load image from file path if available
        if (row.getColumn1() != null && !row.getColumn1().isEmpty()) {
            File imgFile = new File(row.getColumn1());
            if (imgFile.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                holder.col1Image.setImageBitmap(myBitmap);
            } else {
                holder.col1Image.setImageResource(R.drawable.product_image); // fallback
            }
        } else {
            holder.col1Image.setImageResource(R.drawable.product_image); // fallback
        }

        // Set text for other columns
        holder.col2.setText(row.getColumn2());
        holder.col3.setText(row.getColumn3());
        holder.col4.setText(String.valueOf(row.getColumn4()));

        // Add delete button logic for each row in the RecyclerView
        holder.deleteItem.setOnClickListener(v -> {
            if (deleteClickListener != null) {
                deleteClickListener.onDeleteClick(holder.getAdapterPosition(), row);
            }
        });

    /* *********************************************************************************************
     * Todo: Add a long click listener to the row item to
     *  trigger a dialog when a row in a RecyclerView is long-pressed
     * ********************************************************************************************/
        // Get the item for the selected row
        TableRowData item = DATALIST.get(position);

        // Set the long click listener for every row
        holder.itemView.setOnLongClickListener(view -> {
            // Open the Quantity Dialog when a row is long-pressed
            showQuantityDialog(view.getContext(), item, position);
            return true;
        });
    }

    /* *********************************************************************************************
     * Todo: Open a dialog to allow the user to adjust the quantity of an item in the RecyclerView
     *  and update the database accordingly.
     *  If the new quantity is below the minimum quantity, a notification will be sent to the user.
     * ********************************************************************************************/
    private void showQuantityDialog(Context context, TableRowData item, int position) {
        // Creates an `EditText` field to be used as the input field inside the dialog
        final EditText input = new EditText(context);

        // Sets the input type to number only and sets the hint text
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        input.setHint("Enter new quantity");

        // Create an AlertDialog
        new AlertDialog.Builder(context)
                .setTitle("Adjust Quantity")
                // Embed the EditText
                .setView(input)
                // Define the buttons for the dialog
                .setPositiveButton("Update", (dialog, which) -> {
                    // Get the new quantity from the input field
                    String inputText = input.getText().toString().trim();
                    if (!inputText.isEmpty()) {
                        // Parse the new quantity as an integer
                        int newQuantity = Integer.parseInt(inputText);
                        // Updates the quantity field (column 4) of the data model
                        item.setColumn4(newQuantity);
                        // Update the RecyclerView
                        notifyItemChanged(position);

                        // Update the database
                        boolean success = dbHelper.updateItemQuantity(item.getColumn2(), newQuantity);
                        if (!success) {
                            Toast.makeText(context, "Database update failed", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Item quantity updated successfully", Toast.LENGTH_SHORT).show();

                            // Get item's minimum quantity from the database
                            int minQuantity = dbHelper.getMinQuantity(item.getColumn2());
                            Log.d("PrintLog", "Item's " + item.getColumn3() +
                                    " minimum quantity: " + minQuantity + ". New on hand QTY set to: " + newQuantity);

                            if (newQuantity < minQuantity) {
                                Log.d("PrintLog", "Item " + item.getColumn3() +
                                        " is below the allowed minimum quantity!" +
                                        " -> Sending notification if permitted...");

                                // TODO: Send a notification
                                // A notification will be sent to the user if the new quantity is
                                // below the minimum quantity. The NotificationHelper() class is used
                                // to send the notification.
                                NotificationHelper.sendLowStockNotification(context,
                                        item.getColumn3(), newQuantity, "Low Inventory Alert");
                            }
                        }
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    public int getItemCount() {
        return DATALIST.size();
    }
}
