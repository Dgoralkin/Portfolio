#ifndef CORNER_FUNCTIONS_H
#define CORNER_FUNCTIONS_H

#include "Product.h"		// Derived class
// #include "Product.h"		// Base class

// Outputs main menu.
void printMenu();

// Function reads and validates user input. Returns char input.
char getAction();

// Reads sales data from .txt file.
void readSoldProducts(string& fileToRead);

// Writes sales back up data to .dat file.
void backUpSoldProducts(string& fileToRead);

// Adds sold product to the products vector
//  and updated objects itemsSold member.
void addSoldProduct(string& newProduct);

// Prints a list of all products and their sell frequency.
void printSoldProducts();

// Prints a histogram list of all products.
void printProductsHistogram();

//	Function reads user input and outputs numeric value
//	for the frequency of the specific word if such was found.
void searchSoldProduct();

// Function recieves and returns capitalized strings.
string capitilize(string& phrase);

// Clear created class object's memory.
void clearVector();
#endif