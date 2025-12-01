package com.zybooks.mobile2app;

/* *********************************************************************************************
 * This object defines the data for each row in the RecyclerView
 * it defines the second, third, and fourth columns of the table
 * and the getters and setters for each row item
 * *********************************************************************************************/
public class TableRowData {
    private String imagePath, column2, column3;
    private int column4, column5;

    // Constructor
    public TableRowData(String imagePath , String sku, String name, int quantity) {
        this.imagePath = imagePath;
        this.column2 = sku;
        this.column3 = name;
        this.column4 = quantity;
    }

    public TableRowData(String imagePath , String sku, String name, int quantity, int min_quantity) {
        this.imagePath = imagePath;
        this.column2 = sku;
        this.column3 = name;
        this.column4 = quantity;
        this.column5 = min_quantity;
    }

    // Getters
    public String getColumn1() { return imagePath; }
    public String getColumn2() { return column2; }
    public String getColumn3() { return column3; }
    public int getColumn4() { return column4; }
    public int getColumn5() { return column5; }

    // Setters
    public void setColumn1(String column1) { this.imagePath = column1; }
    public void setColumn2(String column2) { this.column2 = column2; }
    public void setColumn3(String column3) { this.column3 = column3; }
    public void setColumn4(int column4) { this.column4 = column4; }
    public void setColumn5(int column5) { this.column5 = column5; }

    @Override
    public String toString() {
        return "SKU: " + column2 + ", Name: " + column3 + ", Quantity: " + column4 +
                ", ImagePath: " + imagePath + ", quantity_min: " + column5;
    }
}
