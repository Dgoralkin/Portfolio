package AppointmentService;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.Date;

public class AppointmentServiceTest {
	
	private AppointmentService appointmentService;

	@BeforeEach
	void setUpBeforeClass() throws Exception {
		appointmentService = new AppointmentService();
	}

	@AfterEach
	void tearDownAfterClass() throws Exception {
		appointmentService = null;
	}

	
	//  Success cases	--------------------------------------------------------
	@Test
	@DisplayName("Build an Appointment Service List")
	// Test that the appointment storage list has been created
	void testAppointmentServiceList() {
		List<Appointment> appointmentList = appointmentService.AppointmentList();
		// Validate that list was created and is empty
		assertEquals(0, appointmentList.size());
		// Validate that list is not null
		assertNotNull(appointmentList);
	}
	
	@Test
	@DisplayName("Add Appointments to the Appointment List")
	// Add and Check if unique appointments were added successfully to the list.
	void testAddAppointments() {
				
		// Current and future appointments test case
		for(int i = 0; i < 5; i++) {
			Date date = new Date(System.currentTimeMillis() + 1000*60*60*24*i);	// Days iterations
			// Create and store appointment instances
			Appointment appointment = new Appointment("ApptId_" + i, "Appointment_" + i + "Description", date);
			appointmentService.AddAppointment(appointment);
			System.out.println("date " + i + " appointment time :" + date.toString());
			
			// Validate that list was created and unique appointment have been added
			assertEquals(i+1, appointmentService.AppointmentList().size());
		}
		
		/*
		// Single appointment test case
		Date dateNow = new Date();
		// Create and store first appointment instance
		Appointment appointment1 = new Appointment("ApptId_1", "Appointment_1_Description", dateNow);
		appointmentService.AddAppointment(appointment1);
		System.out.println("date1 appointment time :" + dateNow.toString());
		// Validate that list was created and unique appointment have been added
		assertEquals(1, appointmentService.AppointmentList().size());
		*/
	}
	
    @Test
    @DisplayName("Search for non existing appointment by ID")
    void SearchNoAppointmentById() {
		
        int appointmentNotFound = appointmentService.searchAppointment("Appt Id_@");
        assertNotNull(appointmentNotFound);
        assertEquals(-1, appointmentNotFound);
    }
    
    @Test
    @DisplayName("Search for existing appointment by ID")
    void SearchAppointmentById() {
    	
		// Create and store appointment instances
		Appointment appointment = new Appointment("Appt Id_@", "Appointment_Description", new Date());
		appointmentService.AddAppointment(appointment);
		
        int appointmentFound = appointmentService.searchAppointment("Appt Id_@");
        assertNotNull(appointmentFound);
        assertEquals(0, appointmentFound);
    }
    
    @Test
    @DisplayName("Delete existing appointment by ID")
    void deleteAppointmentByExistingId() {
    	
		// Create and store 1st appointment instances
		Appointment appointment1 = new Appointment("ApptId_1", "Appointment_Description", new Date());
		appointmentService.AddAppointment(appointment1);
		// Create and store 2nd appointment instances
		Appointment appointment2 = new Appointment("ApptId_2", "Appointment_Description2", 
				new Date(System.currentTimeMillis() + 1000*60*60*24));
		appointmentService.AddAppointment(appointment2);
		
		assertEquals(2, appointmentService.AppointmentList().size());
		// Delete ApptId_1 appointment
        appointmentService.deleteAppointment("ApptId_1");
        assertEquals(1, appointmentService.AppointmentList().size());
        assertEquals(-1, appointmentService.searchAppointment("ApptId_1")); // Validate ApptId_1 deleted successfully
    }
    
   
	//  Exception cases	--------------------------------------------------------
	@Test
	@DisplayName("Delete non-existent appointment")
	void testDeleteNonExistentAppointment() {
		assertThrows(IllegalArgumentException.class, () -> appointmentService.deleteAppointment("NoApptmnt"));
	}
	
	@Test
	@DisplayName("Delete null appointment")
	void testDeleteNullAppointment() {
		assertThrows(IllegalArgumentException.class, () -> appointmentService.deleteAppointment(null));
	}
	
	@Test
	@DisplayName("Add null appointment to the list")
	void testAddNullAppointment() {
		assertThrows(IllegalArgumentException.class, () -> appointmentService.AddAppointment(null));
	}
	
	@Test
	@DisplayName("Add past date appointment to the list")
	void testAddPastAppointment() {
		Date date = new Date(System.currentTimeMillis() - 2000);	// Two seconds before now date
		
		// Create and store appointment instance assuming it was created successfully.
		assertThrows(IllegalArgumentException.class, () -> 
		appointmentService.AddAppointment(new Appointment("Appt1", "AppointmentDescription", date)));
	}
	
    @Test
    @DisplayName("Search an appointment with null ID")
    void searchAppointment_nullId() {
        assertThrows(IllegalArgumentException.class, () -> appointmentService.searchAppointment(null));
    }
    
    @Test
    @DisplayName("Delete Non existing appointment by ID")
    void deleteNonExistingAppointmentId() {
    	
		// Create and store 1st appointment instances
		Appointment appointment1 = new Appointment("ApptId_1", "Appointment_Description", new Date());
		appointmentService.AddAppointment(appointment1);
		
		// Delete not existing appointment
        assertThrows(IllegalArgumentException.class, () -> appointmentService.deleteAppointment("ApptNoId"));
        assertEquals(1, appointmentService.AppointmentList().size());
    }
    
    @Test
    @DisplayName("Delete appointment by null ID")
    void deleteNullAppointmentId() {
    	
		// Create and store 1st appointment instances
		Appointment appointment1 = new Appointment("ApptId_1", "Appointment_Description", new Date());
		appointmentService.AddAppointment(appointment1);
		
		// Delete null appointment
        assertThrows(IllegalArgumentException.class, () -> appointmentService.deleteAppointment(null));
        assertEquals(1, appointmentService.AppointmentList().size());
    }
}