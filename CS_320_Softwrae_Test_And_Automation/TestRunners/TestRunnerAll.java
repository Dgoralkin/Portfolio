package TestRunners;

import ContactService.ContactTest;
import ContactService.ContactServiceTest;
import TaskService.TaskTest;
import TaskService.TaskServiceTest;
import AppointmentService.AppointmentTest;
import AppointmentService.AppointmentServiceTest;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

@SuppressWarnings("deprecation")
@RunWith(JUnitPlatform.class)
@Suite
@IncludeEngines("junit-jupiter")
@SelectClasses({ 
	ContactTest.class, ContactServiceTest.class, 
	TaskTest.class, TaskServiceTest.class, 
	AppointmentTest.class, AppointmentServiceTest.class 
	})
public class TestRunnerAll {
    @Test
    // Execute all tests in ContactTest, ContactServiceTest, -
    // - TaskTest, TaskServiceTest, AppointmentTest, and AppointmentServiceTest.
    public void runAllTests() {
    }
}