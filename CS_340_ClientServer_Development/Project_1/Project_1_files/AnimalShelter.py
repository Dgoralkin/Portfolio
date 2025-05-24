# Standard library imports
from pprint import pprint

# Third-party imports
from pymongo import MongoClient
from typing import List, Dict


class AnimalShelter:
    """ CRUD operations for Animal collection in MongoDB """

    def __init__(self, user: str = "aacuser", password: str = "SNHU1234") -> None:
        """ Initializing the MongoClient. This helps to
        access the MongoDB databases and collections.
        This is hard-wired to use the "AAC" database, the -
        "animals" collection, and the "AAC" admin or user roles.
        Definitions of the connection string variables are
        unique to the individual Localhost environment.
        You must edit the connection variables below to reflect
        your own instance of MongoDB!
        """

        # Connection Variables
        self.user = user
        self.psswrd = password
        self.host = "localhost"
        self.port = 27017
        self.database = 'AAC'
        self.collection = 'animals'

        # Try to authenticate a database user with predefined 'readWrite' permissions.
        try:
            self.client = MongoClient(f"mongodb://{self.user}:{self.psswrd}@{self.host}:{self.port}/?authSource=admin")
            self.client.admin.command('ping')
            print(f"Connected to MongoDB.{self.database}.{self.collection}")
        except Exception as e:
            print("Connection failed:", e)
            exit(1)

        # Initialize Connection to the database and the collection for the authenticated user.
        self.db = self.client[self.database]
        self.animals = self.db[self.collection]

        # Output connection details e.g., user, role, database, collection
        # Optional: print("Connection details: \n", self.animals)
        self.connected_user_info = self.client["admin"].command("usersInfo", self.user)
        print("Connected as:", self.connected_user_info['users'][0]['user'],
              "With:", self.connected_user_info['users'][0]['roles'], end="\n\n")

    def create(self, data) -> bool:
        """Insert a new animal into the collection.
        Returns True if successful, False otherwise.
        """

        # Insert a new animal to the 'animal' collection.
        try:
            created_animal = self.animals.insert_one(data)
            print("Animal with animal_id=", data['animal_id'],
                  "by the name=", data['name'], end=", created successfully!\n")
            return created_animal.acknowledged

        # Raise an Exception if data is None or can't be saved.
        except Exception as e:
            print("Can't Save animal, something went wrong. Error:", e, end="\n\n")
            return False

    def read(self, data) -> List[Dict]:
        """ Search for animals matching the given `breed` and `outcome_type` in the collection
        and output information about matching animals to the console.
        Returns a Dict of the found results.
        """

        try:
            # Find all animals with breed='breed' & outcome_type='outcome_type'
            found_animals = list(self.animals.find({'breed': data['breed'], 'outcome_type': data['outcome_type']}))

            # Output returned results
            if found_animals:  # If any animal found in AAC.animals.
                print("Found animals with breed=", data['breed'],
                      "& outcome_type=", data['outcome_type'], end=", such as:\n")
                pprint(found_animals[0])  # Print first found animal instance.
                print("And", len(found_animals) - 1, "more results.", end="\n\n")

            else:  # If no animals were found in AAC.animals.
                print("No animals found with breed=", data['breed'],
                      "& outcome_type=", data['outcome_type'])
            return found_animals

        # Raise an Exception if data is None
        except Exception as e:
            print("Nothing to search, because data parameter is empty, Error:", e)
            return [{}]

    def update(self, data, update_many: bool = False) -> int:
        """Filter the documents by the data.filter_fields dictionary
        and update the found documents with the data.update_fields fields.
        update_many determines whether to update all or only the first matching document.
        Returns the number of updated documents.
        """

        try:
            # Dynamically extracts whatever filter and update keys and values
            # are in the data dictionary argument.
            filter_fields = data.get('filter_fields', {})
            update_fields = data.get('update_fields', {})
            print("Update filter_fields:", filter_fields, "\t|\t update_fields:", update_fields, end="\n")

            # updateMany() path updates all matching documents.
            if update_many:
                updated = self.animals.update_many(filter_fields, {'$set': update_fields})

            # updateOne() path updates only the first matching document.
            else:
                updated = self.animals.update_one(filter_fields, {'$set': update_fields})

            # Display the number of updated documents.
            print("Matched count:", updated.matched_count, "\t|\t updated modified count:", updated.modified_count)
            return updated.modified_count

        # Raise an Exception if data is None
        except Exception as e:
            print("Nothing to update, because data parameter is empty, Error:", e)
            return 0

    def delete(self, data, delete_many: bool = False) -> int:
        """Filter the documents by the data.filter_fields dictionary
        and delete the found document/s from the collection.
        delete_many determines whether to update all or only the first matching document.
        Returns the number of deleted documents.
        """

        try:
            # Dynamically extracts whatever filter keys and values
            # are in the data dictionary argument.
            filter_fields = data.get('filter_fields', {})
            print("Delete filter_fields:", filter_fields, end="\n")

            # deleteMany() path updates all matching documents.
            if delete_many:
                deleted = self.animals.delete_many(filter_fields)

            # deleteOne() path updates only the first matching document.
            else:
                deleted = self.animals.delete_one(filter_fields)

            # Display the number of deleted documents.
            print("Matched count:", deleted.deleted_count, end=" documents were deleted.\n")
            return deleted.deleted_count

        # Raise an Exception if data is None
        except Exception as e:
            print("Nothing to delete, because data parameter is empty, Error:", e)
            return 0
