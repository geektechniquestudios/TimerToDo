package application;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;

import itemPopulation.ToDoTableItems;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

public class NewTaskController implements Initializable 
{
	@FXML private HBox dateBox;
	@FXML private HBox timeBox;
	@FXML private JFXTextField taskField;
	@FXML private JFXTextField personField;
	@FXML private JFXTextArea descriptionField;
	
	
	
	private JFXDatePicker datePicker;
	private JFXTimePicker timePicker;
	
	private String databaseAddress;
	private String username;
	private String password;
	
	private String completeString;
	private TableView<ToDoTableItems> toDoTable;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) 
	{
		datePicker = new JFXDatePicker();
		datePicker.setDefaultColor(Color.web("#b8c9af"));
		datePicker.setTranslateX(4);
		dateBox.getChildren().add(datePicker);
		
		timePicker = new JFXTimePicker();
		timePicker.setDefaultColor(Color.web("#b8c9af"));
		timePicker.setTranslateX(4);
		timeBox.getChildren().add(timePicker);
	
	}
	
	public void submitWasHit()
	{
		System.out.println(datePicker.getValue() + " " + timePicker.getValue());
		System.out.println
		(	
				"insert into list_items" 															+
						"("																			+
							"time_due," 															+
							"person," 																+
							"task," 																+
							"task_description,"														+
							"completed"																+
						") " 																		+
						"values"																	+ 
						"("																			+
							"\'" + datePicker.getValue() + " " + timePicker.getValue() + ":00\'," 	+				
							"\"" + personField.getText() + "\","									+				
							"\"" + taskField.getText() + "\"," 										+	
							"\"" + descriptionField.getText() + "\","								+				
							false																	+					
						");"
				
		);
		
		Connection someConnection = null;
		//ObservableList<ToDoTableItems> itemsToReturn = FXCollections.observableArrayList();
		
		try
		{
			Class.forName("com.mysql.cj.jdbc.Driver");			
			someConnection = DriverManager.getConnection("jdbc:mysql:" + databaseAddress,username,password);			
			Statement queryToSend = someConnection.createStatement();
	
			queryToSend.executeUpdate
			(			
				"insert into list_items" 													+
					"("																		+
					"time_due," 															+
					"person," 																+
					"task," 																+
					"task_description,"														+
					"completed"																+
				") " 																		+
				"values"																	+ 
				"("																			+
					"\'" + datePicker.getValue() + " " + timePicker.getValue() + ":00\'," 	+				
					"\"" + personField.getText() + "\","									+				
					"\"" + taskField.getText() + "\"," 										+	
					"\"" + descriptionField.getText() + "\","								+				
					false																	+					
				");"
			);
			
			//@todo: if no exception, success window popup, if catch, error window.
			updateTable();//then make the main table update
			datePicker.setValue(null);//clear fields
			timePicker.setValue(null);
			personField.setText(null);
			taskField.setText(null);
			descriptionField.setText(null);
			
				
		}
		catch(SQLException e)
		{
			System.out.println("didn't work");
			e.printStackTrace();			
		} 
		catch (ClassNotFoundException e) {
			System.out.println("didn't work 2");
			e.printStackTrace();
		}	
	}
	
	private void updateTable()
	{
		Connection someConnection = null;
		ObservableList<ToDoTableItems> itemsToReturn = FXCollections.observableArrayList();
		
		try
		{
			Class.forName("com.mysql.cj.jdbc.Driver");			
			someConnection = DriverManager.getConnection("jdbc:mysql:" + databaseAddress,username,password);			
			Statement queryToSend = someConnection.createStatement();
	
			ResultSet returnStatement = queryToSend.executeQuery("SELECT * FROM list_items");
			
			while(returnStatement.next())
			{
				System.out.println(returnStatement.getInt("completed"));
				if(returnStatement.getInt("completed") == 1)
				{
					completeString = "completed"; 
				}
				else
				{
					completeString = "incomplete";
				}
				itemsToReturn.add(new ToDoTableItems
				(
					returnStatement.getString("time_due"),
					returnStatement.getString("time_started"),
					returnStatement.getString("person"),
					returnStatement.getString("task"),
					returnStatement.getString("task_id"),
					completeString,
					
					returnStatement.getString("task_description")
				));			
			}
			
			toDoTable.setItems(itemsToReturn);
				
		}
		catch(SQLException e)
		{
			System.out.println("didn't work");
			e.printStackTrace();
			
		}	
		catch(ClassNotFoundException e)
		{
			System.out.println("didn't work 2");
			e.printStackTrace();
		}
		catch(Exception e)
		{
			System.out.println("didn't work 3");
			e.printStackTrace();
		}
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
	
	public void setToDoTable(TableView<ToDoTableItems> someTable)
	{
		toDoTable = someTable;
	}
}
