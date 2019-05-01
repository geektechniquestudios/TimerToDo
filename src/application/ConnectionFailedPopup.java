package application;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.stage.Stage;

public class ConnectionFailedPopup 
{
	private Stage thisStage;
	
	@FXML private JFXButton continueButton;
	
	@FXML private void continueWasHit()
	{
		thisStage.close();
	}
	
	public void setPopupStage(Stage someStage)
	{
		thisStage = someStage;
	}
	
	EventHandler<ActionEvent> addResourceHandler = event -> 
	{
	    continueWasHit();
	};
}
