#include <iostream>
#include <iomanip>
#include <stdexcept>
#include <vector>
#include "Corner_Functions.h"

using namespace std;

int main() {

    // Variables.
    const char EXIT_CODE = 'Q';
    char userAction = '?';
    string FILE_TO_READ = "CS210_Project_Three_Input_File.txt";
    string FILE_TO_WRITE = "frequency.dat";

    try {

        // Reads sales data from FILE_TO_READ file.
        readSoldProducts(FILE_TO_READ);

        // Writes sales back up data to FILE_TO_WRITE file.
        backUpSoldProducts(FILE_TO_WRITE);

        // Outputs main menu.
        printMenu();

        // Read initial user input.
        userAction = getAction();

        while (userAction != EXIT_CODE) {
            switch (userAction) {
            case '1':                               // Search for a sold item.
                searchSoldProduct();
                break;
            case '2':                               // Show a list of sold items.
                cout << "2" << endl;
                printSoldProducts();
                break;
            case '3':                               // Show sales popularity.
                cout << "3" << endl;
                printProductsHistogram();
                break;
            case 'Q':                               // Quit.
                cout << endl << "See you next time :-)" << endl;
                break;
            default:                                // Unrecognized command output prompt.
                cout << endl << "Unrecognized command. Please try again!" << endl;
                cout << setfill('-') << setw(39) << "";
                break;
            }

            // Output menu and get next user input.
            printMenu();
            userAction = getAction();
        }
    }

    // Catches runtime errors.
    catch (runtime_error& excpt) {

        string errorCode;

        // Read recieved error code.
        errorCode = excpt.what();

        // Output error code response.
        if (errorCode == "1") {
            cout << "Could not open file "<< FILE_TO_READ << endl;
        }
        else if (errorCode == "2") {
            cout << "Error reading " << FILE_TO_READ << endl;
        }
        else if (errorCode == "3") {
            cout << "Error writing to " << FILE_TO_READ << endl;
        }
        else {
            cout << "Other runtime_error..." << endl;
        }
        cout << "Runtime_error... Quit app" << endl;
    }

    // Clear created class object's memory.
    clearVector();
    
    cout << endl;

    return 0;
}