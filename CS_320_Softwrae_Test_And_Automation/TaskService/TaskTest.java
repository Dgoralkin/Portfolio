package TaskService;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class TaskTest {

	final String tenChars = "TenChars10";
	final String twentyChars = "TwentyChars______20!";
	final String twentyOneChars = "TwentyOneChars____21!";
	final String fiftyChars = "fiftyChars_______fiftyOneChars_________________50!";
	final String fiftyOneChars = "fiftyOneChars_______fiftyOneChars_______________51!";
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	//  Success cases	--------------------------------------------------------
	@Test
	void testTask() {
		Task task = new Task(twentyChars, fiftyChars, tenChars);
		assertTrue(task.getName().equals(twentyChars));
		assertTrue(task.getDescription().equals(fiftyChars));
		assertTrue(task.getTaskId().equals(tenChars));
	}
	
	@Test
	void testUpdateName() {
		Task task = new Task(twentyChars, fiftyChars, tenChars);
		task.setName("New_name");
		assertTrue(task.getName().equals("New_name"));
	}
	
	@Test
	void testUpdateDescription() {
		Task task = new Task(twentyChars, fiftyChars, tenChars);
		task.setDescription("NewDescription");
		assertTrue(task.getDescription().equals("NewDescription"));
	}

	//  Fail cases		--------------------------------------------------------
	@Test
	// Test invalid ID input
	void testTaskIdInvalid() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			new Task(twentyChars, fiftyChars, "01234567890");
			new Task(twentyChars, fiftyChars, "");
			new Task(twentyChars, fiftyChars, null);
		});
	}
	
	@Test
	// Test invalid name input
	void testTaskNameInvalid() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			new Task(twentyOneChars, fiftyChars, tenChars);
			new Task("", fiftyChars, tenChars);
			new Task(null, fiftyChars, tenChars);
		});
	}
	
	@Test
	// Test invalid description input
	void testTaskDescriptionInvalid() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			new Task(twentyChars, fiftyOneChars, tenChars);
			new Task(twentyChars, "", tenChars);
			new Task(twentyChars, null, tenChars);
		});
	}
	
	@Test
	// Test invalid name update
	void testTaskNameUpdateFail() {
		Task task = new Task(twentyChars, fiftyChars, tenChars);
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			task.setName(twentyOneChars);
			task.setName(null);
			task.setName("");
		});
	}
	
	@Test
	// Test invalid description update
	void testTaskDescriptionUpdateFail() {
		Task task = new Task(twentyChars, fiftyChars, tenChars);
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			task.setDescription(fiftyOneChars);
			task.setDescription(null);
			task.setDescription("");
		});
	}
}
