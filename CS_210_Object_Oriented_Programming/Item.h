#ifndef ITEM_H
#define ITEM_H

#include <iostream>
#include <string>
using namespace std;

// Base class
class Item {

public:
	// Constructor.
	Item(string product = "Null") {
		this->product = product;
	};

	// Getters	----------------------------
	string GetProductName() const {
		return this->product;
	}

	// Setters	----------------------------
	void SetProductName(string product) {
		this->product = product;
	}

protected:	// To be accessable by Product Class.
	// Class members.
	string product;
};

#endif