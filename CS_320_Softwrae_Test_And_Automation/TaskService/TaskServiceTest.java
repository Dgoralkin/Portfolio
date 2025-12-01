package TaskService;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TaskServiceTest {
	
	private TaskService taskService;

	@BeforeEach
	void setUpBeforeClass() throws Exception {
		taskService = new TaskService();
	}

	@AfterEach
	void tearDownAfterClass() throws Exception {
	}

	
	//  Success cases	--------------------------------------------------------
	@Test
	// Test that the task storage list has been created
	void testTaskService() {
		List<Task> taskList = taskService.ContactTaskList();
		
		// Validate that list was created and is empty
		assertEquals(0, taskList.size());
	}
	
	@Test
	// Add and Check if two unique tasks were added successfully to the list.
	void testAddTask() {
		// Create and store first task instance
		Task task1 = new Task("Task1", "TaskDescription", "TaskId1");
		taskService.AddTask(task1);
		
		// Create and store second task instance
		Task task2 = new Task("Task2", "TaskDescription", "TaskId2");
		taskService.AddTask(task2);
		
		// Validate that list was created and unique tasks have been added
		assertEquals(2, taskService.ContactTaskList().size());
	}
	
	@Test
	// Test the searchTask method
	void testSearchTask() {		
		// Validate that searchTask returns -1 if taskId has not been found.
		assertEquals(-1, taskService.searchTask("TaskId1"));
		
		Task task1 = new Task("Task1", "TaskDescription", "TaskId1");
		taskService.AddTask(task1);
		
		// Validate that searchTask returns the index of the found taskId.
		assertEquals(0, taskService.searchTask("TaskId1"));
	}
	
	// Delete contact per contact ID.
	@Test
	void testDeleteTask() {
		// Create and store task instance in an empty List.
        Task task1 = new Task("Task1", "TaskDescription", "TaskId1");
        taskService.AddTask(task1);
        
        // Delete task from the List with only one contact size
        taskService.deleteTask("TaskId1");
        assertEquals(0, taskService.ContactTaskList().size());
	}
	
	// Update task per contact ID.
	@Test
	void testUpdateTask() {
		// Create and store task instance in an empty List.
        Task task1 = new Task("Task1", "TaskDescription", "TaskId1");
        taskService.AddTask(task1);
        
        // Update task per task ID
        taskService.updateContact("Task1Update", "TaskDescriptionUpdate", "TaskId1");
        assertEquals("Task1Update", taskService.ContactTaskList().get(0).getName());
        assertEquals("TaskDescriptionUpdate", taskService.ContactTaskList().get(0).getDescription());
        
		// Create and store task instance in the List.
        Task task2 = new Task("Task2", "TaskDescription", "TaskId2");
        taskService.AddTask(task2);
        
        // Do not update task if an empty string is passed
        taskService.updateContact("", "", "TaskId2");
        assertEquals("Task2", taskService.ContactTaskList().get(1).getName());
        assertEquals("TaskDescription", taskService.ContactTaskList().get(1).getDescription());
	}

	
	//  Fail cases		--------------------------------------------------------
	@Test
	// Test fail adding null task
	void testAddNullTask() {
		Task task = null;
		// Try adding null task to the list
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			taskService.AddTask(task);
		});
	}
	
	@Test
	// Test fail adding non unique task
	void testAddNonUniqueTask() {
		// Create and store first task instance
		Task task1 = new Task("Task1", "TaskDescription", "TaskId1");
		taskService.AddTask(task1);
		
		// Create and store second task instance with the same taskId
		Task task2 = new Task("Task2", "TaskDescription", "TaskId1");
		
		// Try adding not unique task to the list
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			taskService.AddTask(task2);
		});
	}
	
	@Test
	// Test fail adding null task
	void testSearchTaskNullInput() {
		// Try adding not unique task to the list
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			taskService.searchTask(null);
		});
	}
	
	@Test
	// Test fail deleting null task, or not existent task from an empty list
	void testDeleteInvalidTask() {
		// Try delete null task from the list
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			taskService.deleteTask(null);
		});
		// Try delete non existing taskId from the list
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			taskService.deleteTask("NoTaskId");
		});
	}
	
	@Test
	// Test fail update task, due to null argument input
	void testUpdateNullTask() {
		// Create and store task instance in an empty List.
        Task task1 = new Task("Task1", "TaskDescription", "TaskId1");
        taskService.AddTask(task1);
        
		// Try update task with null inputs
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			taskService.updateContact(null, "", "");
		});
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			taskService.updateContact("", null, "");
		});
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			taskService.updateContact("", "", null);
		});
	}
	
	@Test
	// Test fail update task, due to invalid testId argument input
	void testUpdateInvalidTaskId() {
		// Create and store task instance in an empty List.
        Task task1 = new Task("Task1", "TaskDescription", "TaskId1");
        taskService.AddTask(task1);
        
		// Try update task with null inputs
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			taskService.updateContact("Task1", "TaskDescription", "NoTaskId");
		});
	}
}