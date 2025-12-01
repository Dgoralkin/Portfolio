package TaskService;

import java.util.ArrayList;
import java.util.List;

public class TaskService {
    // Store contacts in an ArrayList locally
    private List<Task> taskList;
    
    // Constructor
    public TaskService() {
    	this.taskList = new ArrayList<>();
    }
    
    // Return the list address to be used by TaskServiceTest
    public List<Task> ContactTaskList() {
        return taskList;
    }
    
    // Search a task in taskList by task ID
    public int searchTask(String taskID) {
        if (taskID == null) {									// Catch invalid null case task
            throw new IllegalArgumentException("Cannot search for a null task.");
        }
        for (int i = 0; i < taskList.size(); i++) {
            if (taskList.get(i).getTaskId().equals(taskID)) {
                return i; // Contact found, return its index
            }
        }
        return -1; // Task not found, return -1
    }
    
    // Add new task to the storage list
    public void AddTask(Task task) {
        if (task == null) {									// Catch invalid null case task
            throw new IllegalArgumentException("Cannot add a null task.");
        }
        if (searchTask(task.getTaskId()) != -1) {			// Validate task ID is unique
            throw new IllegalArgumentException("Task ID must be unique.");
        }
    	taskList.add(task);
    }
    
    // Delete task from taskList if such exists
    public void deleteTask(String ID) {
    	if (ID == null) {
    		throw new IllegalArgumentException("Task ID can not be null.");
    	}
    	int contactIndex = searchTask(ID);					// Search for task in taskList
    	if (contactIndex != -1) {
    		taskList.remove(contactIndex);					// Remove task if taskID was found
    	} else {
    		throw new IllegalArgumentException("Connot delete task.");
    	}
    }
    
    // Update task from taskList if such exists.
    public void updateContact(String name, String description, String taskId) {
    	// Catch null arguments input and throw Exception
    	if (name == null || description == null || taskId == null) {
    		throw new IllegalArgumentException("Connot update task.");
    	}
    	// Search for task in taskList
    	int taskIndex = searchTask(taskId);
    	// If taskId exist, store task's address
    	if (taskIndex != -1) { 
    		Task task = taskList.get(taskIndex);
    		
    		// Validate and update task name if argument is not an empty string
    		if (name != "") {
    			task.setName(name);
    		}
    		// Validate and update task description if argument is not an empty string
    		if (description != "") {
    			task.setDescription(description);
    		}
    	} else {		// If can not find taskId
    		throw new IllegalArgumentException("Connot update task.");
    	}
    }
}
