#include <iostream>
#include <iomanip>
#include <string>
#include <limits>
#include <fstream>
#include <vector>

#include "Corner_Functions.h"

using namespace std;

// Vector to store Product's addresses.
vector<Product*> storeProducts;

// Outputs main menu.
void printMenu() {

    // Variables.
    const string PROMPT_ACTION = "   Please enter your command:\n---------------------------------";
    const string MENU_OPTIONS = "1) - Search for a sold item.\t|\n2) - Show all sold items.\t|\n3) - Show sales popularity.\t|\n4) - Enter \"q/Q\" to Quit.\t|";

    cout << endl << PROMPT_ACTION << endl;
    cout << MENU_OPTIONS << endl;
    cout << "---------------------------------" << endl;

    return;
}

// Function reads and validates user input. Returns char input.
char getAction() {

    // Local variables.
    string input;
    char command = '0';

    cin >> input;

    // While input isn't a single char, return 0.
    while (input.size() > 1) {
        command = '0';
        cin.ignore(numeric_limits<streamsize>::max(), '\n');

        return command;
    }

    // Capitilize and return single char read input.
    command = toupper(input.at(0));
    cin.ignore(numeric_limits<streamsize>::max(), '\n');
    return command;
}

// Reads sales data from input .txt file.
void readSoldProducts(string& fileToRead) {
    
    string readProduct, soldProduct;

    // Open input file stream 
    ifstream inFS;
    cout << "Opening file \"" << fileToRead << "\" for reading." << endl;
    inFS.open(fileToRead);

    // If file could not be read, throw runtime_error #1.
    if (!inFS.is_open()) {
        throw runtime_error("1");
    }

    cout << endl;

    // Keep reading lines until end of file reached.
    while (!inFS.eof() && inFS.good()) {
        inFS >> readProduct;

        if (!inFS.fail()) {

            // Capitalize read line.
            soldProduct = capitilize(readProduct);

            // Add read product to the products vector.
            addSoldProduct(soldProduct);
        }
    }

    // If end-of-file not reached, throw runtime_error #2.
    if (!inFS.eof()) {
        inFS.close();
        throw runtime_error("2");
        exit(EXIT_FAILURE);
    }

    // Close read file.
    inFS.close();
    cout << endl << "File \"" << fileToRead << "\" read successfully. Closing file." << endl;

    return;
}

// Reads sales data from input .txt file.
void backUpSoldProducts(string& fileToWrite) {

    string writeProduct;

    // Open input file stream 
    ofstream writeFile;
    cout << endl << "Opening file \"" << fileToWrite << "\" for writing." << endl;

    // Try to open file for writing.
    writeFile.open(fileToWrite);

    // If can not write to file, throw writing error.
    if (!writeFile.is_open()) {
        cout << "Could not open file myoutfile.txt." << endl;
        throw runtime_error("3");
        exit(EXIT_FAILURE);
    }

    // Write header line to file
    writeProduct = "Product\t\t Items sold\n-----------------------";
    writeFile << writeProduct << endl;

    // Read objects from the storage vector, and write each to .dat file.
    for (int i = 0; i < storeProducts.size(); i++) {

        // Alignment and Indentation mechanism.
        if (storeProducts.at(i)->GetProductName().size() < 8) {
            writeProduct = storeProducts.at(i)->GetProductName() + "\t\t Sold -> " + to_string(storeProducts.at(i)->GetItemsSold());
        }
        else {
            writeProduct = storeProducts.at(i)->GetProductName() + "\t Sold -> " + to_string(storeProducts.at(i)->GetItemsSold());
        }

        // Write line to file
        writeFile << writeProduct << endl;
    }

    // Done writing to file.
    writeFile.close();
    cout << endl << "File \"" << fileToWrite << "\" written successfully. Closing file." << endl;

    return;
}

// Adds sold product to the products vector and updated objects itemsSold member.
void addSoldProduct(string& newProduct) {
    
    // Add first object into empty vector case
    if (storeProducts.size() == 0) {
        // Set a new class object and add it to the products array.
        Product* soldProduct = new Product(newProduct);
        storeProducts.push_back(soldProduct);
    }
    else {
        // Inspect all vector's objects
        for (int i = 0; i < storeProducts.size(); i++) {

            // If product exist in the vector, update it's "itemsSold" member.
            if (newProduct == storeProducts.at(i)->GetProductName()) {
                storeProducts.at(i)->AddItemsSold();
                return;
            }
        }

        // If product is not in the vector, create new object and add it to the vector.
        Product* soldProduct = new Product(newProduct);
        storeProducts.push_back(soldProduct);
    }
    return;
}

// Function reads user input and outputs numeric value for the frequency of the specific word if such was found.
void searchSoldProduct() {

    // Variables.
    string product, input;

    cout << "   Enter item to search:\n---------------------------------" << endl;

    // Read and capitalize user input.
    getline(cin, input);
    product = capitilize(input);

    // Search the product in the products vector.
    for (int i = 0; i < storeProducts.size(); i++) {
        
        // If product found, output it's numeric value (ItemsSold)
        if (product == storeProducts.at(i)->GetProductName()) {
            cout << endl << "   " << storeProducts.at(i)->GetItemsSold() << " " << storeProducts.at(i)->GetProductName() << "/s sold today." << endl;
            cout << "---------------------------------" << endl;
            return;
        }
    }
    // Prompt if product wasn't found.
    cout << endl << "   " << product << " wasn't sold today." << endl;
    cout << "---------------------------------" << endl;

    return;
}

// Prints a list of all products and their sell frequency.
void printSoldProducts() {

    cout << "   Sold items:" << endl;
    cout << setfill('-') << setw(25) << "" << endl;
    cout << setfill(' ') << "";
    cout << "| ";
    cout << left << setw(12) << "   Product    ";
    cout << "| ";
    cout << setfill(' ') << "";
    cout << left << setw(3) << "Sold  |";
    cout << endl;
    cout << setfill('-') << setw(25) << "" << endl;

    for (int i = 0; i < storeProducts.size(); i++) {

        cout << setfill(' ') << "";
        cout << "| ";
        cout << right << setw(2) << i + 1 << ") ";
        cout << setfill(' ') << "";
        cout << left << setw(12) << storeProducts.at(i)->GetProductName();
        cout << setfill(' ') << "";
        cout << "-> ";
        cout << left << setw(3) << storeProducts.at(i)->GetItemsSold();
        cout << "|";
        cout << endl;
    }
    cout << setfill('-') << setw(25) << "" << endl;
    return;
}

// Prints a histogram list of all products.
void printProductsHistogram() {
    
    cout << "   Sold items:" << endl;
    cout << setfill('-') << setw(33) << "" << endl;
    cout << setfill(' ') << "";
    cout << "| ";
    cout << left << setw(12) << "Product";
    cout << "| ";
    cout << setfill(' ') << "";
    cout << left << setw(3) << "Quantity sold   |";
    cout << endl;
    cout << setfill('-') << setw(33) << "" << endl;

    for (int i = 0; i < storeProducts.size(); i++) {

        cout << setfill(' ') << "";
        cout << "| ";
        cout << left << setw(12) << storeProducts.at(i)->GetProductName();
        cout << "| ";
        cout << setfill(' ') << "";
        cout << left << setw(storeProducts.at(i)->GetItemsSold()) << setfill('*') << "";
        cout << endl;
    }
    cout << setfill('-') << setw(33) << "" << endl;
    return;
}

// Function recieves and returns capitalized strings.
string capitilize(string& phrase) {

    for (int i = 0; i < phrase.size(); i++) {
        char letter;
        if (i == 0) {
            letter = toupper(phrase.at(i));
            phrase.at(i) = letter;
        }
        else {
            letter = tolower(phrase.at(i));
            phrase.at(i) = letter;
        }
    }
    return phrase;
}

// Clear created class object's alocated memory.
void clearVector() {
    for (int i = 0; i < storeProducts.size(); i++) {
        delete storeProducts.at(i);
    }
}
