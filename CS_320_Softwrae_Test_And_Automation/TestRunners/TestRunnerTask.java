package TestRunners;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

import TaskService.TaskServiceTest;
import TaskService.TaskTest;

@SuppressWarnings("deprecation")
@RunWith(JUnitPlatform.class)
@Suite
@IncludeEngines("junit-jupiter")
@SelectClasses({ TaskTest.class, TaskServiceTest.class })
public class TestRunnerTask {
    @Test
    // Execute all tests in TaskTest and TaskServiceTest
    public void runAllTests() {
    }
}