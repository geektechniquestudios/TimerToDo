package application;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class FailedConnectionController implements Initializable
{
	private Stage thisStage;
	@FXML private BorderPane mainBorder;

	public void retryWasHit()
	{
		thisStage.close();
	}
	
	public void exitWasHit()
	{
		thisStage.close();
		System.exit(0);//Platform.exit(); didn't work, this throws fatal error. Def a better way
	}

	public void setStage(Stage someStage)
	{
		thisStage = someStage;
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) 
	{
		Platform.runLater(() ->
		{
			mainBorder.requestFocus();

		});
	}
	
}
