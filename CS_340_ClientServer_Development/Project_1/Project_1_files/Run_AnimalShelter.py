# Standard library imports
import os
from datetime import datetime

# Third-party imports
from bson import ObjectId
from AnimalShelter import AnimalShelter

def main():
    """Demonstrates CRUD operations on the AnimalShelter database."""

    # Define an 'admin' user with 'root' permission (for maintenance purposes only).
    if os.getenv("AAC_ADMIN"):
        user = os.getenv("AAC_ADMIN")
        password = os.getenv("AAC_ADMIN_PASS")
    else:   # Connect as a regular 'readWrite' user
        user = "aacuser"
        password = "SNHU1234"

    # Define animal parameters to add to the 'animals' collection.
    new_animal = {
        '_id': ObjectId('682d5fbc2a579703eb609fc4'),
        'age_upon_outcome': '777 years',
        'age_upon_outcome_in_weeks': 777.7777777,
        'animal_id': 'A777777',
        'animal_type': 'Cat',
        'breed': 'Domestic Shorthair Mix',
        'color': 'Black/White',
        'date_of_birth': '4/10/2014',
        'datetime': datetime.now().strftime('%#m/%#d/%Y %#H:%M'),
        'location_lat': 49.809566429306486,
        'location_long': -97.16118309491378,
        'monthyear': '2017-04-11T09:00:00',
        'name': 'Animal_Test',
        'outcome_subtype': 'SCRP',
        'outcome_type': 'Transfer',
        'rec_num': 1,
        'sex_upon_outcome': 'Neutered Male'
    }

    # Define animal parameters to search from the 'animals' collection.
    find_animal = {
        'breed': 'Domestic Shorthair Mix',
        'outcome_type': 'Transfer'
    }

    # Define parameters to update animal document/s in the 'animals' collection.
    update_animal = {
        'filter_fields': {  # Define the filter fields for the update.
            'breed': 'Domestic Shorthair Mix',
            'outcome_type': 'Transfer'
        },
        'update_fields': {  # Define the update fields for the update.
            'breed': 'Test Domestic Shorthair Mix',
            'outcome_type': 'Test Transfer',
            'name': 'Test Animal Name'
        }
    }

    # Define parameters to delete animal document/s in the 'animals' collection.
    delete_animal = {
        'filter_fields': {  # Define the filter fields for the delete.
            'name': 'Test Animal Name',
            'outcome_type': 'Transfer'
        }
    }

    # Create an 'admin' instance of the CRUD AnimalShelter class for maintenance purposes.
    animals_admin = AnimalShelter(user, password)

    # Create a 'user' instance of the CRUD AnimalShelter class.
    animals_user = AnimalShelter()

    # The create function from the AnimalShelter class
    # inserts a predefined animal by passing animal BSON string parameter.
    # Returns True/False upon completion.
    animal_inserted = animals_user.create(new_animal)
    print(animal_inserted, end="\n\n")

    # The read function from the AnimalShelter class,
    # searches for predefined animal by passing search parameters.
    # Store returned Dict results in read_animal.
    returned_animals = animals_user.read(find_animal)
    print(returned_animals, end="\n\n")

    # The update function from the AnimalShelter class -
    # filters the animals by the predefined filter_fields -
    # and updates the found documents with the predefined update_fields values.
    # You can update multiple animals by setting the optional argument update_many=True -
    # Or update only the first found animal by omitting the optional argument.
    # Returns the number of updated animals.
    updated_animals = animals_user.update(update_animal, update_many=False)
    print(updated_animals, end="\n\n")

    # The delete function from the AnimalShelter class -
    # filters the animals by the predefined filter_fields -
    # and deletes the found documents with the predefined update_fields values.
    # You can delete multiple documents by setting the optional argument delete_many=True -
    # Or delete only the first found document by omitting the optional argument.
    # Returns the number of deleted animals.
    deleted_animals = animals_user.delete(delete_animal, delete_many=True)
    print(deleted_animals, end="\n\n")

if __name__ == '__main__':
    main()
