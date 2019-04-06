package itemPopulation;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ToDoTableItems 
{

	private SimpleStringProperty timeDue;
	private SimpleStringProperty timeStarted;
	private SimpleStringProperty person;
	private SimpleStringProperty task;
	private SimpleStringProperty taskID;
	private SimpleStringProperty completed;
	private SimpleStringProperty taskDescription;



	public String getTimeDue() {
		return timeDue.get();
	}

	public String getTimeStarted() {
		return timeStarted.get();
	}

	public String getPerson() {
		return person.get();
	}

	public String getTask() {
		return task.get();
	}

	public String getTaskID() {
		return taskID.get();
	}

	public String getCompleted() {
		return completed.get();
	}

	public String getTaskDescription() {
		return taskDescription.get();
	}
	
	//Setters

	public void setTimeDue(SimpleStringProperty timeDue) {
		this.timeDue = timeDue;
	}

	public void setTimeStarted(SimpleStringProperty timeStarted) {
		this.timeStarted = timeStarted;
	}

	public void setPerson(SimpleStringProperty person) {
		this.person = person;
	}

	public void setTask(SimpleStringProperty task) {
		this.task = task;
	}

	public void setTaskID(SimpleStringProperty taskID) {
		this.taskID = taskID;
	}

	public void setCompleted(SimpleStringProperty completed) {
		this.completed = completed;
	}

	public void setTaskDescription(SimpleStringProperty taskDescription) {
		this.completed = taskDescription;
	}
	
	public ToDoTableItems()
	{
		this.timeDue = new SimpleStringProperty("-");
		this.timeStarted = new SimpleStringProperty("-");
		this.person = new SimpleStringProperty("-");
		this.task = new SimpleStringProperty("-");
		this.taskID = new SimpleStringProperty("-");
		this.completed = new SimpleStringProperty("-");
	}
	
	public ToDoTableItems(String timeDue, String timeStarted, String person, String task, String taskID, String completed, String taskDescription)
	{
		this.timeDue = new SimpleStringProperty(timeDue);
		this.timeStarted = new SimpleStringProperty(timeStarted);
		this.person = new SimpleStringProperty(person);
		this.task = new SimpleStringProperty(task);
		this.taskID = new SimpleStringProperty(taskID);
		this.completed = new SimpleStringProperty(completed);
		
		this.taskDescription = new SimpleStringProperty(taskDescription);
	}
}
