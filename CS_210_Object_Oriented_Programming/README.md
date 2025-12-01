Project_Three - Grocery_Tracking App

The Grocery Tracking app helps businesses summarize the number of items sold during the day. It shows a list of items sold and the total amount of each item sold in a numerical and histogram presentation.
The app reads the data from a .txt file, creates an object for each product, and identifies, counts, and stores each item in a storage vector.
Through its menu, the program allows the user to search for a particular sold product as a search query and output the quantity of its sold products.
The app creates and stores a .dat backup file in the dir folder as it finishes to read all the data from the .txt file.
The program is easily maintainable and upgradable as it stores all its functions in the 'Corner_Functions.cpp' file.
It manages its objects through the base class object item and the derived from product class, which can be modified or easily swapped.
The 'Corner_Grocer_Main.cpp' file executes the program and could be swiftly expanded for additional function operations. The program supports the principles of OOP, input validation, and runtime error handling.
Code's drawback: This code requires the data read from the .txt file to be structured in an ordered manner of one item/product per line without considering possible read errors from the input file. This means the program can read and store irrelevant values from the .txt file if those do not appear in the expected pattern on the file. This scope of the code should be revised and upgraded.
