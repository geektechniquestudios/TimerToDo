package application;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

public class EditController implements Initializable 
{
	@FXML private BorderPane timeBorderPane;
	@FXML private JFXTextArea editDescription;
	@FXML private JFXTextField editPerson;
	@FXML private JFXTextField editTask;
	
	private JFXTimePicker timePicker;
	private JFXDatePicker datePicker;
	private MainUIController mainController;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) 
	{
		timePicker = new JFXTimePicker();
		timePicker.setDefaultColor(Color.web("#bc7a00"));
		timeBorderPane.setRight(timePicker);
		
		datePicker = new JFXDatePicker();
		datePicker.setDefaultColor(Color.web("#bc7a00"));
		timeBorderPane.setLeft(datePicker);		
	}
	
	public void submitHit()
	{
		//MainUIController.mainListWasHit();
	}
	
	public void cancelHit()
	{
		mainController.mainListWasHit();
	}
	
	public void setMainUIController(MainUIController someController)
	{
		mainController = someController;
	}
	
	
	
	//setters kinda
	
	public void setDescription(String someText)
	{
		System.out.println("method fires");
		editDescription.setText(someText);
	}
	
	public void setPerson(String someText)
	{
		editPerson.setText(someText);
	}
	
	public void setTask(String someText)
	{
		editTask.setText(someText);
	}
	
	public void setTimePicker(LocalTime someTime)
	{
		timePicker.setValue(someTime);
	}
	
	public void setDatePicker(LocalDate someDate)
	{
		datePicker.setValue(someDate);
	}
	
}
