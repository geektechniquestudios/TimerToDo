package application;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

public class SettingsController implements Initializable
{
	@FXML private JFXTextField addressField;
	@FXML private JFXTextField nameField;
	@FXML private JFXPasswordField pwField;
	
	private MainUIController mainController;
	
	@FXML private void submitWasHit()
	{
			MainUIController.setDatabaseAddress(addressField.getText());
			MainUIController.setUsername(nameField.getText());
			MainUIController.setPassword(pwField.getText());
			
			NewTaskController.setDatabaseAddress(addressField.getText());
			NewTaskController.setUsername(nameField.getText());
			NewTaskController.setPassword(pwField.getText());
			
			mainController.getSQLTable();
	}
	
	public void setMainUIController(MainUIController someUIController)
	{
		mainController = someUIController;
	}
	
	EventHandler<ActionEvent> addResourceHandler = event -> 
	{
	    submitWasHit();
	};

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) 
	{
		pwField.setOnAction(addResourceHandler);
		nameField.setOnAction(addResourceHandler);
		addressField.setOnAction(addResourceHandler);
	}
}
