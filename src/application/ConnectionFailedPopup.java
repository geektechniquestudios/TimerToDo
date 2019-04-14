package application;

import javafx.stage.Stage;

public class ConnectionFailedPopup 
{
	private Stage thisStage;
	
	public void continueWasHit()
	{
		thisStage.close();
	}
	
	public void setPopupStage(Stage someStage)
	{
		thisStage = someStage;
	}
}
