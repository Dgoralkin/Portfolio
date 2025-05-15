package ContactService;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

public class ContactServiceTest {

	private ContactService contactService;
	
	@BeforeEach
	void setUp() throws Exception {
		contactService = new ContactService();
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	
	// Validate the storage List.
	@Test
	@Tag("List")
	@DisplayName("Validate the storage List")
	void accessListTest() {
        // Get access to the contactList storage list
        List<Contact> contactList = contactService.ContactServiceList();
        
        // Validate that list was created and accessible
        assertEquals(0, contactList.size());
	}
	
	
	// Add and Check if contact was added successfully.
	@Test
	@Tag("addContact")
	@DisplayName("Store new contact in the list")
	void addContactTest() {
		// Create and store contact instance
        Contact contact = new Contact("ID1", "Dany", "Gorelkin", "7785491390", "17 Marymount");
        contactService.addContact(contact);
        
        // Get access to the contactList storage list
        List<Contact> contactList = contactService.ContactServiceList();
        
        // Validate that list was populated with the contact
        assertEquals("ID1", contactList.get(0).getID());
        assertEquals("Dany", contactList.get(0).getFirstName());
        assertEquals("Gorelkin", contactList.get(0).getLastName());
        assertEquals("7785491390", contactList.get(0).getPhone());
        assertEquals("17 Marymount", contactList.get(0).getAddress());
	}


	// Check contact ID input is unique.
	@Test
	@Tag("uniqueID")
	@DisplayName("Validate contact ID is unique")
	void uniqueIDinputTest() {
		
		// Get access to the contactList storage list
        @SuppressWarnings("unused")
		List<Contact> contactList = contactService.ContactServiceList();
        
		// Create and store contact instance
        Contact contact1 = new Contact("ID1", "Dany", "Gorelkin", "7785491390", "17 Marymount");
        contactService.addContact(contact1);
        
        // Create and store contact instance with the same contact ID
        Contact contact2 = new Contact("ID1", "Dany2", "Gorelkin2", "7785491392", "17 Marymount2");
        assertThrows(IllegalArgumentException.class, () -> contactService.addContact(contact2));
	}
	
	
	// Delete contacts per contact ID.
	@Test
	@Tag("Delete")
	@DisplayName("Delete contact")
	void DeleteContactTest() {
		
		// Get access to the contactList storage list
        @SuppressWarnings("unused")
		List<Contact> contactList = contactService.ContactServiceList();
        
		// Create and store contact instance in an empty List.
        Contact contact = new Contact("ID1", "Dany", "Gorelkin", "7785491390", "17 Marymount");
        contactService.addContact(contact);
        // Delete contact from a List with only one contact
        contactService.deleteContact("ID1");
        assertEquals(0, contactService.ContactServiceList().size());
        
        // Try to delete null
        assertThrows(IllegalArgumentException.class, () -> contactService.deleteContact(null));
        // Try to delete not existing contact
        assertThrows(IllegalArgumentException.class, () -> contactService.deleteContact("noID"));
	}
	
	
	// Successfully Update contact details.
	@Test
	@Tag("UpdateSuccess")
	@DisplayName("Update contact success")
	void UpdateContactSuccessTest() {
		
		// Get access to the contactList storage list
        @SuppressWarnings("unused")
		List<Contact> contactList = contactService.ContactServiceList();
        
		// Create and store contact instance in an empty List.
        Contact contact = new Contact("ID1", "Dany", "Gorelkin", "7785491390", "17 Marymount");
        contactService.addContact(contact);
        
        // Update the contact per contact ID
        contactService.updateContact("ID1", "Dany2", "Gorelkin2", "7785491391", "17 Marymount2");
        
        // Read the updated contact from the list
        Contact updatedContact = contactService.ContactServiceList().get(0);
        
        assertEquals("Dany2", updatedContact.getFirstName());
        assertEquals("Gorelkin2", updatedContact.getLastName());
        assertEquals("7785491391", updatedContact.getPhone());
        assertEquals("17 Marymount2", updatedContact.getAddress());
	}
	
	// Fail Update contact details.
	@Test
	@Tag("UpdateFail")
	@DisplayName("Update contact success")
	void UpdateContactFailTest() {
		
		// Get access to the contactList storage list
        @SuppressWarnings("unused")
		List<Contact> contactList = contactService.ContactServiceList();
        
		// Create and store contact instance in an empty List.
        Contact contact = new Contact("ID1", "Dany", "Gorelkin", "7785491390", "17 Marymount");
        contactService.addContact(contact);
        
        // Try to Update the contact with a different contact ID
        assertThrows(IllegalArgumentException.class, () -> 
        contactService.updateContact("ID2", "Dany", "Gorelkin", "7785491390", "17 Marymount"));
        
        // Try to Update the contact with a invalid firstName
        assertThrows(IllegalArgumentException.class, () -> 
        contactService.updateContact("ID1", "", "Gorelkin", "7785491390", "17 Marymount"));
        
        // Try to Update the contact with a invalid lastName
        assertThrows(IllegalArgumentException.class, () -> 
        contactService.updateContact("ID1", "Dany", "", "7785491390", "17 Marymount"));
        
        // Try to Update the contact with a invalid phone
        assertThrows(IllegalArgumentException.class, () -> 
        contactService.updateContact("ID1", "Dany", "Gorelkin", "", "17 Marymount"));
        
        // Try to Update the contact with a invalid address
        assertThrows(IllegalArgumentException.class, () -> 
        contactService.updateContact("ID1", "Dany", "Gorelkin", "7785491390", ""));
	}
	
	@Test
	@Tag("Print")
	@DisplayName("Test Print Contacts")
	void testPrintContacts() {
	    Contact contact1 = new Contact("ID1", "Dany", "Gorelkin", "7785491390", "17 Marymount");
	    contactService.addContact(contact1);
	    assertDoesNotThrow(() -> contactService.printContacts());
	}
	
	@Test
	@Tag("Search")
	@DisplayName("Searching for Contacts")
	void testSearchContact() {
	    Contact contact1 = new Contact("ID1", "Dany", "Gorelkin", "7785491390", "17 Marymount");
	    contactService.addContact(contact1);
	    
	    // Search existing contact
	    assertEquals(0, contactService.ContactServiceList().indexOf(contact1));
	    
	    // Search non-existing contact
	    assertEquals(-1, contactService.ContactServiceList().indexOf(new Contact("ID123", "No", "Name", "1234567890", "123 Street")));
	}

	@Test
	@Tag("Validation")
	@DisplayName("Test inputValidator with unexpected sign")
	void testInputValidatorUnexpectedSign() {
	    assertThrows(IllegalArgumentException.class, () -> 
	        contactService.updateContact("ID1", "ValidName", "ValidLast", "1234567890", "Valid Address")
	    );
	}

	@Test
	@Tag("Delete")
	@DisplayName("Delete Contact from Empty List")
	void testDeleteFromEmptyList() {
	    assertThrows(IllegalArgumentException.class, () -> contactService.deleteContact("ID1"));
	}

	@Test
	@Tag("UpdateFail")
	@DisplayName("Update Contact with Different ID")
	void testUpdateDifferentID() {
	    Contact contact1 = new Contact("ID1", "Dany", "Gorelkin", "7785491390", "17 Marymount");
	    contactService.addContact(contact1);
	    
	    assertThrows(IllegalArgumentException.class, () -> 
	        contactService.updateContact("ID2", "NewName", "NewLast", "1234567890", "New Address"));
	}

	@Test
	@Tag("Validation")
	@DisplayName("Test Input Validator Through Update Contact")
	void testInputValidatorIndirectly() {
	    Contact contact1 = new Contact("ID1", "Dany", "Gorelkin", "7785491390", "17 Marymount");
	    contactService.addContact(contact1);

	    // Test invalid first name
	    assertThrows(IllegalArgumentException.class, () -> 
	        contactService.updateContact("ID1", "", "Gorelkin", "7785491390", "17 Marymount"));

	    // Test invalid last name
	    assertThrows(IllegalArgumentException.class, () -> 
	        contactService.updateContact("ID1", "Dany", "", "7785491390", "17 Marymount"));

	    // Test invalid phone number (non-numeric)
	    assertThrows(IllegalArgumentException.class, () -> 
	        contactService.updateContact("ID1", "Dany", "Gorelkin", "invalidPhone", "17 Marymount"));

	    // Test invalid address (too long)
	    assertThrows(IllegalArgumentException.class, () -> 
	        contactService.updateContact("ID1", "Dany", "Gorelkin", "7785491390", "A very very very long address exceeding 30 chars"));
	}	
}