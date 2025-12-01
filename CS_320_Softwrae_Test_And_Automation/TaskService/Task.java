package TaskService;

public class Task {
	
	private String name;
	private String description;
	private String taskId;
	private final int nameLength = 20;
	private final int descriptionLenghth = 50;
	private final int taskIdLength = 10;
	
	// Constructor
	public Task(String name, String description, String taskId) {
		// Validate name is valid
		if(name == null || name.length() > nameLength || name.length() < 1) {
			// Catch requirements -> null, and length.
			throw new IllegalArgumentException("Invalid name input");
		}
		// Validate description is valid
		if(description == null || description.length() > descriptionLenghth || description.length() < 1) {
			// Catch requirements -> null, and length.
			throw new IllegalArgumentException("Invalid description input");
		}
		// Validate taskId is valid
		if(taskId == null || taskId.length() > taskIdLength || taskId.length() < 1) {
			// Catch requirements -> null, and length.
			throw new IllegalArgumentException("Invalid taskId input");
		}
		// Set variables
		this.name = name;
		this.description = description;
		this.taskId = taskId;
	}
	
	// Return name method
	public void setName(String name) {
		// Validate name is valid
		if(name == null || name.length() > nameLength || name.length() < 1) {
			// Catch requirements of null, and length.
			throw new IllegalArgumentException("Invalid name input");
		}
		this.name = name;
	}
	
	// Return name method
	public String getName() {
		return name;
	}
	
	// Return description method
	public void setDescription(String description) {
		// Validate description is valid
		if(description == null || description.length() > descriptionLenghth || description.length() < 1) {
			// Catch requirements of null, and length.
			throw new IllegalArgumentException("Invalid description input");
		}
		this.description = description;
	}
	
	// Return description method
	public String getDescription() {
		return description;
	}
	// Return TaskId method
	public String getTaskId() {
		return taskId;
	}
}
