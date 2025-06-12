
# AnimalShelter Project - CS 340

## About the Project

The **AnimalShelter** project simplifies the connection to the **AAC MongoDB database**, enabling seamless **Create, Read, Update, and Delete (CRUD)** operations. This middleware layer bridges the MongoDB database and your application logic, abstracting internal implementation concerns.

---

## Motivation

This project aims to save development time by providing a secure and efficient bridge between a frontend application and a MongoDB database. It ensures proper authentication and authorization using the minimum required permissions. Though currently in development, it is open-source and open to contributions and pull requests. The implementation can be adapted for use with a local MongoDB instance or a cloud deployment like MongoDB Atlas.

---

## Getting Started

### Prerequisites

- MongoDB installed on your local machine
- Python 3.x
- `pymongo`, `bson`, and standard Python libraries

### Setting up the Database

1. Run your MongoDB server locally.
2. Import the provided CSV file into your local AAC database.
3. Create a database user:

   ```bash
   use AAC
   db.createUser({
     user: "aacuser",
     pwd: "your_password",
     roles: [{ role: "readWrite", db: "AAC" }]
   })
   ```

4. Create indexes to optimize query performance:

   ```python
   db.animals.createIndex({ "breed": 1, "outcome_type": 1 })
   ```

---

## Installation

To install the project package into your environment:

```bash
pip install git+https://github.com/Dgoralkin/AnimalShelter.git
```

After installation, import the package in your Python code:

```python
from AnimalShelter import AnimalShelter
```

Make sure you also import the following:

```python
from bson.objectid import ObjectId
from datetime import datetime
import os
```

> Sensitive credentials should be stored as environment variables.

---

## Usage

### Connecting to the Database

```python
shelter = AnimalShelter("aacuser", "your_password", "localhost", 27017, "AAC", "animals")
```

### CRUD Examples

#### Create

```python
new_animal = {
    '_id': ObjectId('682d5fbc2a579703eb609fc4'),
    'breed': 'Domestic Shorthair Mix',
    'animal_id': 'A777777',
    'date_of_birth': '4/10/2014',
    'datetime': datetime.now().strftime('%#m/%#d/%Y %#H:%M'),
    'location_lat': 49.809566429306486,
    'name': 'Animal_Test',
    'outcome_type': 'Transfer',
}
shelter.create(new_animal)
```

#### Read

```python
find_animal = {
    'breed': 'Domestic Shorthair Mix',
    'outcome_type': 'Transfer'
}
results = shelter.read(find_animal)
```

#### Update

```python
update_animal = {
    'filter_fields': {'breed': 'Domestic Shorthair Mix', 'outcome_type': 'Transfer'},
    'update_fields': {'breed': 'Test Domestic Shorthair Mix', 'outcome_type': 'Test Transfer'}
}
shelter.update(update_animal)
```

Use `update_many=True` to apply changes to all matching documents.

#### Delete

```python
delete_animal = {
    'filter_fields': {'name': 'Test Animal Name', 'outcome_type': 'Transfer'}
}
shelter.delete(delete_animal)
```

Use `delete_many=True` to delete all matching documents.

---

## Tests

To test your functions:

1. Open a Jupyter Notebook and import the required modules.
2. Try calling each function with valid and invalid inputs to verify error handling.

Examples include:
- Testing `create()` with an empty dictionary should return `False`.
- Testing `read()` with invalid input should return an empty list.
- `update()` and `delete()` can be tested by checking return values and verifying changes in the database.

---

## Roadmap

- Enhanced error handling
- Role-based access control
- Improved logging and validation
- Web integration for frontend testing

---

## Contact

**Daniel Gorelkin**  
📧 [Daniel.Gorelkin@SNHU.edu](mailto:Daniel.Gorelkin@SNHU.edu)  
🔗 [GitHub Profile](https://github.com/Dgoralkin)

---

> _This README was adapted from templates like "Make a README", "Best README Template", and "A Beginner’s Guide to Writing a Kickass README"._
