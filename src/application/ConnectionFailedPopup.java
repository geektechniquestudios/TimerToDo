package application;

import javafx.fxml.FXML;
import javafx.stage.Stage;

public class ConnectionFailedPopup 
{
	private Stage thisStage;
	
	@FXML private void continueWasHit()
	{
		thisStage.close();
	}
	
	public void setPopupStage(Stage someStage)
	{
		thisStage = someStage;
	}
}
