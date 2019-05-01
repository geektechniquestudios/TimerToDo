package application;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;


public class NewTaskController implements Initializable 
{
	@FXML private HBox dateBox;
	@FXML private HBox timeBox;
	@FXML private JFXTextField taskField;
	@FXML private JFXTextField personField;
	@FXML private JFXTextArea descriptionField;
	
	private static String databaseAddress;
	private static String username;
	private static String password;
	
	private MainUIController mainController;
	private JFXDatePicker datePicker;
	private JFXTimePicker timePicker;
	
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
	
	@FXML private void submitWasHit()
	{
		Connection someConnection = null;
		
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
			
			mainController.getSQLTable();//updateTable();//then make the main table update
			datePicker.setValue(null);//clear fields
			timePicker.setValue(null);
			personField.setText(null);
			taskField.setText(null);
			descriptionField.setText(null);
		}
		catch(SQLException e)
		{
			e.printStackTrace();
			mainController.popup("ConnectionFailedPopup.fxml");
		} 
		catch (ClassNotFoundException e) 
		{
			e.printStackTrace();
			mainController.popup("ConnectionFailedPopup.fxml");
		}	
	}

	public static void setDatabaseAddress(String newDatabaseAddress)
	{
		databaseAddress = newDatabaseAddress;
	}
	
	public static void setUsername(String someUsername)
	{
		username = someUsername;
	}
	
	public static void setPassword(String somePassword)
	{
		password = somePassword;
	}
	
	public void setMainUI(MainUIController someMainUI)
	{
		mainController = someMainUI;
	}
}
