package AppointmentService;

import java.util.ArrayList;
import java.util.List;

public class AppointmentService {

    // Store contacts in an ArrayList locally
    private List<Appointment> AppointmentList;
   
    // Constructor
    public AppointmentService() {
    	this.AppointmentList = new ArrayList<>();
    }
    
    // Return the list address to be used by AppointmentServiceTest
    public List<Appointment> AppointmentList() {
        return AppointmentList;
    }
    
    // Search an Appointment in AppointmentList by Appointment ID
    public int searchAppointment(String AppointmentID) {
        if (AppointmentID == null) {									// Catch invalid null case task
            throw new IllegalArgumentException("Cannot search for a null Appointment ID.");
        }
        for (int i = 0; i < AppointmentList.size(); i++) {
            if (AppointmentList.get(i).getAppointmentId().equals(AppointmentID)) {
                return i; // Appointment found, return its index
            }
        }
        return -1; // Appointment not found, return -1
    }
   
    // Add new Appointment to the storage list
    public void AddAppointment(Appointment Appointment) {
        if (Appointment == null) {									// Catch invalid null case Appointment
            throw new IllegalArgumentException("Cannot add a null Appointment.");
        }
        if (searchAppointment(Appointment.getAppointmentId()) != -1) {			// Validate Appointment ID is unique
            throw new IllegalArgumentException("Appointment ID must be unique.");
        }
    	AppointmentList.add(Appointment);
    }
    
    // Delete Appointment from Appointment List if such exists
    public void deleteAppointment(String ID) {
    	if (ID == null) {
    		throw new IllegalArgumentException("Appointment ID can not be null.");
    	}
    	int contactIndex = searchAppointment(ID);					// Search for Appointment in Appointment List
    	if (contactIndex != -1) {
    		AppointmentList.remove(contactIndex);					// Remove Appointment if AppointmentID was found
    	} else {
    		throw new IllegalArgumentException("Connot delete Appointment.");
    	}
    }
}
