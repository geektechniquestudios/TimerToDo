package application;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.stage.Stage;

public class SuccessPopupController 
{
	Stage thisStage;
	
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
