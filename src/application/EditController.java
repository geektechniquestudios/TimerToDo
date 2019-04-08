package application;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTimePicker;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;

public class EditController implements Initializable 
{
	@FXML BorderPane timeBorderPane;
	JFXTimePicker timePicker;
	JFXDatePicker datePicker;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) 
	{
		timePicker = new JFXTimePicker();
		timeBorderPane.setRight(timePicker);
		
		datePicker = new JFXDatePicker();
		timeBorderPane.setLeft(datePicker);		
	}
	
	public void submitHit()
	{
		//MainUIController.mainListWasHit();
	}
	
	public void cancelHit()
	{
		
	}
}
