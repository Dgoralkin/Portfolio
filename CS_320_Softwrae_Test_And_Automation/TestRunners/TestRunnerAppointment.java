package TestRunners;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

import AppointmentService.AppointmentTest;
import AppointmentService.AppointmentServiceTest;

@SuppressWarnings("deprecation")
@RunWith(JUnitPlatform.class)
@Suite
@IncludeEngines("junit-jupiter")
@SelectClasses({ AppointmentTest.class, AppointmentServiceTest.class })
public class TestRunnerAppointment {
    @Test
    // Execute all tests in AppointmentTest and AppointmentServiceTest
    public void runAllTests() {
    }
}