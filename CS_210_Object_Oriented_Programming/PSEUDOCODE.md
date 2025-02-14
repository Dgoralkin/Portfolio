CS 210 Project Three - Corner Grocer app
Created by Daniel Gorelkin
April 15, 2024
  
Run program.
Try to read data from .txt file.									// (Function: readSoldProducts())
If file was successfully opened
	Read file line by line, and capitalize every line, product.		// (Function: capitilize())
	Push read product into a storage vector.						// (Function: addSoldProduct())
		If product doesn’t yet exist in the vector:
			Set object, and add to the vector.
		Else, increment object’s quantity by one.					// (Class Function: AddItemsSold())
	If encounter error while writing file, Throw runtime error (#2) and quit app, else close file.
Else: If file could not be opened, throw runtime error (#1) and quit app.
 
Try to write (back up) data from the storage vector to .dat file.	// (Function: backUpSoldProducts())
  
If encounter writing error, Throw runtime error (#3) and quit app, else close file for writing.
Set while loop:
Get and Validate user input.										// (Function: getAction())
If input is unrecognized: Print prompt and repeat loop.
 
Print main menu.													// (Function: printMenu())
If input is “1”:													// (Function: searchSoldProduct())
	Get user input.
		If item found in the storage vector: Output object’s itemsSold Quantity.
		Else: Output item not found prompt.
	Return and repeat while loop
 
If input is “2”: 													// (Function: printSoldProducts())
	Print a table of all the sold products and the itemsSold Quantity for each product.
Return and repeat while loop.
 
If input is “3”: 													// (Function: printProductsHistogram())
	Print a table of all the sold products and a sales graph (histogram) for each product.
Return and repeat while loop.
 
If input is “Q”: 
End while loop.
Clear allocated memory												// (Function: clearVector())
End program.
