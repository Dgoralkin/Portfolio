package ContactService;

public class Contact {
	private String ID, firstName, lastName, Number, Address;
	
	// Contact constructor
	public Contact (String ID, String firstName, String lastName, String phone, String address) {
		inputValidator(ID, ">", 10);				// Validate 0 < ID < 11 chars
		this.ID = ID;
		inputValidator(firstName, ">", 10);			// Validate 0 < firstName < 11 chars
		this.firstName = firstName;
		inputValidator(lastName, ">", 10);			// Validate 0 < lastName < 11 chars
		this.lastName = lastName;
		inputValidator(phone, "=", 10);				// Validate phone = 10 chars and numeric
		this.Number = phone;
		inputValidator(address, ">", 30);			// Validate 0 < address < 31 chars
		this.Address = address;
	}
	
	// Validate user input is valid, not null and in the right length.
	private String inputValidator(String input, String sign, int size) {
		// String cannot be longer than 10 or 30 chars and not empty/null.
		if (sign == ">") {
			if (input == null || input.length()>size || input=="") {
				System.out.println("Invalid ID/firstName/lastName/address");
				// Input is invalid Exception
				throw new IllegalArgumentException("Invalid ID/firstName/lastName/address");
			}
			return input;
		}
		// String must be exactly 10 chars and not empty/null.
		if (sign == "=") {
			if (input == null || input.length()!=size || !input.matches("\\d+")) {
				System.out.println("Invalid ID/firstName/lastName/address");
				// Input is invalid Exception
				throw new IllegalArgumentException("Invalid phone");
			}
			return input;
		}
		return input;
	}
	
	// set ID method.
	public void setID (String ID) {
		System.out.println("Can not update contact ID!");
		throw new IllegalArgumentException("Can not update contact ID");
	}
	
	// get ID method.
	public String getID () {
		return ID;
	}
	
	// set firstName method.
	public void setFirstName(String firstName) {
		inputValidator(firstName, ">", 10);			// Validate 0 < firstName < 11 chars - *Optional
		this.firstName = firstName;
	}
	
	// get firstName method.
	public String getFirstName() {
		return firstName;
	}
	
	// set lastName method.
	public void setLastName(String lastName) {
		inputValidator(lastName, ">", 10);			// Validate 0 < lastName < 11 chars - *Optional
		this.lastName = lastName;
	}
	
	// get lastName method.
	public String getLastName() {
		return lastName;
	}
	
	// set phone method.
	public void setPhone(String phone) {
		inputValidator(phone, "=", 10);				// Validate phone = 10 chars and numeric - *Optional
		this.Number = phone;
	}
	
	// get phone method.
	public String getPhone() {
		return Number;
	}
	
	// set address method.
	public void setAddress(String address) {
		inputValidator(address, ">", 30);			// Validate 0 < address < 31 chars - *Optional
		this.Address = address;
	}
	
	// get phone method.
	public String getAddress() {
		return Address;
	}
	
	// Display contact fields
	public void printContact() {
		System.out.println(this.ID + ", " + this.firstName + ", "
				+ this.lastName + ", " + this.Number + ", " + this.Address);
	}
}
