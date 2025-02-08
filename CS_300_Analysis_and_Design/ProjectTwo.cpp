//============================================================================
// Name        : BinarySearchTree.cpp
// Author      : Daniel Gorelkin
// Version     : 1.0
// Copyright   : Copyright � 2023 SNHU COCE
// Description : ABCU academic advisors - Binary Search Tree
//============================================================================

#include <iostream>
#include <time.h>
#include <vector>
#include <fstream>
#include <sstream>
#include <string>

using namespace std;

//============================================================================
// Global definitions visible to all methods and classes
//============================================================================

// forward declarations
double strToDouble(string str, char ch);

// define a structure to hold course information
struct Course {
    string courseId; // unique identifier
    string courseTitle;
    vector<string> prerequisites;
    
    Course(string courseNum = "", string title = "") {
        courseId = courseNum;
        courseTitle = title;
    }
};

// Internal structure for tree node
struct Node {
    Course course;
    Node *left;
    Node *right;

    // default constructor
    Node() {
        left = nullptr;
        right = nullptr;
    }

    // initialize with a course
    Node(Course aCourse) :
            Node() {
        course = aCourse;
    }
};

//============================================================================
// Binary Search Tree class definition
//============================================================================

/**
 * Define a class containing data members and methods to
 * implement a binary search tree
 */
class BinarySearchTree {

private:
    Node* root;

    void addNode(Node* node, Course course);
    void inOrder(Node* node);
    void postOrder(Node* node);
    void preOrder(Node* node);
    Node* removeNode(Node* node, string courseId);

public:
    BinarySearchTree();
    virtual ~BinarySearchTree();
    void PrintInOrder();
    void PostOrder();
    void PreOrder();
    void Insert(Course course);
    void Remove(string courseId);
    void deleteTree(Node* node);
    int Size();
    Course Search(string courseId);

    int coursesRead = 0;
};

/**
 * Default constructor
 */
BinarySearchTree::BinarySearchTree() {
    // Initialize housekeeping variables, set tree root to null pointer.
    root = nullptr;
}

/**
 * Destructor
 */
BinarySearchTree::~BinarySearchTree() {

    // Recursively delete every node from the leaves to the root.
    deleteTree(root);
}

/**
 * Destructor helper: recursively deletes left and righr subtrees and finaly, the node itself.
 */
void BinarySearchTree::deleteTree(Node* node) {
    
    // If node is not empty, delete left and right child from the leaf upwards.
    if (node != nullptr) {
        deleteTree(node->left);
        deleteTree(node->right);
        // cout << "Node: " << node << "Deleted." << endl;
        delete node;
    }
}

/**
 * Traverse the tree and display courses in order.
 */
void BinarySearchTree::PrintInOrder() {

    // Check if no courses have not yet been loaded and output prompt.
    if (coursesRead == 0) {
        cout << "Please load courses first." << endl;
        return;
    }

    // Display how many courses exist in BST.
    cout << coursesRead << " Courses found:" << endl;

    // Calls inOrder fuction and passes root node to traverse the tree from leaf to root and output courses to display.
    inOrder(root);
}

/**
 * Traverse the tree in post-order
 */
void BinarySearchTree::PostOrder() {
    // FixMe (4a): Post order root
    // postOrder root
}

/**
 * Traverse the tree in pre-order
 */
void BinarySearchTree::PreOrder() {
    // FixMe (5a): Pre order root
    // preOrder root
}

/**
 * Insert a course
 */
void BinarySearchTree::Insert(Course course) {

    // If tree root is empty, create the root node and populate the first course. 
    if (root == nullptr) {
        root = new Node(course);

        // cout << "Root course: " << course.courseId << " Address: " << &course << endl;
    }
    else {
        // Evaluate the course and insert it to the right place in the tree.
        addNode(root, course);
    }
}

/**
 * Remove a course
 */
void BinarySearchTree::Remove(string courseId) {

    // Search the tree starting from the root node and remove node courseID if found.
    root = removeNode(root, courseId);
}

/**
 * Search for a course
 */
Course BinarySearchTree::Search(string courseId) {

    // Set current node equal to root
    Node* crrnt = root;

    // Empty course placeholder 
    Course returnCourse;

    // Keep looping downwards until bottom reached or matching courseId was found.
    while (crrnt != nullptr) {

        // if match found, return current course
        if (crrnt->course.courseId == courseId) {

            // course was found, set and return returnCourse as current course.
            returnCourse = crrnt->course;
            return returnCourse;
        }
        // if course is smaller than current node, shift pointer one level down and search left subtree
        else if (courseId < crrnt->course.courseId) {
            crrnt = crrnt->left;
        }
        // if course is greater than current node shift pointer one level down and search right subtree
        else {
            crrnt = crrnt->right;
        }
    }    
    // Course was not found, return empty course.
    return returnCourse;
}

/**
 * Add a course to some node (recursive)
 *
 * @param node Current node in tree
 * @param course course to be added
 */
void BinarySearchTree::addNode(Node* node, Course course) {
    
    // if new node is smaller than the root, add to left.
    if (course.courseId < node->course.courseId) {

        // if no left node exist, create and populate new left child node.
        if (node->left == nullptr) {
            // this node becomes left
            node->left = new Node(course);

            // cout << "Child course: " << course.courseId << " | Adrs: " << &course << " Parent: " << node->course.courseId << ", added to the left! " << endl;
        }
        else { // else, recurse down the left node (one height lower).
            addNode(node->left, course);
        }
    }
    // if new node is larger than the root, add to right.
    else {
        // if no right node exist, create and populate new right child node.
        if (node->right == nullptr) {
            // this node becomes right
            node->right = new Node(course);

            // cout << "Child course: " << course.courseId << " | Adrs: " << &course << " Parent: " << node->course.courseId << ", added to the right! " << endl;
        }
        else { // else, recurse down the right node (one height lower).
            addNode(node->right, course);
        }
    }
}

/**
 * Recursive traverse the tree from bottom to top (Leaf to Root).
 * Recursive prints out course data.
 * @param node Root node in tree
 */
void BinarySearchTree::inOrder(Node* node) {

    // if node is not empty, visit left or right subtree.
    if (node != nullptr) {

        // Recursively visit left subtree
        inOrder(node->left);

        // A tree leaf is reached, output course data.
        // Output course number, course name.
        cout << node->course.courseId << ": " << node->course.courseTitle << endl;  // Visit node

        // Recursively visit right subtree
        inOrder(node->right);
    }
}

/**
 * Returns the size of read/loaded courses.
 */
int BinarySearchTree::Size() {
    return coursesRead;
}

//============================================================================
// Static methods used for testing
//============================================================================

/**
 * Display course number and name to the console.
 *
 * @param course struct containing the course info
 */
void displayCourse(Course course) {
    cout << course.courseId << ": " << course.courseTitle << endl;
    return;
}

/**
 * Display course Prerequisites to the console.
 *
 * @param course struct containing the course info
 */
void displayPrerequisites(Course course) {
    if (!course.prerequisites.empty()) {
        cout << "\t - Prerequisites: ";

        for (string prerequisite : course.prerequisites) {
            cout << prerequisite << " | ";
        }
        cout << endl;
    }
    return;
}

/**
 * Remove a course from the tree (recursive)
 */
Node* BinarySearchTree::removeNode(Node* node, string courseId) {

    // if tree is empty and course not found, return empty pointer.
    if (node == nullptr) {
        cout << endl << "Tree is empty or course not found." << endl;
        return nullptr;  // Base case: if tree is empty or course not found
    }

    // Otherwise recurse down the left and search the next subtree.
    if (courseId < node->course.courseId) {

        // Check for match and if so, remove left node using recursive call and return course.
        node->left = removeNode(node->left, courseId);
    }
    
    // Otherwise recurse down the right and search the next subtree.
    else if (courseId > node->course.courseId) {

        // Check for match and if so, remove right node using recursive call and return course.
        node->right = removeNode(node->right, courseId);
    }

    // Cases to handles scenarios where a node with the matching courseId found and will be removed.
    else {

        // Remove leaf node with no successors.
        if (node->left == nullptr && node->right == nullptr) {

            // Display confirmation message.
            cout << endl << "Course ";
            displayCourse(node->course);
            cout << "\t was successfuly removed." << endl;

            // Update courses counter.
            coursesRead--;

            // Delete node and return pointer.
            delete node;
            return nullptr;
        }

        // Otherwise remove internal node with only one child (right successor)
        else if (node->left == nullptr) {

            // Display confirmation message.
            cout << endl << "Course ";
            displayCourse(node->course);
            cout << " was successfuly removed." << endl;

            // Update courses counter.
            coursesRead--;

            Node* temp = node->right;
            delete node;
            return temp;
        }

        // Otherwise remove internal node with only one child (left successor)
        else if (node->right == nullptr) {

            // Display confirmation message.
            cout << endl << "Course ";
            displayCourse(node->course);
            cout << " was successfuly removed." << endl;

            // Update courses counter.
            coursesRead--;

            Node* temp = node->left;
            delete node;
            return temp;
        }

        // Otherwise remove internal node with two childen (two successors)
        else {
            // Find the minimum node in the right subtree (successor)

            // Create temp node to hold right successor.
            Node* temp = node->right;

            // While left node is not empty keep moving temp left.
            while (temp->left != nullptr) {
                temp = temp->left;
            }

            // Make node course (right) equal to temp course (left)
            node->course = temp->course;

            // Remove the right successor node using recursive call.
            node->right = removeNode(node->right, temp->course.courseId);
        }
    }
    
    // return the found node
    return node;
}

/**
 * Load a CSV file containing courses into the BST recursively
 *
 * @param csvPath the path to the CSV file to load
 * @param bst pointer to the BinarySearchTree
 * @param targetCourse (optional) specific course ID to load recursively
 */
void loadCourses(string csvPath, BinarySearchTree* bst, string targetCourse = "") {
    ifstream file(csvPath);

    // Try to open a file.
    if (!file.is_open()) {
        cerr << "Error opening file!" << endl;
        return;
    }

    string line;
    bool courseInserted = false;  // Tracks if we inserted the course in this call for the recursion loadCourses() calls.

    // Read file line by line
    while (getline(file, line)) {
        stringstream ss(line);
        string cell;
        vector<string> row;

        while (getline(ss, cell, ',')) {
            if (!cell.empty()) {
                row.push_back(cell);
            }
        }

        // Validate that a read line has both course number and name, thus, it is valid. Othervise, skip invalid lines.
        if (row.size() < 2) {
            continue;  // Skip invalid lines
        }

        string courseId = row[0];
        string courseTitle = row[1];

        // Check if this course is already in the BST and avoids duplicate course insertion.
        if (!bst->Search(courseId).courseId.empty()) {

            // Course already exists, skip to avoid duplicate course insertion.
            continue;
        }

        // If targetCourse is specified, skip unrelated courses
        if (!targetCourse.empty() && courseId != targetCourse) {
            continue;
        }

        // Create new Course instance and initialize it with course number and course name.
        Course newCourse(courseId, courseTitle);
        bool isValid = true;

        // Validate prerequisites starting from the third column
        for (int i = 2; i < row.size(); i++) {
            string prereqId = row[i];
            Course prereqCourse = bst->Search(prereqId);

            // Checks if the prerequisite course (prereqCourse) exists in the BST.
            if (prereqCourse.courseId.empty()) {    // Returns true if courseId prerequisite hasn’t been inserted yet.
                // Recursively attempt to load the missing prerequisite
                // cout << "Prerequisite course " << prereqId << " not found for Course " << courseId << ". Searching recursively prerequisite " << prereqId << endl;
                cout << "Course " << courseId << " requires prerequisite " << prereqId << ". Searching recursively prerequisite " << prereqId << endl;

                // Recursive call to try to find, load and add the specific missing course prerequisite before the inspected courseId.
                // The recursive call by passing the prereqId, searches the entire CSV file for the missing prerequisite, if found, it inserts the prerequisite into the BST.
                // This allows the program to handle unsorted data, in case prerequisites may show up after dependent courses in the file.
                loadCourses(csvPath, bst, prereqId);

                // Rechecks if the prerequisite was successfully inserted after the recursive attempt has been called.
                prereqCourse = bst->Search(prereqId);

                // Avoid infinite recursion loop if the prerequisite trully doesn’t exist in the file and prevents inserting courses with missing prerequisites.
                if (prereqCourse.courseId.empty()) {
                    cout << "\t Prerequisite " << prereqId << " still missing after recursive load and found to be Invalid!!!"
                        << "\n\t Course " << courseId << " can not be added!!!" << endl;

                    // Mark prerequisite as invalid and stop validation / terminate recursive load.
                    isValid = false;
                    break;
                }
            }

            // Add confirmed prerequisite to the inspected courseId (newCourse) prerequisites vector.
            newCourse.prerequisites.push_back(prereqId);
        }

        // Insert the course to the BST if all prerequisites were checked and found as valid.
        if (isValid) {
            bst->Insert(newCourse);
            cout << "Course " << newCourse.courseTitle << " has been added." << endl;
            bst->coursesRead++; // Increment read corses counter by one apon successful course insertion.
            courseInserted = true;
        }
    }

    file.close();

    // Show an error while trying to load a specific targetCourse and failed because it is missing.
    if (!targetCourse.empty() && !courseInserted) {
        cout << "Error: Target course " << targetCourse << " could not be loaded." << endl;
    }
}


/**
 * Simple C function to convert a string to a double
 * after stripping out unwanted char
 *
 * credit: http://stackoverflow.com/a/24875936
 *
 * @param ch The character to strip out
 */
double strToDouble(string str, char ch) {
    str.erase(remove(str.begin(), str.end(), ch), str.end());
    return atof(str.c_str());
}

/**
 * The one and only main() method
 */
int main(int argc, char* argv[]) {

    // Process command line arguments
    string csvPath, courseKey;
    switch (argc) {
    case 2:
        csvPath = argv[1];
        courseKey = "NULL";
        break;
    case 3:
        csvPath = argv[1];
        courseKey = argv[2];
        break;
    default:
        csvPath = "";
        courseKey = "";
        // csvPath = "CS 300 ABCU_Advising_Program_Input.csv";
        // courseKey = "CSCI300";
    }

    // Define a timer variable
    clock_t ticks;

    // Define a binary search tree to hold all courses
    BinarySearchTree* bst = new BinarySearchTree();

    Course course;

    // Menu options:
    int choice = 0;
    while (choice != 9) {
        cout << endl;
        cout << "Menu:" << endl;
        cout << "  1. Load Courses" << endl;
        cout << "  2. Display All Courses" << endl;
        cout << "  3. Find Course" << endl;
        cout << "  4. Remove Course" << endl;
        cout << "  9. Exit" << endl;
        cout << "Enter choice: ";
        if (!(cin >> choice)) {          // Validae input is of type int.
            cout << endl << "Invalid input. Please try again!" << endl;
            cin.clear();
            cin.ignore(10000, '\n');
            continue;
        }

        switch (choice) {
        
        // Loads all courses by parsing line by line from a CSV file and loading the BST.
        case 1:

            // Initialize a timer variable before loading courses
            ticks = clock();

            cin.ignore();

            // Read file name from user if command line is empty.
            while (csvPath == "") {
                cout << "Enter filename: ";
                getline(cin, csvPath);
            }

            cout << endl; 

            // Call loadCourses() method to read the courses from the file and load the BST.
            loadCourses(csvPath, bst);

            cout << endl << bst->Size() << " courses read successfully" << endl;

            // Calculate elapsed time and display result
            ticks = clock() - ticks; // current clock ticks minus starting clock ticks
            cout << "time: " << ticks << " clock ticks" << endl;
            cout << "time: " << ticks * 1.0 / CLOCKS_PER_SEC << " seconds" << endl;
            
            // Clear file name / path for the next loop.
            csvPath = "";
            break;

        // Prints out a list of all the courses in the program in alphanumeric order to the monitor / display.
        case 2:
            cout << endl;
            bst->PrintInOrder();
            break;

        // Reads input from user and prints out a specigic course in the program and its prerequisites to the monitor / display is such a course is found in the BST.
        case 3:
            ticks = clock();

            cin.ignore();

            // Get Course Number from user if command line (argv[0]) is empty.
            if (courseKey.empty()) {
                cout << "Enter Course Number: ";
                getline(cin, courseKey);
            }
            // Read Course Number from user if command line (argv[1]) is empty.
            else if (courseKey == "NULL") {
                cout << "Enter Course Number: ";
                getline(cin, courseKey);
            }

            // Search for a specific course if it exists in the Binary Search Tree.
            course = bst->Search(courseKey);

            ticks = clock() - ticks; // current clock ticks minus starting clock ticks

            cout << endl << endl;

            // If requested course found in the BST, print course information.
            if (!course.courseId.empty()) {
                displayCourse(course);          // Print course number and course name to the display.
                displayPrerequisites(course);   // Print course prerequisites to the display if such exist.
                cout << endl;
            }
            else {  // If requested course was not found in the BST.
                cout << "Course Number: " << courseKey << " not found." << endl << endl;
            }

            cout << "time: " << ticks << " clock ticks" << endl;
            cout << "time: " << ticks * 1.0 / CLOCKS_PER_SEC << " seconds" << endl;

            courseKey = "";
            
            break;

            // Remove course from the BST.
        case 4:

            cin.ignore();

            // Get Course Number from user if command line (argv[0]) is empty.
            if (courseKey.empty()) {
                cout << "Enter Course Number to remove: ";
                getline(cin, courseKey);
            }
            // Read Course Number from user if command line (argv[1]) is empty.
            else if (courseKey == "NULL") {
                cout << "Enter Course Number to remove: ";
                getline(cin, courseKey);
            }

            bst->Remove(courseKey);

            courseKey = "";

            break;
        }
    }

    cout << "Good bye." << endl;

	return 0;
}
