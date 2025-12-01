#ifndef PRODUCT_H
#define PRODUCT_H

#include <iostream>
#include <string>
#include "Item.h"		// Base Class Item declared in "Item.h"
using namespace std;


// Class Product derived from Class Item (Item.h)
class Product : public Item {

public:
	// Constructor.
	Product(string product = "Null", int itemsSold = 1);

	// Destructor.
	~Product();

	// Member functions.
	int GetItemsSold() const;
	void AddItemsSold();

private:
	// Class members.
	int itemsSold;
};

#endif