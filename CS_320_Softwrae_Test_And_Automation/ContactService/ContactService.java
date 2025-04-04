package ContactService;

import java.util.ArrayList;
import java.util.List;

public class ContactService {
	
    // Store contacts in an ArrayList locally
    private List<Contact> contactList;
	
    // Constructor
    public ContactService() {
        this.contactList = new ArrayList<>();
    }
    
    
    // Add a new contact to contactList if its ID is unique
    public void addContact(Contact contact) {
        if (contact == null) {									// Catch invalid null case contacts
            throw new IllegalArgumentException("Cannot add a null contact.");
        }
        // Validate contact ID is unique
        if (searchContact(contact.getID()) != -1) {
            throw new IllegalArgumentException("Contact ID must be unique.");
        }
        contactList.add(contact);
    }
    

    // Delete contact from contactList if such exists
    public void deleteContact(String ID) {
    	int contactIndex = searchContact(ID);					// Search for contact in contactList
    	if (contactIndex != -1) {
    		contactList.remove(contactIndex);					// Remove contact if ID was found
    	} else {
    		throw new IllegalArgumentException("Connot delete contact.");
    	}
    }
    
    
    // Update contact from contactList if such exists and only if ALL input fields are valid
    public void updateContact(String ID, String firstName, String lastName, String phone, String address) {
    	int contactIndex = searchContact(ID);					// Search for contact in contactList
    	if (contactIndex != -1) {								// If found
    		Contact contact = contactList.get(contactIndex);	// Temporarily store contact address 
    		
    		// Validate and update contact details only if ALL fields are valid
    		if (inputValidator(firstName, ">", 10) && inputValidator(lastName, ">", 10)
    				&& inputValidator(phone, "=", 10) && inputValidator(address, ">", 30)) {
        		// Update fields
        		contact.setFirstName(firstName);
        		contact.setLastName(lastName);
        		contact.setPhone(phone);
        		contact.setAddress(address);
    		}    		
    	} else {
    		throw new IllegalArgumentException("Connot update contact.");
    	}
    }
    
    
	// Validate user input is valid, not null and in the right length.
	private boolean inputValidator (String input, String sign, int size) {
		// String cannot be longer than 10 or 30 chars and not empty/null.
		if (sign == ">") {
			if (input == null || input.length()>size || input=="") {
				// Input is invalid Exception
				throw new IllegalArgumentException("Invalid ID/firstName/lastName/address");
			}
			return true;
		}
		// String must be exactly 10 chars and not empty/null.
		if (sign == "=") {
			if (input == null || input.length()!=size || !input.matches("\\d+")) {
				// Input is invalid Exception
				throw new IllegalArgumentException("Invalid phone");
			}
			return true;
		}
		return true;
	}
    
    
    // Search a contact in contactList by its ID
    private int searchContact(String contactID) {
        for (int i = 0; i < contactList.size(); i++) {
            if (contactList.get(i).getID().equals(contactID)) {
                return i; // Contact found, return its index
            }
        }
        return -1; // Contact not found, return -1
    }
    
    
    // Display contacts from contactList
    public void printContacts() {
    	if (contactList != null) {
        	for (Contact contact : contactList) {
        		contact.printContact();
        	}
        	System.out.println("");
    	}
    }
    
    // Get the list address to be used by ContactServiceTest
    public List<Contact> ContactServiceList() {
        return contactList;
    }
}
