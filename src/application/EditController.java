package application;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
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
	
	private String databaseAddress;
	private String username;
	private String password;
	
	private static String taskNumber;
	private Label descriptionLabel;
	
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
		Connection  someConnection = null;
		
		try
		{
			Class.forName("com.mysql.cj.jdbc.Driver");			
			someConnection = DriverManager.getConnection("jdbc:mysql:" + databaseAddress,username,password);			
			Statement queryToSend = someConnection.createStatement();
	
			queryToSend.executeUpdate
			(			
				"update list_items " 																		+
					"set time_due = \'" + datePicker.getValue() + " " + timePicker.getValue() + ":00\', "   +		
					"person  = \"" + editPerson.getText() + "\", " 											+
					"task = \"" + editTask.getText() + "\", " 												+
					"task_description = \"" + editDescription.getText() + "\" "								+
				"where task_id = " + taskNumber + ";"
			);
			
		
			//@todo: if no exception, success window popup, if catch, error window.
			datePicker.setValue(null);//clear fields
			timePicker.setValue(null);
			editPerson.setText(null);
			editTask.setText(null);
			
			//System.out.println(editDescription.getText());
			descriptionLabel.setText(editDescription.getText());
			
			editDescription.setText(null);
		}
		catch(SQLException e)
		{
			e.printStackTrace();			
		} 
		catch (ClassNotFoundException e) 
		{
			e.printStackTrace();
		}
		
		mainController.getSQLTable();
		mainController.highlightIndex();	
	}
	
	public void cancelHit()
	{
		mainController.mainListWasHit();
	}
	
	public void setMainUIController(MainUIController someController)
	{
		mainController = someController;
	}
	
	//setters
	public void setDescription(String someText)
	{
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
	
	public static void setTaskNum(String someNumber)
	{
		taskNumber = someNumber;
	}
	
	public void setDatabaseAddress(String newDatabaseAddress)
	{
		databaseAddress = newDatabaseAddress;
	}
	
	public void setUsername(String someUsername)
	{
		username = someUsername;
	}
	
	public void setPassword(String somePassword)
	{
		password = somePassword;
	}
	
	public void setDescriptionLabel(Label someLabel)
	{
		descriptionLabel = someLabel;
	}
}
