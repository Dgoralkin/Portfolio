package ContactService;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.DisplayName;

@Tag("Functional")
public class ContactTest {

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}
	
	// Valid constructor call
	@Test
	@Tag("Pass")
	@DisplayName("Create contact, all fields are valid")
	void testContactPass() {
		// Create contact instance
		@SuppressWarnings("unused")
		Contact contact1 = new Contact("ID1", "Dany", "Gorelkin", "7785491390", "17 Marymount, Winnipeg, MB");
	}
	
    // Testing getters are getting values correctly
    @Test
    @Tag("Getters")
    @DisplayName("Test Getters")
    void testContactGetField() {
    	Contact contact1 = new Contact("ID1", "Dany", "Gorelkin", "7785491390", "17 Marymount, Winnipeg, MB");
		assertTrue(contact1.getID().equals("ID1"));
		assertFalse(contact1.getID().equals("1"));
		assertTrue(contact1.getFirstName().equals("Dany"));
		assertFalse(contact1.getFirstName().equals("D"));
		assertTrue(contact1.getLastName().equals("Gorelkin"));
		assertFalse(contact1.getLastName().equals("G"));
		assertTrue(contact1.getPhone().equals("7785491390"));
		assertFalse(contact1.getPhone().equals("778549139"));
		assertTrue(contact1.getAddress().equals("17 Marymount, Winnipeg, MB"));
		assertFalse(contact1.getAddress().equals("17"));
    }
	
	// Testing if one or more of the input fields is an empty string
    @Test
    @Tag("Empty")
    @DisplayName("Constructor: One or more of fields is an empty string")
    void testContactMissing() {
    	Assertions.assertThrows(IllegalArgumentException.class, () -> {
			new Contact("", "Dany", "Gorelkin", "7785491390", "17 Marymount, Winnipeg, MB");
			new Contact("ID1", "", "Gorelkin", "7785491390", "17 Marymount, Winnipeg, MB");
			new Contact("ID1", "Dany", "", "7785491390", "17 Marymount, Winnipeg, MB");
			new Contact("ID1", "Dany", "Gorelkin", "", "17 Marymount, Winnipeg, MB");
			new Contact("ID1", "Dany", "Gorelkin", "7785491390", "");
		});
    }
    
    // Testing if one or more of the input fields is null
    @Test
    @Tag("Null")
    @DisplayName("Constructor: One or more of fields is null")
    void testContactNull() {
    	Assertions.assertThrows(IllegalArgumentException.class, () -> {
			new Contact(null, "Dany", "Gorelkin", "7785491390", "17 Marymount, Winnipeg, MB");
			new Contact("ID1", null, "Gorelkin", "7785491390", "17 Marymount, Winnipeg, MB");
			new Contact("ID1", "Dany", null, "7785491390", "17 Marymount, Winnipeg, MB");
			new Contact("ID1", "Dany", "Gorelkin", null, "17 Marymount, Winnipeg, MB");
			new Contact("ID1", "Dany", "Gorelkin", "7785491390", null);
		});
    }
    
    // Testing if input is longer than 10 chars
    @Test
    @Tag("Long")
    @DisplayName("Constructor: Long input > 10")
    void testContactMoreTen() {
    	Assertions.assertThrows(IllegalArgumentException.class, () -> {
			new Contact("1234567890X", "Dany", "Gorelkin", "7785491390", "17 Marymount, Winnipeg, MB");
			new Contact("ID1", "1234567890X", "Gorelkin", "7785491390", "17 Marymount, Winnipeg, MB");
			new Contact("ID1", "Dany", "1234567890X", "7785491390", "17 Marymount, Winnipeg, MB");
			new Contact("ID1", "Dany", "Gorelkin", "1234567890X", "17 Marymount, Winnipeg, MB");
		});
    }
    
    // Testing if input is longer than 30 chars
    @Test
    @Tag("Long")
    @DisplayName("Constructor: Long input > 30")
    void testContactMoreThirty() {
    	Assertions.assertThrows(IllegalArgumentException.class, () -> {
			new Contact("ID1", "Dany", "Gorelkin", "1234567890", "1234567890          1234567890          !");
		});
    }
    
    // Testing if phone is not numeric
    @Test
    @Tag("Long")
    @DisplayName("Phone Field Is Not Numeric")
    void testContactPhoneIsNumeric() {
    	Assertions.assertThrows(IllegalArgumentException.class, () -> {
			new Contact("ID1", "Dany", "Gorelkin", "wrongPhone", "17 Marymount, Winnipeg, MB");
		});
    }
    
    // Testing setters methods
    @Test
    @Tag("Setters")
    @DisplayName("Set Fields Individually")
    void testContactSetField() {
    	Contact contact1 = new Contact("ID1", "Dany", "Gorelkin", "7785491390", "17 Marymount, Winnipeg, MB");
    	
    	Assertions.assertThrows(IllegalArgumentException.class, () -> {
    		contact1.setID("");
		});
    	
    	Assertions.assertThrows(IllegalArgumentException.class, () -> {
    		contact1.setID(null);
		});
    	
    	Assertions.assertThrows(IllegalArgumentException.class, () -> {
    		contact1.setID("012345678910");
		});
    	
    	Assertions.assertThrows(IllegalArgumentException.class, () -> {
    		contact1.setFirstName("");
		});
    	
    	Assertions.assertThrows(IllegalArgumentException.class, () -> {
    		contact1.setFirstName(null);
		});
    	
    	Assertions.assertThrows(IllegalArgumentException.class, () -> {
    		contact1.setFirstName("012345678910");
		});
    	
    	Assertions.assertThrows(IllegalArgumentException.class, () -> {
    		contact1.setLastName("");
		});
    	
    	Assertions.assertThrows(IllegalArgumentException.class, () -> {
    		contact1.setLastName(null);
		});
    	
    	Assertions.assertThrows(IllegalArgumentException.class, () -> {
    		contact1.setLastName("012345678910");
		});
    	
    	Assertions.assertThrows(IllegalArgumentException.class, () -> {
    		contact1.setPhone("a");
		});
    	
    	Assertions.assertThrows(IllegalArgumentException.class, () -> {
    		contact1.setPhone(null);
		});
    	
    	Assertions.assertThrows(IllegalArgumentException.class, () -> {
    		contact1.setPhone("012345678910");
		});
    	
    	Assertions.assertThrows(IllegalArgumentException.class, () -> {
    		contact1.setAddress("");
		});
    	
    	Assertions.assertThrows(IllegalArgumentException.class, () -> {
    		contact1.setAddress(null);
		});
    	
    	Assertions.assertThrows(IllegalArgumentException.class, () -> {
    		contact1.setAddress("012345678910012345678910012345678910012345678910");
		});
    }
    
    @Test
    @Tag("Print")
    @DisplayName("Test Print Contact")
    void testPrintContact() {
        Contact contact1 = new Contact("ID1", "Dany", "Gorelkin", "7785491390", "17 Marymount, Winnipeg, MB");
        contact1.printContact();
    }
    
    @Test
    @Tag("Setters")
    @DisplayName("Test Setting Valid Values")
    void testSetValidFields() {
        Contact contact1 = new Contact("ID1", "Dany", "Gorelkin", "7785491390", "17 Marymount, Winnipeg, MB");

        contact1.setFirstName("Dany2");
        assertEquals("Dany2", contact1.getFirstName());

        contact1.setLastName("Gorelkin2");
        assertEquals("Gorelkin2", contact1.getLastName());

        contact1.setPhone("1234567890");
        assertEquals("1234567890", contact1.getPhone());

        contact1.setAddress("123 St, Toronto, ON");
        assertEquals("123 St, Toronto, ON", contact1.getAddress());
    }
    
    @Test
    @Tag("Validator")
    @DisplayName("Test inputValidator W/unexpected sign")
    void testInputValidatorUnexpectedSign() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new Contact("ID1", "Dany", "Gorelkin", "abcdefghij", "17 Marymount, Winnipeg, MB");
        });
    }


}
