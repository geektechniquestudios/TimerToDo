package application;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTimePicker;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

public class NewTaskController implements Initializable 
{
	@FXML HBox dateBox;
	@FXML HBox timeBox;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) 
	{
		JFXDatePicker datePicker = new JFXDatePicker();
		datePicker.setDefaultColor(Color.web("#b8c9af"));
		datePicker.setTranslateX(4);
		dateBox.getChildren().add(datePicker);
		
		JFXTimePicker timePicker = new JFXTimePicker();
		timePicker.setDefaultColor(Color.web("#b8c9af"));
		timePicker.setTranslateX(4);
		timeBox.getChildren().add(timePicker);
	
	}
	
}
