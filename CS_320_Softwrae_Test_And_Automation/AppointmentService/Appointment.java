package AppointmentService;

import java.util.Date;

public class Appointment {

	private final String appointmentId;
	private String appointmentDscrptn;
	private Date appointmentDate;
	private final int appointmentIdLength = 10;
	private final int descriptionLenghth = 50;
	
	// Constructor
	public Appointment(String appointmentId, String description, Date appointmentDate) {
	
		// Validate appointment ID input is valid
		if(appointmentId == null || appointmentId.length() > appointmentIdLength || appointmentId.length() < 1) {
			// Catch requirements -> null, and length.
			throw new IllegalArgumentException("Invalid appointment Id input");
		}
		// Validate appointment description input is valid
		if(description == null || description.length() > descriptionLenghth || description.length() < 1) {
			// Catch requirements -> null, and length.
			throw new IllegalArgumentException("Invalid appointment description input");
		}
		// Validate appointment date input is valid. 
		// Allow system 1 seconds response time error while comparing past date.
		if(appointmentDate == null || appointmentDate.before(new Date(System.currentTimeMillis() - 1000))) {
			// Catch requirements -> null, and past date input.
			throw new IllegalArgumentException("Invalid appointment date null input");
		}
		
		// Set variables
		this.appointmentId = appointmentId;
		this.appointmentDscrptn = description;
		this.appointmentDate = appointmentDate;
	}
	
	// Return name method
	public void setAppointmentId(String appointmentId) {
		// Catch requirements of null, and length.
		throw new IllegalArgumentException("Can not update appointmentId");
	}
	
	// Return appointment Date method
	public String getAppointmentId() {
		return appointmentId;
	}
	
	// Update appointment description method
	public void setAppointmentDscrptn(String appointmentDate) {
		// Validate appointment description input is valid
		if(appointmentDate == null || appointmentDate.length() > descriptionLenghth || appointmentDate.length() < 1) {
			// Catch requirements -> null, and length.
			throw new IllegalArgumentException("Invalid appointment description input");
		}
		this.appointmentDscrptn = appointmentDate;
	}
	
	// Return appointment description method
	public String getAppointmentDscrptn() {
		return appointmentDscrptn;
	}
	
	// Update appointment date method
	public void setAppointmentDate(Date appointmentDate) {
		// Validate appointment date input is valid
		if(appointmentDate == null || appointmentDate.before(new Date())) {
			// Catch requirements -> null, and past date input.
			throw new IllegalArgumentException("Invalid appointment date input");
		}
		this.appointmentDate = appointmentDate;
	}
	
	// Return appointment Date method
	public Date getAppointmentDate() {
		return appointmentDate;
	}
}
