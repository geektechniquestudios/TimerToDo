package application;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTimePicker;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

public class EditController implements Initializable 
{
	@FXML BorderPane timeBorderPane;
	JFXTimePicker timePicker;
	JFXDatePicker datePicker;
	MainUIController mainController;
	
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
}
