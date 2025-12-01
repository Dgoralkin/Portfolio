package TestRunners;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

import ContactService.ContactServiceTest;
import ContactService.ContactTest;

@SuppressWarnings("deprecation")
@RunWith(JUnitPlatform.class)
@Suite
@IncludeEngines("junit-jupiter")
@SelectClasses({ ContactTest.class, ContactServiceTest.class })
public class TestRunnerContact {
    @Test
    // Execute all tests in TaskTest and TaskServiceTest
    public void runAllTests() {
    }
}