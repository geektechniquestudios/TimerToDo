package application;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import javafx.fxml.FXML;

public class SettingsController 
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
			//mainController.popup("SuccessPopup.fxml");//connection established popup

			
			//popup telling them connection settings have been updated
			
			//add listener for enter press
	}
	
	public void setMainUIController(MainUIController someUIController)
	{
		mainController = someUIController;
	}
}
