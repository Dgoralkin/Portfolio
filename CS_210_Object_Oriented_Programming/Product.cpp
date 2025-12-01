#include <iostream>
#include <string>
#include "Product.h"

using namespace std;

// Constructor.
Product::Product (string product, int itemsSold) {
	// Set class variables.
	this->product = product;
	this->itemsSold = itemsSold;
	
	// Displays created memory addresses.
	cout << "Object created @: " << this << endl;
}

// Destructor.
Product::~Product() {
	// Displays deleted memory addresses.
	cout << endl << "Curr Object Deleted: " << this;
}

// Getters	----------------------------
// Returns quantity sold.
int Product::GetItemsSold() const {
	return this->itemsSold;
}

// Setters	----------------------------
// Function increments itemsSold by one.
void Product::AddItemsSold() {
	this->itemsSold += 1;
}