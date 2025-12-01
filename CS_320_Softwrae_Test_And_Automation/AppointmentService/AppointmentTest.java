package AppointmentService;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.util.Date;

public class AppointmentTest {

	final String tenChars = "TenChars10";
	final String twentyOneChars = "TwentyOneChars____21!";
	final String fiftyChars = "fiftyChars_______fiftyOneChars_________________50!";
	final String fiftyOneChars = "fiftyOneChars_______fiftyOneChars_______________51!";
	Date nowDate;
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}
	
	//  Success cases	--------------------------------------------------------
	// Test successfully created appointment
	@Test
	void SetAppointmentPassTest() {
	    nowDate = new Date();
	    Appointment appointment = new Appointment(tenChars, fiftyChars, nowDate);

	    assertNotNull(appointment);
	    assertEquals(tenChars, appointment.getAppointmentId());
	    assertEquals(fiftyChars, appointment.getAppointmentDscrptn());
	    assertEquals(nowDate.getTime(), appointment.getAppointmentDate().getTime());
	    assertSame(nowDate, appointment.getAppointmentDate());
	}
	
	// Test successfully update appointment description
	@Test
	void UpdateAppointmentDescription() {
	    nowDate = new Date();
	    Appointment appointment = new Appointment(tenChars, fiftyChars, nowDate);
	    appointment.setAppointmentDscrptn("Updated description");
	    assertTrue(appointment.getAppointmentDscrptn().equals("Updated description"));
	    
	    appointment.setAppointmentDscrptn("Aa1. _!<nuul$%");
	    assertTrue(appointment.getAppointmentDscrptn().equals("Aa1. _!<nuul$%"));
	    
	    String randomLongString = "Aadfbnuul$%Aa1%bf<nuul$%Aa1.ng!l$%Aul$%";
	    appointment.setAppointmentDscrptn(randomLongString);
	    assertTrue(appointment.getAppointmentDscrptn().equals(randomLongString));
	}
	
	// Test successfully update appointment date
	@Test
	void UpdateAppointmentDate() {
	    nowDate = new Date();
	    Appointment appointment = new Appointment(tenChars, fiftyChars, nowDate);
	    appointment.setAppointmentDate(nowDate);
	    assertTrue(appointment.getAppointmentDate().equals(nowDate));
	}
	
	// Test successfully update appointment date
	@Test
	void UpdateAppointmentFutureDate() {
        Appointment appointment = new Appointment(tenChars, fiftyChars, new Date());
        Date futureDate = new Date(System.currentTimeMillis() + 10000);
        appointment.setAppointmentDate(futureDate);
        Assertions.assertEquals(futureDate, appointment.getAppointmentDate());
	}
	
	//  Fail cases		--------------------------------------------------------
	// Test create appointment null input
	@Test
	void FailSetAppointmentNullTest() {
		nowDate = new Date();
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			new Appointment(null, fiftyChars, nowDate);
			new Appointment(tenChars, null, nowDate);
			new Appointment(tenChars, fiftyChars, null);
		});
	}

	// Test create appointment too long input
	@Test
	void FailSetAppointmentLongTest() {
		nowDate = new Date();
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			new Appointment(twentyOneChars, fiftyChars, nowDate);
			new Appointment(tenChars, fiftyOneChars, nowDate);
		});
	}

	// Test create appointment too short / no input
	@Test
	void FailSetAppointmentShortTest() {
		nowDate = new Date();
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			new Appointment("", fiftyChars, nowDate);
			new Appointment(tenChars, "", nowDate);
		});
	}

	// Test create appointment with past date
	@Test
	void FailSetAppointmentPastDateTest() {
		// Set appointment date one second back in past time
		Date appointmentDate = new Date(System.currentTimeMillis() - 2500);
		System.out.println("System date :      "+ new Date());
		System.out.println("Aptmnt past date : "+ appointmentDate);

		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			new Appointment(tenChars, fiftyChars, appointmentDate);
		});
	
		// Set appointment date in the past
		@SuppressWarnings("deprecation")
		Date appointmentLongPastDate = new Date(2020-1900, 3-1, 20);
		System.out.println("Aptmnt past date : "+ appointmentLongPastDate);
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			new Appointment(tenChars, fiftyChars, appointmentLongPastDate);
		});
	}

	// Try to update Appointment ID
	@Test
	void FailSetAppointmentIdTest() {
	    nowDate = new Date();
	    Appointment appointment = new Appointment(tenChars, fiftyChars, nowDate);
		
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			appointment.setAppointmentId("");
		});
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			appointment.setAppointmentId("A");
		});
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			appointment.setAppointmentId(null);
		});
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			appointment.setAppointmentId(fiftyChars);
		});
	}

	// Try to update Appointment description
	@Test
	void FailSetAppointmentDescriptionTest() {
	    Appointment appointment = new Appointment(tenChars, fiftyChars, new Date());
		
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			appointment.setAppointmentDscrptn(null);
		});
		
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			appointment.setAppointmentDscrptn(fiftyOneChars);
		});
		
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			appointment.setAppointmentDscrptn("");
		});
	}

	// Try to update Appointment date
	@Test
	void FailSetAppointmentDateTest() {
	    nowDate = new Date();
	    Appointment appointment = new Appointment(tenChars, fiftyChars, nowDate);
		
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			appointment.setAppointmentDscrptn(null);
		});
	}
	
	@Test
	void FailSetAppointmentDatePastTest() {
	    Appointment appointment = new Appointment(tenChars, fiftyChars, new Date());

	    Date pastDate = new Date(System.currentTimeMillis() - 100000); // 100 seconds in the past
	    Assertions.assertThrows(IllegalArgumentException.class, () -> {
	        appointment.setAppointmentDate(pastDate);
	    });
	}
}